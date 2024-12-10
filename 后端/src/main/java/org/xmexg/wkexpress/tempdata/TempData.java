package org.xmexg.wkexpress.tempdata;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

    // jsp作业，识别码登录
    public int IdCode_liveTime = 600000;  // 识别码有效时间,默认10分钟内有效
    private record LoginIdCode_obj(String idCode, String token, Long time){}
    private final ConcurrentHashMap<String, LoginIdCode_obj> login_IdCode = new ConcurrentHashMap<>();
    public String makeLoginCode(String token) {
        String idCode = UUID.randomUUID().toString().substring(0, 4); // 更安全的ID生成方式, 生成4位随机字符串
        long nowTime = System.currentTimeMillis();
        this.login_IdCode.forEach((k, v) -> { // 在生成识别码前清除过期的识别码
            if (nowTime - v.time > this.IdCode_liveTime) {
                this.login_IdCode.remove(k);
            }
        });
        login_IdCode.put(idCode, new LoginIdCode_obj(idCode, token, nowTime));
        return idCode;
    }
    public String getLoginToken(String code) {
        LoginIdCode_obj obj = login_IdCode.get(code);
        if (obj == null || System.currentTimeMillis() - obj.time > IdCode_liveTime) {
            login_IdCode.remove(code);
            return null;
        }
        login_IdCode.remove(code); // 使用一次后即移除
        return obj.token;
    }
}
