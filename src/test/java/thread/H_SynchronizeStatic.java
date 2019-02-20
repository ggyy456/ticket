package thread;

/*
    synchronized还可以应用在static静态方法上，相当于对Class类进行持锁
    而synchronized用在普通方法上是给对象上锁，他们有本质的区别
    如果同一个类，创建两个不同的对象，那他们的普通方法上的synchronized是异步的，
    但如果是在静态方法上使用synchronized，那即使是创建不同的对象，他们也是同步的
    synchronized放在静态方法上作用等同于synchronized(Class)
 */
public class H_SynchronizeStatic {
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
        synchronized(H_SynchronizeStatic.class) {
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
        final H_SynchronizeStatic ss =  new H_SynchronizeStatic();

        Thread t1 = new Thread(){
            @Override
            public void run(){
                printA();
            }
        };
        t1.setName("a");
        t1.start();

        Thread t2 = new Thread(){
            @Override
            public void run(){
                ss.printB();
            }
        };
        t2.setName("b");
        t2.start();

        Thread t3 = new Thread(){
            @Override
            public void run(){
                ss.printC();
            }
        };
        t3.setName("c");
        t3.start();
    }
}
