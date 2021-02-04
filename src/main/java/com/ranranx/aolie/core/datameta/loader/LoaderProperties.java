package com.ranranx.aolie.core.datameta.loader;

import com.ranranx.aolie.core.datameta.dto.DataOperatorDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author xxl
 *  默认设置一个数据连接, 如果是数据库连接, 则固定为default_dataSource, 并存储在动态数据源里
 * @date 2020/8/13 17:16
 * @version V0.0.1
 **/
@PropertySource("aolie.properties")
@Component
@ConfigurationProperties(prefix = "aolie.loader")
public class LoaderProperties extends DataOperatorDto {


}
