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


    /**
     * 保存从表行,这样前端可以不传入删除的行,通过关联关系删除相应的行
     *
     * @param rows
     * @param clazzRow    插件的类
     * @param classMaster 主表类
     * @param masterKey
     * @return
     */
    <T> HandleResult saveSlaveRowsByObject(List<T> rows, Class<T> clazzRow, Class classMaster, Long
            masterKey, Long schemaId);

    /**
     * 保存指定范围内的数据,内涉及一张表,会生成增删更新的分别操作.
     * @param rows
     * @param dsId
     * @param mapFilter
     * @param version
     * @return
     */
    HandleResult saveRangeRows(List<Map<String, Object>> rows, Long dsId, Map<String, Object> mapFilter, String version);
}
