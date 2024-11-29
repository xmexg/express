package org.xmexg.wkexpress.tempdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.xmexg.wkexpress.service.ExpresspointService;

import java.util.Arrays;

/**
 * 这些数据在启动时加载,然后不变
 */
public class TempData {

    //缓存信息
    public String[] expresspoint;//缓存快递点
    public String[] ManagerList;//缓存管理员列表
    public String[] DeliveryList;//缓存快递员列表

    //错误信息
    public String Penetration = "登录信息已过期,请重新登录";//正在被渗透测试才对
    public String NotLogin = "未登录";
    public String NoPermissionToView = "您没有权限查看该信息";
    public String NoPermissionToOperate = "您没有权限操作该信息";
    public String Order_incomplete = "订单信息不完整";
    public String Order_notexist = "订单不存在";
    public String Order_notpay = "订单未支付";
    public String Order_accepted = "订单已被接单";
    public String Order_notdelivery = "订单未被接单";

    //配置信息
    public int MaxAllowUnpaidOrder = 15;//最大允许未支付订单数
    public int PageSize = 10;//分页大小

    //账户类型
    public int Usertype_Customer = 1;//客户的账户类型
    public int Usertype_Delivery = 2;//快递员的账户类型
    public int Usertype_Black = 3;//黑名单的账户类型
    public int Usertype_Admin = 9;//管理员的账户类型

    //接单部分
    public float Order_amount_max = 50;//订单价格最大值
    public float Order_amount_min = 1;//订单价格最小值
    public String Deliveryorder_Success = "接单成功";
    public String Deliveryorder_Fail = "接单失败";
    public String Order_amount_error = "订单金额无效";

}
