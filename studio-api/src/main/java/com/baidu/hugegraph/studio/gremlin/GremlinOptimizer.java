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

package com.baidu.hugegraph.studio.gremlin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Repository;

import com.baidu.hugegraph.studio.config.StudioApiConfig;

/**
 * Add some rules for gremlin code.
 */
@Repository("gremlinOptimizer")
public class GremlinOptimizer {

    private StudioApiConfig configuration;
    private Map<String, Pattern> suffixPatterns;

    public GremlinOptimizer() {
        configuration = StudioApiConfig.getInstance();
        suffixPatterns = transformSuffixToRegExpPattern(
                         configuration.getAppendLimitSuffixes());
    }

    private Map<String, Pattern> transformSuffixToRegExpPattern(
            Set<String> suffixes) {
        Map<String, Pattern> suffixPatterns = new HashMap<>();
        for (String suffix : suffixes) {
            suffix = suffix.replaceAll("\\.", "\\\\.");
            if (suffix.indexOf("(STR)") > -1) {
                String regExpSuffix = transformSTRWithSingleQuotes(suffix);
                suffixPatterns.put(regExpSuffix, Pattern.compile(regExpSuffix));
                regExpSuffix = transformSTRWithDoubleQuotes(suffix);
                suffixPatterns.put(regExpSuffix, Pattern.compile(regExpSuffix));
                continue;
            }

            if (suffix.indexOf("(NUM)") > -1) {
                String regExpSuffix = transformNum(suffix);
                suffixPatterns.put(regExpSuffix, Pattern.compile(regExpSuffix));
                continue;
            }

            if (suffix.indexOf("()") > -1) {
                String regExpSuffix = suffix.replaceAll("\\(", "\\\\(")
                                            .replaceAll("\\)", "\\\\)");
                regExpSuffix = String.format("(%s)$", regExpSuffix);
                suffixPatterns.put(regExpSuffix, Pattern.compile(regExpSuffix));
                continue;
            }
        }

        return suffixPatterns;
    }

    /**
     * Create regular expression with fun('...').s
     * @param suffix
     * @return regular expression suffix
     */
    private String transformSTRWithSingleQuotes(String suffix) {
        String regExpSuffix = suffix.replaceAll("\\(", "\\\\(")
                                    .replaceAll("\\)", "\\\\)")
                                    .replaceAll("STR", "'[\\\\s\\\\S]+'");
        regExpSuffix = String.format("(%s)$", regExpSuffix);
        return regExpSuffix;
    }

    /**
     * Create regular expression with fun("...").
     *
     * @param suffix
     * @return regular expression suffix
     */
    private String transformSTRWithDoubleQuotes(String suffix) {
        String regExpSuffix = suffix.replaceAll("\\(", "\\\\(")
                                    .replaceAll("\\)", "\\\\)")
                                    .replaceAll("STR", "\"[\\\\s\\\\S]+\"");
        regExpSuffix = String.format("(%s)$", regExpSuffix);
        return regExpSuffix;
    }

    /**
     * Create regular expression with fun(num) , like fun(123).
     *
     * @param suffix
     * @return regular expression suffix
     */
    private String transformNum(String suffix) {
        String regExpSuffix = suffix.replaceAll("\\(", "\\\\(")
                                    .replaceAll("\\)", "\\\\)")
                                    .replaceAll("NUM", "[\\\\d]+");
        regExpSuffix = String.format("(%s)$", regExpSuffix);
        return regExpSuffix;
    }

    /**
     * Add 'limit' to the end of gremlin statement to avoid OOM,
     * when the statement end up with the desired suffix string.
     *
     * @param code
     * @param limit the value need be greater than 0.
     * @return
     */
    public String limitOptimize(String code, int limit) {
        for (Pattern p : suffixPatterns.values()) {
            Matcher m = p.matcher(code);
            if (m.find()) {
                return code + ".limit(" + limit + ")";
            }
        }

        return code;
    }

    public String limitOptimize(String code) {
        return limitOptimize(code, configuration.getLimitData());
    }
}
