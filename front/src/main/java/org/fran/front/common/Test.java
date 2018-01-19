package org.fran.front.common;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by fran on 2018/1/16.
 */
public class Test {

    public static void main(String[] args){
        Test t = new Test();
        t.maxConnectionsPerRoute = 10;
        t.connectTimeoutMillis = 10000;
        t.socketTimeoutMillis = 10000;
        t.sslHostnameValidationEnabled = false;
        t.maxTotalConnections = 10;
        t.initialize();


        HttpGet get = new HttpGet("http://www.baidu.com");
        try {
            CloseableHttpResponse r = t.httpClient.execute(get);
            String res = t.inputStream2String(r.getEntity().getContent());
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String inputStream2String(InputStream in)throws IOException{
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for(int n; (n = in.read(b))!= -1;){
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    private final Timer connectionManagerTimer = new Timer(
            "SimpleHostRoutingFilter.connectionManagerTimer", true);

    int maxTotalConnections;
    int maxConnectionsPerRoute;
    int socketTimeoutMillis;
    int connectTimeoutMillis;
    long timeToLive;
    TimeUnit timeUnit =TimeUnit.SECONDS;
    boolean sslHostnameValidationEnabled;
    private HttpClientConnectionManager connectionManager;
    private CloseableHttpClient httpClient;
    private ApacheHttpClientFactory httpClientFactory;


    private void initialize() {
        ApacheHttpClientConnectionManagerFactory connectionManagerFactory = new DefaultApacheHttpClientConnectionManagerFactory();
        httpClientFactory = new DefaultApacheHttpClientFactory();

        this.connectionManager = connectionManagerFactory.newConnectionManager(
                this.sslHostnameValidationEnabled,
                maxTotalConnections,
                maxConnectionsPerRoute,
                timeToLive,
                timeUnit,
                null);
        this.httpClient = newClient();
        this.connectionManagerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Test.this.connectionManager == null) {
                    return;
                }
                Test.this.connectionManager.closeExpiredConnections();
            }
        }, 30000, 5000);
    }

    protected CloseableHttpClient newClient() {
        final RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeoutMillis)
                .setConnectTimeout(connectTimeoutMillis)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        return httpClientFactory.createBuilder().
                setDefaultRequestConfig(requestConfig).
                setConnectionManager(this.connectionManager).disableRedirectHandling().build();
    }
}
