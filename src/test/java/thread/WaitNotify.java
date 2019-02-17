package thread;

import java.util.ArrayList;
import java.util.List;

/*
    java为每个Object都实现了wait()和notify()方法，他们必须用在被synchronized同步的Object的临界区内。
    通过调用wait()方法可以使处于临界区内的线程进入等待状态，同时释放被同步对象的锁。
    而notify操作可以唤醒一个因调用了wait操作而处于阻塞状态中的线程，使其进入就绪状态。
    被重新唤醒的线程会试图重新获得临界区的控制权，也就是锁，并继续执行临界区内wait之后的代码。
 */
public class WaitNotify {
    private static List list = new ArrayList();

    public static void add(){
        list.add("anyString");
    }

    public static int size(){
        return list.size();
    }

    public static void main(String[] args) {
        try {
            Object lock = new Object();
            Thread1 a = new Thread1(lock);
            a.start();
            Thread.sleep(50);

            Thread2 b = new Thread2(lock);
            b.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Thread1 extends Thread{
    private Object lock;

    public Thread1(Object lock){
        this.lock = lock;
    }

    @Override
    public void run(){
        try{
            synchronized (lock){
                if(WaitNotify.size() != 5) {
                    System.out.println("wait begin time=" + System.currentTimeMillis());
                    lock.wait();
                    System.out.println("wait end time=" + System.currentTimeMillis());
                }
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}

class Thread2 extends Thread{
    private Object lock;

    public Thread2(Object lock){
        this.lock = lock;
    }

    @Override
    public void run(){
        try{
            synchronized (lock){
                for (int i = 0; i < 10; i++) {
                    WaitNotify.add();
                    if(WaitNotify.size() == 5){
                        lock.notify();
                        System.out.println("已发出通知！");
                    }

                    System.out.println("添加了" + (i+1) + "个元素！");
                    Thread.sleep(100);
                }
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}