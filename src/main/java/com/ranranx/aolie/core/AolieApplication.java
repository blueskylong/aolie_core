package com.ranranx.aolie.core;

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
public class AolieApplication {

    public static void main(String[] args) {
        SpringApplication.run(AolieApplication.class, args);
    }

}
