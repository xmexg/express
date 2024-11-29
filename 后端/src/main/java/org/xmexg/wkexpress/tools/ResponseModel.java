package org.xmexg.wkexpress.tools;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 封装返回结果
 */
@Getter
@Component
public class ResponseModel {
    public static final int SUCCESS = 200;
    public static final int FAIL = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int invaliduser = 403;
    public static final int NOTFOUND = 404;
    public static final int notlogin = 405;

    private int code;
    private String message;
    private Object data;

    public String make(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data==null?"":data;
        return JSON.toJSONString(this);
    }

    public String success() {
        return make(SUCCESS, "success", null);
    }

    public String success(Object data) {
        return make(SUCCESS, "success", data);
    }

    public String fail() {
        return make(FAIL, "fail", null);
    }

    public String fail(String message) {
        return make(FAIL, message, null);
    }

    public String unauthorized() {
        return make(UNAUTHORIZED, "unauthorized", null);
    }

    public String unauthorized(String message) {
        return make(UNAUTHORIZED, message, null);
    }

    public String invaliduser() {
        return make(invaliduser, "invaliduser", null);
    }

    public String invaliduser(String message) {
        return make(invaliduser, message, null);
    }

    public String notfound() {
        return make(NOTFOUND, "notfound", null);
    }

    public String notfound(String message) {
        return make(NOTFOUND, message, null);
    }

    public String notlogin() {
        return make(notlogin, "notlogin", null);
    }

    public String notlogin(String message) {
        return make(notlogin, message, null);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}