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
import ReactDOM from 'react-dom';
import classnames from 'classnames';
import Radium from 'radium';
import assign from 'lodash.assign';

const findParentNode = (parentClass, child) => {
    let parent = child.parentNode;
    while (parent && (parent.className === undefined ||
                      parent.className.indexOf(parentClass) === -1)) {
        parent = parent.parentNode;
    }
    return parent;
};

@Radium
export default class Modal extends React.Component {

    static propTypes = {
        className: React.PropTypes.string,
        isOpen: React.PropTypes.bool.isRequired,
        backdrop: React.PropTypes.bool,
        keyboard: React.PropTypes.bool,
        size: React.PropTypes.oneOf(['modal-lg', 'modal-sm', '']),
        onRequestHide: React.PropTypes.func,
        backdropStyles: React.PropTypes.object,
        dialogStyles: React.PropTypes.object,
        children: React.PropTypes.node.isRequired
    };

    static defaultProps = {
        isOpen: false,
        backdrop: true,
        keyboard: true,
        size: '',
        backdropStyles: {},
        dialogStyles: {}
    };

    componentDidMount = () => {
        document.addEventListener('keydown', this.handleKeyDown);
        this.handleBody();
        this.handleParent();
    };

    componentWillUnmount = () => {
        document.removeEventListener('keydown', this.handleKeyDown);
    };

    componentDidUpdate = () => {
        this.handleBody();
        this.handleParent();
    };

    requestHide = () => {
        let {onRequestHide} = this.props;
        if (onRequestHide) {
            onRequestHide();
        }
    };

    handleBackDropClick = (e) => {
        let {backdrop} = this.props;
        if (e.target !== e.currentTarget || !backdrop) {
            return;
        }
        this.requestHide();
    };

    handleFocus = () => {
        this.focus = true;
    };

    handleBlur = () => {
        this.focus = false;
    };

    handleKeyDown = (e) => {
        let {keyboard} = this.props;
        let el = ReactDOM.findDOMNode(this);
        let childrenOpen = el.className.indexOf('children-open') !== -1;
        if (keyboard && this.focus && e.keyCode === 27 && !childrenOpen) {
            e.preventDefault();
            setTimeout(this.requestHide, 0);
        }
    };

    handleBody = () => {
        let openModals = document.getElementsByClassName('modal-backdrop-open');
        if (openModals.length < 1) {
            document.body.className = document.body.className
                                              .replace(/ ?modal-open/, '');
        } else if (document.body.className.indexOf('modal-open') === -1) {
            document.body.className += document.body.className.length ?
                                       ' modal-open' : 'modal-open';
        }
    };

    handleParent = () => {
        let parentNode = findParentNode('modal-backdrop',
                                        ReactDOM.findDOMNode(this));
        if (parentNode) {
            let {isOpen} = this.props;
            if (isOpen) {
                parentNode.className += parentNode.className.length ?
                                        ' children-open' : 'children-open';
                parentNode.style.overflowY = 'hidden';
            } else {
                parentNode.className =
                    parentNode.className.replace(/ ?children-open/, '');
                parentNode.style.overflowY = 'auto';
            }
        }
    };

    render() {
        let {className, isOpen, backdropStyles, size, dialogStyles, children} =
            this.props;
        let backDropClass = classnames(['modal-backdrop', className], {
            'modal-backdrop-open': isOpen
        }).trim();

        backdropStyles = assign({
            base: {
                background: 'rgba(0, 0, 0, .7)',
                opacity: 0,
                visibility: 'hidden',
                transition: 'all 0.4s',
                overflowX: 'hidden',
                overflowY: 'auto'
            },
            open: {
                opacity: 1,
                visibility: 'visible'
            }
        }, backdropStyles);

        let dialogClass = classnames(['modal-dialog', size], {
            'modal-dialog-open': isOpen
        });

        dialogStyles = assign({
            base: {top: -600, transition: 'top 0.4s'},
            open: {top: 0}
        }, dialogStyles);

        return (
            <div className={backDropClass}
                 style={[backdropStyles.base, isOpen && backdropStyles.open]}
                 onClick={this.handleBackDropClick}>
                <div className={dialogClass}
                     style={[dialogStyles.base, isOpen && dialogStyles.open]}
                     tabIndex="-1"
                     onFocus={this.handleFocus}
                     onBlur={this.handleBlur}>
                    <div className="modal-content">
                        {children}
                    </div>
                </div>
            </div>
        );
    }
}
