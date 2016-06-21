package com.Jiakcs.API;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JKJsonReader {
	private JSONObject in = null;
	
	public JKJsonReader(String jsonData){
		in = JSONObject.parseObject(jsonData);
	}
	
	/// 将一个JSON对象转换为字符串
	public String ToString(){
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.ToString() = NULL ");
			return null;
		}
		return in.toJSONString();
	}
	
	/**
	 * 获取 APITYPE
	 * @return
	 */
	public String getAPITYPE(){
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getAPITYPE() = NULL ");
			return "";
		}
		String Result = in.getString("APITYPE");
		
		return Result == null ?"" : Result;
	}
	
	/**
	 * 获取 Token
	 * @return
	 */
	public String getToken(){
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getToken() = NULL ");
			return "";
		}
		String Result = in.getString("Token");
		
		return Result == null ?"" : Result;
	}
	
	/**
	 * 获取 ClientIP
	 * @return
	 */
	public String getClientIP(){
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getClientIP() = NULL ");
			return "";
		}
		String Result = in.getString("ClientIP");
		
		return Result == null ?"" : Result;
	}
	
	/**
	 * 获取 APIDATA
	 * @return
	 */
	public JSONObject getAPIDATA(){
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getClientIP() = NULL ");
			return null;
		}
		return in.getJSONObject("APIDATA");
	}
		
	/**
	 * 返回 节点值(不会返回NULL，NULL返回空串)
	 * @param KeyName 读取的Key节点
	 * @return 节点值
	 */
	public Integer getInteger( String KeyName ) {
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getInteger() = NULL ");
			return 0;
		}
		
		if ( in.getJSONObject("APIDATA") == null){
			JKRunLog.debugWrite("Call VTJsonReader.APIDATA = NULL ");
			return 0;
		}
		return in.getJSONObject("APIDATA").getInteger(KeyName) == null ?0 : in.getJSONObject("APIDATA").getInteger(KeyName);
	}
	

	/**
	 * 返回 节点值(不会返回NULL，NULL返回空串)
	 * @param ParKeyName 读取的父节点
	 * @param KeyName 读取的Key节点
	 * @return 节点值
	 */
	public Integer getInteger( String ParKeyName, String KeyName ) {
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getInteger() = NULL ");
			return 0;
		}
		
		if ( in.getJSONObject("APIDATA") == null){
			JKRunLog.debugWrite("Call VTJsonReader.APIDATA = NULL ");
			return 0;
		}
		
		if ( in.getJSONObject("APIDATA").getJSONObject(ParKeyName) == null){
			JKRunLog.debugWrite("Call VTJsonReader.getInteger() " + ParKeyName + " = NULL ");
			return 0;
		}
		return in.getJSONObject("APIDATA").getJSONObject(ParKeyName).getInteger(KeyName) == null ?0 : in.getJSONObject("APIDATA").getJSONObject(ParKeyName).getInteger(KeyName);
	}
	
	/**
	 * 返回 节点值(不会返回NULL，NULL返回空串)
	 * @param KeyName 读取的Key节点
	 * @return 节点值
	 */
	public String getEmptyString( String KeyName ) {
		String Result = getString(KeyName);
		return Result == null?"": Result;
	}
	

	/**
	 * 返回 节点值(不会返回NULL，NULL返回空串)
	 * @param in 要读取的 JSONObject
	 * @param ParKeyName 读取的父节点
	 * @param KeyName 读取的Key节点
	 * @return 节点值
	 */
	public String getEmptyString( String ParKeyName, String KeyName ) {
		String Result = getString(ParKeyName, KeyName);
		return Result == null?"": Result;
	}
	
	
	/**
	 * 返回 节点值
	 * @param in 要读取的 JSONObject
	 * @param KeyName 读取的Key节点
	 * @return 节点值
	 */
	public String getString( String KeyName ) {
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getString() = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA") == null){
			JKRunLog.debugWrite("Call VTJsonReader.APIDATA = NULL ");
			return null;
		}
		return in.getJSONObject("APIDATA").getString(KeyName);
	}
	
	/**
	 * 返回 节点值
	 * @param in 要读取的 JSONObject
	 * @param ParKeyName 读取的父节点
	 * @param KeyName 读取的Key节点
	 * @return 节点值
	 */
	public String getString( String ParKeyName, String KeyName ) {
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getString() = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA") == null){
			JKRunLog.debugWrite("Call VTJsonReader.APIDATA = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA").getJSONObject(ParKeyName) == null){
			JKRunLog.debugWrite("Call VTJsonReader.getString() " + ParKeyName + " = NULL ");
			return null;
		}
		return in.getJSONObject("APIDATA").getJSONObject(ParKeyName).getString(KeyName);
	}
	
	/**
	 * 返回 JSONArray
	 * @param in 要读取的 JSONObject
	 * @param KeyName 读取的Key节点
	 * @return JSONArray
	 */
	public JSONArray getArray( String KeyName ) {
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getArray() = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA") == null){
			JKRunLog.debugWrite("Call VTJsonReader.APIDATA = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA").getJSONArray(KeyName) == null){
			JKRunLog.debugWrite("Call VTJsonReader.getArray() " + KeyName + " = NULL ");
			return null;
		}
		return in.getJSONObject("APIDATA").getJSONArray("KeyName");
		
	}
	
	/**
	 * 返回 JSONArray
	 * @param in 要读取的 JSONObject
	 * @param ParKeyName 读取的父节点
	 * @param KeyName 读取的Key节点
	 * @return JSONArray
	 */
	public JSONArray getArray( String ParKeyName, String KeyName ) {
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getArray() = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA") == null){
			JKRunLog.debugWrite("Call VTJsonReader.getArray() APIDATA = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA").getJSONObject(ParKeyName) == null){
			JKRunLog.debugWrite("Call VTJsonReader.getArray() " + ParKeyName + " = NULL ");
			return null;
		}	

		if ( in.getJSONObject("APIDATA").getJSONObject(ParKeyName).getJSONArray("KeyName") == null){
			JKRunLog.debugWrite("Call VTJsonReader.getArray() " + ParKeyName + "." + KeyName + " = NULL ");
			return null;
		}
		
		return in.getJSONObject("APIDATA").getJSONObject(ParKeyName).getJSONArray("KeyName");		
	}
	
	/**
	 * 返回 JSONArray
	 * @param in 要读取的 JSONObject
	 * @param KeyName 读取的Key节点
	 * @return JSONArray
	 */
	public JSONObject getJSONObject( String KeyName ) {
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getJSONObject() = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA") == null){
			JKRunLog.debugWrite("Call VTJsonReader.getJSONObject().APIDATA = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA").getJSONObject(KeyName) == null){
			JKRunLog.debugWrite("Call VTJsonReader.getJSONObject().APIDATA. " + KeyName + " = NULL ");
			return null;
		}
		return in.getJSONObject("APIDATA").getJSONObject("KeyName");
		
	}
	
	/**
	 * 返回 JSONArray
	 * @param in 要读取的 JSONObject
	 * @param ParKeyName 读取的父节点
	 * @param KeyName 读取的Key节点
	 * @return JSONArray
	 */
	public JSONObject getJSONObject( String ParKeyName, String KeyName ) {
		if ( in == null){
			JKRunLog.debugWrite("Call VTJsonReader.getJSONObject() = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA") == null){
			JKRunLog.debugWrite("Call VTJsonReader.getJSONObject().APIDATA = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA").getJSONObject(ParKeyName) == null){
			JKRunLog.debugWrite("Call VTJsonReader.getJSONObject().APIDATA." + KeyName + " = NULL ");
			return null;
		}
		
		if ( in.getJSONObject("APIDATA").getJSONObject(ParKeyName).getJSONArray(KeyName) == null){
			JKRunLog.debugWrite("Call VTJsonReader.getJSONObject().APIDATA." + ParKeyName + "." + KeyName + " = NULL ");
			return null;
		}
		
		return in.getJSONObject("APIDATA").getJSONObject(ParKeyName).getJSONObject("KeyName");		
	}
}