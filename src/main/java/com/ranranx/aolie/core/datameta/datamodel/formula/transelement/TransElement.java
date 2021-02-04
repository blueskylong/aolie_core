package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

/**
 * @author xxl
 *  翻译元素接口
 * @date 2020/8/13 20:10
 * @version V0.0.1
 **/
public interface TransElement extends TransCenter {
    /**
     * 是不是可以翻译的英文(JS)元素
     *
     * @param str
     * @return
     */
    boolean isMatchInner(String str);

    /**
     * 是不是可以翻译的中文元素
     *
     * @param str
     * @return
     */
    boolean isMatchCn(String str);

    /**
     * 翻译器的执行顺序,越小越靠前
     *
     * @return
     */
    int getOrder();

    /**
     * 取得元素的类型
     *
     * @return
     */
    int getElementType();

    /**
     * 取得元素名称
     *
     * @return
     */
    String getName();

    /**
     * 是否只适用过滤条件
     *
     * @return
     */
    boolean isOnlyForFilter();

    /**
     * 取得按钮显示内容,也可以用于错误中的提示
     *
     * @return
     */
    String getExpressionCN();
}
