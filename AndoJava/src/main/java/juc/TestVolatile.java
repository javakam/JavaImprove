package juc;

import java.util.concurrent.ConcurrentHashMap;

/*
 * 一、volatile(互斥锁) 关键字：当多个线程进行操作共享数据时，可以保证内存中的数据可见。
 * 					   相较于 synchronized 是一种较为轻量级的同步策略。
 *
 * 注意：
 * 1. volatile 不具备“互斥性” -> 每个线程都可以同时访问,不过每次访问都是从主存中取
 * 2. volatile 不能保证变量的“原子性” -> 不可分割性
 * 3. volatile 每次都会从主存中取值,相对于什么都不设置,执行效率是降低了(JVM 重排序),但是相较于 synchronized 方式,效率提高很多
 */
public class TestVolatile {

    public static void main(String[] args) {

        ThreadDemo td = new ThreadDemo();
        new Thread(td).start();

        while (true) {
            if (td.isFlag()) {
                System.out.println("------------------");
                break;
            }
            //如果 flag 没有 volatile 修饰,可以采用 synchronized 方式,每次都会重新从主存中读数据并刷新缓存,效率会降低
            //synchronized (td){
            //}
        }
    }

}

//https://blog.csdn.net/javazejian/article/details/72772461
//https://www.cnblogs.com/sunleejon/p/12499518.html
class Yzx {
    public volatile int inc = 0;

    public void increase() {
        inc++;
    }

    public static void main(String[] args) {
        System.out.println("原子性------------------");

        final Yzx test = new Yzx();
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    for (int j = 0; j < 1000; j++)
                        test.increase();
                }
            }.start();
        }
        while (Thread.activeCount() > 1)  //保证前面的线程都执行完
            Thread.yield();
        System.out.println(test.inc);
    }
}

class ThreadDemo implements Runnable {

    private volatile boolean flag = false;

    @Override
    public void run() {

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }

        flag = true;

        System.out.println("flag=" + isFlag());

    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}