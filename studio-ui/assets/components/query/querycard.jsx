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
import ChangeButton from '../commoncomponents/changebutton';
import CardEditor from "./cardeditor";
import QueryResult from "./queryresult";
import CardFooter from "./cardfooter";
import {changeCardView, execute, loadCard} from './actions';

const initPanelHeight = 1;
const initCardContentHeight = 200;
const headHeight = 32;
const panelBodyPadding = 18;
const cardHeadHeight = 40;
const cardEditorHeight = 48;
const cardContentToolbox = 30;
const footer = 25;
const cardContentPadding = 10;
const cardContentBottomMargin = 8;

export const Gremlin = 'Gremlin';
export const GremlinMode = 'ace/mode/gremlin';
export const EDITOR_ID_SUFFIX = '_editor';
export const LOADING_ID_SUFFIX = '_loading';

class QueryCard extends React.Component {

    constructor() {
        super();
    }

    componentDidMount() {
        this.props.loadCard();
    }

    componentWillUnmount() {
    }

    render() {
        let card = this.props.card;
        let cardConfig = card.cardConfig;
        let fullScreen = card.cardConfig.fullScreen;
        let editorView = card.cardConfig.editorView;

        let screenContainer = fullScreen ?
                              'container-fluid full-screen' : 'container';
        let screenCol = fullScreen ?
                        'col-md-12 full-screen-col-md-12' : 'col-md-12';
        let display = editorView ? 'block' : 'none';
        let panelHeight = this.computeHeight();
        panelHeight = fullScreen ? panelHeight + 'px' : '1px';
        let cardContentHeight = this.cardContentHeight(fullScreen, editorView);
        let queryResultClass = {'height': cardContentHeight};

        let language = card.language.toLowerCase()
                           .replace(/[a-z]/, (L) => L.toUpperCase());

        return (
            <div className={screenContainer}>
                <div className="row card">
                    <div className={screenCol}>
                        <div className="panel panel-default"
                             style={{minHeight: panelHeight}}>
                            <div className="panel-body">
                                <div className="card-header">
                                    <div className="pull-left"
                                         style={{display: display}}>
                                        <div className="query_panel_language">
                                            {language}
                                        </div>
                                    </div>
                                    <div className="btn-group btn-group-sm
                                                    pull-right">
                                        <button type="button"
                                                style={{display: display}}
                                                className="btn btn-link "
                                                onClick={this.execute}>
                                            <i className="fa fa-play"
                                               aria-hidden="true"/>
                                        </button>
                                        <ChangeButton
                                            cssFlag={editorView}
                                            trueCss="fa fa-eye"
                                            falseCss="fa fa-eye-slash"
                                            onClick={this.editorViewMode}/>
                                    </div>
                                </div>

                                <div style={{clear: 'both', display: display}}/>

                                <div className="card-editor"
                                     ref={el => this.cardEditor = el}
                                     style={{display: display}}>
                                    <CardEditor
                                        id={card.id + EDITOR_ID_SUFFIX}
                                        language={card.language}
                                        code={card.code}/>
                                </div>

                                <div className="card-content">
                                    <div ref={el => this.progressWrapper = el}
                                         id={card.id + LOADING_ID_SUFFIX}>
                                        <div className="progress-wrapper"
                                             style={{display: 'none'}}>
                                            <img className="loading-image"
                                                 src="/images/spinner.gif"/>
                                        </div>
                                        <div className="loading-backdrop"/>
                                    </div>

                                    <QueryResult cardId={card.id}
                                                 status={card.status}
                                                 result={card.result}
                                                 className={queryResultClass}
                                                 cardConfig={cardConfig}/>
                                </div>

                                <div className="card-footer">
                                    <CardFooter language={card.language}
                                                result={card.result}/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    execute = () => {
        this.progressWrapper.style.display = 'block';
        let cardId = this.props.card.id;
        let editorId = this.props.card.id + EDITOR_ID_SUFFIX;
        let editorContent = ace.edit(editorId).getValue();

        let card = {
            'id': cardId,
            'code': editorContent
        }
        this.props.execute(card);
    }

    updateCard = () => {
        let editorId = this.props.card.id + EDITOR_ID_SUFFIX;
        let editorContent = ace.edit(editorId).getValue();

        let card = {
            'id':this.props.card.id,
            'code': editorContent,
        }
        this.props.updateCard(card);
    }

    computeHeight = () => {
        let screenHeight = window.innerHeight ||
                           document.documentElement.clientHeight;
        let cardPanelHeight = screenHeight - headHeight;
        return cardPanelHeight;
    }

    cardContentHeight = (fullScreen, editorView) => {
        let cardPanelHeight = fullScreen ?
                              this.computeHeight() : initPanelHeight;
        let currentEditorHeight = cardEditorHeight;

        if (fullScreen) {
            if (editorView) {
                let placeHeight = panelBodyPadding +
                    cardHeadHeight +
                    currentEditorHeight +
                    cardContentToolbox +
                    cardContentPadding +
                    cardContentBottomMargin +
                    footer;
                return cardPanelHeight - placeHeight;
            } else {
                let placeHeight = panelBodyPadding +
                    cardContentToolbox +
                    cardContentPadding +
                    cardContentBottomMargin +
                    footer - 10;
                return cardPanelHeight - placeHeight;
            }
        } else {
            return initCardContentHeight;
        }
    }

    editorViewMode = cssFlag => {
        let editorId = this.props.card.id + EDITOR_ID_SUFFIX;
        let editorContent = ace.edit(editorId).getValue();

        let card = {
            'id':this.props.card.id,
            'code': editorContent,
            'cardConfig': {
                ...this.props.card.cardConfig,
                'editorView': cssFlag
            }
        };
        this.props.updateCard(card);
    };
}

// Map Redux state to component props
function mapStateToProps(state) {
    return {};
}

// Map Redux actions to component props
function mapDispatchToProps(dispatch) {
    return {
        updateCard: (card) => dispatch(changeCardView(card)),
        execute: (card) => dispatch(execute(card)),
        loadCard: () => dispatch(loadCard())
    };
}

// Connected Component
export default  connect(
    mapStateToProps,
    mapDispatchToProps
)(QueryCard);
