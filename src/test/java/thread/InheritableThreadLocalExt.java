package thread;

import java.util.Date;

/*
    InheritableThreadLocal可以在子线程中取得父线程继承下来的值。
    同时还可以通过覆盖childValue方法对值进行进一步处理。
 */
public class InheritableThreadLocalExt {
    public static InheritableThreadLocal<String> itl = new InheritableThreadLocal<String>(){
        @Override
        protected String initialValue(){
            return ""+System.currentTimeMillis();
        }

        @Override
        protected String childValue(String parentValue){
            return parentValue+" 我在子线程中增加的！";
        }
    };

    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName()+":"+itl.get());

        new Thread(){
            @Override
            public void run(){
                System.out.println(Thread.currentThread().getName()+":"+itl.get());
            }
        }.start();
    }

}
