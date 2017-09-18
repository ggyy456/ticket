package com.mjx.test;

import com.mjx.entity.ConstantTicket;
import com.mjx.entity.Train;
import com.mjx.util.UUIDGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2017-9-18.
 */
public class TestTicket {
    public static void main(String[] args) {




        System.out.println(UUIDGenerator.getUUID());
    }

    public static List<Train> databaseToList(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            System.out.println("正在连接数据库..........");
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/ticket";
            String user = "root";//数据库用户名
            String pwd = "root";//数据库密码
            conn=(Connection) DriverManager.getConnection(url,user,pwd);
            System.out.println("数据库连接成功！！！");

            String sql="select TRAIN_ID,TRAIN_TYPE From T_TRAIN "
                    +"where BEGIN_STATION='北京' and END_STATION in('上海','天津')";

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

            System.out.println("导入完成！！！");

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void listToDatabase(List<Train> list){
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            System.out.println("正在连接数据库..........");
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/ticket";
            String user = "root";//数据库用户名
            String pwd = "root";//数据库密码
            conn=(Connection) DriverManager.getConnection(url,user,pwd);
            System.out.println("数据库连接成功！！！");

            String sql="insert into t_ticket(train_id,ticket_no,ticket_time,ticket_type,is_sell) values(?,?,?,?,?)";

            pstmt = conn.prepareStatement(sql);

            String[] hotCity = ConstantTicket.HOT_CITY;
            Random rd = new Random();

            for (int i = 0; i < list.size(); i++) {

            }


            pstmt.close();
            conn.close();

            System.out.println("导入完成！！！");

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
