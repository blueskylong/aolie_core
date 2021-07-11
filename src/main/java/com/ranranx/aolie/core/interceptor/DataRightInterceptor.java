package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.application.user.dto.RightResourceDto;
import com.ranranx.aolie.application.user.service.UserService;
import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.Ordered;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.datameta.dto.ReferenceDto;
import com.ranranx.aolie.core.datameta.dto.TableDto;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.*;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import com.ranranx.aolie.core.runtime.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.*;

/**
 * 数据权限拦截器,此拦截器,主要是根据用户权限,来给操作增加过滤条件,如果是增加,则要在内存中判断数据是否超权限范围
 * 如果数据表本身就是一个权限控制项,则也要增加数据的过滤(但要考虑一下,保存和删除操作时的判断 ,因为在没有的情况下,是不允许删除,比如,可能直接允许管理员修改数据等)
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/22 0029 9:05
 **/
@DbOperInterceptor
@Order(value = 10)
public class DataRightInterceptor implements IOperInterceptor, CommandLineRunner {
    /**
     * 权限ID与引用的关系
     */
    private Map<String, Long> mapRightIdToRefId;
    /**
     * 引用ID 与权限 的ID的关系
     */
    private Map<String, Long> mapRefIdToRightId;
    /**
     * 所有权限表 key:version+tableName value idFieldName
     */
    private Map<String, String> arrRightTable;
    @Autowired
    private HandlerFactory handlerFactory;

    @Autowired
    private UserService userService;

    /**
     * 所有的数据源请求都要处理
     *
     * @param type       处理类型
     * @param objExtInfo 额外信息
     * @return 是否可以处理
     */
    @Override
    public boolean isCanHandle(String type, Object objExtInfo) {
        return true;
    }

    @Override
    public HandleResult beforeOper(OperParam param, String handleType, Map<String, Object> globalParamData)
            throws InvalidException {
        LoginUser user = SessionUtils.getLoginUser();
        //没有登录的和超级管理员不做数据权限过滤
        if (user == null || Constants.UserType.superAdmin.equals(user.getUserType())) {
            return null;
        }
        if (Constants.HandleType.TYPE_QUERY.equalsIgnoreCase(handleType)) {
            return beforeQuery((QueryParam) param);
        } else if (Constants.HandleType.TYPE_INSERT.equalsIgnoreCase(handleType)) {
            return beforeInsert((InsertParam) param);
        } else if (Constants.HandleType.TYPE_UPDATE.equalsIgnoreCase(handleType)) {
            return beforeUpdate((UpdateParam) param);
        } else if (Constants.HandleType.TYPE_DELETE.equalsIgnoreCase(handleType)) {
            return beforeDelete((DeleteParam) param);
        }

        return null;
    }

    /**
     * 判断一张表是不是权限来源的表
     *
     * @param tableName
     * @return
     */
    private String getRightTableInfo(String tableName, String version) {
        return this.arrRightTable.get(version + "_" + tableName);
    }


    /**
     * 处理查询
     *
     * @param param 查询参数
     * @return 返回错误信息或空信息
     */
    private HandleResult beforeQuery(QueryParam param) {
        //没有登录的和超级管理员不做数据权限过滤
        if (param.isMaskDataRight()) {
            return null;
        }
        TableInfo[] tables = param.getTables();
        if (tables == null || tables.length < 1) {
            return null;
        }
        for (int i = 0; i < tables.length; i++) {
            TableInfo tableInfo = tables[i];
            //检查是不是权限表,如果是权限表,则直接添加条件
            //这里需要放弃 公共选项表,此表需要额外的条件进行过滤,所以暂时不处理
            if (!Constants.DefaultTableName.commonSelection.equalsIgnoreCase(tableInfo.getTableDto().getTableName())) {
                String info = this.getRightTableInfo(tableInfo.getTableDto().getTableName(),
                        tableInfo.getTableDto().getVersionCode());
                //如果有值,则说明此表是权限数据来源表
                if (info != null) {
                    //info格式是 idfield:rsid
                    String[] infos = info.split(":");
                    Set<Long> ids = SessionUtils.getLoginUser().getMapRights().get(Long.parseLong(infos[1]));
                    //如果没有权限数据,则直接返回空值
                    if (ids == null || ids.isEmpty()) {
                        return HandleResult.success(0);
                    }
                    param.appendCriteria()
                            .andIn(tableInfo.getTableDto().getTableName(), infos[0], CommonUtils.toList(ids));
                }
            }
            List<Column> lstColumn = findTableRightCols(tableInfo);
            if (lstColumn != null && !lstColumn.isEmpty()) {
                for (Column column : lstColumn) {
                    //一般情况下,查找到此用户此列的权限值,添加到条件中即可,
                    //考虑到另一些情况,权限数据量过多时,需要使用关联查询,--这种暂时不实现
                    Long rightId = getColumnRightId(column);
                    Set<Long> ids = SessionUtils.getLoginUser().getMapRights().get(rightId);
                    if (ids == null || ids.isEmpty()) {
                        //如果需要的权限用户没有,则直接返回空的结果
                        return HandleResult.success(0);
                    }
                    //如存在,则添加条件
                    param.appendCriteria()
                            .andIn(tableInfo.getTableDto().getTableName(),
                                    column.getColumnDto().getFieldName(), CommonUtils.toList(ids));
                }
            }
        }
        return null;
    }

    private Long getColumnRightId(Column column) {
        return this.mapRefIdToRightId.get(column.getColumnDto().getVersionCode()
                + "_" + column.getColumnDto().getRefId());
    }

    /**
     * 查询表中有权限限制的列
     *
     * @return
     */
    private List<Column> findTableRightCols(TableInfo tableInfo) {
        List<Column> result = new ArrayList<>();
        List<Column> lstColumn = tableInfo.getLstColumn();
        if (lstColumn == null || lstColumn.isEmpty()) {
            return null;
        }
        lstColumn.forEach(column -> {
            if (column.getColumnDto().getRefId() != null
                    && this.mapRefIdToRightId.containsKey(column.getColumnDto().getVersionCode() + "_" + column.getColumnDto())) {
                result.add(column);
            }
        });
        return result;
    }


    private void initRightInfo() {
        List<RightResourceDto> lstDto = userService.findAllRightSourceDto(null);
        this.mapRefIdToRightId = new HashMap<>();
        this.mapRightIdToRefId = new HashMap<>();
        this.arrRightTable = new HashMap<>();
        if (lstDto == null || lstDto.isEmpty()) {
            return;
        }
        lstDto.forEach(el -> {
            this.mapRightIdToRefId.put(el.getVersionCode() + "_" + el.getRsId(), el.getResourceId());
            this.mapRefIdToRightId.put(el.getVersionCode() + "_" + el.getResourceId(), el.getRsId());
            //查询权限对应的表,不重复保存
            ReferenceDto referenceDto = SchemaHolder.getReference(el.getResourceId(),
                    el.getVersionCode()).getReferenceDto();
            this.arrRightTable.put(el.getVersionCode() + "_" + referenceDto.getTableName(),
                    referenceDto.getIdField() + ":" + el.getRsId());
        });

    }

    /**
     * 处理更新
     *
     * @param param 更新参数
     * @return 返回错误信息或空信息
     */
    private HandleResult beforeUpdate(UpdateParam param) {
        return check(param.getTable(), param.getCriteria());
    }

    private HandleResult check(TableInfo tableInfo, Criteria criteria) {
        //检查是不是权限表,如果是权限表,则直接添加条件
        String info = this.getRightTableInfo(tableInfo.getTableDto().getTableName(),
                tableInfo.getTableDto().getVersionCode());
        if (info != null) {
            //如果是权限数据表,则增加条件
            //info格式是 idfield:rsid
            String[] infos = info.split(":");
            Set<Long> ids = SessionUtils.getLoginUser().getMapRights().get(infos[1]);
            if (ids == null || ids.isEmpty()) {
                //如果需要的权限用户没有,则直接返回空的结果
                return HandleResult.success(0);
            }
            criteria
                    .andIn(tableInfo.getTableDto().getTableName(), infos[0], CommonUtils.toList(ids));

        }
        List<Column> lstColumn = findTableRightCols(tableInfo);
        if (lstColumn != null && !lstColumn.isEmpty()) {
            String tableName = tableInfo.getTableDto().getTableName();
            for (Column column : lstColumn) {
                //一般情况下,查找到此用户此列的权限值,添加到条件中即可,
                //考虑到另一些情况,权限数据量过多时,需要使用关联查询,--这种暂时不实现
                Long rightId = getColumnRightId(column);
                Set<Long> ids = SessionUtils.getLoginUser().getMapRights().get(rightId);
                if (ids == null || ids.isEmpty()) {
                    //如果需要的权限用户没有,则直接返回空的结果
                    return HandleResult.success(0);
                }
                //如存在,则添加条件
                criteria
                        .andIn(tableName, column.getColumnDto().getFieldName(), CommonUtils.toList(ids));
            }
        }
        return null;
    }

    /**
     * 处理插入
     *
     * @param param 插入参数
     * @return 返回错误信息或空信息
     */
    private HandleResult beforeInsert(InsertParam param) {
        List<Map<String, Object>> lstRows = param.getLstRows();
        if (lstRows == null || lstRows.isEmpty()) {
            return null;
        }
        TableDto tableDto = param.getTable().getTableDto();
//        String info = this.getRightTableInfo(tableDto.getTableName(),
//                tableDto.getVersionCode());
//        if (info != null) {
//            //如果是权限数据表,则增加条件
//            //info格式是 idfield:rsid
//            //这里就需要处理什么样的用户可以增加这此权限 数据
//            if (SessionUtils.isSuperAdmin()) {
//                return null;
//            } else {
//                return HandleResult.failure("无权限增加权限数据");
//            }
//        }
        List<Column> lstColumn = findTableRightCols(param.getTable());
        if (lstColumn != null && !lstColumn.isEmpty()) {
            for (Column column : lstColumn) {
                //一般情况下,查找到此用户此列的权限值,添加到条件中即可,
                //考虑到另一些情况,权限数据量过多时,需要使用关联查询,--这种暂时不实现
                Long rightId = getColumnRightId(column);
                Set<Long> ids = SessionUtils.getLoginUser().getMapRights().get(rightId);
                if (ids == null || ids.isEmpty()) {
                    //如果需要的权限用户没有,则直接返回空的结果
                    return HandleResult.success(0);
                }
                //检查内存中的相关字段,是不是在权限中
                for (Map<String, Object> row : lstRows) {
                    Object value = row.get(column.getColumnDto().getFieldName());
                    //如果没有提供值,则不判断
                    if (value == null) {
                        continue;
                    }
                    if (!ids.contains(value)) {
                        return HandleResult.failure("超出插入的权限范围,表:" + tableDto.getTitle()
                                + " 列:" + column.getColumnDto().getTitle() + " 值:" + value + " 不在权限范围内,不允许插入");
                    }
                }

            }
        }
        return null;
    }

    /**
     * 处理删除
     *
     * @param param
     * @return 返回错误信息或空信息
     */
    private HandleResult beforeDelete(DeleteParam param) {
        return check(param.getTable(), param.getCriteria());
    }

    @Override
    public int getOrder() {
        return Ordered.BASE_ORDER - 25;
    }

    @Override
    public void run(String... args) throws Exception {
        initRightInfo();
    }


}
