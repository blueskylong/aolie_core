
package com.ranranx.aolie.core.tree;


import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.exceptions.BaseException;
import com.ranranx.aolie.core.exceptions.InvalidException;

/**
 *
 */
public class LevelProvider {
    public static final String DEFAULT_RULE = "3|6|9|12|15|18|21";

    /**
     * @param args
     */
    public static void main(String[] args) {
        LevelProvider aProvider = new LevelProvider(SysCodeRule
                .createClient(new int[]{2, 6, 7, 9}), "");
        try {
            System.out.println(aProvider.getNextCode());
            System.out.println(aProvider.getNextCode());
            System.out.println(aProvider.getFirstSubCode());
            System.out.println(aProvider.getNextCode());
            System.out.println(aProvider.getNextCode());
            System.out.println(aProvider.getFirstSubCode());
            System.out.println(aProvider.getNextCode());
            System.out.println(aProvider.getNextCode());
            System.out.println(aProvider.getFirstSubCode());
            System.out.println(aProvider.getNextCode());
            System.out.println(aProvider.getNextCode());
            System.out.println(aProvider.getFirstSubCode());
            System.out.println(aProvider.getNextCode());
            System.out.println(aProvider.getNextCode());
            System.out.println(aProvider.getFirstSubCode());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private String sCurrentCode = "";// 记录当前使用的编码

    private SysCodeRule rule;// 传入的编码规则

    public LevelProvider(SysCodeRule rule, String startCode) {
        this.rule = rule;
        this.sCurrentCode = startCode;

    }

    public LevelProvider(String startCode) {
        String rule = DEFAULT_RULE;
        String[] levels = rule.split("[\\|]");
        int[] iLevel = new int[levels.length];
        for (int i = 0; i < levels.length; i++) {
            iLevel[i] = Integer.parseInt(levels[i]);
        }
        this.rule = SysCodeRule
                .createClient(iLevel);
        this.sCurrentCode = startCode;
    }

    public LevelProvider(String rule, String startCode) {
        if (CommonUtils.isEmpty(rule)) {
            rule = DEFAULT_RULE;
        }
        String[] levels = rule.split("|");
        int[] iLevel = new int[levels.length];
        for (int i = 0; i < levels.length; i++) {
            iLevel[i] = Integer.parseInt(levels[i]);
        }
        this.rule = SysCodeRule
                .createClient(iLevel);
        this.sCurrentCode = startCode;

    }

    public String getFirstSubCode() throws BaseException {// 取得第一个子节点的编码
        if ("".equals(sCurrentCode)) {// 如果是第一个节点,则根据规则生成形如001的字符串
            Integer firstLevel = (Integer) rule.originRules().get(0);
            String firstStr = "1";
            for (int i = 0; i < firstLevel.intValue() - 1; i++) {
                firstStr = "0" + firstStr;

            }
            sCurrentCode = firstStr;
            return firstStr;

        }
        int iLength = rule.nextLevelLength(sCurrentCode)
                - sCurrentCode.length();
        if (iLength < 0) {
            throw new InvalidException("级次超限！");
        }
        // 添加第一个默认下级返回
        String firstStr = "1";
        for (int i = 0; i < iLength - 1; i++) {
            firstStr = "0" + firstStr;
        }
        sCurrentCode = sCurrentCode + firstStr;// 替换为最新的编码
        return sCurrentCode;
    }

    public String getNextCode() throws BaseException {
        if ("".equals(sCurrentCode)) {// 如果是第一个节点,则根据规则生成形如001的字符串
            Integer firstLevel = (Integer) rule.originRules().get(0);
            String firstStr = "1";
            for (int i = 0; i < firstLevel.intValue() - 1; i++) {
                firstStr = "0" + firstStr;

            }
            sCurrentCode = firstStr;
            return firstStr;

        }
        // 添加下一个，可能会出现溢出的现象
        String curSubStr = rule.concurrent(sCurrentCode);
        if (rule.levelOf(sCurrentCode) == -1) {
            throw new InvalidException("提供的编码长度不正确");
        }
        int iCurIndex = Integer.parseInt(curSubStr);
        iCurIndex = iCurIndex + 1;// 增加一个数
        if (String.valueOf(iCurIndex).length() > curSubStr.length()) {// 检查是否溢出
            throw new InvalidException("当前级次已超过限制，请检查");
        }
        String sNewSubStr = String.valueOf(iCurIndex);
        int iCount = curSubStr.length() - sNewSubStr.length();
        for (int i = 0; i < iCount; i++) {
            sNewSubStr = "0" + sNewSubStr;
        }
        String sPre = rule.previous(sCurrentCode);
        if (sPre == null) {
            sPre = "";
        }
        this.sCurrentCode = sPre + sNewSubStr;
        return sCurrentCode;
    }

    public int getLevelLength(int iLevel) {
        return ((Integer) rule.originRules().get(iLevel)).intValue();
    }

    public String getSCurrentCode() {
        return sCurrentCode;
    }

    public void setSCurrentCode(String currentCode) {
        sCurrentCode = currentCode;
    }

    public String getParentCode() {
        sCurrentCode = rule.previous(sCurrentCode);
        return sCurrentCode;
    }

    /**
     * 取得当前根编码
     *
     * @return
     */
    public String getFirstLevelCode() {
        sCurrentCode = rule.rootCode(sCurrentCode);
        return sCurrentCode;
    }

    public SysCodeRule getRule() {
        return rule;
    }

    public void setRule(SysCodeRule rule) {
        this.rule = rule;
    }
}
