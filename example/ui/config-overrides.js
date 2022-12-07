// webpack.config.js
const webpack = require("webpack");

// normalized overrides functions
module.exports = {
  // The Webpack config to use when compiling your react app for development or production.
  webpack: function (config, env) {
    const override = {
      plugins: [
        ...config.plugins,
        new webpack.ProvidePlugin({
          Buffer: ["buffer", "Buffer"],
        }),
      ],
      resolve: {
        ...config.resolve,
        fallback: {
          assert: require.resolve("assert/"),
          stream: require.resolve("stream-browserify"),
          buffer: require.resolve("buffer"),
          os: require.resolve("os-browserify/browser"),
          url: require.resolve("url/"),
          https: require.resolve("https-browserify"),
          http: require.resolve("stream-http"),
          crypto: require.resolve("crypto-browserify"),
        },
      },
    };

    return { ...config, ...override };
  },
};
