package com.ranranx.aolie.ds.dataoperator.multids;

import com.alibaba.druid.pool.DruidDataSource;
import com.ranranx.aolie.datameta.dto.DataOperatorDto;

import javax.sql.DataSource;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/11 15:46
 * @Version V0.0.1
 **/
public class DataSourceWrapper {
    private DataOperatorDto dto;

    private DataSource dataSource;


    private void init() {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(dto.getUrl());
        ds.setDriverClassName(dto.getDriverClassName());
        ds.setUsername(dto.getUserName());
        ds.setPassword(dto.getPassword());
        this.dataSource = ds;
    }

    public DataOperatorDto getDto() {
        return dto;
    }

    public void setDto(DataOperatorDto dto) {
        this.dto = dto;
        init();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
