package com.ranranx.aolie.core.handler.param.condition;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.ds.dataoperator.IDataOperator;
import com.ranranx.aolie.core.ds.dataoperator.mybatis.MyBatisDataOperator;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;

import java.util.Collection;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/14 15:39
 * @Version V0.0.1
 **/
public class Criterion implements ICondition {
    private String preCondition;

    private Object value;

    private Object secondValue;

    private String andOr;

    private boolean noValue;

    private boolean singleValue;

    private boolean betweenValue;

    private boolean listValue;
    /**
     * in 查询
     */
    private boolean inSelectionValue;

    protected Criterion(String preCondition) {
        this(preCondition, false);
    }

    protected Criterion(String preCondition, Object value, String typeHandler) {
        this(preCondition, value, typeHandler, false);
    }

    protected Criterion(String preCondition, Object value) {
        this(preCondition, value, null, false);
    }

    protected Criterion(String preCondition, Object value, Object secondValue, String typeHandler) {
        this(preCondition, value, secondValue, typeHandler, false);
    }

    protected Criterion(String preCondition, Object value, Object secondValue) {
        this(preCondition, value, secondValue, null, false);
    }

    protected Criterion(String preCondition, boolean isOr) {
        super();
        this.preCondition = preCondition;
        this.noValue = true;
        this.andOr = isOr ? "or" : "and";
    }

    protected Criterion(String preCondition, Object value, String typeHandler, boolean isOr) {
        super();
        this.preCondition = preCondition;
        this.value = value;
        this.andOr = isOr ? "or" : "and";
        if (value instanceof Collection<?>) {
            this.listValue = true;
        } else if (value instanceof QueryParamDefinition) {
            this.inSelectionValue = true;
        } else {
            this.singleValue = true;
        }
    }

    protected Criterion(String preCondition, Object value, boolean isOr) {
        this(preCondition, value, null, isOr);
    }

    protected Criterion(String preCondition, Object value, Object secondValue, String typeHandler, boolean isOr) {
        super();
        this.preCondition = preCondition;
        this.value = value;
        this.secondValue = secondValue;
        this.betweenValue = true;
        this.andOr = isOr ? "or" : "and";
    }

    protected Criterion(String preCondition, Object value, Object secondValue, boolean isOr) {
        this(preCondition, value, secondValue, null, isOr);
    }

    public String getAndOr() {
        return andOr;
    }

    public void setAndOr(String andOr) {
        this.andOr = andOr;
    }

    public String getPreCondition() {
        return preCondition;
    }

    public Object getSecondValue() {
        return secondValue;
    }


    public Object getValue() {
        return value;
    }

    public boolean isBetweenValue() {
        return betweenValue;
    }

    public boolean isListValue() {
        return listValue;
    }

    public boolean isNoValue() {
        return noValue;
    }

    public boolean isSingleValue() {
        return singleValue;
    }

    @Override
    public String getSqlWhere(Map<String, Object> mapValue, String alias, int index, boolean needLogic) {
        String firstParamName = getParamName(index) + "_1";
        String secondParamName = getParamName(index) + "_2";
        String andOrStr = (needLogic ? " " + getAndOr() + "  " : "");
        if (CommonUtils.isNotEmpty(alias)) {
            alias = alias + ".";
        } else {
            alias = "";
        }
        if (noValue) {
            return andOrStr + preCondition;
        } else if (singleValue) {
            if (preCondition.indexOf("like") == preCondition.length() - 4) {
                mapValue.put(firstParamName, "%" + value + "%");
            } else {
                mapValue.put(firstParamName, value);
            }
            return andOrStr + alias + preCondition + wrapperParam(firstParamName);
        } else if (listValue && CommonUtils.isNotEmpty(value)) {
            return andOrStr + alias + preCondition + genInString(mapValue, alias, index);
        } else if (inSelectionValue) {
            Map<String, Object> mapValues = genInSelect((QueryParamDefinition) value);
            String sql = "(" + mapValues.remove(IDataOperator.SQL_PARAM_NAME).toString() + ")";
            mapValue.putAll(mapValues);
            return andOrStr + alias + preCondition + sql;
        } else if (betweenValue) {
            mapValue.put(firstParamName, value);
            mapValue.put(secondParamName, secondValue);
            return andOrStr + alias + preCondition + " #{" + firstParamName + "} and #{" + secondParamName + "}";
        }
        return "";

    }

    private Map<String, Object> genInSelect(QueryParamDefinition definition) {
        return MyBatisDataOperator.genSelectParams(definition);
    }

    private String wrapperParam(String paramName) {
        return " #{" + paramName + "}";
    }

    private String genInString(Map<String, Object> mapValue, String alias, int index) {
        String[] values = value.toString().split(",");
        StringBuilder sb = new StringBuilder("(");
        String parIndex = makeParIndexName(index);
        for (int i = 0; i < values.length; i++) {
            mapValue.put(parIndex + i, values[i]);
            sb.append(" #{").append(parIndex + i).append("},");
        }
        return sb.substring(0, sb.length() - 1) + ")";
    }

    private String makeParIndexName(int index) {
        return "P" + index + "__";
    }
}
