package org.xmexg.wkexpress.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 存放一些关于sql的小工具
 */
@Component
public class MySqlTools {

    /**
     * 把要拼接进sql的文本进行替换，防止sql注入
     */
    public String TransactSQLInjection(String text) {
        return text.replaceAll(".*([';]+|(--)+).*", " ");
    }
}
