package com.ranranx.aolie.core.logger.serivce;

public interface DbLoggerService {
    /**
     * 信息级别
     *
     * @param message
     * @param sysId
     * @param userId
     */
    void info(String message, Integer sysId, Long userId);

    /**
     * 错误级别
     *
     * @param message
     * @param sysId
     * @param userId
     */
    void error(String message, Integer sysId, Long userId);

}
