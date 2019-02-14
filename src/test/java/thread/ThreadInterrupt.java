package thread;

/*
    停止线程的方式：可以使用interrupt方法，stop方法已经被废弃。
    interrupt 用来给线程设置一个状态，在线程中可以通过isInterrupted方法来看这个状态是否存在，可进行手动停止线程运行。
    停止线程的方式可以return或者抛出异常，抛出异常可以让上层接收到具体的异常信息。
 */
public class ThreadInterrupt extends Thread{

    @Override
    public void run() {
        for (int i = 0; i < 500000; i++) {
            if(this.isInterrupted()){
                System.out.println("已经是停止状态，要退出了！");
                return;
                //throw new InterruptedException();
            }

            System.out.println("i="+i);
        }
    }

    public static void main(String[] args) {
        try {
            ThreadInterrupt ti = new ThreadInterrupt();
            ti.start();
            Thread.sleep(200);
            ti.interrupt();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }



}
