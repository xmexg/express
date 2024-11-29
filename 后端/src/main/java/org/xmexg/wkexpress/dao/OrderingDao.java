package org.xmexg.wkexpress.dao;

import org.apache.ibatis.annotations.*;
import org.xmexg.wkexpress.entity.Ordering;

import java.util.List;

/**
 * 正在进行的订单
 * 订单中包含如下信息:openid:支付者的微信唯一标识,orderid:服务器生成的订单号,ordertime:用户下单时间,ordertype:下单类型(0:用户确认价格后取货,1:直接取货),orderpricecon:用户是否已确认订单价格(0:未确认,1:已确认或不需确认),orderamount:订单金额,pickup:取货地点,pickdown:送达地点,pickcode:取货码,pickname:包裹名,name:姓名,phone:手机号,note:备注,shipper送货员的openid,payid:微信支付订单号,paytime:用户付款的时间
 */
@Mapper
public interface OrderingDao {

    /**
     * 名称不带client的,包含敏感信息,只能在服务器内使用,不能传给客户端
     * 名称带client的,不包含敏感信息,可以传给客户端
     */


    /**
     * 查询所有
     * 慎用!
     */
    @Select("select * from booking")
    public List<Ordering> getAll();


    //根据openid(发布者)查询
    @Select("select * from booking where openid = #{openid}")
    public List<Ordering> getById(Integer id);

    //根据取货点查询
    @Select("select * from booking where pickup = #{pickup} limit #{num} offset #{startnum}")
    public List<Ordering> getByPickup(String pickup, int startnum, int num);

    //根据送达点查询
    @Select("select * from booking where pickdown = #{pickdown} limit #{num} offset #{startnum}")
    public List<Ordering> getByPickdown(String pickdown, int startnum, int num);

    //根据订单号查询
    @Select("select * from booking where orderid = #{orderid}")
    public Ordering getOrderByOrderid(String orderid);

    //添加订单
    @Insert("insert into booking values (#{openid}, #{orderid}, #{ordertime}, #{ordertype}, #{orderpricecon}, #{orderamount}, #{pickup}, #{pickdown}, #{pickcode}, #{pickname}, #{name}, #{phone}, #{note}, #{shipper}, #{payid}, #{paytime}, #{status}, #{changepriceable})")
    public int insert(Ordering ordering);

    //删除订单
    @Delete("delete from booking where orderid = #{orderid}")
    public int deleteByOrderid(String orderid);

    //更新订单
    @Update("update booking set openid=#{openid}, orderid=#{orderid}, ordertime=#{ordertime}, ordertype=#{ordertype}, orderpricecon=#{orderpricecon}, orderamount=#{orderamount}, pickup=#{pickup}, pickdown=#{pickdown}, pickcode=#{pickcode}, pickname=#{pickname}, name=#{name}, phone=#{phone}, note=#{note}, shipper=#{shipper}, payid=#{payid}, paytime=#{paytime}, status=#{status} where orderid=#{orderid}")
    public int update(Ordering ordering);

    // 获取未支付订单
    @Select("select * from booking where openid = #{user_openid} and paytime is null")
    public List<Ordering> getUnpaidOrder(String user_openid);


    //带client的是不含openid的,可以返回给客户端的,不带的是不可以的
    //用户获取自己的订单
    @Select("select * from booking where openid = #{openid}")
    public List<Ordering> client_getOrderById_all(String openid);
    @Select("select * from booking where openid = #{openid} limit #{num} offset #{startnum}")
    public List<Ordering> client_getOrderById(String openid, int startnum, int num);

    //在订单页面获取订单列表,顾客用
    @Select("select orderid,ordertime,ordertype,pickup,pickdown,status,changepriceable from booking where status < 10")
    public List<Ordering> client_orderPage_getOrder_all();
    @Select("select orderid,ordertime,ordertype,pickup,pickdown,status,changepriceable from booking where status < 10 limit #{num} offset #{startnum}")
    public List<Ordering> client_orderPage_getOrder(int startnum, int num);

    //在订单页面获取订单列表,管理员和配送员用
    @Select("select * from booking where status < 10 and shipper is null")
    public List<Ordering> client_orderPage_getOrder_manager_all();
    @Select("select * from booking where status < 10 and shipper is null limit #{num} offset #{startnum}")
    public List<Ordering> client_orderPage_getOrder_manager(int startnum, int num);

    // 用户获取自己待接单的订单
    @Select("select * from booking where openid = #{openid} and status < 10 and shipper is null")
    public List<Ordering> client_getOrderById_book(String openid);

    // 用户获取自己已完成的订单
    @Select("select * from booking where openid = #{openid} and status >= 10")
    public List<Ordering> client_getOrderById_finished(String openid);

    // 快递员获取自己的所有配送订单
    @Select("select * from booking where shipper = #{openid}")
    public List<Ordering> client_getOrderById_shipper(String openid);

    // 用户取消订单
    @Update("update booking set status = 12 where orderid = #{orderid}")
    int cancelByOrderid(String orderid);

    // 快递员取消订单
    @Update("update booking set status = 1,shipper = null where orderid = #{orderid}")
    boolean delivery_cancel(String orderid);

    // 快递员完成订单
    @Update("update booking set status = 11 where orderid = #{orderid}")
    boolean delivery_finish(String orderid);

    // 用户获取正在进行的订单数量,所有openid, status<10的订单数量
    @Select("select count(*) from booking where openid = #{openid} and status < 10")
    int user_order_ing(String openid);

    // 用户获取已完成的订单数量,所有openid, status>=10的订单数量
    @Select("select count(*) from booking where openid = #{openid} and status >= 10")
    int user_order_finished(String openid);

    // 快递员正在进行的订单数量,所有shipper, status<10的订单数量
    @Select("select count(*) from booking where shipper = #{openid} and status < 10")
    int shipper_order_ing(String openid);

    // 快递员已完成的订单数量,所有shipper, status>=10的订单数量
    @Select("select count(*) from booking where shipper = #{openid} and status >= 10")
    int shipper_order_finished(String openid);

}
