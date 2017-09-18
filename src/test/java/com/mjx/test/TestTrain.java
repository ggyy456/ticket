package com.mjx.test;

import com.mjx.entity.ConstantTicket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;

/**
 * Created by Administrator on 2017-9-18.
 */
public class TestTrain {

    public static void main(String[] args) {
        //sqlToDatabase();
    }

    public static void sqlToDatabase(){
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

            String sql="insert into t_train(train_no,train_type,begin_station,end_station,begin_time,end_time,take_time) values(?,?,?,?,?,?,?)";

            pstmt = conn.prepareStatement(sql);

            String[] hotCity = ConstantTicket.HOT_CITY;
            Random rd = new Random();
            for(int i=0;i<hotCity.length;i++){
                for(int j=0;j<hotCity.length;j++){
                    if(!hotCity[i].equals(hotCity[j])){
                        createTrainData(rd,hotCity[i],hotCity[j],pstmt);
                    }
                }
            }
            pstmt.close();
            conn.close();

            System.out.println("导入完成！！！");

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createTrainData(Random rd,String beginStation,String endStation,PreparedStatement pstmt) throws Exception{
        String[] trainType = ConstantTicket.TRAIN_TYPE;
        int[] trainTypeNum = ConstantTicket.TRAIN_TYPE_NUM;
        int trainNum = 0;
        for (int i = 0; i < trainType.length ; i++) {
            if(trainType[i].equals("G")){
                trainNum = rd.nextInt(trainTypeNum[i])+10;
            }
            else{
                trainNum = rd.nextInt(trainTypeNum[i]);
            }

            if(trainNum!=0){
                for (int j = 0; j < trainNum ; j++) {
                    pstmt.setString(1,randomTrainNo(trainType[i],rd));
                    pstmt.setString(2,trainType[i]);
                    pstmt.setString(3,beginStation);
                    pstmt.setString(4,endStation);
                    pstmt.setString(5,randomTime(rd));
                    pstmt.setString(6,randomTime(rd));
                    pstmt.setString(7,randomTime(rd));
                    pstmt.execute();
                    System.out.println("beginStation = [" + beginStation + "], endStation = [" + endStation + "]");
                }
            }
        }
    }

    public static String randomTrainNo(String trainType,Random rd){
        return  trainType+(rd.nextInt(999)+1);
    }

    public static String randomTime(Random rd){
        int hour = rd.nextInt(24);
        int min = rd.nextInt(60);
        String hh = hour<10?"0"+hour:""+hour;
        String mm = min<10?"0"+min:""+min;
        return hh+":"+mm;
    }
}
