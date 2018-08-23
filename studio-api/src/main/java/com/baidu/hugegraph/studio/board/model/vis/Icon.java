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

import static com.baidu.hugegraph.studio.board.model.vis.VisNode.ICON_CODE;
import static com.baidu.hugegraph.studio.board.model.vis.VisNode.ICON_COLOR;
import static com.baidu.hugegraph.studio.board.model.vis.VisNode.ICON_SIZE;
import static com.baidu.hugegraph.studio.board.model.vis.VisNode.VIS_SHAPE;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Icon {

    private String face;
    private String code;
    private Integer size;
    private String color;

    public Icon() {

    }

    public Icon(Builder builder) {
        this.face = builder.face;
        this.code = builder.code;
        this.size = builder.size;
        this.color = builder.color;
    }

    public static class Builder {
        private String face = "FontAwesome";
        private String code = "\uf111";
        private Integer size = 50;
        private String color = "#2B7CE9";

        public Builder(Map<String, Object> userData) {
            Object shape = userData.get(VIS_SHAPE);
            if (shape instanceof String) {
                String shapeStr = (String) shape;
                if (shapeStr.equals("icon")) {
                    Object iconCode = userData.get(ICON_CODE);
                    if (iconCode instanceof String &&
                        StringUtils.isNotBlank((String) iconCode)) {
                        this.code = (String) iconCode;
                    }

                    Object iconColor = userData.get(ICON_COLOR);
                    if (iconColor instanceof String &&
                        StringUtils.isNotBlank((String) iconColor)) {
                        this.color = (String) iconColor;
                    }

                    Object iconSize = userData.get(ICON_SIZE);
                    if (iconSize instanceof Number) {
                        this.size = ((Number) iconSize).intValue();
                    }
                }
            }
        }

        public Icon build() {
            return new Icon(this);
        }

        public Builder face(String face) {
            this.face = face;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder size(Integer size) {
            this.size = size;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
