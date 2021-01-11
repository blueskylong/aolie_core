package com.ranranx.aolie.core.ds.definition;

import com.ranranx.aolie.core.common.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/7 15:30
 * @Version V0.0.1
 **/
public class InsertParamDefinition {

    /**
     * 插入的表名
     */
    private String tableName;

    /**
     * 需要插入的信息
     */
    private List<Map<String, Object>> lstRows;
    /**
     * 是否需要转换成下划线
     */
    private boolean needConvertToUnderLine = false;

    /**
     * 直接语句.暂时提供在复杂语句下使用.
     */
    private SqlExp sqlExp;

    public SqlExp getSqlExp() {
        return sqlExp;
    }

    public void setSqlExp(SqlExp sqlExp) {
        this.sqlExp = sqlExp;
    }

    public boolean isNeedConvertToUnderLine() {
        return needConvertToUnderLine;
    }

    public void setNeedConvertToUnderLine(boolean needConvertToUnderLine) {
        this.needConvertToUnderLine = needConvertToUnderLine;
    }

    public void setObjects(List<?> lstObj) {
        if (CommonUtils.isEmpty(tableName) && lstObj != null && !lstObj.isEmpty()) {
            this.tableName = CommonUtils.getTableName(lstObj.get(0).getClass());
        }
        this.needConvertToUnderLine = true;
        this.lstRows = CommonUtils.toMapAndConvertToUnderLine(lstObj);
    }

    public void setObject(Object obj) {
        if (CommonUtils.isEmpty(tableName) && obj != null) {
            this.tableName = CommonUtils.getTableName(obj.getClass());
        }
        this.needConvertToUnderLine = true;
        this.lstRows = new ArrayList<>();
        this.lstRows.add(CommonUtils.toMap(obj, needConvertToUnderLine));
    }

    public List<Map<String, Object>> getLstRows() {
        return lstRows;
    }

    public void setLstRows(List<Map<String, Object>> lstRows) {
        this.lstRows = lstRows;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTableDto(Class clazz) {
        tableName = CommonUtils.getTableName(clazz);
    }
}
