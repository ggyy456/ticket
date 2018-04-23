package com.mjx.service;


import com.mjx.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/8/2 0002.
 */
public interface RedisService {

    public void trainToRedis(final HttpServletResponse response);

    public void ticketToRedis(final HttpServletResponse response,String city);
}
