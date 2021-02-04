package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.Ordered;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.datameta.datamodel.validator.IValidator;
import com.ranranx.aolie.core.datameta.datamodel.validator.ValidatorCenter;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *  数据保存前的验证, 不包含约束.此类的更新检查依赖 UpdateRowIdInterceptor起作用
 * 由UpdateRowIdInterceptor将变动的ID都收集,这里检查这些数据的合法性
 * @date 2021/1/29 0029 9:05
 * @version V0.0.1
 **/
@DbOperInterceptor
public class ValidatorInterceptor implements IOperInterceptor {
    Logger logger = LoggerFactory.getLogger(ValidatorInterceptor.class);

    @Autowired
    protected List<IValidator> lstValidator;

    @Autowired
    private HandlerFactory handlerFactory;

    /**
     * 操作前调用,如果返回有内容,则会直接返回
     *
     * @param param
     * @return
     * @throws InvalidException
     */
    @Override
    public HandleResult beforeOper(Object param, String handleType,
                                   Map<String, Object> globalParamData) throws InvalidException {
        return null;
    }

    /**
     * 如果是更新,则校验更新后的数据
     *
     * @param param
     * @param handleType
     * @param globalParamData 扩展数据,所有的拦截器,可以多这里取数,也可以在这里放置数据,此数据贯穿整个处理过程
     * @param result
     * @return
     */
    @Override
    public HandleResult afterOper(Object param, String handleType, Map<String, Object> globalParamData, HandleResult result) {
        if (!result.isSuccess()) {
            return null;
        }
        if (Constants.HandleType.TYPE_UPDATE.equals(handleType)) {
            logger.info("表单验证--->保存后验证");
            String err = validateUpdate((UpdateParam) param, globalParamData);
            if (CommonUtils.isNotEmpty(err)) {
                return HandleResult.failure(err);
            }

        } else if (Constants.HandleType.TYPE_INSERT.equals(handleType) && param instanceof InsertParam) {
            //如果是插入,直接验证提供的数据
            String err = validateInsert((InsertParam) param, result);
            if (CommonUtils.isNotEmpty(err)) {
                return HandleResult.failure(err);
            }
        }
        return null;
    }

    /**
     * 检查插入
     *
     * @param handleResult 执行插入和公式计算后的结果
     * @return
     */
    private String validateInsert(InsertParam insertParam, HandleResult handleResult) {
        List<Map<String, Object>> lstData = findInsertRows(insertParam, handleResult);
        ValidatorCenter validatorCenter = insertParam.getTable().getValidatorCenter(this.lstValidator);
        return validateData(lstData, validatorCenter);
    }

    /**
     * 查询新插入的行数据
     *
     * @param handleResult
     * @return
     */
    private List<Map<String, Object>> findInsertRows(InsertParam param, HandleResult handleResult) {
        //根据约定,插入成功后,传回的是插入数据的ID数据
        List<Object> lstId = (List<Object>) handleResult.getLstData().get(0)
                .get(Constants.ConstFieldName.CHANGE_KEYS_FEILD);
        return queryByIds(lstId, param.getTable());
    }

    private String validateData(List<Map<String, Object>> lstData, ValidatorCenter validatorCenter) {

        StringBuilder errs = new StringBuilder();
        String err;
        for (Map<String, Object> row : lstData) {
            err = validatorCenter.validateAll(row);
            if (CommonUtils.isNotEmpty(err)) {
                errs.append(err);
            }
        }
        return errs.toString();
    }

    /**
     * 查询更新的行数据
     *
     * @param param
     * @param globalParamData
     * @return
     */
    private List<Map<String, Object>> findUpdateRows(UpdateParam param, Map<String, Object> globalParamData) {
        List<Object> lstKey = (List<Object>) globalParamData.get(GetRowIdInterceptor.PARAM_IDS);
        return queryByIds(lstKey, param.getTable());
    }

    private List<Map<String, Object>> queryByIds(List<Object> lstKey, TableInfo tableInfo) {
        if (lstKey == null || lstKey.isEmpty()) {
            return null;
        }
        QueryParam queryParam = new QueryParam();
        queryParam.setTable(new TableInfo[]{tableInfo});
        queryParam.appendCriteria().andIn(tableInfo.getKeyField(), lstKey);
        HandleResult result = handlerFactory.handleQuery(queryParam);
        if (result.isSuccess() && result.getLstData() != null) {
            return result.getLstData();
        }
        return null;
    }

    /**
     * 验证更新
     *
     * @param param
     * @param globalParamData
     * @return
     */
    private String validateUpdate(UpdateParam param, Map<String, Object> globalParamData) {

        List<Map<String, Object>> lstData = findUpdateRows(param, globalParamData);

        if (lstData != null && !lstData.isEmpty()) {
            return validateData(lstData, param.getTable().getValidatorCenter(this.lstValidator));
        }
        return null;

    }

    /**
     * 是否可以处理
     *
     * @param type
     * @param objExtinfo
     * @return
     */
    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return type == Constants.HandleType.TYPE_UPDATE
                || type == Constants.HandleType.TYPE_INSERT;
    }

    @Override
    public int getOrder() {
        return Ordered.BASE_ORDER + 20;
    }
}
