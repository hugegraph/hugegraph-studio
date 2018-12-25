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

package com.baidu.hugegraph.studio.config;

import com.baidu.hugegraph.config.HugeConfig;
import com.baidu.hugegraph.config.OptionSpace;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudioApiConfig {

    private static final String DEFAULT_CONFIGURATION_FILE =
            "hugegraph-studio.properties";
    private final ObjectMapper mapper = new ObjectMapper();

    private HugeConfig config;

    static {
        OptionSpace.register("studio-api", StudioApiOptions.instance());
    }

    private StudioApiConfig() {
        String studioHome = System.getProperty("studio.home");
        if (studioHome != null) {
            config = new HugeConfig(String.format("%s/conf/%s",
                                    studioHome, DEFAULT_CONFIGURATION_FILE));
        } else {
            URL confUrl = this.getClass()
                              .getClassLoader()
                              .getResource(DEFAULT_CONFIGURATION_FILE);
            Preconditions.checkNotNull(confUrl);
            config = new HugeConfig(confUrl.getFile());
        }
    }

    private static class StudioConfigurationHolder {
        private static final StudioApiConfig conf =
                new StudioApiConfig();
    }

    public static StudioApiConfig getInstance() {
        return StudioConfigurationHolder.conf;
    }

    public String getGraphServerUrl() {
        String host = this.getGraphServerHost();
        Integer port = this.getGraphServerPort();
        return String.format("http://%s:%d", host, port);
    }

    public String getGraphServerHost() {
        return this.config.get(StudioApiOptions.GRAPH_SERVER_HOST);
    }

    public Integer getGraphServerPort() {
        return this.config.get(StudioApiOptions.GRAPH_SERVER_PORT);
    }

    public String getGraphName() {
        return this.config.get(StudioApiOptions.GRAPH_NAME);
    }

    public int getClientTimeout() {
        return this.config.get(StudioApiOptions.CLIENT_TIMEOUT);
    }

    public String getBoardFilePath() {
        return String.format("%s/%s", getBaseDirectory(), "board");
    }

    public String getBaseDirectory() {
        String userDataDir = this.config.get(StudioApiOptions.DATA_BASE_DIR);
        if (StringUtils.isBlank(userDataDir)) {
            userDataDir = "~/.hugegraph-studio";
        }
        return replaceHomeDirReferences(userDataDir);
    }

    public int getLimitData() {
        return this.config.get(StudioApiOptions.SHOW_LIMIT_DATA);
    }

    public int getLimitEdgeTotal() {
        return this.config.get(StudioApiOptions.SHOW_LIMIT_EDGE_TOTAL);
    }

    public int getLimitEdgeIncrement() {
        return this.config.get(StudioApiOptions.SHOW_LIMIT_EDGE_INCREMENT);
    }

    public List<Map<String, String>> getVertexVisColor() {
        String colors = this.config.get(StudioApiOptions.VERTEX_VIS_COLOR);
        try {
            return mapper.readValue(colors, List.class);
        } catch (IOException e) {
            throw new RuntimeException("Invalid vertex.vis.color", e);
        }
    }

    public Set<String> getAppendLimitSuffixes() {
        List<String> gremlins =
                this.config.get(StudioApiOptions.GREMLINS_APPEND_LIMIT_SUFFIX);

        if (gremlins == null || gremlins.size() == 0) {
            return new HashSet<>();
        }
        Set<String> gremlinSet = new HashSet<>(gremlins);
        for (String g : gremlins) {
            gremlinSet.add(g);
        }
        return gremlinSet;
    }

    private String replaceHomeDirReferences(String confDir) {
        assert !confDir.isEmpty();

        if (System.getProperty("user.home") == null) {
            return confDir;
        }
        return confDir.replaceFirst("^~", System.getProperty("user.home"));
    }

    public String getVertexShape() {
        return this.config.get(StudioApiOptions.VERTEX_VIS_SHAPE);
    }

    public String getVertexColor() {
        return this.config.get(StudioApiOptions.VERTEX_VIS_COLOR);
    }

    public Integer getVertexSize() {
        return this.config.get(StudioApiOptions.VERTEX_VIS_SIZE);
    }

    public Integer getVertexMaxSize() {
        return this.config.get(StudioApiOptions.VERTEX_SCALING_MAX_SIZE);
    }

    public Integer getVertexMinSize() {
        return this.config.get(StudioApiOptions.VERTEX_SCALING_MIN_SIZE);
    }

    public String getVertexFontColor() {
        return this.config.get(StudioApiOptions.VERTEX_VIS_FONT_COLOR);
    }

    public Integer getVertexFontSize() {
        return this.config.get(StudioApiOptions.VERTEX_VIS_FONT_SIZE);
    }

    public String getEdgeDefaultColor() {
        return this.config.get(StudioApiOptions.EDGE_VIS_COLOR_DEFAULT);
    }

    public String getEdgeHoverColor() {
        return this.config.get(StudioApiOptions.EDGE_VIS_COLOR_HOVER);
    }

    public String getEdgeHighlightColor() {
        return this.config.get(StudioApiOptions.EDGE_VIS_COLOR_HIGHLIGHT);
    }

    public String getEdgeFontColor() {
        return this.config.get(StudioApiOptions.EDGE_VIS_FONT_COLOR);
    }

    public Integer getEdgeFontSize() {
        return this.config.get(StudioApiOptions.EDGE_VIS_FONT_SIZE);
    }
}
