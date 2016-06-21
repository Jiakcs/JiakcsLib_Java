package com.Jiakcs.MySQL;

import com.Jiakcs.API.JKRunLog;

/**
 * 数据库字段表示类
 * @author Jiakcs
 *
 */
public class DBColStruct {
	private String colName = "";
	private Boolean colMainKey = false;
	private String colType = "S";
	private Object colValue = "";
	private Boolean colUpdateed = false;		// 字段是否存在更新
	
	/** 
	 * 构造函数,创建一个字段
	 * @param sName 字段名称
	 * @param sValue 字段值
	 */
	public DBColStruct(String sName,Object sValue){
		this(sName,sValue,"S");
	}
	
	/** 
	 * 构造函数,创建一个字段
	 * @param sName 字段名称
	 * @param sValue 字段值
	 */
	public DBColStruct(String sName,Object sValue,String sType){
		colName = sName;
		colValue = sValue;
		colType = sType;
	}
	
	/** 
	 * 获取字段是否主键
	 */
	public Boolean isMainKey(){
		return colMainKey;
	}
	
	/** 
	 * 获取字段是否更新
	 */
	public Boolean isUpdateed(){
		return colUpdateed;
	}
	
	/** 
	 * 指定字段是否主键
	 * @param bcolMainKey 是否主键
	 */
	public void setMainKey(Boolean bcolMainKey){
		setMainKey(bcolMainKey,"S");
	}
	
	/** 
	 * 指定字段是否主键
	 * @param bcolMainKey 是否主键
	 * @param colType 主键类型 S I
	 */
	public void setMainKey(Boolean bcolMainKey,String colType){
		colMainKey = bcolMainKey;
		colType = colType;
	}
	
	/** 
	 * 指定字段为主键
	 */
	public void setMainKey(){
		setMainKey(true,"S");
	}
	
	/** 
	 * 设置字段名
	 * @param sName 字段名
	 */
	public void setColName(String sName){
		colName = sName;
	}
	
	/** 
	 * 设置字段值
	 * @param sValue 字段值
	 */
	public void setColValue(Object sValue){
		colValue = sValue;
		colUpdateed = true;
	}
	
	/**
	 * 卸载资源
	 */
	public void dispose(){
		colValue = null;
	}
	
	/**
	 * 获取字段值
	 */
	public Object getColValue(){
		return colValue;
	}
	
	/**
	 * 获取字段名称
	 */
	public String getColName(){
		return colName;
	}
	
	/**
	 * 获取字段类型
	 */
	public String getColType(){
		return colType;
	}
	
	/**
	 * 获取字段是否主键
	 */
	public Boolean getColKey(){
		return colMainKey;
	}
	
	/**
	 * 获取字段字符串值
	 */
	public String getColValueString(){
		return colValue.toString();
	}
}
