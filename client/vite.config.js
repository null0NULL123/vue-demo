import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import path from "node:path";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  server: {
    proxy: {
      "/api": {
        //获取路径中包含了/api的请求
        target: "http://localhost:3000", //后台服务所在的源
        changeOrigin: true, //修改源
        rewrite: (path) => path.replace(/^\/api/, ""), ///api替换为''
      },
    },
  },
  build: {
    minify: "terser",
    terserOptions: {
      compress: {
        drop_console: true, // 移除console
        drop_debugger: true, // 移除debugger
      },
      format: {
        comments: false, // 移除注释
      },
    },
  },
});
