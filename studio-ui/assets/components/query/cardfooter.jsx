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

export default class CardFooter extends React.Component {

    constructor() {
        super();
    }

    render() {
        return (
            <div>
                {this.showFooter()}
            </div>
        );
    }

    showFooter = () => {
        if (this.props.result !== undefined && this.props.result !== null) {
            if (this.props.result.duration !== undefined &&
                this.props.result.duration !== null) {
                return (
                    <div>
                        Real-time Success.
                        Duration {this.props.result.duration * 1.0 / 1000}s.
                        &nbsp;
                        {this.props.result.message}
                    </div>
                );
            } else {
                return <div/>;
            }
        } else {
            return <div/>
        }
    }

}
