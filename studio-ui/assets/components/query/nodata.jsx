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
import { LOADING_ID_SUFFIX } from './querycard';

export default  class NoData extends React.Component {

    constructor() {
        super();
        this.state = {
            showDetail: false
        }
    }

    render() {
        let display = this.state.showDetail ? 'block' : 'none';
        return (
            <div>
                <div className="alert alert-danger err_msg">
                    <div className="err_title">
                        Return data is null...
                        <span className="label label-danger detail"
                              onClick={this.showDetail}>
                            Detail
                        </span>
                    </div>
                    <div style={{
                                    height: this.props.height + 'px',
                                    display: display
                                }}
                         className="code-content"
                         id={this.props.id}>
                    </div>
                </div>
            </div>
        );
    }

    showDetail = () => {
        this.setState({
            showDetail: !this.state.showDetail
        });
    }

    componentDidUpdate() {
        let paneJson = '#' + this.props.id;
        let json = JSON.stringify(this.props.content);
        $(paneJson).JSONView(json, {collapsed: true});
        this.loadDone();
    }

    componentDidMount() {
        let paneJson = '#' + this.props.id;
        let json = JSON.stringify(this.props.content);
        $(paneJson).JSONView(json, {collapsed: true});
        this.loadDone();
    }

    loadDone = () => {
        let loadingId = this.props.cardId + LOADING_ID_SUFFIX;
        document.getElementById(loadingId).style.display = 'none';
    }
}
