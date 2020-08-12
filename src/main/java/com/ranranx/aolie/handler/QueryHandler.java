package com.ranranx.aolie.handler;

import com.ranranx.aolie.common.Constants;
import com.ranranx.aolie.datameta.datamodal.Table;
import com.ranranx.aolie.datameta.dto.TableDto;
import com.ranranx.aolie.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.handler.param.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        List<Map<String, Object>> lstData = factory.getDataOperatorByName(param.getTable().getDsKey()).select(null);
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
     * 检查并生成查询参数
     *
     * @param mapParams
     * @return
     */
    @Override
    protected T checkAndMakeParam(Map<String, Object> mapParams) {
        //TODO
        QueryParam param = new QueryParam();
        TableDto dto = new TableDto();
        dto.setDataOperId(1L);
        dto.setVersionCode("1");
        Table table = new Table();
        table.setTableDto(dto);
        param.setTable(table);
        return (T) param;
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
