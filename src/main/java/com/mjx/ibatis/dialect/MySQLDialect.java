package com.mjx.ibatis.dialect;

/**
 * Created by Administrator on 2017-9-8.
 */
public class MySQLDialect implements Dialect {
    protected static final String SQL_END_DELIMITER = ";";

    public MySQLDialect() {
    }

    public String getLimitString(String sql, boolean hasOffset) {
        return (new StringBuffer(sql.length() + 20)).append(this.trim(sql)).append(hasOffset?" limit ?,?":" limit ?").append(";").toString();
    }

    public String getLimitString(String sql, int offset, int limit) {
        sql = this.trim(sql);
        StringBuffer sb = new StringBuffer(sql.length() + 20);
        sb.append(sql);
        if(offset > 0) {
            sb.append(" limit ").append(offset).append(',').append(limit).append(";");
        } else {
            sb.append(" limit ").append(limit).append(";");
        }

        return sb.toString();
    }

    public boolean supportsLimit() {
        return true;
    }

    private String trim(String sql) {
        sql = sql.trim();
        if(sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1 - ";".length());
        }

        return sql;
    }
}

