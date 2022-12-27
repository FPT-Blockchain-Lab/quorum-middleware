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
        ...config.resolve
      },
    };

    return { ...config, ...override };
  },
};
