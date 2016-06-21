package com.Jiakcs.MySQL;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.Jiakcs.API.JKRunLog;
import com.alibaba.fastjson.JSONObject;


/**
 * 数据库行操作类
 * @author Jiakcs
 *
 */
public class DBRowStruct{
	private static final long serialVersionUID = 1L;
	
	protected List<DBColStruct> colUpdateList = new ArrayList<DBColStruct>();
	protected String DBName = "";
	protected String TableName = "";

	protected Boolean isViewer = false;	// 是否视图
	protected Boolean rowUpdateed = false;	// 数据是否存在更新
	
	protected String mainKeyName = "";
	protected String mainKeyType = "";
	protected List<DBColStruct> colData = new ArrayList<DBColStruct>();
	
	/**
	 * 填充资源
	 */
	protected void fillObject(DBRowStruct newData){
		clearCols();
		for(int i=0;i<newData.colData.size();i++){
			colData.add(newData.colData.get(i));
		}
	}
	
	/**
	 * 清除字段信息
	 */
	protected void clearCols(){
		for(int i=0;i<colData.size();i++){
			colData.get(i).dispose();
			colData.set(i, null);
		}
		colData.clear();
	}
	
	/**
	 * 卸载资源
	 */
	public void dispose(){
		clearCols();
	}
	
	/**
	 * 增加要更新的数据键值对
	 * @param sName 字段名称
	 * @param sValue 值
	 * @param sType S 字符串, I 数字, B 布尔, D 日期, A 数组列表
	 */
	protected void putDataList(String sName,String sValue, String sType){
		colUpdateList.add(new DBColStruct(sName,sValue,sType));
	}
	
	/**
	 * 清空临时数据更新表
	 */
	protected void clearDataList(){
		for(int i=0;i<colUpdateList.size();i++){
			colUpdateList.get(i).dispose();
			colUpdateList.set(i, null);
		}
		colUpdateList.clear();
	}
	
	
	
	/**
	 * 添加新字段
	 * @param newCol 新字段
	 */
	protected void addNewCol(DBColStruct newCol){
		colData.add(newCol);
	}

	/**
	 * 添加新字段
	 * @param sName 字段名称
	 * @param sValue 字段值
	 */
	protected void addNewCol(String sName,Object sValue){
		addNewCol( new DBColStruct(sName,sValue));
	}

	/**
	 * 获取字段
	 * @param colName 字段名称
	 */
	private DBColStruct getCol(String colName){
		for(DBColStruct one : colData){
			if ( one.getColName().equals(colName))
				return one;
		}
		return null;
	}
	
	
	/**
	 * 获取主键字段
	 */
	private DBColStruct getMainKeyCol(){
		for(DBColStruct one : colData){
			if (one.isMainKey())
				return one;
		}
		return null;
	}
	
	/* 
	 * 更改字段的名称
	 * @param colName 字段名称
	 * @param newName 字段名称
	 */
	public void changeName(String colName, String newName){
		DBColStruct Item = getCol(colName);
		if (Item != null){
			Item.setColName(newName);
			isViewer = true;
		}
	}
	
	/* 
	 * 移除某个字段
	 * @param colName 字段名称
	 */
	public void RemoveName(String colName){
		for(int i=0;i<colData.size();i++ ){
			if ( colData.get(i).getColName().equals(colName)){
				colData.remove(i);
				break;
			}
		}
	}
	
	/**
	 * 设置字段的值
	 * @param colName 字段名称
	 * @param colValue 字段值
	 */
	protected void setValue(String colName, Object colValue){
		DBColStruct Item = getCol(colName);
		if (Item != null){
			Item.setColValue(colValue);
			rowUpdateed = true;
		}else{
			addNewCol(colName,colValue);
		}
	}
	
	/**
	 * 设置字段的值为随机的UUID
	 * @param colName 字段名称
	 */
	public void setUUID(String colName){
		setValue(colName, UUID.randomUUID().toString().replace("-",""));
	}
	
	/** 
	 * 指定字段是否主键
	 * @param colName 主键字段名称
	 * @param colType 主键字段类型
	 */
	protected void setMainKey(String colName,String colType){
		mainKeyName = colName;
		mainKeyType = colType;
	}
	
	/* 
	 * 获取字段是否为NULL
	 */
	public Boolean isNull(String colName){
		DBColStruct Item = getCol(colName);
		return Item==null?true:Item.getColValue()==null?true:false;
	}
	
	/* 
	 * 获取字段是否更新过
	 */
	protected Boolean isUpdate(String colName){
		DBColStruct Item = getCol(colName);
		return Item==null?false:Item.isUpdateed();
	}
	
	/* 
	 * 获取数据行的 Json 值
	 */
	public JSONObject getJson(){
		if ( colData == null) return null;
		if ( colData.size() < 1) return null;
		JSONObject Result = new JSONObject();
		for( DBColStruct one : colData ){
			Result.put(one.getColName(), one.getColValue());
		}
		return Result;
	}
	
	
	/* 
	 * 获取数据行的 Json 值
	 */
	public List<DBColStruct> getCols(){
		return colData;
	}
	
	/* 
	 * 获取字段的值:Integer
	 */
	public Integer getInteger(String colName){
		DBColStruct Item = getCol(colName);
		return Item==null?null:Item.getColValue()==null?null:(Integer)Item.getColValue();
	}
	
	/* 
	 * 获取字段的值:String
	 */
	public String getString(String colName){
		DBColStruct Item = getCol(colName);
		return Item==null?null:Item.getColValue()==null?null:Item.getColValueString();
	}
	/* 
	 * 获取字段的值:Date
	 */
	public Date getDate(String colName){
		DBColStruct Item = getCol(colName);
		return Item==null?null:Item.getColValue()==null?null:(Date)Item.getColValue();
	}
	/* 
	 * 获取字段的值:Blob
	 */
	public Blob getBlob(String colName){
		DBColStruct Item = getCol(colName);
		return Item==null?null:Item.getColValue()==null?null:(Blob)Item.getColValue();
	}
	/* 
	 * 获取字段的值:Float
	 */
	public Float getFloat(String colName){
		DBColStruct Item = getCol(colName);
		return Item==null?null:Item.getColValue()==null?null:(Float)Item.getColValue();
	}
	/* 
	 * 获取字段的值:Double
	 */
	public Double getDouble(String colName){
		DBColStruct Item = getCol(colName);
		return Item==null?null:Item.getColValue()==null?null:(Double)Item.getColValue();
	}
	
	/* 
	 * 获取字段的值:byte
	 */
	public byte getbyte(String colName){
		DBColStruct Item = getCol(colName);
		return Item==null?null:Item.getColValue()==null?null:(byte)Item.getColValue();
	}
	
	/* 
	 * 获取字段的值:byte[]
	 */
	public byte[] getbyteArray(String colName){
		DBColStruct Item = getCol(colName);
		return Item==null?null:Item.getColValue()==null?null:(byte[])Item.getColValue();
	}
	
	/**
	 * 更新数据到数据库
	 * @return 是否执行成功
	 */
	protected Boolean _doUpdate(){
		if ( isViewer){
			JKRunLog.debugWrite("不能对视图执行更新操作!");
			return false;
		}
		
		if ( mainKeyName.length() > 0){
			String mainKey_QueryFilter = String.format("%s_=_%s", mainKeyType, mainKeyName);
			
			DBEngine opaDb = new DBEngine(DBName);
			opaDb.openDB(TableName)
				.andQueryFilter(mainKey_QueryFilter, getString(mainKeyName));
			for(int i=0; i<colUpdateList.size(); i++){
				opaDb.setColValue(colUpdateList.get(i).getColType(),
						colUpdateList.get(i).getColName(), 
						colUpdateList.get(i).getColValueString());
			}
			Boolean opaResult = opaDb.doUpdate();
			
			opaDb.Dispose();
			opaDb = null;
			
			return opaResult;
		}else{
			JKRunLog.debugWrite("未找到主键信息,无法更新数据!");
		}
		return false;
	}
	
	/**
	 * 添加数据到数据库
	 * @return 是否执行成功
	 */
	protected Boolean _doInsert(){
		if ( isViewer){
			JKRunLog.debugWrite("不能对视图执行新增操作!");
			return false;
		}
		
		DBEngine opaDb = new DBEngine(DBName);
		opaDb.openDB(TableName);
		for (int i = 0; i < colUpdateList.size(); i++) {
			opaDb.setColValue(colUpdateList.get(i).getColType(),
					colUpdateList.get(i).getColName(), colUpdateList.get(i)
							.getColValueString());
		}
		Boolean opaResult = opaDb.doInsert();

		opaDb.Dispose();
		opaDb = null;

		return opaResult;
		
		
	}
	
	/**
	 * 删除数据从数据库
	 * @return 是否执行成功
	 */
	protected Boolean _doDelete(){
		if ( isViewer){
			JKRunLog.debugWrite("不能对视图执行删除操作!");
			return false;
		}

		if ( mainKeyName.length() > 0){
			String mainKey_QueryFilter = String.format("%s_=_%s", mainKeyType, mainKeyName);
			
			DBEngine opaDb = new DBEngine(DBName);
			Boolean opaResult = opaDb.openDB(TableName)
				.andQueryFilter(mainKey_QueryFilter, getString(mainKeyName))
				.doDelete();
			opaDb.Dispose();
			opaDb = null;
			
			return opaResult;
		}else{
			JKRunLog.debugWrite("未找到主键信息,无法删除数据!");
		}
		return false;
		
	}
	
}
