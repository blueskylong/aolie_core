package com.ranranx.aolie.core.handler;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.datameta.dto.TableDto;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 数据查询服务
 * @Date 2020/8/4 14:33
 * @Version V0.0.1
 **/
@Component
public class QueryHandler<T extends QueryParam> extends BaseHandler<T> {
    @Autowired
    private DataOperatorFactory factory;

    /**
     * 查询所有的子查询的数据 //TODO
     *
     * @param param
     * @return
     */
    private HandleResult findAllResult(QueryParam param) {

        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        List<String> lst = new ArrayList<>();
        lst.add(CommonUtils.getTableName(TableDto.class));
        queryParamDefinition.setTableNames(lst);
        Criteria criteria = queryParamDefinition.appendCriteria();
        criteria.andEqualTo("version", "1");

        //TODO 这里需要将查询分解,合并
        List<Map<String, Object>> lstData = factory.getDataOperatorByKey(null).select(queryParamDefinition);
        HandleResult result = new HandleResult();
        result.setSuccess(true);
        result.setLstData(lstData);
        return result;
    }

    /**
     * 合并数据
     *
     * @param param
     * @param mapResult
     * @return
     */
    private HandleResult mergeResults(QueryParam param, Map<String, HandleResult> mapResult) {
        if (mapResult == null || mapResult.isEmpty()) {
            return new HandleResult();
        } else if (mapResult.size() == 1) {
            return mapResult.values().iterator().next();
        }
        //TODO doMerge
        return null;
    }


    /**
     * 默认可以处理的类型
     *
     * @return
     */
    @Override
    String getCanHandleType() {
        return Constants.HandleType.TYPE_QUERY;
    }

    /**
     * 执行的地方
     *
     * @param param
     * @return
     */
    @Override
    protected HandleResult handle(QueryParam param) {
        return findAllResult(param);
    }


    /**
     * 是否需要事务
     *
     * @return
     */
    @Override
    public boolean needTransaction() {
        return false;
    }
}
