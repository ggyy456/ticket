package com.mjx.service.impl;

import com.mjx.entity.User;
import com.mjx.ibatis.IDAO;
import com.mjx.service.UserService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

@SuppressWarnings("unchecked")
public class UserServiceImpl implements UserService {

    private IDAO<User> userDAO;

    public void setUserDAO(IDAO<User> userDAO) {
        this.userDAO = userDAO;
    }
    /*
            * 获取列表
            * */
    public List<User> getUserList(int id){
        List<User> list = null;
        User condition = new User();
        condition.setUserId(id);

        list = userDAO.getList(condition);
        //list = userDAO.getAll();

        return list;
    }

    public void saveUser() {
        User u = new User();
        u.setUserName("11111");
        for (int i = 0; i < 500; i++) {
//            if(i==30){
//                u.setUserName("超过长度超过长度超过长度超过长度超过长度超过长度超过长度超过长度超过长度超过长度超过长度");
//            }
            userDAO.save(u);
        }
    }

    public void updateUser(){
        Integer min = (Integer)userDAO.execute("getEntityMin");
        if(min!=null){
            User u = new User();
            u.setUserId(min);
            u.setCreateTs(getCurrentTimestamp());
            userDAO.update(u);
        }
    }

    protected Timestamp getCurrentTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }
}
