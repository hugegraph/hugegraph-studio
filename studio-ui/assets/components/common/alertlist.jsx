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
import {Alert} from 'react-bootstrap';
import {connect} from 'react-redux';
import {hideAllAlert, alertHide} from './actions';

class AlertList extends React.Component {
    constructor() {
        super();
    }

    componentDidMount() {
        this.props.hideAllAlert();
    }

    render() {
        return (
            <div>
                {this.props.alerts.items.map((item, i) => (
                    <Alert
                        key={i}
                        bsStyle={item.messageType}
                        onDismiss={() => this.props.alertHide(item.key)}>
                        {item.messageText}
                    </Alert>
                ))}
            </div>
        );
    }
}

// Map Redux state to component props
function mapStateToProps(state) {
    return {
        alerts: state.alerts
    };
}

// Map Redux actions to component props
function mapDispatchToProps(dispatch) {
    return {
        alertHide: key => dispatch(alertHide(key)),
        hideAllAlert: () => dispatch(hideAllAlert())
    };
}

// Connected Component
export default connect(
    mapStateToProps,
    mapDispatchToProps
)(AlertList);
