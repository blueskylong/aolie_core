package com.ranranx.aolie.ds.definition;

import com.ranranx.aolie.common.Ordered;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/7 15:18
 * @Version V0.0.1
 **/
public class FieldOrder implements Ordered {
    /**
     * 字段名
     */
    private String field;
    /**
     * 是否升序
     */
    private boolean isAsc;
    /**
     * 顺序
     */
    private int order;

    public boolean isAsc() {
        return isAsc;
    }

    public void setAsc(boolean asc) {
        isAsc = asc;
    }

    public FieldOrder() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * 取得
     *
     * @return
     */
    public String getOrderExp() {
        return field + (isAsc ? " asc " : " desc ");
    }
}
