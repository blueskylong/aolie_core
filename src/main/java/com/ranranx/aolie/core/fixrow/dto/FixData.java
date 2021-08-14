package com.ranranx.aolie.core.fixrow.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;
/**
 * @author xxl 
 * @date 2021-08-10 17:52:37
 * @version 1.0
 */
@Table(name = "aolie_s_fix_data")
public class FixData extends BaseDto implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long dataId;
	private Long fixId;
	private String lvlCode;
	private Short isInclude;
	private Short sumUp;
	private Short canDelete;
	private Short canInsert;
	private String c1;
	private String c2;
	private String c3;
	private String c4;
	private String c5;
	private String c6;
	private String c7;
	private String c8;
	private Integer n1;
	private Integer n2;
	private Integer n3;
	private Integer n4;
	private Integer n5;
	private Integer n6;
	private Integer n7;
	private String f1;
	private String f2;
	private String f3;
	private String f4;
	private String f5;
	private String f6;
	private String f7;
	private Long id1;
	private Long id2;
	private Long id3;
	private String itemName;
	public void setDataId(Long dataId){
		this.dataId = dataId;
	}
	public Long getDataId(){
		return this.dataId;
	}
	public void setFixId(Long fixId){
		this.fixId = fixId;
	}
	public Long getFixId(){
		return this.fixId;
	}
	public void setLvlCode(String lvlCode){
		this.lvlCode = lvlCode;
	}
	public String getLvlCode(){
		return this.lvlCode;
	}
	public void setIsInclude(Short isInclude){
		this.isInclude = isInclude;
	}
	public Short getIsInclude(){
		return this.isInclude;
	}
	public void setSumUp(Short sumUp){
		this.sumUp = sumUp;
	}
	public Short getSumUp(){
		return this.sumUp;
	}
	public void setCanDelete(Short canDelete){
		this.canDelete = canDelete;
	}
	public Short getCanDelete(){
		return this.canDelete;
	}
	public void setCanInsert(Short canInsert){
		this.canInsert = canInsert;
	}
	public Short getCanInsert(){
		return this.canInsert;
	}
	public void setC1(String c1){
		this.c1 = c1;
	}
	public String getC1(){
		return this.c1;
	}
	public void setC2(String c2){
		this.c2 = c2;
	}
	public String getC2(){
		return this.c2;
	}
	public void setC3(String c3){
		this.c3 = c3;
	}
	public String getC3(){
		return this.c3;
	}
	public void setC4(String c4){
		this.c4 = c4;
	}
	public String getC4(){
		return this.c4;
	}
	public void setC5(String c5){
		this.c5 = c5;
	}
	public String getC5(){
		return this.c5;
	}
	public void setC6(String c6){
		this.c6 = c6;
	}
	public String getC6(){
		return this.c6;
	}
	public void setC7(String c7){
		this.c7 = c7;
	}
	public String getC7(){
		return this.c7;
	}
	public void setC8(String c8){
		this.c8 = c8;
	}
	public String getC8(){
		return this.c8;
	}
	public void setN1(Integer n1){
		this.n1 = n1;
	}
	public Integer getN1(){
		return this.n1;
	}
	public void setN2(Integer n2){
		this.n2 = n2;
	}
	public Integer getN2(){
		return this.n2;
	}
	public void setN3(Integer n3){
		this.n3 = n3;
	}
	public Integer getN3(){
		return this.n3;
	}
	public void setN4(Integer n4){
		this.n4 = n4;
	}
	public Integer getN4(){
		return this.n4;
	}
	public void setN5(Integer n5){
		this.n5 = n5;
	}
	public Integer getN5(){
		return this.n5;
	}
	public void setN6(Integer n6){
		this.n6 = n6;
	}
	public Integer getN6(){
		return this.n6;
	}
	public void setN7(Integer n7){
		this.n7 = n7;
	}
	public Integer getN7(){
		return this.n7;
	}
	public void setF1(String f1){
		this.f1 = f1;
	}
	public String getF1(){
		return this.f1;
	}
	public void setF2(String f2){
		this.f2 = f2;
	}
	public String getF2(){
		return this.f2;
	}
	public void setF3(String f3){
		this.f3 = f3;
	}
	public String getF3(){
		return this.f3;
	}
	public void setF4(String f4){
		this.f4 = f4;
	}
	public String getF4(){
		return this.f4;
	}
	public void setF5(String f5){
		this.f5 = f5;
	}
	public String getF5(){
		return this.f5;
	}
	public void setF6(String f6){
		this.f6 = f6;
	}
	public String getF6(){
		return this.f6;
	}
	public void setF7(String f7){
		this.f7 = f7;
	}
	public String getF7(){
		return this.f7;
	}
	public void setId1(Long id1){
		this.id1 = id1;
	}
	public Long getId1(){
		return this.id1;
	}
	public void setId2(Long id2){
		this.id2 = id2;
	}
	public Long getId2(){
		return this.id2;
	}
	public void setId3(Long id3){
		this.id3 = id3;
	}
	public Long getId3(){
		return this.id3;
	}
	public void setItemName(String itemName){
		this.itemName = itemName;
	}
	public String getItemName(){
		return this.itemName;
	}

}