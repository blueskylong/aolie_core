package com.ranranx.aolie.ds.dataoperator.mybatis;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.datameta.dto.DataOperatorDto;
import com.ranranx.aolie.ds.dataoperator.IDataOperator;
import com.ranranx.aolie.ds.dataoperator.multids.DynamicDataSource;
import com.ranranx.aolie.ds.definition.DeleteSqlDefinition;
import com.ranranx.aolie.ds.definition.InsertSqlDefinition;
import com.ranranx.aolie.ds.definition.QuerySqlDefinition;
import com.ranranx.aolie.ds.definition.UpdateSqlDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/11 10:12
 * @Version V0.0.1
 **/

public class MyBatisDataOperator implements IDataOperator {

    /**
     * 数据库操作接口信息
     */
    private DataOperatorDto dto;

    /**
     * 数据源ID
     */
    private String dsKey;

    @Autowired
    private MyBatisGeneralMapper mapper;

    /**
     * 查询
     *
     * @param querySqlDefinition
     * @RETURN
     */
    @Override
    public List<Map<String, Object>> select(QuerySqlDefinition querySqlDefinition) {
        Map<String, Object> map = new HashMap<>();
        map.put("sql", "select * from city where countrycode = #{countrycode}");
        map.put("countrycode", "AFG");
        DynamicDataSource.setDataSource(dsKey);
        return mapper.select(map);
    }

    /**
     * 删除
     *
     * @param deleteSqlDefinition
     * @return
     */
    @Override
    public int delete(DeleteSqlDefinition deleteSqlDefinition) {
        return 0;
    }

    /**
     * 更新
     *
     * @param updateSqlDefinition
     * @return
     */
    @Override
    public int update(UpdateSqlDefinition updateSqlDefinition) {
        return 0;
    }

    /**
     * 插入
     *
     * @param insertSqlDefinition
     * @return
     */
    @Override
    public int insert(InsertSqlDefinition insertSqlDefinition) {
        return 0;
    }

    /**
     * 取得连接的唯一标识,用来区分不同的数据源
     *
     * @return
     */
    @Override
    public String getKey() {
        return CommonUtils.makeKey(dto.getName(), dto.getVersionCode());
    }

    /**
     * 取得名称,这是定义此数据源的名字,待用
     *
     * @return
     */
    @Override
    public String getName() {
        return dto.getDsName();
    }

    /**
     * 取得版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return dto.getVersionCode();
    }

    public DataOperatorDto getDto() {
        return dto;
    }

    public void setDto(DataOperatorDto dto) {
        this.dto = dto;
        dsKey = CommonUtils.makeKey(dto.getName(), dto.getVersionCode());
    }
}
