package com.mjx.test;

import com.mjx.entity.TrainDTO;
import com.mjx.redis.JedisUtil;
import com.mjx.redis.SerializeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.util.SafeEncoder;

import java.util.List;

/**
 * Created by Administrator on 2017-12-5.
 */
public class RedisJava {
    public static void main(String[] args) {



    }

    //直接使用RedisUtils实例进行五大数据类型的操作：（这样，使用完后会自动归还到池子中）
    public void test1(){
        JedisUtil jedisUtil= JedisUtil.getInstance();
        JedisUtil.Strings strings=jedisUtil.new Strings();
        strings.set("nnn", "nnnn");
        System.out.println("-----"+strings.get("nnn"));
    }

    //通过RedisUtil实例获取Jedis连接对象；这样就可以用原生的方式使用；最后使用完后需要手动将其归还到池子中
    public void test2(){
        Jedis jedis=JedisUtil.getInstance().getJedis();
        for (int i = 0; i < 10; i++) {
            jedis.set("test", "test");
            System.out.println(i+"=="+jedis.get("test"));

        }
        JedisUtil.getInstance().returnJedis(jedis);
    }

    //将java对象存到redis中
    public void test3(){
        TrainDTO t1 = new TrainDTO();
        t1.setTrainNo("1111");
        t1.setTrainType("2");

        JedisUtil jedisUtil= JedisUtil.getInstance();
        Jedis jedis=JedisUtil.getInstance().getJedis();

        JedisUtil.Strings strings=jedisUtil.new Strings();
        strings.set("object3", SerializeUtil.serialize(t1));

        //jedis.set(SafeEncoder.encode("object1"),SerializeUtil.serialize(p));
        byte[] personBy = jedis.get(SafeEncoder.encode("object3"));
        TrainDTO t2 = (TrainDTO) SerializeUtil.unserialize(personBy);
        System.out.println(t2.getTrainNo());
    }
}
