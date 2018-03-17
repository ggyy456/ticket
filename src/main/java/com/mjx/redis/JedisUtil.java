package com.mjx.redis;

/**
 * Created by Administrator on 2017-12-7.
 */
import java.util.List;
import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Mr.hu
 * @version crateTime：2013-10-30 下午5:41:30
 * Class Explain:JedisUtil
 */
public class JedisUtil {

    //private Logger log = Logger.getLogger(this.getClass());

    private static JedisPool jedisPool = null;

    private JedisUtil() {

    }
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(300);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(200);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(1000 * 100);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);

        //redis如果设置了密码：
        /*jedisPool = new JedisPool(config, JRedisPoolConfig.REDIS_IP,
                JRedisPoolConfig.REDIS_PORT,
                10000,JRedisPoolConfig.REDIS_PASSWORD);    */

        //redis未设置了密码：
        jedisPool = new JedisPool(config, "127.0.0.1",6379);
    }

    /**
       * 获取Jedis实例
         单例
       */
     public synchronized static Jedis getJedis() {
         try {
             if (jedisPool != null) {
                 Jedis resource = jedisPool.getResource();
                 return resource;
             } else {
                 return null;
             }
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
     }

    /**
     * 销毁连接(放到catch中)
     * @param jedis
     */
    public static void returnResource(Jedis jedis) {
        if (null != jedis && null != jedisPool) {
            //jedisPool.returnResource(jedis);
            jedisPool.close();
        }
    }


    /**
     * 设置过期时间
     * @author ruan 2013-4-11
     * @param key
     * @param seconds
     */
    public static void expire(String key, int seconds) {
        if (seconds <= 0) {
            return;
        }
        Jedis jedis = getJedis();
        jedis.expire(key, seconds);
        returnResource(jedis);
    }


    /**
     * 判断key是否存在
     */
    public static boolean existsKey(String key){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                return false;
            } else {
                return jedis.exists(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 删除缓存中的对象，根据key
     * @param key
     * @return
     */
    public static boolean delKey(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.del(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 向缓存中设置对象
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setObject(String key, Object value) {
        Jedis jedis = null;
        try {
            String objectJson = JSON.toJSONString(value);
            jedis = getJedis();
            if (jedis != null) {
                jedis.set(key, objectJson);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 根据key 获取对象
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObject(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            return JSON.parseObject(value, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            returnResource(jedis);
        }
    }

    public static <T> List<T> getList(String key,Class<T> entityClass){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null) {
                return JSON.parseArray(jedis.get(key),entityClass);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            returnResource(jedis);
        }
    }



    public static void main(String[] args) {
        Jedis jedis=JedisUtil.getJedis();
    }
}
