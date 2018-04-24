package com.mjx.redis;

import com.mjx.entity.TrainDTO;
import com.mjx.ibatis.PagingMapImpl;
import com.mjx.util.ContextUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        long startTime = System.currentTimeMillis();

        RedisUtil redisUtil = (RedisUtil) ContextUtil.get("redisUtil");

        Set<Object> ids = new HashSet<Object>();
        Set<Object> stationIds = redisUtil.sinter(condition);  //得到两个id集合的交集
        ids.addAll(stationIds);
        if(!trainTypes.equals("")){
            String[] trainType = trainTypes.split(",");
            Set<Object> typeIds = redisUtil.union(new ArrayList(Arrays.asList(trainType)));
            ids.retainAll(typeIds);
        }

        List<TrainDTO> trainList = new ArrayList<TrainDTO>();
        if(ids.size() > 0) {
            List<Object> trainJsonList = redisUtil.hmget("data:trainList", ids);

            for(Object js:trainJsonList){
                TrainDTO dto = (TrainDTO)js;
                long firstSeatSet = redisUtil.sGetSetSize("join:"+dto.getTrainId()+"一等座");
                long secondSeatSet = redisUtil.sGetSetSize("join:"+dto.getTrainId()+"二等座");
                long businessSeatSet = redisUtil.sGetSetSize("join:"+dto.getTrainId()+"商务座");
                long hardSeatSet = redisUtil.sGetSetSize("join:"+dto.getTrainId()+"硬座");
                long softSeatSet = redisUtil.sGetSetSize("join:"+dto.getTrainId()+"软座");
                long hardSleepSet = redisUtil.sGetSetSize("join:"+dto.getTrainId()+"硬卧");
                long softSleepSet = redisUtil.sGetSetSize("join:"+dto.getTrainId()+"软卧");
                long noSeatSet = redisUtil.sGetSetSize("join:"+dto.getTrainId()+"无座");

                dto.setFirstSeat(String.valueOf(firstSeatSet));
                dto.setSecondSeat(String.valueOf(secondSeatSet));
                dto.setBusinessSeat(String.valueOf(businessSeatSet));
                dto.setHardSeat(String.valueOf(hardSeatSet));
                dto.setSoftSeat(String.valueOf(softSeatSet));
                dto.setHardSleep(String.valueOf(hardSleepSet));
                dto.setSoftSleep(String.valueOf(softSleepSet));
                dto.setNoSeat(String.valueOf(noSeatSet));

                trainList.add(dto);
            }
        }

        long endTime=System.currentTimeMillis();
        float excTime=(float)(endTime-startTime)/1000;
        System.out.println("执行时间："+excTime+"s");

        paging.setSizePerPage(100);
        paging.setTotalSize(trainList.size());
        paging.setData(trainList);
        paging.setStartIndex(0);
        paging.computePage();

        setModel(paging);
        return SUCCESS;
    }
}
