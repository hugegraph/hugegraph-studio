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

package com.baidu.hugegraph.studio.board.model.vis;

import com.baidu.hugegraph.studio.config.StudioApiConfig;

public class EdgeColor {

    private String color;
    private String highlight;
    private String hover;

    public EdgeColor() {
    }

    public EdgeColor(Builder builder) {
        this.color = builder.color;
        this.highlight = builder.highlight;
        this.hover = builder.hover;
    }

    public static class Builder {
        private String color;
        private String highlight;
        private String hover;

        public Builder(StudioApiConfig conf) {
            color = conf.getEdgeDefaultColor();
            highlight = conf.getEdgeHighlightColor();
            hover = conf.getEdgeHoverColor();
        }

        public EdgeColor build() {
            return new EdgeColor(this);
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder highlight(String highlight) {
            this.highlight = highlight;
            return this;
        }

        public Builder hover(String hover) {
            this.hover = hover;
            return this;
        }

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getHover() {
        return hover;
    }

    public void setHover(String hover) {
        this.hover = hover;
    }
}
