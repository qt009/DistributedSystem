package Thrift;

import org.apache.thrift.TProcessor;

public class Thrift extends Thread{
    TProcessor tp;
    public static void main (String[] args) throws InterruptedException {
        while (true){
            System.out.println("hello");
            Thread.sleep(3000);
        }
    }
}
