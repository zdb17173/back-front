package org.fran.front.springboot.ha.keepalive;

import org.fran.front.springboot.ha.Server;

public interface IKeepAlive {
    public boolean isAlive(Server server) throws Exception;
}
