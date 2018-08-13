package org.fran.front.springboot.ha;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import org.fran.front.springboot.ha.keepalive.DefaultKeepAlive;
import org.fran.front.springboot.ha.keepalive.IKeepAlive;
import org.fran.front.springboot.ha.keepalive.KeepAlivePublisher;
import org.fran.front.springboot.ha.loadbalancer.DefaultLoadBalancer;
import org.fran.front.springboot.ha.loadbalancer.ILoadBalancer;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HAService {

    Keeper keeper;
    ILoadBalancer loadBalancerHandle;
    IKeepAlive keepAliveHandle;
    Executor executor;
    boolean started = false;
    long interval = 3000;

    public static HAService build(){
        HAService ha = new HAService();
        ha.keeper = new Keeper();
        return ha;
    }

    public HAService withDiscovery(){
        return this;
    }

    public HAService withKeepAlive(IKeepAlive iKeepAlive){
        this.keepAliveHandle = iKeepAlive;
        return this;
    }

    public HAService withLoadBalance(ILoadBalancer iLoadBalancer){
        this.loadBalancerHandle = iLoadBalancer;
        return this;
    }

    public HAService withFixedServer(String key, String... urls){
        for(String url : urls){
            keeper.addServer(key, 0 , url);
        }
        return this;
    }

    public HAService withFixedServer(String key, boolean keepalive, String keepalivePath,
                                     String keepaliveExpectedContent, String... urls){
        for(String url : urls){
            keeper.addServer(key, 0 , url, keepalive, keepalivePath , keepaliveExpectedContent);
        }
        return this;
    }

    public HAService withExecutor(Executor executor){
        this.executor = executor;
        return this;
    }

    public HAService start(){
        init();
        return this;
    }

    public void destroy(){

    }

    public Server getServer(String key){
        if(started)
            return loadBalancerHandle.getServer(keeper.getServers(key).servers);
        else
            return null;
    }

    private void init(){
        //init loadbalancer
        if(loadBalancerHandle == null){
            loadBalancerHandle = new DefaultLoadBalancer();
        }

        //init keepalive executor
        if(executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }

        //TODO: discovery server

        //keepalive thread
        keepalive();
        executor.execute(()->{
            while(true){
                try{
                    keepalive();
                }catch (Exception e){}
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    break;
                }
            }

        });

        started = true;
    }

    private void keepalive(){

        Flowable.just(keeper.servers).concatMap(s -> {
            List<Server> allserver = new ArrayList<>();
            for(ServerGroup ss : s.values()){
                if(ss.using) {
                    allserver.addAll(ss.getServers());
                }
            }
            return (subscriber) ->{
                subscriber.onNext(allserver);
            };
        }).subscribe( all -> {
            for (Server server :
                    (List<Server>)all) {
                try{
                    Flowable.just(server).concatMap((s) -> {
                        return new KeepAlivePublisher(keepAliveHandle, s);
                    }).doOnError(err -> {

                    }).subscribe(next ->{ server.setState(true); }, err->{ server.setState(false); });

                    System.out.println(server);
                }catch (Exception e){}
            }
        });


        /*.subscribe(allsever->{
            for (Server server :
                    (List<Server>)allsever) {
                Flowable<Server> flow = Flowable.just(server).concatMap((s) -> {
                    return new KeepAlivePublisher(keepAliveHandle, s);
                }).doOnError(err -> {
                    server.setState(false);
                });
                try{
                    flow.blockingFirst();
                }catch (Exception e){}
                System.out.println(server);
            }
        })*/

        /*Flowable<Server> flow = Flowable.just(server).concatMap((s) -> {
            return new KeepAlivePublisher(keepAliveHandle, s);
        }).doOnError(err -> {
            server.setState(false);
        });
        try{
            flow.blockingFirst();
        }catch (Exception e){}*/
    }

    private static class Keeper {
        private Map<String, ServerGroup> servers = new ConcurrentHashMap<>();

        protected ServerGroup getServers(String key){
            return servers.get(key);
        }

        public void addServer(String key, long id, String url){
            addServer(key, id, url, false, null, null);
        }

        public void addServer(String key, long id, String url, boolean useKeepalive, String path, String expectedContent){
            if(servers.containsKey(key)){
                synchronized (servers) {
                    servers.get(key).add(new Server(id, url, path, expectedContent));
                }
            }else{
                synchronized (servers){
                    ServerGroup group = new ServerGroup();
                    group.using = useKeepalive;
                    group.add(new Server(id, url, path, expectedContent));
                    servers.put(key, group);
                }
            }
        }
    }

    private static class ServerGroup{
        List<Server> servers = new ArrayList<>();
        private boolean using;

        public void add(Server server){
            servers.add(server);
        }

        public List<Server> getServers() {
            return servers;
        }

        public void setServers(List<Server> servers) {
            this.servers = servers;
        }

        public boolean isUsing() {
            return using;
        }

        public void setUsing(boolean using) {
            this.using = using;
        }
    }


}
