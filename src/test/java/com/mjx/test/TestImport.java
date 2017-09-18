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
        //excelToDatabase();
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
