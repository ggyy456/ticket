package com.mjx.redis;

import com.alibaba.fastjson.JSON;
import com.mjx.entity.Train;
import com.mjx.entity.TrainDTO;
import com.mjx.ibatis.PagingMapImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        PagingMapImpl paging =  new PagingMapImpl(new Train());
        paging.setSmartClient(true);

        Jedis jedis = JedisUtil.getJedis();
        Set<String> ids = jedis.sinter("始北京","终武汉");  //得到两个id集合的交集
        String[] idArr = ids.toArray(new String[]{});
        List<String> trainJsonList = jedis.hmget("trainList", idArr);
        List<Train> trainList = new ArrayList<Train>();
        for(String js:trainJsonList){
            trainList.add(JSON.parseObject(js, Train.class));
        }

        paging.setSizePerPage(100);
        paging.setTotalSize(idArr.length);
        paging.setData(trainList);
        paging.setStartIndex(0);
        paging.computePage();

        setModel(paging);
        return SUCCESS;
    }
}
