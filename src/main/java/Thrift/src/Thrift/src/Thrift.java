package Thrift.src;

import org.apache.thrift.*;
public class Thrift extends Thread{
    TProcessor tp;
    public static void main (String[] args) throws InterruptedException {
        while (true){
            System.out.println("hello");
            Thread.sleep(3000);
        }
    }
}
