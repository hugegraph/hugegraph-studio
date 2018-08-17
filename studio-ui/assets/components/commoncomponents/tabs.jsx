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

export class Tabs extends React.Component {

    constructor() {
        super();
        this.state = {
            tabs: []
        }
    }

    componentWillReceiveProps(nextProps) {
        this.setState({tabs: nextProps.tabs});
    }

    render() {
        return (
            <div>
                <div className="react-tabs">
                    <ul className="nav nav-pills">
                        {
                            this.state.tabs.map((tab, index) => {
                                if (tab.isActive) {
                                    return (
                                        <li className="active" key={index}>
                                            <a onClick={() =>
                                                this.onClick(tab.type)}>
                                                <i className={tab.label}
                                                   aria-hidden="true"/>
                                            </a>
                                        </li>
                                    )
                                } else {
                                    return (
                                        <li key={index}>
                                            <a onClick={() =>
                                                this.onClick(tab.type)}>
                                                <i className={tab.label}
                                                   aria-hidden="true"/>
                                            </a>
                                        </li>
                                    )
                                }
                            })
                        }
                    </ul>
                </div>
                <div className="tab-content">
                    {
                        this.props.children.map((child, index) => {
                            let tabPane = null
                            let curTab = this.state.tabs[index]
                            if (curTab.isActive) {
                                tabPane =
                                    <div key={index}
                                         className="show">
                                        {child}
                                    </div>
                            } else {
                                if (curTab.exist) {
                                    tabPane =
                                        <div key={index}
                                             className="hidden">
                                            {child}
                                        </div>
                                } else {
                                    tabPane = null
                                }
                            }
                            return tabPane
                        })
                    }
                </div>
            </div>
        );
    }

    componentDidMount() {
        this.setState({tabs: this.props.tabs});
    }

    onClick = (type) => {
        let tabs = this.state.tabs.map(tab => {
            if (tab.type === type) {
                if (!tab.exist) {
                    this.props.loading();
                }
                tab.isActive = true;
                tab.exist = true;
            }
            else {
                tab.isActive = false;
            }
            return tab;
        });
        this.props.onChangeTab(type, tabs);
    }
}

export class TabPane extends React.Component {

    render() {
        return (
            <div>
                {this.props.children}
            </div>
        );
    }
}
