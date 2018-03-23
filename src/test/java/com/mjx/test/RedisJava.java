package com.mjx.test;

import com.alibaba.fastjson.JSON;
import com.mjx.entity.Ticket;
import com.mjx.entity.TrainDTO;
import com.mjx.redis.JedisUtil;
import com.mjx.redis.SerializeUtil;
import com.mjx.util.ConfigHelper;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017-12-5.
 */
public class RedisJava {
    public static void main(String[] args) {
        JedisUtil jedis = JedisUtil.getInstance();
        //System.out.println("清空库中所有数据："+jedis.getJedis().flushDB());

        test4();

        jedis.del("data:userTicket");
//        List<String> list = jedis.lrange("data:userTicket", 0, -1);
//        for(String s:list){
//            System.out.println(s);
//        }
//
//        System.out.println(list.size());



    }

    public static void batchDel(String pre_str){
        JedisUtil jedis = JedisUtil.getInstance();
        Set<String> set = jedis.keys(pre_str +"*");
        Iterator<String> it = set.iterator();
        while(it.hasNext()){
            String keyStr = it.next();
            System.out.println(keyStr);
            jedis.del(keyStr);
        }
    }

    /*
        注：序列化对象时，需要对象也实现Serializable接口
        正常情况下效率也挺高，但是如果再高并发的情况下，序列化和反序列化消耗太多
        redis不支持存储object和泛型，是有理由的
        建议使用json来存储
    */
    public static void test1(){
        JedisUtil jedis= JedisUtil.getInstance();
        System.out.println("清空库中所有数据："+jedis.getJedis().flushDB());

        TrainDTO t1 = new TrainDTO();
        t1.setTrainNo("1111");
        t1.setTrainType("1");

        TrainDTO t2 = new TrainDTO();
        t2.setTrainNo("2222");
        t2.setTrainType("2");

        List<TrainDTO> list1 = new ArrayList<TrainDTO>();
        list1.add(t1);
        list1.add(t2);

        jedis.set("list".getBytes(), SerializeUtil.serialize(list1));

        byte[] bs = jedis.get("list".getBytes());
        List<TrainDTO> list2 = (List<TrainDTO>)SerializeUtil.unserialize(bs);
        for(TrainDTO dto : list2){
            System.out.println("list TrainNo is:" + dto.getTrainNo());
        }

    }

    /*
        JSON方式存储list
     */
    public static void test2(){
        JedisUtil jedis= JedisUtil.getInstance();
        System.out.println("清空库中所有数据："+jedis.getJedis().flushDB());

        TrainDTO t1 = new TrainDTO();
        t1.setTrainNo("1111");
        t1.setTrainType("1");

        TrainDTO t2 = new TrainDTO();
        t2.setTrainNo("2222");
        t2.setTrainType("2");

        List<TrainDTO> list1 = new ArrayList<TrainDTO>();
        list1.add(t1);
        list1.add(t2);

        String jsonList = JSON.toJSONString(list1);
        jedis.set("jsonList",jsonList);

        List<TrainDTO> list2 = JSON.parseArray(jedis.get("jsonList"),TrainDTO.class);
        System.out.println(list2.get(1).getTrainNo());
    }

    public static void test3(){
        Jedis jedis = JedisUtil.getInstance().getJedis();
        //System.out.println("清空库中所有数据："+jedis.flushDB());

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            System.out.println("正在连接数据库..........");
            Class.forName(ConfigHelper.getJdbcDriver());
            String url = ConfigHelper.getJdbcUrl();
            String user = ConfigHelper.getJdbcUsername();
            String pwd = ConfigHelper.getJdbcPassword();
            conn=(Connection) DriverManager.getConnection(url,user,pwd);
            System.out.println("数据库连接成功！！！");

            String sql="select  TRAIN_ID, TRAIN_NO ,TRAIN_TYPE ,BEGIN_STATION ,END_STATION ,BEGIN_TIME ,END_TIME ,TAKE_TIME From T_TRAIN ";

            stmt = (Statement) conn.createStatement();
            stmt.execute(sql);//执行select语句用executeQuery()方法，执行insert、update、delete语句用executeUpdate()方法。
            rs=(ResultSet) stmt.getResultSet();
            List<TrainDTO> list = new ArrayList<TrainDTO>();
            while(rs.next()) { //当前记录指针移动到下一条记录上
                TrainDTO t = new TrainDTO();
                t.setTrainId(rs.getInt("TRAIN_ID"));
                t.setTrainNo(rs.getString("TRAIN_NO"));
                t.setTrainType(rs.getString("TRAIN_TYPE"));
                t.setBeginStation(rs.getString("BEGIN_STATION"));
                t.setEndStation(rs.getString("END_STATION"));
                t.setBeginTime(rs.getString("BEGIN_TIME"));
                t.setEndTime(rs.getString("END_TIME"));
                t.setTakeTime(rs.getString("TAKE_TIME"));
                list.add(t);
            }

            long startTime=System.currentTimeMillis();

            //JedisUtil.setObject("train",list);
            String t1 = "00:00";
            String t2 = "06:00";
            String t3 = "12:00";
            String t4 = "18:00";
            String t5 = "24:00";

            for(TrainDTO tr:list){
                String id = tr.getTrainId().toString();
                String t = tr.getBeginTime();
                jedis.hset("data:trainList", id , JSON.toJSONString(tr));
                jedis.sadd("query:begin:"+tr.getBeginStation() , id);
                jedis.sadd("query:end:"+tr.getEndStation() , id);
                jedis.sadd("query:type:"+tr.getTrainType(), id);

                if(t.compareTo(t1)>=0 && t.compareTo(t2)<=0){
                    jedis.sadd("query:time1", id);
                }
                if(t.compareTo(t2)>=0 && t.compareTo(t3)<=0){
                    jedis.sadd("query:time2", id);
                }
                if(t.compareTo(t3)>=0 && t.compareTo(t4)<=0){
                    jedis.sadd("query:time3", id);
                }
                if(t.compareTo(t4)>=0 && t.compareTo(t5)<=0){
                    jedis.sadd("query:time4", id);
                }
            }

            long endTime=System.currentTimeMillis();
            float excTime=(float)(endTime-startTime)/1000;
            System.out.println("执行时间："+excTime+"s");

            rs.close();
            stmt.close();
            conn.close();

            System.out.println("完成！！！");

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }finally {
            jedis.close();
        }

    }

    public static void test4(){
        Jedis jedis= JedisUtil.getInstance().getJedis();
        //清除当前数据
        jedis.del("data:ticketList");
        batchDel("join:");

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            System.out.println("正在连接数据库..........");
            Class.forName(ConfigHelper.getJdbcDriver());
            String url = ConfigHelper.getJdbcUrl();
            String user = ConfigHelper.getJdbcUsername();
            String pwd = ConfigHelper.getJdbcPassword();
            conn=(Connection) DriverManager.getConnection(url,user,pwd);
            System.out.println("数据库连接成功！！！");

            String sql="select  TICKET_ID , TRAIN_ID  ,TICKET_TYPE ,IS_SELL  From T_TICKET ";

            stmt = (Statement) conn.createStatement();
            stmt.execute(sql);//执行select语句用executeQuery()方法，执行insert、update、delete语句用executeUpdate()方法。
            rs=(ResultSet) stmt.getResultSet();
            List<Ticket> list = new ArrayList<Ticket>();
            while(rs.next()) { //当前记录指针移动到下一条记录上
                Ticket t = new Ticket();
                t.setTicketId(rs.getInt("TICKET_ID"));
                t.setTrainId(rs.getInt("TRAIN_ID"));
                t.setTicketType(rs.getString("TICKET_TYPE"));
                t.setIsSell(rs.getString("IS_SELL"));
                list.add(t);
            }

            long startTime=System.currentTimeMillis();

            for(Ticket t:list){
                String id = t.getTicketId().toString();
                jedis.hset("data:ticketList", id , t.getIsSell());
                jedis.sadd("join:"+t.getTrainId()+t.getTicketType() , id);
            }

            long endTime=System.currentTimeMillis();
            float excTime=(float)(endTime-startTime)/1000;
            System.out.println("执行时间："+excTime+"s");

            rs.close();
            stmt.close();
            conn.close();

            System.out.println("完成！！！");

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }finally {
            jedis.close();
        }

    }

    public static void test5(){
        long startTime = System.currentTimeMillis();

        JedisUtil jedis= JedisUtil.getInstance();
        //完成select * from t_train where train_type='G'
        //Set<String> ids = jedis.smembers("G");

        //完成select * from t_train where begin_station='北京' and end_station='武汉'
        Set<String> ids = jedis.sinter("始北京","终武汉");  //得到两个id集合的交集

        String[] idArr = ids.toArray(new String[]{});
        List<String> trainJsonList = jedis.hmget("trainList", idArr);    //从user的hash类型中获取多个filed的value,传入的field可以为字符串数组
        //jedis.sort()
        System.out.println(ids);
        System.out.println(trainJsonList);   //打印出所有的满足条件的user

        long endTime=System.currentTimeMillis();
        float excTime=(float)(endTime-startTime)/1000;
        System.out.println("执行时间："+excTime+"s");


    }



}
