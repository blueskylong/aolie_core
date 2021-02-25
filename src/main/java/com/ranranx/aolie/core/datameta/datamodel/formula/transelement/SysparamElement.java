package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.common.SystemParam;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.datameta.datamodel.Formula;
import com.ranranx.aolie.core.datameta.datamodel.Schema;
import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaTools;

import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * 系统元素翻译
 * @version V0.0.1
 * @date 2021/1/28 15:10
 **/
@FormulaElementTranslator
public class SysparamElement implements TransElement {

    @Override
    public int getElementType() {
        return DmConstants.FormulaElementType.sysparam;
    }

    @Override
    public String getName() {
        return "系统参数";
    }

    @Override
    public String getExpressionCN() {
        return this.getName();
    }

    @Override
    public int getOrder() {
        return this.getElementType();
    }

    @Override
    public boolean isMatchCn(String str) {
        return FormulaTools.isSysParam(str.trim());
    }

    @Override
    public boolean isMatchInner(String str) {
        return FormulaTools.isSysParam(str.trim());
    }

    @Override
    public String transToCn(String curElement, TransCenter transcenter) {
        System.out.println(this.getName() + "  matched!");
        List<String> sysParams = FormulaTools.getSysParams(curElement);
        if (sysParams != null && sysParams.size() > 0) {
            for (String paramExp : sysParams) {
                SystemParam paramInfo = SessionUtils.getParamInfo(paramExp);
                curElement = FormulaTools.replaceParamNameStr(curElement, paramExp, paramInfo.getName());
            }
        }
        return curElement;
    }

    @Override
    public String transToInner(String curElement, Schema schema, TransCenter transcenter) {
        System.out.println(this.getName() + "  matched!");
        List<String> sysParams = FormulaTools.getSysParams(curElement);
        String version = schema.getSchemaDto().getVersionCode();
        if (sysParams != null && sysParams.size() > 0) {
            for (String paramName : sysParams) {
                SystemParam paramInfo = SessionUtils.getParamInfoByName(paramName, version);
                curElement = FormulaTools.replaceParamNameStr(curElement, paramName,
                        paramInfo.getId() + "");
            }
        }
        return curElement;
    }

    @Override
    public String transToValue(String curElement, long tableId, Map<String, Object> rowData,
                               Schema schema, TransCenter transcenter, Formula formula) {
        List<String> sysParams = FormulaTools.getSysParams(curElement);
        if (sysParams != null && sysParams.size() > 0) {
            for (String paramExp : sysParams) {
                SystemParam paramInfo = SessionUtils.getParamInfo(paramExp);
                Object value = "";
                if (paramInfo != null) {
                    if (!DmConstants.FieldType.INT.equals(paramInfo.getDataType())
                            && !DmConstants.FieldType.DECIMAL.equals(paramInfo.getDataType())) {
                        value = "'" + paramInfo.getValue() + "'";
                    } else {
                        value = paramInfo.getValue();
                    }
                }
                curElement = FormulaTools.replaceParamValueStr(curElement, paramExp, value.toString(), null);
            }
        }
        return curElement;
    }

    @Override
    public boolean isOnlyForFilter() {
        return false;
    }

}
