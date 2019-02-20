package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    Condition用于唤醒线程，类似于notify方法；
    Condition是由Lock对象创建出来的，一个Lock对象可以创建多个Condition实例。
 */
public class P_Condition {
    private Lock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();

    public void await(){
        try {
            lock.lock();
            System.out.println("await");
            cond.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signal(){
        try {
            lock.lock();
            System.out.println("signal");
            cond.signal();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final P_Condition c = new P_Condition();

        new Thread(){
            @Override
            public void run(){
                c.await();
            }
        }.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        c.signal();

    }
}
