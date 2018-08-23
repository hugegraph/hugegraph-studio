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
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.baidu.hugegraph.studio.board.model.vis;

import static com.baidu.hugegraph.studio.board.model.vis.VisNode.SCALING_MAX;
import static com.baidu.hugegraph.studio.board.model.vis.VisNode.SCALING_MIN;

import java.util.Map;

import com.baidu.hugegraph.studio.config.StudioApiConfig;

public class Scaling {

    private Integer min = 10;
    private Integer max = 30;

    public Scaling() {

    }

    public Scaling(Builder builder) {
        this.min = builder.min;
        this.max = builder.max;
    }

    public static class Builder {
        private Integer min = 10;
        private Integer max = 30;

        public Builder(StudioApiConfig conf, Map<String, Object> userData) {
            this.min = conf.getVertexMinSize();
            this.max = conf.getVertexMaxSize();

            Object min = userData.get(SCALING_MIN);
            if (min instanceof Number) {
                this.min = ((Number) min).intValue();
            }

            Object max = userData.get(SCALING_MAX);
            if (max instanceof Number) {
                this.min = ((Number) max).intValue();
            }
        }

        public Scaling build() {
            return new Scaling(this);
        }

        public Builder min(Integer min) {
            this.min = min;
            return this;
        }

        public Builder max(Integer max) {
            this.max = max;
            return this;
        }

    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
