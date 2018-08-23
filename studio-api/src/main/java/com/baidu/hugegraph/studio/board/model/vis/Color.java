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

import static com.baidu.hugegraph.studio.board.model.vis.VisNode.COLOR_BACKGROUND;
import static com.baidu.hugegraph.studio.board.model.vis.VisNode.COLOR_BORDER;
import static com.baidu.hugegraph.studio.board.model.vis.VisNode.COLOR_HIGHLIGHT_BACKGROUND;
import static com.baidu.hugegraph.studio.board.model.vis.VisNode.COLOR_HIGHLIGHT_BORDER;
import static com.baidu.hugegraph.studio.board.model.vis.VisNode.COLOR_HOVER_BACKGROUND;
import static com.baidu.hugegraph.studio.board.model.vis.VisNode.COLOR_HOVER_BORDER;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.baidu.hugegraph.studio.config.NodeColorOption;

public class Color {

    private static final String DEFAULT = "default";
    private static final String HOVER = "hover";
    private static final String HIGHLIGHT = "highlight";

    private static final String DEFAULT_BACKGROUND_COLOR = "#00ccff";
    private static final String DEFAULT_BORDER_COLOR = "#00ccff";
    private static final String BACKGROUND = "background";
    private static final String BORDER = "border";

    private String background;
    private String border;
    private Map<String, String> highlight;
    private Map<String, String> hover;

    public Color() {
    }

    public Color(Builder builder) {
        this.background = builder.background;
        this.border = builder.border;
        this.highlight = builder.highlight;
        this.hover = builder.hover;
    }

    public static class Builder {
        private String background = DEFAULT_BACKGROUND_COLOR;
        private String border = DEFAULT_BORDER_COLOR;
        private Map<String, String> highlight = new HashMap<>();
        private Map<String, String> hover = new HashMap<>();

        public Builder(Map<String, Object> userData,
                       NodeColorOption colorOption) {
            Map<String, String> curColorOption = colorOption.getColor();
            this.background = curColorOption.get(DEFAULT);
            this.border = curColorOption.get(DEFAULT);
            this.highlight = new HashMap<>();
            this.highlight.put(BACKGROUND, curColorOption.get(HIGHLIGHT));
            this.highlight.put(BORDER, curColorOption.get(HIGHLIGHT));
            this.hover = new HashMap<>();
            this.hover.put(BACKGROUND, curColorOption.get(HOVER));
            this.hover.put(BORDER, curColorOption.get(HOVER));

            Object border = userData.get(COLOR_BORDER);

            if (border instanceof String &&
                !StringUtils.isBlank((String) border)) {
                this.border = (String) border;
            }

            Object background = userData.get(COLOR_BACKGROUND);
            if (background instanceof String &&
                StringUtils.isNotBlank((String) background)) {
                this.background = (String) background;
            }

            Object highlightBorder = userData.get(COLOR_HIGHLIGHT_BORDER);
            if (highlightBorder instanceof String &&
                StringUtils.isNotBlank((String) highlightBorder)) {
                this.highlight.replace(BORDER, (String) highlightBorder);
            }

            Object highlightBackground =
                    userData.get(COLOR_HIGHLIGHT_BACKGROUND);
            if (highlightBackground instanceof String &&
                StringUtils.isNotBlank((String) highlightBackground)) {
                this.highlight
                        .replace(BACKGROUND, (String) highlightBackground);
            }

            Object hoverBorder = userData.get(COLOR_HOVER_BORDER);
            if (hoverBorder instanceof String &&
                StringUtils.isNotBlank((String) hoverBorder)) {
                this.hover.replace(BORDER, (String) hoverBorder);
            }

            Object hoverBackground = userData.get(COLOR_HOVER_BACKGROUND);
            if (hoverBackground instanceof String &&
                StringUtils.isNotBlank((String) hoverBackground)) {
                this.hover.replace(BACKGROUND, (String) hoverBackground);
            }
        }

        public Color build() {
            return new Color(this);
        }

        public Builder background(String background) {
            this.background = background;
            return this;
        }

        public Builder border(String border) {
            this.border = border;
            return this;
        }

        public Builder highlight(Map<String, String> highlight) {
            this.highlight = highlight;
            return this;
        }

        public Builder hover(Map<String, String> hover) {
            this.hover = hover;
            return this;
        }

    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Map<String, String> getHighlight() {
        return highlight;
    }

    public void setHighlight(Map<String, String> highlight) {
        this.highlight = highlight;
    }

    public Map<String, String> getHover() {
        return hover;
    }

    public void setHover(Map<String, String> hover) {
        this.hover = hover;
    }
}
