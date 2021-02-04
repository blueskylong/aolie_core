package com.ranranx.aolie.core.ds.dataoperator;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.dto.DataOperatorDto;
import com.ranranx.aolie.core.ds.definition.DeleteParamDefinition;
import com.ranranx.aolie.core.ds.definition.InsertParamDefinition;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.ds.definition.UpdateParamDefinition;
import com.ranranx.aolie.core.exceptions.DataInvalidException;
import com.ranranx.aolie.core.exceptions.InvalidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *  数据源上一层接口, 屏蔽具体数据库的操作 Handler只会调用对应的DataOperator来操作数据库
 * @date 2020/8/4 13:33
 * @version V1.0
 **/
public interface IDataOperator {
    final Map<String, String> mapMysqlFieldTypeRelation = new HashMap<>();

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
     * 查询
     *
     * @param queryParamDefinition
     * @return
     */
    default <T> List<T> select(QueryParamDefinition queryParamDefinition, Class<T> clazz) {
        return CommonUtils.convertCamelAndToObject(select(queryParamDefinition), clazz);
    }

    /**
     * 查询
     *
     * @param queryParamDefinition
     * @return
     */
    default <T> T selectOne(QueryParamDefinition queryParamDefinition, Class<T> clazz) {
        List<Map<String, Object>> lstData = select(queryParamDefinition);
        if (lstData == null || lstData.isEmpty()) {
            return null;
        }
        if (lstData.size() > 1) {
            throw new DataInvalidException("SelectOne返回多行数据");
        }
        try {
            return CommonUtils.populateBean(clazz, CommonUtils.convertToCamel(lstData.get(0)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidException("生成对象失败:" + e.getMessage());
        }
    }

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

    /**
     * 请求直接执行查询语句,用于复杂查询
     *
     * @param mapParam
     * @return
     */
    List<Map<String, Object>> selectDirect(Map<String, Object> mapParam);

    /**
     * 请求直接执行语句,用于复杂操作
     *
     * @param mapParam
     * @return
     */
    int executeDirect(Map<String, Object> mapParam);


    /**
     * 设置配置信息
     *
     * @param dto
     */
    void setDto(DataOperatorDto dto);

    /**
     * 转换数据库类型到系统
     * @param colType
     * @return
     */
    String convertColType(String colType);
}
