package thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    容易引起误解的题目：
    1、首先应该清楚在类里面的方法上加synchronized，“对象监听器”都是当前类对象，该类里面的所有方法都共用一个锁，他们是同步的。
    2、调用start表示新启动一个线程，而执行运行同步方法则只是属于main调用的普通方法，也属于main方法里的主线程，也是一个线程。
    3、运行以下程序，结果为，先调用b方法，然后调用a方法；出现这个结果，首先应该清楚的是，这两个方法共用同一个锁，
       所以a和b方法肯定是一前一后调用的，他们必须是同步的；如果先调用了b方法，那么即使里面休眠了一段时间，也得等他执行完以后再
       调用a方法；然后再来看看，为什么我们程序里先调用了t.start(),但还是先运行的b方法呢，以我个人的理解，start方法会启动线程，
       然后由线程来调用run方法，run方法再去调用a，然后b方法紧接着就调用了，所以b先抢占了锁。为了验证我的想法，可以在t.start()
       下面让主线程休眠10ms，这样出现的结果就是先调用a，然后调用b。
    4、如果把a或b其中一个改为static方法，则肯定会先调用a，因为这两个同步方法的“对象监听器”不是同一个，普通方法的“对象监听器”
       是类对象，静态方法的“对象监听器”是class，所以他们两者并不会同步，会异步调用。

 */
public class A_TestSubject {
    synchronized public void a(){
        //try {
        //Thread.sleep(2000);
        System.out.println(Thread.currentThread().getName()+" aaaaaaaaaaa");
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
    }

    synchronized public void b(){
        try {
            System.out.println(Thread.currentThread().getName()+" 进bbbbbbbbbbb");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName()+" 出bbbbbbbbbbb");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final A_TestSubject ts = new A_TestSubject();

        Thread t = new Thread(){
            @Override
            public void run(){
                ts.a();
            }
        };
        //t.start();

//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //ts.b();

        ts.simpleDateFormatNotSafe();
    }

    //SimpleDateFormat非线程安全，处理的时候需要加同步块或者加锁
    public void simpleDateFormatNotSafe(){
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String dateString = "2000-06-06";
        final Lock lock = new ReentrantLock();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    //lock.lock();
                    Date date = sdf.parse(dateString);
                    //lock.unlock();
                    String newDateString = sdf.format(date).toString();
                    System.out.println(dateString+"|"+newDateString);
                } catch (ParseException e) {
                    //e.printStackTrace();
                } finally {
                    //lock.unlock();
                }
            }
        };

        Thread[] t = new Thread[10];
        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(r);
        }
        for (int i = 0; i < t.length; i++) {
            t[i].start();
        }
    }
}
