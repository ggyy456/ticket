package com.mjx.service;


import com.mjx.entity.User;
import com.mjx.ibatis.IDAO;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrator on 2017/8/2 0002.
 */
public interface UserService {
    List<User> getUserList(int id);

    void saveUser();
}
