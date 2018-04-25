package com.mjx.action;

import com.mjx.service.TrainService;
import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 ab -n 10000 -c 5000 -k http://localhost:8081/ticket/train/concurrencyTest.do?n=4

 */

public class TrainAction extends ActionSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainAction.class);

    private TrainService trainService;
    private int n;

    public String toTrainListPage() {
        LOGGER.info("进入TrainAction的queryTrainList方法");
        return SUCCESS;
    }

    public String toRedisListPage() {
        LOGGER.info("进入TrainAction的queryRedisList方法");
        return SUCCESS;
    }

    public String concurrencyTest(){
        trainService.concurrencyTest(n);
        return SUCCESS;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public TrainService getTrainService() {
        return trainService;
    }

    public void setTrainService(TrainService trainService) {
        this.trainService = trainService;
    }
}
