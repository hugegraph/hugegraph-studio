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
 * License for the specific language governing permissions and limitations under
 * the License.
 */
const {resolve} = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');




module.exports = {
    context: resolve(__dirname, '.'),
    entry: {
        // activate HMR for React
        A: 'react-hot-loader/patch',

        // bundle the client for hot reloading
        // only- means to only hot reload for successful updates
        C: 'webpack/hot/only-dev-server',

        // the entry point of our app
        index: './assets/index.js',
        vendors: ['react', 'react-dom', 'react-router-dom']

    },
    output: {
        // filename: 'bundle.js',
        filename: '[name].js',

        path: resolve(__dirname, 'dist'),

        // necessary for HMR to know where to load the hot update chunks
        publicPath: '/'
    },
    externals: {
        jquery: 'jQuery'
    },
    devtool: 'inline-source-map',
    devServer: {
        // enable HMR on the server
        hot: true,

        // match the output path
        contentBase: resolve(__dirname, 'dist'),
        historyApiFallback: true,
        // match the output `publicPath`
        publicPath: '/',
        port: 8082,
        proxy: {
            '/api': {
                target: 'http://localhost:8088/',
                pathRewrite: {'^/api': '/api/'},
                changeOrigin: true
            }
        }
    },

    module: {
        rules: [
            {
                test: /\.js[x]?$/,
                use: ['babel-loader'],
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
                use: ExtractTextPlugin.extract({fallback: 'style-loader', use: 'css-loader'})
            },
            {test: /\.(jpg|png|gif)$/, use: 'url-loader?limit=25000&name=image/[hash].[ext]'},
            {test: /\.woff[2]?$/, use: 'url-loader?limit=10000&minetype=application/font-woff&name=fonts/[hash].[ext]'},
            {test: /\.ttf$/, use: 'url-loader?limit=10000&name=fonts/[hash].[ext]'},
            {test: /\.eot$/, use: 'url-loader?limit=10000&name=fonts/[hash].[ext]'},
            {test: /\.svg$/, use: 'url-loader?limit=10000&name=fonts/[hash].[ext]'},
            // load html
            {test: /\.html$/, use: 'html-loader'},
        ]
    },

    resolve: {
        extensions: ['.js', '.jsx']
    },

    plugins: [

        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'assets/index.html',
            inject: 'body',
            hash: true,
            chunks: ['vendors', 'index'],
            // min html
            minify: {
                removeComments: true,
                collapseWhitespace: false
            }
        }),
        // enable HMR globally
        new webpack.HotModuleReplacementPlugin(),

        // prints more readable module names in the browser console on HMR updates
        new webpack.NamedModulesPlugin(),

        new ExtractTextPlugin({
            filename: '[name].css'
        }),
        new webpack.optimize.CommonsChunkPlugin({
            // Specify the common bundle's name.
            name: 'vendors',
            filename: 'vendors.bundle.js'
        })
    ],
};