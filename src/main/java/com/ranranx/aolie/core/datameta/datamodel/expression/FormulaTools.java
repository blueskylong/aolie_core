package com.ranranx.aolie.core.datameta.datamodel.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xxl
 *
 * @date 2020/12/28 0028 9:31
 * @version V0.0.1
 **/
public class FormulaTools {
    static final AtomicInteger ser = new AtomicInteger(1);
    static final Pattern colRegPatten = Pattern.compile("\\$\\{(.+?)\\}");
    static final Pattern paramRegPatten = Pattern.compile("\\#\\{(.+?)\\}");

    /**
     * 取得列参数,形如${1}
     *
     * @param str
     */
    public static List<String> getColumnParams(String str) {
        List<String> lstResult = new ArrayList<>();
        Matcher m = colRegPatten.matcher(str);
        while (m.find()) {
            lstResult.add(m.group());
        }
        return lstResult;
    }

    /**
     * 直接取得参数里的字段ID
     *
     * @param str
     * @return
     */
    public static List<Long> getColumnIds(String str) {
        List<Long> lstResult = new ArrayList<>();
        Matcher m = colRegPatten.matcher(str);
        while (m.find()) {
            lstResult.add(Long.parseLong(getParamInnerStr(m.group())));
        }
        return lstResult;
    }

    /**
     * 取得列ID,形如: 从${1} 中取出1
     *
     * @param param
     */
    public static String getParamInnerStr(String param) {
        return param.substring(2, param.length() - 1);
    }

    public static List<Long> getSysParams(String str) {
        List<Long> lstResult = new ArrayList<>();
        Matcher m = paramRegPatten.matcher(str);
        while (m.find()) {
            lstResult.add(Long.parseLong(getParamInnerStr(m.group())));
        }
        return lstResult;
    }

    public static void main(String[] args) {
        System.out.println(FormulaTools.getColumnParams("${1}+${2}>${4}"));
        List<String> columnParams = FormulaTools.getColumnParams("${1}+${2}>${4}");
        for (String str : columnParams) {
            System.out.println(FormulaTools.getParamInnerStr(str));
        }
        System.out.println(FormulaTools.getSysParams("#{1}+#{2}>#{4}"));
    }

    public static String genParamName() {
        return "P_" + ser.getAndIncrement();
    }

}
