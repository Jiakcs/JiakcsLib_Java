package com.Jiakcs.MySQL;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.Jiakcs.API.JKAPI;
import com.Jiakcs.API.JKRunLog;

/**
 * 数据库操作基础引擎
 * @author Jiakcs
 *
 */
public class DBEngine {
	
	private DBCoreJDBC DBTable = null;
	protected String TableName = "";
	protected String DBName = "";
	
	protected List<DBRowStruct> lisRows = new ArrayList<DBRowStruct>();
	
	protected List<String> SqlTableName = new ArrayList<String>(); 
	protected List<String> SqlFields = new ArrayList<String>(); 
	protected List<String> SqlWhere = new ArrayList<String>(); 
	protected List<String> SqlOrderBy = new ArrayList<String>(); 
	protected List<String> SqlGroupBy = new ArrayList<String>();	
	protected List<String> SqlJoin = new ArrayList<String>();		
	protected List<String> ConvtDate = new ArrayList<String>();			
	protected String SqlPage = "";	
	
	/// 用于更新字段,保存需要更新的字段列表
	protected List<DBColStruct> SqlColNameValue = new ArrayList<DBColStruct>();
	
	/// 最后使用的 UUID 值
	protected String lastUUID = "";
	/**
	 * 返回最后使用的 UUID 值
	 * @return
	 */
	public String getLastUUID(){
		return lastUUID;
	}
	
	/**
	 * 初始化构造函数
	 */
 	public DBEngine(){
	}
 	
 	/**
	 * 初始化构造函数
	 */
 	public DBEngine(String _DBName){
 		DBName = _DBName;
	}
	
	
	/**
	 * 卸载对象资源
	 */
	public void Dispose(){	
		ClearRows();
		
		CloseDBTalbe();
	}
	
	/**
	 * 初始化数据库对象
	 */
	private void OpenDBTalbe() {
		if (DBTable == null){
			DBTable = new DBCoreJDBC();
			DBTable.Connection(DBName);
		}
	}
	
	/**
	 * 初始化数据库对象
	 */
	private void CloseDBTalbe() {
		if ( DBTable != null){
			DBTable.CloseConnection();
			DBTable = null;
		}
	}
	
	
	/**
	 * 开启一个SQL事务
	 */
	public DBEngine openDB(){
		return openDB(TableName);
	}
	
	/**
	 * 开启一个SQL事务
	 */
	public DBEngine openDB(String newTableName){
		clearFilter();
		TableName = newTableName;
		SqlTableName.add(newTableName);
		
		return this;
	}
	
	/**
	 * 清除所有查询器
	 */
	private void clearFilter(){
		ConvtDate.clear();
		SqlTableName.clear();
		SqlFields.clear();
		SqlWhere.clear();
		SqlOrderBy.clear();
		SqlGroupBy.clear();
		SqlJoin.clear();
		SqlPage = "";
		
		for(int i=0;i<SqlColNameValue.size();i++){
			SqlColNameValue.set(i,null);
		}
		SqlColNameValue.clear();
	}
	

	/**
	 * 添加需要转换日期的字段
	 * @param FieldName
	 * @return
	 */	
	public DBEngine AddConvtDate(String FieldName ){
		if(FieldName.length() > 0){
			ConvtDate.add(FieldName);
		}
		return this;
	}
	
	public DBEngine setFields(String Fields ){
		SqlFields.clear();
		return AddFields(Fields);
	}
	
	public DBEngine AddFields(String Fields ){
		String[] Arr = Fields.split(",");
		for(int i=0;i<Arr.length;i++){
			AddField(Arr[i].trim());
		}
		return this;
	}
	
	public DBEngine AddField(String Field ){
		if (Field.length() > 0){
			if (! SqlFields.contains(Field))
				SqlFields.add(Field);
		}		
		return this;
	}
	
	/**
	 * 添加查询条件块 (), 添加 ( 
	 * @param andType AND  OR
	 */
	public DBEngine addQueryAreaBegin(String andType){
		if ( SqlWhere.size() > 0 ) SqlWhere.add(andType);
		SqlWhere.add("(");
		return this;
	}
	/**
	 * 添加查询条件块 (), 添加 )
	 */
	public DBEngine addQueryAreaEnd(){
		SqlWhere.add(")");		
		return this;
	}
	
	/**
	 * 添加 AND 查询条件
	 * @param queryFilter  查询条件  类型#条件#字段名  S_=_id
	 * @param 类型  S 字符串, I 数字, B 布尔, D 日期, A 数组列表
	 * @param 条件  > , < , = , <> 
	 * @param Value  查询值
	 */
	public DBEngine andQueryFilter(String queryFilter, String Value){
		return addQueryFilter("and" ,queryFilter,Value);
	}
	
	/**
	 * 添加 OR 查询条件
	 * @param queryFilter  查询条件  类型#条件#字段名  S_=_id
	 * @param 类型  S 字符串, I 数字, B 布尔, D 日期, A 数组列表
	 * @param 条件  > , < , = , <> 
	 * @param Value  查询值
	 */
	public DBEngine orQueryFilter(String queryFilter, String Value){
		return addQueryFilter("or" ,queryFilter,Value);
	}
	
	/**
	 * 添加查询条件
	 * @param queryType  查询条件  AND OR
	 * @param queryFilter  查询条件  类型#条件#字段名  S_=_id
	 * @param 类型  S 字符串, I 数字, B 布尔, D 日期, A 数组列表
	 * @param 条件  > , < , = , <> 
	 * @param Value  查询值
	 */
	private DBEngine addQueryFilter(String queryType, String queryFilter, String Value){
		String[] ArrQuery = queryFilter.split("#");		
		if ( ArrQuery.length < 2) return this;
		
		if ((queryType.length() >0) && ( SqlWhere.size() > 0 )){
			if ( ! SqlWhere.get(SqlWhere.size()-1).equals("("))			
				SqlWhere.add(queryType);
		}
		String colName = queryFilter.substring(ArrQuery[0].length() + 1 );
		switch(ArrQuery[0].substring(0,1).toUpperCase()){
			case "S": SqlWhere.add(String.format("%s%s'%s'",colName,ArrQuery[0].substring(1),Value));break;
			case "I": SqlWhere.add(String.format("%s%s%s",colName,ArrQuery[0].substring(1),Value));break;
			case "B": SqlWhere.add(String.format("%s%s%s",colName,ArrQuery[0].substring(1),Value));break;
			case "D": SqlWhere.add(String.format("%s%s'%s'",colName,ArrQuery[0].substring(1),Value));break;
			case "A": SqlWhere.add(String.format("%s%s(%s)",colName,ArrQuery[0].substring(1),Value));break;
		}
		return this;
	}
	
	/**
	 * 添加 OR Like 查询条件
	 * @param queryFilter  查询字段
	 * @param 查询值  %Value
	 */
	public DBEngine orLikeLeftFilter(String FieldName, String Value){
		if ( SqlWhere.size() > 0 ){
			if ( ! SqlWhere.get(SqlWhere.size()-1).equals("("))			
				SqlWhere.add("or");
		}
		SqlWhere.add(String.format("%s like '%%s'",FieldName,Value));
		return this;
	}
	/**
	 * 添加 And Like 查询条件
	 * @param queryFilter  查询字段
	 * @param 查询值  %Value
	 */
	public DBEngine andLikeLeftFilter(String FieldName, String Value){
		if ( SqlWhere.size() > 0 ){
			if ( ! SqlWhere.get(SqlWhere.size()-1).equals("("))			
				SqlWhere.add("and");
		}
		SqlWhere.add(String.format("%s like '%%s'",FieldName,Value));
		return this;
	}
	
	/**
	 * 添加 OR Like 查询条件
	 * @param queryFilter  查询字段
	 * @param 查询值  Value%
	 */
	public DBEngine orLikeRightFilter(String FieldName, String Value){
		if ( SqlWhere.size() > 0 ){
			if ( ! SqlWhere.get(SqlWhere.size()-1).equals("("))			
				SqlWhere.add("or");
		}
		SqlWhere.add(String.format("%s like '%s%'",FieldName,Value));
		return this;
	}
	/**
	 * 添加 And Like 查询条件
	 * @param queryFilter  查询字段
	 * @param 查询值  Value%
	 */
	public DBEngine andLikeRightFilter(String FieldName, String Value){
		if ( SqlWhere.size() > 0 ){
			if ( ! SqlWhere.get(SqlWhere.size()-1).equals("("))			
				SqlWhere.add("and");
		}
		SqlWhere.add(String.format("%s like '%s%'",FieldName,Value));
		return this;
	}
	
	/**
	 * 添加 OR Like 查询条件
	 * @param queryFilter  查询字段
	 * @param 查询值  %Value%
	 */
	public DBEngine orLikeFilter(String FieldName, String Value){
		if ( SqlWhere.size() > 0 ){
			if ( ! SqlWhere.get(SqlWhere.size()-1).equals("("))			
				SqlWhere.add("or");
		}
		SqlWhere.add(String.format("%s like '%%s%'",FieldName,Value));
		return this;
	}
	/**
	 * 添加 And Like 查询条件
	 * @param queryFilter  查询字段
	 * @param 查询值  %Value%
	 */
	public DBEngine andLikeFilter(String FieldName, String Value){
		if ( SqlWhere.size() > 0 ){
			if ( ! SqlWhere.get(SqlWhere.size()-1).equals("("))			
				SqlWhere.add("and");
		}
		SqlWhere.add(String.format("%s like '%%s%'",FieldName,Value));
		return this;
	}
	
	/**
	 * 添加 OR In 查询条件
	 * @param queryFilter  查询字段
	 * @param 查询值  in (Value)
	 */
	public DBEngine orInFilter(String FieldName, String Value){
		if ( SqlWhere.size() > 0 ){
			if ( ! SqlWhere.get(SqlWhere.size()-1).equals("("))			
				SqlWhere.add("or");
		}
		SqlWhere.add(String.format("%s in (%s)",FieldName,Value));
		return this;
	}
	/**
	 * 添加 And Like 查询条件
	 * @param queryFilter  查询字段
	 * @param 查询值  in (Value)
	 */
	public DBEngine andInFilter(String FieldName, String Value){
		if ( SqlWhere.size() > 0 ){
			if ( ! SqlWhere.get(SqlWhere.size()-1).equals("("))			
				SqlWhere.add("and");
		}
		SqlWhere.add(String.format("%s in (%s)",FieldName,Value));
		return this;
	}
	
	/**
	 * 添加 OR In 查询条件,自动在查询值上加 单引号
	 * @param queryFilter  查询字段
	 * @param 查询值  in ('Value1','Value2')
	 */
	public DBEngine orInExFilter(String FieldName, String Value){
		if ( SqlWhere.size() > 0 ){
			if ( ! SqlWhere.get(SqlWhere.size()-1).equals("("))			
				SqlWhere.add("or");
		}
		String[] arr = Value.split(",");
		String ValueEx = "";
		for(String One : arr){
			if ( One.trim().length() > 0){
				if ( ValueEx.length() >0)
					ValueEx += ("," + "'" +  One + "'"); 
				else
					ValueEx += ("'" +  One + "'"); 
			}
		}
		SqlWhere.add(String.format("%s in (%s)",FieldName,ValueEx));
		return this;
	}
	/**
	 * 添加 And Like 查询条件,自动在查询值上加 单引号
	 * @param queryFilter  查询字段
	 * @param 查询值  in ('Value1','Value2')
	 */
	public DBEngine andInExFilter(String FieldName, String Value){
		if ( SqlWhere.size() > 0 ){
			if ( ! SqlWhere.get(SqlWhere.size()-1).equals("("))			
				SqlWhere.add("and");
		}
		String[] arr = Value.split(",");
		String ValueEx = "";
		for(String One : arr){
			if ( One.trim().length() > 0){
				if ( ValueEx.length() >0)
					ValueEx += ("," + "'" +  One + "'"); 
				else
					ValueEx += ("'" +  One + "'"); 
			}
		}
		SqlWhere.add(String.format("%s in (%s)",FieldName,ValueEx));
		return this;
	}
	
	
	/**
	 * 添加随机排序-进行随机查询
	 */
	public DBEngine addOrderByRand(){
		if (SqlOrderBy.size() > 0) 
			SqlOrderBy.add(String.format(",%s ", "RAND()"));
		else
			SqlOrderBy.add(String.format("%s ", "RAND()"));
		
		return this;
	}
	
	/**
	 * 添加正向排序条件
	 * @param FieldName 字段名
	 */
	public DBEngine addOrderByAscFilter(String FieldName){
		if (SqlOrderBy.size() > 0) 
			SqlOrderBy.add(String.format(",%s asc", FieldName));
		else
			SqlOrderBy.add(String.format("%s asc", FieldName));
		
		return this;
	}
	
	/**
	 * 添加反向排序条件
	 * @param FieldName 字段名
	 */
	public DBEngine addOrderByDescFilter(String FieldName){
		if (SqlOrderBy.size() > 0) 
			SqlOrderBy.add(String.format(",%s desc", FieldName));
		else
			SqlOrderBy.add(String.format("%s desc", FieldName));
		
		return this;
	}
	
	/**
	 * 添加分组 Group by 
	 * @param FieldName 字段名
	 */
	public DBEngine addGroupByFilter(String FieldName){
		if (SqlGroupBy.size() > 1) 
			SqlGroupBy.add(String.format(",%s", FieldName));
		else
			SqlGroupBy.add(String.format("%s", FieldName));
		return this;
	}
	

	/**
	 * 添加联表关系
	 * @param joinType 连接类型 INNER OUTER LEFT RIGHT
	 * @param joinTableName 表B名称
	 * @param orcField	表A.id
	 * @param toField 表B.id
	 * @return
	 */
	private DBEngine addJoin(String joinType, String leftTableName,String rightTableName, String orcField, String toField){
		
		String TName = rightTableName;
		if ( ! SqlTableName.contains(leftTableName) ){
			TName = rightTableName;
			SqlTableName.add(leftTableName);
		}
		if ( ! SqlTableName.contains(rightTableName) ){
			TName = rightTableName;
			SqlTableName.add(rightTableName);
		}
		
		if( orcField.contains(".")) leftTableName = ""; else leftTableName += ".";		
		if( toField.contains(".")) rightTableName = ""; else rightTableName += ".";
		
		SqlJoin.add(
				String.format(" %s JOIN %s ON %s%s = %s%s ", joinType, TName,leftTableName, orcField,rightTableName,toField)
				);
		return this;
		//INNER JOIN dd_user ON dd_user.id = dd_book.db_user_id
	}
	
	/**
	 * 添加Inner联表关系
	 * @param joinTableName 表B名称
	 * @param orcField	表A.id
	 * @param toField 表B.id
	 * @return
	 */
	public DBEngine join(String leftTableName,String rightTableName, String orcField, String toField){
		return addJoin("", leftTableName,rightTableName ,orcField,toField);
	}
	
	/**
	 * 添加Left联表关系
	 * @param joinTableName 表B名称
	 * @param orcField	表A.id
	 * @param toField 表B.id
	 * @return
	 */
	public DBEngine joinLeftJoin(String leftTableName,String rightTableName, String orcField, String toField){
		return addJoin("LEFT", leftTableName,rightTableName,orcField,toField);
	}
	/**
	 * 添加Right联表关系
	 * @param joinTableName 表B名称
	 * @param orcField	表A.id
	 * @param toField 表B.id
	 * @return
	 */
	public DBEngine joinRight(String leftTableName,String rightTableName, String orcField, String toField){
		return addJoin("RIGHT", leftTableName,rightTableName,orcField,toField);
	}
	
	
	/**
	 * 添加Inner联表关系
	 * @param joinTableName 表B名称
	 * @param orcField	表A.id
	 * @param toField 表B.id
	 * @return
	 */
	public DBEngine joinInner(String leftTableName,String rightTableName, String orcField, String toField){
		return addJoin("INNER", leftTableName,rightTableName ,orcField,toField);
	}
	
	/**
	 * 添加Outer联表关系
	 * @param joinTableName 表B名称
	 * @param orcField	表A.id
	 * @param toField 表B.id
	 * @return
	 */
	public DBEngine joinLeftOuter(String leftTableName,String rightTableName, String orcField, String toField){
		return addJoin("LEFT OUTER", leftTableName,rightTableName,orcField,toField);
	}
	
	/**
	 * 添加Outer联表关系
	 * @param joinTableName 表B名称
	 * @param orcField	表A.id
	 * @param toField 表B.id
	 * @return
	 */
	public DBEngine joinRightOuter(String leftTableName,String rightTableName, String orcField, String toField){
		return addJoin("RIGHT OUTER", leftTableName,rightTableName,orcField,toField);
	}
	
	/**
	 * 添加Outer联表关系
	 * @param joinTableName 表B名称
	 * @param orcField	表A.id
	 * @param toField 表B.id
	 * @return
	 */
	private DBEngine splitPage( Integer skip, Integer take ){
		
		if (take > 0){
			if ( skip >0 )
				SqlPage = String.format(" LIMIT %s OFFSET %s ", take,skip);
			else
				SqlPage = String.format(" LIMIT %s ", take,skip);
		}
		return this;
	}
	
	
	
	/*
	 * 组建SQL语句
	 */
	private String createSqlCommand(){
		String SqlCommand = "";
		SqlCommand = "select ";
		if(SqlFields.size() < 1) SqlFields.add("*");
		
		String strFields = "";
		for(String one : SqlFields){
			if (strFields.length() > 0)
				strFields += ("," + one);
			else
				strFields += one;
		}
		
		SqlCommand += strFields + " from ";
		for(String one : SqlTableName) SqlCommand += one;
		// 添加联表关系
		for(String one : SqlJoin) SqlCommand += one;
		
		if ( SqlWhere.size() > 0 ){
			SqlCommand += " where ";
			for(String one : SqlWhere) SqlCommand += (one + " ");
		}
		if ( SqlOrderBy.size() > 0 ){
			SqlCommand += " order by ";
			for(String one : SqlOrderBy) SqlCommand += one;
		}
		if ( SqlGroupBy.size() > 0 ){
			SqlCommand += " group by ";
			for(String one : SqlGroupBy) SqlCommand += one;
		}
		
		if ( SqlPage.length() > 0 ){
			SqlCommand += SqlPage;
		}
		
		return SqlCommand;
	}
	
	/*
	 * 组建SQL语句
	 */
	private String createSqlCommandBase(String Fields){
		String SqlCommand = "";
		SqlCommand = "select ";
		SqlCommand += Fields + " from ";
		for(String one : SqlTableName) SqlCommand += one;
		// 添加联表关系
		for(String one : SqlJoin) SqlCommand += one;
		
		if ( SqlWhere.size() > 0 ){
			SqlCommand += " where ";
			for(String one : SqlWhere) SqlCommand += (one + " ");
		}
		if ( SqlOrderBy.size() > 0 ){
			SqlCommand += " order by ";
			for(String one : SqlOrderBy) SqlCommand += one;
		}
		if ( SqlGroupBy.size() > 0 ){
			SqlCommand += " group by ";
			for(String one : SqlGroupBy) SqlCommand += one;
		}
		return SqlCommand;
	}
	
	/**
	 * 直接执行SQL语句
	 */
	public List<DBRowStruct> doSelectSql(String SQLCommand){
		FillResult(SQLCommand,0);
		return lisRows == null? null: lisRows;
	}
	
	/**
	 * 通过添加的查询条件,获取结果集合
	 */
	public List<DBRowStruct> doSelect(){
		FillResult(createSqlCommand(),0);
		return lisRows == null? null: lisRows;
	}
	
	
	/**
	 * 通过添加的查询条件,获取结果集合
	 * skip 跳过的记录数 （分页设置）
	 * take 返回的记录数 （分页设置）
	 */
	public List<DBRowStruct> doSelect(Integer skip, Integer take){
		splitPage(skip,take);
		return doSelect();
	}
	
	/**
	 * 通过添加的查询条件,获取结果集合
	 */
	public DBRowStruct doSelectFirst(){
		return doSelectFirst(0);
	}
	
	/**
	 * 通过添加的查询条件,获取第一条记录
	 * skip 跳过的记录数
	 */
	public DBRowStruct doSelectFirst(Integer skip){
		splitPage(skip,1);
		List<DBRowStruct> rows = doSelect();
		if ( rows != null)
			if (rows.size() > 0)
				return rows.get(0);
		return null;
	}
	
	
	
	
	
	
	
	/**
	 * 通过添加的查询条件,获取结果集合Max最大值
	 */
	public Integer getMax(String colName){
		Integer iMax = 0;
		FillResult(createSqlCommandBase(String.format(" max(%s) as __max", colName)),0);
		if (lisRows != null){
			if (lisRows.size() > 0)
				return lisRows.get(0).getInteger("__max");
		}
		return iMax;
	}
	
	/**
	 * 通过添加的查询条件,获取结果集合count数
	 */
	public Integer getCount(){
		Integer iMax = 0;
		SqlFields.clear();
		SqlFields.add( String.format(" count(*) as __count"));
		FillResult(createSqlCommand(),0);
		if (lisRows != null){
			if (lisRows.size() > 0)
				return lisRows.get(0).getInteger("__count");
		}
		return iMax;
	}
	
	
	
	/**
	 * 添加要更新的字段值
	 * @param colType  类型  S 字符串, I 数字, B 布尔, D 日期, A 数组列表
	 * @param colName  字段名
	 * @param colValue  值
	 */
	public DBEngine setColValue( String colType, String colName,String colValue){
		switch(colType.toUpperCase()){
			case "S": SqlColNameValue.add(new DBColStruct(colName, String.format("'%s'",colValue)));break;
			case "I": SqlColNameValue.add(new DBColStruct(colName, String.format("%s",colValue)));break;
			case "B": SqlColNameValue.add(new DBColStruct(colName, String.format("%s",colValue)));break;
			case "D": SqlColNameValue.add(new DBColStruct(colName, String.format("'%s'",colValue)));break;
			case "O": SqlColNameValue.add(new DBColStruct(colName, String.format("%s",colValue)));break;
		}
		return this;
	}
	
	/**
	 * 添加要更新的字段值(String)
	 * @param colName  字段名
	 * @param colValue  值
	 */
	public DBEngine setColValue( String colName,String colValue){
		return setColValue("S", colName,colValue);
	}
	/**
	 * 添加要更新的字段值(Integer)
	 * @param colName  字段名
	 * @param colValue  值
	 */
	public DBEngine setColValue( String colName,Integer colValue){
		return setColValue("I", colName,colValue.toString());
	}
	/**
	 * 添加要更新的字段值(Date)
	 * @param colName  字段名
	 * @param colValue  值
	 */
	public DBEngine setColValue( String colName,Date colValue){
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return setColValue("D", colName, timeFormat.format(colValue));
	}
	/**
	 * 添加要更新的字段值(Boolean)
	 * @param colName  字段名
	 * @param colValue  值
	 */
	public DBEngine setColValue( String colName,Boolean colValue){
		return setColValue("S", colName, colValue.toString());
	}
	
	/**
	 * 添加要更新的字段值(Null)
	 * @param colName  字段名
	 * @param colValue  值
	 */
	public DBEngine setColNull( String colName){
		return setColValue("O", colName, null);
	}
	
	/**
	 * 添加要更新的字段值(UUID)
	 * @param colName  字段名
	 * @param colValue  值
	 */
	public DBEngine setColUUID( String colName){
		lastUUID = UUID.randomUUID().toString().replace("-","");
		return setColValue("S", colName, lastUUID);
	}
	
	/**
	 * 更新数据到数据库
	 */
	public Boolean doUpdate(){
		String SqlCommand = "";
		SqlCommand = "update " + TableName + " set ";
		
		String SqlCols = "";

		for(DBColStruct one : SqlColNameValue){
			if ( SqlCols.length() > 0)
				SqlCols += String.format(",%s=%s",one.getColName() , one.getColValueString());
			else
				SqlCols += String.format("%s=%s",one.getColName() , one.getColValueString());
		}
		SqlCommand += SqlCols;
		
		if ( SqlWhere.size() > 0 ){
			SqlCommand += " where ";
			for(String one : SqlWhere) SqlCommand += (one + " ");
		}

		JKRunLog.debugWrite("更新 SQL( " + SqlCommand + ")");

		OpenDBTalbe();	// 连接数据库
		Boolean Result = DBTable.Execute(SqlCommand);
		CloseDBTalbe();	// 关闭数据库
		return Result;
	}
	
	/**
	 * 添加数据到数据库
	 */
	public Boolean doInsert(){
		String SqlCommand = "";
		SqlCommand = "insert into  ";
		for(String one : SqlTableName) {SqlCommand += one; break;};
		SqlCommand += " ";
		
		String SqlCols = "";
		String SqlValues = "";
		for(DBColStruct Item : SqlColNameValue){
			if ( SqlCols.length() > 0){
				SqlCols += String.format(",%s",Item.getColName());
				SqlValues += String.format(",%s",Item.getColValueString());
			}else{
				SqlCols += String.format("%s",Item.getColName());
				SqlValues += String.format("%s",Item.getColValueString());
			}
		}
		SqlCommand += String.format("(%s) values (%s)",SqlCols,SqlValues);

		JKRunLog.debugWrite("新增 SQL( " + SqlCommand + ")");

		OpenDBTalbe();	// 连接数据库
		Boolean Result = DBTable.Execute(SqlCommand);
		CloseDBTalbe();	// 关闭数据库
		return Result;
		
	}
	
	/**
	 * 删除数据从数据库
	 */
	public Boolean doDelete(){
		String SqlCommand = "";
		SqlCommand = "delete from ";
		for(String one : SqlTableName) {SqlCommand += one; break;};
		
		if ( SqlWhere.size() > 0 ){
			SqlCommand += " where ";
			for(String one : SqlWhere) SqlCommand += (one + " ");
		}
		
		JKRunLog.debugWrite("删除 SQL( " + SqlCommand + ")");
		

		OpenDBTalbe();	// 连接数据库
		Boolean Result = DBTable.Execute(SqlCommand);
		CloseDBTalbe();	// 关闭数据库
		return Result;
		
	}
	
	/**
	 * 通过一个SQL语句查询来获取表的所有数据
	 * @param SQLCommand SQL完整语句
	 * @param maxCount 0 返回所有数据 , >0 最大返回行数
	 */
	private void FillResult(String SqlCommand, Integer maxCount) {
		JKRunLog.debugWrite("查询 SQL( " + SqlCommand + ")");
		ClearRows();	// 清空现有数据
		
		try {	
			OpenDBTalbe();	// 连接数据库
			ResultSet resultLis = DBTable.GetResultSet(SqlCommand);
			ResultSetMetaData metaData = resultLis.getMetaData();
			Integer curIdx = -1;
			while (resultLis.next()) {
				DBRowStruct row = new DBRowStruct();				
				for (int i = 0; i < metaData.getColumnCount(); i++) {
					String ColName = metaData.getColumnName(i + 1);
					Object ColValue = resultLis.getObject(i + 1);
					if ( ConvtDate.contains(ColName)){
						try{
							row.addNewCol(ColName, JKAPI.DataTime2String((Date)ColValue));
						}catch(Exception e){
							row.addNewCol(ColName, ColValue);
						}
					}else{					
						row.addNewCol(ColName, ColValue);
					}
				}
				lisRows.add(row);
				
				curIdx += 1;
				if (( maxCount > 0 ) && ( curIdx > maxCount)) break;
			}			
			metaData = null;
			resultLis.close();
			resultLis = null;
		} catch (Exception e) {
			JKRunLog.writeErrorln("获取数据库表错误!！" + e.toString());
			//e.printStackTrace();
		}
		CloseDBTalbe();	// 关闭数据库
		
	} // end FillResult(String SQLCommand)
	
	/**
	 * 通过一个SQL语句查询来获取表的所有数据
	 * @param SQLCommand SQL完整语句
	 * @param maxCount 0 返回所有数据 , >0 最大返回行数
	 */
	private Object getResultOneFields(String SqlCommand, Integer maxCount) {
		JKRunLog.debugWrite("查询 SQL( " + SqlCommand + ")");
		Object result = null;
		try {	
			OpenDBTalbe();	// 连接数据库
			ResultSet resultLis = DBTable.GetResultSet(SqlCommand);
			if ( resultLis!= null){
				result = resultLis.getObject(0);
			}
			resultLis.close();
			resultLis = null;
		} catch (Exception e) {
			JKRunLog.writeErrorln("获取数据库表错误!！" + e.toString());
		}
		
		CloseDBTalbe();	// 关闭数据库
		
		return result;
		
	} // end FillResult(String SQLCommand)
	
	/**
	 * 清空数据缓存表
	 */
	private void ClearRows(){
		for(int i=0;i<lisRows.size();i++){
			lisRows.get(i).dispose();
			lisRows.set(i, null);
		}
		lisRows.clear();
			
	}
	
}



















