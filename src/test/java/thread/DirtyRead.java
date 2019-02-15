package thread;

import javassist.convert.TransformReadField;

/*
    出现脏读是因为public void getValue()方法并不是同步的，所以可以在任意时候进行调用；
    解决方法当然就是加上同步synchronized关键字
 */
public class DirtyRead {
    private String username = "A";
    private String password = "AA";

    synchronized public void setValue(String username,String password){
        try{
            this.username = username;
            Thread.sleep(2000);
            this.password = password;
            System.out.println("setValue method thread name="+Thread.currentThread().getName()+" username="+username+" password="+password);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    //synchronized public void getValue(){
    public void getValue(){
        System.out.println("getValue method thread name="+Thread.currentThread().getName()+" username="+username+" password="+password);
    }

    public static void main(String[] args) {
        try {
            DirtyRead dr = new DirtyRead();
            ThreadDirty t = new ThreadDirty(dr);
            t.start();
            Thread.sleep(200);
            dr.getValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class ThreadDirty extends Thread {
    private DirtyRead dr;

    public ThreadDirty(DirtyRead dr){
        this.dr = dr;
    }

    @Override
    public void run(){
        dr.setValue("B","BB");
    }
}
