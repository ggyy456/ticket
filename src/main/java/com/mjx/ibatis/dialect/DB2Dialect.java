package com.mjx.ibatis.dialect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DB2Dialect implements Dialect{
	public String ROW_NAME ="rownum";
	private static final String TAG_EXTPART = "_EXT_PART_";
	private static final String TAG_BREAK = "_break_";
	private static final String TAG_SOURCETABLE = "_t_";
	private static final String TAG_SOURCEROW = "_r_";
	
	public String getLimitString(String sql, boolean hasOffset) {
		sql = sql.trim();
		String sqlTemp =  sql.toUpperCase();
		int i = sqlTemp.lastIndexOf("WITH UR");
		String with="";
		if (i>1){
			sql=new String(sql.substring(0,i));
			with=" WITH UR ";
		}
		
		StringBuilder pagingSelect = new StringBuilder( sql.length()+100 );
		
		String[] returnSql = parseOrderBy(sql);
		
		String[] selectCustomSql = parseSelectCustomSql(returnSql[1]);
		
//		System.out.println("--------"+selectCustomSql[0]);
//		System.out.println("--------"+selectCustomSql[1]);
//		System.out.println("--------"+returnSql[0]);
//		System.out.println("--------"+returnSql[1]);
		
		
		if (hasOffset) {
 
			pagingSelect.append(" SELECT "+selectCustomSql[0]+" FROM ( ");
		}else{
			pagingSelect.append(" SELECT "+selectCustomSql[0]+" FROM ( ");
		}

		
		pagingSelect.append("SELECT t1_.*,rownumber() over( "+returnSql[0]+") rownum_ FROM (");
		
		
		pagingSelect.append(selectCustomSql[1]);
		
		if (hasOffset) {
			pagingSelect.append(" ) as t1_ )  as t0_ WHERE t0_.rownum_ between ? and ? ");
		}
		else {
			//pagingSelect.append(") as t1_ )  as t0_ WHERE t0_.rownum_ <= ? ");
			pagingSelect.append(" ) as t1_ )  as t0_ WHERE t0_.rownum_ between 1 and ? ");
		}
		pagingSelect.append( with );
	//	System.out.println("--------"+pagingSelect.toString());
		return pagingSelect.toString();
		

	}

	private String[] parseSelectCustomSql(String sql){
	
		String sqlTemp =  sql.toUpperCase();
		String[] returnSql= {" t0_.*",sql};
		int index = sqlTemp.indexOf("CUSTOM_SELECT");
		int lastIndex = sqlTemp.lastIndexOf("CUSTOM_SELECT");
		if (-1==index&&-1==lastIndex){
			return returnSql;
		
		}else{
 
 
			returnSql[0]=new String(sql.substring(index+13,lastIndex));
 
			
			returnSql[1]=new String(sql.substring(0,index)) +  new String(sql.substring(lastIndex+13,sqlTemp.length()));
		}
		return returnSql;
		
	}
	
	
	private String[] parseOrderBy(String sql){
		String returnSql[] = {"",""};
		String sqlTemp =  sql.toUpperCase();
		
		int index = sqlTemp.lastIndexOf("ORDER BY");
		if (-1==index){
			returnSql[1]=sql;
		
		}else{
			
			
			
			if (sqlTemp.indexOf("ROW_NUMBER_OVER")!=-1){
				returnSql[0]=new String(sql.substring(sqlTemp.indexOf("ROW_NUMBER_OVER")+15,sqlTemp.length()));
		//	returnSql[1]=new String(sql.substring(0,sqlTemp.indexOf("ROW_NUMBER_OVER")));
			returnSql[1]=new String(sql.substring(0,index));
			
			}else{
				
				returnSql[0] = new String(sql.substring( index, sqlTemp.length()));
				returnSql[0] = cut( returnSql[0], new String []{ "HAVING", "(", ")" });
			//	returnSql[1]= sql;
				returnSql[1]=new String(sql.substring(0,index));
		
			}
			
		}
		return returnSql;
		
	}
	private String cut(String src,String[] cutStr){
		for (String s1 : cutStr){
			if (src.indexOf(s1) != -1){
				return new String(src.substring( 0, src.indexOf(s1)));
			}
		}
		return src;
	}
	/**
	 * 查找配对()
	 * @param str
	 * @param begin
	 * @return
	 */
	private static int indexOfBracket(String str, int begin){
		int end = -1;
		Matcher matcher = Pattern.compile("[\\(\\)]").matcher(str.substring(begin));
		int lvl = 0;
		while(matcher.find()){
			if("(".equals(matcher.group())){//新的一层(
				lvl++;
			}else{
				if(lvl==1){
					end = matcher.end();
					break;
				}
				lvl--;
			}
		}
		if(end!=-1){//找到了
			end+=begin;
		}
		return end;
	}
	/**
	 * CUSTOM_SELECT 后面跟限制数量后的查询处理
	 * ROW_NUMBER_OVER 后面跟order by部分
	 */
	public String getLimitString(String sql, int startIndex, int sizePerPage) {
		sql = sql.trim();
	
		StringBuilder pagingSelect = new StringBuilder( sql.length()+100 );
		String sqlt = sql.toUpperCase();
		
		if (sqlt.startsWith("WITH")){
			int withEnd = indexOfBracket(sql, sql.indexOf(")")+1);
			String with =new String( sql.substring(0,withEnd));//从 with xx(...) as (...)的第一个)之后开始计算找对应的)
			pagingSelect.append(with);
			sql = new String(sql.substring(withEnd));//将with部分剔除
		}
		
 
		int i = sql.lastIndexOf("WITH UR");
		String with="";
		if (i>1){
			sql=new String(sql.substring(0,i));
			with=" WITH UR ";
		}
		String tmp0 = "t0_";
		String rownum = "rownum_";
		//除了第一页的情况
		String[] extPart = parseExtPart(sql,tmp0,rownum);//扩展查询结果数据
		String[] returnSql = parseOrderBy(extPart[0]);
		
		String[] selectCustomSql = parseSelectCustomSql(returnSql[1]);
		pagingSelect.append(" SELECT").append(selectCustomSql[0]).append(extPart[1]).append("FROM (");
 
		pagingSelect.append("SELECT t1_.*,rownumber() over(").append(returnSql[0]).append(") ").append(rownum).append(" FROM (");
		pagingSelect.append(selectCustomSql[1]);
		pagingSelect.append(") as t1_ ) as ").append(tmp0);
		pagingSelect.append(extPart[2]);//扩展表数据
		//其他页的情况 
		if (startIndex>0) {
			//pagingSelect.append(" ) as table WHERE rownum between "+offset+" and "+(offset*limit));
			
			pagingSelect.append(" WHERE ").append(tmp0).append(".").append(rownum).append("  between ").append((startIndex+1)+" and   ").append((startIndex+sizePerPage));
		}
		else {//第一页的情况
			if (sizePerPage!=0){
				pagingSelect.append(" WHERE ").append(tmp0).append(".").append(rownum).append(" between 1 and ").append((sizePerPage));
			}
		}
		pagingSelect.append(extPart[3]);
		pagingSelect.append(with);
		return pagingSelect.toString();//扩展表数据
	}
	public static void main(String[] args) {
		String sql = "sdfa$sds1";
		System.out.println(sql.indexOf("\\$"));
	}
	/**
	 * 解析出扩展部分数据
	 * @param sql
	 * @param alias
	 * @return
	 */
	private static String[] parseExtPart(String sql, String talias, String ralias) {
		String[] extPart = new String[]{sql,"","",""};
		if(sql.contains(TAG_EXTPART)){
			String extPartStr = sql.substring(sql.indexOf(TAG_EXTPART)+TAG_EXTPART.length(), sql.lastIndexOf(TAG_EXTPART));
			extPartStr = extPartStr.replaceAll(TAG_SOURCETABLE, talias).replaceAll(TAG_SOURCEROW, ralias);
			String[] arr = extPartStr.split(TAG_BREAK);
			if(arr.length>=2){
				extPart[0] = new StringBuilder(sql.substring(0, sql.indexOf(TAG_EXTPART))).append(sql.substring(sql.lastIndexOf(TAG_EXTPART)+TAG_EXTPART.length(), sql.length())).toString();
				extPart[1] = new StringBuilder(" ").append(arr[0]).toString();
				extPart[2] = new StringBuilder(" ").append(arr[1]).toString();
				if(arr.length>2){
					extPart[3] = new StringBuilder(" ").append(arr[2]).toString();
				}
			}
		}
		return extPart;
	}

	public String getLimitString2(String sql,int offset, int limit) {

		int startOfSelect = sql.toLowerCase().indexOf("select");

		StringBuilder pagingSelect = new StringBuilder( sql.length()+100 )
					.append(  new String(sql.substring(0, startOfSelect) )) //add the comment
					.append("select * from ( select ") //nest the main query in an outer select
					.append( getRowNumber(sql) ); //add the rownnumber bit into the outer query select list

		if ( hasDistinct(sql) ) {
			pagingSelect.append(" row_.* from ( ") //add another (inner) nested select
				.append( new String(sql.substring(startOfSelect)) ) //add the main query
				.append(" ) as row_"); //close off the inner nested select
		}
		else {
			pagingSelect.append( new String( sql.substring( startOfSelect + 6 )) ); //add the main query
		}

		pagingSelect.append(" ) as temp_ where rownumber_ ");

		//add the restriction to the outer select
		if (offset>1) {
			pagingSelect.append("between "+offset+" and "+(offset*limit));
		}
		else {
			
			pagingSelect.append("between 1 and "+(limit));
			//pagingSelect.append("<= "+limit);
		}
		
		//System.out.println("_________________________________________"+pagingSelect.toString());
		return pagingSelect.toString();
	}
	
	
	
	public String getLimitString2(String sql, boolean hasOffset) {

		int startOfSelect = sql.toLowerCase().indexOf("select");

		StringBuilder pagingSelect = new StringBuilder( sql.length()+100 )
					.append( new String( sql.substring(0, startOfSelect)) ) //add the comment
					.append("select * from ( select ") //nest the main query in an outer select
					.append( getRowNumber(sql) ); //add the rownnumber bit into the outer query select list

		if ( hasDistinct(sql) ) {
			pagingSelect.append(" row_.* from ( ") //add another (inner) nested select
				.append(  new String(sql.substring(startOfSelect)) ) //add the main query
				.append(" ) as row_"); //close off the inner nested select
		}
		else {
			pagingSelect.append( new String( sql.substring( startOfSelect + 6 )) ); //add the main query
		}

		pagingSelect.append(" ) as temp_ where rownumber_ ");

		//add the restriction to the outer select
		if (hasOffset) {
			pagingSelect.append("between ?+1 and ?");
		}
		else {
			pagingSelect.append("between 1 and ?");
			//pagingSelect.append("<= ?");
		}

		return pagingSelect.toString();
	}

	private String getRowNumber(String sql) {
		StringBuilder rownumber = new StringBuilder(50)
			.append("rownumber() over(");

		int orderByIndex = sql.toLowerCase().indexOf("order by");

		if ( orderByIndex>0 && !hasDistinct(sql) ) {
			rownumber.append(  new String(sql.substring(orderByIndex)) );
		}

		rownumber.append(") as rownumber_,");

		return rownumber.toString();
	}
	
	private static boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct")>=0;
	}

	
	

	public boolean supportsLimit() {
		// TODO Auto-generated method stub
		return true;
	}

}
