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

package com.baidu.hugegraph.studio.board.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CardConfig is used to save the front page widget's status.
 * The status such as :
 *   1. which is the current tab ?
 *   2. what is the height of current input ?
 *   3. what is the status of every button ?
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CardConfig {
    /**
     * The View type.
     */
    @JsonProperty("viewType")
    public ViewType viewType;

    /**
     * The card height.
     */
    @JsonProperty("cardHeight")
    public Integer cardHeight;

    /**
     * The card width.
     */
    @JsonProperty("cardWidth")
    public Integer cardWidth;

    /**
     * The Full screen.
     */
    @JsonProperty("fullScreen")
    public boolean fullScreen;

    /**
     * The View.
     */
    @JsonProperty("view")
    public boolean view;

    /**
     * Gets view type.
     *
     * @return the view type
     */
    public ViewType getViewType() {
        return viewType;
    }

    /**
     * Sets view type.
     *
     * @param viewType the view type
     */
    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    /**
     * Gets card height.
     *
     * @return the card height
     */
    public Integer getCardHeight() {
        return cardHeight;
    }

    /**
     * Sets card height.
     *
     * @param cardHeight the card height
     */
    public void setCardHeight(Integer cardHeight) {
        this.cardHeight = cardHeight;
    }

    /**
     * Gets card width.
     *
     * @return the card width
     */
    public Integer getCardWidth() {
        return cardWidth;
    }

    /**
     * Sets card width.
     *
     * @param cardWidth the card width
     */
    public void setCardWidth(Integer cardWidth) {
        this.cardWidth = cardWidth;
    }

    /**
     * Is full screen boolean.
     *
     * @return the boolean
     */
    public boolean isFullScreen() {
        return fullScreen;
    }

    /**
     * Sets full screen.
     *
     * @param fullScreen the full screen
     */
    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    /**
     * Is view boolean.
     *
     * @return the boolean
     */
    public boolean isView() {
        return view;
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(boolean view) {
        this.view = view;
    }
}
