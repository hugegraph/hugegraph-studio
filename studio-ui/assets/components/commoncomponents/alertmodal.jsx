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
import Modal from './modal';

export default class AlertModal extends React.Component {

    render() {
        return (
            <Modal isOpen={true}>
                <div className="modal-header">
                    <button type="button"
                            className="close"
                            data-dismiss="modal"
                            aria-label="Close"
                            onClick={() => this.props.cancel()}>
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <div className="alert-message">
                        <h4 className="modal-title">{this.props.message}</h4>
                    </div>
                </div>
                <div className="modal-footer">
                    <button type="button"
                            className="btn btn-default"
                            data-dismiss="modal"
                            onClick={() => this.props.cancel()}>
                        Cancel
                    </button>
                    <button type="button"
                            className="btn btn-primary"
                            onClick={(id) => this.props.confirm(id)}>
                        Yes
                    </button>
                </div>
            </Modal>
        )
    }
}
