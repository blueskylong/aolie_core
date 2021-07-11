package com.ranranx.aolie.core.api.impl;

import com.ranranx.aolie.core.api.interfaces.ModelApi;
import com.ranranx.aolie.core.datameta.dto.VersionDto;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.service.DataModelService;
import com.ranranx.aolie.core.service.DmDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/6/11 0011 16:00
 **/
@Service
@Transactional(readOnly = true)
public class ModelApiImpl implements ModelApi {
    @Autowired
    private DataModelService dmService;

    @Autowired
    private DmDataService dmDataService;

    @Override
    public List<VersionDto> getVersions() {
        return dmService.getVersions();
    }

    /**
     * 保存从表行,这样前端可以不传入删除的行,通过关联关系删除相应的行
     *
     * @param rows
     * @param dsId
     * @param masterDsId
     * @param masterKey
     * @return
     */
    @Override
    public HandleResult saveSlaveRows(List<Map<String, Object>> rows, Long dsId, Long masterDsId, Long masterKey) {
        return dmDataService.saveSlaveRows(rows, dsId, masterDsId, masterKey);
    }
}
