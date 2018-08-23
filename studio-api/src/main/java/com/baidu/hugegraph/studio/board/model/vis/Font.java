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

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Font {

    // Font
    protected static final String FONT_COLOR = "vis.font.color";
    protected static final String FONT_SIZE = "vis.font.size";
    protected static final String FONT_FACE = "vis.font.face";
    protected static final String FONT_MULTI = "vis.font.multi";

    private String color;
    private Integer size;
    private String face;
    private Boolean multi;

    public Font() {

    }

    private Font(Builder builder) {
        color = builder.color;
        size = builder.size;
        face = builder.face;
        multi = builder.multi;
    }

    public static class Builder {
        private String color = "#343434";
        private Integer size = 12;
        private String face = "arial";
        private Boolean multi = false;


        public Builder() {

        }


        public Builder(Map<String, Object> userData) {
            Object fontSize = userData.get(FONT_SIZE);
            if (fontSize instanceof Number) {
                Number size = (Number) fontSize;
                if (size != null) {
                    this.size = size.intValue();
                }
            }

            Object fontColor = userData.get(FONT_COLOR);
            if (fontColor instanceof String &&
                StringUtils.isNotBlank((String) fontColor)) {
                this.color = (String) fontColor;
            }

            Object fontFace = userData.get(FONT_FACE);
            if (fontFace instanceof String &&
                StringUtils.isNotBlank((String) fontFace)) {
                this.face = (String) fontFace;
            }

            Object fontMulti = userData.get(FONT_MULTI);
            if (fontMulti instanceof Boolean) {
                Boolean multi = (Boolean) fontMulti;
                if (multi != null) {
                    this.multi = multi;
                }
            }
        }

        public Font build() {
            return new Font(this);
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder size(Integer size) {
            this.size = size;
            return this;
        }

        public Builder face(String face) {
            this.face = face;
            return this;
        }

        public Builder multi(Boolean multi) {
            this.multi = multi;
            return this;
        }

    }

    public String getColor() {
        return color;
    }

    public Integer getSize() {
        return size;
    }

    public String getFace() {
        return face;
    }

    public Boolean getMulti() {
        return multi;
    }

}
