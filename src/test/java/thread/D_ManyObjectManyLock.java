package thread;

/*
    两个线程分别访问同一个类的两个不同实例的相同名称的同步方法，效果却是以异步的方式运行的。
    由于创建了2个业务对象，在系统中产生了2个锁，所以运行结果是异步的。
    关键字synchronized取得的是对象锁，而不是把一段代码或方法当作锁，所以哪个线程先执行带synchronized关键字的方法，
    哪个线程就持有该方法所属的对象的锁Lock，那么其他线程只能呈等待状态，前提是多个线程访问的是同一个对象。
    但如果多个线程访问多个对象，则JVM会创建多个锁，示例就是创建了2个ManyObjectManyLock类的对象
    所以就会产生2个锁，如果两个线程都调用同一个对象，则运行结果是同步的。
 */
public class D_ManyObjectManyLock {
    private int num = 0;

    synchronized public void setNum(String name){
        try {
            if("a".equals(name)){
                num = 100;
                System.out.println("a set over!");
                Thread.sleep(2000);
            }
            else{
                num = 200;
                System.out.println("b set over!");
            }
            System.out.println(name+" num="+num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final D_ManyObjectManyLock obj1 = new D_ManyObjectManyLock();
        final D_ManyObjectManyLock obj2 = new D_ManyObjectManyLock();

        new Thread(){
            @Override
            public void run(){
                obj1.setNum("a");
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                obj2.setNum("b");
                //obj1.setNum("b");
            }
        }.start();

    }
}
