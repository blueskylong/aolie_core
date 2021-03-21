package com.ranranx.aolie.core.affix.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;
/**
 * @author xxl 
 * @date 2021-03-18 17:14:19
 * @version 1.0
 */
@Table(name = "aolie_dm_affix")
public class AffixDto extends BaseDto implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long affixId;
	private Long columnId;
	private Long rowId;
	private String oraFilename;
	private Integer fileSize;
	private String fileName;
	private String fileExt;
	private String filePath;
	public void setAffixId(Long affixId){
		this.affixId = affixId;
	}
	public Long getAffixId(){
		return this.affixId;
	}
	public void setColumnId(Long columnId){
		this.columnId = columnId;
	}
	public Long getColumnId(){
		return this.columnId;
	}
	public void setRowId(Long rowId){
		this.rowId = rowId;
	}
	public Long getRowId(){
		return this.rowId;
	}
	public void setOraFilename(String oraFilename){
		this.oraFilename = oraFilename;
	}
	public String getOraFilename(){
		return this.oraFilename;
	}
	public void setFileSize(Integer fileSize){
		this.fileSize = fileSize;
	}
	public Integer getFileSize(){
		return this.fileSize;
	}
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	public String getFileName(){
		return this.fileName;
	}
	public void setFileExt(String fileExt){
		this.fileExt = fileExt;
	}
	public String getFileExt(){
		return this.fileExt;
	}
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	public String getFilePath(){
		return this.filePath;
	}

}