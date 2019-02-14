package thread;

/*
    setPriority()设置线程优先级，1-10级，10级为最优先
 */
public class ThreadPriority extends Thread{

    @Override
    public void run() {
        for (int i = 0; i < 5000; i++) {
            Thread.yield();
            System.out.println(Thread.currentThread().getName()+":"+i);
        }
    }

    public static void main(String[] args) {
        ThreadPriority tp1 = new ThreadPriority();
        tp1.setPriority(1);
        tp1.start();

        ThreadPriority tp2 = new ThreadPriority();
        tp2.setPriority(8);
        tp2.start();
    }



}
