package com.ranranx.aolie.core.interfaces;

import com.ranranx.aolie.core.common.BaseDto;

import java.util.List;

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
     * 根据ID更新
     *
     * @param lstDto
     * @param <T>
     * @return
     */
    <T extends BaseDto> int updateByIds(List<T> lstDto, boolean isSelective, Long schemaId);
}
