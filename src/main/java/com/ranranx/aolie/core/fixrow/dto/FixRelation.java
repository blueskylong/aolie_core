package com.ranranx.aolie.core.fixrow.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;
/**
 * @author xxl 
 * @date 2021-08-10 17:52:37
 * @version 1.0
 */
@Table(name = "aolie_s_fix_relation")
public class FixRelation extends BaseDto implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long fixRelationId;
	private Long fixId;
	private Long tableId;
	public void setFixRelationId(Long fixRelationId){
		this.fixRelationId = fixRelationId;
	}
	public Long getFixRelationId(){
		return this.fixRelationId;
	}
	public void setFixId(Long fixId){
		this.fixId = fixId;
	}
	public Long getFixId(){
		return this.fixId;
	}
	public void setTableId(Long tableId){
		this.tableId = tableId;
	}
	public Long getTableId(){
		return this.tableId;
	}

}
