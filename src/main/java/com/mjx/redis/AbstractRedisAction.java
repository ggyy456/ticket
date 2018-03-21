package com.mjx.redis;

import com.alibaba.fastjson.JSON;
import com.mjx.entity.TrainDTO;
import com.mjx.ibatis.PagingMapImpl;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by Administrator on 2018-3-19.
 */
public class AbstractRedisAction extends ActionSupport implements ModelDriven {

    protected static Logger LOG = LoggerFactory.getLogger(AbstractRedisAction.class);
    private Object model;

    /***
     * @param obj
     */
    public void setModel(Object obj) {
        this.model = obj;
    }

    /***
     * @return model;
     */
    public Object getModel() {
        return model;
    }

    @Override
    public String execute() throws Exception {
        Map parameters = ActionContext.getContext().getParameters();
        PagingMapImpl paging =  new PagingMapImpl(new TrainDTO());
        paging.setSmartClient(true);

        String beginStation = parameters.get("beginStation") == null ? "" : ((String[]) parameters.get("beginStation"))[0];
        String endStation = parameters.get("endStation") == null ? "" : ((String[]) parameters.get("endStation"))[0];
        String trainTypes = parameters.get("trainType") == null ? "" : ((String[]) parameters.get("trainType"))[0];
        String beginTime = parameters.get("beginTime") == null ? "" : ((String[]) parameters.get("beginTime"))[0];

        List<String> condition = new ArrayList<String>();
        condition.add("query:begin:"+beginStation);
        condition.add("query:end:"+endStation);
        if(!beginTime.equals("")){
            condition.add(beginTime);
        }

        Jedis jedis = JedisUtil.getJedis();
        Set<String> ids = new HashSet<String>();
        Set<String> stationIds = jedis.sinter(condition.toArray(new String[]{}));  //得到两个id集合的交集
        ids.addAll(stationIds);
        if(!trainTypes.equals("")){
            String[] trainType = trainTypes.split(",");
            Set<String> typeIds = jedis.sunion(trainType);
            ids.retainAll(typeIds);
        }

        List<TrainDTO> trainList = new ArrayList<TrainDTO>();
        if(ids.size() > 0) {
            String[] idArr = ids.toArray(new String[]{});
            List<String> trainJsonList = jedis.hmget("data:trainList", idArr);

            for(String js:trainJsonList){
                TrainDTO dto = JSON.parseObject(js, TrainDTO.class);
                Set<String> firstSeatSet = jedis.smembers("join:"+dto.getTrainId()+"一等座");
                Set<String> secondSeatSet = jedis.smembers("join:"+dto.getTrainId()+"二等座");
                Set<String> businessSeatSet = jedis.smembers("join:"+dto.getTrainId()+"商务座");
                Set<String> hardSeatSet = jedis.smembers("join:"+dto.getTrainId()+"硬座");
                Set<String> softSeatSet = jedis.smembers("join:"+dto.getTrainId()+"软座");
                Set<String> hardSleepSet = jedis.smembers("join:"+dto.getTrainId()+"硬卧");
                Set<String> softSleepSet = jedis.smembers("join:"+dto.getTrainId()+"软卧");
                Set<String> noSeatSet = jedis.smembers("join:"+dto.getTrainId()+"无座");

                dto.setFirstSeat(firstSeatSet.size()+"");
                dto.setSecondSeat(secondSeatSet.size()+"");
                dto.setBusinessSeat(businessSeatSet.size()+"");
                dto.setHardSeat(hardSeatSet.size()+"");
                dto.setSoftSeat(softSeatSet.size()+"");
                dto.setHardSleep(hardSleepSet.size()+"");
                dto.setSoftSleep(softSleepSet.size()+"");
                dto.setNoSeat(noSeatSet.size()+"");

                trainList.add(dto);
            }
        }

        paging.setSizePerPage(100);
        paging.setTotalSize(trainList.size());
        paging.setData(trainList);
        paging.setStartIndex(0);
        paging.computePage();

        setModel(paging);
        return SUCCESS;
    }
}
