package com.ranranx.aolie.application.user.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;
/**
 * @author xxl 
 * @date 2021-01-08 13:43:06
 * @version 1.0
 */
@Table(name = "aolie_s_right_relation")
public class RightRelationDto extends BaseDto implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long rsIdFrom;
	private Long rsIdTo;
	private Long rrId;
	private Short transmitable;
	private String relationName;
	public void setRsIdFrom(Long rsIdFrom){
		this.rsIdFrom = rsIdFrom;
	}
	public Long getRsIdFrom(){
		return this.rsIdFrom;
	}
	public void setRsIdTo(Long rsIdTo){
		this.rsIdTo = rsIdTo;
	}
	public Long getRsIdTo(){
		return this.rsIdTo;
	}
	public void setRrId(Long rrId){
		this.rrId = rrId;
	}
	public Long getRrId(){
		return this.rrId;
	}
	public void setTransmitable(Short transmitable){
		this.transmitable = transmitable;
	}
	public Short getTransmitable(){
		return this.transmitable;
	}
	public void setRelationName(String relationName){
		this.relationName = relationName;
	}
	public String getRelationName(){
		return this.relationName;
	}

}