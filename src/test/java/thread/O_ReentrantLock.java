package thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    ReentrantLock作用等同于synchronized方法
    lock持有的是“对象监听器”
 */
public class O_ReentrantLock {
    private Lock lock = new ReentrantLock();

    public void methodA(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" methodA begin");
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getName()+" methodA end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void methodB(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" methodB begin");
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getName()+" methodB end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final O_ReentrantLock rl = new O_ReentrantLock();

        new Thread(){
            @Override
            public void run(){
                rl.methodA();
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                rl.methodB();
            }
        }.start();
    }


}
