package com.mjx.service.impl;

import com.mjx.entity.ConstantTicket;
import com.mjx.entity.Train;
import com.mjx.ibatis.IDAO;
import com.mjx.redis.RedisUtil;
import com.mjx.service.TrainService;
import com.mjx.util.ContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void concurrencyTest(int n){
        RedisUtil redisUtil = (RedisUtil) ContextUtil.get("redisUtil");

        lock.lock();
        try {
            Random rd = new Random();
            int userId = rd.nextInt(54753)+1;

            LOGGER.info(userId+"参加抢票");

            String trainTypeStr = ConstantTicket.TRAIN_TYPE[rd.nextInt(6)];
            String beginStr = ConstantTicket.HOT_CITY[rd.nextInt(n)];
            String endStr = ConstantTicket.HOT_CITY[rd.nextInt(33)];
            String beginStation = "query:begin:"+beginStr;
            String endStation = "query:end:"+endStr;
            String trainType = "query:type:"+trainTypeStr;
            String ticketType = randomTicketType(trainTypeStr,rd);

            Set<Object> trainIds = redisUtil.sinter(new ArrayList(Arrays.asList(beginStation,endStation,trainType)));  //得到id交集
            Iterator<Object> trainIt = trainIds.iterator();
            int num = 0;

            outer:while(trainIt.hasNext()){
                String trainId = (String)trainIt.next();
                Set<Object> ticketIds = redisUtil.sGet("join:"+trainId+ticketType);
                while(ticketIds.isEmpty() && ++num<4){
                    ticketType = randomTicketType(trainTypeStr,rd);
                    ticketIds = redisUtil.sGet("join:"+trainId+ticketType);
                }
                Iterator<Object> ticketIt = ticketIds.iterator();

                while (ticketIt.hasNext()){
                    String ticketId = (String)ticketIt.next();
                    if(!redisUtil.hHasKey("data:userTicket",ticketId)){
                        redisUtil.hset("data:userTicket",ticketId,userId,650);  //设置过期时间
                        redisUtil.setRemove("join:"+trainId+ticketType,ticketId);
                        LOGGER.info("恭喜"+userId+"抢到票"+ticketId);
                        break outer;
                    }
                }
            }
        }finally {
            lock.unlock();
        }

    }

    private String randomTicketType(String trainTypeStr,Random rd){
        String ticketType = "";

        if(ConstantTicket.TRAIN_TYPE_G.equals(trainTypeStr)){
            ticketType = ConstantTicket.TICKET_TYPE_G[rd.nextInt(3)];
        }
        else if(ConstantTicket.TRAIN_TYPE_C.equals(trainTypeStr)){
            ticketType = ConstantTicket.TICKET_TYPE_C[rd.nextInt(3)];
        }
        else if(ConstantTicket.TRAIN_TYPE_D.equals(trainTypeStr)){
            ticketType = ConstantTicket.TICKET_TYPE_D[rd.nextInt(3)];
        }
        else if(ConstantTicket.TRAIN_TYPE_Z.equals(trainTypeStr)){
            ticketType = ConstantTicket.TICKET_TYPE_Z[rd.nextInt(2)];
        }
        else if(ConstantTicket.TRAIN_TYPE_T.equals(trainTypeStr)){
            ticketType = ConstantTicket.TICKET_TYPE_T[rd.nextInt(5)];
        }
        else if(ConstantTicket.TRAIN_TYPE_K.equals(trainTypeStr)){
            ticketType = ConstantTicket.TICKET_TYPE_K[rd.nextInt(5)];
        }

        return ticketType;
    }

}
