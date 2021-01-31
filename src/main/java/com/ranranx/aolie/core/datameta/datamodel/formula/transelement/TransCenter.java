package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.datameta.datamodel.Schema;

import java.util.Map;

/**
 * @Author xxl
 * @Description 翻译控制中心
 * @Date 2020/8/13 20:10
 * @Version V0.0.1
 **/
public interface TransCenter {
    /**
     * 翻译成中文
     *
     * @param curElement
     * @param transcenter
     * @return
     */
    String transToCn(String curElement, TransCenter transcenter);

    /**
     * 翻译成内部表达式(JS)
     *
     * @param curElement
     * @param schema
     * @param transcenter
     * @return
     */
    String transToInner(String curElement, Schema schema, TransCenter transcenter);

    /**
     * 翻译成值表达式,
     *
     * @param curElement
     * @param rowData
     * @param schema
     * @param transcenter
     * @return
     */
    String transToValue(String curElement, Map<String, Object> rowData, Schema schema, TransCenter transcenter);
}
