package thread;

import java.util.ArrayList;
import java.util.List;

/*
    为防止list.remove出异常，可用while来一直检查list的长度
    notify可能导致有的没有通知到，出现“假死”情况，使用notifyAll可以避免。
 */
public class J_ProduceConsume {
    private List list = new ArrayList();

    synchronized  public void produce(){
        try{
            while(list.size() == 1){
                System.out.println("produce 开始进入wait");
                this.wait();
                System.out.println("produce 开始离开wait");
            }
            list.add("anyString="+Math.random());
            this.notifyAll();
            System.out.println("produce="+list.size());
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    synchronized  public void consume(){
        try{
            while(list.size() == 0){
                System.out.println("consume 开始进入wait");
                this.wait();
                System.out.println("consume 开始离开wait");
            }
            list.remove(0);
            this.notifyAll();
            System.out.println("consume="+list.size());
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final J_ProduceConsume pc = new J_ProduceConsume();

        Thread produce = new Thread(){
            @Override
            public void run(){
                while(true){
                    pc.produce();
                }
            }
        };
        Thread consume1 = new Thread(){
            @Override
            public void run(){
                while(true){
                    pc.consume();
                }
            }
        };
        Thread consume2 = new Thread(){
            @Override
            public void run(){
                while(true){
                    pc.consume();
                }
            }
        };
        Thread consume3 = new Thread(){
            @Override
            public void run(){
                while(true){
                    pc.consume();
                }
            }
        };

        produce.start();
        consume1.start();
        consume2.start();
        consume3.start();
    }
}
