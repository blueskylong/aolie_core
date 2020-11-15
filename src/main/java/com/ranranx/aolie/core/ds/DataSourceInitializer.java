package com.ranranx.aolie.core.ds;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.DataOperatorInfo;
import com.ranranx.aolie.core.datameta.dto.DataOperatorDto;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.dataoperator.IDataOperator;
import com.ranranx.aolie.core.ds.dataoperator.multids.DataSourceWrapper;
import com.ranranx.aolie.core.ds.dataoperator.multids.DynamicDataSource;
import com.ranranx.aolie.core.ds.dataoperator.mybatis.MyBatisDataOperator;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.exceptions.InvalidException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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
        dynamicDataSource.addDataSourceWrappers(createDataSourceWrapper(applicationContext, lstDop, dataOperatorFactory));
    }


    private List<DataSourceWrapper> createDataSourceWrapper(ApplicationContext applicationContext,
                                                            List<DataOperatorDto> lstDop, DataOperatorFactory dataOperatorFactory) {
        List<DataSourceWrapper> lstResult = new ArrayList<>();
        if (lstDop == null || lstDop.isEmpty()) {
            return lstResult;
        }
        DataSourceWrapper wrapper;
        for (DataOperatorDto dto : lstDop) {
            if (dto.getId() == 0) {
                continue;
            }
            DataOperatorInfo dataOperatorInfo = new DataOperatorInfo(dto);
            AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
            try {
                IDataOperator operator;
                if (dto.getOperatorClass() != null) {
                    operator = (IDataOperator) Class.forName(dto.getOperatorClass()).newInstance();
                } else {
                    operator = new MyBatisDataOperator(dto);
                }
                factory.autowireBean(operator);
//                factory.initializeBean(operator, dataOperatorInfo.getDsKey());
                lstResult.add(new DataSourceWrapper(dto));
                dataOperatorFactory.regDataOperator(dataOperatorInfo.getDsKey(), operator);
            } catch (Exception e) {
                throw new InvalidException("生成数据源失败:" + dto.getName());
            }
        }
        return lstResult;
    }

    private List<DataOperatorDto> findAllDataOperatorDto(IDataOperator dataOperator) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        List<String> lst = new ArrayList<>();
        lst.add(CommonUtils.getTableName(DataOperatorDto.class));
        queryParamDefinition.setTableNames(lst);
        return CommonUtils.convertCamelAndToObject(dataOperator.select(queryParamDefinition), DataOperatorDto.class);
    }
}
