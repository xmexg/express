# express_ui
- jsp后台管理的前端部分，基于vue  
- 这不是jsp作业的前端页面，jsp作业的前端页面见[tradition文件](../traditional/README.md)

# 路由说明
- [控制跳转](https://github.com/xmexg/express/blob/main/%E6%89%A9%E5%B1%95web%E7%AE%A1%E7%90%86/wkexpress_web/src/main/webapp/index.jsp) 和 [登录页面](https://github.com/xmexg/express/blob/main/%E6%89%A9%E5%B1%95web%E7%AE%A1%E7%90%86/wkexpress_web/src/main/webapp/login.html) 由 [jsp项目](https://github.com/xmexg/express/tree/main/%E6%89%A9%E5%B1%95web%E7%AE%A1%E7%90%86/wkexpress_web) 实现，本项目实现登录后的后台管理页面  
- `控制跳转页面`会控制跳转前往`登录`还是`本管理项目`

## 如何运行
- 假设你已成功克隆整个仓库并cd到了本目录

### 安装包
```sh
npm install
```

### 开发运行 (脱离jsp单独运行)

```sh
npm run dev
```

### 构建jsp用的页面到dist目录 (由jsp管理页面，免去再开多余的服务器)
```sh
npm run build
```
