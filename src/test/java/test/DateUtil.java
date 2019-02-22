package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String currentDateStr(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        String date = df.format(new Date());
        return date;
    }

    public static Date getDateByString(String datestring){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

}
