package com.ranranx.aolie.core.plugs.model;

import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.exceptions.IllegalOperatorException;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.interfaces.IBaseDbService;
import com.ranranx.aolie.core.interfaces.IPlus;
import com.ranranx.aolie.core.plugs.dto.PlugDto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/6 0006 18:25
 **/
public abstract class BasePlug implements IPlus {
    protected final IBaseDbService service;

    private PlugDto dto;

    public BasePlug(IBaseDbService plugsService) {
        this.service = plugsService;
    }


    @Override
    public PlugDto getPlugInfo() {
        if (dto != null) {
            return dto;
        }
        PlugDto dto = new PlugDto();
        dto.setPlugUnitCode(getPlugCode());
        dto.setVersionCode(SessionUtils.getDefaultVersion());
        List<PlugDto> lstPlus = service.queryList(dto, Constants.DEFAULT_SYS_SCHEMA);
        if (lstPlus == null || lstPlus.isEmpty()) {
            this.dto = getDefaultDto();
        } else {
            this.dto = lstPlus.get(0);
            //版本检查
            if (isNewVersion(this.dto.getVersion(), getNewVersion())) {
                this.dto.setStatus(Constants.PlugStatus.OLD);
            } else {
                this.dto.setStatus(Constants.PlugStatus.INSTALLED);
            }
        }
        this.dto.setNewVersion(getNewVersion());

        return this.dto;
    }

    private boolean isNewVersion(String ver1, String ver2) {
        String[] arrVer1 = ver1.split("\\.");
        String[] arrVer2 = ver2.split("\\.");
        if (arrVer1.length != arrVer2.length || arrVer1.length != 3) {
            throw new InvalidParamException("版本信息不正确");
        }
        for (int i = 0; i < 3; i++) {
            int i1 = Integer.parseInt(arrVer1[i]);
            int i2 = Integer.parseInt(arrVer2[i]);
            if (i2 > i1) {
                return true;
            } else if (i2 < i1) {
                return false;
            }
        }
        return false;

    }

    /**
     * 取得插件标识码
     *
     * @return 标识码
     */
    protected abstract String getPlugCode();

    public PlugDto getDefaultDto() {
        PlugDto dto = new PlugDto();
        dto.setPlugUnitCode(getPlugCode());
        dto.setName(getPlugName());
        dto.setPlugId(IdGenerator.getNextId(PlugDto.class.getName()));
        dto.setVersionCode(SessionUtils.getDefaultVersion());
        dto.setVersion(getNewVersion());
        dto.setMemo(getMemo());
        dto.setStatus(Constants.PlugStatus.NEW);
        dto.setNewVersion(getNewVersion());
        return dto;
    }

    /**
     * 取得说明
     *
     * @return
     */
    protected abstract String getMemo();

    private void updateDto() {
        this.dto = null;
        getPlugInfo();
    }

    protected abstract String getPlugName();

    @Override
    public boolean install() {
        PlugDto plugInfo = getPlugInfo();
        if (!plugInfo.getStatus().equals(Constants.PlugStatus.NEW)) {
            throw new IllegalOperatorException("插件已安装，不需要再次安装");
        }
        boolean result = doInstall();
        if (result) {
            updateDto();
        }
        start();
        return result;
    }

    /**
     * 执行安装
     *
     * @return 是否成功
     */
    protected abstract boolean doInstall();


    @Override
    public boolean uninstall() {
        PlugDto plugInfo = getPlugInfo();
        if (plugInfo.getStatus().equals(Constants.PlugStatus.NEW)) {
            throw new IllegalOperatorException("插件未安装，不需要安装，请去除相关插件包即可");
        }
        stop();
        boolean result = doUninstall();

        if (result) {
            service.deleteById(dto, Constants.DEFAULT_SYS_SCHEMA);
            updateDto();
        }
        return result;
    }

    /**
     * 执行卸载
     *
     * @return 是否成功
     */
    protected abstract boolean doUninstall();

    @Override
    public boolean update() {
        PlugDto plugInfo = getPlugInfo();
        if (plugInfo.getStatus().equals(Constants.PlugStatus.NEW)) {
            throw new IllegalOperatorException("插件未安装，不需要更新");
        }
        if (plugInfo.getStatus().equals(Constants.PlugStatus.INSTALLED)) {
            throw new IllegalOperatorException("插件已是最新，不需要更新");
        }
        stop();
        boolean result = doUpdate();
        if (result) {
            updatePlugInfo();
            updateDto();
        }
        start();
        return result;
    }

    private void updatePlugInfo() {
        PlugDto plugInfo = getPlugInfo();
        plugInfo.setVersion(getNewVersion());
        plugInfo.setStatus(Constants.PlugStatus.INSTALLED);
        plugInfo.setInstallTime(new Date());
        service.updateByIds(Arrays.asList(plugInfo), true, Constants.DEFAULT_SYS_SCHEMA);
    }

    /**
     * 执行更新
     *
     * @return 是否成功
     */
    protected abstract boolean doUpdate();


    @Override
    public String check() {
        return null;
    }

    @Override
    public String repair() {
        PlugDto plugInfo = getPlugInfo();
        if (plugInfo.getStatus().equals(Constants.PlugStatus.NEW)) {
            throw new IllegalOperatorException("插件未安装，不需要修复");
        }
        return doRepair();
    }

    protected void insertPlugInfo() {
        PlugDto plugInfo = getPlugInfo();
        plugInfo.setStatus(Constants.PlugStatus.INSTALLED);
        plugInfo.setInstallTime(new Date());
        plugInfo.setPlugId(-1L);
        service.insert(plugInfo, Constants.DEFAULT_SYS_SCHEMA);
    }

    /**
     * 执行修复
     *
     * @return 错误信息
     */
    protected abstract String doRepair();

    protected abstract void stop();

    protected abstract void start();

    @Override
    public void updatePlug() {
        PlugDto plugInfo = getPlugInfo();
        if (plugInfo.getStatus() != Constants.PlugStatus.NEW) {
            start();
        } else {
            stop();
        }
    }
}
