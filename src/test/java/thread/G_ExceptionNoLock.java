package thread;

/*
    当一个线程执行的代码出现异常时，其所持有的锁会自动释放
    线程a出现异常并释放锁，线程b进入方法正常打印
 */
public class G_ExceptionNoLock {
    synchronized public void testMethod(){
        if("a".equals(Thread.currentThread().getName())){
            System.out.println("ThreadName="+Thread.currentThread().getName()+",beginTime="+System.currentTimeMillis());

            while (true){
                if("0.123456".equals((""+Math.random()).substring(0,8))){
                    System.out.println("ThreadName="+Thread.currentThread().getName()+",excepitonTime="+System.currentTimeMillis());
                    Integer.parseInt("a");
                }
            }
        }
        else{
            System.out.println("ThreadName="+Thread.currentThread().getName()+",beginTime="+System.currentTimeMillis());
        }
    }

    public static void main(String[] args) {
        try {
            final G_ExceptionNoLock enl = new G_ExceptionNoLock();

            Thread a = new Thread(){
                @Override
                public void run(){
                    enl.testMethod();
                }
            };
            a.setName("a");
            a.start();

            Thread.sleep(500);

            Thread b = new Thread(){
                @Override
                public void run(){
                    enl.testMethod();
                }
            };
            b.setName("b");
            b.start();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
