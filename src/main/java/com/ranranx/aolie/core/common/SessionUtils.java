package com.ranranx.aolie.core.common;

import com.ranranx.aolie.application.right.RightNode;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.runtime.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/9/11 10:34
 **/
public class SessionUtils {

    /**
     * 系统参数  key: id_version value:SystemParam
     */
    private static Map<String, SystemParam> sysParams = new HashMap<>();
    /**
     * 系统参数的值
     */
    private static Map<String, Map<Long, Object>> sysParamValues = new HashMap<>();
    /**
     * 系统权限结构 key:versionCode,value:rightNode
     */
    private static Map<String, RightNode> mapRightStruct;

    private static String defaultVersion;

    private static AtomicInteger onlineUserCount = new AtomicInteger(0);

    public static String getLoginVersion() {
        if (getLoginUser() == null) {
            return Constants.DEFAULT_VERSION;
        }
        return getLoginUser().getVersionCode();
    }

    public static LoginUser getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object myUser = (auth != null) ? auth.getPrincipal() : null;
        if (myUser instanceof String) {
            return null;
        }
        return (LoginUser) myUser;
    }


    public static boolean isSuperAdmin() {
        return Constants.UserType.superAdmin.equals(getLoginUser().getUserType());
    }

    /**
     * 初始化全局参数
     */
    public static Map<String, SystemParam> getUserParam() {
        LoginUser user = getLoginUser();
        Map<String, SystemParam> params = new HashMap<String, SystemParam>();
        //增加人员
        return params;
    }

    /**
     * 取得所有参数,包含系统参数和用户参数值
     *
     * @return
     */
    public static Map<String, Object> getAllParamValues() {
        Map<Long, Object> params = new HashMap<>();
        params.putAll(getSysParamValues(SessionUtils.getLoginVersion()));
        params.putAll(SessionUtils.getLoginUser().getParamValues());
        return convert(params);
    }

    public static Map<String, Object> convert(Map<Long, Object> values) {
        Map<String, Object> result = new HashMap<>();
        values.forEach((key, value) -> {
            result.put(String.valueOf(key), value);
        });
        return result;
    }

    /**
     * 取得所有参数,包含系统参数和用户参数信息
     *
     * @return
     */
    public static Map<Long, SystemParam> getAllParams() {
        Map<Long, SystemParam> params = new HashMap<>();
        if (getLoginVersion() != null) {
            params.putAll(findConstParams(getLoginVersion()));
        }
        if (SessionUtils.getLoginUser() != null) {
            params.putAll(SessionUtils.getLoginUser().getParams());
        }
        return params;
    }

    /**
     * 取得所有参数,包含系统参数和用户参数信息
     *
     * @return
     */
    public static Map<Long, SystemParam> getAllParams(String versionCode) {
        Map<Long, SystemParam> params = new HashMap<>();
        params.putAll(findConstParams(versionCode));
        if (SessionUtils.getLoginUser() != null) {
            params.putAll(SessionUtils.getLoginUser().getParams());
        }

        return params;
    }

    private static Map<Long, SystemParam> findConstParams(String version) {
        Iterator<SystemParam> iterator = sysParams.values().iterator();
        Map<Long, SystemParam> map = new HashMap<>();
        while (iterator.hasNext()) {
            SystemParam next = iterator.next();
            if (next.getVersionCode().equals(version)) {
                map.put(next.getId(), next);
            }
        }
        return map;
    }

    /**
     * 增加一个系统参数
     *
     * @param param 系统参数
     */
    private static void addParam(SystemParam param) {
        sysParams.put(param.getKey(), param);
        Map<Long, Object> paramValues =
                sysParamValues.computeIfAbsent(param.getVersionCode(), key -> new HashMap<>());
        paramValues.put(param.getId(), param.getValue());
    }

    /**
     * 批量增加系统全局参数
     *
     * @param params 系统参数
     */
    public static void addParams(List<SystemParam> params) {
        if (params == null || params.isEmpty()) {
            return;
        }
        params.forEach((param) -> {
            addParam(param);
        });
    }

    /**
     * 取得系统参数值
     *
     * @param versionCode
     * @return
     */
    public static Map<Long, Object> getSysParamValues(String versionCode) {
        return sysParamValues.get(versionCode);
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
        if (SessionUtils.sysParams.containsKey(paramId)) {
            return SessionUtils.sysParams.get(paramId);
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
    public static SystemParam getParamInfoByName(String name, String version) {
        Iterator<Map.Entry<String, SystemParam>> iterator =
                sysParams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SystemParam> next = iterator.next();
            if (next.getValue().getName().equals(name) && next.getValue().getVersionCode().equals(version)) {
                return next.getValue();
            }
        }

        Iterator<Map.Entry<Long, SystemParam>> params = SessionUtils.getLoginUser().getParams().entrySet().iterator();
        while (params.hasNext()) {
            Map.Entry<Long, SystemParam> next = params.next();
            if (next.getValue().getName().equals(name) && next.getValue().getVersionCode().equals(version)) {
                return next.getValue();
            }
        }

        throw new NotExistException("系统参数[" + name + "]不存在");

    }

    public static Map<String, RightNode> getMapRightStruct() {
        return mapRightStruct;
    }

    public static void setMapRightStruct(Map<String, RightNode> mapRightStruct) {
        SessionUtils.mapRightStruct = mapRightStruct;
    }

    public static int getOnlineUserNum() {
        return onlineUserCount.get();
    }

    public static int incrementSessionNum() {
        return onlineUserCount.incrementAndGet();
    }

    public static int decrementSessionNum() {
        return onlineUserCount.decrementAndGet();
    }


    public static String getDefaultVersion() {
        return defaultVersion;
    }

    public static void setDefaultVersion(String defaultVersion) {
        SessionUtils.defaultVersion = defaultVersion;
    }
}
