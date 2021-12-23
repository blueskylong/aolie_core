package com.ranranx.aolie.core.logger.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;
/**
 * @author xxl 
 * @date 2021-12-16 09:59:44
 * @version 1.0
 */
@Table(name = "aolie_s_log")
public class LogInfo extends BaseDto implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private Long userId;
	private Long schemaId;
	private Long tableId;
	private Integer resultSize;
	private String operType;
	private Integer logType;
	private String memo;
	private String path;
	private String operId;
	private Long logId;
	private Long lastTime;
	private Integer sysId;
	public void setStartTime(java.util.Date startTime){
		this.startTime = startTime;
	}
	public java.util.Date getStartTime(){
		return this.startTime;
	}
	public void setEndTime(java.util.Date endTime){
		this.endTime = endTime;
	}
	public java.util.Date getEndTime(){
		return this.endTime;
	}
	public void setUserId(Long userId){
		this.userId = userId;
	}
	public Long getUserId(){
		return this.userId;
	}
	public void setSchemaId(Long schemaId){
		this.schemaId = schemaId;
	}
	public Long getSchemaId(){
		return this.schemaId;
	}
	public void setTableId(Long tableId){
		this.tableId = tableId;
	}
	public Long getTableId(){
		return this.tableId;
	}
	public void setResultSize(Integer resultSize){
		this.resultSize = resultSize;
	}
	public Integer getResultSize(){
		return this.resultSize;
	}
	public void setOperType(String operType){
		this.operType = operType;
	}
	public String getOperType(){
		return this.operType;
	}
	public void setLogType(Integer logType){
		this.logType = logType;
	}
	public Integer getLogType(){
		return this.logType;
	}
	public void setMemo(String memo){
		this.memo = memo;
	}
	public String getMemo(){
		return this.memo;
	}
	public void setPath(String path){
		this.path = path;
	}
	public String getPath(){
		return this.path;
	}
	public void setOperId(String operId){
		this.operId = operId;
	}
	public String getOperId(){
		return this.operId;
	}
	public void setLogId(Long logId){
		this.logId = logId;
	}
	public Long getLogId(){
		return this.logId;
	}
	public void setLastTime(Long lastTime){
		this.lastTime = lastTime;
	}
	public Long getLastTime(){
		return this.lastTime;
	}
	public void setSysId(Integer sysId){
		this.sysId = sysId;
	}
	public Integer getSysId(){
		return this.sysId;
	}

}