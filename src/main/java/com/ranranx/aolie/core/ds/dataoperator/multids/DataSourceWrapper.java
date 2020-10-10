package com.ranranx.aolie.core.ds.dataoperator.multids;

import com.alibaba.druid.pool.DruidDataSource;
import com.ranranx.aolie.core.datameta.dto.DataOperatorDto;

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

    public DataSourceWrapper() {

    }

    public DataSourceWrapper(DataOperatorDto dto) {
        this.setDto(dto);
    }

    private void init() {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(dto.getUrl());
        ds.setDriverClassName(dto.getDriverClassName());
        ds.setUsername(dto.getUserName());
        ds.setPassword(dto.getPassword());
        this.dataSource = ds;
    }

    /**
     * 是不是默认数据源
     *
     * @return
     */
    public boolean isDefault() {
        return (this.dto.getIsDefault() != null && this.dto.getIsDefault() == 1);
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
