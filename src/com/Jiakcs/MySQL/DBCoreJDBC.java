package com.Jiakcs.MySQL;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.Jiakcs.API.JKRunLog;

/**
 * 数据库JDBC操作基础类
 * @author Jiakcs
 *
 */
public class DBCoreJDBC {
	
	private Connection connection = null;
	private Statement statement = null;

	private String MySQL_Driver = "com.mysql.jdbc.Driver";
	//private String MySQL_Jdbc = "jdbc:mysql://192.168.0.10:3306/vt_vkyun_data?useUnicode=true&characterEncoding=utf-8&autoReconnect=true";
	//private String MySQL_User = "root";
	//private String MySQL_Pwd = "root";

	private String MySQL_Jdbc = "";
	private String MySQL_User = "";
	private String MySQL_Pwd = "";


	/**
	 * 初始化数据库连接
	 */
	public Boolean Connection() {
		return Connection("");
	}
	
	/**
	 * 初始化数据库连接
	 */
	public Boolean Connection(String PropertyType) {

		LoadProperties(PropertyType);	// 加载配置文件

		try {
			Class.forName(MySQL_Driver);
		} catch (ClassNotFoundException e) {
			JKRunLog.writeErrorln("找不到JDBC驱动程序类 ，加载驱动失败！");
			return false;
		}

		try {
			connection = java.sql.DriverManager.getConnection(MySQL_Jdbc, MySQL_User, MySQL_Pwd);
			statement = connection.createStatement();
		} catch (Exception e) {
			JKRunLog.writeErrorln("数据库连接错误: JDBC:" + MySQL_Jdbc);
			JKRunLog.writeErrorln("数据库连接错误: User:" + MySQL_User);
			JKRunLog.writeErrorln("数据库连接错误: Pwd:" + MySQL_Pwd);
			return false;
		}
		return true;
	}

	/**
	 * 关闭连接
	 */
	public void CloseConnection() {
		if (connection != null){
			try {				
				connection.close();
			} catch (SQLException e) {
				JKRunLog.writeErrorln("关闭MySQL连接失败!");
			}
		if (statement != null)
			try {
				statement.close();
			} catch (SQLException e) {
				JKRunLog.writeErrorln("卸载连接库资源失败!");
			}
		}
	}

	private void LoadProperties(){
		LoadProperties("");
	}
	/**
	 * 加载配置文件
	 */
	private void LoadProperties(String PropertyType ){
		if(PropertiesUtil.getProp("ProjectName").equals("JiakcsLib")){
			if ( PropertyType.length() > 0)
				PropertyType = "_" + PropertyType;
			
	        MySQL_Jdbc = PropertiesUtil.getProp("MySQL_jdbc" + PropertyType);
	        MySQL_User = PropertiesUtil.getProp("MySQL_User" + PropertyType);
	        MySQL_Pwd = PropertiesUtil.getProp("MySQL_Pwd" + PropertyType);
			if ( MySQL_Jdbc == ""){
			    JKRunLog.writeErrorln("加载数据库配置文件失败[JiakcsConfig.properties ==> MySQL_jdbc" + PropertyType + "]!");
			}
		}else{
			JKRunLog.writeErrorln("加载配置文件失败![JiakcsConfig.properties]");			
		}
	}

	/**
	 * 执行 SQL 查询语句,返回记录表
	 */
	public ResultSet GetResultSet(String sql) {
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
		} catch (SQLException e) {
			JKRunLog.writeErrorln("Select SQL 命令错误: " + sql);
			// e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 执行SQL语句进行更新\删除等操作
	 */
	public boolean Execute(String hql) {
		try {
			return statement.execute(hql);
		} catch (SQLException e) {
			JKRunLog.writeErrorln("Execute SQL 命令错误: " + hql);
			// e.printStackTrace();
		}

		return false;
	}


}
