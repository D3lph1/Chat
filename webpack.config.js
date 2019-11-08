const path = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
    entry: {
        app: './web/src/main/resources/assets/app.js'
    },
    output: {
        path: path.resolve(__dirname, './web/src/main/resources/static'),
        publicPath: '/',
        filename: 'js/[name].min.js'
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                loader: 'babel-loader',
                exclude: /node_modules/
            },
            {
                test: /\.(png|jpg|gif|svg)$/,
                loader: 'file-loader',
                options: {
                    name: 'img/[name].[ext]?[hash]'
                }
            },
            {
                test: /\.css$/,
                use: ExtractTextPlugin.extract({
                    use: 'css-loader'
                })
            },
            {
                test: /\.(less)$/,
                use: ExtractTextPlugin.extract({
                    use: ['css-loader', 'less-loader']
                })
            },
            {
                test: /\.(eot|svg|ttf|woff|woff2|otf)$/,
                use: [{
                    loader: "url-loader",
                    options: {
                        name: "font/[name].[ext]"
                    }
                }]
            }
        ]
    },
    node: {
        net: 'empty',
    },
    devServer: {
        historyApiFallback: true,
        noInfo: true,
        overlay: true
    },
    watchOptions: {
        aggregateTimeout: 300,
        poll: false,
        ignored: /node_modules/
    },
    performance: {
        hints: false
    },
    devtool: '#eval-source-map',
    plugins: [
        new webpack.ProvidePlugin({
            Waves: 'node-waves'
        }),
        new ExtractTextPlugin("css/app.min.css")
    ]
};

if (process.env.NODE_ENV === 'production') {
    // module.exports.devtool = '#source-map';
    module.exports.devtool = undefined;

    // http://vue-loader.vuejs.org/en/workflow/production.html
    module.exports.plugins = (module.exports.plugins || []).concat([
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: '"production"'
            }
        }),
        new webpack.optimize.UglifyJsPlugin({
            sourceMap: true,
            compress: {
                warnings: false
            }
        }),
        new webpack.LoaderOptionsPlugin({
            minimize: true
        }),
    ])
}
