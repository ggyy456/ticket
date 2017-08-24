package com.mjx.service.impl;

import com.mjx.entity.User;
import com.mjx.ibatis.IDAO;
import com.mjx.service.UserService;

import java.sql.SQLException;
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

        try {
            list = userDAO.getList(condition);
            //list = userDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveUser() {
        User u = new User();
        u.setUserName("11111");
        for (int i = 0; i < 50; i++) {
            try {
                userDAO.save(u);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
