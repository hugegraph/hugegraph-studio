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

export const ALERT_SHOW = 'alert_show';
export const ALERT_HIDE = 'alert_hide';

export function alertShow(messageText, messageType, key) {
    return {
        type: ALERT_SHOW,
        payload: {
            messageText, messageType, key
        }
    };
}

export function alertHide(key) {
    return {
        type: ALERT_HIDE,
        payload: {key}
    };
}

export function alertMessage(messageText, messageType, delay = 1000) {
    let msgContent = 'messageText must be string type and messageType must ' +
                     'be either [%s, %s, %s, %s]';
    let msgType = ['success', 'warning', 'danger', 'info'];
    return (dispatch, getState) => {
        if (typeof messageText === 'string' &&
            msgType.indexOf(messageType) > -1) {
            const key = getState().alerts.lastKey + 1;
            dispatch(hideAllAlert(0));
            dispatch(alertShow(messageText, messageType, key));
            if (messageType === 'danger') {
                setTimeout(() => dispatch(alertHide(key)), 5000);
            } else if (messageType === 'warning') {
                setTimeout(() => dispatch(alertHide(key)), 5000);
            } else {
                setTimeout(() => dispatch(alertHide(key)), delay);
            }
        } else {
            console.error(msgContent, ...msgType);
        }
    };
}

export function hideAllAlert(delay = 0) {
    return (dispatch, getState) => {
        getState().alerts.items.forEach(item => {
            setTimeout(() => {
                dispatch(alertHide(item.key));
            }, delay);
        });
    };
}

export function checkStatus(response) {
    if (response.status >= 200 && response.status < 300) {
        return response
    } else {
        let error = new Error(response.statusText);
        error.status = response.status;
        throw error
    }
}

export function parseJSON(response) {
    return response.json()
}
