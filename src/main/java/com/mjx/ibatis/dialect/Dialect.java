package com.mjx.ibatis.dialect;

/**
 * Created by Administrator on 2017-9-8.
 */
public interface Dialect {
    boolean supportsLimit();

    String getLimitString(String var1, boolean var2);

    String getLimitString(String var1, int var2, int var3);
}
