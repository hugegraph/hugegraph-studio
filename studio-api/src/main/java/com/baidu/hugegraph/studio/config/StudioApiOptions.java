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

import static com.baidu.hugegraph.config.OptionChecker.disallowEmpty;
import static com.baidu.hugegraph.config.OptionChecker.positiveInt;
import static com.baidu.hugegraph.config.OptionChecker.rangeInt;

import com.baidu.hugegraph.config.ConfigListOption;
import com.baidu.hugegraph.config.ConfigOption;
import com.baidu.hugegraph.config.OptionHolder;

/**
 * The type Studio api options.
 */
public class StudioApiOptions extends OptionHolder {

    private StudioApiOptions() {
        super();
    }

    private static volatile StudioApiOptions instance;

    /**
     * Instance studio api options.
     *
     * @return the studio api options
     */
    public static StudioApiOptions instance() {
        if (instance == null) {
            synchronized (StudioApiOptions.class) {
                if (instance == null) {
                    instance = new StudioApiOptions();
                    instance.registerOptions();
                }
            }
        }
        return instance;
    }

    public static final ConfigOption<String> GRAPH_SERVER_HOST =
            new ConfigOption<>(
                    "graph.server.host",
                    "The host of HugeGraphServer",
                    disallowEmpty(),
                    "localhost"
            );

    public static final ConfigOption<Integer> GRAPH_SERVER_PORT =
            new ConfigOption<>(
                    "graph.server.port",
                    "The port of HugeGraphServer",
                    positiveInt(),
                    8080
            );

    public static final ConfigOption<String> GRAPH_NAME =
            new ConfigOption<>(
                    "graph.name",
                    "The connected graph name",
                    disallowEmpty(),
                    "hugegraph"
            );

    public static final ConfigOption<Integer> CLIENT_TIMEOUT =
            new ConfigOption<>(
                    "client.timeout",
                    "The timeout of hugegraph client in seconds",
                    positiveInt(),
                    30
            );

    /**
     * The constant DATA_BASE_DIR.
     */
    public static final ConfigOption<String> DATA_BASE_DIR =
            new ConfigOption<>(
                    "data.base_directory",
                    "The base directory of HugeGraphStudio's user data.",
                    disallowEmpty(),
                    "~/.hugegraph-studio"
            );

    /**
     * The constant SHOW_LIMIT_DATA.
     */
    public static final ConfigOption<Integer> SHOW_LIMIT_DATA =
            new ConfigOption<>(
                    "show.limit.data",
                    "MAX_SIZE for the data render in web.",
                    rangeInt(1, 10000),
                    100
            );

    /**
     * The constant SHOW_LIMIT_EDGE_TOTAL.
     */
    public static final ConfigOption<Integer> SHOW_LIMIT_EDGE_TOTAL =
            new ConfigOption<>(
                    "show.limit.edge.total",
                    "MAX_SIZE for the edge render in web.",
                    rangeInt(1, 10000),
                    100
            );

    /**
     * The constant SHOW_LIMIT_EDGE_INCREMENT.
     */
    public static final ConfigOption<Integer> SHOW_LIMIT_EDGE_INCREMENT =
            new ConfigOption<>(
                    "show.limit.edge.increment",
                    "MAX_SIZE for the edge increment render in web.",
                    rangeInt(1, 200),
                    50
            );

    /**
     * The constant GREMLIN_EXCLUDE_LIMIT.
     */
    public static final ConfigListOption<String> GREMLINS_APPEND_LIMIT_SUFFIX =
            new ConfigListOption<>(
                    "gremlin.limit_suffix",
                    "The suffixes of gremlin statement which should be " +
                    "appended limit()",
                    disallowEmpty(),
                    String.class,
                    ".V()", ".E()"
            );

    /**
     * The constant VERTEX_VIS_FONT_COLOR.
     */
    public static final ConfigOption<String> VERTEX_VIS_FONT_COLOR =
            new ConfigOption<>(
                    "vertex.vis.font.color",
                    false,
                    "The vertex font color",
                    disallowEmpty(),
                    String.class,
                    "#343434"
            );

    /**
     * The constant VERTEX_VIS_FONT_COLOR.
     */
    public static final ConfigOption<Integer> VERTEX_VIS_FONT_SIZE =
            new ConfigOption<>(
                    "vertex.vis.font.size",
                    false,
                    "The vertex font size",
                    positiveInt(),
                    Integer.class,
                    12
            );

    /**
     * The constant VERTEX_VIS_COLOR.
     */
    public static final ConfigOption<String> VERTEX_VIS_COLOR =
            new ConfigOption<>(
                    "vertex.vis.color",
                    false,
                    "The vertex background color",
                    disallowEmpty(),
                    String.class,
                    "[{\"common\":\"#00ccff\",\"hover\":\"#ec3112\"," +
                    "\"highlight\":\"#fb6a02\"}]"
            );

    /**
     * The constant VERTEX_VIS_SHAPE.
     */
    public static final ConfigOption<String> VERTEX_VIS_SHAPE =
            new ConfigOption<>(
                    "vertex.vis.shape",
                    false,
                    "The vertex shape",
                    disallowEmpty(),
                    String.class,
                    "dot"
            );

    /**
     * The constant VERTEX_VIS_SIZE.
     */
    public static final ConfigOption<Integer> VERTEX_VIS_SIZE =
            new ConfigOption<>(
                    "vertex.vis.size",
                    false,
                    "The vertex size",
                    positiveInt(),
                    Integer.class,
                    25
            );

    /**
     * The constant VERTEX_SCALING_MAX_SIZE.
     */
    public static final ConfigOption<Integer> VERTEX_SCALING_MAX_SIZE =
            new ConfigOption<>(
                    "vertex.vis.scaling.max",
                    false,
                    "The vertex max size",
                    positiveInt(),
                    Integer.class,
                    30
            );

    /**
     * The constant VERTEX_SCALING_MIN_SIZE.
     */
    public static final ConfigOption<Integer> VERTEX_SCALING_MIN_SIZE =
            new ConfigOption<>(
                    "vertex.vis.scaling.min",
                    false,
                    "The vertex min size",
                    positiveInt(),
                    Integer.class,
                    25
            );

    /**
     * The constant EDGE_VIS_COLOR_DEFAULT.
     */
    public static final ConfigOption<String> EDGE_VIS_COLOR_DEFAULT =
            new ConfigOption<>(
                    "edge.vis.color.default",
                    false,
                    "The edge default color",
                    disallowEmpty(),
                    String.class,
                    "#808080"
            );

    /**
     * The constant EDGE_VIS_COLOR_HOVER.
     */
    public static final ConfigOption<String> EDGE_VIS_COLOR_HOVER =
            new ConfigOption<>(
                    "edge.vis.color.hover",
                    false,
                    "The edge hover color",
                    disallowEmpty(),
                    String.class,
                    "#808080"
            );

    /**
     * The constant EDGE_VIS_COLOR_HIGHT.
     */
    public static final ConfigOption<String> EDGE_VIS_COLOR_HIGHLIGHT =
            new ConfigOption<>(
                    "edge.vis.color.highlight",
                    false,
                    "The edge highlight color",
                    disallowEmpty(),
                    String.class,
                    "#808080"
            );

    /**
     * The constant EDGE_VIS_FONT_COLOR.
     */
    public static final ConfigOption<String> EDGE_VIS_FONT_COLOR =
            new ConfigOption<>(
                    "edge.vis.font.color",
                    false,
                    "The edge font color",
                    disallowEmpty(),
                    String.class,
                    "#33333"
            );

    /**
     * The constant VERTEX_SCALING_MIN_SIZE.
     */
    public static final ConfigOption<Integer> EDGE_VIS_FONT_SIZE =
            new ConfigOption<>(
                    "edge.vis.font.size",
                    false,
                    "The edge font size",
                    positiveInt(),
                    Integer.class,
                    12
            );
}
