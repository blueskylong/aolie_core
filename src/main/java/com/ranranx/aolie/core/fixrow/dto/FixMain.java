package com.ranranx.aolie.core.fixrow.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;
/**
 * @author xxl 
 * @date 2021-08-10 17:52:37
 * @version 1.0
 */
@Table(name = "aolie_s_fix")
public class FixMain extends BaseDto implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long fixId;
	private String title;
	private String memo;
	private Long blockId;
	public void setFixId(Long fixId){
		this.fixId = fixId;
	}
	public Long getFixId(){
		return this.fixId;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return this.title;
	}
	public void setMemo(String memo){
		this.memo = memo;
	}
	public String getMemo(){
		return this.memo;
	}
	public void setBlockId(Long blockId){
		this.blockId = blockId;
	}
	public Long getBlockId(){
		return this.blockId;
	}

}