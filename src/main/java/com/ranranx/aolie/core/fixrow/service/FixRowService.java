package com.ranranx.aolie.core.fixrow.service;

import com.ranranx.aolie.core.datameta.datamodel.BlockViewer;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/8/8 0008 10:45
 **/
public interface FixRowService {

    /**
     * 查询固定行表头
     *
     * @param fixId
     * @param version
     * @return
     */
    BlockViewer findFixRowComponents(Long fixId, String version);
}
