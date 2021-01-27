package com.ranranx.aolie.core.tools;


import com.ranranx.aolie.core.common.CommonUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xlei @qq 251425887 @tel 13352818008\n");
 * @Email dlei0009@163.com\n");
 * modified by  xxl
 * @Description 这里是生成DTO的工具类, 做了修改
 * @Date 2020/12/9 22:18
 * @Version V0.0.1
 **/
public class DtoGenerator {
    public DtoGenerator() {
    }

    public static void main(String[] args) {
        DTOHelper dto = new DTOHelper("com.ranranx.aolie.application.user.dto", "aolie_s_right_resource");

        try {
            dto.createDto();
            System.out.println("==========恭喜你，生成DTO成功！==========");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    static String toCamel(String name) {
        name = CommonUtils.toCamelStr(name);
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    public static class DTOHelper {
        private String driverClass;
        private String jdbcUrl;
        private String user;
        private String password;
        private String domainPackage;
        private String tablePrefix;

        public DTOHelper(String packageName, String tablePreFix) {
            Properties props = new Properties();

            try {
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("aolie.properties");
                if (is != null) {
                    props.load(is);
                    this.driverClass = "com.mysql.jdbc.Driver";
                    this.jdbcUrl = props.getProperty("url");
                    this.user = props.getProperty("userName");
                    this.password = props.getProperty("password");
                    this.domainPackage = packageName;
                    this.tablePrefix = tablePreFix;
//                    this.mapperPackage = props.getProperty("dto.mapper");
                }
            } catch (IOException var3) {
                var3.printStackTrace();
            }

        }

        public void createDto() throws Exception {
            String databaseName = this.jdbcUrl.substring(this.jdbcUrl.lastIndexOf("/") + 1).split("\\?")[0];
            Class.forName(this.driverClass);
            Connection conn = DriverManager.getConnection(this.jdbcUrl, this.user, this.password);
            PreparedStatement pstmt = conn.prepareStatement("SELECT table_name FROM information_schema.TABLES WHERE table_schema = ?");
            pstmt.setString(1, databaseName);
            ResultSet rs = pstmt.executeQuery();
            ArrayList tables = new ArrayList();

            while (rs.next()) {
                tables.add(rs.getString(1));
            }

            Iterator iterator = tables.iterator();

            while (iterator.hasNext()) {
                String tableName = (String) iterator.next();
                if (tableName.indexOf(this.tablePrefix) != 0) {
                    continue;
                }
                pstmt = conn.prepareStatement("SELECT * FROM " + databaseName + "." + tableName + " LIMIT 0,1");
                ResultSetMetaData rd = pstmt.getMetaData();
                Map<String, Integer> columns = new LinkedHashMap();
                for (int i = 0; i < rd.getColumnCount(); ++i) {
                    columns.put(rd.getColumnName(i + 1), rd.getColumnType(i + 1));
                }
                String className = toCamel(tableName.toLowerCase());
                generatorDto(className, this.domainPackage, columns, tableName.toLowerCase());
            }

        }

        static Map<String, String> mapReservedField = new HashMap();

        static {
            mapReservedField.put("version_code", null);
            mapReservedField.put("create_date", null);
            mapReservedField.put("last_update_date", null);
            mapReservedField.put("create_user", null);
            mapReservedField.put("last_update_user", null);
        }

        private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public static void generatorDto(String className, String packageName, Map<String, Integer> columns, String tableName) {
            try {
                StringBuilder res = new StringBuilder();
                res.append("package " + packageName + ";\n\n");
                res.append("import javax.persistence.Table;\n");
                res.append("import com.ranranx.aolie.core.common.BaseDto;\n");
                res.append("/**\n");
                res.append(" * @author xxl \n");
                res.append(" * @date " + sdf.format(Calendar.getInstance().getTime()) + "\n");
                res.append(" * @version 1.0\n");
                res.append(" */\n");
                res.append("@Table(name = \"" + tableName + "\")\n");
                res.append("public class " + className + " extends BaseDto implements java.io.Serializable{\n\n");
                res.append("\tprivate static final long serialVersionUID = 1L;\n");
                Iterator iterator = columns.entrySet().iterator();

                Map.Entry map;
                String fieldType;
                String fieldName;
                String methodName;
                int i;
                while (iterator.hasNext()) {
                    map = (Map.Entry) iterator.next();
                    fieldType = toJavaType((Integer) map.getValue());
                    fieldName = ((String) map.getKey()).toLowerCase();
                    if (mapReservedField.containsKey(fieldName)) {
                        continue;
                    }

                    fieldName = CommonUtils.toCamelStr(fieldName);

                    res.append("\tprivate " + fieldType + " " + fieldName + ";\n");
                }

                iterator = columns.entrySet().iterator();

                while (iterator.hasNext()) {
                    map = (Map.Entry) iterator.next();
                    fieldType = toJavaType((Integer) map.getValue());
                    String oraFieldName = ((String) map.getKey()).toLowerCase();
                    if (mapReservedField.containsKey(oraFieldName)) {
                        continue;
                    }

                    fieldName = CommonUtils.toCamelStr(oraFieldName);
                    methodName = toCamel(oraFieldName);


                    res.append("\tpublic void set" + methodName + "(");
                    res.append(fieldType + " " + fieldName);
                    res.append("){\n");
                    res.append("\t\tthis." + fieldName + " = " + fieldName + ";\n");
                    res.append("\t}\n");
                    res.append("\tpublic " + fieldType + " get" + methodName + "(){\n");
                    res.append("\t\treturn this." + fieldName + ";\n");
                    res.append("\t}\n");
                }

                res.append("\n}");
                File file = new File("src/main/java/" + packageName.replaceAll("\\.", "/"));
                if (!file.exists()) {
                    file.mkdirs();
                }

                file = new File("src/main/java/" + packageName.replaceAll("\\.", "/") + File.separator + className + ".java");
                FileWriter fw = new FileWriter(file);
                fw.write(res.toString());
                fw.flush();
                fw.close();
            } catch (Exception var10) {
                var10.printStackTrace();
            }

        }


        private static String toJavaType(int dataType) {
            switch (dataType) {
                case -5:
                    return "Long";
                case 4:
                    return "Integer";
                case 5:
                    return "Short";
                case 8:
                    return "Double";
                case 12:
                    return "String";
                case 91:
                case 93:
                    return "java.util.Date";
                default:
                    return "String";
            }
        }
    }

}