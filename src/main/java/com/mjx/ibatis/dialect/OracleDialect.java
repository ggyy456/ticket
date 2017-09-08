package com.mjx.ibatis.dialect;

/**
 * Created by Administrator on 2017-9-8.
 */
public class OracleDialect implements Dialect {
    public OracleDialect() {
    }

    public String getLimitString(String sql, boolean hasOffset) {
        sql = sql.trim();
        boolean isForUpdate = false;
        if(sql.toLowerCase().endsWith(" for update")) {
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }

        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        if(hasOffset) {
            pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        } else {
            pagingSelect.append("select * from ( ");
        }

        pagingSelect.append(sql);
        if(hasOffset) {
            pagingSelect.append(" ) row_ where rownum < ?) where rownum_ > =?");
        } else {
            pagingSelect.append(" ) where rownum < ?");
        }

        if(isForUpdate) {
            pagingSelect.append(" for update");
        }

        return pagingSelect.toString();
    }

    public String getLimitString(String sql, int startIndex, int sizePerPage) {
        sql = sql.trim();
        boolean isForUpdate = false;
        if(sql.toLowerCase().endsWith(" for update")) {
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }

        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        if(startIndex > 0) {
            pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        } else {
            pagingSelect.append("select * from ( ");
        }

        pagingSelect.append(sql);
        if(startIndex > 0) {
            pagingSelect.append(" ) row_ where rownum <= " + (startIndex + sizePerPage) + ") where rownum_ >" + startIndex);
        } else if(sizePerPage == 0) {
            pagingSelect.append(" ) ");
        } else {
            pagingSelect.append(" ) where rownum <= " + sizePerPage);
        }

        if(isForUpdate) {
            pagingSelect.append(" for update");
        }

        return pagingSelect.toString();
    }

    public boolean supportsLimit() {
        return true;
    }
}
