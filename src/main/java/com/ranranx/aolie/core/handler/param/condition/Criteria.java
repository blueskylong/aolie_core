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

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.handler.param.condition.express.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * 此设计复制于tk.mybatis插件, 并做修改
 * @version V0.0.1
 * @date 2020/8/14 15:38
 **/
public class Criteria implements ICondition {

    protected List<ICondition> lstCondition = new ArrayList<>();
    //值是否不能为空
    protected boolean notNull;
    //连接条件
    protected String andOr;


    private static Criteria alwaysFalse = new Criteria();


    static {
        alwaysFalse.andCustomCondition(null, null, "1=2");
    }

    public Criteria(boolean notNull) {
        super();
        this.notNull = notNull;
        lstCondition = new ArrayList<>();
        andOr = AND;
    }

    public static Criteria getFalseExpression() {
        return alwaysFalse;
    }

    public Criteria() {
        this(false);
    }

    public Criteria createSubOrCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAndOr("or");
        lstCondition.add(criteria);
        return criteria;
    }

    public Criteria addSubOrCriteria(Criteria criteriaIn) {
        Criteria criteria = criteriaIn;
        criteria.setAndOr(OR);
        lstCondition.add(criteria);
        return criteria;
    }

    public Criteria addSubAndCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAndOr(AND);
        lstCondition.add(criteria);
        return criteria;
    }

    public Criteria createSubAndCriteria(Criteria criteriaIn) {
        Criteria criteria = criteriaIn;
        criteria.setAndOr(AND);
        lstCondition.add(criteria);
        return criteria;
    }

    public Criteria or(Criteria criteriaIn) {
        Criteria criteria = criteriaIn;
        criteria.setAndOr(OR);
        lstCondition.add(criteria);
        return this;
    }

    public Criteria and(Criteria criteriaIn) {
        Criteria criteria = criteriaIn;
        criteria.setAndOr(AND);
        lstCondition.add(criteria);
        return this;
    }


    /**
     * 增加自定义条件
     *
     * @param tableName
     * @param fieldName
     * @param condition
     * @param values
     */
    protected void addCustomCondition(String tableName, String fieldName, String condition, Map<String, Object> values) {
        CustomCondition customCondition = new CustomCondition(tableName, fieldName, condition, values);
        lstCondition.add(customCondition);
    }

//    protected void addCriterion(String condition, Object value, String property) {
//        if (value == null) {
//            if (notNull) {
//                throw new InvalidException("Value for " + property + " cannot be null");
//            } else {
//                return;
//            }
//        }
//        if (property == null) {
//            return;
//        }
//        lstCondition.add(new Criterion(condition, value));
//    }
//
//    protected void addCriterion(String condition, Object value1, Object value2, String property) {
//        if (value1 == null || value2 == null) {
//            if (notNull) {
//                throw new InvalidException("Between values for " + property + " cannot be null");
//            } else {
//                return;
//            }
//        }
//        if (property == null) {
//            return;
//        }
//        lstCondition.add(new Criterion(condition, value1, value2));
//    }
//
//    protected void addOrCriterion(String condition) {
//        if (condition == null) {
//            throw new InvalidException("Value for condition cannot be null");
//        }
//        if (condition.startsWith("null")) {
//            return;
//        }
//        lstCondition.add(new Criterion(condition, true));
//    }
//
//    protected void addOrCriterion(String condition, Object value, String property) {
//        if (value == null) {
//            if (notNull) {
//                throw new InvalidException("Value for " + property + " cannot be null");
//            } else {
//                return;
//            }
//        }
//        if (property == null) {
//            return;
//        }
//        lstCondition.add(new Criterion(condition, value, true));
//    }
//
//    protected void addOrCriterion(String condition, Object value1, Object value2, String property) {
//        if (value1 == null || value2 == null) {
//            if (notNull) {
//                throw new InvalidException("Between values for " + property + " cannot be null");
//            } else {
//                return;
//            }
//        }
//        if (property == null) {
//            return;
//        }
//        lstCondition.add(new Criterion(condition, value1, value2, true));
//    }

    public Criteria andIsNull(String tableName, String fieldName) {
        this.lstCondition.add(new IsNull(tableName, fieldName));
        return this;
    }

    public Criteria andIsNotNull(String tableName, String fieldName) {
        lstCondition.add(new IsNotNull(tableName, fieldName));
        return this;
    }

    public Criteria andEqualTo(String tableName, String fieldName, Object value) {
        lstCondition.add(new Equals(tableName, fieldName, value));
        return this;
    }

    public Criteria andNotEqualTo(String tableName, String fieldName, Object value) {
        lstCondition.add(new NotEquals(tableName, fieldName, value));
        return this;
    }

    public Criteria andGreaterThan(String tableName, String fieldName, Object value) {
        lstCondition.add(new GreaterThan(tableName, fieldName, value));
        return this;
    }

    public Criteria andGreaterThanOrEqualTo(String tableName, String fieldName, Object value) {
        lstCondition.add(new GreaterEqualsThan(tableName, fieldName, value));
        return this;
    }

    public Criteria andLessThan(String tableName, String fieldName, Object value) {
        lstCondition.add(new LessThan(tableName, fieldName, value));
        return this;
    }

    public Criteria andLessThanOrEqualTo(String tableName, String fieldName, Object value) {
        lstCondition.add(new LessEqualsThan(tableName, fieldName, value));
        return this;
    }

    public Criteria andIn(String tableName, String fieldName, List value) {
        lstCondition.add(new In(tableName, fieldName, value));
        return this;
    }

    public Criteria andNotIn(String tableName, String fieldName, List value) {
        lstCondition.add(new NotIn(tableName, fieldName, value));
        return this;
    }

    public Criteria andNotExists(QueryParam value) {
        lstCondition.add(new NotExists(value));
        return this;
    }

    public Criteria andExists(QueryParam param) {
        lstCondition.add(new Exists(param));
        return this;
    }

    public Criteria andBetween(String tableName, String fieldName, Object value1, Object value2) {
        lstCondition.add(new Between(tableName, fieldName, value1, value2));
        return this;
    }

    public Criteria andNotBetween(String tableName, String fieldName, Object value1, Object value2) {
        lstCondition.add(new NotBetween(tableName, fieldName, value1, value2));
        return this;
    }

    public Criteria andInclude(String tableName, String fieldName, String value) {
        lstCondition.add(new Include(tableName, fieldName, value));
        return this;
    }

    public Criteria andStartWith(String tableName, String fieldName, String value) {
        lstCondition.add(new StartWith(tableName, fieldName, value));
        return this;
    }

    public Criteria andEndWith(String tableName, String fieldName, String value) {
        lstCondition.add(new EndWith(tableName, fieldName, value));
        return this;
    }

    public Criteria andNotStartWith(String tableName, String fieldName, String value) {
        lstCondition.add(new NotStartWith(tableName, fieldName, value));
        return this;
    }

    public Criteria andNotEndWith(String tableName, String fieldName, String value) {
        lstCondition.add(new NotEndWith(tableName, fieldName, value));
        return this;
    }


    public Criteria andExclude(String tableName, String fieldName, String value) {
        lstCondition.add(new Exclude(tableName, fieldName, value));
        return this;
    }

    /**
     * 手写条件
     *
     * @param condition 例如 "length(countryname)<5"
     * @return
     */
    public Criteria andCustomCondition(String tableName, String fieldName, String condition) {
        lstCondition.add(new CustomCondition(tableName, fieldName, condition, null));
        return this;
    }

    /**
     * 手写左边条件，右边用value值 ,可以是多个值
     *
     * @param condition 例如 "length(countryname)="   或  length(#{f1}) >#{f2})
     * @param params    例如 5  或 Map {f1:"dd",f2:5};
     * @return
     */
    public Criteria andCustomCondition(String tableName, String fieldName, String condition, Map<String, Object> params) {
        lstCondition.add(new CustomCondition(tableName, fieldName, condition, params));
        return this;
    }

    /**
     * 将此对象的不为空的字段参数作为相等查询条件
     *
     * @param param 参数对象
     * @author Bob {@link}0haizhu0@gmail.com
     * @date 2015年7月17日 下午12:48:08
     */
    public Criteria andEqualToDto(String tableName, Object param) {
        if (param == null) {
            return this;
        }
        MetaObject metaObject = SystemMetaObject.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            //属性和列对应Map中有此属性
            Object value = metaObject.getValue(property);
            //属性值不为空
            if (value != null) {

                andEqualTo(tableName, CommonUtils.convertToUnderline(property), value);
            }
        }
        return this;
    }

    /**
     * 将此对象的所有字段参数作为相等查询条件，如果字段为 null，则为 is null
     *
     * @param param 参数对象
     */
    public Criteria andAllEqualToDto(String tableName, Object param) {
        MetaObject metaObject = SystemMetaObject.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            //属性和列对应Map中有此属
            Object value = metaObject.getValue(property);
            //属性值不为空
            if (value != null) {
                andEqualTo(tableName, CommonUtils.convertToUnderline(property), value);
            } else {
                andIsNull(tableName, CommonUtils.convertToUnderline(property));
            }

        }
        return this;
    }

    //-----------------------------------------------
    public Criteria orIsNull(String tableName, String fieldName) {
        this.lstCondition.add(new IsNull(tableName, fieldName).setIsOr(true));
        return this;
    }

    public Criteria orIsNotNull(String tableName, String fieldName) {
        lstCondition.add(new IsNotNull(tableName, fieldName).setIsOr(true));
        return this;
    }

    public Criteria orEqualTo(String tableName, String fieldName, Object value) {
        lstCondition.add(new Equals(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orNotEqualTo(String tableName, String fieldName, Object value) {
        lstCondition.add(new NotEquals(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orGreaterThan(String tableName, String fieldName, Object value) {
        lstCondition.add(new GreaterThan(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orGreaterThanOrEqualTo(String tableName, String fieldName, Object value) {
        lstCondition.add(new GreaterEqualsThan(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orLessThan(String tableName, String fieldName, Object value) {
        lstCondition.add(new LessThan(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orLessThanOrEqualTo(String tableName, String fieldName, Object value) {
        lstCondition.add(new LessEqualsThan(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orIn(String tableName, String fieldName, List value) {
        lstCondition.add(new In(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orNotIn(String tableName, String fieldName, List value) {
        lstCondition.add(new NotIn(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orNotExists(QueryParam value) {
        lstCondition.add(new NotExists(value).setIsOr(true));
        return this;
    }

    public Criteria orBetween(String tableName, String fieldName, Object value1, Object value2) {
        lstCondition.add(new Between(tableName, fieldName, value1, value2).setIsOr(true));
        return this;
    }

    public Criteria orNotBetween(String tableName, String fieldName, Object value1, Object value2) {
        lstCondition.add(new NotBetween(tableName, fieldName, value1, value2).setIsOr(true));
        return this;
    }

    public Criteria orInclude(String tableName, String fieldName, String value) {
        lstCondition.add(new Include(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orStartWith(String tableName, String fieldName, String value) {
        lstCondition.add(new StartWith(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orEndWith(String tableName, String fieldName, String value) {
        lstCondition.add(new EndWith(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orNotStartWith(String tableName, String fieldName, String value) {
        lstCondition.add(new NotStartWith(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orNotEndWith(String tableName, String fieldName, String value) {
        lstCondition.add(new NotEndWith(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    public Criteria orExclude(String tableName, String fieldName, String value) {
        lstCondition.add(new Exclude(tableName, fieldName, value).setIsOr(true));
        return this;
    }

    /**
     * 手写条件
     *
     * @param condition 例如 "length(countryname)<5"
     * @return
     */
    public Criteria orCustomCondition(String tableName, String fieldName, String condition) {
        lstCondition.add(new CustomCondition(tableName, fieldName, condition, null).setIsOr(true));
        return this;
    }

    /**
     * 手写左边条件，右边用value值 ,可以是多个值
     *
     * @param condition 例如 "length(countryname)="   或  length(#{f1}) >#{f2})
     * @param params    例如 5  或 Map {f1:"dd",f2:5};
     * @return
     */
    public Criteria orCustomCondition(String tableName, String fieldName, String condition, Map<String, Object> params) {
        lstCondition.add(new CustomCondition(tableName, fieldName, condition, params).setIsOr(true));
        return this;
    }

    /**
     * 将此对象的不为空的字段参数作为相等查询条件
     *
     * @param param 参数对象
     * @author Bob {@link}0haizhu0@gmail.com
     * @date 2015年7月17日 下午12:48:08
     */
    public Criteria orEqualToDto(String tableName, Object param) {
        if (param == null) {
            return this;
        }
        MetaObject metaObject = SystemMetaObject.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            //属性和列对应Map中有此属性
            Object value = metaObject.getValue(property);
            //属性值不为空
            if (value != null) {
                andEqualTo(tableName, property, value);

            }
        }
        return this;
    }

    /**
     * 将此对象的所有字段参数作为相等查询条件，如果字段为 null，则为 is null
     *
     * @param param 参数对象
     */
    public Criteria orAllEqualToDto(String tableName, Object param) {
        MetaObject metaObject = SystemMetaObject.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            //属性和列对应Map中有此属
            Object value = metaObject.getValue(property);
            //属性值不为空
            if (value != null) {
                andEqualTo(tableName,  CommonUtils.convertToUnderline(property), value);
            } else {
                andIsNull(tableName,  CommonUtils.convertToUnderline(property));
            }

        }
        return this;
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
    public String getSqlWhere(Map<String, Object> mapValue, Map<String, String> alias, int index, boolean needLogic) {
        if (lstCondition == null || lstCondition.isEmpty()) {
            return "";
        }
        return (needLogic ? " " + getAndOr() + " " : "") + "(" + makeSubWhere(mapValue, alias, index) + ")";
    }

    private String makeSubWhere(Map<String, Object> mapValue, Map<String, String> alias, int parIndex) {
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


    public Criteria orExists(QueryParam param) {
        lstCondition.add(new Exists(param).setIsOr(true));
        return this;
    }

    /**
     * 是不是没有设置条件
     *
     * @return
     */
    public boolean isEmpty() {
        return this.lstCondition == null || lstCondition.isEmpty();
    }

}
