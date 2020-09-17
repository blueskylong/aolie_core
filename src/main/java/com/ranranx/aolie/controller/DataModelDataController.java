package com.ranranx.aolie.controller;

import com.ranranx.aolie.common.JQParameter;
import com.ranranx.aolie.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.handler.HandleResult;
import com.ranranx.aolie.handler.param.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/9/11 10:03
 * @Version V0.0.1
 **/
@RestController
@RequestMapping("/data")
public class DataModelDataController {
    @Autowired
    private DataOperatorFactory factory;

    @RequestMapping("/findBlockData/{blockId}")
    public HandleResult findBlockDataForJqGrid(@PathVariable Long blockId, JQParameter queryParams) throws Exception {

        List<Map<String, Object>> lstData = factory.getDefaultDataOperator().select(queryParams.getQueryParamDefinition());
        HandleResult result = new HandleResult();
        result.setSuccess(true);
        result.setLstData(lstData);
        result.setPage(new Page(1, lstData.size(), 20));
        return result;
    }
}
