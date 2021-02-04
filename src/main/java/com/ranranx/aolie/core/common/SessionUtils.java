package com.ranranx.aolie.core.common;

import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.runtime.LoginUser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author xxl
 *
 * @date 2020/9/11 10:34
 * @version V0.0.1
 **/
public class SessionUtils {
    private static LoginUser user = new LoginUser();

    public static String getLoginVersion() {
        return "000000";
    }

    public static LoginUser getLoginUser() {
        return user;
    }

    public static Map<String, SystemParam> PARAMS;

    static {
        user.setAccountCode("999");
        user.setUserId(1L);
        user.setBelongOrg(1L);
        user.setBelongOrgCode("001001");
        user.setRoleId(1L);
        user.setUserName("admin");
        user.setVersionCode("000000");
        user.setUserType(1);
        user.setParams(getUserParam(user));
    }

    /**
     * 初始化全局参数
     */
    static Map<String, SystemParam> getUserParam(LoginUser user) {
        Map<String, SystemParam> params = new HashMap<String, SystemParam>();
        //增加人员
        params.put(DmConstants.GlobalParamsIds.userId + "",
                new SystemParam("登录用户ID", DmConstants.FieldType.INT, user.getUserId(), DmConstants.GlobalParamsIds.userId));
        params.put(DmConstants.GlobalParamsIds.roleId + "",
                new SystemParam("登录角色ID", DmConstants.FieldType.INT, user.getRoleId(), DmConstants.GlobalParamsIds.roleId));
        params.put(DmConstants.GlobalParamsIds.version + "",
                new SystemParam("版本号", DmConstants.FieldType.VARCHAR, user.getVersionCode(), DmConstants.GlobalParamsIds.version));
        params.put(DmConstants.GlobalParamsIds.userName + "",
                new SystemParam("登录用户名", DmConstants.FieldType.VARCHAR, user.getUserName(), DmConstants.GlobalParamsIds.userName));
        params.put(DmConstants.GlobalParamsIds.userBelong + "",
                new SystemParam("登录用户所属机构", DmConstants.FieldType.INT, user.getUserId(), DmConstants.GlobalParamsIds.userBelong));
        params.put(DmConstants.GlobalParamsIds.userAccount + "",
                new SystemParam("登录帐号", DmConstants.FieldType.VARCHAR, user.getAccountCode(), DmConstants.GlobalParamsIds.userAccount));
        return params;
    }

    /**
     * 取得所有参数,包含系统参数和用户参数
     *
     * @return
     */
    static Map<String, SystemParam> getAllSysParams() {
        Map<String, SystemParam> params = new HashMap<>();
        params.putAll(SessionUtils.PARAMS);
        params.putAll(SessionUtils.getLoginUser().getParams());
        return params;
    }

    /**
     * 取得系统参数值
     *
     * @param paramId
     */
    public static Object getParamValue(String paramId) {
        SystemParam paramInfo = getParamInfo(paramId);
        if (paramInfo != null) {
            return paramInfo.getValue();
        }
        return null;
    }

    /**
     * 取得系统参数值
     *
     * @param paramId
     */
    public static SystemParam getParamInfo(String paramId) {
        if (SessionUtils.PARAMS.containsKey(paramId)) {
            return SessionUtils.PARAMS.get(paramId);
        }
        if (SessionUtils.getLoginUser().getParams().containsKey(paramId)) {
            return SessionUtils.getLoginUser().getParams().get(paramId);
        }
        return null;
    }

    /**
     * 取得系统参数
     *
     * @param name
     */
    public static SystemParam getParamInfoByName(String name) {
        Iterator<Map.Entry<String, SystemParam>> iterator =
                SessionUtils.PARAMS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SystemParam> next = iterator.next();
            if (next.getValue().getName().equals(name)) {
                return next.getValue();
            }
        }
        iterator =
                SessionUtils.getLoginUser().getParams().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SystemParam> next = iterator.next();
            if (next.getValue().getName().equals(name)) {
                return next.getValue();
            }
        }

        throw new NotExistException("系统参数[" + name + "]不存在");


    }
}
