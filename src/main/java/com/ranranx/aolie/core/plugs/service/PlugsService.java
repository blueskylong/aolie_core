package com.ranranx.aolie.core.plugs.service;

import com.ranranx.aolie.core.interfaces.IBaseDbService;
import com.ranranx.aolie.core.plugs.dto.PlugDto;

import java.util.List;

/**
 * 插件管理服务
 */
public interface PlugsService extends IBaseDbService {

    /**
     * 取得插件信息
     *
     * @return
     */
    List<PlugDto> getPlugInfos();

    /**
     * 安装
     *
     * @return
     */
    boolean install(Long plugId);

    /**
     * 卸载
     *
     * @return
     */
    boolean uninstall(Long plugId);

    /**
     * 升级
     *
     * @return
     */
    boolean update(Long plugId);


    /**
     * 检查当前系统配置问题
     *
     * @return
     */
    String check(Long plugId);

    /**
     * 修复
     *
     * @return
     */

    String repair(Long plugId);


}
