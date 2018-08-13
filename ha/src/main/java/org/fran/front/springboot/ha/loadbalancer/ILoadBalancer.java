package org.fran.front.springboot.ha.loadbalancer;

import org.fran.front.springboot.ha.Server;

import java.util.List;

public interface ILoadBalancer {
    public Server getServer(List<Server> servers);
}
