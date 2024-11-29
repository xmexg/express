package org.xmexg.wkexpress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmexg.wkexpress.WkexpressApplication;
import org.xmexg.wkexpress.service.ExpresspointService;

/**
 * 快递点相关
 */
@RestController
@RequestMapping("/express")
public class Express {

    @Autowired
    private ExpresspointService expresspointService;

//    获取快递点
    @RequestMapping("/point")
    public String[] point(){
        if (WkexpressApplication.tempData.expresspoint != null) {
            return WkexpressApplication.tempData.expresspoint;
        } else {
            return updatepoint();
        }
    }

//    更新快递点
//    @RequestMapping("/updatepoint")
    public String[] updatepoint(){
        String[] exp = expresspointService.getAll();
        WkexpressApplication.tempData.expresspoint = exp;
        return exp;
    }

    // 获取快递点
//    @RequestMapping("/point")
//    public String[] point() {
//        return expresspointService.getAll();
//    }
}
