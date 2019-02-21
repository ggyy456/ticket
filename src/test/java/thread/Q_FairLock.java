package thread;

import java.util.concurrent.locks.ReentrantLock;

/*
    Lock分为"公平锁"和"非公平锁":
    公平锁表示线程获取锁的顺序是按照线程加锁的顺序来分配的，即先start启动的线程就先获得锁；
    而非公平锁就是一种获取锁的抢占机制，先启动线程不一定就先获得锁。
 */
public class Q_FairLock {
    private ReentrantLock lock = new ReentrantLock(true);   //true为公平锁，false为非公平锁

    public void lock(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" 获得锁定！");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final Q_FairLock f = new Q_FairLock();

        Runnable r = new Runnable(){
            @Override
            public void run(){
                System.out.println(Thread.currentThread().getName()+" 运行了！");
                f.lock();
            }
        };

        Thread[] array = new Thread[10];
        for (int i = 0; i < 10; i++) {
            array[i] = new Thread(r);
        }
        for (int i = 0; i < 10; i++) {
            array[i].start();
        }
    }
}
