package org.xmexg.wkexpress.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmexg.wkexpress.dao.ExpresspointDao;
import org.xmexg.wkexpress.entity.Expresspoint;

import java.util.List;

@Service
public class ExpresspointService {

    @Autowired
    private ExpresspointDao expresspointDao;

    public String[] getAll(){
        List<Expresspoint> expresspoints = expresspointDao.getAll();//获取所有快递点,并且该list只有一列
        //list转数组
        return expresspoints.stream().map(Expresspoint::getPoint).toArray(String[]::new);
    }
}
