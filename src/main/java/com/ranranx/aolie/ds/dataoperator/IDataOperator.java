package com.ranranx.aolie.ds.dataoperator;

import com.ranranx.aolie.ds.definition.DeleteParamDefinition;
import com.ranranx.aolie.ds.definition.InsertParamDefinition;
import com.ranranx.aolie.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.ds.definition.UpdateParamDefinition;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 数据源上一层接口, 屏蔽具体数据库的操作 Handler只会调用对应的DataOperator来操作数据库
 * @Date 2020/8/4 13:33
 * @Version V1.0
 **/
public interface IDataOperator {
    /**
     * 默认的语句参数的KEY(键名)
     */
    final String SQL_PARAM_NAME = "sql__";
    /**
     * 默认所有表都需要有ID字段
     */
    final String FIELD_ID = "id";

    /**
     * 查询
     *
     * @param queryParamDefinition
     * @return
     */
    List<Map<String, Object>> select(QueryParamDefinition queryParamDefinition);

    /**
     * 删除
     *
     * @param deleteParamDefinition
     * @return
     */
    int delete(DeleteParamDefinition deleteParamDefinition);

    /**
     * 更新
     *
     * @param updateParamDefinition
     * @return
     */
    int update(UpdateParamDefinition updateParamDefinition);

    /**
     * 插入
     *
     * @param insertParamDefinition
     * @return
     */
    int insert(InsertParamDefinition insertParamDefinition);

    /**
     * 取得连接的唯一标识,用来区分不同的数据源
     *
     * @return
     */
    String getKey();

    /**
     * 取得名称,这是定义此数据源的名字
     *
     * @return
     */
    String getName();

    /**
     * 取得版本
     *
     * @return
     */
    String getVersion();

}
