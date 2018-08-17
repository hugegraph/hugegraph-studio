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


export default class DefaultResult extends React.Component {

    render() {
        return null;
    }

    componentDidUpdate() {
        this.loadDone();
    }

    componentDidMount() {
        this.loadDone();
    }

    loadDone = () => {
        let loadingId = this.props.id + LOADING_ID_SUFFIX;
        document.getElementById(loadingId).style.display = 'none';
    }
}
