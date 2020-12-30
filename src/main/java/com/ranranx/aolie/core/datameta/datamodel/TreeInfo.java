package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.common.CommonUtils;

/**
 * @Author xxl
 * @Description 树的结构信息
 * @Date 2020/12/19 22:04
 * @Version V0.0.1
 **/
public class TreeInfo {
    private Column idField;
    private Column codeField;
    private Column textField;
    private Column parentField;

    public Column getIdField() {
        return idField;
    }

    public void setIdField(Column idField) {
        this.idField = idField;
    }

    public Column getCodeField() {
        return codeField;
    }

    public void setCodeField(Column codeField) {
        this.codeField = codeField;
    }

    public Column getTextField() {
        return textField;
    }

    public void setTextField(Column textField) {
        this.textField = textField;
    }

    public Column getParentField() {
        return parentField;
    }

    public void setParentField(Column parentField) {
        this.parentField = parentField;
    }

    public boolean isValid() {
        return (CommonUtils.isNotEmpty(idField) && CommonUtils.isNotEmpty(textField)
                && (CommonUtils.isNotEmpty(codeField) || CommonUtils.isNotEmpty(parentField)));
    }
}
