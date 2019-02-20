package thread;

/*
    join的作用，等待子线程运行完毕后，才能执行主线程的代码
    Thread.sleep方法不释放锁，而join是会释放锁的
 */
public class L_ThreadJoin extends Thread {
    @Override
    public void run(){
        try{
            System.out.println(Thread.currentThread().getName()+"：begin");
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName()+"：end");
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    synchronized  public void last(){
        System.out.println(Thread.currentThread().getName()+"：last");
    }

    public static void main(String[] args) {
        final L_ThreadJoin tj = new L_ThreadJoin();
        tj.setName("tj");

        Thread t = new Thread(){
            @Override
            public void run(){
                try {
                    synchronized (tj) {
                        System.out.println(Thread.currentThread().getName()+"：begin");
                        tj.start();
                        //tj.join();
                        Thread.sleep(3000);
                        System.out.println(Thread.currentThread().getName()+"：end");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.setName("t");
        t.start();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tj.last();
        //System.out.println(Thread.currentThread().getName()+"：last");
    }

}
