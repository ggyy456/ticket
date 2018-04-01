package com.mjx.service.impl;

import com.mjx.entity.Train;
import com.mjx.entity.TrainDTO;
import com.mjx.entity.User;
import com.mjx.ibatis.IDAO;
import com.mjx.redis.JedisUtil;
import com.mjx.service.TrainService;
import com.mjx.service.UserService;
import com.mjx.util.ConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.swing.text.html.HTMLDocument;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

@SuppressWarnings("unchecked")
public class TrainServiceImpl implements TrainService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainServiceImpl.class);
    private Lock lock = new ReentrantLock();    //注意这个地方

    private IDAO<Train> trainDAO;

    public IDAO<Train> getTrainDAO() {
        return trainDAO;
    }

    public void setTrainDAO(IDAO<Train> trainDAO) {
        this.trainDAO = trainDAO;
    }

    //lock比synchronized 效率稍高，并发访问时
    public void concurrencyTest(){
        Jedis jedis= JedisUtil.getInstance().getJedis();
        lock.lock();
        try {
            Random rd = new Random();
            int userId = rd.nextInt(54753)+1;

            LOGGER.info(userId+"参加抢票");

            String beginStation = "query:begin:北京";
            String endStation = "query:end:上海";
            String trainType = "query:type:G";
            String ticketType = "二等座";

            Set<String> trainIds = jedis.sinter(beginStation,endStation,trainType);  //得到id交集
            Iterator<String> trainIt = trainIds.iterator();

            outer:while(trainIt.hasNext()){
                String trainId = trainIt.next();
                Set<String> ticketIds = jedis.smembers("join:"+trainId+ticketType);
                Iterator<String> ticketIt = ticketIds.iterator();

                while (ticketIt.hasNext()){
                    String ticketId = ticketIt.next();
                    if(!jedis.hexists("data:userTicket",ticketId)){
                        jedis.hset("data:userTicket",ticketId,userId+"");
                        jedis.expire("data:userTicket",650);    //设置过期时间

                        jedis.srem("join:"+trainId+ticketType,ticketId);
                        LOGGER.info("恭喜"+userId+"抢到票"+ticketId);
                        break outer;
                    }
                }
            }
        }finally {
            jedis.close();
            lock.unlock();
        }


    }

}
