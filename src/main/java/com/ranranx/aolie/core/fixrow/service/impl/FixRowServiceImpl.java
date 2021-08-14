package com.ranranx.aolie.core.fixrow.service.impl;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.datameta.datamodel.BlockViewer;
import com.ranranx.aolie.core.datameta.datamodel.Component;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.fixrow.dto.FixMain;
import com.ranranx.aolie.core.fixrow.service.FixRowService;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.service.BaseDbService;
import com.ranranx.aolie.core.service.UIService;
import com.ranranx.aolie.core.tree.LevelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/8/8 0008 10:45
 **/
@Service
@Transactional(readOnly = true)
public class FixRowServiceImpl extends BaseDbService implements FixRowService {
    /**
     * 固定列信息的固定属性列的定义视图。
     */
    private static final Long FIX_COL_OPERCOL = 1002L;
    @Autowired
    private DataOperatorFactory factory;

    @Autowired
    private HandlerFactory handlerFactory;

    @Autowired
    private UIService uiService;

    /**
     * 查询固定行表头
     *
     * @param fixId
     * @param version
     * @return
     */
    @Override
    public BlockViewer findFixRowComponents(Long fixId, String version) {


        FixMain fixMain = new FixMain();
        fixMain.setFixId(fixId);
        fixMain = queryOne(fixMain, Constants.DEFAULT_SYS_SCHEMA);
        Long blockId = fixMain.getBlockId();
        if (blockId == null) {
            throw new NotExistException("没有指定固定行的列视图");
        }
        //下面开始组装
        BlockViewer viewerInfo = uiService.getViewerInfo(blockId, fixMain.getVersionCode());
        if (viewerInfo == null) {
            throw new NotExistException("没有指定固定行的列视图");
        }

        BlockViewer fixViewer = uiService.getViewerInfo(FIX_COL_OPERCOL, fixMain.getVersionCode());

        if (fixViewer == null) {
            throw new NotExistException("固定行固定属性列视图没有定义");
        }
        //将固定列接入到后面
        LevelProvider provider = new LevelProvider("900");
        List<Component> lstFixComponent = fixViewer.getLstComponent();
        for (Component component : lstFixComponent) {
            component.getComponentDto().setLvlCode(provider.getNextCode());
        }
        BlockViewer fixViewerCopy = CommonUtils.deepClone(fixViewer);
        fixViewerCopy.getLstComponent().addAll(viewerInfo.getLstComponent());
        return fixViewerCopy;
    }

}
