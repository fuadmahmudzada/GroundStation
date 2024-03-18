//package org.example.groundstationdemo;
//
//
//public class Example {
//    private final String name;
//    public Example(String name) {
//        this.name = name;
//    }
//    public String getName() {
//        return this.name;
//    }
//
//    public synchronized void call(Example caller) {
//        System.out.println(this.getName() + "has asked to call me" + caller.getName());
//        try {
//            Thread.sleep(100);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        caller.callMe(this);
//    }
//    public synchronized void callMe(Example caller){
//        System.out.println(this.getName() + "has called me" + caller.getName());
//    }
//    public static void main(String[] args) {
//        Example caller1 = new Example("caller-1");
//        Example caller2 = new Example("caller-2");
//        new Thread(new Runnable()) {
//            public void run() {
//                caller1.call(caller2);
//            }
//        }).start();
//new Thread(new Runnable()){
//    public void run() {
//        caller2.call(caller1);}
//    }).start();
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//public class RaceTest2 {
//
//
//    final static int NUMRUNNERS = 2;
//
//    public static void main(String[] args) {
//
//        PoliteRunner[] runners = new PoliteRunner[NUMRUNNERS];
//
//        for (int i = 0; i < NUMRUNNERS; i++) {
//            runners[i] = new PoliteRunner(i);
//            runners[i].setPriority(2);
//        }
//        for (int i = 0; i < NUMRUNNERS; i++) {
//            runners[i].start();
//        }
//    }
//}
//class PoliteRunner extends Thread {
//
//    public int tick = 1;
//    public int num;
//
//    PoliteRunner(int num) {
//        this.num = num;
//    }
//
//    public void run() {
//        while (tick < 400000) {
//            tick++;
//            if ((tick % 50000) == 0) {
//                System.out.println("Thread #" + num + ", tick = " + tick);
//                Thread.yield();
//            }
//        }
//    }
//}