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

    //批量更新效率太低，有待提高
    protected void run()  {
        long startTime=System.currentTimeMillis();

        Jedis jedis = JedisUtil.getInstance().getJedis();
        long second = jedis.ttl("data:userTicket");

        if(second <= 600){
            BatchInfo batchInsert = new BatchInfo(BatchInfo.INSERT);
            BatchInfo batchUpdate = new BatchInfo(BatchInfo.UPDATE);
            TrainDTO dto = null;

            Set<String> keySet = jedis.hkeys("data:userTicket");
            Iterator<String> keyIt = keySet.iterator();
            while(keyIt.hasNext()){
                dto = new TrainDTO();
                String ticketId = keyIt.next();
                String userId = jedis.hget("data:userTicket",ticketId);
                dto.setTicketId(Integer.valueOf(ticketId));
                dto.setUserId(Integer.valueOf(userId));
                dto.setIsSell("1");
                batchInsert.addBatch("Train.saveUserTicket", dto);
                batchUpdate.addBatch("Train.updateTicket",dto);
            }

            trainDAO.handleBatch(batchInsert);
            trainDAO.handleBatch(batchUpdate);
            jedis.del("data:userTicket");
        }

        jedis.disconnect();

        long endTime=System.currentTimeMillis();
        float excTime=(float)(endTime-startTime)/1000;
        LOGGER.info("执行时间："+excTime+"s");

    }
}
