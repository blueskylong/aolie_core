package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.dto.ReferenceDto;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.param.DeleteParam;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import com.ranranx.aolie.core.service.DataModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 引用数据缓存的处理, 当保存的表数据, 与引用表中的表名相同时, 则清除缓存相应的引用数据
 * @Date 2020/12/21 21:08
 * @Version V0.0.1
 **/
@Component
public class ReferenceDataInterceptor implements IOperInterceptor {
    @Autowired
    private CacheManager cacheManager;

    private Logger logger = LoggerFactory.getLogger(ReferenceDataInterceptor.class);

    @Autowired
    private DataModelService dmService;

    private Map<String, ReferenceDto> referTables = null;

    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return !type.equals(Constants.HandleType.TYPE_QUERY);
    }

    @Override
    public HandleResult beforeOper(Object param) throws InvalidException {
        return null;
    }

    @Override
    public HandleResult afterOper(Object param, HandleResult result) {
        if (result.isSuccess()) {
            String tableName;
            String versionCode;
            if (param instanceof DeleteParam) {
                tableName = ((DeleteParam) param).getTable().getTableDto().getTableName();
                versionCode = ((DeleteParam) param).getTable().getTableDto().getVersionCode();
            } else if (param instanceof InsertParam) {
                tableName = ((InsertParam) param).getTable().getTableDto().getTableName();
                versionCode = ((InsertParam) param).getTable().getTableDto().getVersionCode();
            } else if (param instanceof UpdateParam) {
                tableName = ((UpdateParam) param).getTable().getTableDto().getTableName();
                versionCode = ((UpdateParam) param).getTable().getTableDto().getVersionCode();
            } else {
                return null;
            }
            ReferenceDto tableInReference = findTableInReference(tableName, versionCode);
            if (tableInReference != null) {
                logger.info("&&清除缓存-->" + tableName);
                dmService.clearReferenceData(tableInReference.getRefId(), versionCode);
            }

        }
        return null;
    }

    @Override
    public HandleResult beforeReturn(Object param, HandleResult handleResult) {
        return null;
    }

    private ReferenceDto findTableInReference(String tableName, String version) {
        if (this.referTables == null) {
            synchronized (tableName) {
                if (this.referTables == null) {
                    this.referTables = new HashMap<>();
                    String[] versions = SchemaHolder.getInstance().getAllVersionCode();
                    for (int i = 0; i < versions.length; i++) {
                        List<ReferenceDto> referenceDtos = SchemaHolder.getInstance()
                                .getReferenceDtos(versions[i]);
                        if (referenceDtos != null) {
                            for (ReferenceDto referenceDto : referenceDtos) {
                                this.referTables.put(CommonUtils.makeKey(referenceDto.getTableName().toLowerCase(),
                                        referenceDto.getVersionCode()),
                                        referenceDto);
                            }
                        }
                    }
                }

            }
        }
        return this.referTables.get(CommonUtils.makeKey(tableName.toLowerCase(), version));
    }

    @Override
    public int getOrder() {
        return 3002;
    }
}
