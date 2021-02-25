package com.ranranx.aolie.application.right;

import com.ranranx.aolie.application.user.dto.RightResourceDto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限树,生成时,可检查权限树是不是出现循环
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/20 0020 10:43
 **/
public class RightNode implements Serializable {
    /**
     * 下级权限节点,根据关系 配置查询
     */
    private List<RightNode> lstSub = new ArrayList<>();

    private List<RightNode> lstParent = new ArrayList<>();

    /**
     * 权限ID
     */
    private Long rightId;
    /**
     * 权限名称
     */
    private String rightName;

    private RightResourceDto dto;

    public RightNode(RightResourceDto dto) {
        this.rightId = dto.getRsId();
        this.rightName = dto.getRsName();
    }

    public RightNode(Long id, String rightName) {
        this.rightId = id;
        this.rightName = rightName;
    }

    public List<RightNode> getLstSub() {
        return lstSub;
    }

    public void setLstSub(List<RightNode> lstSub) {
        this.lstSub = lstSub;
    }

    public void addSubNode(RightNode node) {
        lstSub.add(node);
    }

    public void addParentNode(RightNode node) {
        lstParent.add(node);
    }

    public Long getRightId() {
        return rightId;
    }

    public void setRightId(Long rightId) {
        this.rightId = rightId;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public RightResourceDto getDto() {
        return dto;
    }

    public void setDto(RightResourceDto dto) {
        this.dto = dto;
    }

    public List<RightNode> getLstParent() {
        return lstParent;
    }

    public void setLstParent(List<RightNode> lstParent) {
        this.lstParent = lstParent;
    }

}
