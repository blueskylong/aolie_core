package com.ranranx.aolie.application.user.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;
/**
 * @author xxl 
 * @date 2021-01-08 13:43:06
 * @version 1.0
 */
@Table(name = "aolie_s_right_relation_detail")
public class RightRelationDetailDto extends BaseDto implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long rrId;
	private Long idSource;
	private Long idTarget;
	private Long rrDetailId;
	public void setRrId(Long rrId){
		this.rrId = rrId;
	}
	public Long getRrId(){
		return this.rrId;
	}
	public void setIdSource(Long idSource){
		this.idSource = idSource;
	}
	public Long getIdSource(){
		return this.idSource;
	}
	public void setIdTarget(Long idTarget){
		this.idTarget = idTarget;
	}
	public Long getIdTarget(){
		return this.idTarget;
	}
	public void setRrDetailId(Long rrDetailId){
		this.rrDetailId = rrDetailId;
	}
	public Long getRrDetailId(){
		return this.rrDetailId;
	}

}