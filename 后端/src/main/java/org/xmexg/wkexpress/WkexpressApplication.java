package org.xmexg.wkexpress;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xmexg.wkexpress.tempdata.TempData;

@SpringBootApplication
@MapperScan("org.xmexg.wkexpress.dao")
public class WkexpressApplication {
	/**
	 * 存放运行时所需要的一些变量和常量
	 */
	public static TempData tempData = new TempData();


	public static void main(String[] args) {
		SpringApplication.run(WkexpressApplication.class, args);
	}

}
