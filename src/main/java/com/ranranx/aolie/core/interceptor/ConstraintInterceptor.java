package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.Ordered;
import com.ranranx.aolie.core.datameta.datamodel.Constraint;
import com.ranranx.aolie.core.datameta.datamodel.Schema;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaParse;
import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaTools;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 数据保存前的验证, 约束  已集成到ValidatorInterceptor拦截器中
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/4 0029 9:05
 **/
@DbOperInterceptor
public class ConstraintInterceptor implements IOperInterceptor {
    @Autowired
    private HandlerFactory handlerFactory;

    /**
     * 是否可以处理
     *
     * @param type       处理类型
     * @param objExtInfo 额外信息
     * @return 是否可以处理
     */
    @Override
    public boolean isCanHandle(String type, Object objExtInfo) {
        return Constants.HandleType.TYPE_UPDATE.equals(type) ||
                Constants.HandleType.TYPE_INSERT.equals(type);
    }

    /**
     * 这个拦截需要在计算公式拦截和GetRowIdInterceptor拦截器之后,检查的数据依赖其行号及所影响的表
     *
     * @param param           处理参数
     * @param handleType      处理类型
     * @param globalParamData 扩展数据,所有的拦截器,可以多这里取数,也可以在这里放置数据,此数据贯穿整个处理过程
     * @param result          处理结果
     * @return 如果存在问题, 则返回处理结果
     */
    @Override
    public HandleResult afterOper(Object param, String handleType, Map<String, Object> globalParamData,
                                  HandleResult result) {
        //取得当前表
        Map<Long, List<Map<String, Object>>> mapTableAndRows
                = (Map<Long, List<Map<String, Object>>>) globalParamData.get(CHANGED_TABLE_ROWS);
        if (mapTableAndRows == null || mapTableAndRows.isEmpty()) {
            return null;
        }
        Long schemaId = -1L;
        String version = null;
        if (Constants.HandleType.TYPE_UPDATE.equals(handleType)) {
            schemaId = ((UpdateParam) param).getTable().getTableDto().getSchemaId();
            version = ((UpdateParam) param).getTable().getTableDto().getVersionCode();
        } else if (Constants.HandleType.TYPE_INSERT.equals(handleType)) {
            schemaId = ((InsertParam) param).getTable().getTableDto().getSchemaId();
            version = ((InsertParam) param).getTable().getTableDto().getVersionCode();
        }
        Schema schema = SchemaHolder.getInstance().getSchema(schemaId, version);
        FormulaParse formulaParse = FormulaParse.getInstance(true, schema);
        //收集检查的数据
        Iterator<Map.Entry<Long, List<Map<String, Object>>>> iterator =
                mapTableAndRows.entrySet().iterator();
        for (; iterator.hasNext(); ) {
            Map.Entry<Long, List<Map<String, Object>>> next = iterator.next();
            Long tableId = next.getKey();
            List<Map<String, Object>> lstRows = next.getValue();
            if (lstRows == null || lstRows.isEmpty()) {
                continue;
            }
            //执行单表检查
            String err = checkOneTable(tableId, lstRows, schema, formulaParse);
            if (CommonUtils.isNotEmpty(err)) {
                return HandleResult.failure(err);
            }
        }
        return null;

    }

    private String checkOneTable(Long tableId, List<Map<String, Object>> tableRows,
                                 Schema schema, FormulaParse formulaParse) {
        //取得表的所有约束
        List<Constraint> lstConstraint = schema.findTableRefConstraints(tableId);
        if (lstConstraint == null || lstConstraint.isEmpty()) {
            return null;
        }
        //取得行数据

        if (tableRows == null || tableRows.isEmpty()) {
            return null;
        }
        //判定
        for (Map<String, Object> row : tableRows) {
            for (Constraint cons : lstConstraint) {
                String filter = cons.getConstraintDto().getFilter();
                String expression = cons.getConstraintDto().getExpression();
                //先计算条件是不是符合,再计算表达式是不是符合
                if (filter != null) {
                    String valueExp = formulaParse.transToValue(filter, tableId,
                            row, null, formulaParse, null);
                    if (Boolean.FALSE.equals(FormulaTools.calcExpresion(valueExp))) {
                        continue;
                    }
                }
                //计算表达式
                String valueExp = formulaParse.transToValue(expression, tableId,
                        row, null, formulaParse, null);
                if (Boolean.FALSE.equals(FormulaTools.calcExpresion(valueExp))) {
                    String errInfo = cons.getConstraintDto().getMemo();
                    if (CommonUtils.isEmpty(errInfo)) {
                        return "检查约束不通过.";
                    }
                    return errInfo;
                }
            }

        }
        return null;
    }

    @Override
    public int getOrder() {
        //确保在计算公式的后面
        return Ordered.BASE_ORDER + 50;
    }
}
