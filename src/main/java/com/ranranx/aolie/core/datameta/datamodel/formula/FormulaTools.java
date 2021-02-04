package com.ranranx.aolie.core.datameta.datamodel.formula;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;

import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xxl
 *  公式工具类
 * @date 2020/8/13 20:10
 * @version V0.0.1
 **/
public class FormulaTools {


    static Pattern colReg = Pattern.compile("\\$\\{[^}]+\\}$");
    static Pattern colReg_g = Pattern.compile("\\$\\{(.+?)\\}");
    static Pattern paramReg = Pattern.compile("\\#\\{[^}]+\\}$");
    static Pattern paramReg_g = Pattern.compile("\\#\\{(.+?)\\}");

    /**
     * 取得列参数,形如${1}
     *
     * @param str
     */
    public static List<String> getColumnParams(String str) {
        if (str == null) {
            return new ArrayList<>();
        }
        return FormulaTools.getParam(str, FormulaTools.colReg_g);
    }

    /**
     * 检查字符串是不是列参数
     *
     * @param str
     */
    public static boolean isColumnParam(String str) {
        return FormulaTools.colReg.matcher(str).find();
    }

    public static boolean isSysParam(String str) {
        return FormulaTools.paramReg.matcher(str).find();
    }

    /**
     * 取得列ID,形如: 从${1} 中取出1
     *
     * @param param
     */
    public static String getParamInnerStr(String param) {
        return param.substring(2, param.length() - 1);
    }

    public static List<String> getParam(String str, Pattern regEx) {
        List<String> matchers = getMatchers(regEx, str);
        List<String> result = new ArrayList<>();
        if (matchers != null && !matchers.isEmpty()) {
            for (String item : matchers) {

                String innerParam = getParamInnerStr(item);
                if (result.indexOf(innerParam) == -1) {
                    result.add(innerParam);
                }
            }
        }
        return result;
    }

    public static List<String> getMatchers(Pattern pattern, String source) {
        Matcher matcher = pattern.matcher(source);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public static List<String> getSysParams(String str) {
        return FormulaTools.getParam(str, FormulaTools.paramReg_g);
    }

    /**
     * 替换列参数
     *
     * @param str
     * @param toReplace
     * @param replace
     */
    public static String replaceColumnNameStr(String str, String toReplace, String replace) {
        return str.replace("${" + toReplace + "}", "${" + replace + "}");
    }

    /**
     * 替换系统参数
     *
     * @param str
     * @param toReplace
     * @param replace
     */
    public static String replaceParamNameStr(String str, String toReplace, String replace) {
        return str.replace("#{" + toReplace + "}", "#{" + replace + "}");
    }

    /**
     * 替换列参数
     *
     * @param str
     * @param toReplace
     * @param fieldValue
     */
    public static String replaceColumnValueStr(String str, String toReplace,
                                               String fieldValue, String fieldType) {
        if (CommonUtils.isEmpty(fieldType) || fieldType.equals(DmConstants.FieldType.INT)
                || fieldType.equals(DmConstants.FieldType.DECIMAL)) {
            return str.replace("${" + toReplace + "}", fieldValue);
        } else {
            return str.replace("${" + toReplace + "}", "'" + fieldValue + "'");
        }

    }

    /**
     * 替换系统参数,直接替换成值的表达式,
     *
     * @param str
     * @param toReplace
     * @param value
     */
    public static String replaceParamValueStr(String str, String toReplace, String value, String fieldType) {

        if (CommonUtils.isEmpty(fieldType) || fieldType.equals(DmConstants.FieldType.INT)
                || fieldType.equals(DmConstants.FieldType.DECIMAL)) {
            return str.replace("#{" + toReplace + "}", value);
        } else {
            return str.replace("#{" + toReplace + "}", "'" + value + "'");
        }

    }

    //TODO 计算JS 表达式
    public static Object calcExpresion(String expresion) {
        try {
            return new ScriptEngineManager().getEngineByName("js").eval(expresion);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
