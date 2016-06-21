package com.Jiakcs.API;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;

import com.Jiakcs.MySQL.PropertiesUtil;

/**
 * 日志输出类
 * 
 * @author Jiakcs
 *
 */
public class JKRunLog {
	private static String LvString = "";
	private static Boolean errorMode = true;
	private static Boolean debugMode = true;
	private static Boolean printMode = true;
	private static Boolean printBigMode = false;
	private static Boolean libDebugMode = false;
	private static String outFile = "";

	/**
	 * 加载配置文件
	 */
	static {
		if (PropertiesUtil.getProp("ProjectName").equals("JiakcsLib")) {
			errorMode = PropertiesUtil.getProp("ErrorMode").equals("ON") ? true
					: false;
			debugMode = PropertiesUtil.getProp("DebugMode").equals("ON") ? true
					: false;
			printMode = PropertiesUtil.getProp("PrintMode").equals("ON") ? true
					: false;
			printBigMode = PropertiesUtil.getProp("PrintBigMode").equals("ON") ? true
					: false;
			libDebugMode = PropertiesUtil.getProp("LibDebugMode").equals("ON") ? true
					: false;
			outFile = PropertiesUtil.getProp("OutFile");
		}
	}

	/**
	 * 进入一个方法
	 * 
	 * @param FunName
	 *            方法名
	 */
	public static void libDebugEnterFun(String FunName) {
		if (libDebugMode) {
			LvString += "--";
			libDebugWrite("Enter " + FunName);
		}
	}

	/**
	 * 离开一个方法
	 * 
	 * @param FunName
	 *            方法名
	 */
	public static void libDebugLeaveFun(String FunName) {
		if (libDebugMode) {
			libDebugWrite("Leave: " + FunName);
			if (LvString.length() > 2)
				LvString = LvString.substring(2);
			else
				LvString = "";
		}
	}

	/**
	 * 输出调试信息
	 * 
	 * @param Context
	 *            调试信息
	 */
	public static void libDebugWrite(String Context) {
		if (libDebugMode)
			printLnString("[libDebug]:" + LvString + Context);
	}

	/**
	 * 输出调试信息
	 * 
	 * @param Context
	 *            调试信息
	 */
	public static void debugWrite(String Context) {
		if (debugMode)
			printLnString("[Debug]:" + LvString + Context);
	}

	/**
	 * 输出错误信息
	 * 
	 * @param Context
	 *            调试信息
	 */
	public static void writeErrorln(String Context) {
		if (errorMode)
			printLnString("[Error]:" + LvString + Context);
	}

	/**
	 * 输出测试数据
	 * 
	 * @param Context
	 *            测试数据
	 */
	public static void println(String Context) {
		if (printMode)
			printLnString("[println]:" + Context);
	}

	/**
	 * 输出大测试数据(比较多的数据)
	 * 
	 * @param Context
	 *            测试数据
	 */
	public static void printlnBig(String Context) {
		if (printBigMode)
			printLnString("[println]:" + Context);
	}

	/**
	 * 输出信息
	 * 
	 * @param Context
	 *            输出字符串
	 */
	private static void printLnString(String Context) {
		Context = "[" + JKAPI.Time2String(new Date()) + "]==>" + Context;
		System.out.println(Context);

		if (outFile.length() > 0) {
			WriteToFile(Context);
		} else {
		}
	}

	private static void WriteToFile(String Context) {
		if (outFile.length() > 0) {
			try {
				String FileName = outFile + JKAPI.Data2String(new Date())+ ".jsp";				
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(FileName,true),"UTF-8");  
	            out.write(Context + " <BR/>\r\n");  
	            out.flush();  
	            out.close();  
	                
	                
			} catch (IOException e) {
				//e.printStackTrace();
				outFile = "";
			}
		}
	}
}