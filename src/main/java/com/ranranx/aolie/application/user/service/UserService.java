package com.ranranx.aolie.application.user.service;

import com.ranranx.aolie.application.user.dto.RightRelationDetailDto;
import com.ranranx.aolie.application.user.dto.RightRelationDto;
import com.ranranx.aolie.application.user.dto.RightSourceDto;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.SqlExp;
import com.ranranx.aolie.core.exceptions.IllegalOperatorException;
import com.ranranx.aolie.core.exceptions.InvalidConfigException;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.DeleteParam;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import com.ranranx.aolie.core.service.DmDataService;
import com.ranranx.aolie.core.tools.SqlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author xxl
 * @Description
 * @Date 2021/1/6 0006 15:30
 * @Version V0.0.1
 **/
@Service
@Transactional(readOnly = true)
public class UserService {
    private static final String USER_RESOURCE_TABLE_NAME = "aolie_s_user_right";

    @Autowired
    private HandlerFactory handlerFactory;

    @Autowired
    private DmDataService dataService;

    /**
     * 查询用户的所有数据权限,
     *
     * @param userId
     * @param versionCode
     * @return 数据块形式为:   rs_id:List<UserToResource>
     */
    public HandleResult getUserRights(Long userId, String versionCode) {
        HandleResult result = getUserRightData(userId, versionCode);
        if (result.isSuccess()) {
            result.setData(makeResult((List<Map<String, Object>>) result.getData()));
        }
        return result;
    }

    private HandleResult getUserRightData(Long userId, String versionCode) {
        if (userId == null || userId < 0) {
            throw new InvalidParamException("用户id不合法");
        }
        TableInfo table =
                SchemaHolder.findTableByTableName(USER_RESOURCE_TABLE_NAME, Constants.DEFAULT_SYS_SCHEMA, versionCode);
        if (table == null) {
            throw new NotExistException("未查询到用户表定义");
        }

        QueryParam param = new QueryParam();
        param.setTable(new TableInfo[]{table});
        param.setLstOrder(table.getDefaultOrder());
        param.appendCriteria().andEqualTo("user_id", userId).andEqualTo("version_code", versionCode);
        return handlerFactory.handleQuery(param);

    }

    /**
     * 生成结果
     *
     * @param lstData
     * @return
     */
    private Map<Object, List<Map<String, Object>>> makeResult(List<Map<String, Object>> lstData) {
        if (lstData == null || lstData.isEmpty()) {
            return new HashMap<>(0);
        }
        Object objRsId;
        List<Map<String, Object>> lst;
        Map<Object, List<Map<String, Object>>> result = new HashMap<>(10);
        for (Map<String, Object> row : lstData) {
            objRsId = row.get("rs_id");
            lst = result.get(objRsId);
            if (lst == null) {
                lst = new ArrayList<>();
                result.put(objRsId, lst);
            }
            lst.add(row);
        }
        return result;
    }

    /**
     * 保存用户权限
     *
     * @param userId
     * @param version
     * @param mapNewUserRight rsID:list<rsDetailId>
     * @return
     */
    @Transactional(readOnly = false)
    public HandleResult saveUserRight(long userId, String version, Map<Long, List<Long>> mapNewUserRight) {
        HandleResult userRightData = this.getUserRightData(userId, version);
        if (!userRightData.isSuccess()) {
            return userRightData;
        }
        List<Map<String, Object>> lstExistRight = (List<Map<String, Object>>) userRightData.getData();
        if (lstExistRight == null) {
            lstExistRight = new ArrayList<>();
        }
        Map<String, long[]> mapNewRight = toMap(mapNewUserRight);
        List<Object> lstToDelete = new ArrayList<>();
        String key;
        for (Map<String, Object> row : lstExistRight) {
            key = genRsDetailKey(row.get("rs_id"), row.get("rs_detail_id"));
            //如果新的数据里也有,则不需要处理
            if (mapNewRight.containsKey(key)) {
                mapNewRight.remove(key);
            } else {
                //如果新的数据里没有,则要删除
                lstToDelete.add(row.get("user_right_id"));
            }
        }
        TableInfo table =
                SchemaHolder.findTableByTableName(USER_RESOURCE_TABLE_NAME, Constants.DEFAULT_SYS_SCHEMA, version);
        if (table == null) {
            throw new NotExistException("未查询到用户表定义");
        }
        HandleResult result = null;
        int count = 0;
        if (!lstToDelete.isEmpty()) {
            //执行删除
            result = dataService.deleteRowByIds(lstToDelete, table.getTableDto().getTableId());
            count += result.getChangeNum();
            if (!result.isSuccess()) {
                return result;
            }
        }
        //增加
        if (!mapNewRight.isEmpty()) {
            result = dataService.saveRowByAdd(toList(mapNewRight, userId), table.getTableDto().getTableId());
            if (!result.isSuccess()) {
                throw new IllegalOperatorException(result.getErr());
            }
            count += result.getChangeNum();
        }
        if (result == null) {
            return HandleResult.success(0);
        }
        result.setChangeNum(count);
        return result;


    }

    private List<Map<String, Object>> toList(Map<String, long[]> mapUserRight, Long userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        Iterator<long[]> iterator = mapUserRight.values().iterator();
        Map<String, Object> row;
        int index = -1;
        String version = SessionUtils.getLoginVersion();
        while (iterator.hasNext()) {
            long[] ids = iterator.next();
            row = new HashMap<>();
            row.put("user_id", userId);
            row.put("rs_id", ids[0]);
            row.put("rs_detail_id", ids[1]);
            row.put("user_right_id", index--);
            row.put("version_code", version);
            result.add(row);
        }
        return result;
    }

    private Map<String, long[]> toMap(Map<Long, List<Long>> mapNewUserRight) {
        if (mapNewUserRight == null || mapNewUserRight.isEmpty()) {
            return new HashMap<>();
        }
        Iterator<Map.Entry<Long, List<Long>>> iterator = mapNewUserRight.entrySet().iterator();
        Map<String, long[]> result = new HashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<Long, List<Long>> entry = iterator.next();
            oneRsToMap(entry.getKey(), entry.getValue(), result);
        }
        return result;
    }

    private void oneRsToMap(Long rsId, List<Long> lstDetailId, Map<String, long[]> result) {
        if (lstDetailId == null) {
            return;
        }
        for (Long detailId : lstDetailId) {
            result.put(genRsDetailKey(rsId, detailId), new long[]{rsId, detailId});
        }
        return;
    }

    private String genRsDetailKey(Object rsId, Object rsDetailId) {
        return rsId.toString() + "_" + rsDetailId.toString();
    }


    /**
     * 查询菜单和按钮,组成一个树
     *
     * @return
     */
    public HandleResult findMenuAndButton() {
        QueryParam param = new QueryParam();
        SqlExp exp = new SqlExp();
        exp.setSql(SqlLoader.getSql("userRight.findAllMenu"));
        exp.addParam("versionCode", SessionUtils.getLoginVersion());
        param.setSqlExp(exp);
        return handlerFactory.handleRequest(Constants.HandleType.TYPE_QUERY, param);
    }

    /**
     * 查询一权限关系数据
     *
     * @param rrId        权限关系ID
     * @param sourceId    主权限的ID
     * @param versionCode
     * @return
     */
    public HandleResult findRightRelationDetail(long rrId, long sourceId, String versionCode) {
        //Query

        TableInfo tableInfo = SchemaHolder.findTableByTableName(CommonUtils.getTableName(RightRelationDetailDto.class),
                Constants.DEFAULT_SYS_SCHEMA, versionCode);
        QueryParam param = new QueryParam();
        param.setTable(new TableInfo[]{tableInfo});
        param.setResultClass(RightRelationDetailDto.class);
        Criteria criteria = param.appendCriteria().andEqualTo("rr_id", rrId);
        if (sourceId >= 0) {
            criteria.andEqualTo("id_source", sourceId);
        }
        return handlerFactory.handleQuery(param);
    }

    /**
     * 查询一权限关系数据
     *
     * @param sourceRsId  源权限ID
     * @param destRsId    目标权限 ID
     * @param sourceId    源权限的ID值
     * @param versionCode
     * @return
     */
    public HandleResult findRightRelationDetail(long sourceRsId, long destRsId, long sourceId, String versionCode) {
        //Query

        long rrid = findRrid(sourceRsId, destRsId, versionCode);
        if (rrid < 0) {
            return HandleResult.failure("没有查询到二个权限的关联关系");
        }
        return findRightRelationDetail(rrid, sourceId, versionCode);
    }

    private long findRrid(long sourceRsId, Long destRsId, String versionCode) {
        QueryParam param = new QueryParam();
        TableInfo info = SchemaHolder.findTableByTableName(
                CommonUtils.getTableName(RightRelationDto.class),
                Constants.DEFAULT_SYS_SCHEMA, versionCode);
        param.setTable(new TableInfo[]{info});
        param.appendCriteria().andEqualTo("rs_id_from", sourceRsId)
                .andEqualTo("rs_id_to", destRsId);
        HandleResult result = handlerFactory.handleQuery(param);
        if (!result.isSuccess() || result.getData() == null) {
            return -1;
        }
        List<Map<String, Object>> lstData = (List<Map<String, Object>>) result.getData();
        if (lstData == null || lstData.isEmpty()) {
            return -1;
        }
        return CommonUtils.getIntegerField(lstData.get(0), "rr_id").longValue();
    }

    /**
     * @param rsSource                主资源
     * @param sourceId                主权限定义ID
     * @param destNewRsIdAndDetailIds 从权限定义ID 及权限数据ID
     * @param versionCode
     * @return
     */
    @Transactional(readOnly = false)
    public HandleResult saveRightRelationDetails(long rsSource, long sourceId, Map<Long, List<Long>> destNewRsIdAndDetailIds, String versionCode) {
        Iterator<Map.Entry<Long, List<Long>>> iterator = destNewRsIdAndDetailIds.entrySet().iterator();
        HandleResult result = HandleResult.success(0);
        while (iterator.hasNext()) {
            Map.Entry<Long, List<Long>> rsEntry = iterator.next();
            Long destRsId = rsEntry.getKey();
            List<Long> lstIds = rsEntry.getValue();
            long rrId = findRrid(rsSource, destRsId, versionCode);
            if (rrId < 0) {
                throw new InvalidConfigException("没有查询到二个权限的关联关系");
            }
            HandleResult oneResult = saveRightRelationDetail(rrId, sourceId, lstIds, versionCode);
            if (!oneResult.isSuccess()) {
                throw new IllegalOperatorException(oneResult.getErr());
            }
            result.setChangeNum(result.getChangeNum() + oneResult.getChangeNum());
        }
        return result;
    }

    /**
     * 保存权限关系
     *
     * @param rrId
     * @param destNewIds
     * @param versionCode
     * @return
     */
    @Transactional(readOnly = false)
    public HandleResult saveRightRelationDetailsByRrId(long rrId, Map<Long, List<Long>> destNewIds, String versionCode) {
        HandleResult existDetail = findRightRelationDetail(rrId, -1, versionCode);
        List<RightRelationDetailDto> lstDto = (List<RightRelationDetailDto>) existDetail.getData();
        List<Object> toDelete = new ArrayList<>();
        if (destNewIds == null) {
            destNewIds = new HashMap<>();
        }
        long targetId, sourceId;
        if (lstDto != null && !lstDto.isEmpty()) {
            for (RightRelationDetailDto dto : lstDto) {
                targetId = dto.getIdTarget();
                sourceId = dto.getIdSource();
                //如果新的不存在,则要删除
                if (!destNewIds.containsKey(sourceId) ||
                        (destNewIds.get(sourceId) != null && destNewIds.get(sourceId).indexOf(targetId) == -1)) {
                    toDelete.add(dto.getRrDetailId());
                } else if (destNewIds.containsKey(sourceId) && destNewIds.get(sourceId) != null && destNewIds.get(sourceId).indexOf(targetId) != -1) {
                    //如果有则在列表中删除
                    destNewIds.get(sourceId).remove(targetId);
                }
            }

        }
        HandleResult result = HandleResult.success(0);
        //如果删除的不是空,则去删除
        if (!toDelete.isEmpty()) {
            HandleResult deleteResult = deleteRrDetail(rrId, toDelete, versionCode);
            if (!deleteResult.isSuccess()) {
                return deleteResult;
            }
            result.setChangeNum(deleteResult.getChangeNum());
        }
        if (!destNewIds.isEmpty()) {
            Iterator<Map.Entry<Long, List<Long>>> iterator = destNewIds.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, List<Long>> entry = iterator.next();
                sourceId = entry.getKey();
                List<Long> lstDest = entry.getValue();
                if (lstDest == null || lstDest.isEmpty()) {
                    continue;
                }
                HandleResult insertResult = insertRrDetail(rrId, sourceId, lstDest, versionCode);
                if (!insertResult.isSuccess()) {
                    throw new IllegalOperatorException(insertResult.getErr());
                }
                result.setChangeNum(result.getChangeNum() + insertResult.getChangeNum());
            }

        }
        return result;
    }

    /**
     * 保存一个权限设置
     *
     * @param rrId        关系ID
     * @param sourceId    主权限数据ID
     * @param destNewIds  从权限数据ID
     * @param versionCode
     * @return
     */
    public HandleResult saveRightRelationDetail(long rrId, long sourceId, List<Long> destNewIds, String versionCode) {
        HandleResult existDetail = findRightRelationDetail(rrId, sourceId, versionCode);
        List<RightRelationDetailDto> lstDto = (List<RightRelationDetailDto>) existDetail.getData();
        List<Object> toDelete = new ArrayList<>();
        if (destNewIds == null) {
            destNewIds = new ArrayList<>();
        }
        long targetId;
        if (lstDto != null && !lstDto.isEmpty()) {
            for (RightRelationDetailDto dto : lstDto) {
                targetId = dto.getIdTarget();
                //如果新的不存在,则要删除
                if (destNewIds.indexOf(targetId) == -1) {
                    toDelete.add(dto.getRrDetailId());
                } else {
                    //如果有则在列表中删除
                    destNewIds.remove(targetId);
                }
            }

        }
        HandleResult result = HandleResult.success(0);
        //如果删除的不是空,则去删除
        if (!toDelete.isEmpty()) {
            HandleResult deleteResult = deleteRrDetail(rrId, toDelete, versionCode);
            if (!deleteResult.isSuccess()) {
                return deleteResult;
            }
            result.setChangeNum(deleteResult.getChangeNum());
        }
        if (destNewIds != null && !destNewIds.isEmpty()) {
            HandleResult insertResult = insertRrDetail(rrId, sourceId, destNewIds, versionCode);
            if (!insertResult.isSuccess()) {
                throw new IllegalOperatorException(insertResult.getErr());
            }
            result.setChangeNum(result.getChangeNum() + insertResult.getChangeNum());
        }
        return result;
    }

    private HandleResult deleteRrDetail(long rrId, List<Object> detailId, String versionCode) {
        DeleteParam param = new DeleteParam();
        TableInfo info = SchemaHolder.findTableByTableName(
                CommonUtils.getTableName(RightRelationDetailDto.class),
                Constants.DEFAULT_SYS_SCHEMA, versionCode);
        param.setTable(info);
        param.setIds(detailId);
        return handlerFactory.handleDelete(param);
    }

    private HandleResult insertRrDetail(long rrId, long sourceId, List<Long> lstTargetId, String versionCode) {
        InsertParam param = new InsertParam();
        long id = -1;
        List<RightRelationDetailDto> lstDto = new ArrayList<>();
        RightRelationDetailDto dto;
        for (Long targetId : lstTargetId) {
            dto = new RightRelationDetailDto();
            dto.setIdSource(sourceId);
            dto.setIdTarget(targetId);
            dto.setRrDetailId(id--);
            dto.setVersionCode(versionCode);
            dto.setRrId(rrId);
            lstDto.add(dto);
        }
        param.setObjects(lstDto, Constants.DEFAULT_SYS_SCHEMA);
        return handlerFactory.handleInsert(param);
    }

    /**
     * 根据权限资源ID,查询权限资源全信息
     *
     * @param lstId
     * @param versionCode
     * @return
     */
    public HandleResult findRightResources(List<Long> lstId, String versionCode) {
        if (lstId == null || lstId.isEmpty()) {
            return null;
        }
        QueryParam param = new QueryParam();
        param.setTableDtos(Constants.DEFAULT_SYS_SCHEMA, versionCode, RightSourceDto.class);
        param.appendCriteria().andIn("rs_id", lstId);
        return handlerFactory.handleQuery(param);
    }

}
