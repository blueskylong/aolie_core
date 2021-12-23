package com.ranranx.aolie.core.handler.param.condition;

import java.util.Map;

/**
 * @author xxl
 * 条件接口
 * @version V0.0.1
 * @date 2020/8/16 7:16
 **/
public interface ICondition {
    final String AND = "and";
    final String OR = "or";

    /**
     * 取得查询的条件,并将参数放到Map中, 语句的格式为   field1 = #{field1Value}  则map中有 {"field1Value":"value1"}
     *
     * @param mapValue 语句中的参数值
     * @param mapAlias 过虑表的别名
     * @return
     */
    String getSqlWhere(Map<String, Object> mapValue, Map<String, String> mapAlias, int[] index, boolean needLogic);

}
