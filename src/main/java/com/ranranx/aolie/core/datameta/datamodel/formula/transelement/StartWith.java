package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

/**
 * @author xxl
 *  系统元素翻译
 * @date 2021/1/28 15:10
 * @version V0.0.1
 **/
@FormulaElementTranslator
public class StartWith extends IncludeWith {

  

    public StartWith(){
         elementCN = "以(*)开始";

         startStr = ".startsWith(";
         startStrCN = "以(";
         endStr = ")";
         endStrCN = ")开始";
    }
    @Override
    public int getOrder() {
        return this.getElementType() + 16;
    }

    @Override
    public String getExpressionCN() {
        return "以()开始";
    }

    // getElementType(): number {
    //     return DmConstants.FormulaElementType.compare;
    // }
    //
    // isMatchCn(str): boolean {
    //     return str.indexOf(this.startStrCN) !== -1 && str.endsWith(this.endStrCN);
    // }
    //
    // isMatchInner(str): boolean {
    //     if (str.indexOf(this.startStr) === -1) {
    //         return false;
    //     }
    //     return (str.trim().endsWith(this.endStr));
    // }
    //
    // /**
    //  *   ${f1}.indexOf("我")===0  =>  ${字段1} 以("我")开头
    //  * @param curElement
    //  * @param preElement
    //  * @param transcenter
    //  */
    // transToCn(curElement, preElement, transcenter?: TransCenter): string {
    //     //前面部分,可以变量,也可能是常量,需要中心去处理
    //     let indexPos = curElement.indexOf(this.startStr);
    //     let endPos = curElement.indexOf((this.endStr));
    //     //前面部分
    //     let pre = curElement.substr(0, indexPos);
    //     let preStr = transcenter.transToCn(pre, null, transcenter);
    //     //中间部门,也需要中心处理
    //     let innerParam = curElement.substr(indexPos + this.startStr.length,
    //         endPos - indexPos + this.startStr.length);
    //     let innerParamStr = transcenter.transToCn(innerParam, null, transcenter);
    //     return preStr + this.startStrCN + innerParamStr + this.endStrCN;
    // }
    //
    // /**
    //  * ${字段1} 以("我")开头  =>  ${f1}.indexOf("我")===0
    //  * @param curElement
    //  * @param preElement
    //  * @param transcenter
    //  */
    // transToInner(curElement, preElement, transcenter?: TransCenter): string {
    //     //前面部分,可以变量,也可能是常量,需要中心去处理
    //     let indexPos = curElement.indexOf(this.startStrCN);
    //     let endPos = curElement.indexOf((this.endStrCN));
    //     //前面部分
    //     let pre = curElement.substr(0, indexPos);
    //     let preStr = transcenter.transToInner(pre, null, transcenter);
    //     //中间部门,也需要中心处理
    //     let innerParam = curElement.substr(indexPos + this.startStr.length,
    //         endPos - indexPos + this.startStr.length);
    //     let innerParamStr = transcenter.transToInner(innerParam, null, transcenter);
    //     return preStr + this.startStr + innerParamStr + this.endStr;
    // }


}
