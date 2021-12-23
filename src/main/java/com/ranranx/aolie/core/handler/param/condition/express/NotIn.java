package com.ranranx.aolie.core.handler.param.condition.express;

import com.ranranx.aolie.core.common.SqlTools;

import java.util.List;
import java.util.Map;

/**
 * 不在列表内
 * in 表达式 每500分一段
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 19:26
 **/
public class NotIn extends BaseCondition {
    public NotIn(String tableName, String fieldName, List<Object> lst) {
        super(tableName, fieldName, lst, null);

    }

    @Override
    String getOperExpress() {
        return null;
    }

    @Override
    public String getSqlWhere(Map<String, Object> mapValue, Map<String, String> alias,
                              int[] index, boolean needLogic) {
        String andOrStr = (needLogic ? " " + andOr + "  " : "");
        return andOrStr + genInString(mapValue, alias, index);
    }

    private String genInString(Map<String, Object> mapValue, Map<String, String> alias, int[] index) {
        return SqlTools.genNotInClause(getFullFieldName(alias), (java.util.List<Object>) value1, index, mapValue);
    }
}
