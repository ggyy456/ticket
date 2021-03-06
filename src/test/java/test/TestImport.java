package test;

import com.mjx.util.ConfigHelper;
import jxl.Sheet;
import jxl.Workbook;
import java.io.File;
import java.sql.*;

/**
 * Created by Administrator on 2017-9-14.
 */
public class TestImport {
    public static void main(String[] args) {
        excelToDatabase();
    }

    //将excel文件导入数据库
    public static void excelToDatabase(){
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            System.out.println("正在连接数据库..........");
            Class.forName(ConfigHelper.getJdbcDriver());
            String url = ConfigHelper.getJdbcUrl();
            String user = ConfigHelper.getJdbcUsername();
            String pwd = ConfigHelper.getJdbcPassword();
            conn=(Connection) DriverManager.getConnection(url,user,pwd);
            conn.setAutoCommit(false); // 设置手动提交
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
            for (int row = 0; row < rows; row++)
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

                pstmt.addBatch();
            }

            pstmt.executeBatch(); // 执行批量处理
            conn.commit();  // 提交

            pstmt.close();
            conn.close();


            System.out.println("导入完成！！！");

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
