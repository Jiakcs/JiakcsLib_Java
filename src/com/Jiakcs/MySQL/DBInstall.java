package com.Jiakcs.MySQL;

import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.Jiakcs.API.JKRunLog;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

/**
 * 从数据库创建对象类
 * @author Jiakcs
 *
 */
public class DBInstall {
	private String DBClassContext = "";
	public void CreateDBClass(String DBName, String PackageName , String DBClassFileName){
		DBCoreJDBC DBTable = new DBCoreJDBC();
		DBTable.Connection(DBName);	
		try {
			DBClassContext = "";
			WriteClassLn("package " + PackageName + ";" , 0);
			WriteClassLn("import java.sql.Blob;" , 0);
			WriteClassLn("import java.util.Date;" , 0);
			WriteClassLn("import java.util.ArrayList;" , 0);
			WriteClassLn("import java.util.List;" , 0);
			WriteClassLn("import com.Jiakcs.API.JKRunLog;" , 0);
			WriteClassLn("import com.Jiakcs.MySQL.DBRowStruct;" , 0);
			WriteClassLn("" , 0);
		
			List<String> LisTable = new ArrayList<String>();
			ResultSet showTables = DBTable.GetResultSet("show tables");
			while (showTables.next()) { LisTable.add(showTables.getString(1));}
			showTables.close();
			
			for(String Item : LisTable){
				
				//List<String> lisVariable = new ArrayList<String>();
				List<String> lisProperty = new ArrayList<String>();
				List<String> lisdoInsert = new ArrayList<String>();
				List<String> lisdoUpdate = new ArrayList<String>();
				
				String PRIName = "";
				String PRIType = "";
				
				ResultSet showColumn = DBTable.GetResultSet("SHOW FULL COLUMNS FROM " + Item);
				while (showColumn.next()) {
					String ColumnName = showColumn.getString(1);
					String columnType = showColumn.getString(2);
					String columnNull = showColumn.getString(4);
					String columnPRI = showColumn.getString(5);
					String columnComment = showColumn.getString(9);
					
					String colType = columnType;
					String colOneType = "S";
					int iPos = columnType.indexOf("(");
					if (iPos >0) columnType = columnType.substring(0, iPos);
					colType = "String";
					switch(columnType){
						case "varchar": colType = "String";colOneType="S"; break;
						case "int": colType = "Integer";colOneType="I";break;
						case "tinyint": colType = "Integer";colOneType="I";break;
						case "smallint": colType = "Integer";colOneType="I";break;
						case "mediumint": colType = "Integer";colOneType="I";break;
						case "bigint": colType = "Integer";colOneType="I";break;
						case "bit": colType = "byte";colOneType="I";break;
						case "double": colType = "Double";colOneType="I";break;
						case "float": colType = "Double";colOneType="I";break;
						case "decimal": colType = "Integer";colOneType="I";break;
						case "char": colType = "char";colOneType="S";break;
						case "date": colType = "String";colOneType="D";break;
						case "datetime": colType = "String"; colOneType="D";break;
						case "time": colType = "String";colOneType="D";break;
						case "year": colType = "String";colOneType="D";break;
						case "timestamp": colType = "Date";colOneType="D";break;
						case "tinyblob": colType = "Blob";colOneType="S";break;
						case "mediumblob": colType = "Blob";colOneType="S";break;
						case "blob": colType = "Blob";colOneType="S";break;
						case "longblob": colType = "Blob";colOneType="S";break;
						case "tinytext": colType = "String";colOneType="S";break;
						case "text": colType = "String";colOneType="S";break;
						case "mediumtext": colType = "String";colOneType="S";break;
						case "longtext": colType = "String";colOneType="S";break;
						case "enum": colType = "Integer";colOneType="S";break;
						case "binary": colType = "byte[]";colOneType="I";break;
						case "varbinary": colType = "byte[]";colOneType="I";break;
						/*
						case "set": colType = "String";break;
						case "point": colType = "String";break;
						case "linestring": colType = "String";break;
						case "polygon": colType = "String";break;
						case "geometry": colType = "String";break;
						case "multipoint": colType = "String";break;
						case "multilinestring": colType = "String";break;
						case "multipolygon": colType = "String";break;
						case "geometrycollection": colType = "String";break;
						 */
					}
					
					if (columnPRI.equals("PRI")){
						PRIName = ColumnName;
						PRIType = "S";
						switch(colType){
							case "String": PRIType = "S"; break;
							case "Integer": PRIType = "I"; break;
						}
					}
					
					lisProperty.add("/**");
					lisProperty.add(String.format(" * 获取字段值:%s (%s)",ColumnName,columnComment));
					lisProperty.add(" */");
					lisProperty.add(String.format("public %s get_%s(){ return get%s(\"%s\"); }",colType,ColumnName,colType.replace("[]", "Array"),ColumnName));
					lisProperty.add("");	
					lisProperty.add("/**");
					lisProperty.add(String.format(" * 设置字段值:%s (%s)",ColumnName,columnComment));
					lisProperty.add(" */");
					lisProperty.add(String.format("public void set_%s(%s value){ setValue(\"%s\", value); }",ColumnName,colType,ColumnName));
					lisProperty.add("");	
					
					//public String get_id(){ return getString("id"); }
					lisdoInsert.add(String.format("if ( !isNull(\"%s\")) putDataList(\"%s\", getString(\"%s\") , \"%s\");",ColumnName,ColumnName,ColumnName,colOneType ));
					
					//if (isUpdate("id")) putDataList("id", get_id() , "S");
					lisdoUpdate.add(String.format("if (isUpdate(\"%s\")) putDataList(\"%s\", getString(\"%s\") , \"%s\");",ColumnName,ColumnName,ColumnName,colOneType ));
				}
				showColumn.close();
				
				// 类头部
				WriteClassLn(String.format("class %s extends DBRowStruct{",Item) , 0);
				WriteClassLn("" , 1);
				// 构造函数
				WriteClassLn(String.format("public %s(){",Item) , 1);
				WriteClassLn(String.format("DBName = \"%s\";",DBName) , 2);
				WriteClassLn(String.format("TableName = \"%s\";",Item) , 2);
				WriteClassLn(String.format("setMainKey(\"%s\",\"%s\");",PRIName, PRIType) , 2);
				
				WriteClassLn("}" , 1);	
				WriteClassLn("" , 1);
				WriteClassLn("" , 0);
				
				// 字段读写属性方法
				for(String One : lisProperty)
					WriteClassLn(One , 1);
				
				WriteClassLn("" , 0);
				WriteClassLn("" , 0);
				
				// 添加数据接口方法
				WriteClassLn("/**" , 1);
				WriteClassLn(" * 添加数据到数据库" , 1);
				WriteClassLn(" */" , 1);
				WriteClassLn("public Boolean doInsert(){" , 1);
				WriteClassLn("clearDataList();" , 2);
				for(String One : lisdoInsert)
					WriteClassLn(One , 2);
				WriteClassLn("return _doInsert();" , 2);
				WriteClassLn("}" , 1);	
				
				// 更新数据接口方法
				WriteClassLn("/**" , 1);
				WriteClassLn(" * 更新数据到数据库" , 1);
				WriteClassLn(" */" , 1);
				WriteClassLn("public Boolean doUpdate(){" , 1);
				WriteClassLn("clearDataList();" , 2);
				for(String One : lisdoUpdate)
					WriteClassLn(One , 2);
				WriteClassLn("return _doUpdate();" , 2);
				WriteClassLn("}" , 1);	
				
				WriteClassLn("/**" , 1);
				WriteClassLn(" * 删除数据从数据库" , 1);
				WriteClassLn(" */" , 1);
				WriteClassLn("public Boolean doDelete(){" , 1);
				WriteClassLn("return _doDelete();" , 2);
				WriteClassLn("}" , 1);	
				
				
				WriteClassLn("/**",1);
				WriteClassLn(" * 第一行数据填充本对象,并返回全部数据集",1);
				WriteClassLn(" * @param Items 填充数据(通过查询串)",1);
				WriteClassLn(" * @return 所有数据集",1);
				WriteClassLn(" */",1);
				
				WriteClassLn("public List<" + Item + "> fillRows(List<DBRowStruct> Items){",1);
				WriteClassLn("Boolean isFirst = true;",2);
				WriteClassLn("List<" + Item + "> Rows = new ArrayList<" + Item + ">();",2);
				WriteClassLn("if ( Items != null){",2);
				WriteClassLn("for(DBRowStruct one : Items ){",3);
				WriteClassLn("if ( isFirst ) fillObject(one);",4);
				WriteClassLn("isFirst = false;",4);
				WriteClassLn("" + Item + " newItem = new " + Item + "();",4);
				WriteClassLn("newItem.fillObject(one);",4);
				WriteClassLn("Rows.add(newItem);",4);
				WriteClassLn("}",3);
				WriteClassLn("}",2);
				WriteClassLn("return Rows;",2);
				WriteClassLn("}",1);
				
				
				// 类结尾
				WriteClassLn("}" , 0);	
				WriteClassLn("" , 0);
				WriteClassLn("" , 0);
				
				JKRunLog.println("生成表结构完成!　[" + Item + "]");
			}			
		} catch (Exception e) {
			JKRunLog.writeErrorln("获取数据库表错误!！" + e.toString());
		}
		WriteClassLn("" , 0);
		WriteClassLn("" , 0);
		
		WriteClass2File(DBClassFileName);
		

		JKRunLog.println("数据库结构类更新成功!　[" + DBClassFileName + "]");
		
	}
	
	private void WriteClassLn(String line , int Level){
		for(int i=0;i<Level;i++){
			DBClassContext += "\t";
		}
		DBClassContext += (line + "\r\n");
	}
	
	private void WriteClass2File(String FileName){
		try{
			FileWriter fw = new FileWriter(FileName, false);
			fw.write(DBClassContext);
			fw.close();
		}catch(Exception e){
			JKRunLog.println("生成数据库表类文件失败!");
		}
	}
	
} // end class
