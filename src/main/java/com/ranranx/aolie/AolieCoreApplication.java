package com.ranranx.aolie;

import com.ranranx.aolie.core.common.CommonUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * For xxl the 40Th
 *
 * @version V0.0.1
 * @date 2020-08-04 11:10
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AolieCoreApplication {

    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            for (String param : args) {
                String[] p = param.split(":");
                if (p.length > 1) {
                    CommonUtils.addGlobalParam(p[0], p[1]);
                } else {
                    CommonUtils.addGlobalParam(param, param);
                }
            }
        }
        SpringApplication.run(AolieCoreApplication.class, args);
    }

}
