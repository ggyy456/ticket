package thread;

import java.util.ArrayList;
import java.util.List;

/*
    java为每个Object都实现了wait()和notify()方法，他们必须用在被synchronized同步的Object的临界区内。
    通过调用wait方法可以使处于临界区内的线程进入等待状态，同时释放被同步对象的锁。
    而notify操作可以唤醒一个因调用了wait操作而处于阻塞状态中的线程，使其进入就绪状态。
    被重新唤醒的线程会试图重新获得临界区的控制权，也就是锁，并继续执行临界区内wait之后的代码。
    wait被执行后，锁被自动释放，但执行完notify方法，锁却不自动释放，等同步方法块里面的代码都执行完毕才会释放锁。
    如果有多个wait，调用notify一次只随机通知一个线程进行唤醒，notifyAll唤醒全部。
    wait(long)带参数，表示等待一段时间以后，无论是否被唤醒，超过这个时间则自动唤醒。
 */
public class I_WaitNotify {
    private static List list = new ArrayList();

    public static void add(){
        list.add("anyString");
    }

    public static int size(){
        return list.size();
    }

    public static void main(String[] args) {
        try {
            final Object lock = new Object();

            new Thread(){
                @Override
                public void run(){
                    try{
                        synchronized (lock){
                            System.out.println("wait begin time=" + System.currentTimeMillis());
                            lock.wait();
                            System.out.println("wait end time=" + System.currentTimeMillis());
                        }
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }.start();

            Thread.sleep(50);

            new Thread(){
                @Override
                public void run(){
                    try{
                        synchronized (lock){
                            for (int i = 0; i < 10; i++) {
                                I_WaitNotify.add();
                                if(I_WaitNotify.size() == 5){
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
            }.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
