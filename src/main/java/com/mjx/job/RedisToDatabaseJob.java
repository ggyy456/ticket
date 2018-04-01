package com.mjx.job;

import com.mjx.entity.Train;
import com.mjx.entity.TrainDTO;
import com.mjx.ibatis.BatchInfo;
import com.mjx.ibatis.IDAO;
import com.mjx.redis.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2018/3/31.
 */
public class RedisToDatabaseJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisToDatabaseJob.class);
    private IDAO<Train> trainDAO;

    public IDAO<Train> getTrainDAO() {
        return trainDAO;
    }

    public void setTrainDAO(IDAO<Train> trainDAO) {
        this.trainDAO = trainDAO;
    }

    protected void run()  {
        long startTime=System.currentTimeMillis();

        Jedis jedis = JedisUtil.getInstance().getJedis();
        long second = jedis.ttl("data:userTicket");

        if(second <= 600){
            BatchInfo batchInfo = new BatchInfo(BatchInfo.INSERT);
            TrainDTO dto = null;

            Set<String> keySet = jedis.hkeys("data:userTicket");
            Iterator<String> keyIt = keySet.iterator();
            while(keyIt.hasNext()){
                dto = new TrainDTO();
                String ticketId = keyIt.next();
                String userId = jedis.hget("data:userTicket",ticketId);
                dto.setTicketId(Integer.valueOf(ticketId));
                dto.setUserId(Integer.valueOf(userId));
                batchInfo.addBatch("Train.saveUserTicket", dto);
            }

            trainDAO.handleBatch(batchInfo);
            jedis.del("data:userTicket");
        }

        jedis.disconnect();

        long endTime=System.currentTimeMillis();
        float excTime=(float)(endTime-startTime)/1000;
        LOGGER.info("执行时间："+excTime+"s");

    }
}
