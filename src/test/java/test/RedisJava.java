package test;

import com.alibaba.fastjson.JSON;
import com.mjx.entity.Ticket;
import com.mjx.entity.TrainDTO;
import com.mjx.redis.JedisUtil;
import com.mjx.util.ConfigHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/*
    注：序列化对象时，需要对象也实现Serializable接口
    正常情况下效率也挺高，但是如果再高并发的情况下，序列化和反序列化消耗太多
    redis不支持存储object和泛型，是有理由的
    建议使用json来存储

    Pipeline 对于大批量数据的存储和读取速度极快
*/
public class RedisJava {
    public static void main(String[] args) {
        JedisUtil jedis = JedisUtil.getInstance();
//        System.out.println("清空库中所有数据："+jedis.getJedis().flushDB());

        test2();

//        Set<String> set = jedis.keys( "join:*");
//        Iterator<String> it = set.iterator();
//        long num = 0;
//        while(it.hasNext()){
//            String keyStr = it.next();
//            long n = jedis.scard(keyStr);
//            num += n;
//        }
//
//        System.out.println(num);

        //jedis.set("data:aaa","adfasdfasdlfjasdlfjasldfasdf");
        //jedis.expire("data:aaa",100);
        //System.out.println(jedis.ttl("data:aaa"));
        //jedis.expire("data:userTicket",10);

//        jedis.del("data:userTicket");
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

    public static void test1(){
        Jedis jedis = JedisUtil.getInstance().getJedis();
        Pipeline pipeline = jedis.pipelined();

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

            String t1 = "00:00";
            String t2 = "06:00";
            String t3 = "12:00";
            String t4 = "18:00";
            String t5 = "24:00";

            for(TrainDTO tr:list){
                String id = tr.getTrainId().toString();
                String t = tr.getBeginTime();
                pipeline.hset("data:trainList", id , JSON.toJSONString(tr));
                pipeline.sadd("query:begin:"+tr.getBeginStation() , id);
                pipeline.sadd("query:end:"+tr.getEndStation() , id);
                pipeline.sadd("query:type:"+tr.getTrainType(), id);

                if(t.compareTo(t1)>=0 && t.compareTo(t2)<=0){
                    pipeline.sadd("query:time1", id);
                }
                if(t.compareTo(t2)>=0 && t.compareTo(t3)<=0){
                    pipeline.sadd("query:time2", id);
                }
                if(t.compareTo(t3)>=0 && t.compareTo(t4)<=0){
                    pipeline.sadd("query:time3", id);
                }
                if(t.compareTo(t4)>=0 && t.compareTo(t5)<=0){
                    pipeline.sadd("query:time4", id);
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
            jedis.disconnect();
        }

    }

    public static void test2(){
        Jedis jedis= JedisUtil.getInstance().getJedis();
        Pipeline pipeline = jedis.pipelined();

        //清除当前数据
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

            String sql = "SELECT t1.TICKET_ID , t1.TRAIN_ID  ,t1.TICKET_TYPE ,t1.IS_SELL  from t_ticket t1 "
                      + "join t_train t2 on t1.train_id=t2.train_id "
                      + "where t2.begin_station='北京'  ";

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

            int num=0;
            for(Ticket t:list){
                String id = t.getTicketId().toString();
                //jedis.sadd("join:"+t.getTrainId()+t.getTicketType() , id);
                pipeline.sadd("join:"+t.getTrainId()+t.getTicketType() , id);
            }
            pipeline.sync();        //必须使用该方法，不然会丢失数据
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
            jedis.disconnect();
        }

    }


}
