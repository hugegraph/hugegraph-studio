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

import org.apache.commons.lang3.StringUtils;

import com.baidu.hugegraph.config.ConfigOption;
import com.baidu.hugegraph.config.OptionHolder;

/**
 * Options for StudioConfiguration.
 */
public class StudioServerOptions extends OptionHolder {

    private StudioServerOptions() {
        super();
    }

    private static volatile StudioServerOptions instance;

    /**
     * Single Instance for StudioOptions.
     *
     * @return the studio options
     */
    public static StudioServerOptions instance() {
        if (instance == null) {
            synchronized (StudioServerOptions.class) {
                if (instance == null) {
                    instance = new StudioServerOptions();
                    instance.registerOptions();
                }
            }
        }
        return instance;
    }

    // hugegraph-studio.sh -Dstudio.home="$STUDIO_HOME"
    public static final ConfigOption<String> STUDIO_SERVER_BASE_PATH =
            new ConfigOption<>(
                    "studio.server.base_path",
                    "The base path of HugeGraphStudio",
                    disallowEmpty(),
                    StringUtils.isNotBlank(System.getProperty("studio.home")) ?
                                           System.getProperty("studio.home") :
                                           System.getProperty("user.dir")
            );

    public static final ConfigOption<String> STUDIO_SERVER_UI_DIR =
            new ConfigOption<>(
                    "studio.server.ui",
                    "The ui directory of HugeGraphStudio",
                    disallowEmpty(),
                    "ui"
            );

    public static final ConfigOption<String> STUDIO_SERVER_WAR_DIR =
            new ConfigOption<>(
                    "studio.server.api.war",
                    "The war directory of HugeGraphStudio",
                    disallowEmpty(),
                    "war/studio-api.war"
            );

    /**
     * The constant STUDIO_SERVER_HTTP_BIND_ADDRESS.
     */
    public static final ConfigOption<String> STUDIO_SERVER_HTTP_BIND_ADDRESS =
            new ConfigOption<>(
                    "studio.server.host",
                    "The http bind address of HugeGraphStudio",
                    disallowEmpty(),
                    "localhost"
            );

    /**
     * The constant STUDIO_SERVER_HTTP_PORT.
     */
    public static final ConfigOption<Integer> STUDIO_SERVER_HTTP_PORT =
            new ConfigOption<>(
                    "studio.server.port",
                    "The http port of HugeGraphStudio",
                    positiveInt(),
                    8088
            );
}
