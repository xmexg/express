package org.xmexg.wkexpress.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmexg.wkexpress.WkexpressApplication;
import org.xmexg.wkexpress.dao.OrderingDao;
import org.xmexg.wkexpress.entity.Ordering;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderingService {

    @Autowired
    private OrderingDao orderingDao;

    public boolean updateOrder(Ordering order) {
        int update = orderingDao.update(order);
        return update > 0;
    }

    public List<Ordering> getAll(){
        return orderingDao.getAll();
    }

    public boolean addOrder(Ordering ordering){
        int insert = orderingDao.insert(ordering);
        return insert > 0;
    }

    public int unpaidOrderNum(String user_openid) {
        //List通过size()方法可以获取到List的长度
        return orderingDao.getUnpaidOrder(user_openid).size();
    }

    // 获取未支付的订单
    public List<Ordering> getUnpaidOrder(String user_openid) {
        return orderingDao.getUnpaidOrder(user_openid);
    }

    // 通过订单id获取订单
    public Ordering getOrderByOrderid(String orderid) {
        return orderingDao.getOrderByOrderid(orderid);
    }

    //前端用户获取自己的订单,不会包含用户的openid和快递员信息
    public List<Ordering> client_getOrderById(String user_openid, Integer page) {
        if(page == null || page < 1){
            page = 1;
        }
        return orderingDao.client_getOrderById(user_openid, (page-1)*WkexpressApplication.tempData.PageSize, WkexpressApplication.tempData.PageSize);
    }

    //前端订单页面获取简要的订单信息,包含orderid,ordertime,ordertype,pickup,pickdown,分别对应订单号,下单时间,下单类型,取货地点,送达地点
    public List<Ordering> client_orderPage_getOrder(Integer page) {//普通用户使用
        if(page == null || page < 1){
            page = 1;
        }
        return orderingDao.client_orderPage_getOrder((page-1)*WkexpressApplication.tempData.PageSize, WkexpressApplication.tempData.PageSize);
    }
    public List<Ordering> client_orderPage_getOrder_manager(Integer page) {//管理员和派送员使用
        if(page == null || page < 1){
            page = 1;
        }
        return orderingDao.client_orderPage_getOrder_manager((page-1)*WkexpressApplication.tempData.PageSize, WkexpressApplication.tempData.PageSize);
    }
    public List<Ordering> client_orderPage_getOrder_all(){
        return orderingDao.client_orderPage_getOrder_all();
    }
    public List<Ordering> client_orderPage_getOrder_manager_all(){
        return orderingDao.client_orderPage_getOrder_manager_all();
    }

    // 模拟完成支付订单
    public boolean fakepayOrder(String orderid) {
        System.out.println(orderid);
        // 模拟完成订单的快递员就是该用户自己
        Ordering order = orderingDao.getOrderByOrderid(orderid);
        order.setShipper(order.getOpenid());
        order.setPayid("模拟支付,虚拟id");
        order.setPaytime(new Timestamp(System.currentTimeMillis()));
        order.setChangepriceable(0); //设置不能改变价格
        int update = orderingDao.update(order);
        return update > 0;
    }

    // 用户获取自己已下单但未接单的订单
    public List<Ordering> client_getOrderById_book(String userOpenid) {
        return orderingDao.client_getOrderById_book(userOpenid);
    }
    
    // 用户获取自己的所有订单，包括已完成和正在派送
    public List<Ordering> client_getOrderById_all(String openid){
        return orderingDao.client_getOrderById_all(openid);
    }

    // 用户取消订单
    public boolean cancelOrder(String orderid) {
        int delete = orderingDao.cancelByOrderid(orderid);
        return delete > 0;
    }

    // 派送员查看自己的派送订单,包括正在进行和已完成
    public List<Ordering> mydeliveryorder_all(String openid){
        return orderingDao.client_getOrderById_shipper(openid);
    }

    // 派送员取消订单
    public boolean delivery_cancel(String orderid){
        return orderingDao.delivery_cancel(orderid);
    }

    // 派送员完成订单
    public boolean delivery_finish(String orderid){
        return orderingDao.delivery_finish(orderid);
    }

    // 用户获取已下单、已完成的订单数量
    public int[] getNum_order(String openid){
        int[] num = new int[2];
        num[0] = orderingDao.user_order_ing(openid);
        num[1] = orderingDao.user_order_finished(openid);
        return num;
    }

    // 派送员获取派送中，已派送订单数量
    public int[] getNum_delivery(String openid){
        int[] num = new int[2];
        num[0] = orderingDao.shipper_order_ing(openid);
        num[1] = orderingDao.shipper_order_finished(openid);
        return num;
    }
}
