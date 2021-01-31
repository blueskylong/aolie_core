package com.ranranx.aolie.core.datameta.datamodel.validator;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;

import java.util.*;

/**
 * @Author xxl
 * @Description 验证中心, 每一张表, 可以共用一个实例, 线程安全
 * @Date 2020/8/13 20:10
 * @Version V0.0.1
 **/
public class ValidatorCenter {


    protected TableInfo tableInfo;

    protected Map<String, List<IValidator>> mapValidator;

    public ValidatorCenter(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public void setValidators(List<IValidator> lstValidator) {
        this.initFieldValidator(this.tableInfo, lstValidator);
    }

    protected void initFieldValidator(TableInfo tableInfo
            , List<IValidator> lstValidator) {
        if (tableInfo == null) {
            return;
        }
        this.tableInfo = tableInfo;
        this.mapValidator = new HashMap<>();
        List<IValidator> lstComValidator = new ArrayList<IValidator>();
        for (Column col : tableInfo.getLstColumn()) {
            lstComValidator = new ArrayList<>();
            for (IValidator validator : lstValidator) {
                if (validator.isConcerned(col, tableInfo)) {
                    lstComValidator.add(validator.getInstance(col, tableInfo));
                }
            }
            if (!lstComValidator.isEmpty()) {
                this.mapValidator.put(col.getColumnDto().getFieldName(),
                        lstComValidator);
            }
        }
    }


    /**
     * 一次性检查所有项目
     *
     * @param row
     * @return
     */
    public String validateAll(Map<String, Object> row) {
        if (this.mapValidator.isEmpty()) {
            return null;
        }
        Iterator<Map.Entry<String, List<IValidator>>> iterator = this.mapValidator.entrySet().iterator();
        String err;
        StringBuilder sb = new StringBuilder();
        String fieldName;
        List<IValidator> lstValidator;
        while (iterator.hasNext()) {
            Map.Entry<String, List<IValidator>> nextEntry = iterator.next();
            fieldName = nextEntry.getKey();
            lstValidator = nextEntry.getValue();
            err = validateField(fieldName, row.get(fieldName), row, tableInfo, lstValidator);
            if (CommonUtils.isNotEmpty(err)) {
                sb.append(err);
            }
        }
        return sb.toString();
    }

    public String validateField(String fieldName, Object value, Map<String, Object> row,
                                TableInfo tableInfo, List<IValidator> lstValidator) {
        String errs = "";
        for (IValidator validator : lstValidator) {
            String err = validator.validateField(fieldName, value, row, tableInfo);
            if (CommonUtils.isNotEmpty(err)) {
                errs += err + ";";
            }
        }
        return errs;
    }


}
