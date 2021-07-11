package com.ranranx.aolie.core.interfaces;

import com.ranranx.aolie.core.plugs.dto.PlugDto;

/**
 * @author xxl
 * 插件接口
 * @version V1.0
 * @date 2021/7/6 13:28
 **/
public interface IPlus {
    /**
     * 取得插件信息
     *
     * @return
     */
    PlugDto getPlugInfo();

    /**
     * 安装
     *
     * @return
     */
    boolean install();

    /**
     * 卸载
     *
     * @return
     */
    boolean uninstall();

    /**
     * 升级
     *
     * @return
     */
    boolean update();

    /**
     * 最新版本
     *
     * @return
     */
    String getNewVersion();

    /**
     * 检查当前系统配置问题
     *
     * @return
     */
    String check();

    /**
     * 修复
     *
     * @return
     */
    String repair();

    /**
     * 更新插件状态,如启用和停用
     */
    void updatePlug();

}
