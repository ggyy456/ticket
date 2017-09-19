package com.mjx.action;

import com.mjx.entity.User;
import com.mjx.service.UserService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;


public class TrainAction extends ActionSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainAction.class);

    public String toTrainListPage() {
        LOGGER.info("进入TrainAction的queryTrainList方法");
        return SUCCESS;
    }

}
