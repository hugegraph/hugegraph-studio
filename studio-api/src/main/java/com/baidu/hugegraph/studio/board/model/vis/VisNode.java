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

import com.baidu.hugegraph.studio.config.NodeColorOption;
import com.baidu.hugegraph.studio.config.StudioApiConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

import jersey.repackaged.com.google.common.collect.ImmutableMap;

/**
 * The style of vertexLabel.
 */
public class VisNode {

    // Shape
    protected static final String VIS_SHAPE = "vis.shape";

    // Size
    protected static final String VIS_SIZE = "vis.size";

    // Scaling
    protected static final String SCALING_MIN = "vis.scaling.min";
    protected static final String SCALING_MAX = "vis.scaling.max";

    // Color
    protected static final String COLOR_BORDER = "vis.border";
    protected static final String COLOR_BACKGROUND = "vis.background";
    protected static final String COLOR_HIGHLIGHT_BORDER =
            "vis.highlight.border";
    protected static final String COLOR_HIGHLIGHT_BACKGROUND =
            "vis.highlight.background";
    protected static final String COLOR_HOVER_BORDER = "vis.hover.border";
    protected static final String COLOR_HOVER_BACKGROUND =
            "vis.hover.background";

    // Icon
    protected static final String ICON_CODE = "vis.icon.code";
    protected static final String ICON_COLOR = "vis.icon.color";
    protected static final String ICON_SIZE = "vis.icon.size";

    private String shape;
    private Integer size;

    @JsonProperty
    private Color color;
    @JsonProperty
    private Icon icon;
    @JsonProperty
    private Scaling scaling;

    public VisNode() {
    }

    public VisNode(String shape, Integer size, Color color, Icon icon,
                   Scaling scaling) {
        this.shape = shape;
        this.size = size;
        this.color = color;
        this.icon = icon;
        this.scaling = scaling;
    }

    public VisNode(NodeColorOption colorOption) {
        this(ImmutableMap.of(), colorOption);
    }

    public VisNode(Map<String, Object> userData, NodeColorOption colorOption) {
        this(StudioApiConfig.getInstance().getVertexShape(),
             StudioApiConfig.getInstance().getVertexSize(),
             new Color.Builder(userData, colorOption).build(),
             new Icon.Builder(userData).build(),
             new Scaling.Builder(StudioApiConfig.getInstance(), userData)
                        .build());

        // Size
        Object size = userData.get(VIS_SIZE);
        if (size instanceof Number) {
            this.size = ((Number) size).intValue();
        }

        // Shape
        Object shape = userData.get(VIS_SHAPE);
        if (shape instanceof String) {
            this.shape = (String) shape;
        }
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Scaling getScaling() {
        return scaling;
    }

    public void setScaling(Scaling scaling) {
        this.scaling = scaling;
    }
}
