package com.ranranx.aolie.core.fixrow.dto;

import javax.persistence.Table;
import com.ranranx.aolie.core.common.BaseDto;
/**
 * @author xxl 
 * @date 2021-08-10 17:52:37
 * @version 1.0
 */
@Table(name = "aolie_s_fix_relation_detail")
public class FixRelationDetail extends BaseDto implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long detailId;
	private Long fixRelationId;
	private Long fixColId;
	private Long columnId;
	public void setDetailId(Long detailId){
		this.detailId = detailId;
	}
	public Long getDetailId(){
		return this.detailId;
	}
	public void setFixRelationId(Long fixRelationId){
		this.fixRelationId = fixRelationId;
	}
	public Long getFixRelationId(){
		return this.fixRelationId;
	}
	public void setFixColId(Long fixColId){
		this.fixColId = fixColId;
	}
	public Long getFixColId(){
		return this.fixColId;
	}
	public void setColumnId(Long columnId){
		this.columnId = columnId;
	}
	public Long getColumnId(){
		return this.columnId;
	}

}