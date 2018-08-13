package org.fran.front.springboot.ha.keepalive;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.fran.front.springboot.ha.Server;

import java.io.IOException;

public class DefaultKeepAlive implements IKeepAlive{

    private static RequestConfig requestConfig = null;
    private static CloseableHttpClient httpClient = null;

    private static int connectTimeout = 3000;
    private static int connectionRequestTimeout = 10000;
    private static int socketTimeOut = 10000;

    static {
        httpClient = HttpClientBuilder.create().build();
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeOut)
                .build();
    }

    @Override
    public boolean isAlive(Server server) throws Exception {
        String urlStr = server.getUrl();
        urlStr += server.getPath();

        boolean isAlive = false;

        HttpGet getRequest = new HttpGet(urlStr);
        getRequest.setConfig(requestConfig);
        String content= null;
        try {
            HttpResponse response = httpClient.execute(getRequest);
            content = EntityUtils.toString(response.getEntity());
            isAlive = (response.getStatusLine().getStatusCode() == 200);
            if (server.getExpectContent()!=null){
                if (content == null){
                    isAlive = false;
                }else{
                    if (content.equals(server.getExpectContent())){
                        isAlive = true;
                    }else{
                        isAlive = false;
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        }finally{
            // Release the connection.
            getRequest.abort();
        }

        return isAlive;
    }
}
