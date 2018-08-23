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

import org.slf4j.Logger;

import com.baidu.hugegraph.config.HugeConfig;
import com.baidu.hugegraph.config.OptionSpace;
import com.baidu.hugegraph.util.Log;

/**
 * The type Studio configuration.
 */
public class StudioServerConfig {

    private static final Logger LOG = Log.logger(StudioServerConfig.class);

    static {
        OptionSpace.register("studio", StudioServerOptions.instance());
    }

    private HugeConfig config;

    public StudioServerConfig(final String fileName) {
        // hugegraph-studio.sh -Dstudio.home="$STUDIO_HOME"
        final String homeDir = System.getProperty("studio.home");
        config = new HugeConfig(String.format("%s/conf/%s",
                                homeDir, fileName));
    }

    public int getHttpPort() {
        return this.config.get(StudioServerOptions.STUDIO_SERVER_HTTP_PORT);
    }

    public String getHttpBindAddress() {
        return this.config.get(StudioServerOptions.STUDIO_SERVER_HTTP_BIND_ADDRESS);
    }

    public String getServerBasePath() {
        return this.config.get(StudioServerOptions.STUDIO_SERVER_BASE_PATH);
    }

    public String getServerUIDirectory() {
        return this.config.get(StudioServerOptions.STUDIO_SERVER_UI_DIR);
    }

    public String getServerWarDirectory() {
        return this.config.get(StudioServerOptions.STUDIO_SERVER_WAR_DIR);
    }
}
