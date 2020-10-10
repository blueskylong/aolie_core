package com.ranranx.aolie.core.handler.param.condition;

import java.util.Map;

/**
 * @Author xxl
 * @Description 条件接口
 * @Date 2020/8/16 7:16
 * @Version V0.0.1
 **/
public interface ICondition {
    /**
     * 取得查询的条件,并将参数放到Map中, 语句的格式为   field1 = #{field1Value}  则map中有 {"field1Value":"value1"}
     *
     * @param mapValue 语句中的参数值
     * @param alias    过虑表的别名
     * @return
     */
    String getSqlWhere(Map<String, Object> mapValue, String alias, int index, boolean needLogic);

    /**
     * 取得参数的名称
     *
     * @param index
     * @return
     */
    default String getParamName(int index) {
        return "P" + index;
    }
}
