package com.ranranx.aolie.core.runtime;

import com.ranranx.aolie.core.interfaces.IGlobalParamGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *
 * @date 2020/12/28 0028 14:23
 * @version V0.0.1
 **/
@Service
public class GlobalParameterService {

    @Autowired
    private List<IGlobalParamGenerator> lstGenerator;
    /**
     * 做缓存,以用户的ID和角色ID和版本来区分,用户没有登录的,则不缓存
     */
    private Map<String, Map<Long, GlobalParam>> userParamCache = new HashMap<>();
    /**
     * 存与用户无关的参数
     */
    private Map<Long, GlobalParam> constParam = null;

    public Map<Long, GlobalParam> getGlobalValues(LoginUser user) {
        if (user == null) {
            return getConstParam();
        }
        //以下采集与用户相关的数据
        Map<Long, GlobalParam> map = new HashMap<>();
        map.putAll(getUserParam(user));
        map.putAll(getConstParam());
        return map;
    }

    private Map<Long, GlobalParam> getUserParam(LoginUser user) {
        String key = genKey(user);
        if (userParamCache.containsKey(key)) {
            return userParamCache.get(key);
        }
        Map<Long, GlobalParam> mapResult = new HashMap<>();
        if (this.lstGenerator == null || this.lstGenerator.isEmpty()) {
            return mapResult;
        }
        for (IGlobalParamGenerator globalParamGenerator : lstGenerator) {
            mapResult.putAll(toMap(globalParamGenerator.getParams(user)));
        }
        userParamCache.put(key, mapResult);
        return mapResult;
    }

    private String genKey(LoginUser user) {
        return new StringBuilder(user.getUserId().toString()).append("_")
                .append(user.getRoleId()).append("_").append(user.getVersionCode()).toString();
    }

    private Map<Long, GlobalParam> getConstParam() {
        if (this.constParam != null) {
            return this.constParam;
        }
        this.constParam = new HashMap<>();
        if (this.lstGenerator == null || this.lstGenerator.isEmpty()) {
            return this.constParam;
        }
        for (IGlobalParamGenerator globalParamGenerator : lstGenerator) {
            this.constParam.putAll(toMap(globalParamGenerator.getConstParams()));
        }
        return this.constParam;
    }

    private Map<Long, GlobalParam> toMap(List<GlobalParam> lstParam) {
        Map<Long, GlobalParam> map = new HashMap<>();
        if (lstParam == null || lstParam.isEmpty()) {
            return map;
        }
        for (GlobalParam param : lstParam) {
            map.put(param.getParamId(), param);
        }
        return map;
    }
}
