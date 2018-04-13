package com.mjx.service.impl;

import com.mjx.entity.Train;
import com.mjx.entity.User;
import com.mjx.ibatis.IDAO;
import com.mjx.service.RedisService;
import com.mjx.service.UserService;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

@SuppressWarnings("unchecked")
public class RedisServiceImpl implements RedisService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceImpl.class);
    private RedisTemplate redisTemplate;
    private IDAO<Train> trainDAO;

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setTrainDAO(IDAO<Train> trainDAO) {
        this.trainDAO = trainDAO;
    }

    @Override
    public void trainToRedis() {
        try {
            LOGGER.info("进入polling");
            HttpServletRequest request = ServletActionContext.getRequest();
            HttpServletResponse response = ServletActionContext.getResponse();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            redisTemplate.opsForValue().set("aaaaaaaa","bbbbbbbbbbbbbb");
            long startTime=System.currentTimeMillis();
            int num=0;
            for(int i=0;i<100000;i++){
                /* 这句话比较重要，我们通过response给页面返回一个js脚本，让js执行父页面的对应的jsFun，参数就是我们的data */
                if(i%100==0) {
                    response.getWriter().write("<script type=\"text/javascript\">parent.jsFun(\"" + "数据" + (num++) + "\")</script>");
                    response.flushBuffer();
                }
            }

            long endTime=System.currentTimeMillis();
            float excTime=(float)(endTime-startTime)/1000;
            response.getWriter().write("<script type=\"text/javascript\">parent.jsFun(\"" + "执行时间："+excTime + "s\")</script>");
            response.flushBuffer();
            LOGGER.info("执行时间："+excTime + "s");

        } catch (Exception e) {
            System.err.println("long connection was broken!");
            e.printStackTrace();
        }
    }

    @Override
    public void ticketToRedis() {

    }
}
