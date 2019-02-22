package thread;

import test.DateUtil;

import java.util.Timer;
import java.util.TimerTask;

/*
    Timer类的主要作用是计划任务，如果设定的执行时间晚于当前时间，则按设定时间执行task任务；
    如果设定的执行时间早于当前时间，则立即执行task任务。
    TimerTask是以队列的方式一个一个被顺序执行的，所以执行的时间有可能和预期的时间不一致，
    因为前面的任务有可能消耗的时间比较长，则后面的任务运行的时间也会被延迟。
 */
public class S_Timer {
    //true表示守护线程，运行完毕就销毁；false表示非守护线程，运行完毕以后也不会销毁，默认是false
    private static Timer timer = new Timer(true);

    public static class MyTask extends TimerTask{
        @Override
        public void run() {
            System.out.println("运行时间："+ DateUtil.currentDateStr());
        }
    }

    public static void main(String[] args) {
        MyTask myTask = new MyTask();
        String datestring = "2019-02-21 16:38:10";
        System.out.println("开启时间："+ DateUtil.currentDateStr());
        timer.schedule(myTask,DateUtil.getDateByString(datestring));
        //可以设置每隔一段时间执行一次，设置这个参数必须Timer构造函数要设置为false才能生效
        //timer.schedule(myTask,DateUtil.getDateByString(datestring),2000);
    }


}
