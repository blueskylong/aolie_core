package com.ranranx.aolie.exceptions;

/**
 * @Author xxl
 * @Description 基本异常类
 * @Date 2020/8/4 17:13
 * @Version V0.0.1
 **/
public class BaseException extends RuntimeException {
    public BaseException(String errorInfo) {
        super(errorInfo);
    }

}
