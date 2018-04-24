package com.mjx.job;

import com.mjx.entity.Train;
import com.mjx.entity.TrainDTO;
import com.mjx.ibatis.BatchInfo;
import com.mjx.ibatis.IDAO;
import com.mjx.redis.RedisUtil;
import com.mjx.util.ContextUtil;
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

        RedisUtil redisUtil = (RedisUtil) ContextUtil.get("redisUtil");
        long second = redisUtil.getExpire("data:userTicket");

        if(second <= 600){
            BatchInfo batchInsert = new BatchInfo(BatchInfo.INSERT);
            BatchInfo batchUpdate = new BatchInfo(BatchInfo.UPDATE);
            TrainDTO dto = null;

            Set<Object> keySet = redisUtil.hkeys("data:userTicket");
            Iterator<Object> keyIt = keySet.iterator();
            String ids = "";
            int num = 0;
            while(keyIt.hasNext()){
                dto = new TrainDTO();
                String ticketId = (String)keyIt.next();
                String userId = (String)redisUtil.hget("data:userTicket",ticketId);
                dto.setTicketId(Integer.valueOf(ticketId));
                dto.setUserId(Integer.valueOf(userId));
                batchInsert.addBatch("Train.saveUserTicket", dto);

                ids += ","+ticketId;
                if(++num % 1000 == 0) {
                    batchUpdate.addBatch("Train.updateTicket", ids.substring(1));
                    ids = "";
                }
            }

            if(!"".equals(ids)){
                batchUpdate.addBatch("Train.updateTicket", ids.substring(1));
            }

            trainDAO.handleBatch(batchInsert);
            trainDAO.handleBatch(batchUpdate);
            redisUtil.del("data:userTicket");
        }

        long endTime=System.currentTimeMillis();
        float excTime=(float)(endTime-startTime)/1000;
        LOGGER.info("执行时间："+excTime+"s");

    }
}
