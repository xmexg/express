package org.xmexg.wkexpress.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 退款模板
 */
public class Refund {
    @Getter
    @Setter
    String status;  // 退款状态
    @Getter
    @Setter
    String reason;  // 退款原因
    @Getter
    @Setter
    double price;  // 退款金额

    public Refund(String status, String reason, double price) {
        this.status = status;
        this.reason = reason;
        this.price = price;
    }

    public Refund(String reason, double price) {
        this.status = "退款成功";
        this.reason = reason;
        this.price = price;
    }

    public Refund(String status, String reason) {
        this.status = status;
        this.reason = reason;
        this.price = 0;
    }
}
