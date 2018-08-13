package org.fran.front.springboot.ha.test;

import org.fran.front.springboot.ha.Server;
import org.fran.front.springboot.ha.keepalive.DefaultKeepAlive;
import org.fran.front.springboot.ha.keepalive.KeepAlivePublisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class TestKeepAlivePublisher {
    public static void main(String[] args){

//        KeepAlivePublisher b = new KeepAlivePublisher(new Server(1l, "http://www.baidu.com"));
        KeepAlivePublisher b = new KeepAlivePublisher(
                new DefaultKeepAlive(),
                new Server(1l, "http://www.google.com"));
        b.subscribe(new Subscriber() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Object o) {
                System.out.println(o);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
