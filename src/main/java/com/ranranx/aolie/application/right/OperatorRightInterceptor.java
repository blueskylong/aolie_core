package com.ranranx.aolie.application.right;

import com.ranranx.aolie.application.menu.dto.MenuButtonDto;
import com.ranranx.aolie.application.user.service.UserService;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.Ordered;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.param.DeleteParam;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import com.ranranx.aolie.core.interceptor.IOperInterceptor;
import com.ranranx.aolie.core.interfaces.ICacheRefTableChanged;
import com.ranranx.aolie.core.runtime.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 操作权限拦截器,
 * 主要任务是判断当前的表操作是不是有权限
 * 判断的依据是:
 * 1.读操作,不做限制.由查询条件限制
 * 2.其它操作,如增,删,改等操作,检查没有配置相应的操作,如果配置了,并且授权给了此用户,则可以继续. (思考:是否还要增加例外表?)
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/4 0029 9:05
 **/
//@DbOperInterceptor
public class OperatorRightInterceptor implements IOperInterceptor, ICacheRefTableChanged, ApplicationListener<ApplicationReadyEvent> {

    private Logger logger = LoggerFactory.getLogger(OperatorRightInterceptor.class);
    @Autowired
    private UserService userService;

    /**
     * 表操作对应权限(按钮ID)
     * 所有存在权限设置的按钮 key:version+tableId+operId, value:此表此操作的按钮列表. 因为同一个操作,可能在不同的功能里,所以可能会出现多次.
     * 而出现一次,就认定有权限
     * 这个数据只可以判断各个操作分开的情况,如存在删除按钮 ,增加按钮等.而有些情况,如只有树勾选操作,这时只有一个保存按钮,这个数据集就无法判断是否有权限
     */
    private Map<String, Set<Long>> mapOperatorToBtn = null;
    /**
     * 表对就所有操作
     * 当根据上一数据集无法判断是否有权限里,则使用此数据集辅助判断.这里的判断规则是,如果此数据表没有指定相关的操作按钮(删除,修改),而有保存按钮,
     * 则默认用户有相关的操作权限.例:权限设置功能里没有删除按钮,有保存按钮,则允许用户删除数据,也没有增加按钮,也是允许用户增加操作的.
     * 保存分为几种,一种是保存一行和多行,处理相同,还有一种是保存级次,如果没有修改按钮,则只允许更新,而不可以删除和增加
     */
    private Map<String, Set<Long>> mapTableToOperator = null;

    /**
     * 不是查询的都处理
     *
     * @param type       处理类型
     * @param objExtInfo 额外信息
     * @return 是否可以处理
     */
    @Override
    public boolean isCanHandle(String type, Object objExtInfo) {
        return !Constants.HandleType.TYPE_QUERY.equals(type);
    }

    @Override
    public HandleResult beforeOper(Object param, String handleType,
                                   Map<String, Object> globalParamData) throws InvalidException {
        //判断此用户有没有授予此权限
        LoginUser loginUser = SessionUtils.getLoginUser();
        //未登录的,不处理
        if (loginUser == null) {
            return null;
        }
        String[] keys = getOperatorRightKey(param, handleType);
        String rightKey = keys[1];
        Set<Long> lstUserRights = loginUser.getMapRights().get(Constants.DefaultRsIds.menuButton);
        //如果系统中已定义此操作
        if (this.mapOperatorToBtn.containsKey(rightKey)) {

            //找到当前用户授予的所有按钮信息

            //如果存在授予,则通过
            if (lstUserRights != null && containAnyElement(lstUserRights, this.mapOperatorToBtn.get(rightKey))) {
                return null;
            } else {
                logger.error("非法操作数据,userId:" + loginUser.getUserId()
                        + ",操作类型" + handleType + ",操作键:" + rightKey);
                return HandleResult.failure("非法操作数据");
            }
        } else {
            //如果系统没有定义此表的此操作,则要检查是不是存在保存按钮,
            String tableKey = keys[0];
            if (!mapTableToOperator.containsKey(tableKey)) {
                //如果没有定义任何操作的,是不不允许直接操作//TODO 这里可以做成一个开关参数
                logger.error("未定义操作的表进行了操作,userId:" + loginUser.getUserId()
                        + ",操作类型" + handleType + ",操作键:" + tableKey);
                return HandleResult.failure("此表不可以操作");
            } else {
                //如果此表定义了保存(多行保存)操作,并且此用户有此权限,则为合法操作
                Set<Long> lstTableAllOperator = mapTableToOperator.get(tableKey);

                //有保存级次的权限,则可以做更新操作
                if (Constants.HandleType.TYPE_UPDATE.equalsIgnoreCase(handleType)
                        && hasSaveLevelOperator(lstTableAllOperator)) {
                    if (lstUserRights != null && containAnyElement(lstUserRights,
                            this.mapOperatorToBtn.get(tableKey + "_" + Constants.TableOperType.saveLevel))) {
                        return null;
                    }
                }
                //没有相应的操作定义 ,但有保存按钮权限,而此用户有此权限,则可以操作
                if (hasSaveOperator(lstTableAllOperator)) {
                    //检查用户是否有此权限
                    if (lstUserRights != null && containAnyElement(lstUserRights,
                            this.mapOperatorToBtn.get(tableKey + "_" + Constants.TableOperType.saveSingle))) {
                        return null;
                    }
                }
                logger.error("定义保存的表,用户未授予极限,userId:" + loginUser.getUserId()
                        + ",操作类型" + handleType + ",操作键:" + tableKey);
                return HandleResult.failure("无保存权限");

            }

        }

    }

    /**
     * 是否存在保存按钮
     *
     * @param allOperator
     * @return
     */
    private boolean hasSaveOperator(Set<Long> allOperator) {
        return allOperator.contains(Constants.TableOperType.saveMulti.longValue())
                || allOperator.contains(Constants.TableOperType.saveSingle.longValue());
    }

    /**
     * 是否存在保存级次按钮
     *
     * @param allOperator
     * @return
     */
    private boolean hasSaveLevelOperator(Set<Long> allOperator) {
        return allOperator.contains(Constants.TableOperType.saveLevel.longValue());
    }

    /**
     * 判断是否含有任何一个指定元素
     *
     * @param hasRight   全集
     * @param needRights 指定集
     * @return 是否含有
     */
    private boolean containAnyElement(Set<Long> hasRight, Set<Long> needRights) {
        for (Long id : needRights) {
            if (hasRight.contains(id)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 取得权限键
     *
     * @param param
     * @param handleType
     * @return
     */
    private String[] getOperatorRightKey(Object param, String handleType) {
        TableInfo tableInfo = null;
        Integer operType = null;
        //更新分二个,所以特殊处理
        if (Constants.HandleType.TYPE_UPDATE.equalsIgnoreCase(handleType)) {
            UpdateParam updateParam = (UpdateParam) param;
            tableInfo = updateParam.getTable();
            //多行编辑已改成了单行编辑,所以这里只需要处理单行编辑
            operType = Constants.TableOperType.edit;
        } else if (Constants.HandleType.TYPE_INSERT.equalsIgnoreCase(handleType)) {
            //插入
            InsertParam insertParam = (InsertParam) param;
            tableInfo = insertParam.getTable();
            operType = Constants.TableOperType.add;
        } else {
            DeleteParam deleteParam = (DeleteParam) param;
            tableInfo = deleteParam.getTable();
            operType = Constants.TableOperType.delete;
        }
        return new String[]{SessionUtils.getLoginVersion() + "_" + tableInfo.getTableDto().getTableId(),
                SessionUtils.getLoginVersion() + "_" + tableInfo.getTableDto().getTableId() + "_" + operType};

    }

    @Override
    public int getOrder() {
        //确保在计算公式的后面
        return Ordered.BASE_ORDER - 30;
    }

    @Override
    public List<String> getCareTables() {
        return Arrays.asList(CommonUtils.getTableName(MenuButtonDto.class));
    }

    @Override

    public void refresh(String tableName) {
        initOperatorRights();
    }


    public void initOperatorRights() {
        Map<String, Set<Long>>[] allButtonsOperator = userService.findAllButtonsOperator();
        this.mapOperatorToBtn = allButtonsOperator[0];
        this.mapTableToOperator = allButtonsOperator[1];
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initOperatorRights();
    }
}
