package com.ranranx.aolie.core.handler.param.condition.express;

import com.ranranx.aolie.core.ds.dataoperator.mybatis.SqlBuilder;
import com.ranranx.aolie.core.ds.definition.Field;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.ds.definition.SqlExp;
import com.ranranx.aolie.core.handler.param.ParamConverter;
import com.ranranx.aolie.core.handler.param.QueryParam;

import java.util.Arrays;
import java.util.Map;

/**
 * 存在子查询
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 20:52
 **/
public class Exists extends BaseCondition {
    public Exists(QueryParam value) {
        super(null, null, value, null);
    }

    @Override
    String getOperExpress() {
        return null;
    }

    @Override
    public String getSqlWhere(Map<String, Object> mapValue, Map<String, String> alias, int index, boolean needLogic) {
        QueryParam param = (QueryParam) value1;
        QueryParamDefinition paramDefinition = ParamConverter.convertQueryParam(param);
        Field field = new Field();
        field.setFieldName("1");
        paramDefinition.setFields(Arrays.asList(field));
        SqlExp sqlExp = SqlBuilder.genSelectParams(paramDefinition, alias);

        mapValue.putAll(sqlExp.getParamValues());
        String andOrStr = (needLogic ? " " + andOr + "  " : "");
        return andOrStr + " " + getKeyWord() + "(" + sqlExp.getSql() + ")";

    }

    protected String getKeyWord() {
        return "exists";
    }

    @Override
    public String checkValid() {
        if (value1 == null) {
            return "没有提供子查询条件";
        }
        return null;
    }
}
