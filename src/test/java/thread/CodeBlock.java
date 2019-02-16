package thread;

/*
    1、synchronized (this)
    当一个线程访问object的一个synchronized同步代码块时,另一个线程仍然可以访问该object对象中的非synchronized同步代码块,可以加快代码执行效率。
    在使用同步synchronized(this)同步代码块时需要主要的是，当一个线程访问object的一个synchronized同步代码块时，其他线程对同一个object中
    的所有其他synchronized同步代码块的访问将被阻塞，这说明synchronized使用的“对象监视器”是一个。

    2、synchronized (object)
    当持有“对象监视器”为同一个对象的前提下，同一时间只有一个线程可以执行synchronized (object)同步代码块中的代码。
    如果“对象监视器”为不同对象，则同一时间可以有多个线程执行同步代码块中的代码。

    总结：this为当前类对象，object可以传入任何对象
    如果使用this，则该类中其他的同步代码块也呈阻塞状态
    如果使用object，则与不同对象监视器的其他同步代码块互补干扰，可以异步执行，这样也很容易造成脏读
 */
public class CodeBlock {
    private String data1;
    private String data2;

    public void synchronizeWithThis(){
        try {
            System.out.println(Thread.currentThread().getName()+" begin synchronizeWithThis");
            Thread.sleep(3000);
            String getData1 = "长时间处理任务后从远程返回的值1 threadName="+Thread.currentThread().getName();
            String getData2 = "长时间处理任务后从远程返回的值2 threadName="+Thread.currentThread().getName();

            synchronized (this){
                data1 = getData1;
                data2 = getData2;
            }

            System.out.println(data1);
            System.out.println(data2);
            System.out.println(Thread.currentThread().getName()+" end synchronizeWithThis");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void synchronizeWithObject(){
        try {
            String anyString = new String();
            synchronized(anyString){
                System.out.println(Thread.currentThread().getName()+" begin synchronizeWithObject");
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName()+" end synchronizeWithObject");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CodeBlock cb = new CodeBlock();
        ThreadBlockThis tt1 = new ThreadBlockThis(cb);
        ThreadBlockThis tt2 = new ThreadBlockThis(cb);
        tt1.start();
        tt2.start();

        //ThreadBlockObject to = new ThreadBlockObject(cb);
        //to.start();
    }

}

class ThreadBlockThis extends Thread {
    private CodeBlock cb;

    public ThreadBlockThis(CodeBlock cb){
        this.cb = cb;
    }

    @Override
    public void run(){
        cb.synchronizeWithThis();
    }
}

class ThreadBlockObject extends Thread {
    private CodeBlock cb;

    public ThreadBlockObject(CodeBlock cb){
        this.cb = cb;
    }

    @Override
    public void run(){
        cb.synchronizeWithObject();
    }
}
