'use strict'

var webpack = require('webpack'),
    jsPath = 'frontend',
    path = require('path'),
    srcPath = path.join(__dirname, jsPath);

var vue = require('vue-loader')
var ExtractTextPlugin = require('extract-text-webpack-plugin')
var cssLoader = ExtractTextPlugin.extract({fallback: "style-loader", use: "css-loader"})

function resolve(dir) {
    return path.join(__dirname, '..', dir)
}

module.exports = {
    mode: 'development',
    target: 'web',
    context: __dirname,
    entry: './src/app.js',
    resolve: {
        alias: {
            'vue$': 'vue/dist/vue.common.js'
        },
        modules: ['node_modules', jsPath]
    },
    output: {
        path: __dirname + "/../public/javascripts/",
        publicPath: '/assets/javascripts/',
        filename: 'app.js'
    },
    module: {
        rules: [
            {
                test: /\.vue$/,
                exclude: /node_modules/,
                loader: 'vue-loader'
            },
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel-loader'
            },
            {
                test: /\.css$/,
                loader: cssLoader
            },
            {
                test: /\.(js|vue)$/,
                loader: 'eslint-loader',
                enforce: 'pre',
                include: [resolve('src'), resolve('test')],
                exclude: /node_modules/,
                options: {
                    formatter: require('eslint-friendly-formatter')
                }
            }
        ]
    },
    performance: {
        hints: false
    },
    devtool: "#source-map"
}

if (process.env.NODE_ENV !== 'production') {
    module.exports.output.filename = module.exports.output.filename.replace(/\.js$/, '.min.js')
    module.exports.devtool = "#source-map"

    module.exports.plugins = (module.exports.plugins || []).concat([
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: '"production"'
            }
        }),
        /*
        new webpack.optimize.UglifyJsPlugin({
            sourceMap: true,
            compress: {
                warnings: false
            }
        }),*/
        new webpack.LoaderOptionsPlugin({
            minimize: true
        })
    ])
}
