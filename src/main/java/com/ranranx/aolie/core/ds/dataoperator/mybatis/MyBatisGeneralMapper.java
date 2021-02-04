package com.ranranx.aolie.core.ds.dataoperator.mybatis;

import com.ranranx.aolie.core.ds.dataoperator.IDataOperator;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *
 * @date 2020/8/11 12:12
 * @version V0.0.1
 **/
@Mapper
public interface MyBatisGeneralMapper {

    /**
     * 查询
     *
     * @return
     */
    @Select("${" + IDataOperator.SQL_PARAM_NAME + "}")
    List<Map<String, Object>> select(Map<String, Object> map);

    /**
     * 删除
     *
     * @return
     */
    @Delete("${" + IDataOperator.SQL_PARAM_NAME + "}")
    int delete(Map<String, Object> map);

    /**
     * 更新
     *
     * @return
     */
    @Update("${" + IDataOperator.SQL_PARAM_NAME + "}")
    public int update(Map<String, Object> map);

    /**
     * 插入
     *
     * @return
     */
    @Insert("${" + IDataOperator.SQL_PARAM_NAME + "}")
    public int insert(Map<String, Object> map);

}
