const path = require("path");
const webpack = require("webpack");
const NodePolyfillPlugin = require("node-polyfill-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const WebpackBundleAnalyzer = require("webpack-bundle-analyzer");
const UglifyJsPlugin = require("uglifyjs-webpack-plugin");

module.exports = (_, argv) => {
    const production = argv.mode === "production";
    return {
        mode: production ? "production" : "development",
        entry: {
            bundle: "./src/middleware/index.ts",
        },
        module: {
            rules: [
                {
                    test: /\.ts?$/,
                    use: "ts-loader",
                    exclude: /node_modules/,
                },
                {
                    test: /\.js$/,
                    enforce: "pre",
                    use: ["source-map-loader"],
                },
            ],
        },
        resolve: {
            extensions: [".ts", ".js"],
            alias: {
                "bn.js": path.resolve(__dirname, "node_modules/bn.js"),
            },
        },
        plugins: [
            new CleanWebpackPlugin(),
            new NodePolyfillPlugin(),
            new WebpackBundleAnalyzer.BundleAnalyzerPlugin({
                analyzerMode: production ? "disabled" : "server",
            }),
            new webpack.SourceMapDevToolPlugin({
                filename: "[file].map",
            }),
        ],
        output: {
            filename: "[name].min.js",
            path: path.resolve(__dirname, "dist.browser"),
            globalObject: "this",
            libraryTarget: "umd",
        },
        optimization: {
            minimize: true,
            minimizer: [
                new UglifyJsPlugin({
                    include: /\.min\.js$/,
                }),
            ],
        },
    };
};
