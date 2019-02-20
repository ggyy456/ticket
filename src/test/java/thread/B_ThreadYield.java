package thread;

/*
    yield()方法的作用是放弃当前的CPU资源，将它让给其他的任务去占用CPU执行时间。
    但放弃的时间不确定，有可能刚刚放弃，马上又获得CPU时间片
 */
public class B_ThreadYield extends Thread{

    @Override
    public void run() {
        for (int i = 0; i < 500000; i++) {
            Thread.yield();
            System.out.println("i="+i);
        }
    }

    public static void main(String[] args) {
        B_ThreadYield ty = new B_ThreadYield();
        ty.start();
    }



}
