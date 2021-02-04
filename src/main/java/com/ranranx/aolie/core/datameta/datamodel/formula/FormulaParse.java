package com.ranranx.aolie.core.datameta.datamodel.formula;

import com.ranranx.aolie.core.datameta.datamodel.Formula;
import com.ranranx.aolie.core.datameta.datamodel.Schema;
import com.ranranx.aolie.core.datameta.datamodel.formula.transelement.TransCenter;
import com.ranranx.aolie.core.datameta.datamodel.formula.transelement.TransElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author xxl
 *  公式或条件解析器(目前只做翻译, 和部分的方法检查) ,容器管理的实例不可使用,只是为了接收元素注入
 * @date 2020/8/13 20:10
 * @version V0.0.1
 **/
@Component
public class FormulaParse implements TransCenter {
    private boolean isFilter;
    private Schema schema;
    private static final Map<String, FormulaParse> mapFormulaParse = new HashMap<>();

    /**
     * 全部的翻译器
     */
    static List<TransElement> arrElement;
    /**
     * 仅公式的翻译器
     */
    static List<TransElement> arrFormulaElement;

    public static void clearCache() {
        mapFormulaParse.clear();
    }

    /**
     * 取得实例,
     *
     * @param isFilter
     * @param schema
     * @return
     */
    public static FormulaParse getInstance(boolean isFilter, Schema schema) {
        String key = genKey(isFilter, schema);
        FormulaParse parse = mapFormulaParse.get(key);
        if (parse == null) {
            parse = new FormulaParse(isFilter, schema);
            mapFormulaParse.put(key, parse);
        }
        return parse;

    }

    private static String genKey(boolean isFilter, Schema schema) {
        return isFilter + "_" + schema.getSchemaDto().getSchemaId() + "_"
                + schema.getSchemaDto().getVersionCode();

    }

    FormulaParse(boolean isFilter, Schema schema) {
        this.isFilter = isFilter;
        this.schema = schema;
    }

    /**
     * 引构造函数,只为了接收容器的元素注入,不要手动调用
     * @param lstElement
     */
    @Autowired
    public FormulaParse(List<TransElement> lstElement) {
        lstElement.sort((TransElement a, TransElement b) -> {
            return a.getOrder() >= b.getOrder() ? 1 : -1;
        });
        arrElement = lstElement;
        arrFormulaElement = new ArrayList<>();
        for (TransElement element : arrElement) {
            if (!element.isOnlyForFilter()) {
                arrFormulaElement.add(element);
            }
        }
    }

    /**
     * 注册公式翻译元素
     *
     * @param ele
     */
    public static void regTransElement(TransElement ele) {
        FormulaParse.arrElement.add(ele);
        //排序
        FormulaParse.arrElement.sort((TransElement a, TransElement b) -> {
            if (a.getOrder() >= b.getOrder()) {
                return 1;
            } else {
                return -1;
            }
        });
        if (!ele.isOnlyForFilter()) {
            FormulaParse.arrFormulaElement.add(ele);
        }
        FormulaParse.arrFormulaElement.sort((TransElement a, TransElement b) -> {
            if (a.getOrder() >= b.getOrder()) {
                return 1;
            } else {
                return -1;
            }
        });

    }

    static List<TransElement> getTransElements() {
        return FormulaParse.arrElement;
    }

    @Override
    public String transToCn(String curElement, TransCenter transcenter) {
        for (TransElement transElement : this.getTranslator()) {
            if (transElement.isMatchInner(curElement)) {
                return transElement.transToCn(curElement, this);
            }
        }
        return curElement;
    }

    @Override
    public String transToInner(String curElement, Schema schema, TransCenter transcenter) {
        if (schema == null) {
            schema = this.schema;
        }
        for (TransElement transElement : this.getTranslator()) {
            if (transElement.isMatchCn(curElement)) {
                return transElement.transToInner(curElement, schema, this);
            }
        }
        return curElement;
    }

    private List<TransElement> getTranslator() {
        if (!this.isFilter) {
            return FormulaParse.arrFormulaElement;
        }
        return FormulaParse.arrElement;

    }


    @Override
    public String transToValue(String curElement, long rowTableId, Map<String, Object> rowData,
                               Schema schema, TransCenter transcenter, Formula formula) {
        if (schema == null) {
            schema = this.schema;
        }
        for (TransElement transElement : this.getTranslator()) {
            if (transElement.isMatchInner(curElement)) {
                return transElement.transToValue(curElement, rowTableId, rowData, schema, this, formula);
            }
        }
        return curElement;
    }

}

