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
import {Nav, NavDropdown, MenuItem} from 'react-bootstrap';

export default class DropDownMenu extends React.Component {

    constructor() {
        super();
        this.state = {selectMenu: ''};
    }

    componentDidMount() {

        if (this.props.initItem !== undefined) {
            this.setState({selectMenu: this.props.initItem});
        } else {
            if (this.props.menuItems !== undefined &&
                this.props.menuItems.length > 0) {
                this.setState({selectMenu: this.props.menuItems[0]});
            } else {
                this.setState({selectMenu: ''});
            }
        }
    }

    handleSelect = (eventKey) => {
        this.setState({selectMenu: `${eventKey}`});
        this.props.onChange(`${eventKey}`);
    }

    render() {
        let menuItems = this.props.menuItems.map((item, index) =>
            <MenuItem key={index} eventKey={item}>{item}</MenuItem>
        );
        let title = this.state.selectMenu;
        let finTitle = title.toLowerCase()
                            .replace(/[a-z]/, (L) => L.toUpperCase());

        return (
            <Nav bsStyle="pills" onSelect={this.handleSelect}>
                <NavDropdown title={finTitle}
                             id={'dropdown_menu_' + this.props.id}>
                    {menuItems}
                </NavDropdown>
            </Nav>
        );
    }
}
