
package com.ranranx.aolie.core.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SysCodeRule implements Serializable {
    public static final int[] DEFAULT_RULE = new int[]{3, 6, 9, 12, 15, 18, 21};
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 代码规则的匹配模式（是否是一个可拆分的），否则就是直接平均增量的
     */
    private static final Pattern splittedCodePattern = Pattern.compile(".+\\|.+");

    /**
     * 代码规则的拆分模式
     */
    private static final Pattern codeSplitter = Pattern.compile("\\s*\\|\\s*");

    private String name;

    private String codeRuleStr;

    private static final int DEFAULT_CODE_RULE = 1;

    /**
     * 客户端标识
     */
    public static final int CLIENT_SIDE = 0;

    /**
     * 服务端标识
     */
    public static final int SERVER_SIDE = 1;

    /**
     * 储存每一级代码长度的数组
     */
    private List<Integer> codeLength = new ArrayList<Integer>();


    public static SysCodeRule createDefault() {
        return SysCodeRule.create(DEFAULT_RULE);
    }

    /**
     * 创建代码规则解析对象
     *
     * @param codeRule 代码规则数组
     * @throws Exception
     */
    public static SysCodeRule create(int[] codeRule) {
        try {
            SysCodeRule codeRuleObj = new SysCodeRule();
            String codeRuleStr = "";
            for (int i = 0; i < codeRule.length; i++) {
                codeRuleObj.codeLength.add(new Integer(codeRule[i]));
                codeRuleStr += String.valueOf(codeRule[i]) + '|';
            }
            codeRuleObj.codeRuleStr = codeRuleStr.substring(0, codeRuleStr.length() - 1);
            return codeRuleObj;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 创建代码规则解析对象
     *
     * @param codeRuleStr 代码规则字符串
     * @throws Exception
     */
    public static SysCodeRule create(String codeRuleStr) {
        try {
            SysCodeRule codeRuleObj = new SysCodeRule();
            if (splittedCodePattern.matcher(codeRuleStr).matches()) {
                String[] ruleArray = codeSplitter.split(codeRuleStr);
                codeRuleObj.createRuleArray(ruleArray);
            } else {
                codeRuleObj.createRuleArray(new String[]{codeRuleStr});
            }
            codeRuleObj.codeRuleStr = codeRuleStr;
            return codeRuleObj;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 根据当前编码获得最顶级的编码值
     *
     * @param code
     * @return
     */
    public String rootCode(String code) {
        int len = ((Integer) this.codeLength.get(0)).intValue();
        return code.substring(0, len);
    }

    /**
     * 获得当前代码的级次
     *
     * @param code
     * @return 级次值
     */
    public int levelOf(String code) {
        try {
            return this.codeLength.indexOf(new Integer(code.length()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取本级编码的下级编码的长度
     *
     * @param code 本级编码
     * @return
     */
    public int nextLevelLength(String code) {
        int level = 0;
        if (!isNullStr(code)) {
            level = this.levelOf(code);
            if (level < 0 || level == this.codeLength.size() - 1) {
                level = -1;
            } else {
                level++;
            }
        } else {
            level = 0;
        }
        return level >= 0 ? ((Integer) codeLength.get(level)).intValue() : -1;
    }

    /**
     * 由本级编码获取上级编码
     *
     * @param code 本级编码
     * @return
     */
    public String previous(String code) {
        if (code == null) {
            return null;
        }
        int idx = levelOf(code);
        if (idx > 0) {
            int previousLength = ((Integer) codeLength.get(idx - 1)).intValue();
            return code.substring(0, previousLength);
        }
        return null;
    }

    /**
     * 获取去除上级编码后的本级小编码，例如001002的上级编码是001， 本函数处理过001002的返回值就是002
     *
     * @param code
     * @return
     */
    public String concurrent(String code) {
        int idx = levelOf(code);
        if (idx > 0) {
            int previousLength = ((Integer) codeLength.get(idx - 1)).intValue();
            return code.substring(previousLength, code.length());
        }
        return code;
    }

    /**
     * 吴锁明2012-03-12 根据级次获取编码
     *
     * @param level 级次
     * @param code  编码
     * @return
     */
    public String getCodeByLevel(int level, String code) {
        if (level > levelOf(code) || level < 0) {
            return code;
        } else {
            int previousLength = ((Integer) codeLength.get(level)).intValue();
            return code.substring(0, previousLength);
        }

    }

    /**
     * 获取同级的下一个编码(有bug)
     *
     * @return
     * @deprecated
     */
    public String nextCodeSameLvl(String code) {
        String pCode = "";
        String cCode = "0";
        int idx = levelOf(code);
        int cLength;
        if (idx > 0) {
            int previousLength = ((Integer) codeLength.get(idx - 1)).intValue();
            pCode = code.substring(0, previousLength);
            cCode = code.substring(previousLength, code.length());
            cLength = ((Integer) codeLength.get(idx)).intValue() - ((Integer) codeLength.get(idx - 1)).intValue();
        } else {
            cLength = ((Integer) codeLength.get(0)).intValue();
        }
        cCode = "" + (Integer.parseInt(cCode) + 1);
        while (cCode.length() < cLength) {
            cCode = "0" + cCode;
        }
        return pCode + cCode;
    }

    /**
     * 获取同级的下一个编码
     *
     * @return
     */
    public String nextCodeSameLvl2(String code) {
        String pCode = "";
        String cCode = "0";
        int idx = levelOf(code);
        int cLength;
        if (idx > 0) {
            int previousLength = ((Integer) codeLength.get(idx - 1)).intValue();
            pCode = code.substring(0, previousLength);
            cCode = code.substring(previousLength, code.length());
            cLength = ((Integer) codeLength.get(idx)).intValue() - ((Integer) codeLength.get(idx - 1)).intValue();
        } else {
            cCode = code;
            cLength = ((Integer) codeLength.get(0)).intValue();

        }
        cCode = "" + (Integer.parseInt(cCode) + 1);
        while (cCode.length() < cLength) {
            cCode = "0" + cCode;
        }
        return pCode + cCode;
    }

    /**
     * 获取下级的第一个编码
     *
     * @return
     */
    public String nextCodeNextLvl(String code) {
        String cCode = "1";
        int idx = levelOf(code);
        int cLength = ((Integer) codeLength.get(idx + 1)).intValue() - ((Integer) codeLength.get(idx)).intValue();
        while (cCode.length() < cLength) {
            cCode = "0" + cCode;
        }
        return code + cCode;
    }

    /**
     * 返回系统编码规则字符串配置
     *
     * @return
     */
    public String originRuleStr() {
        return this.codeRuleStr;
    }

    /**
     * 返回分割的字符编码
     */
    public String[] getOriginRule() {
        return codeSplitter.split(codeRuleStr);
    }

    /**
     * 返回原始的编码规则表List<Integer>
     *
     * @return
     */
    public List<Integer> originRules() {
        return this.codeLength;
    }

    protected void createRuleArray(String[] ruleArray) {
        for (int i = 0; i < ruleArray.length; i++) {
            if (isNumber(ruleArray[i])) {
                codeLength.add(new Integer(ruleArray[i]));
            }
        }
    }

    protected void createRuleArray(String ruleStr) {
        for (int i = 0, delta = Integer.parseInt(ruleStr); i < DEFAULT_CODE_RULE; i++, delta += Integer
                .parseInt(ruleStr)) {
            codeLength.add(new Integer(delta));
        }
    }

    private static final Pattern NUMBER_PATTERN = Pattern
            // .compile("[+-]{0,1}\\d+(\\.{0,1}\\d+){0,1}");
            // .compile("[+-]{0,1}\\d+(\\.{0,1}\\d+){0,1}|[+-]{0,1}\\d+(\\.{0,1}\\d+){0,1}E?\\d+|[+-]{0,1}(\\.{0,1}\\d+){0,1}");
            .compile("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)(E\\d+)?");

    public static boolean isNumber(String str) {
        return NUMBER_PATTERN.matcher(str).matches();
    }

    public String name() {
        return name;
    }

    /**
     * 判断一个空字符串（null或者""）
     *
     * @param s 待判断的字符串
     * @return 判断结果
     */
    public static boolean isNullStr(String s) {
        return s == null || s.trim().length() <= 0 || s.trim().equals("null");
    }

    public static boolean isNullStr(Object s) {
        return s == null || s.toString() == null || (s.toString().trim().length()) <= 0
                || s.toString().trim().equals("null");
    }
}
