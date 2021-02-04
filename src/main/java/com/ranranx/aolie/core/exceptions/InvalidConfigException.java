package com.ranranx.aolie.core.exceptions;

/**
 * @author xxl
 *
 * @date 2020/8/10 9:48
 * @version V0.0.1
 **/
public class InvalidConfigException extends BaseException {

    public InvalidConfigException(String errorInfo) {
        super("系统配置不正确:" + errorInfo);

    }
}
