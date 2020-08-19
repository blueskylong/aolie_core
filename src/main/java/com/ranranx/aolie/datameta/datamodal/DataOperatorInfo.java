package com.ranranx.aolie.datameta.datamodal;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.datameta.dto.DataOperatorDto;
import com.ranranx.aolie.ds.dataoperator.DataSourceUtils;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/11 9:06
 * @Version V0.0.1
 **/
public class DataOperatorInfo {

    private DataOperatorDto operatorDto;

    public DataOperatorInfo(DataOperatorDto operatorDto) {
        this.operatorDto = operatorDto;
    }

    public DataOperatorDto getOperatorDto() {
        return operatorDto;
    }

    public void setOperatorDto(DataOperatorDto operatorDto) {
        this.operatorDto = operatorDto;
    }

    /**
     * 取得数据源的ID
     *
     * @return
     */
    public String getDsKey() {
        return DataSourceUtils.makeDsKey(operatorDto.getName(), operatorDto.getVersionCode());
    }

    /**
     * 取得缓存中的Key
     *
     * @return
     */
    public String getCacheKey() {
        return CommonUtils.makeKey(operatorDto.getId().toString(), operatorDto.getVersionCode());
    }


}
