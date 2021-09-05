package com.ranranx.aolie.core.fixrow.service;

import com.ranranx.aolie.core.datameta.datamodel.BlockViewer;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.fixrow.dto.FixMain;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.param.QueryParam;

import java.util.List;
import java.util.Map;

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


    /**
     * 同步更新固定行对应设置
     *
     * @param schemaId
     */

    void syncFixSet(Long schemaId, String version);

    /**
     * 保存固定行数据,传过来的数据，是业务表的字段，需要先转换成固定行表字段（表fix_data)
     *
     * @param rows
     * @param fixId
     * @return
     */

    HandleResult saveFixData(List<Map<String, Object>> rows, Long fixId, String version);

    /**
     * 查询一个固定行数据
     *
     * @param fixId
     * @param version
     * @return
     */
    List<Map<String, Object>> findFixData(Long fixId, String version, boolean isOnlyRelationFields);

    /**
     * 检查 固定行数据情况
     *
     * @param mapKeyValue
     * @param tableInfo
     */
    boolean checkNeedFixBlock(Map<String, Object> mapKeyValue, TableInfo tableInfo);

    /**
     * 执行插入,这里不再检查,请先执行检查
     *
     * @param mapKeyValue
     * @param tableInfo
     * @param oraQueryParam 原来查询参数，如果有
     * @return
     */
    HandleResult copyFixTableRow(Map<String, Object> mapKeyValue, TableInfo tableInfo, QueryParam oraQueryParam);

    /**
     * 生成控制信息
     * TODO 这里如果固定内容升级了,应该如何处理,是需要解决的,当前只处理
     *
     * @param lstData
     * @param fixId
     * @param version
     */
    void makeFullControlInfo(List<Map<String, Object>> lstData, Long fixId, String version);

    /**
     * 通过表查询固定行信息
     *
     * @param tableId
     * @param version
     * @return
     */
    FixMain findFixMainByTable(Long tableId, String version);

    /**
     * 通过表查询固定行信息
     *
     * @param version
     * @return
     */
    HandleResult findFixMain(String version);

    /**
     * 保存业务的固定数据，不分页，需要向上汇总计算
     *
     * @param lstRow
     * @param mapKey
     * @param fixId
     * @return
     */
    HandleResult saveBusiFixData(List<Map<String, Object>> lstRow, Map<String, Object> mapKey, long fixId, String version);
}
