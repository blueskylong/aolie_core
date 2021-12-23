package com.ranranx.aolie.core.logger.serivce.impl;

import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.logger.dto.LogInfo;
import com.ranranx.aolie.core.logger.serivce.DbLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/12/16 0016 10:00
 **/
@Service("DbLoggerServiceImpl")
@Transactional(readOnly = true)
public class DbLoggerServiceImpl implements DbLoggerService {
    @Autowired
    private HandlerFactory handlerFactory;


    /**
     * 信息级别
     *
     * @param message
     * @param sysId
     * @param userId
     */
    @Override
    @Transactional(readOnly = false)
    public void info(String message, Integer sysId, Long userId) {
        insertLog(message, sysId, userId, Constants.LoggerType.INFO);
    }

    /**
     * 错误级别
     *
     * @param message
     * @param sysId
     * @param userId
     */
    @Override
    @Transactional(readOnly = false)
    public void error(String message, Integer sysId, Long userId) {
        insertLog(message, sysId, userId, Constants.LoggerType.ERROR);

    }

    private void insertLog(String message, Integer sysId, Long userId, Constants.LoggerType loggerType) {
        LogInfo logInfo = new LogInfo();
        logInfo.setMemo(message);
        logInfo.setSysId(sysId);
        logInfo.setUserId(userId);
        logInfo.setLogType(loggerType.getValue());
        InsertParam insertParam = new InsertParam();
        insertParam.setObject(logInfo, Constants.DEFAULT_SYS_SCHEMA);
        handlerFactory.handleInsert(insertParam);
    }
}
