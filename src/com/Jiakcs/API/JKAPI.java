package com.Jiakcs.API;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.Jiakcs.MySQL.PropertiesUtil;

public class JKAPI {
	/**
	 * 日期时间转换字符串格式化
	 * @param _date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String DataTime2String(Date _date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(_date);
	}
	
	/**
	 * 日期转换字符串格式化
	 * @param _date
	 * @return yyyy-MM-dd
	 */
	public static String Data2String(Date _date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(_date);
	}
	
	/**
	 * 时间转换字符串格式化
	 * @param _date
	 * @return H:mm:ss
	 */
	public static String Time2String(Date _date){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(_date);
	}
	
	/**
	 * Long日期时间转换字符串格式化
	 * @param millSec
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String LongToStringDate(Long millSec){
	     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     Date date= new Date(millSec);
	     return sdf.format(date);
	}
	
	/**
	 * Long日期时间转换字符串格式化
	 * @param millSec
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String LongToStringDate(String _millSec){
		if ( _millSec == null) return "";
		if ( _millSec == "") return "";
		Long millSec = Long.parseLong(_millSec);
	     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     Date date= new Date(millSec);
	     return sdf.format(date);
	}
	
	/**
	 * 支付串转整型数据
	 * @param _value
	 * @return
	 */
	public static Integer Str2Int(String _value){
		if ( _value == null) return 0;
		 return Integer.parseInt(_value);
	}
	
	/**
	 * 支付串转整型浮点数
	 * @param _value
	 * @return
	 */
	public static Double Str2Double(String _value){
		if ( _value == null) return 0.0;
		 return Double.parseDouble(_value);
	}
	
	/**
	 * 读取配置文件的值
	 * @param Key
	 * @return
	 */
	public static String getJiakcsProp(String Key){
		String result = "";
        Properties prop = new Properties();
        InputStream in = PropertiesUtil.class.getResourceAsStream("/JiakcsConfig.properties");
        try {
            prop.load(in);
            if(prop.containsKey(Key)){
                result = (prop.get(Key)+"").trim();
            }else{
                result = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return result;
	}
	
}
