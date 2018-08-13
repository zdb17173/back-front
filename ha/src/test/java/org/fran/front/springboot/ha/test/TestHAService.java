package org.fran.front.springboot.ha.test;

import org.fran.front.springboot.ha.HAService;
import org.fran.front.springboot.ha.Server;
import org.fran.front.springboot.ha.keepalive.DefaultKeepAlive;
import org.fran.front.springboot.ha.loadbalancer.DefaultLoadBalancer;

public class TestHAService {
    public static void main(String[] args) throws InterruptedException {
        HAService ha = HAService.build()
                .withKeepAlive(new DefaultKeepAlive())
                .withLoadBalance(new DefaultLoadBalancer())
                .withFixedServer("a",true, null, null,
                        "http://www.google.com", "http://www.baidu.com",
                        "http://dsadsa.ccc", "https://www.cgtn.com/",
                        "http://www.sina.com")
                .start();

        new Thread(()->{
            while(true) {
                System.out.println(Thread.currentThread().getId() + ":" + ha.getServer("a"));
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
            while(true) {
                System.out.println(Thread.currentThread().getId() + ":" + ha.getServer("a"));
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
            while(true) {
                System.out.println(Thread.currentThread().getId() + ":" + ha.getServer("a"));
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(100000l);

    }
}
