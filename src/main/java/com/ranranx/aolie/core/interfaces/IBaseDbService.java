package com.ranranx.aolie.core.interfaces;

import com.ranranx.aolie.core.common.BaseDto;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;

import java.util.List;
import java.util.Map;

/**
 * 数据基础操作
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/6 0006 8:08
 **/
public interface IBaseDbService {

    /**
     * 插入一条信息
     *
     * @param dto
     * @param <T>
     * @return
     */
    <T extends BaseDto> int insert(T dto, Long schemaId);


    /**
     * 批量插入
     *
     * @param dto
     * @param schemaId
     * @param <T>
     * @return
     */
    <T extends BaseDto> int insertBatch(List<T> dto, Long schemaId);

    /**
     * 批量插入
     *
     * @param lstData
     * @param tableInfo
     * @return
     */
    int insertBatch(List<Map<String, Object>> lstData, TableInfo tableInfo);

    /**
     * 根据条件删除，只限等于条件，条件不可以为空
     *
     * @param dto
     * @param <T>
     * @return
     */
    <T extends BaseDto> int delete(T dto, Long schemaId);

    /**
     * 根据条件删除，只限等于条件，条件不可以为空
     *
     * @param dto
     * @param <T>
     * @return
     */
    <T extends BaseDto> int deleteById(T dto, Long schemaId);

    /**
     * 查询一条信息
     *
     * @param dto
     * @param <T>
     * @return
     */
    <T extends BaseDto> T queryOne(T dto, Long schemaId);

    /**
     * 查询多条信息
     *
     * @param dto
     * @param <T>
     * @return
     */
    <T extends BaseDto> List<T> queryList(T dto, Long schemaId);

    /**
     * 查询多条信息
     *
     * @param dto
     * @return
     */
    <T extends BaseDto> List<Map<String, Object>> queryMapList(T dto, Long schemaId);

    /**
     * 根据ID更新
     *
     * @param lstDto
     * @param <T>
     * @return
     */
    <T extends BaseDto> int updateByIds(List<T> lstDto, boolean isSelective, Long schemaId);
}
