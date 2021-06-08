package com.ranranx.aolie.application.user.service;

import com.ranranx.aolie.application.user.dto.RightRelationDto;
import com.ranranx.aolie.application.user.dto.RightResourceDto;
import com.ranranx.aolie.application.user.dto.UserDto;
import com.ranranx.aolie.core.handler.HandleResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {


    /**
     * 查询用户的所有数据权限,
     *
     * @param userId
     * @param versionCode
     * @return 数据块形式为:   rs_id:List<UserToResource>
     */
    HandleResult getUserRights(Long userId, String versionCode);


    /**
     * 保存用户权限
     *
     * @param userId
     * @param version
     * @param mapNewUserRight rsID:list<rsDetailId>
     * @return
     */
    HandleResult saveUserRight(long userId, String version, Map<Long, List<Long>> mapNewUserRight);


    /**
     * 查询菜单和按钮,组成一个树
     *
     * @return
     */
    HandleResult findMenuAndButton();

    /**
     * 查询一权限关系数据
     *
     * @param rrId        权限关系ID
     * @param sourceId    主权限的ID
     * @param versionCode
     * @return
     */
    HandleResult findRightRelationDetail(long rrId, long sourceId, String versionCode);

    /**
     * 查询一权限关系数据
     *
     * @param sourceRsId  源权限ID
     * @param destRsId    目标权限 ID
     * @param sourceId    源权限的ID值
     * @param versionCode
     * @return
     */
    HandleResult findRightRelationDetail(long sourceRsId, long destRsId, long sourceId, String versionCode);


    /**
     * @param rsSource                主资源
     * @param sourceId                主权限定义ID
     * @param destNewRsIdAndDetailIds 从权限定义ID 及权限数据ID
     * @param versionCode
     * @return
     */
    HandleResult saveRightRelationDetails(long rsSource, long sourceId, Map<Long, List<Long>> destNewRsIdAndDetailIds, String versionCode);

    /**
     * 保存权限关系
     *
     * @param rrId
     * @param destNewIds
     * @param versionCode
     * @return
     */
    HandleResult saveRightRelationDetailsByRrId(long rrId, Map<Long, List<Long>> destNewIds, String versionCode);

    /**
     * 保存一个权限设置
     *
     * @param rrId        关系ID
     * @param sourceId    主权限数据ID
     * @param destNewIds  从权限数据ID
     * @param versionCode
     * @return
     */
    HandleResult saveRightRelationDetail(long rrId, long sourceId, List<Long> destNewIds, String versionCode);

    /**
     * 根据权限资源ID,查询权限资源全信息
     *
     * @param lstId
     * @param versionCode
     * @return
     */
    HandleResult findRightResources(List<Long> lstId, String versionCode);

    /**
     * 查询角色对应的其它资源信息
     *
     * @return
     */
    HandleResult findRoleRightOtherRelation();

    /**
     * 查询所有权限关系
     *
     * @return
     */
    List<RightRelationDto> findAllRelationDto(String version);

    /**
     * 查询所有权限关系
     *
     * @return
     */
    List<RightResourceDto> findAllRightSourceDto(String version);

    /**
     * 查询直接授予用户的权限
     *
     * @param userId      用户ID
     * @param rsId        资源类型ID
     * @param versionCode 版本号
     * @return 返回权限的明细ID
     */
    Set<Long> findUserDirectRights(Long userId, Long rsId, String versionCode);

    /**
     * 查询直接授予用户的所有权限
     *
     * @param userId      用户ID
     * @param versionCode 版本号
     * @return 返回权限的明细ID
     */
    Map<Long, Set<Long>> findUserDirectAllRights(Long userId, String versionCode,Long roleId);

    /**
     * 查询传递的权限明细
     *
     * @param rsFrom     开始的资源类别ID
     * @param rsTo       结束的资源类别ID
     * @param lstFromIds 开始资源的所有ID
     * @return 对应结果节点的所有ID
     */
    Set<Long> findNextRights(Long rsFrom, Long rsTo, Set<Long> lstFromIds, String versionCode);

    /**
     * 查询所有的带有权限的操作按钮信息
     * 所有存在权限设置的按钮 key:version+tableId+operId, value:此表此操作的按钮列表. 因为同一个操作,可能在不同的功能里,所以可能会出现多次.
     * 而出现一次,就认定有权限
     *
     * @return
     */
    Map<String, Set<Long>>[] findAllButtonsOperator();

    /**
     * 根据用户名或编辑,查询用户列表
     * @param name
     * @return
     */
    List<UserDto> findUserByCodeOrName(String name);




}
