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

export default  class TableResult extends React.Component {

    constructor() {
        super();
    }

    shouldComponentUpdate(nextProps, nextState) {
        if (this.props.content === nextProps.content &&
            this.props.height === nextProps.height) {
            return false;
        } else {
            return true;
        }
    }

    render() {
        const tableContent = this.props.content.data;
        let dataType = this.props.content.type;
        switch (dataType) {
            case 'VERTEX':
                return (
                    <div style={{height: this.props.height + 'px'}}
                         className="code-content">
                        <table className="table table-bordered table-striped">
                            <tbody>
                            <tr className="info">
                                <th>id</th>
                                <th>label</th>
                                <th>properties</th>
                            </tr>
                            {
                                tableContent.map((vertex, index) =>
                                    <tr key={index}>
                                        <td>{vertex.id}</td>
                                        <td>{vertex.label}</td>
                                        <td>
                                            {JSON.stringify(vertex.properties)}
                                        </td>
                                    </tr>
                                )
                            }
                            </tbody>
                        </table>
                    </div>
                );
            case 'EDGE':
                return (
                    <div style={{height: this.props.height + 'px'}}
                         className="code-content">
                        <table className="table table-bordered table-striped">
                            <tbody>
                            <tr className="info">
                                <th>id</th>
                                <th>label</th>
                                <th>source</th>
                                <th>target</th>
                                <th>properties</th>
                            </tr>
                            {
                                tableContent.map((edge, index) =>
                                    <tr key={index}>
                                        <td>{edge.id}</td>
                                        <td>{edge.label}</td>
                                        <td>{edge.outV}</td>
                                        <td>{edge.inV}</td>
                                        <td>
                                            {JSON.stringify(edge.properties)}
                                        </td>
                                    </tr>
                                )
                            }
                            </tbody>
                        </table>
                    </div>
                );
            case 'SINGLE':
                return (
                    <div style={{height: this.props.height + 'px'}}
                         className="code-content">
                        <table className="table table-bordered table-striped">
                            <tbody>
                            <tr className="info">
                                <th>Result</th>
                            </tr>
                            <tr>
                                <td>{JSON.stringify(tableContent)}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                );
            case 'PATH': {
                return (
                    <div style={{height: this.props.height + 'px'}}
                         className="code-content">
                        {
                            this.createPathTable(tableContent)
                        }
                    </div>
                );
            }
            default:
                return (<div/>);
        }
    }

    createPathTable = (tableContent) => {
        console.log(tableContent);
        return (
            <table className="table table-bordered table-striped">
                <tbody>
                <tr className="info">
                    <th>path</th>
                </tr>
                {
                    tableContent.map((path, index) => {
                        return (
                            <tr key={index}>
                                <td>
                                    {this.createPathTdContent(path.objects)}
                                </td>
                            </tr>);
                    })
                }
                </tbody>
            </table>
        );
    };
    createPathTdContent = (objects) => {
        return (
            objects.map((obj, index) => {
               return (
                   <span key={index} className="label label-default path_value">
                       {obj.id !== undefined ? obj.id : obj}
                   </span>
               );
            }));
    }

    componentDidUpdate() {
        this.loadDone();
    }

    componentDidMount() {
        this.loadDone();
    }

    loadDone = () => {
        let loadingId = this.props.cardId + LOADING_ID_SUFFIX;
        document.getElementById(loadingId).style.display = 'none';
    }
}
