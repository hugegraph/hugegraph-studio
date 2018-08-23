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

package com.baidu.hugegraph.studio.board.model;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.baidu.hugegraph.structure.graph.Edge;
import com.baidu.hugegraph.structure.graph.Vertex;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The result entity for jersey restful api, and will be return as json.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResult {

    @JsonProperty("id")
    private String id;

    @JsonProperty("data")
    private List<Object> data;

    @JsonProperty("type")
    private QueryResult.Type type;

    @JsonProperty("duration")
    private Long duration = null;

    @JsonProperty("showNum")
    private int showNum = 0;

    @JsonProperty("message")
    private String message;

    @JsonProperty("graph")
    private Graph graph;

    /**
     * Instantiates a new Result.
     */
    public QueryResult() {
        this.id = UUID.randomUUID().toString();
        this.graph = new Graph();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public Long getDuration() {
        return this.duration;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public List<Object> getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(List<Object> data) {
        this.data = data;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getShowNum() {
        return showNum;
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets graph. when result type is VERTEX / EDGE / PATH,web page can draw
     * network graphic with javascript.
     *
     * To return vertices and edges list to meet the needs of network graphic
     * drawing.
     *
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Sets graph vertices.
     *
     * @param vertices the vertices
     */
    public void setGraphVertices(List<Vertex> vertices) {

        this.graph.setVertices(vertices);
    }

    /**
     * Sets graph edges.
     *
     * @param edges the edges
     */
    public void setGraphEdges(List<Edge> edges) {

        this.graph.setEdges(edges);
    }


    public void setStyles(Map<String, Object> styles) {
        this.graph.setStyles(styles);
    }

    /**
     * The type of gremlin result:
     *
     * g.V() -> VERTEX
     * g.E() -> EDGE
     * g.V().count() -> NUMBER
     * g.V().outE().path() -> PATH
     */
    public enum Type {
        /**
         * Vertex type.
         */
        VERTEX,

        /**
         * Edge type.
         */
        EDGE,

        /**
         * Path type.
         */
        PATH,

        /**
         * Empty type.
         */
        EMPTY,

        /**
         * Number type.
         */
        NUMBER,

        /**
         * Single type including number,string,boolean.
         */
        SINGLE,

        /**
         * IF TYPE NOT IN THE ABOVE, SET TO OTHER
         */
        OTHER
    }

    /**
     * The Graph class is used for contain Vertices & edges.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Graph {

        @JsonProperty
        private List<Vertex> vertices;

        @JsonProperty
        private List<Edge> edges;

        @JsonProperty
        private Map<String, Object> styles;

        /**
         * Instantiates a new Graph.
         */
        public Graph() {

        }

        /**
         * Gets vertices.
         *
         * @return the vertices
         */
        public List<Vertex> getVertices() {
            return vertices;
        }

        /**
         * Sets vertices.
         *
         * @param vertices the vertices
         */
        public void setVertices(List<Vertex> vertices) {
            if (vertices == null) {
                this.vertices = vertices;
            } else {
                // Distinct using map
                this.vertices = vertices.stream()
                                        .collect(toMap(Vertex::id, v -> v,
                                                (v1, v2) -> v1)).values()
                                        .stream().collect(Collectors.toList());
            }
        }

        /**
         * Gets edges.
         *
         * @return the edges
         */
        public List<Edge> getEdges() {
            return edges;
        }

        /**
         * Sets edges.
         * distinct using map
         *
         * @param edges the edges
         */
        public void setEdges(List<Edge> edges) {
            if (edges == null) {
                this.edges = edges;
            } else {
                this.edges = edges.stream()
                        .collect(toMap(Edge::id, e -> e, (e1, e2) -> e1))
                        .values().stream()
                        .collect(Collectors.toList());

            }
        }

        public Map<String, Object> getStyles() {
            return styles;
        }

        public void setStyles(Map<String, Object> styles) {
            this.styles = styles;
        }
    }
}
