package com.mjx.util;

/**
 * Created by jack on 2017/5/22.
 * 提供相关常量配置项
 */
public interface ConfigConstant {
    String CONFIG_FILE= "config/dialect.properties";

    String JDBC_DRIVER="test.driverName";
    String JDBC_URL="test.url";
    String JDBC_USERNAME="test.username";
    String JDBC_PASSWORD="test.password";

    String APP_BASE_PACKAGE="ticket.app.base_package";
    String APP_JSP_PATH="ticket.app.jsp_path";
    String APP_ASSET_PATH="ticket.app.asset_path";
}
