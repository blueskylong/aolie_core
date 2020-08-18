package com.ranranx.aolie.ds;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.datameta.dto.DataOperatorDto;
import com.ranranx.aolie.datameta.dto.TableDto;
import com.ranranx.aolie.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.ds.dataoperator.IDataOperator;
import com.ranranx.aolie.ds.dataoperator.multids.DataSourceWrapper;
import com.ranranx.aolie.ds.dataoperator.multids.DynamicDataSource;
import com.ranranx.aolie.ds.definition.DeleteParamDefinition;
import com.ranranx.aolie.ds.definition.InsertParamDefinition;
import com.ranranx.aolie.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.handler.param.DeleteParam;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 所有数据源初始化
 * @Date 2020/8/13 20:10
 * @Version V0.0.1
 **/
@Configuration
public class DataSourceInitializer implements ApplicationContextAware {

    private DynamicDataSource dynamicDataSource;

    public DataSourceInitializer(DynamicDataSource dynamicDataSource) {
        this.dynamicDataSource = dynamicDataSource;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataOperatorFactory dataOperatorFactory = (DataOperatorFactory) applicationContext.getBean("DataOperatorFactory");
        IDataOperator dataOperator = dataOperatorFactory.getDefaultDataOperator();
        List<DataOperatorDto> lstDop = findAllDataOperatorDto(dataOperator);

        dynamicDataSource.addDataSourceWrappers(createDataSourceWrapper(lstDop));
    }


    private List<DataSourceWrapper> createDataSourceWrapper(List<DataOperatorDto> lstDop) {
        List<DataSourceWrapper> lstResult = new ArrayList<>();
        if (lstDop == null || lstDop.isEmpty()) {
            return lstResult;
        }
        DataSourceWrapper wrapper;
        for (DataOperatorDto dto : lstDop) {
            lstResult.add(new DataSourceWrapper(dto));
        }
        return lstResult;
    }

    private List<DataOperatorDto> findAllDataOperatorDto(IDataOperator dataOperator) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableName(CommonUtils.getTableName(DataOperatorDto.class));
        return CommonUtils.convertCamelAndToObject(dataOperator.select(queryParamDefinition), DataOperatorDto.class);
    }
}
