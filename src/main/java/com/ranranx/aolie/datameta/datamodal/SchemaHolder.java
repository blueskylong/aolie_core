package com.ranranx.aolie.datameta.datamodal;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.datameta.dto.DataOperatorDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xxl
 * @Description 暂时使用这个来手动组装
 * @Date 2020/8/6 10:53
 * @Version V0.0.1
 **/
@Component
public class SchemaHolder {
    /**
     * 所有表信息
     */
    private static Map<String, Table> mapTables;
    /**
     * 所有字段信息
     */
    private static Map<String, Field> mapFields;
    /**
     * 表和字段对应信息
     */
    private static Map<String, Long> mapFieldToTable;

    /**
     * 数据库操作信息列表
     */
    private static Map<String, DataOperatorInfo> mapOperatorInfo;

    /**
     * 版本对库
     */
    private static Map<String, Schema> mapSchema;


    public SchemaHolder() {
        refresh();
    }

    /**
     * 取得并生成
     *
     * @param version
     * @return
     */
    public static Schema getSchema(String version) {
        if (mapSchema == null) {
            synchronized (SchemaHolder.class) {
                if (mapSchema == null) {
                    refresh();
                }
                return mapSchema.get(version);

            }
        }
        return mapSchema.get(version);
    }

    public static Field getField(Long fieldId, String version) {
        return mapFields.get(CommonUtils.makeKey(fieldId.toString(), version));
    }

    public static Table getTable(Long tableId, String version) {
        return mapTables.get(CommonUtils.makeKey(tableId.toString(), version));
    }

    public static DataOperatorInfo getDataOperatorInfo(Long id, String version) {
        return mapOperatorInfo.get(CommonUtils.makeKey(id.toString(), version));
    }


    /**
     * 初始化数据.
     */
    public static void refresh() {
        mapTables = new HashMap<>(20);
        mapFields = new HashMap<>(200);
        mapFieldToTable = new HashMap<>(200);
        mapSchema = new HashMap<>(200);
        mapOperatorInfo = new HashMap<>(200);

        DataOperatorDto dto = new DataOperatorDto();
        dto.setVersionCode("1");
        dto.setId(1L);
        dto.setName("mysql");
        DataOperatorInfo info = new DataOperatorInfo(dto);
        mapOperatorInfo.put(info.getCacheKey(), info);

        dto = new DataOperatorDto();
        dto.setVersionCode("1");
        dto.setId(2L);
        dto.setName("mysql2");
        info = new DataOperatorInfo(dto);
        mapOperatorInfo.put(info.getCacheKey(), info);
    }
}
