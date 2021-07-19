package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.datameta.datamodel.Formula;
import com.ranranx.aolie.core.datameta.datamodel.Schema;
import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaTools;
import com.ranranx.aolie.core.exceptions.IllegalOperatorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 分组函数
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/14 0014 15:13
 **/
public abstract class BaseGroupElement extends AbstractTransElement {

    public static final Pattern PATTEN_GROUP_PARAM
            = Pattern.compile(FormulaTools.GROUP_PARAM_FIX + "[^#]*+" + FormulaTools.GROUP_PARAM_FIX);


    @Override
    public String getExpressionCN() {
        return getName();
    }

    @Override
    public int getOrder() {
        return 1;
    }

    protected String elementCN = "加和(*)";

    protected String startStr = "sum(";
    protected String startStrCN = "加和(";
    protected String endStr = ")";
    protected String endStrCN = ")";


    @Override
    public int getElementType() {
        return DmConstants.FormulaElementType.group;
    }

    @Override
    public boolean isMatchCn(String str) {
        String strTrim = str.trim();
        return strTrim.startsWith(startStrCN) && strTrim.endsWith(this.endStrCN);
    }

    @Override
    public boolean isMatchInner(String str) {
        String strTrim = str.trim();
        return strTrim.startsWith(startStr) && strTrim.endsWith(this.endStr);
    }

    /**
     * ${f1}.indexOf("我")===0  =>  ${字段1} 以("我")开头
     *
     * @param curElement
     * @param transcenter
     */
    @Override
    public String transToCn(String curElement, TransCenter transcenter) {
        //前面部分,可以变量,也可能是常量,需要中心去处理
        String innerParam = getEnInnerStr(curElement);
        String innerParamStr = transcenter.transToCn(innerParam, transcenter);
        return this.startStrCN + innerParamStr + this.endStrCN;
    }

    /**
     * ${字段1} 以("我")开头  =>  ${f1}.indexOf("我")===0
     *
     * @param curElement
     * @param transcenter
     */
    @Override
    public String transToInner(String curElement, Schema schema, TransCenter transcenter) {
        //中间部门,也需要中心处理
        String innerParam = curElement.substring(this.startStrCN.length(),
                curElement.length() - 1);
        String innerParamStr = transcenter.transToInner(innerParam, schema, transcenter);
        return this.startStr + innerParamStr + this.endStr;
    }

    /**
     * 翻译成值表达式,
     * 当前的表达式形式为 sum(${table1.field1}+3)
     * 因为有数值,所以这里直接返回计算值
     *
     * @param curElement
     * @param rowData
     * @param schema
     * @param transcenter
     */
    @Override
    public String transToValue(String curElement, long rowTableId, Map<String, Object> rowData,
                               Schema schema, TransCenter transcenter, Formula formula, Map<String, List<Object>> mapGroup) {
        //中间部门,也需要中心处理
        String innerParam = getEnInnerStr(curElement);
        String innerParamStr = transcenter.transToValue(innerParam, rowTableId, rowData, schema, transcenter, formula, mapGroup);
        if (CommonUtils.isEmpty(innerParam)) {
            return curElement;
        }
        //开始计算分组信息
        return calcGroupField(calcAllRowFormula(innerParamStr, mapGroup));
    }

    /**
     * 取得中间表达式
     *
     * @param curElement
     * @return
     */
    private String getEnInnerStr(String curElement) {
        int indexPos = curElement.indexOf(this.startStr);
        return curElement.substring(indexPos + this.startStr.length(),
                curElement.length() - this.endStr.length());

    }

    /**
     * 计算第一行的数值
     *
     * @param innerParamStr ####12321321#####+3+####3334####
     * @param mapGroup      key:####12321321##### list:[2,43]
     * @return
     */
    protected List<Object> calcAllRowFormula(String innerParamStr, Map<String, List<Object>> mapGroup) {
        List<String> lstParam = FormulaTools.getParam(innerParamStr, PATTEN_GROUP_PARAM, false);
        List<List<Object>> lstValues = new ArrayList<>();
        //拿到所有的值
        if (lstParam == null || lstParam.isEmpty()) {
            //如果没有参数，则只可能是一常量值
            return Arrays.asList(FormulaTools.calcExpresion(innerParamStr));
        }
        int size = 0;
        for (String param : lstParam) {
            List<Object> lstValue = mapGroup.remove(param);
            if (size != 0 && lstValue.size() != size) {
                //这种情况应该不会出现
                throw new IllegalOperatorException("分组内部的数据不一致");
            }
            size = lstValue.size();
            lstValues.add(lstValue);
        }
        //循环计算每一行的值
        int iParamCount = lstParam.size();
        List<Object> lstResult = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String sFormula = innerParamStr;
            for (int j = 0; j < iParamCount; j++) {
                sFormula = sFormula.replaceAll(lstParam.get(j), lstValues.get(j).get(i).toString());
            }
            lstResult.add(FormulaTools.calcExpresion(sFormula));
        }
        return lstResult;
    }

    @Override
    public boolean isOnlyForFilter() {
        return false;
    }

    abstract String calcGroupField(List<Object> lstObj);
}
