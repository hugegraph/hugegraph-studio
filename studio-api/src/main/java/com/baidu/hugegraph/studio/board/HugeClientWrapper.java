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

package com.baidu.hugegraph.studio.board;

import com.baidu.hugegraph.driver.HugeClient;
import com.baidu.hugegraph.driver.HugeClientBuilder;
import com.baidu.hugegraph.studio.config.StudioApiConfig;

public final class HugeClientWrapper {

    private static volatile HugeClient instance;

    public static HugeClient get(StudioApiConfig config) {
        if (instance == null) {
            synchronized(HugeClientWrapper.class) {
                if (instance == null) {
                    instance = newHugeClient(config);
                }
            }
        }
        return instance;
    }

    private HugeClientWrapper() {}

    private static HugeClient newHugeClient(StudioApiConfig config) {
        HugeClientBuilder builder;
        builder = new HugeClientBuilder(config.getGraphServerUrl(),
                                        config.getGraphName());
        builder.configTimeout(config.getClientTimeout());
        return builder.build();
    }
}
