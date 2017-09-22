package com.mjx.service.impl;

import com.mjx.entity.Train;
import com.mjx.entity.TrainDTO;
import com.mjx.entity.User;
import com.mjx.ibatis.IDAO;
import com.mjx.service.TrainService;
import com.mjx.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

@SuppressWarnings("unchecked")
public class TrainServiceImpl implements TrainService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainServiceImpl.class);

    private IDAO<Train> trainDAO;

    public IDAO<Train> getTrainDAO() {
        return trainDAO;
    }

    public void setTrainDAO(IDAO<Train> trainDAO) {
        this.trainDAO = trainDAO;
    }

    public synchronized void concurrencyTest(){
        Random rd = new Random();
        int userId = rd.nextInt(54753)+1;

        LOGGER.info(userId+"参加抢票");

        TrainDTO cond = new TrainDTO();
        cond.setBeginStation("北京");
        cond.setEndStation("上海");
        cond.setTrainType("'Z'");
        cond.setTicketType("硬卧");
        cond.setIsSell("0");

        TrainDTO ticket = (TrainDTO)trainDAO.execute("getEntityTicket",cond);
        if(ticket!=null){
            cond.setUserId(userId);
            cond.setTrainId(ticket.getTrainId());
            TrainDTO dto = (TrainDTO)trainDAO.execute("getEntityUser",cond);
            if(dto==null){
                ticket.setUserId(userId);
                trainDAO.execute("saveUserTicket",ticket);
                trainDAO.execute("updateTicket",ticket);

                LOGGER.info("恭喜"+userId+"抢到票"+ticket.getTicketId());
            }
        }

    }

}
