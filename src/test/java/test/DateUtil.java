package test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String currentDateStr(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        String date = df.format(new Date());
        return date;
    }

}
