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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {

    private String id;
    private String code;

    /**
     * Instantiates a new board card.
     *
     * @param id           the id
     * @param code         the code
     */
    @JsonCreator
    public Card(@JsonProperty("id") String id,
                @JsonProperty("code") String code) {
        this.id = id;
        this.code = code;
    }

    /**
     * Instantiates a new board card.
     */
    public Card() {
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Card's id is uuid
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * The code user input in the front page code editor.
     *
     * @return the code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }
        Card that = (Card) o;

        return (that.id == null && this.id == null) ||
               (this.id != null && this.id.equals(that.id));
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }
}
