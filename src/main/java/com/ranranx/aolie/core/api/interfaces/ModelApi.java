package com.ranranx.aolie.core.api.interfaces;

import com.ranranx.aolie.core.datameta.dto.VersionDto;
import com.ranranx.aolie.core.handler.HandleResult;

import java.util.List;
import java.util.Map;

/**
 * 对外提供模型服务
 */
public interface ModelApi {

    /**
     * 取得所有版本信息
     *
     * @return
     */
    List<VersionDto> getVersions();

    /**
     * 保存从表行,这样前端可以不传入删除的行,通过关联关系删除相应的行
     *
     * @param rows
     * @param dsId
     * @param masterDsId
     * @param masterKey
     * @return
     */
    HandleResult saveSlaveRows(List<Map<String, Object>> rows, Long dsId, Long masterDsId, Long masterKey);
}
