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
 * @Author xxl
 * @Description 数据保存前的验证, 不包含约束.此类的更新检查依赖 UpdateRowIdInterceptor起作用
 * 由UpdateRowIdInterceptor将变动的ID都收集,这里检查这些数据的合法性
 * @Date 2021/1/29 0029 9:05
 * @Version V0.0.1
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
        //如果是插入,直接验证提供的数据
        if (Constants.HandleType.TYPE_INSERT.equals(handleType) && param instanceof InsertParam) {
            String err = validateInsert((InsertParam) param);
            if (CommonUtils.isNotEmpty(err)) {
                return HandleResult.failure(err);
            }
        }
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
        if (Constants.HandleType.TYPE_UPDATE.equals(handleType)) {
            logger.info("表单验证--->保存后验证");
            String err = validateUpdate((UpdateParam) param, globalParamData);
            if (CommonUtils.isNotEmpty(err)) {
                return HandleResult.failure(err);
            }

        }
        return null;
    }

    /**
     * 检查插入
     *
     * @param param
     * @return
     */
    private String validateInsert(InsertParam param) {
        List<Map<String, Object>> lstData = param.getLstRows();
        ValidatorCenter validatorCenter = param.getTable().getValidatorCenter(this.lstValidator);
        return validateData(lstData, validatorCenter);
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

    private String validateUpdate(UpdateParam param, Map<String, Object> globalParamData) {

        List<Object> lstKey = (List<Object>) globalParamData.get(UpdateRowIdInterceptor.PARAM_IDS);
        if (lstKey == null || lstKey.isEmpty()) {
            return null;
        }
        QueryParam queryParam = new QueryParam();
        queryParam.setTable(new TableInfo[]{param.getTable()});
        queryParam.appendCriteria().andIn(param.getTable().getKeyField(), lstKey);
        HandleResult result = handlerFactory.handleQuery(queryParam);
        if (result.isSuccess() && result.getLstData() != null) {
            return validateData(result.getLstData(), param.getTable().getValidatorCenter(this.lstValidator));
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
