/*
 * Copyright 2017 HugeGraph Authors
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.baidu.hugegraph.studio.board.service;

import static com.baidu.hugegraph.studio.board.model.QueryResult.Type;
import static com.baidu.hugegraph.studio.board.model.QueryResult.Type.EDGE;
import static com.baidu.hugegraph.studio.board.model.QueryResult.Type.EMPTY;
import static com.baidu.hugegraph.studio.board.model.QueryResult.Type.OTHER;
import static com.baidu.hugegraph.studio.board.model.QueryResult.Type.PATH;
import static com.baidu.hugegraph.studio.board.model.QueryResult.Type.SINGLE;
import static com.baidu.hugegraph.studio.board.model.QueryResult.Type.VERTEX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.hugegraph.driver.GremlinManager;
import com.baidu.hugegraph.driver.HugeClient;
import com.baidu.hugegraph.driver.SchemaManager;
import com.baidu.hugegraph.structure.graph.Edge;
import com.baidu.hugegraph.structure.graph.Vertex;
import com.baidu.hugegraph.structure.gremlin.Result;
import com.baidu.hugegraph.structure.gremlin.ResultSet;
import com.baidu.hugegraph.structure.schema.VertexLabel;
import com.baidu.hugegraph.studio.board.model.Board;
import com.baidu.hugegraph.studio.board.model.Card;
import com.baidu.hugegraph.studio.board.model.QueryResult;
import com.baidu.hugegraph.studio.board.model.vis.EdgeColor;
import com.baidu.hugegraph.studio.board.model.vis.Font;
import com.baidu.hugegraph.studio.board.model.vis.VisNode;
import com.baidu.hugegraph.studio.board.serializer.BoardSerializer;
import com.baidu.hugegraph.studio.config.NodeColorOption;
import com.baidu.hugegraph.studio.config.StudioApiConfig;
import com.baidu.hugegraph.studio.gremlin.GremlinOptimizer;
import com.baidu.hugegraph.util.Log;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

/**
 * Board service for Jersey Restful Api
 */
@Path("board")
public class BoardService {

    private static final Logger LOG = Log.logger(BoardService.class);
    private static final int GREMLIN_MAX_IDS = 250;

    // The max number of one vertex related edge.
    // Vis can deal with about 200 edges.
    private static final int MAX_EDGES_PER_VERTEX = 200;
    private static final StudioApiConfig conf = StudioApiConfig.getInstance();

    @Autowired
    private BoardSerializer boardSerializer;
    
    @Autowired
    private GremlinOptimizer gremlinOptimizer;

    private HugeClient newHugeClient() {
        return new HugeClient(conf.getGraphServerUrl(), conf.getGraphName(),
                              conf.getClientTimeout());
    }

    /**
     * To execute the code (gremlin) in the card of page.
     *
     * Execute the gremlin code via HugeClient.
     * Gremlin result will be stored in two places, the original data is saved
     * as a List<Object>, another is translated into a graph or a table object
     * if possible.
     *
     * @param card The card value of the current card.
     * @return The whole graph with json(Vertices & Eges).
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response executeGremlin(Card card) {
        Preconditions.checkArgument(card != null);

        HugeClient client = null;
        try {
            client = newHugeClient();
        } catch (Exception e) {
            QueryResult result = new QueryResult();
            result.setMessage("Failed to connect HugeGraphServer");
            return Response.status(500).entity(result).build();
        }

        try {
            QueryResult queryResult = this.executeQuery(card, client);

            Board board = new Board();
            board.setCard(card);
            board.setResult(queryResult);
            boardSerializer.save(board);

            return Response.status(200).entity(queryResult).build();
        } catch (Exception e) {
            QueryResult result = new QueryResult();
            result.setMessage(e.getMessage());
            return Response.status(500).entity(result).build();
        }
    }

    private QueryResult executeQuery(Card card, HugeClient client) {
        long startTime = System.currentTimeMillis();

        GremlinManager gremlinManager = client.gremlin();

        int limit = conf.getLimitData();
        // To know whether has more record,
        // so add "limit(limit+1)" after code.
        String limitCode = gremlinOptimizer.limitOptimize(card.getCode(),
                                                          limit + 1);
        LOG.info(limitCode);

        // Execute gremlin by HugeClient.
        ResultSet resultSet = gremlinManager.gremlin(limitCode).execute();

        QueryResult queryResult = new QueryResult();
        /*
         * Gremlin result will be stored in two places, the original
         * data is saved as a List<Object>, another is translated
         * into a graph or a table object if possible.
         */
        queryResult.setData(resultSet.data());

        List<Vertex> vertices = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Map<String, Object> styles = new HashMap<>();
        List<com.baidu.hugegraph.structure.graph.Path> paths = new ArrayList<>();
        if (!resultSet.iterator().hasNext()) {
            queryResult.setType(EMPTY);
        }

        queryResult.setType(getResultType(resultSet, limit));
        int count = 0;

        for (Iterator<Result> results = resultSet.iterator();
             results.hasNext();) {

            /*
             * The result might be null, and the object must be got via
             * Result.getObject method.
             */
            Result or = results.next();
            if (or == null) {
                continue;
            }
            Object object = or.getObject();
            if (object instanceof Vertex) {
                vertices.add((Vertex) object);
            } else if (object instanceof Edge) {
                edges.add((Edge) object);
            } else if (object instanceof
                       com.baidu.hugegraph.structure.graph.Path) {
                // Convert Object to Path
                paths.add((com.baidu.hugegraph.structure.graph.Path) object);
            }
            if (++count >= limit) {
                break;
            }
        }

        /*
         * When the results contains not only vertices\edges\paths,
         * how to deal with that?
         */
        switch (queryResult.getType()) {
            case PATH:
                // Extract vertices from paths ;
                vertices = getVertexFromPath(client, paths);
                edges = getEdgeFromVertex(client, vertices);
                styles = getGraphStyles(client);
                break;
            case VERTEX:
                // Extract edges from vertex ;
                edges = getEdgeFromVertex(client, vertices);
                styles = getGraphStyles(client);
                break;
            case EDGE:
                // Extract vertices from edges ;
                vertices = getVertexFromEdge(client, edges);
                styles = getGraphStyles(client);
                break;
            default:
                break;
        }

        queryResult.setGraphVertices(vertices);
        queryResult.setGraphEdges(edges);
        queryResult.setStyles(styles);
        queryResult.setShowNum(count);
        String message = "";
        if (count < resultSet.size()) {
            message = String.format("Partial %s records are shown!", count);
        }
        queryResult.setMessage(message);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        queryResult.setDuration(duration);

        return queryResult;
    }

    /**
     * The method is used for get a vertex's adjacency nodes when a graph is
     * shown. The user can select any vertex which is interested in as a start
     * point, add it's adjacency vertices & edges to current graph by executing
     * the gremlin statement of 'g.V(id).bothE()'.
     *
     * After successful of the gremlin need to merge the current local query
     * results to the page card's result.
     *
     * Note: this method should be executed after @see executeGremlin
     * (String, String) has been executed.
     *
     * @param vertexId The id of vertex as a start point.
     * @return The offset graph(vertices & edges).
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adjacentVertices(
           @QueryParam("vertexId") String vertexId,
           @QueryParam("label") String label) {
        Preconditions.checkArgument(vertexId != null);
        Preconditions.checkArgument(StringUtils.isNotBlank(label),
                                    "parameter label is blank");

        HugeClient client = null;
        try {
            client = newHugeClient();
        } catch (Exception e) {
            QueryResult result = new QueryResult();
            result.setMessage("Failed to connect HugeGraphServer");
            return Response.status(500).entity(result).build();
        }

        try {
            Board board = boardSerializer.load();
            Preconditions.checkArgument(board != null);

            QueryResult resultNew = this.queryAdjacentVertices(board, client,
                                                               vertexId, label);
            boardSerializer.save(board);

            return Response.status(200).entity(resultNew).build();
        } catch (Exception e) {
            QueryResult result = new QueryResult();
            result.setMessage(e.getMessage());
            return Response.status(500).entity(result).build();
        }
    }

    private QueryResult queryAdjacentVertices(Board board, HugeClient client,
                                              String vertexId, String label) {
        long startTime = System.currentTimeMillis();

        QueryResult queryResult = board.getResult();
        /*
         * This method should be executed after the method of @see
         * executeGremlin(String,String). It must has a start
         * point and the result must have vertices or edges.
         */
        Preconditions.checkArgument(queryResult != null &&
                                    queryResult.getGraph() != null);

        SchemaManager schema = client.schema();
        VertexLabel vertexLabel = schema.getVertexLabel(label);

        Object transformedVertexId = transformId(vertexId, vertexLabel);

        // To know whether has more record,
        // so add "limit(limit+1)" after code.
        int limit = MAX_EDGES_PER_VERTEX + 1;
        String code = gremlinOptimizer.limitOptimize(
                      String.format("g.V(%s).bothE()",
                      formatId(transformedVertexId)), limit);
        LOG.info(code);
        Set<Object> vertexIds = new HashSet<>();
        Set<String> visitedEdgeIds = new HashSet<>();
        List<Vertex> vertices = queryResult.getGraph().getVertices();
        List<Edge> edges = queryResult.getGraph().getEdges();
        vertices.forEach(v -> vertexIds.add(v.id()));
        edges.forEach(e -> {
            if (e.source().equals(transformedVertexId) ||
                e.target().equals(transformedVertexId)) {
                visitedEdgeIds.add(e.id());
            }
        });

        Preconditions.checkArgument(vertexIds.contains(transformedVertexId));

        GremlinManager gremlinManager = client.gremlin();
        ResultSet resultSet = gremlinManager.gremlin(code).execute();
        queryResult.setData(resultSet.data());

        Iterator<Result> iterator = resultSet.iterator();

        List<Edge> edgesNew = new ArrayList<>();
        List<Vertex> verticesNew = new ArrayList<>();

        String message = "";
        while (iterator.hasNext()) {
            Edge e = iterator.next().getEdge();
            if (visitedEdgeIds.contains(e.id())) {
                continue;
            }
            if (edgesNew.size() > conf.getLimitEdgeIncrement()) {
                message = String.format("%s edges increase, but more edges" +
                                        "aren't shown.", edgesNew.size());
                break;
            }
            if (visitedEdgeIds.size() > MAX_EDGES_PER_VERTEX) {
                message = String.format("There are more than %s edges and " +
                                        "not all are shown!",
                                        MAX_EDGES_PER_VERTEX);
                break;
            }
            if (edges.size() + edgesNew.size() > conf.getLimitEdgeTotal()) {
                message = String.format("There are more than %s edges and " +
                                        "not all are shown!",
                                        conf.getLimitEdgeTotal());
                break;
            }
            visitedEdgeIds.add(e.id());
            edgesNew.add(e);
        }

        List<Vertex> verticesFromEdges = getVertexFromEdge(client, edgesNew);
        if (verticesFromEdges != null) {
            verticesFromEdges.forEach(v -> {
                if (!vertexIds.contains(v.id())) {
                    vertexIds.add(v.id());
                    verticesNew.add(v);
                }
            });
        }

        // Save the current query result to card.
        vertices.addAll(verticesNew);
        edges.addAll(edgesNew);

        QueryResult resultNew = new QueryResult();
        queryResult.setGraphVertices(vertices);
        queryResult.setGraphEdges(edges);
        queryResult.setStyles(resultNew.getGraph().getStyles());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        resultNew.setType(EDGE);
        resultNew.setGraphVertices(verticesNew);
        resultNew.setGraphEdges(edgesNew);
        resultNew.setStyles(getGraphStyles(client));
        resultNew.setDuration(duration);
        resultNew.setMessage(message);

        return resultNew;
    }

    private Type getResultType(ResultSet resultSet, int limit) {
        int i = 0;
        Type type = EMPTY;
        for (Iterator<Result> results = resultSet.iterator();
             results.hasNext(); ) {

            Result or = results.next();
            Object object = or.getObject();
            if (object instanceof Vertex) {
                type = VERTEX;
            } else if (object instanceof Edge) {
                type = EDGE;
            } else if (object instanceof
                    com.baidu.hugegraph.structure.graph.Path) {
                type = PATH;
            } else if (object instanceof Number ||
                    object instanceof String ||
                    object instanceof Boolean) {
                if (type == EMPTY) {
                    type = SINGLE;
                }
            } else {
                type = OTHER;
            }
            if (++i >= limit) {
                break;
            }
        }
        return type;
    }

    private Object transformId(String vertexId, VertexLabel vertexLabel) {
        Object transformedVertexId = vertexId;
        switch (vertexLabel.idStrategy()) {
            case AUTOMATIC:
            case CUSTOMIZE_NUMBER:
                try {
                    transformedVertexId = Integer.valueOf(vertexId);
                } catch (NumberFormatException ignored) {
                    try {
                        transformedVertexId = Long.valueOf(vertexId);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                                  "The vertexId does no match with itself " +
                                  "idStrategy");
                    }
                }
                break;
            case PRIMARY_KEY:
            case CUSTOMIZE_STRING:
                break;
            default:
                throw new IllegalArgumentException(String.format(
                          "The vertexLabel isStrategy %s is not supported",
                          vertexLabel.idStrategy().name()));
        }
        return transformedVertexId;
    }

    private String formatId(Object id) {
        if (id instanceof String) {
            String transformedId =
                    StringUtils.replace(id.toString(), "\\", "\\\\");
            transformedId = StringUtils.replace(transformedId, "\"", "\\\"");
            transformedId = StringUtils.replace(transformedId, "'", "\\'");
            transformedId = StringUtils.replace(transformedId, "\n", "\\n");
            return String.format("'%s'", transformedId);
        }
        return id.toString();
    }

    private Map<String, Object> getGraphStyles(HugeClient client) {
        Map<String, VisNode> groups = new HashMap<>();
        NodeColorOption colorOption =
                new NodeColorOption(StudioApiConfig.getInstance()
                                                   .getVertexVisColor());
        List<VertexLabel> vertexLabels = client.schema().getVertexLabels();
        Collections.sort(vertexLabels, new Comparator<VertexLabel>() {
            @Override
            public int compare(VertexLabel o1, VertexLabel o2) {
                return o1.name().compareTo(o2.name());
            }
        });

        for (VertexLabel vertexLabel : vertexLabels) {
            if (vertexLabel.userdata() != null &&
                !vertexLabel.userdata().isEmpty()) {
                groups.put(vertexLabel.name(),
                           new VisNode(vertexLabel.userdata(), colorOption));
            } else {
                groups.put(vertexLabel.name(), new VisNode(colorOption));
            }
        }

        Font vertexFont = new Font.Builder().size(conf.getVertexFontSize())
                                            .color(conf.getVertexFontColor())
                                            .build();
        Font edgeFont = new Font.Builder().size(conf.getEdgeFontSize())
                                          .color(conf.getEdgeFontColor())
                                          .build();
        return ImmutableMap.of(
                "groups", groups,
                "font", vertexFont,
                "edgeColor", new EdgeColor.Builder(conf).build(),
                "edgeFont", edgeFont
        );
    }

    private List<Vertex> getVertexFromEdge(HugeClient client,
                                           List<Edge> edges) {
        if (edges == null || edges.size() == 0) {
            return null;
        }
        Set<Object> vertexIds = new HashSet<>();
        edges.forEach(e -> {
            vertexIds.add(e.source());
            vertexIds.add(e.target());
        });
        return getVertices(client, new ArrayList<>(vertexIds));

    }

    private List<Edge> getEdgeFromVertex(HugeClient client,
                                         List<Vertex> vertices) {

        if (vertices == null || vertices.size() == 0) {
            return null;
        }
        List<Edge> edges = new ArrayList<>();

        Set<Object> vertexIds = new HashSet<>();
        vertices.forEach(v -> vertexIds.add(v.id()));

        List<String> idList = new ArrayList<>();
        for (Vertex vertex : vertices) {
            idList.add(formatId(vertex.id()));
        }

        Lists.partition(idList, GREMLIN_MAX_IDS)
                .forEach(group -> {
                    String ids = StringUtils.join(group, ",");
                    /*
                     * De-duplication by edgeId. Reserve the edges only if both
                     * srcVertexId and tgtVertexId is a member of vertices.
                     */
                    String code = String.format("g.V(%s).bothE().dedup()" +
                                                ".limit(800000)", ids);
                    LOG.info(code);
                    ResultSet resultSet =
                            client.gremlin().gremlin(code).execute();

                    Iterator<Result> resultIterator = resultSet.iterator();

                    Map<Object, Integer> edgesNumPerVertex = new HashMap<>();

                    while (resultIterator.hasNext()) {
                        Edge edge = resultIterator.next().getEdge();
                        /*
                         * As the results is queried by 'g.V(id).bothE()', the
                         * source vertex of edge from results is in the set of
                         * vertexIds. Hence, just reserve the edge which that
                         * the target in the set of vertexIds.
                         */
                        Object target = edge.target();
                        Object source = edge.source();
                        if (vertexIds.contains(target) &&
                            vertexIds.contains(source)) {
                            Integer count = edgesNumPerVertex.get(source);
                            if (count == null) {
                                count = 0;
                            }
                            edgesNumPerVertex.put(source, count++);
                            if (count > MAX_EDGES_PER_VERTEX) {
                                break;
                            }

                            count = edgesNumPerVertex.get(target);
                            if (count == null) {
                                count = 0;
                            }
                            edgesNumPerVertex.put(target, count++);
                            if (count > MAX_EDGES_PER_VERTEX) {
                                break;
                            }

                            edges.add(edge);
                            if (edges.size() >= conf.getLimitEdgeTotal()) {
                                break;
                            }
                        }
                    }
                });
        return edges;
    }

    private List<Vertex> getVertices(HugeClient client,
                                     List<Object> vertexIds) {
        if (vertexIds == null || vertexIds.size() == 0) {
            return null;
        }
        List<Vertex> vertices = new ArrayList<>();

        List<String> idList = new ArrayList<>();
        for (Object vertexId : vertexIds) {
            idList.add(formatId(vertexId));
        }
        Lists.partition(idList, GREMLIN_MAX_IDS)
                .forEach(group -> {
                    String ids = StringUtils.join(group, ",");
                    String gremlin = String.format("g.V(%s)", ids);
                    LOG.info(gremlin);
                    ResultSet resultSet =
                            client.gremlin().gremlin(gremlin).execute();
                    Iterator<Result> results = resultSet.iterator();
                    List<Vertex> finalVertices = vertices;
                    results.forEachRemaining(
                            vertex -> finalVertices.add((Vertex) vertex.getObject()));
                });
        return vertices;
    }

    private List<Vertex> getVertexFromPath(HugeClient client,
                                           List<com.baidu.hugegraph.structure
                                           .graph.Path> paths) {
        if (paths == null) {
            return null;
        }

        Set<Object> vertexIds = new HashSet<>();
        // The path node can be a Vertex, or an Edge.
        paths.forEach(path -> path.objects().forEach(obj -> {
            if (obj instanceof Vertex) {
                Vertex vertex = (Vertex) obj;
                vertexIds.add(vertex.id());
            } else if (obj instanceof Edge) {
                Edge edge = (Edge) obj;
                vertexIds.add(edge.source());
                vertexIds.add(edge.target());
            }
        }));
        return getVertices(client, new ArrayList<>(vertexIds));
    }
}
