/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ranranx.aolie.core.handler.param.condition;

import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.exceptions.InvalidException;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *  此设计复制于tk.mybatis插件, 并做修改
 * @date 2020/8/14 15:38
 * @version V0.0.1
 **/
public class Criteria implements ICondition {

    protected List<ICondition> lstCondition = new ArrayList<>();
    //字段是否必须存在
    protected boolean exists;
    //值是否不能为空
    protected boolean notNull;
    //连接条件
    protected String andOr;

    protected String tableName;
    private static Criteria alwaysFalse = new Criteria();


    static {
        alwaysFalse.andCondition("1=2");
    }

    public Criteria(boolean exists, boolean notNull) {
        super();
        this.exists = exists;
        this.notNull = notNull;
        lstCondition = new ArrayList<>();
        andOr = "and";
    }

    public static Criteria getFalseExpression() {
        return alwaysFalse;
    }

    public Criteria() {
        this(false, false);
    }

    public Criteria createSubOrCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAndOr("or");
        lstCondition.add(criteria);
        return criteria;
    }

    public Criteria createSubAndCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAndOr("and");
        lstCondition.add(criteria);
        return criteria;
    }


    protected void addCriterion(String condition) {
        if (condition == null) {
            throw new InvalidException("Value for condition cannot be null");
        }
        if (condition.startsWith("null")) {
            return;
        }
        lstCondition.add(new Criterion(condition));
    }

    protected void addCriterion(String condition, Object value, String property) {
        if (value == null) {
            if (notNull) {
                throw new InvalidException("Value for " + property + " cannot be null");
            } else {
                return;
            }
        }
        if (property == null) {
            return;
        }
        lstCondition.add(new Criterion(condition, value));
    }

    protected void addCriterion(String condition, Object value1, Object value2, String property) {
        if (value1 == null || value2 == null) {
            if (notNull) {
                throw new InvalidException("Between values for " + property + " cannot be null");
            } else {
                return;
            }
        }
        if (property == null) {
            return;
        }
        lstCondition.add(new Criterion(condition, value1, value2));
    }

    protected void addOrCriterion(String condition) {
        if (condition == null) {
            throw new InvalidException("Value for condition cannot be null");
        }
        if (condition.startsWith("null")) {
            return;
        }
        lstCondition.add(new Criterion(condition, true));
    }

    protected void addOrCriterion(String condition, Object value, String property) {
        if (value == null) {
            if (notNull) {
                throw new InvalidException("Value for " + property + " cannot be null");
            } else {
                return;
            }
        }
        if (property == null) {
            return;
        }
        lstCondition.add(new Criterion(condition, value, true));
    }

    protected void addOrCriterion(String condition, Object value1, Object value2, String property) {
        if (value1 == null || value2 == null) {
            if (notNull) {
                throw new InvalidException("Between values for " + property + " cannot be null");
            } else {
                return;
            }
        }
        if (property == null) {
            return;
        }
        lstCondition.add(new Criterion(condition, value1, value2, true));
    }

    public Criteria andIsNull(String property) {
        addCriterion(property + " is null");
        return (Criteria) this;
    }

    public Criteria andIsNotNull(String property) {
        addCriterion(property + " is not null");
        return (Criteria) this;
    }

    public Criteria andEqualTo(String property, Object value) {
        addCriterion(property + " =", value, property);
        return (Criteria) this;
    }

    public Criteria andNotEqualTo(String property, Object value) {
        addCriterion(property + " <>", value, property);
        return (Criteria) this;
    }

    public Criteria andGreaterThan(String property, Object value) {
        addCriterion(property + " >", value, property);
        return (Criteria) this;
    }

    public Criteria andGreaterThanOrEqualTo(String property, Object value) {
        addCriterion(property + " >=", value, property);
        return (Criteria) this;
    }

    public Criteria andLessThan(String property, Object value) {
        addCriterion(property + " <", value, property);
        return (Criteria) this;
    }

    public Criteria andLessThanOrEqualTo(String property, Object value) {
        addCriterion(property + " <=", value, property);
        return (Criteria) this;
    }

    public Criteria andIn(String property, Iterable values) {
        addCriterion(property + " in", values, property);
        return (Criteria) this;
    }

    public Criteria andNotIn(String property, Iterable values) {
        addCriterion(property + " not in", values, property);
        return (Criteria) this;
    }

    public Criteria andNotIn(String property, QueryParamDefinition queryParamDefinition) {
        addCriterion(property + " not in", queryParamDefinition, property);
        return (Criteria) this;
    }

    public Criteria andBetween(String property, Object value1, Object value2) {
        addCriterion(property + " between", value1, value2, property);
        return (Criteria) this;
    }

    public Criteria andNotBetween(String property, Object value1, Object value2) {
        addCriterion(property + " not between", value1, value2, property);
        return (Criteria) this;
    }

    public Criteria andLike(String property, String value) {
        addCriterion(property + "  like", value, property);
        return (Criteria) this;
    }

    public Criteria andNotLike(String property, String value) {
        addCriterion(property + "  not like", value, property);
        return (Criteria) this;
    }

    /**
     * 手写条件
     *
     * @param condition 例如 "length(countryname)<5"
     * @return
     */
    public Criteria andCondition(String condition) {
        addCriterion(condition);
        return (Criteria) this;
    }

    /**
     * 手写左边条件，右边用value值 ,可以是多个值
     *
     * @param condition 例如 "length(countryname)="   或  length(#{f1}) >#{f2})
     * @param value     例如 5  或 Map {f1:"dd",f2:5};
     * @return
     */
    public Criteria andCondition(String condition, Object value) {
        if (value instanceof Collections) {
            lstCondition.add(new Criterion(condition, value));
        } else {
            lstCondition.add(new Criterion(condition, value));
        }

        return (Criteria) this;
    }

    /**
     * 将此对象的不为空的字段参数作为相等查询条件
     *
     * @param param 参数对象
     * @author Bob {@link}0haizhu0@gmail.com
     * @date 2015年7月17日 下午12:48:08
     */
    public Criteria andEqualTo(Object param) {
        if (param == null) {
            return (Criteria) this;
        }
        MetaObject metaObject = SystemMetaObject.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            //属性和列对应Map中有此属性
            Object value = metaObject.getValue(property);
            //属性值不为空
            if (value != null) {
                andEqualTo(property, value);

            }
        }
        return (Criteria) this;
    }

    /**
     * 将此对象的所有字段参数作为相等查询条件，如果字段为 null，则为 is null
     *
     * @param param 参数对象
     */
    public Criteria andAllEqualTo(Object param) {
        MetaObject metaObject = SystemMetaObject.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            //属性和列对应Map中有此属
            Object value = metaObject.getValue(property);
            //属性值不为空
            if (value != null) {
                andEqualTo(property, value);
            } else {
                andIsNull(property);
            }

        }
        return (Criteria) this;
    }

    public Criteria orIsNull(String property) {
        addOrCriterion(property + " is null");
        return (Criteria) this;
    }

    public Criteria orIsNotNull(String property) {
        addOrCriterion(property + " is not null");
        return (Criteria) this;
    }

    public Criteria orEqualTo(String property, Object value) {
        addOrCriterion(property + " =", value, property);
        return (Criteria) this;
    }

    public Criteria orNotEqualTo(String property, Object value) {
        addOrCriterion(property + " <>", value, property);
        return (Criteria) this;
    }

    public Criteria orGreaterThan(String property, Object value) {
        addOrCriterion(property + " >", value, property);
        return (Criteria) this;
    }

    public Criteria orGreaterThanOrEqualTo(String property, Object value) {
        addOrCriterion(property + " >=", value, property);
        return (Criteria) this;
    }

    public Criteria orLessThan(String property, Object value) {
        addOrCriterion(property + " <", value, property);
        return (Criteria) this;
    }

    public Criteria orLessThanOrEqualTo(String property, Object value) {
        addOrCriterion(property + " <=", value, property);
        return (Criteria) this;
    }

    public Criteria orIn(String property, Iterable values) {
        addOrCriterion(property + " in", values, property);
        return (Criteria) this;
    }

    public Criteria orNotIn(String property, Iterable values) {
        addOrCriterion(property + " not in", values, property);
        return (Criteria) this;
    }

    public Criteria orBetween(String property, Object value1, Object value2) {
        addOrCriterion(property + " between", value1, value2, property);
        return (Criteria) this;
    }

    public Criteria orNotBetween(String property, Object value1, Object value2) {
        addOrCriterion(property + " not between", value1, value2, property);
        return (Criteria) this;
    }

    public Criteria orLike(String property, String value) {
        addOrCriterion(property + "  like", value, property);
        return (Criteria) this;
    }

    public Criteria orNotLike(String property, String value) {
        addOrCriterion(property + "  not like", value, property);
        return (Criteria) this;
    }

    /**
     * 手写条件
     *
     * @param condition 例如 "length(countryname)<5"
     * @return
     */
    public Criteria orCondition(String condition) {
        addOrCriterion(condition);
        return (Criteria) this;
    }

    /**
     * 手写左边条件，右边用value值
     *
     * @param condition 例如 "length(countryname)="
     * @param value     例如 5
     * @return
     */
    public Criteria orCondition(String condition, Object value) {
        lstCondition.add(new Criterion(condition, value, true));
        return (Criteria) this;
    }

    /**
     * 将此对象的不为空的字段参数作为相等查询条件
     *
     * @param param 参数对象
     * @author Bob {@link}0haizhu0@gmail.com
     * @date 2015年7月17日 下午12:48:08
     */
    public Criteria orEqualTo(Object param) {
        MetaObject metaObject = SystemMetaObject.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            //属性和列对应Map中有此属性
            Object value = metaObject.getValue(property);
            //属性值不为空
            if (value != null) {
                orEqualTo(property, value);
            }

        }
        return (Criteria) this;
    }

    /**
     * 将此对象的所有字段参数作为相等查询条件，如果字段为 null，则为 is null
     *
     * @param param 参数对象
     */
    public Criteria orAllEqualTo(Object param) {
        MetaObject metaObject = SystemMetaObject.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            //属性和列对应Map中有此属性
            Object value = metaObject.getValue(property);
            //属性值不为空
            if (value != null) {
                orEqualTo(property, value);
            } else {
                orIsNull(property);
            }

        }
        return (Criteria) this;
    }


    public String getAndOr() {
        return andOr;
    }

    public void setAndOr(String andOr) {
        this.andOr = andOr;
    }

    public boolean isValid() {
        return lstCondition.size() > 0;
    }

    @Override
    public String getSqlWhere(Map<String, Object> mapValue, String alias, int index, boolean needLogic) {
        if (lstCondition == null || lstCondition.isEmpty()) {
            return "";
        }
        return (needLogic ? " " + getAndOr() + " " : "") + "(" + makeSubWhere(mapValue, alias, index) + ")";
    }

    private String makeSubWhere(Map<String, Object> mapValue, String alias, int parIndex) {
        StringBuilder sb = new StringBuilder();
        /**
         * 下级的参数编号,后移二位
         */
        int startIndex = parIndex * 100;
        for (int index = 1; index <= lstCondition.size(); index++) {
            sb.append(lstCondition.get(index - 1).getSqlWhere(mapValue, alias, startIndex + index, index != 1));
        }
        return sb.toString();

    }

    /**
     * 是不是没有设置条件
     *
     * @return
     */
    public boolean isEmpty() {
        return this.lstCondition == null || lstCondition.isEmpty();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
