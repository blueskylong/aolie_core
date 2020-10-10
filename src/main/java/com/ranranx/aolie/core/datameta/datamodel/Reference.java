package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.datameta.dto.ReferenceDto;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:34
 * @Version V0.0.1
 **/
public class Reference {
    private ReferenceDto referenceDto;

    public Reference() {

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
