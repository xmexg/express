package org.xmexg.wkexpress.dao;

import org.apache.ibatis.annotations.*;
import org.xmexg.wkexpress.entity.User;

import java.util.List;

@Mapper
public interface UserDao {

    @Select("select * from userlist where user_id = #{user_id}")
    public User getUserById(int user_id);

    @Select("select * from userlist where user_openid = #{user_openid}")
    public User getUserByOpenid(String user_openid);

    @Select("select * from userlist where user_token = #{user_token}")
    public User getUserByToken(String user_token);

    @Select("select * from userlist where user_type = #{user_type} limit #{num} offset #{startnum}")
    public List<User> getUserByType(int user_type, int startnum, int num);

    @Insert("insert into userlist(user_type,user_session_key,user_openid,user_token,user_creattime) values(#{user_type},#{user_session_key},#{user_openid},#{user_token},#{user_creattime})")
    @Options(useGeneratedKeys = true, keyProperty = "user_id", keyColumn = "user_id")//返回主键,keyProperty是实体类的属性名,keyColumn是数据库的字段名
    public int addUser(User user);

    //更新user_session_key
    @Update("update userlist set user_session_key = #{user_session_key} where user_id = #{user_id}")
    public void updateSessionKey(User user);

    //更新用户信息,不建议使用
    @Update("update userlist set user_type = #{user_type},user_session_key = #{user_session_key},user_openid = #{user_openid},user_token = #{user_token},user_creattime = #{user_creattime} where user_id = #{user_id}")
    public void updateUser(User user);

    // 更改用户类型
    @Update("update userlist set user_type = #{type} where user_openid = #{openid}")
    int changeUserType(String openid, int type);
}
