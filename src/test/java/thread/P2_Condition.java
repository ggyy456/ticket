package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    Condition实现顺序执行,无论多个线程是什么顺序执行，能保证ABC三个方法是依次执行。
 */
public class P2_Condition {
    volatile public static int nextPrint = 1;
    private Lock lock = new ReentrantLock();
    private Condition condA = lock.newCondition();
    private Condition condB = lock.newCondition();
    private Condition condC = lock.newCondition();

    public void methodA(){
        try {
            lock.lock();
            while(nextPrint != 1){
                condA.await();
            }
            nextPrint = 2;
            System.out.println(Thread.currentThread().getName()+"执行A");
            condB.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void methodB(){
        try {
            lock.lock();
            while(nextPrint != 2){
                condB.await();
            }
            nextPrint = 3;
            System.out.println(Thread.currentThread().getName()+"执行B");
            condC.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void methodC(){
        try {
            lock.lock();
            while(nextPrint != 3){
                condC.await();
            }
            nextPrint = 1;
            System.out.println(Thread.currentThread().getName()+"执行C");
            condA.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final P2_Condition c = new P2_Condition();

        Runnable ra = new Runnable(){
            @Override
            public void run(){
                c.methodA();
            }
        };
        Runnable rb = new Runnable(){
            @Override
            public void run(){
                c.methodB();
            }
        };
        Runnable rc = new Runnable(){
            @Override
            public void run(){
                c.methodC();
            }
        };

        Thread[] aArray = new Thread[5];
        Thread[] bArray = new Thread[5];
        Thread[] cArray = new Thread[5];

        for (int i = 0; i < 5; i++) {
            aArray[i] = new Thread(ra);
            bArray[i] = new Thread(rb);
            cArray[i] = new Thread(rc);

            bArray[i].start();
            aArray[i].start();
            cArray[i].start();

        }


    }
}
