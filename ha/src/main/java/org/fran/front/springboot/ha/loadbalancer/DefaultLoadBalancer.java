package org.fran.front.springboot.ha.loadbalancer;

import org.fran.front.springboot.ha.Server;

import java.util.ArrayList;
import java.util.List;

public class DefaultLoadBalancer implements ILoadBalancer{
    @Override
    public Server getServer(List<Server> servers) {
        if(servers == null || servers.size() == 0)
            return null;
        else{
            long tid = Thread.currentThread().getId();
            long num = tid % servers.size();
            Server s = servers.get(Long.valueOf(num).intValue());
            if(!s.isDelete() && s.getStage())
                return s;
            else
                return next(Long.valueOf(num).intValue(), servers);
        }
    }

    private Server next(int index, List<Server> servers){
        boolean f = true;
        for(int i =index; i < servers.size(); i ++){
            Server r = servers.get(i);
            if(!r.isDelete() && r.getStage()){
                return r;
            }
            if(i == servers.size() - 1 && f){ i = 0; f = false;}
        }
        return servers.get(0);
    }

}
