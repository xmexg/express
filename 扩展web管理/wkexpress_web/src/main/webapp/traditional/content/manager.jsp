<%--
  Created by IntelliJ IDEA.
  User: vina
  Date: 2024/12/19
  Time: 22:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/svg+xml" href="../res/logo.svg">
    <link rel="stylesheet" type="text/css" href="../css/manager.css">
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
            <el-input-tag draggable v-model="expressPointTag" tag-type="primary" tag-effect="dark" @change="handleChange" @close="handleClose">
                <template #tag="{ value }">
                    <div class="flex items-center">
                        <el-icon class="mr-1">
                            <ElementPlus />
                        </el-icon>
                        <span>{{ value }}</span>
                    </div>
                </template>
            </el-input-tag>
            <div>
                <el-button type="primary" @click="set_ExpressPoint">设置</el-button>
                <el-button type="info" @click="get_ExpressPoint">重置/刷新</el-button>
            </div>
        </div>
        <div class="drag base list userlist">
            <h3>用户列表 ({{userList.length}})</h3>
            <el-table :data="userList" height="30%" style="width: 100%">
                <el-table-column prop="user_id" label="用户id"></el-table-column>
                <el-table-column prop="user_type" label="用户身份"></el-table-column>
                <el-table-column prop="user_openid" label="用户openid"></el-table-column>
                <el-table-column prop="user_creattime" label="创建时间"></el-table-column>
            </el-table>
            <el-button type="info" @click="get_AllUsers">刷新用户列表</el-button>
        </div>
        <div class="drag base list orderlist">
            <h3>订单列表 ({{orderList.length}})</h3>
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
            <el-button type="info" @click="get_AllOrders">刷新订单列表</el-button>
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
            this.get_AllUsers();  // 更新用户列表
            this.get_AllOrders();  // 更新订单列表
        },
        methods: {
            handleChange(value) {
                console.log(value);
            },
            handleClose(value) {
                console.log(value);
            },
            set_ExpressPoint() {
                let that=this
                console.log('set_ExpressPoint');
                $.ajax({
                    url: '../../expresspoint/setpoint',
                    type: 'POST',
                    data: JSON.stringify(this.expressPointTag),
                    success: function (data) {
                        data = JSON.parse(data);
                        that.$message({
                            message: '设置快递点成功',
                            type: 'success'
                        });
                        // confirm('设置快递点成功');
                        console.log(data);
                    },
                    fail: function (data) {
                        that.$message({
                            message: '设置快递点失败',
                            type: 'error'
                        });
                        console.log(data);
                    }
                });
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
                        that.$message({
                            message: '获取快递点失败',
                            type: 'error'
                        });
                        console.log(data);
                    }
                });
            },
            get_AllUsers() {
                let that = this;
                $.ajax({
                    url: '../../user/listAllUsers',
                    type: 'POST',
                    success: function (data) {
                        console.log(data);
                        data = JSON.parse(data);
                        if(data.code == 200) {
                            that.userList = data.data;
                            that.$message({
                                message: '获取用户列表',
                                type: 'success'
                            });
                        } else {
                            that.$message({
                                message: '获取用户列表失败',
                                type: 'error'
                            });
                        }
                    },
                    fail: function (data) {
                        that.$message({
                            message: '获取用户列表失败',
                            type: 'error'
                        });
                        console.log(data);
                    }
                });
            },
            get_AllOrders() {
                let that = this;
                $.ajax({
                    url: '../../order/ListAllOrders',
                    type: 'POST',
                    success: function (data) {
                        console.log(data);
                        data = JSON.parse(data);
                        if(data.code == 200) {
                            that.orderList = data.data;
                            that.$message({
                                message: '获取订单列表',
                                type: 'success'
                            });
                        } else {
                            that.$message({
                                message: '获取订单列表失败',
                                type: 'error'
                            });
                        }
                    },
                    fail: function (data) {
                        that.$message({
                            message: '获取订单列表失败',
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
