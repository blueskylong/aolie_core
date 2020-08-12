package com.ranranx.aolie.ds.dataoperator;

import com.ranranx.aolie.ds.definition.DeleteSqlDefinition;
import com.ranranx.aolie.ds.definition.InsertSqlDefinition;
import com.ranranx.aolie.ds.definition.QuerySqlDefinition;
import com.ranranx.aolie.ds.definition.UpdateSqlDefinition;

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
     * 查询
     *
     * @param querySqlDefinition
     * @return
     */
    List<Map<String, Object>> select(QuerySqlDefinition querySqlDefinition);

    /**
     * 删除
     *
     * @param deleteSqlDefinition
     * @return
     */
    int delete(DeleteSqlDefinition deleteSqlDefinition);

    /**
     * 更新
     *
     * @param updateSqlDefinition
     * @return
     */
    int update(UpdateSqlDefinition updateSqlDefinition);

    /**
     * 插入
     *
     * @param insertSqlDefinition
     * @return
     */
    int insert(InsertSqlDefinition insertSqlDefinition);

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
