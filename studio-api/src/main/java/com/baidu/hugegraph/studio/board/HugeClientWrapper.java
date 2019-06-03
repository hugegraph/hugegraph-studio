/*
 * Copyright (C) 2019 Baidu, Inc. All Rights Reserved.
 */

package com.baidu.hugegraph.studio.board;

import com.baidu.hugegraph.driver.HugeClient;
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
        return new HugeClient(config.getGraphServerUrl(), config.getGraphName(),
                              config.getClientTimeout());
    }
}
