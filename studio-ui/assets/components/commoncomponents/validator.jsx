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

/**
 * Data validator
 *
 * @param {value} p1 the value which need be verified
 * @return {Json Object} the result ,
 *         like {flag:true or false,message:'information ...'}
 */
export var isNumber = (value, message = 'Please input number') => {
    let regex = /^\+?[1-9][0-9]*$/;
    const result = {
        flag: regex.test(value),
        message: message
    }
    return result;
}

export var isNull = (value, message = 'Please enter a value') => {
    let flag = true;
    if (value === '' || value === null || value === undefined) {
        flag = false;
    }
    const result = {
        flag: flag,
        message: message
    }
    return result;
}

export var isIp = (value, message = 'Please enter a host,like x.x.x.x') => {
    let regex = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    const result = {
        flag: regex.test(value),
        message: message
    }
    return result;
}
