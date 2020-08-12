package com.ranranx.aolie.controller;

import com.ranranx.aolie.common.Constants;
import com.ranranx.aolie.ds.dataoperator.multids.DynamicDataSource;
import com.ranranx.aolie.handler.HandleResult;
import com.ranranx.aolie.handler.HandlerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Random;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/10 14:50
 * @Version V0.0.1
 **/
@RequestMapping("/dataService")
@RestController
public class DataOperatorController {
    @Autowired
    private HandlerFactory factory;

    /**
     * 查询数据
     *
     * @param mapParam
     * @return
     */
    @PostMapping("/findData")
    public HandleResult findData(Map<String, Object> mapParam) {
        String[] key = new String[]{"mysql__1", "mysql2__1"};
        DynamicDataSource.setDataSource(key[new Random().nextInt(1)]);
        return factory.getHandler(Constants.HandleType.TYPE_QUERY, mapParam)
                .doHandle(mapParam);
    }

    /**
     * 新增数据
     *
     * @param mapParam
     * @return
     */
    @PostMapping("/insert")
    public HandleResult insert(Map<String, Object> mapParam) {
        return factory.getHandler(Constants.HandleType.TYPE_INSERT, mapParam)
                .doHandle(mapParam);
    }

    @PostMapping("/update")
    public HandleResult update(Map<String, Object> mapParam) {
        return factory.getHandler(Constants.HandleType.TYPE_UPDATE, mapParam)
                .doHandle(mapParam);
    }

    @PostMapping("/delete")
    public HandleResult delete(Map<String, Object> mapParam) {
        return factory.getHandler(Constants.HandleType.TYPE_DELETE, mapParam)
                .doHandle(mapParam);
    }


}
