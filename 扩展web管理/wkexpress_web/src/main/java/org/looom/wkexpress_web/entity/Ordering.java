package org.looom.wkexpress_web.entity;


import java.sql.Timestamp;

/*
 * 订单实体类
 * 包含如下属性:
 * openid:支付者的微信唯一标识,orderid:服务器生成的订单号,ordertime:用户下单时间,ordertype:下单类型(0:用户确认价格后取货,1:直接取货),orderpricecon:用户是否已确认订单价格(0:未确认,1:已确认或不需确认),orderamount:订单金额,pickup:取货地点,pickdown:送达地点,pickcode:取货码,pickname:包裹名,name:姓名,phone:手机号,note:备注,shipper送货员的openid,payid:微信支付订单号,paytime:用户付款的时间
 */
public class Ordering {
    private String openid;
    private String orderid;
    private Timestamp ordertime;
    private int ordertype;
    private int orderpricecon;
    private double orderamount;
    private String pickup;
    private String pickdown;//需要检验合法性
    private String pickcode;//需要检验合法性
    private String pickname;//需要检验合法性
    private String name;//需要检验合法性
    private String phone;//需要检验合法性
    private String note;//需要检验合法性
    private String shipper;
    private String payid;
    private Timestamp paytime;
    private int status; // 订单状态
    private int changepriceable; //能否改变价格，1能，0否

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public Timestamp getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Timestamp ordertime) {
        this.ordertime = ordertime;
    }

    public int getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(int ordertype) {
        this.ordertype = ordertype;
    }

    public int getOrderpricecon() {
        return orderpricecon;
    }

    public void setOrderpricecon(int orderpricecon) {
        this.orderpricecon = orderpricecon;
    }

    public double getOrderamount() {
        return orderamount;
    }

    public void setOrderamount(double orderamount) {
        this.orderamount = orderamount;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getPickdown() {
        return pickdown;
    }

    public void setPickdown(String pickdown) {
        this.pickdown = pickdown;
    }

    public String getPickcode() {
        return pickcode;
    }

    public void setPickcode(String pickcode) {
        this.pickcode = pickcode;
    }

    public String getPickname() {
        return pickname;
    }

    public void setPickname(String pickname) {
        this.pickname = pickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid;
    }

    public Timestamp getPaytime() {
        return paytime;
    }

    public void setPaytime(Timestamp paytime) {
        this.paytime = paytime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChangepriceable() {
        return changepriceable;
    }

    public void setChangepriceable(int changepriceable){
        this.changepriceable=changepriceable;
    }

    public String toString(){
        return "openid:"+openid+" orderid:"+orderid+" ordertime:"+ordertime+" ordertype:"+ordertype+" orderpricecon:"+orderpricecon+" orderamount:"+orderamount+" pickup:"+pickup+" pickdown:"+pickdown+" pickcode:"+pickcode+" pickname:"+pickname+" name:"+name+" phone:"+phone+" note:"+note+" shipper:"+shipper+" payid:"+payid+" paytime:"+paytime+" status:"+status+" changepriceable:"+changepriceable;
    }
}
