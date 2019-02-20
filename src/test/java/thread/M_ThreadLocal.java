package thread;

/*
    ThreadLocal实现每个线程绑定自己的值，从而隔离其他线程的值。
 */
public class M_ThreadLocal {
    public static ThreadLocal<String> tl = new ThreadLocal<String>();

    public static void main(String[] args) {

        new Thread(){
            @Override
            public void run(){
                if(tl.get()==null){
                    tl.set("a");
                }
                System.out.println(Thread.currentThread().getName()+":"+tl.get());
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                if(tl.get()==null){
                    tl.set("b");
                }
                System.out.println(Thread.currentThread().getName()+":"+tl.get());
            }
        }.start();

        if(tl.get()==null){
            tl.set("c");
        }
        System.out.println(Thread.currentThread().getName()+":"+tl.get());
    }

}
