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
import { GremlinMode } from  './querycard';

export default class CardEditor extends React.Component {

    shouldComponentUpdate(nextProps, nextState) {
        if (this.props.code === nextProps.code &&
            this.props.language == nextProps.language) {
            return false;
        } else {
            return true;
        }
    }

    render() {
        return (
            <div>
                <pre id={this.props.id} ref={el => this.editor = el}/>
            </div>
        );
    }

    componentDidUpdate() {
        let editor = ace.edit(this.editor);
        editor.session.setMode(GremlinMode);
        if (this.props.code !== null) {
            editor.setValue(this.props.code);
        } else {
            editor.setValue('');
        }
    }

    componentDidMount() {
        let editor = ace.edit(this.editor);
        ace.require('ace/ext/old_ie');
        ace.require('ace/ext/language_tools');
        editor.setTheme('ace/theme/chrome');
        editor.session.setMode(GremlinMode);
        editor.setShowPrintMargin(false);
        editor.renderer.setShowGutter(false);
        editor.$blockScrolling = Infinity;
        editor.setAutoScrollEditorIntoView(true);
        editor.setOption('maxLines', 15);
        editor.setOption('minLines', 3);
        this.editor.style.fontSize = '12px';
        if (this.props.code !== null) {
            editor.setValue(this.props.code);
        } else {
            editor.setValue('');
        }
        editor.resize();
        editor.setOptions({
            enableBasicAutocompletion: true,
            enableSnippets: true,
            enableLiveAutocompletion: true
        });
    }
}
