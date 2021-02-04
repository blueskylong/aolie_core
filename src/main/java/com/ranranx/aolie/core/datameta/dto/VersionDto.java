package com.ranranx.aolie.core.datameta.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;

/**
 * @author xxl
 *
 * @date 2020/8/6 12:46
 * @version V0.0.1
 **/
@Table(name = "aolie_dm_version")
public class VersionDto extends BaseDto {
    private Long versionId;
    private String versionCode;
    private String versionName;
    private String memo;

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    @Override
    public String getVersionCode() {
        return versionCode;
    }

    @Override
    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
