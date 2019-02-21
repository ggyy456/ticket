package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    Condition用于唤醒线程，类似于notify方法；
    Condition是由Lock对象创建出来的，一个Lock对象可以创建多个Condition实例。
    Condition可以通过创建出来的多个实例，分别唤醒指定的线程。
 */
public class P1_Condition {
    private Lock lock = new ReentrantLock();
    private Condition condA = lock.newCondition();
    private Condition condB = lock.newCondition();

    public void awaitA(){
        try {
            lock.lock();
            System.out.println("awaitA 开始");
            condA.await();
            System.out.println("awaitA 结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void awaitB(){
        try {
            lock.lock();
            System.out.println("awaitB 开始");
            condB.await();
            System.out.println("awaitB 结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signalAllA(){
        try {
            lock.lock();
            System.out.println("signalA 开始");
            condA.signalAll();
            System.out.println("signalA 结束");
        } finally {
            lock.unlock();
        }
    }

    public void signalAllB(){
        try {
            lock.lock();
            System.out.println("signalB 开始");
            condB.signalAll();
            System.out.println("signalB 结束");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final P1_Condition c = new P1_Condition();

        new Thread(){
            @Override
            public void run(){
                c.awaitA();
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                c.awaitB();
            }
        }.start();

        try {
            Thread.sleep(2000);
            c.signalAllA();
            Thread.sleep(2000);
            c.signalAllB();

            System.out.println("全部结束！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
