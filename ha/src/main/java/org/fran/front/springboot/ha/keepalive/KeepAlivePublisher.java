package org.fran.front.springboot.ha.keepalive;

import org.fran.front.springboot.ha.Server;
import org.fran.front.springboot.ha.exceptions.KeepAliveError;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

public class KeepAlivePublisher implements Publisher<Server> {

    String expectedContent = null;
    String pingPath;
    Server server;
    IKeepAlive iKeepAlive;

    public KeepAlivePublisher(IKeepAlive iKeepAlive, Server server){
        this.pingPath = server.getPath();
        this.expectedContent = server.getExpectContent();
        this.server = server;
        this.iKeepAlive = iKeepAlive;
    }

    public String getPingPath(){
        return pingPath == null ? "" : pingPath;
    }

    public String getExpectedContent(){
        return expectedContent;
    }

    @Override
    public void subscribe(Subscriber s) {
        try {
            if(iKeepAlive.isAlive(server)){
                s.onComplete();
            }else{
                s.onError(new KeepAliveError());
            }
        } catch (Exception e) {
            s.onError(e);
        }
    }
}
