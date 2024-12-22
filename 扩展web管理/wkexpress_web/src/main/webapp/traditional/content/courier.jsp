<%--
  Created by IntelliJ IDEA.
  User: vina
  Date: 2024/12/19
  Time: 22:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/svg+xml" href="../res/logo.svg">
    <link rel="stylesheet" type="text/css" href="../css/courier.css">
    <link rel="stylesheet" type="text/css" href="../css/element-plus.css">
    <title>管理 - 校园快递</title>
    <script src="../js/jquery-3.7.1.min.js"></script>
    <script src="../js/jquery-ui.min.js"></script>
    <script src="../js/vue.global.js"></script>
    <script src="../js/element-plus.js"></script>
    <script src="../js/element-plus-icon-vue.js"></script>
</head>
<body>
<div id="app">
    <div class="drag base --el-box-shadow-light">
        <h3>快递点 ({{expressPointTag.length}})</h3>
        <el-tag v-for="tag in expressPointTag" tag-type="primary" tag-effect="dark" @change="handleChange" @close="handleClose">
            {{ tag }}
        </el-tag>
        <div>
            <el-button type="info" @click="get_ExpressPoint">刷新快递点</el-button>
        </div>
    </div>
    <div class="drag base list orderlist">
        <h3>我的派送 ({{orderList.length}})</h3>
        <el-table :data="orderList" height="30%" style="width: 100%">
            <el-table-column prop="openid" label="openid"></el-table-column>
            <el-table-column prop="orderid" label="订单号"></el-table-column>
            <el-table-column prop="ordertime" label="下单时间"></el-table-column>
            <el-table-column prop="ordertype" label="订单类型"></el-table-column>
            <el-table-column prop="orderpricecon" label="确认价格"></el-table-column>
            <el-table-column prop="orderamount" label="订单金额"></el-table-column>
            <el-table-column prop="pickup" label="快递点"></el-table-column>
            <el-table-column prop="pickdown" label="送达点"></el-table-column>
            <el-table-column prop="pickdown" label="取货码"></el-table-column>
            <el-table-column prop="pickname" label="包裹名"></el-table-column>
            <el-table-column prop="name" label="用户名"></el-table-column>
            <el-table-column prop="phone" label="手机号"></el-table-column>
            <el-table-column prop="node" label="备注"></el-table-column>
            <el-table-column prop="shipper" label="配送员"></el-table-column>
            <el-table-column prop="payid" label="付款号"></el-table-column>
            <el-table-column prop="paytime" label="付款时间"></el-table-column>
            <el-table-column prop="status" label="订单状态"></el-table-column>
            <el-table-column prop="changepriceable" label="金额可变"></el-table-column>
        </el-table>
        <el-button type="info" @click="get_MyOrders">刷新订单列表</el-button>
    </div>
</div>
</body>
<script type="module">
    const { createApp } = Vue;

    createApp({
        data() {
            return {
                expressPointTag: [],  // 快递点标签
                userList: [],  // 用户列表
                orderList: [],  // 订单列表
            };
        },
        beforeMount() {
            this.get_ExpressPoint();  // 更新快递点
            this.get_MyOrders();  // 更新订单列表
        },
        methods: {
            handleChange(value) {
                console.log(value);
            },
            handleClose(value) {
                console.log(value);
            },
            get_ExpressPoint() {
                console.log('get_ExpressPoint');
                let that = this;
                $.ajax({
                    url: '../../expresspoint/getPoint',
                    type: 'POST',
                    success: function (data) {
                        console.log(data);
                        data = JSON.parse(data);
                        if(data.code == 200) {
                            that.expressPointTag = data.data;
                            that.$message({
                                message: '获取快递点成功',
                                type: 'success'
                            });
                        } else {
                            that.$message({
                                message: '获取快递点失败',
                                type: 'error'
                            });
                        }
                    },
                    fail: function (data) {
                        // confirm('获取快递点失败: ' + data);
                        that.$message({
                            message: '获取快递点失败',
                            type: 'error'
                        });
                        console.log(data);
                    }
                });
            },
            get_MyOrders() {
                let that = this;
                $.ajax({
                    url: '../../order/ListMyOrders',
                    type: 'POST',
                    success: function (data) {
                        console.log(data);
                        data = JSON.parse(data);
                        if(data.code == 200) {
                            that.orderList = data.data;
                            that.$message({
                                message: '获取订单成功',
                                type: 'success'
                            });
                        } else {
                            that.$message({
                                message: '获取订单失败',
                                type: 'error'
                            });
                        }
                    },
                    fail: function (data) {
                        that.$message({
                            message: '获取订单失败',
                            type: 'error'
                        });
                        console.log(data);
                    }
                });
            }
        }
    }).component('Plus', ElementPlusIconsVue.Plus).use(ElementPlus).mount('#app');
</script>
</html>

