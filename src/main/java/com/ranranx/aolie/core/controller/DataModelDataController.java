package com.ranranx.aolie.core.controller;

import com.ranranx.aolie.core.common.JQParameter;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.param.Page;
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
        QueryParamDefinition params = queryParams.getQueryParamDefinition();
        List<Map<String, Object>> lstData = factory.getDefaultDataOperator().select(params);
        HandleResult result = new HandleResult();
        result.setSuccess(true);
        result.setLstData(lstData);
        //如果有分页,则需要再次查询总数
        Page page = params.getPage();
        if (page != null) {
            page.setTotalRecord(lstData.size());
            result.setPage(page);
        }
        return result;
    }

    @RequestMapping("/findBlockDataNoPage/{blockId}")
    public List<Map<String, Object>> findBlockDataNoPage(@PathVariable Long blockId, JQParameter queryParams) throws Exception {
        QueryParamDefinition params = queryParams.getQueryParamDefinition();
        return factory.getDefaultDataOperator().select(params);
    }
}
