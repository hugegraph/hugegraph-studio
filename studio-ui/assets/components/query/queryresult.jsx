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

import React from 'react';
import GremlinResult from './gremlinresult';
import ErrorResult from './errorresult';
import DefaultResult from './defaultresult';

export default class QueryResult extends React.Component {

    constructor() {
        super();
    }

    render() {
        let result = this.showResult();
        return (
            <div>{result}</div>
        );
    }

    showResult = () => {
        let resultPanel = null;
        let result = this.props.result;
        if (result === null || result === undefined) {
            return <DefaultResult id={this.props.cardId}/>;
        }

        let status = this.props.status;
        if (status === null || status === undefined) {
            return <DefaultResult id={this.props.cardId}/>;
        }

        if (status >= 200 && status <= 300) {
            let defaultTabKey = 1;
            if (this.props.cardConfig !== null) {
                if (this.props.cardConfig.viewType !== null &&
                    this.props.cardConfig.viewType !== undefined) {
                    defaultTabKey = this.props.cardConfig.viewType;
                }
            }
            resultPanel =
                <GremlinResult defaultTabKey={defaultTabKey}
                               id={this.props.cardId}
                               content={result}
                               height={this.props.className.height}
                               cardConfig={this.props.cardConfig}/>;
        } else {
            resultPanel = <ErrorResult status={status}
                                       msg={this.props.result.message}
                                       id={this.props.cardId}/>;
        }
        return resultPanel;
    };
}
