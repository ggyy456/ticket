package com.mjx.test;

import com.mjx.entity.ConstantTicket;
import jxl.Sheet;
import jxl.Workbook;
import java.io.File;
import java.sql.*;
import java.util.Random;

/**
 * Created by Administrator on 2017-9-14.
 */
public class TestImport {
    public static void main(String[] args) {
        sqlToDatabase();
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


    //将excel文件导入数据库
    public static void excelToDatabase(){
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            System.out.println("正在连接数据库..........");
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            String url = "jdbc:db2://192.168.50.66:50001/lasntfdb:currentSchema=LAS;";
            String user = "las";//数据库用户名
            String pwd = "las";//数据库密码
            conn=(Connection) DriverManager.getConnection(url,user,pwd);
            System.out.println("数据库连接成功！！！");


            String sql="insert into t_user(USER_NAME,SEX,PHONE,PROVINCE,CITY,SOURCE) values(?,?,?,?,?,?)";

            pstmt = conn.prepareStatement(sql);

            File xlsFile = new File("F:/2222.xls");
            // 获得工作簿对象
            Workbook workbook = Workbook.getWorkbook(xlsFile);
            // 获得所有工作表
            Sheet sheet = workbook.getSheets()[0];
            // 获得行数
            int rows = sheet.getRows();
            // 读取数据
            for (int row = 49560; row < rows; row++)
            {
                String username = sheet.getCell(0, row).getContents();
                String source = sheet.getCell(1, row).getContents();
                String sex = sheet.getCell(2, row).getContents();
                String phone = sheet.getCell(3, row).getContents();
                String province = sheet.getCell(4, row).getContents();
                String city = sheet.getCell(5, row).getContents();

                pstmt.setString(1,username);
                pstmt.setString(2,sex);
                pstmt.setString(3,phone);
                pstmt.setString(4,province);
                pstmt.setString(5,city);
                pstmt.setString(6,source);

                pstmt.execute();
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
