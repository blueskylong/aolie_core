package com.ranranx.aolie.core.affix.dto;

public class AffixModel {

	public AffixModel(String fileRealName, String filePath, String fileName) {
		this.setFileName(fileName);
		this.setFileRealName(fileRealName);
		this.setFilePath(filePath);
	}

	public AffixModel(){
		
	}
	
	private String fileRealName = "";
	private String filePath = "";
	private String fileName = "";
	public String getFileRealName() {
		return fileRealName;
	}
	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
