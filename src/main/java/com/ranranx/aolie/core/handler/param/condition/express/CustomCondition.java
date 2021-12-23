package com.ranranx.aolie.core.handler.param.condition.express;

import com.ranranx.aolie.core.common.CommonUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 自定义条件,其中fieldName可以用合法的语句,map里放着语句中用到的参数值,
 * 如 field1 =#{value1}   map: {"value1:gn"}
 * 可以继续使用__FIELD__来占位,将会被替换
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 22:26
 **/
public class CustomCondition extends BaseCondition {


    public CustomCondition(String tableName, String fieldName, String sql, Map<String, Object> map) {
        super(tableName, fieldName, sql, map);

    }

    @Override
    String getOperExpress() {
        return null;
    }

    /**
     * 取得字段完整名
     *
     * @param alias
     * @return
     */
    protected String getAlias(Map<String, String> alias) {
        if (CommonUtils.isEmpty(this.fieldName)) {
            return null;
        }
        String tableAlias = "";
        if (CommonUtils.isNotEmpty(this.tableName)) {
            if (alias.containsKey(this.tableName)) {
                tableAlias = alias.get(this.tableName) + ".";
            } else {
                tableAlias = tableName + ".";
            }

        }
        return tableAlias;
    }

    @Override
    public String getSqlWhere(Map<String, Object> mapValue, Map<String, String> alias,
                              int[] index, boolean needLogic) {

        StringBuilder sb = new StringBuilder();
        String tableAlias = getAlias(alias);
        String result = (String) value1;
        if (CommonUtils.isNotEmpty(tableAlias)) {
            result = StringUtils.replace(result, tableAlias, PLACEHOLDER_FIELD_NAME);
        }
        Map<String, Object> values = (Map<String, Object>) this.value2;
        if (values != null && !values.isEmpty()) {
            mapValue.putAll(values);
        }
        String andOrStr = (needLogic ? " " + andOr + "  " : "");
        return andOrStr + result;

    }

    @Override
    public String checkValid() {
        if (CommonUtils.isEmpty(value1)) {
            return "没有提供语句";
        }
        return null;
    }
}
