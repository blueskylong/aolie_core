package com.ranranx.aolie.ds;

import com.ranranx.aolie.ds.definition.DeleteSqlDefinition;
import com.ranranx.aolie.ds.definition.InsertSqlDefinition;
import com.ranranx.aolie.ds.definition.QuerySqlDefinition;
import com.ranranx.aolie.ds.definition.UpdateSqlDefinition;
import com.ranranx.aolie.engine.param.QueryParam;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 数据源上一层接口, 屏蔽具体数据库的操作
 * @Date 2020/8/4 13:33
 * @Version V1.0
 **/
public interface DataOperator {

    List<Map<String, Object>> select(QuerySqlDefinition querySqlDefinition);

    int delete(DeleteSqlDefinition deleteSqlDefinition);

    int update(UpdateSqlDefinition updateSqlDefinition);

    int insert(InsertSqlDefinition insertSqlDefinition);

}
