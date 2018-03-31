package test;

import com.mjx.entity.ConstantTicket;
import com.mjx.entity.Train;
import com.mjx.util.ConfigHelper;
import com.mjx.util.UUIDGenerator;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 插入近2000w条测试数据
 * mysql批量插入速度非常快，需设置useServerPrepStmts=false&rewriteBatchedStatements=true
 * 多线程插入并不比单线程快，反而线程越多越慢
 */
public class TestTicket implements Runnable{

    public static void main(String[] args) {
//        ExecutorService pool = Executors.newFixedThreadPool(11);
//        for(int i=0;i<ConstantTicket.HOT_CITY.length;i=i+3) {
//            pool.execute(new TestTicket());
//        }
//        //结束线程池
//        pool.shutdown();

        ExecutorService pool = Executors.newFixedThreadPool(1);
        pool.execute(new TestTicket());
        pool.shutdown();
    }

    @Override
    public void run() {
        listToDatabase(databaseToList());
    }

    public List<Train> databaseToList(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            System.out.println("正在连接数据库..........");
            Class.forName(ConfigHelper.getJdbcDriver());
            String url = ConfigHelper.getJdbcUrl();//+"&useUnicode=true&characterEncoding=utf-8";
            String user = ConfigHelper.getJdbcUsername();
            String pwd = ConfigHelper.getJdbcPassword();
            conn=(Connection) DriverManager.getConnection(url,user,pwd);
            System.out.println("数据库连接成功！！！");

            String sql="select TRAIN_ID ,TRAIN_TYPE  From T_TRAIN ";

            stmt = (Statement) conn.createStatement();
            stmt.execute(sql);//执行select语句用executeQuery()方法，执行insert、update、delete语句用executeUpdate()方法。
            rs=(ResultSet) stmt.getResultSet();
            List<Train> list = new ArrayList<Train>();
            while(rs.next()) { //当前记录指针移动到下一条记录上
                Train t = new Train();
                t.setTrainId(rs.getInt("TRAIN_ID"));
                t.setTrainType(rs.getString("TRAIN_TYPE"));
                list.add(t);
            }

            rs.close();
            stmt.close();
            conn.close();

            System.out.println("查询完成！！！");

            return list;

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void listToDatabase(List<Train> list){
        Connection conn = null;
        PreparedStatement pstmt = null;
        long startTime=System.currentTimeMillis();
        try{
            System.out.println("正在连接数据库..........");
            Class.forName(ConfigHelper.getJdbcDriver());
            String url = ConfigHelper.getJdbcUrl();//+"&useUnicode=true&characterEncoding=utf-8";
            String user = ConfigHelper.getJdbcUsername();
            String pwd = ConfigHelper.getJdbcPassword();
            conn=(Connection) DriverManager.getConnection(url,user,pwd);
            conn.setAutoCommit(false); // 设置手动提交
            System.out.println("数据库连接成功！！！");

            String sql="insert into t_ticket(train_id,ticket_no,ticket_time,ticket_type,is_sell) values(?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            Map<String,String[]> map = new HashMap<String,String[]>();
            map.put("G",ConstantTicket.TICKET_TYPE_G);
            map.put("C",ConstantTicket.TICKET_TYPE_C);
            map.put("D",ConstantTicket.TICKET_TYPE_D);
            map.put("Z",ConstantTicket.TICKET_TYPE_Z);
            map.put("T",ConstantTicket.TICKET_TYPE_T);
            map.put("K",ConstantTicket.TICKET_TYPE_K);

            Map<String,int[]> numMap = new HashMap<String,int[]>();
            numMap.put("G",ConstantTicket.TICKET_TYPE_G_NUM);
            numMap.put("C",ConstantTicket.TICKET_TYPE_C_NUM);
            numMap.put("D",ConstantTicket.TICKET_TYPE_D_NUM);
            numMap.put("Z",ConstantTicket.TICKET_TYPE_Z_NUM);
            numMap.put("T",ConstantTicket.TICKET_TYPE_T_NUM);
            numMap.put("K",ConstantTicket.TICKET_TYPE_K_NUM);

            for (int i = 0; i < list.size(); i++) {
                int trainId = list.get(i).getTrainId();
                String trainType = list.get(i).getTrainType();
                String[] ticketTypes = map.get(trainType);
                int[] ticketTypeNums = numMap.get(trainType);

                for (int j = 0; j < ticketTypes.length; j++) {
                    for (int k = 0; k < ticketTypeNums[j]; k++) {
                        pstmt.setInt(1,trainId);
                        pstmt.setString(2,UUIDGenerator.getUUID());
                        pstmt.setString(3,"2017-09-18");
                        pstmt.setString(4,ticketTypes[j]);
                        pstmt.setString(5,"0");
                        pstmt.addBatch();
                    }
                }

                if(i>0 && i%800==0){
                    pstmt.executeBatch();
                    conn.commit();

                    System.out.println("批次"+i+"导入完成！！！");
                }
            }

            pstmt.executeBatch(); // 执行批量处理
            conn.commit();  // 提交

            pstmt.close();
            conn.close();

            long endTime=System.currentTimeMillis();
            float excTime=(float)(endTime-startTime)/1000;
            System.out.println("执行时间："+excTime+"s");

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
