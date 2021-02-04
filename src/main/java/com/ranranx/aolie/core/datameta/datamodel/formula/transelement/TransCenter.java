package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.datameta.datamodel.Formula;
import com.ranranx.aolie.core.datameta.datamodel.Schema;

import java.util.Map;

/**
 * @author xxl
 *  翻译控制中心
 * @date 2020/8/13 20:10
 * @version V0.0.1
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
     * 翻译成值表达式,支持跨表取数
     *
     * @param curElement
     * @param rowTableId  当前rowData的表ID
     * @param rowData
     * @param schema
     * @param transcenter
     * @param formula
     * @return
     */
    String transToValue(String curElement, long rowTableId, Map<String, Object> rowData,
                        Schema schema, TransCenter transcenter, Formula formula);
}
