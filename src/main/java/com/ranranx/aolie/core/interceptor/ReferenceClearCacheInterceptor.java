package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.datameta.dto.ReferenceDto;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.param.OperParam;
import com.ranranx.aolie.core.service.DataModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * 引用数据缓存的处理, 当保存的表数据, 与引用表中的表名相同时, 则清除缓存相应的引用数据
 * @version V0.0.1
 * @date 2020/12/21 21:08
 **/
@DbOperInterceptor
public class ReferenceClearCacheInterceptor implements IOperInterceptor {
    @Autowired
    private CacheManager cacheManager;

    private Logger logger = LoggerFactory.getLogger(ReferenceClearCacheInterceptor.class);

    @Autowired
    private DataModelService dmService;

    private Map<String, ReferenceDto> referTables = null;

    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return !type.equals(Constants.HandleType.TYPE_QUERY);
    }


    @Override
    public HandleResult afterOper(OperParam param, String handleType,
                                  Map<String, Object> extendData, HandleResult result) {
        if (result.isSuccess()) {
            String tableName;
            String versionCode;
            TableInfo table = param.getTable();
            if (table == null) {
                return null;
            }
            tableName = table.getTableDto().getTableName();
            versionCode = table.getTableDto().getVersionCode();
            ReferenceDto tableInReference = findTableInReference(tableName, versionCode);
            if (tableInReference != null) {
                logger.info("&&清除缓存-->" + tableName);
                dmService.clearReferenceData(tableInReference.getRefId(), versionCode);
            }

        }
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
        return BASE_ORDER + 2;
    }
}
