package com.ranranx.aolie.application.user.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;

/**
 * @author xxl
 * @version 1.0
 * @date 2021-01-08 13:46:51
 */
@Table(name = "aolie_s_user_right")
public class UserRightDto extends BaseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long userRightId;
    private Long userId;
    private Long rsId;
    private Long rsDetailId;

    public void setUserRightId(Long userRightId) {
        this.userRightId = userRightId;
    }

    public Long getUserRightId() {
        return this.userRightId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setRsId(Long rsId) {
        this.rsId = rsId;
    }

    public Long getRsId() {
        return this.rsId;
    }

    public void setRsDetailId(Long rsDetailId) {
        this.rsDetailId = rsDetailId;
    }

    public Long getRsDetailId() {
        return this.rsDetailId;
    }

}