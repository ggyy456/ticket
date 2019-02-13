package thread;

public class TestThread extends Thread{
    private int count = 5;

    public static void main(String[] args) {
        TestThread tt = new TestThread();

        Thread t1 = new Thread(tt,"A");
        Thread t2 = new Thread(tt,"B");
        Thread t3 = new Thread(tt,"C");
        Thread t4 = new Thread(tt,"D");
        Thread t5 = new Thread(tt,"E");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }


    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+count--);
    }
}
