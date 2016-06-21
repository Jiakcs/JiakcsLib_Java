package com.Jiakcs;

import java.util.List;

import com.Jiakcs.API.JKHttp;
import com.Jiakcs.API.JKJsonWriter;
import com.Jiakcs.API.JKRunLog;
import com.Jiakcs.MySQL.DBEngine;
import com.Jiakcs.MySQL.DBInstall;
import com.Jiakcs.MySQL.DBRowStruct;

public class JiakcsTest {
	
	public static void main(String[] args) {
		 for(int i=1;i<=1000000;i++){
			 int iCount = 1;
			 for(int j=2;j<= (i/2);j++){
				 if (i % j ==0 )
					 iCount += j;
			 }
			 if (iCount == i)
				 System.out.println(i);
		 }
		
	}
	
	
	public static String split(String str,int num) {
		byte[] strs = str.getBytes();
		int iNum = 0;
		int iIdx = 0;
		for(int i=0;i<strs.length && iNum<=num;i++ ){
			if (strs[i] > 0) iNum += 1;
			else{iNum += 1;	i=i+2;}
			iIdx = i;
		}
		
		byte[] news = new byte[iIdx];
		System.arraycopy(strs,0,news,0,iIdx);
		
		return new String(news); 
	}

	public static void main_Bak(String[] args) {
		        Integer f1 = 100, f2 = 100, f3 = 150, f4 = 150;
		        System.out.println(f1 == f2);
		        System.out.println(f3 == f4);

		        String str = split("我ABC", 4);

				System.out.println(str);

				 String str2 = split("我ABC走DEF", 5);

				System.out.println(str2); 
				
		      
		
	
		/*JKRunLog.println("中文中文");
		*//** 数据库查询 **//*
		DBEngine dbTable = new DBEngine();  // 创建数据库引擎

		dbTable.openDB("lc_news_page")
				.andQueryFilter("I=#is_Delete", "0");

		
		dbTable.setFields("id,newsClassKey,newsTitle,newsTopLevel,is_HotNews,createTime");
		dbTable.AddConvtDate("createTime");
		List<DBRowStruct> list = dbTable.doSelect();
		
		for(DBRowStruct One :list ){
			JKRunLog.println(One.getString("newsTitle") + " " + One.getString("createTime"));
		}*/
		
		
		
		
		
		//String jsonStr = "{'APITYPE': 'USER_1001','Token': 'LearnWeb','ClientIP': '0:0:0:0:0:0:0:1','APIDATA': {'Contact':'15300241221','Password':'123456'}}";
		//String jsonData = JKHttp.PostUnicode2String("http://auth.learn.it.com:8411/InterFace/GlobalAuth/", jsonStr);		
		//JKRunLog.println(jsonStr);
		//JKRunLog.println(jsonData);
		
		//InstallDB();	 
		 
		//DBController dbTable = new DBController();
		//List<DBRowStruct> tsRows = dbTable.openDB("lc_course_group")
		//	.setFields("id,gradeKey,groupContext,groupLableKey,groupTitle,groupType")
		//	.andQueryFilter("S=#groupStatus","1").doSelect();
				  
		//JKJsonWriter jw = new JKJsonWriter("1", ""); jw.putDb("List",
		//		tsRows); 
		//JKRunLog.println(jw.toJsonString());
		
		 //InstallDB();
		/*
		 * DBController dbTable = new DBController();
		 * 
		 * List<DBRowStruct> tsRows = dbTable.openDB("lc_course_group")
		 * .setFields
		 * ("id,gradeKey,groupContext,groupLableKey,groupTitle,groupType")
		 * .andQueryFilter("S=_groupStatus", "1") .doSelect();
		 * 
		 * JKJsonWriter jw = new JKJsonWriter("1", ""); jw.putDb("List",
		 * tsRows); JKRunLog.println(jw.toJsonString());
		 * 
		 * // ==========================================================
		 * 
		 * DBRowStruct tsRow = dbTable.openDB("lc_course_group")
		 * .setFields("id,gradeKey,groupContext,groupLableKey,groupTitle,groupType"
		 * ) .andQueryFilter("S=_groupStatus", "1") .doSelectFirst();
		 * 
		 * JKJsonWriter jw2 = new JKJsonWriter("1", ""); jw2.putDb(tsRow);
		 * JKRunLog.println(jw2.toJsonString());
		 * 
		 * // ==========================================================
		 * 
		 * 
		 * 
		 * tsRow.changeName("gradeKey", "年级"); tsRow.changeName("groupContext",
		 * "课程简介"); tsRow.changeName("groupLableKey", "标签");
		 * tsRow.changeName("groupTitle", "课程名称"); tsRow.changeName("groupType",
		 * "直播类型");
		 * 
		 * 
		 * JKJsonWriter jw3 = new JKJsonWriter("1", ""); jw3.putDb(tsRow);
		 * JKRunLog.println(jw3.toJsonString());
		 * 
		 * // ==========================================================
		 */

		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * lc_course_group courses = new lc_course_group();
		 * 
		 * List<lc_course_group> Rows = courses.fillRows(
		 * dbTable.openDB("lc_course_group") .andQueryFilter("S=_groupStatus",
		 * "1") .doSelect()); for( lc_course_group One : Rows){
		 * JKRunLog.println("1:" + One.get_groupTitle()); }
		 * 
		 * 
		 * List<DBRowStruct> Rows2 = dbTable.openDB("lc_course_group")
		 * .andQueryFilter("S=_groupStatus", "1") .doSelect();
		 * 
		 * Date groupTitle = Rows2.get(2).getDate("groupTitle");
		 * 
		 * 
		 * 
		 * 
		 * 
		 * for( DBRowStruct One : Rows2){ JKRunLog.println("2:" +
		 * One.getString("groupTitle"));
		 * 
		 * One.getDouble("groupTitle"); }
		 * 
		 * List<lc_course_group> group = courses.fillRows(Rows2); for(
		 * lc_course_group One : group){ One.get_gradeKey(); }
		 * 
		 * 
		 * List<DBRowStruct> Rows3 = dbTable.openDB("lc_course_group")
		 * .andQueryFilter("S=_groupStatus", "1")
		 * .join("lc_global_auth_user","lc_course_group"
		 * ,"db_Global_Auth_User_id" , "db_Global_Auth_User_id") .doSelect();
		 * 
		 * for( DBRowStruct One : Rows2){ JKRunLog.println("3:" +
		 * One.getString("groupTitle") + "-" + One.getString("fullName")); }
		 */
	}

	private static void InstallDB() {
		 DBInstall ins = new DBInstall();
		 ins.CreateDBClass("",
				 "com.jszj.learn.service", "V:\\MyEclipse2015\\LearnAPI\\src\\main\\java\\com\\jszj\\learn\\service\\DBCollection.java");
	}

	private static void runDB() {
		/*
		 * // 定义数据库引擎 DBController dbTable = new DBController(); // 定义一张表对象
		 * dd_book book = new dd_book(); // 根据条件查询一个结果集 List<dd_book> bookRows =
		 * book.fillRows( dbTable.openDB("dd_book") .orQueryFilter("S_=_id",
		 * "1001") .orQueryFilter("S_=_id", "1002") .doSelect());
		 * 
		 * // 通过表对象访问第一条数据 RunLog.println("书名(1):" + book.get_bname()); //
		 * 通过集合表对象访问第 N 条数据 RunLog.println("书名(2):" +
		 * bookRows.get(1).get_bname());
		 * 
		 * 
		 * // 通过表对象修改字段值 book.set_bname(book.get_bname() + "1");
		 * RunLog.println("修改书名(1)为:" + book.get_bname()); // 执行更新表
		 * book.doUpdate();
		 * 
		 * // 再次查询结果, 只需要一条数据返回的情况下,不需要集合表对象接收
		 * book.fillRows(dbTable.openDB("dd_book") .andQueryFilter("S_=_id",
		 * "1001") .doSelect()); RunLog.println("新书名(1):" + book.get_bname());
		 * 
		 * dd_user user = new dd_user(); //user.fillRows(
		 * 
		 * 
		 * // 新建一行数据 dd_book newBook = new dd_book(); newBook.setUUID("id");
		 * newBook.set_bactive(1); newBook.set_bktype("T");
		 * newBook.set_bname("新书"); newBook.set_btime("2016-2-18 16:30:22");
		 * newBook.set_db_user_id("1001"); // 执行插入 newBook.doInsert();
		 * 
		 * 
		 * // 查询一条数据 book.fillRows(dbTable.openDB("dd_book")
		 * .andQueryFilter("S_=_bktype", "T") .doSelect());
		 * RunLog.println("准备删除书名:" + book.get_bname()); // 删除这条数据
		 * book.doDelete();
		 */
	}

}
