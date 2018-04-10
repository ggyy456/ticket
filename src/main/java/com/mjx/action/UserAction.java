package com.mjx.action;

import com.mjx.entity.User;
import com.mjx.service.UserService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 ab -n 5000 -c 500 -k http://localhost:8081/ticket/login.do
 ab常用参数的介绍：
 -n ：总共的请求执行数，缺省是1；
 -c： 并发数，缺省是1；
 -t：测试所进行的总时间，秒为单位，缺省50000s
 -p：POST时的数据文件
 -w: 以HTML表的格式输出结果
 -k: 发送keep-alive指令到服务器端

 ###################################### JDBC并发测试结果 ###########################################
 以5000连接，200并发为例：
 数据库mysql和dbcp连接池的最大连接数扩容，最好保持一致
 POOL.setMaxTotal(1000);     dbcp连接池中的最大连接数
 max_connections=1000        mysql的my.ini中的最大连接数
 设置完这两个参数以后，并发测试成功！！
 ###################################### JDBC并发测试结果 ###########################################

 使用ibatis后则不用设置线程池最大连接数，应该默认已经设置过了，mysql数据库设置即可

 */

public class UserAction extends ActionSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAction.class);

    private UserService userService;

    private List<User> list;

    private static final long serialVersionUID = 1L;

    private String city;

    public String execute(){
        return SUCCESS;
    }

    public String polling() {
        try {
            LOGGER.info("进入polling");
            HttpServletRequest request = ServletActionContext.getRequest();
            HttpServletResponse response = ServletActionContext.getResponse();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");

            int num=0;
            boolean flag = true;// 用来表示长连接是否已经被断开（如果数据发送失败了就说明是断开了）
            for(int i=0;i<100000;i++){
                flag = this.sendData("jsFun", (num++)+"", response);
                if (!flag) {// 如果数据发送失败，那么就退出了，说明页面长连接已经断开了
                    break;
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean sendData(String jsFun, String data, HttpServletResponse response) {
        try {
            response.setContentType("text/html;charset=utf-8");
            /* 这句话比较重要，我们通过response给页面返回一个js脚本，让js执行父页面的对应的jsFun，参数就是我们的data */
            response.getWriter().write(
                    "<script type=\"text/javascript\">parent." + jsFun + "(\""
                            + data + "\")</script>");
            response.flushBuffer();
            return true;
        } catch (Exception e) {
            System.err.println("long connection was broken!");
            return false;
        }

    }

    public String hello() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setHeader("Access-Control-Allow-Origin", "*");

        LOGGER.info("进入hello");

        list = userService.getUserList(city);

        return SUCCESS;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
