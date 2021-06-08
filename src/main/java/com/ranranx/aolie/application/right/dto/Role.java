package com.ranranx.aolie.application.right.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;
/**
 * @author xxl 
 * @date 2021-06-07 09:25:50
 * @version 1.0
 */
@Table(name = "aolie_s_role")
public class Role extends BaseDto implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long roleId;
	private String roleName;
	private Integer xh;
	private Integer roleType;
	private String lvlCode;
	private Short enabled;
	public void setRoleId(Long roleId){
		this.roleId = roleId;
	}
	public Long getRoleId(){
		return this.roleId;
	}
	public void setRoleName(String roleName){
		this.roleName = roleName;
	}
	public String getRoleName(){
		return this.roleName;
	}
	public void setXh(Integer xh){
		this.xh = xh;
	}
	public Integer getXh(){
		return this.xh;
	}
	public void setRoleType(Integer roleType){
		this.roleType = roleType;
	}
	public Integer getRoleType(){
		return this.roleType;
	}
	public void setLvlCode(String lvlCode){
		this.lvlCode = lvlCode;
	}
	public String getLvlCode(){
		return this.lvlCode;
	}
	public void setEnabled(Short enabled){
		this.enabled = enabled;
	}
	public Short getEnabled(){
		return this.enabled;
	}

}