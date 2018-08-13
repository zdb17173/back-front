package org.fran.front.springboot.ha.test;

import org.fran.front.springboot.ha.Server;
import org.fran.front.springboot.ha.loadbalancer.DefaultLoadBalancer;

import java.util.ArrayList;
import java.util.List;

public class TestDefaultLoadBalancer {
    public static void main(String[] args){

        List<Server> ss = new ArrayList<>();
        ss.add(new Server(0, "111"){
            {
                setState(false);
            }
        });
        ss.add(new Server(1, "222"){
            {
                setState(true);
            }
        });
        ss.add(new Server(2, "333"){
            {
                setDelete(false);
            }
        });
        ss.add(new Server(3, "444"){
            {
                setDelete(true);
            }
        });
        ss.add(new Server(4, "555"){
            {
                setState(false);
            }
        });

        System.out.println(new DefaultLoadBalancer().getServer(ss));
    }
}
