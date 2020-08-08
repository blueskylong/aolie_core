package com.ranranx.aolie.ds.definition;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/7 15:18
 * @Version V0.0.1
 **/
public class FieldOrder {
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
    private int index;

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
