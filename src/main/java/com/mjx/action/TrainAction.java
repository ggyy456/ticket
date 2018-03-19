package com.mjx.action;

import com.mjx.entity.User;
import com.mjx.service.TrainService;
import com.mjx.service.UserService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 ab -n 5000 -c 500 -k http://localhost:8081/ticket/train/concurrencyTest.do

 */

public class TrainAction extends ActionSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainAction.class);

    private TrainService trainService;

    public String toTrainListPage() {
        LOGGER.info("进入TrainAction的queryTrainList方法");
        return SUCCESS;
    }

    public String toRedisListPage() {
        LOGGER.info("进入TrainAction的queryRedisList方法");
        return SUCCESS;
    }

    public String concurrencyTest(){
        trainService.concurrencyTest();
        return SUCCESS;
    }


    public TrainService getTrainService() {
        return trainService;
    }

    public void setTrainService(TrainService trainService) {
        this.trainService = trainService;
    }
}
