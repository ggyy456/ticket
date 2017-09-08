package com.mjx.ibatis.dialect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017-9-8.
 */
public class SQLServerDialect implements Dialect {
    private static Logger log = LoggerFactory.getLogger(SQLServerDialect.class);
    public String ROW_NAME = "rownum";

    public SQLServerDialect() {
    }

    public String getLimitString(String sql, boolean hasOffset) {
        sql = sql.trim();
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        if(hasOffset) {
            pagingSelect.append(" SELECT t0_.* FROM ( ");
        } else {
            pagingSelect.append(" SELECT t0_.* FROM ( ");
        }

        String order = this.parseOrderBy(sql);
        pagingSelect.append("SELECT t1_.*,ROW_NUMBER() over( " + order + ") rownum_ FROM (");
        pagingSelect.append(this.cutSql(sql));
        if(hasOffset) {
            pagingSelect.append(" ) as t1_ )  as t0_ WHERE t0_.rownum_ between ? and ? ");
        } else {
            pagingSelect.append(") as t1_ )  as t0_ WHERE t0_.rownum_ <= ? ");
        }

        return pagingSelect.toString();
    }

    private String parseOrderBy(String sql) {
        String sql1 = sql.toUpperCase();
        int index = sql1.lastIndexOf("ORDER BY");
        if(-1 == index) {
            return "";
        } else {
            sql1 = sql1.substring(index, sql1.length());
            sql1 = this.cut(sql1, new String[]{"HAVING", "(", ")"});
            return sql1;
        }
    }

    private String cut(String src, String[] cutStr) {
        String[] var6 = cutStr;
        int var5 = cutStr.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            String s1 = var6[var4];
            if(src.indexOf(s1) != -1) {
                return src.substring(0, src.indexOf(s1));
            }
        }

        return src;
    }

    private String cutSql(String sql) {
        String sql1 = sql.toUpperCase();
        int index = sql1.lastIndexOf("ORDER BY");
        if(-1 == index) {
            return sql;
        } else {
            sql1 = sql1.substring(0, index);
            return sql1;
        }
    }

    public String getLimitString(String sql, int startIndex, int sizePerPage) {
        sql = sql.trim();
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        if(startIndex > 0) {
            pagingSelect.append(" SELECT t0_.* FROM (");
        } else {
            pagingSelect.append(" SELECT t0_.* FROM (");
        }

        String order = this.parseOrderBy(sql);
        pagingSelect.append("SELECT t1_.*,ROW_NUMBER() over(" + order + ") rownum_ FROM (");
        pagingSelect.append(this.cutSql(sql));
        if(startIndex > 0) {
            pagingSelect.append(") as t1_ ) as t0_ WHERE t0_.rownum_ > " + startIndex + " and t0_.rownum_ <= " + (startIndex + sizePerPage));
        } else if(sizePerPage == 0) {
            pagingSelect.append(") as t1_ ) as t0_ ");
        } else {
            pagingSelect.append(") as t1_ ) as t0_ WHERE t0_.rownum_ <= " + sizePerPage + " ");
        }

        return pagingSelect.toString();
    }

    public String getLimitString2(String sql, int offset, int limit) {
        int startOfSelect = sql.toLowerCase().indexOf("select");
        StringBuffer pagingSelect = (new StringBuffer(sql.length() + 100)).append(sql.substring(0, startOfSelect)).append("select * from ( select ").append(this.getRowNumber(sql));
        if(hasDistinct(sql)) {
            pagingSelect.append(" row_.* from ( ").append(sql.substring(startOfSelect)).append(" ) as row_");
        } else {
            pagingSelect.append(sql.substring(startOfSelect + 6));
        }

        pagingSelect.append(" ) as temp_ where rownumber_ ");
        if(offset > 1) {
            pagingSelect.append("between " + offset + " and " + offset * limit);
        } else {
            pagingSelect.append("<= " + limit);
        }

        return pagingSelect.toString();
    }

    public String getLimitString2(String sql, boolean hasOffset) {
        int startOfSelect = sql.toLowerCase().indexOf("select");
        StringBuffer pagingSelect = (new StringBuffer(sql.length() + 100)).append(sql.substring(0, startOfSelect)).append("select * from ( select ").append(this.getRowNumber(sql));
        if(hasDistinct(sql)) {
            pagingSelect.append(" row_.* from ( ").append(sql.substring(startOfSelect)).append(" ) as row_");
        } else {
            pagingSelect.append(sql.substring(startOfSelect + 6));
        }

        pagingSelect.append(" ) as temp_ where rownumber_ ");
        if(hasOffset) {
            pagingSelect.append("between ?+1 and ?");
        } else {
            pagingSelect.append("<= ?");
        }

        return pagingSelect.toString();
    }

    private String getRowNumber(String sql) {
        StringBuffer rownumber = (new StringBuffer(50)).append("rownumber() over(");
        int orderByIndex = sql.toLowerCase().indexOf("order by");
        if(orderByIndex > 0 && !hasDistinct(sql)) {
            rownumber.append(sql.substring(orderByIndex));
        }

        rownumber.append(") as rownumber_,");
        return rownumber.toString();
    }

    private static boolean hasDistinct(String sql) {
        return sql.toLowerCase().indexOf("select distinct") >= 0;
    }

    public boolean supportsLimit() {
        return true;
    }
}

