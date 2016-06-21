package com.Jiakcs.API;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class JKHttp {

	/**
	 * 发送 JSON 完整请求,并等待返回数据
	 * 请求的字符串自动转码 Unicode
	 * @param httpUrl
	 * @param jsonData
	 */
	public static String PostUnicodeData(String httpUrl, String jsonData) {
		JKRunLog.debugWrite("C==>S: " + jsonData);	
		
		if( ! JKUnicode.isUnicode(jsonData))
			jsonData = JKUnicode.string2Unicode(jsonData);
		
		String responseMsg = Post( httpUrl, jsonData);
		
		JKRunLog.debugWrite("S==>C: " + responseMsg);		
		return responseMsg;
	}
	
	/**
	 * 发送 JSON 完整请求,并等待返回数据
	 * 
	 * @param httpUrl
	 * @param jsonData
	 * @return 返回 Unicode 解码后的 String
	 */
	public static String PostUnicode2String(String httpUrl, String jsonData) {
		JKRunLog.debugWrite("C==>S: " + jsonData);	
		
		if( ! JKUnicode.isUnicode(jsonData))
			jsonData = JKUnicode.string2Unicode(jsonData);
		String responseMsg = Post( httpUrl, jsonData);
		if(JKUnicode.isUnicode(responseMsg))
			responseMsg = JKUnicode.unicode2String(responseMsg);
		

		JKRunLog.debugWrite("S==>C: " + responseMsg);
		
		return responseMsg;
	}
	
	private static String Post(String httpUrl, String jsonData){
		String responseMsg = "";
		
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset("UTF-8");
		PostMethod post = new PostMethod(httpUrl);
		post.getParams().setContentCharset("UTF-8");
		post.setRequestBody( jsonData);
		try {

			httpClient.executeMethod(post);// 200
			responseMsg = post.getResponseBodyAsString().trim();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		

		return responseMsg;
	}
	
	/**
	 * 发送 JSON 完整请求,并等待返回数据
	 * 请求的字符串自动转码 Unicode
	 * 
	 * @param httpUrl
	 * @param jsonData 
	 */
	public static String PostData(String httpUrl, String jsonData) {
		JKRunLog.debugWrite("C==>S: " + jsonData);	
		String responseMsg = Post( httpUrl, jsonData);
		JKRunLog.debugWrite("S==>C: " + responseMsg);
		return responseMsg;
	}

	/**
	 * 发送 JSON 完整请求,并等待返回数据
	 * 
	 * @param httpUrl
	 * @param jsonData
	 */
	public static String GetData(String httpUrl) {
		System.out.println("GET C==>S: " + httpUrl);

		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset("UTF-8");
		GetMethod in = new GetMethod(httpUrl);
		in.getParams().setContentCharset("UTF-8");
		String responseMsg = "";
		try {

			httpClient.executeMethod(in);// 200
			responseMsg = in.getResponseBodyAsString().trim();
			System.out.println("S==>C: " + responseMsg);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			in.releaseConnection();
		}

		return responseMsg;
	}
	
	
	/**
	 * 发送 JSON 完整请求,并等待返回数据
	 * 
	 * @param httpUrl
	 * @param jsonData
	 */
	public static String GetUnicode2String(String httpUrl) {
		String Result = GetData( httpUrl);
		if(JKUnicode.isUnicode(Result))
			Result = JKUnicode.unicode2String(Result);
		
		return Result;
	}
	
}
