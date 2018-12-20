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

import {alertMessage, checkStatus, parseJSON} from '../common/actions';
import {HTTPSTATUS} from '../httpstatus';

export const UPDATE_CARD = 'update_card';
export const UPDATE_GRAPH = 'update_graph';

export function updateCard(card) {
    changeCardView(card);
}

export function changeCardView(data) {
    return {
        type: UPDATE_CARD,
        data
    };
}

export function loadCard() {
    let url = '/api/v1/board';
    return dispatch => {
        return fetch(url)
            .then(checkStatus)
            .then(parseJSON)
            .then(data => {
                let newCard = {
                    id: data.card.id,
                    code: data.card.code
                };
                dispatch(changeCardView(newCard));
            })
            .catch(err => {
                dispatch(alertMessage('Load Board Fetch Exception:' + err, 'danger'));
            });
    };
}

export function execute(card) {
    return dispatch => {
        let responseStatus = {
            status: 200,
            statusText: 'ok'
        };

        let myHeaders = new Headers();
        myHeaders.append('Content-Type', 'application/json');
        let url = '/api/v1/board';
        return fetch(
            url,
            {
                method: 'POST',
                body: JSON.stringify(card),
                headers: myHeaders
            })
            .then(response => {
                responseStatus.status = response.status;
                responseStatus.statusText = response.statusText;
                return response.json();
            })
            .then(response => {
                if (responseStatus.status >= 200 &&
                    responseStatus.status < 300) {
                    return response;
                } else {
                    let error = new Error(responseStatus.statusText);
                    error.response = response;
                    throw error;
                }
            })
            .then(data => {
                let newCard = {
                    ...card,
                    status:responseStatus.status,
                    result: data
                };
                dispatch(changeCardView(newCard));
            })
            .catch(err => {
                if (responseStatus.statusText === '') {
                    responseStatus.statusText =
                        HTTPSTATUS[responseStatus.status];
                }

                let newCard = {
                    ...card,
                    status: responseStatus.status,
                    result: {
                        message: {
                            title: responseStatus.statusText,
                            detailedMsg: err.response.message
                        }
                    }
                };

                dispatch(changeCardView(newCard));
            });
    };

}

export function updateGraph(cellId, graph) {
    return {
        type: UPDATE_GRAPH,
        cellId,
        graph
    };
}

