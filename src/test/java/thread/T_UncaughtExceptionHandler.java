package thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.UUID;

//UncaughtExceptionHandler可以捕捉线程内的异常
public class T_UncaughtExceptionHandler {

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                String uuid = UUID.randomUUID().toString();
                Integer.parseInt(uuid);
            }
        };

        UncaughtExceptionHandler ueh = new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("线程"+t.getName()+"出现了异常！");
                e.printStackTrace();
            }
        };

        Thread[] t = new Thread[5];
        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(r);
        }
        for (int i = 0; i < t.length; i++) {
            t[i].start();
            t[i].setUncaughtExceptionHandler(ueh);
        }
    }
}
