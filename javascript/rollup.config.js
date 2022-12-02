import resolve from "@rollup/plugin-node-resolve";
import commonjs from "@rollup/plugin-commonjs";
import json from "@rollup/plugin-json";
import nodePolyfills from "rollup-plugin-polyfill-node";
import typescript from "@rollup/plugin-typescript";
import terser from "@rollup/plugin-terser";

export default {
    input: "src/middleware/index.ts",
    output: {
        file: "./dist.browser/index.js",
        format: "umd",
        name: "LC",
        sourcemap: true,
    },
    context: "commonjsGlobal",
    plugins: [commonjs(), resolve({ browser: true }), json(), nodePolyfills(), typescript({ module: "ESNext" }), terser()],
};
