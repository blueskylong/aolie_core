package com.ranranx.aolie.core.runtime;

import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.common.SystemParam;
import com.ranranx.aolie.core.interfaces.ISystemIniter;
import com.ranranx.aolie.core.interfaces.ISystemParamGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收集系统参数, 收集用户参数
 *
 * @author xxl
 * @version V0.0.1
 * @date 2020/12/28 0028 14:23
 **/
@Service
public class GlobalParameterService implements ISystemIniter {

    @Autowired
    private List<ISystemParamGenerator> lstGenerator;

    public Map<Long, SystemParam> getUserParam(LoginUser user) {
        String key = genKey(user);

        Map<Long, SystemParam> mapResult = new HashMap<>();
        if (this.lstGenerator == null || this.lstGenerator.isEmpty()) {
            return mapResult;
        }
        for (ISystemParamGenerator globalParamGenerator : lstGenerator) {
            mapResult.putAll(toMap(globalParamGenerator.getUserParams(user)));
        }
        return mapResult;
    }

    private String genKey(LoginUser user) {
        return new StringBuilder(user.getUserId().toString()).append("_")
                .append(user.getRoleId()).append("_").append(user.getVersionCode()).toString();
    }

    /**
     * 取得所有不会变化的系统参数
     *
     * @return
     */
    private List<SystemParam> getConstParam() {

        List<SystemParam> constParam = new ArrayList<>();
        if (this.lstGenerator == null || this.lstGenerator.isEmpty()) {
            return constParam;
        }
        for (ISystemParamGenerator globalParamGenerator : lstGenerator) {
            List<SystemParam> params = globalParamGenerator.getConstParams();
            if (params != null) {
                constParam.addAll(params);
            }

        }
        return constParam;
    }

    @Override
    public void init() {
        SessionUtils.addParams(getConstParam());
    }

    @Override
    public int getOrder() {
        return 100;
    }

    private Map<Long, SystemParam> toMap(List<SystemParam> lstParam) {
        Map<Long, SystemParam> map = new HashMap<>();
        if (lstParam == null || lstParam.isEmpty()) {
            return map;
        }
        for (SystemParam param : lstParam) {
            map.put(param.getId(), param);
        }
        return map;
    }


}
