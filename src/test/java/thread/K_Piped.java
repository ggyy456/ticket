package thread;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/*
    管道流(pipeStream)，用于在不同线程间直接传送数据，通过使用管道，实现不同线程的通信，而无须借助于类似临时文件之类的东西。
    在Java的JDK中提供了4个类来使线程间进行通信：
    1）PipedInputStream 和 PipedOutputStream
    2）PipedReader 和 PipedWriter
 */

public class K_Piped {
    public void writeMethod(PipedWriter out){
        try {
            System.out.println("write:");
            for (int i = 0; i < 300; i++) {
                String data = ""+(i+1);
                out.write(data);
                System.out.print(data);
            }
            System.out.println();

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMethod(PipedReader in){
        try {
            System.out.println("read:");
            char[] byteArray = new char[20];
            int readLength = in.read(byteArray);
            while (readLength != -1) {
                String data = new String(byteArray,0,readLength);
                System.out.print(data);
                readLength = in.read(byteArray);
            }
            System.out.println();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            final K_Piped pp = new K_Piped();
            final PipedWriter out = new PipedWriter();
            final PipedReader in = new PipedReader();
            out.connect(in);

            Thread tr = new Thread(){
                @Override
                public void run(){
                    pp.readMethod(in);
                }
            };
            tr.start();

            Thread tw = new Thread(){
                @Override
                public void run(){
                    pp.writeMethod(out);
                }
            };

            Thread.sleep(2000);
            tw.start();
        } catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}


