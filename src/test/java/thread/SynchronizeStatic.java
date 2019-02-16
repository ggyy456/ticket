package thread;

/*
    synchronized还可以应用在static静态方法上，相当于对Class类进行持锁
    而synchronized用在普通方法上是给对象上锁，他们有本质的区别
    如果同一个类，创建两个不同的对象，那他们的普通方法上的synchronized是异步的，
    但如果是在静态方法上使用synchronized，那即使是创建不同的对象，他们也是同步的
    synchronized放在静态方法上作用等同于synchronized(Class)
 */
public class SynchronizeStatic {
    synchronized public static void printA(){
        try {
            System.out.println("线程名称为：" + Thread.currentThread().getName() +"在"+ System.currentTimeMillis()+"进入printA");
            Thread.sleep(2000);
            System.out.println("线程名称为：" + Thread.currentThread().getName() +"在"+ System.currentTimeMillis()+ "离开printA");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printB(){
        synchronized(SynchronizeStatic.class) {
            try {
                System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入printB");
                Thread.sleep(2000);
                System.out.println("线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开printB");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized public void printC(){
        try {
            System.out.println("线程名称为：" + Thread.currentThread().getName() +"在"+ System.currentTimeMillis()+ "进入printC");
            Thread.sleep(2000);
            System.out.println("线程名称为：" + Thread.currentThread().getName() +"在"+ System.currentTimeMillis()+ "离开printC");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SynchronizeStatic ss =  new SynchronizeStatic();
        ThreadStatic t1 = new ThreadStatic(ss,"a");
        t1.setName("a");
        t1.start();
        ThreadStatic t2 = new ThreadStatic(ss,"b");
        t2.setName("b");
        t2.start();
        ThreadStatic t3 = new ThreadStatic(ss,"c");
        t3.setName("c");
        t3.start();
    }
}

class ThreadStatic extends Thread {
    private SynchronizeStatic ss;
    private String sign;

    public ThreadStatic(SynchronizeStatic ss,String sign){
        this.ss = ss;
        this.sign = sign;
    }

    @Override
    public void run(){
        if("a".equals(sign)) {
            SynchronizeStatic.printA();
        }
        else if("b".equals(sign)) {
            ss.printB();
        }else if("c".equals(sign)) {
            ss.printC();
        }
    }
}