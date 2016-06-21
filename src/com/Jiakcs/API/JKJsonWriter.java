package com.Jiakcs.API;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Jiakcs.MySQL.DBColStruct;
import com.Jiakcs.MySQL.DBRowStruct;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JKJsonWriter {
	JSONObject jsonObj = null;
	
	/**
	 * 使用APIHead初始化JSON对象,用作发送请求
	 * @param apiType 请求类型
	 */
	public JKJsonWriter(String apiType){
		initRequestJson(apiType);
	}
	
	/**
	 * 使用APIHead初始化JSON对象,用作返回结果
	 * @param strResult 返回结果 1 成功 0 失败
	 * @param strTip 返回提示信息 Result =0 返回信息 Result =1 返回空
	 */
	public JKJsonWriter(String strResult , String strTip){
		initResponseJson(strResult , strTip);
	}
	
	/**
	 * 返回JOSN对象
	 * @return
	 */
	public JSONObject getJson(){
		return jsonObj;
	}
	
	/**
	 * 返回JSON字符串
	 * @return
	 */
	public String getJsonString(){
		if ( jsonObj == null )	return "";
		//return jsonObj.toJSONString();
		return JSONObject.toJSONString(jsonObj, SerializerFeature.WriteMapNullValue);
	}
	
	/**
	 * 返回JSON字符串
	 * @return
	 */
	public String toJsonString(){
		return getJsonString();
	}
	
	/**
	 * 使用APIHead初始化JSON对象,用作发送请求
	 * @param apiType 请求类型
	 */
	public JKJsonWriter initRequestJson(String apiType) {
		if (jsonObj == null)
			jsonObj = new JSONObject();
		jsonObj.clear();

		jsonObj.put("APITYPE", apiType);
		jsonObj.put("Token", "LEARN_API");
		jsonObj.put("ClientIP", "127.0.0.1");
		jsonObj.put("APIDATA", new JSONObject());
		
		return this;
	}
	
	/**
	 * 使用APIHead初始化JSON对象,用作返回结果
	 * @param strResult 返回结果 1 成功 0 失败
	 * @param strTip 返回提示信息 Result =0 返回信息 Result =1 返回空
	 */
	public JKJsonWriter initResponseJson(String strResult , String strTip) {
		if (jsonObj == null)
			jsonObj = new JSONObject();
		jsonObj.clear();
		jsonObj.put("APIDATA", new JSONObject());
		
		this.put("Result",strResult );
		this.put("Tip",strTip );
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个键值
	 * @param Key 主键
	 * @param Value 值
	 */
	public JKJsonWriter put(String Key, String Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		jsonObj.getJSONObject("APIDATA").put(Key, Value);
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个键值
	 * @param Key 主键
	 * @param Value 值
	 */
	public JKJsonWriter put(String Key, int Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		jsonObj.getJSONObject("APIDATA").put(Key, Value);
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个数组
	 * @param Key 主键
	 * @param Value 数组值
	 */
	public JKJsonWriter put(String Key, JSONArray Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		jsonObj.getJSONObject("APIDATA").put(Key, Value);
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个 Map<String,Object>
	 * @param Key 主键
	 * @param Value 数组值
	 */
	public JKJsonWriter put(String Key, Map<String,Object> Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		jsonObj.getJSONObject("APIDATA").put(Key, JSONArray.toJSON(Value));
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个 List<Map>
	 * @param Key 主键
	 * @param Value 数组值
	 */
	public JKJsonWriter put(String Key, List<Map> Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		jsonObj.getJSONObject("APIDATA").put(Key, JSONArray.toJSON(Value));
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个 JSONObject
	 * @param Key 主键
	 * @param Value 数组值
	 */
	public JKJsonWriter put(String Key, JSONObject jo) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		jsonObj.getJSONObject("APIDATA").put(Key, jo);
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个 List<DBRowStruct> 数组
	 * @param Key 主键
	 * @param Value List<DBRowStruct>
	 */
	public JKJsonWriter putDb(String Key, List<DBRowStruct> Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		putObject(Key, new JSONArray());
		
		JSONArray Par = jsonObj.getJSONObject("APIDATA").getJSONArray(Key);
		if ( Par != null){
			for(DBRowStruct one : Value ){
				Par.add(one.getJson());
			}
		}
		
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一行数据的所有字段 DBRowStruct 
	 * @param Value DBRowStruct
	 */
	public JKJsonWriter putDb(DBRowStruct Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;		
		List<DBColStruct> colData = Value.getCols();
		for( DBColStruct one : colData ){
			jsonObj.getJSONObject("APIDATA").put(one.getColName(), one.getColValue());
		}
		return this;
	}
	
	/**
	 * 在 APIDATA ==> Key 下面添加 一行数据的所有字段 DBRowStruct 
	 * @param Key 主键
	 * @param Value DBRowStruct
	 */
	public JKJsonWriter putDb(String Key, DBRowStruct Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		putObject(Key, new JSONArray());
		
		JSONArray Par = jsonObj.getJSONObject("APIDATA").getJSONArray(Key);
		if ( Par != null){
			Par.add(Value.getJson());
		}
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个 Object
	 * @param Key 主键
	 * @param Value Object
	 */
	public JKJsonWriter putObject(String Key, Object Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		jsonObj.getJSONObject("APIDATA").put(Key, JSONObject.toJSON(Value));
		
		return this;
	}
	
	
	
	/**
	 * 在 APIDATA 区域内添加一个键值
	 * @param ParKey 父主键
	 * @param Key 主键
	 * @param Value 值
	 */
	public JKJsonWriter put(String ParKey, String Key, String Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		if (jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey) == null)
			jsonObj.getJSONObject("APIDATA").put(ParKey, new JSONObject());
		
		jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey).put(Key, Value);
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个数组
	 * @param ParKey 父主键
	 * @param Key 主键
	 * @param Value 数组值
	 */
	public JKJsonWriter put(String ParKey, String Key, JSONArray Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		if (jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey) == null)
			jsonObj.getJSONObject("APIDATA").put(ParKey, new JSONObject());
		
		jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey).put(Key, Value);
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个 Map<String,Object>
	 * @param ParKey 父主键
	 * @param Key 主键
	 * @param Value 数组值
	 */
	public JKJsonWriter put(String ParKey, String Key, Map<String,Object> Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		if (jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey) == null)
			jsonObj.getJSONObject("APIDATA").put(ParKey, new JSONObject());
		
		jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey).put(Key, JSONArray.toJSON(Value));
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个 List<Map>
	 * @param ParKey 父主键
	 * @param Key 主键
	 * @param Value 数组值
	 */
	public JKJsonWriter put(String ParKey, String Key, List<Map> Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		if (jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey) == null)
			jsonObj.getJSONObject("APIDATA").put(ParKey, new JSONObject());
		
		jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey).put(Key, JSONArray.toJSON(Value));
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个 List<Map>
	 * @param ParKey 父主键
	 * @param Key 主键
	 * @param Value 数组值
	 */
	public JKJsonWriter put(String ParKey, String Key, String childKey, List<Map> Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		if (jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey) == null)
			jsonObj.getJSONObject("APIDATA").put(ParKey, new JSONObject());
		if (jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey).getJSONObject(Key) == null)
			jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey).put(Key, new JSONObject());
		
		jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey).getJSONObject(Key).put(childKey, JSONArray.toJSON(Value));
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个 List<DBRowStruct> 数组
	 * @param Key 主键
	 * @param Value List<DBRowStruct>
	 */
	public JKJsonWriter putDb(String ParKey,String Key, List<DBRowStruct> Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		putObject(Key, new JSONArray());
		
		JSONArray Par = jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey).getJSONArray(Key);
		if ( Par != null){
			for(DBRowStruct one : Value ){
				Par.add(one.getJson());
			}
		}
		
		
		return this;
	}
	
	
	/**
	 * 在 APIDATA ==> Key 下面添加 一行数据的所有字段 DBRowStruct 
	 * @param Key 主键
	 * @param Value DBRowStruct
	 */
	public JKJsonWriter putDb(String ParKey,String Key, DBRowStruct Value) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		if (jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey) == null) return this;
		putObject(Key, new JSONArray());
		
		JSONArray Par = jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey).getJSONArray(Key);
		if ( Par != null){
			Par.add(Value.getJson());
		}
		
		return this;
	}
	
	/**
	 * 在 APIDATA 区域内添加一个 JSONObject
	 * @param ParKey 父主键
	 * @param Key 主键
	 * @param Value 数组值
	 */
	public JKJsonWriter put(String ParKey, String Key, JSONObject jo) {
		if ( jsonObj == null )	return this;
		if (jsonObj.getJSONObject("APIDATA") == null) return this;
		if (jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey) == null) return this;
		jsonObj.getJSONObject("APIDATA").getJSONObject(ParKey).put(Key, jo);
		
		return this;
	}
	
}
