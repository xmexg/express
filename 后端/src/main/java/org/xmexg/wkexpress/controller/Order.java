package org.xmexg.wkexpress.controller;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xmexg.wkexpress.WkexpressApplication;
import org.xmexg.wkexpress.entity.Ordering;
import org.xmexg.wkexpress.entity.Refund;
import org.xmexg.wkexpress.entity.User;
import org.xmexg.wkexpress.service.ExpresspointService;
import org.xmexg.wkexpress.service.OrderingService;
import org.xmexg.wkexpress.service.UserService;
import org.xmexg.wkexpress.tools.MySqlTools;
import org.xmexg.wkexpress.tools.ResponseModel;

import java.sql.Timestamp;
import java.util.List;

/**
 * 处理快递订单业务
 */
@RestController
@RequestMapping("/order")
public class Order {

    @Autowired
    OrderingService orderingService;
    @Autowired
    UserService userService;
    @Autowired
    ExpresspointService expresspointService;
    @Autowired
    MySqlTools mysqltools;
    @Autowired
    ResponseModel responseModel;

    /**
     * 用户取消订单
     *
     * @param orderid_json 订单id
     * @param token        用户token
     * @return 取消结果
     */
    @PostMapping("/order_cancel")
    public String cancelOrder(@RequestBody String orderid_json, @RequestHeader String token) {
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        String orderid = JSON.parseObject(orderid_json).getString("orderid");
        System.out.println("取消订单 orderid: " + orderid + " token: " + token);
        Ordering ordering = orderingService.getOrderByOrderid(orderid);
        if (ordering == null) return responseModel.fail("订单不存在");
        if (ordering.getOpenid() == null || !ordering.getOpenid().equals(user.getUser_openid()))
            return responseModel.fail("订单不属于您");
        if (ordering.getShipper() != null) return responseModel.fail("订单已被接单,无法取消");
        if (ordering.getStatus() >= 10) return responseModel.fail("订单已完成或已取消,无法(再次)取消");
        // 如果订单已支付，则退款
        Refund refund = new Refund("取消成功", "用户取消订单");
        if (ordering.getPaytime() != null) {
            refund = new Refund("取消成功", "用户取消订单,已退款"+ordering.getOrderamount()+"元", Double.parseDouble(ordering.getOrderamount()));
        }
        boolean cancel = orderingService.cancelOrder(orderid);
        return cancel ? responseModel.success(refund) : responseModel.fail("取消失败，请稍后重试");
    }

    /**
     * 获取未支付订单
     *
     * @param token 用户token
     * @return 未支付订单
     */
    @PostMapping("/getUnpaidOrder")
    public String getUnpaidOrder(@RequestHeader(value = "token", required = false) String token) {
        System.out.println("查询未支付订单 token: " + token);
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        String user_openid = user.getUser_openid();
        List<Ordering> orderings = orderingService.getUnpaidOrder(user_openid);
        return responseModel.success(orderings);
    }

    /**
     * 模拟支付订单
     *
     * @param orderid 订单id
     * @param token   用户token
     * @return 支付结果
     */
    @PostMapping("/payOrder_fake")
    public String payOrder(@RequestBody String orderid, @RequestHeader(value = "token", required = false) String token) {
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();

        String orderid_str = JSON.parseObject(orderid).getString("orderid");
        System.out.println("模拟支付订单 orderid: " + orderid + " 解析id: " + orderid_str + " token: " + token);
        Ordering order = orderingService.getOrderByOrderid(orderid_str);
        if (order == null) return responseModel.fail("订单不存在");
        if (order.getOpenid() == null || !order.getOpenid().equals(user.getUser_openid()))
            return responseModel.fail("订单不属于您");
        if (order.getPaytime() != null) return responseModel.fail("订单已支付");
        boolean fakepay = orderingService.fakepayOrder(orderid_str);
        return fakepay ? responseModel.success("模拟支付成功") : responseModel.fail("模拟支付失败");
    }


    /**
     * @param ordering 订单信息
     * @param token    用户token
     * @return 保存结果
     */
    @PostMapping("/addOrder")
    public String addOrder(@RequestBody Ordering ordering, @RequestHeader(value = "token", required = false) String token) {
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();

        //查询用户没有未支付的订单
        String user_openid = user.getUser_openid();
        boolean haveUnpaidOrder = orderingService.unpaidOrderNum(user_openid) >= WkexpressApplication.tempData.MaxAllowUnpaidOrder;
        if (haveUnpaidOrder) return responseModel.fail("您有未支付的订单,请先支付");

        //查询是否已有该订单(检查订单号)
        //订单号生成规则:user_id+快递点编码+纯数字取货码
        String pickcode = ordering.getPickcode();
        //通过正则表达只保留纯数字
        String regexnotnum = "[^0-9]";//正则表达式,匹配非数字
        pickcode = pickcode.replaceAll(regexnotnum, "");//替换非数字为空 123abc456 -> 123456
        String orderid = user.getUser_id() + ordering.getPickup() + pickcode;
        boolean haveOrder = orderingService.getOrderByOrderid(orderid) != null;
        if (haveOrder) {
            return responseModel.fail("该订单已存在,订单号:" + orderid);
        }

        //检查订单信息是否完整
        if (ordering.getPickup() == null || ordering.getPickcode() == null || ordering.getPickup().isEmpty() || ordering.getPickcode().isEmpty() || ordering.getPickname() == null || ordering.getPickname().isEmpty() || ordering.getPhone() == null || ordering.getPhone().isEmpty()) {
            return WkexpressApplication.tempData.Order_incomplete;
        }

        //准备保存订单,填写订单信息
        //转换快递点编码为快递点名称
        int pickup = Integer.parseInt(ordering.getPickup());
        if (WkexpressApplication.tempData.expresspoint != null) {
            ordering.setPickup(WkexpressApplication.tempData.expresspoint[pickup]);
        } else {
            WkexpressApplication.tempData.expresspoint = expresspointService.getAll();
            ordering.setPickup(WkexpressApplication.tempData.expresspoint[pickup]);
        }
        ordering.setOrderid(orderid);
        //时间精确到秒
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ordering.setOrdertime(timestamp);
        ordering.setOpenid(user_openid);
        ordering.setStatus(1); // 已下单的状态是1
        ordering.setChangepriceable(1); // 能改变价格

        boolean save = orderingService.addOrder(ordering);
        return save ? responseModel.success("保存成功") : responseModel.fail("保存失败");
    }

    //测试使用,显示所有订单
    @RequestMapping("/showallorder")
    public String showallorder(String token) {
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        if (user.getUser_type() != WkexpressApplication.tempData.Usertype_Admin) {
            return responseModel.fail("您没有权限查看该信息");
        }

        List<Ordering> orderings = orderingService.getAll();
        return orderings == null ? "" : JSON.toJSONString(orderings);
    }

    //订单页面显示用户订单,任何人可查看,普通用户查看只会显示关键内容,派送员和管理员查看显示更多关键信息
//    @RequestMapping("/showorder")//旧的
//    public String showorder(Integer page){
//        List<Ordering> orderings = orderingService.client_orderPage_getOrder(page);
//        String msg = orderings == null ? "" : JSON.toJSONString(orderings);
//        return msg;
//    }
    @PostMapping("/showorder")
    public String showorder(@RequestParam String token, @RequestParam Integer page) {
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        List<Ordering> orderings = null;
        if (user.getUser_type() == WkexpressApplication.tempData.Usertype_Admin || user.getUser_type() == WkexpressApplication.tempData.Usertype_Delivery) {
            //管理员和派送员显示详细信息
            System.out.println("管理员和派送员显示详细信息");
            orderings = orderingService.client_orderPage_getOrder_manager_all();
        }
        if (user.getUser_type() == WkexpressApplication.tempData.Usertype_Customer) {
            //顾客显示简要信息
//            orderings = orderingService.client_orderPage_getOrder_all();
            // 顾客显示自己的订单信息
            orderings = orderingService.client_getOrderById_book(user.getUser_openid());
        }
        String msg = orderings == null ? "" : JSON.toJSONString(orderings);
        System.out.println(msg);
        return msg;
    }

    //派送员或管理员在前台点击接单后,在此接口接收信息,将订单状态改为已接单状态
    @PostMapping("/deliveryorder")
    public String deliveryorder(@RequestParam String token, @RequestParam String orderid, @RequestParam Double orderamount) {
        System.out.println("token:" + token + "\torderid:" + orderid + "\torderamount:" + orderamount);
        //检查token是否合法
        //未登录
        if (token == null) {
            return responseModel.fail(WkexpressApplication.tempData.NotLogin);
        }
        //过滤参数
        token = mysqltools.TransactSQLInjection(token);
        orderid = mysqltools.TransactSQLInjection(orderid);
        //检查是否存在该用户
        User user = userService.getUserByToken(token);
        if (user == null) {
            return responseModel.fail(WkexpressApplication.tempData.Penetration);
        }
        if (user.getUser_type() != WkexpressApplication.tempData.Usertype_Admin && user.getUser_type() != WkexpressApplication.tempData.Usertype_Delivery) {
            return responseModel.fail(WkexpressApplication.tempData.NoPermissionToOperate);//没有权限操作
        }

        //检查价格
        if (orderamount < WkexpressApplication.tempData.Order_amount_min || orderamount > WkexpressApplication.tempData.Order_amount_max) {
            return responseModel.fail(WkexpressApplication.tempData.Order_amount_error);//价格错误
        }

        //检查订单是否未被接单
        Ordering order = orderingService.getOrderByOrderid(orderid);
        if (order.getShipper() != null) {
            return responseModel.fail(WkexpressApplication.tempData.Order_accepted);//订单已被接单
        }

        //更新订单信息
        if(order.getChangepriceable()==1) order.setOrderamount(String.valueOf(orderamount));
        order.setShipper(user.getUser_openid());
        order.setStatus(2);
        boolean updateok = orderingService.updateOrder(order);
        return updateok ? responseModel.success(WkexpressApplication.tempData.Deliveryorder_Success) : responseModel.fail(WkexpressApplication.tempData.Deliveryorder_Fail);
    }


    //已弃用
    //查看具体的订单,只有管理员和派送员可查看
    @RequestMapping("/showorderdetailbyorderid")
    public String showorderdetailbyorderid(String token, String orderid) {
        //检查token是否合法
        //未登录
        if (token == null) {
            return WkexpressApplication.tempData.NotLogin;
        }
        //过滤参数
        token = mysqltools.TransactSQLInjection(token);
        orderid = mysqltools.TransactSQLInjection(orderid);
        //检查是否存在该用户
        User user = userService.getUserByToken(token);
        if (user == null) {
            return WkexpressApplication.tempData.Penetration;
        }
        if (user.getUser_type() != WkexpressApplication.tempData.Usertype_Admin && user.getUser_type() != WkexpressApplication.tempData.Usertype_Delivery) {
            return WkexpressApplication.tempData.NoPermissionToView;//没有权限查看该信息
        }

        Ordering ordering = orderingService.getOrderByOrderid(orderid);
        return responseModel.success(ordering);
    }

    //用户查看自己的订单
    @RequestMapping("/myorder_all")
    public String myorder(@RequestHeader String token) {
        //检查token是否合法
        //未登录
        if (token == null) {
            return WkexpressApplication.tempData.NotLogin;
        }
        //过滤参数
        token = mysqltools.TransactSQLInjection(token);
        //检查是否存在该用户
        User user = userService.getUserByToken(token);
        if (user == null) {
            return WkexpressApplication.tempData.Penetration;//能发送数据,说明登录了,用户登录时会返回token,不应该出现这种情况,除非渗透测试
        }

        List<Ordering> orderings = orderingService.client_getOrderById_all(user.getUser_openid());
        return orderings == null ? responseModel.fail() : responseModel.success(orderings);
    }

    // 派送员查看自己的派送订单
    @RequestMapping("/mydeliveryorder_all")
    public String mydeliveryorder(@RequestHeader String token) {
        //检查token是否合法
        //未登录
        if (token == null) {
            return WkexpressApplication.tempData.NotLogin;
        }
        //过滤参数
        token = mysqltools.TransactSQLInjection(token);
        //检查是否存在该用户
        User user = userService.getUserByToken(token);
        if (user == null) {
            return WkexpressApplication.tempData.Penetration;//能发送数据,说明登录了,用户登录时会返回token,不应该出现这种情况,除非渗透测试
        }
        if (user.getUser_type()!=1 && user.getUser_type()!=9) return responseModel.fail("权限不足");
        List<Ordering> orderings = orderingService.mydeliveryorder_all(user.getUser_openid());
        return orderings == null ? responseModel.fail() : responseModel.success(orderings);
    }

    // 派送员取消派送
    @PostMapping("/delivery_cancel")
    public String delivery_cancel(@RequestBody String orderid_json, @RequestHeader String token) {
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        if (user.getUser_type()!=1 && user.getUser_type()!=9) return responseModel.fail("权限不足");
        String orderid = JSON.parseObject(orderid_json).getString("orderid");
        System.out.println("取消订单 orderid: " + orderid + " token: " + token);
        Ordering ordering = orderingService.getOrderByOrderid(orderid);
        if (ordering == null) return responseModel.fail("订单不存在");
        if (ordering.getShipper() == null || !ordering.getShipper().equals(user.getUser_openid())) return responseModel.fail("订单不属于您");
        if (ordering.getStatus() >= 10) return responseModel.fail("订单已完成或已取消,无法(再次)取消");
        return orderingService.delivery_cancel(orderid)? responseModel.success("取消订单成功") : responseModel.fail("取消订单失败");
    }

    // 派送员完成派送
    @PostMapping("/delivery_finish")
    public String delivery_finish(@RequestBody String orderid_json, @RequestHeader String token) {
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        if (user.getUser_type()!=1 && user.getUser_type()!=9) return responseModel.fail("权限不足");
        String orderid = JSON.parseObject(orderid_json).getString("orderid");
        System.out.println("完成订单 orderid: " + orderid + " token: " + token);
        Ordering ordering = orderingService.getOrderByOrderid(orderid);
        if (ordering == null) return responseModel.fail("订单不存在");
        if (ordering.getShipper() == null || !ordering.getShipper().equals(user.getUser_openid())) return responseModel.fail("订单不属于您");
        if (ordering.getStatus() >= 10) return responseModel.fail("订单已完成或已取消,无法(再次)完成");
        return orderingService.delivery_finish(orderid)? responseModel.success("订单完成") : responseModel.fail("失败，请稍后重试");
    }

    // 用户获取已下单、已完成的订单数量
    @PostMapping("/getNum_order")
    public String getNum_order(@RequestHeader String token){
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        return responseModel.success(orderingService.getNum_order(user.getUser_openid()));
    }

    // 派送员获取派送中，已派送订单数量
    @PostMapping("/getNum_delivery")
    public String getNum_delivery(@RequestHeader String token){
        if (token == null || token.isEmpty()) return responseModel.notlogin();
        token = mysqltools.TransactSQLInjection(token);
        User user = userService.getUserByToken(token);
        if (user == null) return responseModel.invaliduser();
        return responseModel.success(orderingService.getNum_delivery(user.getUser_openid()));
    }

}
