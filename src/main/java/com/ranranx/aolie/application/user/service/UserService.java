package com.ranranx.aolie.application.user.service;

import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.exceptions.IllegalOperatorException;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.service.DmDataService;
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
    private static final String USER_RESOURCE_TABLE_NAME = "aulie_s_user_right";

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
        return handlerFactory.handleRequest(Constants.HandleType.TYPE_QUERY, param);

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
}
