package thread;


import test.DateUtil;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class R_ReentrantReadWriteLock {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void read(){
        try {
            System.out.println(Thread.currentThread().getName()+" 进入读锁 "+ DateUtil.currentDateStr());
            lock.readLock().lock();
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName()+" 离开读锁 "+ DateUtil.currentDateStr());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(){
        try {
            System.out.println(Thread.currentThread().getName()+" 进入写锁 "+ DateUtil.currentDateStr());
            lock.writeLock().lock();
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName()+" 离开写锁 "+ DateUtil.currentDateStr());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void main(String[] args) {
        final R_ReentrantReadWriteLock rw = new R_ReentrantReadWriteLock();

        new Thread(){
            @Override
            public void run(){
                rw.read();
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                rw.read();
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                rw.write();
            }
        }.start();

    }
}
