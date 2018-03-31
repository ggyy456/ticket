package test;

import com.mjx.redis.JedisUtil;
import redis.clients.jedis.SortingParams;

import java.util.List;

/**
 * Created by Administrator on 2018/3/18.
 */
public class RedisSort {
    public static void main(String[] args) {
        testSort1();
    }

    public static void testSort1() {
        JedisUtil jedis = JedisUtil.getInstance();
        jedis.sadd("tom:friend:list", "123");  //tom的好友列表
        jedis.sadd("tom:friend:list", "456");
        jedis.sadd("tom:friend:list", "789");
        jedis.sadd("tom:friend:list", "101");

        jedis.set("uid:sort:123", "1000");  //好友对应的成绩
        jedis.set("uid:sort:456", "6000");
        jedis.set("uid:sort:789", "100");
        jedis.set("uid:sort:101", "5999");

        jedis.set("uid:123", "{'uid':123,'name':'lucy'}"); //好友的详细信息
        jedis.set("uid:456", "{'uid':456,'name':'jack'}");
        jedis.set("uid:789", "{'uid':789,'name':'marry'}");
        jedis.set("uid:101", "{'uid':101,'name':'icej'}");

        SortingParams sortingParameters = new SortingParams();
        //sortingParameters.desc();
        //sortingParameters.limit(0, 2);
        sortingParameters.get("uid:*");
        //   sortingParameters.get("uid:sort:*");
        sortingParameters.by("uid:sort:*");
        List<String> result=jedis.sort("tom:friend:list", sortingParameters);
        for(String item:result){
            System.out.println("item..."+item);
        }
    }

    public static void testSort2() {
        JedisUtil jedis = JedisUtil.getInstance();
        jedis.del("user:66", "user:55", "user:33", "user:22", "user:11", "userlist");
        jedis.lpush("userlist", "33");
        jedis.lpush("userlist", "22");
        jedis.lpush("userlist", "55");
        jedis.lpush("userlist", "11");

        jedis.hset("user:66", "name", "66");
        jedis.hset("user:55", "name", "55");
        jedis.hset("user:33", "name", "33");
        jedis.hset("user:22", "name", "79");
        jedis.hset("user:11", "name", "24");
        jedis.hset("user:11", "add", "beijing");
        jedis.hset("user:22", "add", "shanghai");
        jedis.hset("user:33", "add", "guangzhou");
        jedis.hset("user:55", "add", "chongqing");
        jedis.hset("user:66", "add", "xi'an");

        SortingParams sortingParameters = new SortingParams();
        // 符号 "->" 用于分割哈希表的键名(key name)和索引域(hash field)，格式为 "key->field" 。
        sortingParameters.get("user:*->name");
        sortingParameters.get("user:*->add");
//      sortingParameters.by("user:*->name");
//      sortingParameters.get("#");
        List<String> result = jedis.sort("userlist", sortingParameters);

        for (String item : result) {
            System.out.println("item...." + item);
        }

    }
}
