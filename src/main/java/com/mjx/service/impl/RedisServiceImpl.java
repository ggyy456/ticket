package com.mjx.service.impl;

import com.mjx.entity.Train;
import com.mjx.entity.TrainDTO;
import com.mjx.entity.User;
import com.mjx.ibatis.IDAO;
import com.mjx.redis.RedisUtil;
import com.mjx.service.RedisService;
import com.mjx.service.UserService;
import com.mjx.util.ContextUtil;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

@SuppressWarnings("unchecked")
public class RedisServiceImpl implements RedisService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceImpl.class);
    private IDAO<Train> trainDAO;

    public void setTrainDAO(IDAO<Train> trainDAO) {
        this.trainDAO = trainDAO;
    }

    @Override
    public void trainToRedis() {
        try {
            LOGGER.info("进入polling");
            RedisUtil redisUtil = (RedisUtil)ContextUtil.get("redisUtil");
            HttpServletRequest request = ServletActionContext.getRequest();
            final HttpServletResponse response = ServletActionContext.getResponse();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");

            final String t1 = "00:00";
            final String t2 = "06:00";
            final String t3 = "12:00";
            final String t4 = "18:00";
            final String t5 = "24:00";
            final List<TrainDTO> trainList = (List<TrainDTO>)trainDAO.execute("getListTrain");
            final RedisSerializer<String> stringSerializer = redisUtil.getStringSerializer();
            final RedisSerializer<Object> jsonSerializer = redisUtil.getJsonSerializer();

            long startTime=System.currentTimeMillis();
            redisUtil.executePipelined(new RedisCallback<List<Object>>() {
                @Override
                public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                    int num=0;
                    for (TrainDTO dto:trainList) {
                        String id = dto.getTrainId().toString();
                        byte[] byteId = stringSerializer.serialize(id);
                        String t = dto.getBeginTime();
                        connection.hSet(stringSerializer.serialize("data:trainList"), byteId, jsonSerializer.serialize(dto));
                        connection.sAdd(stringSerializer.serialize("query:begin:"+dto.getBeginStation()) , byteId);
                        connection.sAdd(stringSerializer.serialize("query:end:"+dto.getEndStation()) , byteId);
                        connection.sAdd(stringSerializer.serialize("query:type:"+dto.getTrainType()), byteId);

                        if(t.compareTo(t1)>=0 && t.compareTo(t2)<=0){
                            connection.sAdd(stringSerializer.serialize("query:time1"), byteId);
                        }
                        if(t.compareTo(t2)>=0 && t.compareTo(t3)<=0){
                            connection.sAdd(stringSerializer.serialize("query:time2"), byteId);
                        }
                        if(t.compareTo(t3)>=0 && t.compareTo(t4)<=0){
                            connection.sAdd(stringSerializer.serialize("query:time3"), byteId);
                        }
                        if(t.compareTo(t4)>=0 && t.compareTo(t5)<=0){
                            connection.sAdd(stringSerializer.serialize("query:time4"), byteId);
                        }

                        if((num++)%100==0) {
                            try {
                                response.getWriter().write("<script type=\"text/javascript\">parent.jsFun(\"" + dto.getBeginStation()+"—" + dto.getEndStation() + "\")</script>");
                                response.flushBuffer();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return null;
                }
            });

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
