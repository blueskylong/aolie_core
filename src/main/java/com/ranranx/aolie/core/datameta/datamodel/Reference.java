package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.dto.ReferenceDto;

import java.io.Serializable;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/8/5 17:34
 **/
public class Reference implements Serializable {
    private ReferenceDto referenceDto;

    /**
     * 是否按父子节点形成树结构
     *
     * @return
     */
    public boolean isHasParentIdField() {
        return CommonUtils.isNotEmpty(referenceDto.getParentField());
    }

    public Reference(ReferenceDto referenceDto) {
        this.referenceDto = referenceDto;
    }

    public ReferenceDto getReferenceDto() {
        return referenceDto;
    }

    public void setReferenceDto(ReferenceDto referenceDto) {
        this.referenceDto = referenceDto;
    }
}
