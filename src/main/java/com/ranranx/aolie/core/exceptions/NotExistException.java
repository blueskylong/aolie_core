package com.ranranx.aolie.core.exceptions;

/**
 * @author xxl
 *
 * @date 2020/8/10 9:48
 * @version V0.0.1
 **/
public class NotExistException extends BaseException {

    public NotExistException(String errorInfo) {
        super("指定的目标不存在:" + errorInfo);

    }
}
