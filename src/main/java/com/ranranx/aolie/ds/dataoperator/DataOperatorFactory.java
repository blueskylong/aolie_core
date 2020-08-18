package com.ranranx.aolie.ds.dataoperator;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.exceptions.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/10 17:38
 * @Version V0.0.1
 **/
@Component("DataOperatorFactory")
public class DataOperatorFactory {

    @Autowired
    private Map<String, IDataOperator> mapDataOperator;


    public IDataOperator getDataOperatorByName(String name, String version) {
        IDataOperator iDataOperator = mapDataOperator.get(CommonUtils.makeKey(name, version));
        if (iDataOperator != null) {
            return iDataOperator;
        }
        throw new NotExistException("数据源名:" + name + ",版本:" + version + " 的数据源不存在");
    }

    public IDataOperator getDefaultDataOperator() {
        return mapDataOperator.get(DataSourceUtils.getDefaultDataSourceKey());
    }

    public IDataOperator getDataOperatorByName(String key) {
        if (key == null) {
            key = DataSourceUtils.getDefaultDataSourceKey();
        }
        IDataOperator iDataOperator = mapDataOperator.get(key);
        if (iDataOperator != null) {
            return iDataOperator;
        }
        throw new NotExistException("数据源:" + key + " 数据源不存在");
    }


}
