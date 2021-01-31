package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.datameta.datamodel.Schema;

@FormulaElementTranslator
public class IncludeWith extends AbstractTransElement {

    protected String elementCN = "含有(*)";

    protected String startStr = ".indexOf(";
    protected String startStrCN = "含有(";
    protected String endStr = ")!==-1";
    protected String endStrCN = ")";

    @Override
    public int getOrder() {
        return this.getElementType() + 51;
    }

    @Override
    public String getExpressionCN() {
        return "含有()";
    }

    @Override
    public int getElementType() {
        return DmConstants.FormulaElementType.compare;
    }

    @Override
    public boolean isMatchCn(String str) {
        return str.indexOf(this.startStrCN) != -1 && str.endsWith(this.endStrCN);
    }

    @Override
    public boolean isMatchInner(String str) {
        if (str.indexOf(this.startStr) == -1) {
            return false;
        }
        return (str.trim().endsWith(this.endStr));
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
        int indexPos = curElement.indexOf(this.startStr);
        int endPos = curElement.indexOf((this.endStr));
        //前面部分
        String pre = curElement.substring(0, indexPos);
        String preStr = transcenter.transToCn(pre, transcenter);
        //中间部门,也需要中心处理
        String innerParam = curElement.substring(indexPos + this.startStr.length(),
                endPos - indexPos + this.startStr.length());
        String innerParamStr = transcenter.transToCn(innerParam, transcenter);
        return preStr + this.startStrCN + innerParamStr + this.endStrCN;
    }

    /**
     * ${字段1} 以("我")开头  =>  ${f1}.indexOf("我")===0
     *
     * @param curElement
     * @param transcenter
     */
    @Override
    public String transToInner(String curElement, Schema schema, TransCenter transcenter) {
        //前面部分,可以变量,也可能是常量,需要中心去处理
        int indexPos = curElement.indexOf(this.startStrCN);
        int endPos = curElement.indexOf((this.endStrCN));
        //前面部分
        String pre = curElement.substring(0, indexPos);
        String preStr = transcenter.transToInner(pre, schema, transcenter);
        //中间部门,也需要中心处理
        String innerParam = curElement.substring(indexPos + this.startStr.length(),
                endPos - indexPos + this.startStr.length());
        String innerParamStr = transcenter.transToInner(innerParam, schema, transcenter);
        return preStr + this.startStr + innerParamStr + this.endStr;
    }
}

