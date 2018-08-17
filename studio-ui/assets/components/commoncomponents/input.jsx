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

const COMMON = 'common';
const ERR = 'verificationFailed';

export  default class Input extends React.Component {

    constructor(props) {
        super(props);
        // status:The status of component ,including COMMON,ERR
        // isChange:The component has been operated

        // isValidateByForce:The component is triggered to validate
        // by the another component
        this.state = {
            status: COMMON,
            message: '',
            value: '',
            isChange: false,
            isValidateByForce: false
        }
        this.handleChange = this.handleChange.bind(this);
        this.validateData = this.validateData.bind(this);
        this.keyPress = this.keyPress.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.isValidateByForce === undefined) {
            this.state.isValidateByForce = false;
        } else {
            this.state.isValidateByForce = nextProps.isValidateByForce;
        }
        this.state.isChange = false;
        let value = nextProps.value;
        const validationResult = this.validateData(value);
        this.state.isValidateByForce = false;
        this.props.onChange(nextProps.name, value, validationResult.flag);
    }

    render() {
        let value = this.state.value;
        if (value === null || value === undefined) {
            value = '';
        }

        if (this.state.status === COMMON)
            return (
                <div className={this.props.className}>
                    <input type="text"
                           className="form-control"
                           placeholder={this.props.placeholder}
                           name={this.props.name}
                           value={value}
                           onChange={this.handleChange}
                           onKeyPress={this.keyPress}/>
                </div>
            );
        else
            return (
                <div className={this.props.className}>
                    <input type="text"
                           className="form-control has-error"
                           placeholder={this.props.placeholder}
                           name={this.props.name}
                           value={value}
                           onChange={this.handleChange}
                           onKeyPress={this.keyPress}/>
                    <div className="form-err-message">{this.state.message}</div>
                </div>
            );
    }

    keyPress(event) {
        if (event.key === 'Enter') {
            if (this.props.onKeyPress !== undefined) {
                this.props.onKeyPress();
            }
        }
    }

    handleChange(event) {
        this.state.isChange = true;
        let value = event.target.value;
        let name = event.target.name;
        const validationResult = this.validateData(value);
        this.props.onChange(name, value, validationResult.flag);
    }

    validateData(value) {
        const validator = this.props.validator;
        let validation = {};
        if (validator !== undefined) {
            if (this.props.message !== undefined) {
                validation = validator(value, this.props.message);
            } else {
                validation = validator(value);
            }
        } else {
            validation = {flag: true};
        }

        if (validation.flag) {
            this.setState({status: COMMON, value: value});
        } else {
            if (this.state.isValidateByForce) {
                this.setState({
                    status: ERR,
                    message: validation.message,
                    value: value
                });
            } else {
                if (!this.state.isChange) {
                    this.setState({
                        status: COMMON,
                        message: validation.message,
                        value: value
                    });
                } else {
                    this.setState({
                        status: ERR,
                        message: validation.message,
                        value: value
                    });
                }
            }
        }
        return validation;
    }

}
