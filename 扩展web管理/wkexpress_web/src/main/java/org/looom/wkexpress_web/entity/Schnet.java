package org.looom.wkexpress_web.entity;

import java.sql.Timestamp;

/**
 * 用于存放办理校园网的信息，其中id:主键,openid:用户微信的唯一标识符,name:学生姓名,phone:学生手机号,stuid:学号,idcard:身份证号,department:院系,duration,办理时长,orderdate:订单创建时间,state:办理状态(暂时无用,0:未付款,1:已付款,2:已取消),money:办理金额
 */
public class Schnet {

    private String openid;
    private String name;
    private String phone;
    private String stuid;
    private String idcard;
    private String department;
    private String duration;
    private Timestamp orderdate;
    private int state;
    private double money;

    public String openid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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

    public String getStuid() {
        return stuid;
    }

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Timestamp getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Timestamp orderdate) {
        this.orderdate = orderdate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Schnet{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", stuid='" + stuid + '\'' +
                ", idcard='" + idcard + '\'' +
                ", department='" + department + '\'' +
                ", duration='" + duration + '\'' +
                ", orderdate=" + orderdate +
                ", state=" + state +
                ", money=" + money +
                '}';
    }

}
