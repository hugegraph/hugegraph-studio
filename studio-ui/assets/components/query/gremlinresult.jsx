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
import {connect} from 'react-redux';
import {Tabs, TabPane} from '../commoncomponents/tabs';
import Graph from './graph';
import Code from './code';
import NoData from './nodata';
import TableResult from './table';
import {changeCardView} from './actions';

export const TABLE = 'TABLE';
export const RAW = 'RAW';
export const GRAPH = 'GRAPH';
export const NODATA = 'NODATA';

class GremlinResult extends React.Component {

    constructor() {
        super();
        this.state = {
            tabs: []
        }

        this.curTabs = [];
    }

    componentWillReceiveProps(nextProps) {
        let tabs = this.getTabs(nextProps.content, nextProps.defaultTabKey);
        this.setState({tabs: tabs});
    }

    render() {
        let tabPanes = this.state.tabs.map((tab, index) => {
            if (tab.type === GRAPH) {
                return (
                    <TabPane key={index}>
                        <Graph id={this.props.id + '_graph'}
                               content={this.props.content}
                               height={this.props.height}
                               cardId={this.props.id}/>
                    </TabPane>
                )
            } else if (tab.type === TABLE) {
                return (
                    <TabPane key={index}>
                        <TableResult content={this.props.content}
                                     height={this.props.height}
                                     cardId={this.props.id}/>
                    </TabPane>
                )
            } else if (tab.type === NODATA) {
                return (
                    <TabPane key={index}>
                        <NoData id={this.props.id + '_none'}
                                content={this.props.content}
                                height={this.props.height}
                                cardId={this.props.id}/>
                    </TabPane>
                )
            } else {
                return (
                    <TabPane key={index}>
                        <Code id={this.props.id + '_code'}
                              content={this.props.content}
                              height={this.props.height}
                              cardId={this.props.id}/>
                    </TabPane>
                )
            }
        });

        return (
            <Tabs tabs={this.state.tabs}
                  loading={this.loading}
                  onChangeTab={this.onChangeTab}>
                {tabPanes}
            </Tabs>
        );
    }

    componentDidUpdate() {
        if (this.state.tabs.length === 0) {
            this.loadDone();
        }
    }

    componentDidMount() {
        let tabs = this.getTabs(this.props.content, this.props.defaultTabKey);
        if (tabs.length === 0) {
            this.loadDone();
        }
        this.setState({tabs: tabs});
    }

    loadDone = () => {
        let loadingId = this.props.id + '_loading';
        document.getElementById(loadingId).style.display = 'none';
    }

    loading = () => {
        let loadingId = this.props.id + '_loading';
        document.getElementById(loadingId).style.display = 'block';
    }

    onChangeTab = (type, tabs) => {
        this.curTabs = tabs;

        let card = {
            'cardConfig': {
                ...this.props.cardConfig,
                'viewType': type
            }
        }
        this.props.updateCard(card);
    }

    getTabs = (content, defaultTabKey) => {
        let nextTabs = [];
        switch (content.type) {
            case 'SINGLE':
                nextTabs = [{
                    type: TABLE,
                    isActive: false,
                    exist: false,
                    label: 'fa fa-table'
                }, {
                    type: RAW,
                    isActive: false,
                    exist: false,
                    label: 'fa fa-code'
                }];
                break;
            case 'OTHER':
            case 'EMPTY':
                nextTabs = [{
                    type: RAW,
                    isActive: false,
                    exist: false,
                    label: 'fa fa-code'
                }];
                break;
            case 'EDGE':
            case 'VERTEX':
            case 'PATH':
                nextTabs = [{
                    type: TABLE,
                    isActive: false,
                    exist: false,
                    label: 'fa fa-table'
                }, {
                    type: GRAPH,
                    isActive: false,
                    exist: false,
                    label: 'fa fa-joomla'
                }, {
                    type: RAW,
                    isActive: false,
                    exist: false,
                    label: 'fa fa-code'
                }];
                break;
            case null:
                nextTabs = [{
                    type: NODATA,
                    isActive: false,
                    exist: false,
                    label: 'fa fa-code'
                }];
                break;
            default:
                nextTabs = [];
        }

        // Use the current tabs and the defaultTabKey to identify the nextTabs;
        // the current tabs keep the state of tabs now，which can be changed
        // by the function "onChangeTab"
        this.curTabs.forEach(curTab => {
            nextTabs = nextTabs.map(nextTab => {
                if (nextTab.type === curTab.type) {
                    nextTab.exist = curTab.exist;
                }
                return nextTab;
            })
        });


        // According the current selected tabKey to identify next default Tab;
        // details as follows:
        // (1) if defaultTabKey === 1 ,next default tab is nextTabs[0]
        // (2) if defaultTabKey !==1 and the new resultTabs contain this type
        //         next default tab is  the current selected tabKey
        //     else
        //         next default tab is nextTabs[0]
        if (nextTabs.length > 0) {
            if (defaultTabKey === 1) {
                nextTabs[0].isActive = true;
                nextTabs[0].exist = true;
            } else {
                let isExistDefaultTabKey = false;
                nextTabs = nextTabs.map(tab => {
                    if (tab.type === defaultTabKey) {
                        tab.isActive = true;
                        tab.exist = true;
                        isExistDefaultTabKey = true;
                    }
                    return tab;
                })
                if (!isExistDefaultTabKey) {
                    nextTabs[0].isActive = true;
                    nextTabs[0].exist = true;
                }
            }
        }
        return nextTabs;
    }
}

// Map Redux state to component props
function mapStateToProps(state) {
    return {};
}

// Map Redux actions to component props
function mapDispatchToProps(dispatch) {
    return {
        updateCard: (card) => dispatch(changeCardView(card))
    };
}

// Connected Component
export default  connect(
    mapStateToProps,
    mapDispatchToProps
)(GremlinResult);
