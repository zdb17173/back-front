package org.fran.front.fileupload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

/**
 * Created by leon on 2016/8/1.
 */
public class FileUploadClient {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadClient.class);

    private String domain;
    private String newsUploadUrl = "/api/news/upload";
    private String uploadUrl = "/api/upload/do";
    private String domainUploadUrl = "/api/upload/domain";
    private String newsPublishUrl = "/api/news/publish";

    private HttpClient client;
    private ObjectMapper mapper;

    public FileUploadClient(String address){
        this.domain = address;
        this.newsUploadUrl = this.domain + this.newsUploadUrl;
        this.newsPublishUrl = this.domain + this.newsPublishUrl;
        this.uploadUrl = this.domain + this.uploadUrl;
        this.domainUploadUrl = this.domain + this.domainUploadUrl;

        this.client = HttpClientBuilder.create().build();

        this.mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public UploadResult doNewsUpload(String newsId, InputStream fileInputStream, String fileName) {
        UploadResult uploadResult = new UploadResult();

        HttpPost post = null;
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addBinaryBody(Constants.ParamFile, fileInputStream, ContentType.DEFAULT_BINARY, fileName);
            builder.addTextBody(Constants.ParamDescription, "cms", ContentType.TEXT_PLAIN);
            builder.addTextBody(Constants.ParamNewsId, newsId, ContentType.TEXT_PLAIN);
            HttpEntity multipartForm = builder.build();

            post = new HttpPost(this.newsUploadUrl);
            post.setEntity(multipartForm);

            HttpResponse response = client.execute(post);
            HttpEntity body = response.getEntity();

            UploadResult result = mapper.readValue(body.getContent(), UploadResult.class);
            logger.debug("url:" + result.getUrl());

            uploadResult.setErrorMsg(result.getErrorMsg());
            uploadResult.setStatus(result.getStatus());
            uploadResult.setUrl(result.getUrl());
        } catch (Exception e){
            String errorMsg = "Exception:" + e.getMessage();
            logger.error(errorMsg);

            uploadResult.setStatus(APIStatus.FAIL);
            uploadResult.setErrorMsg(e.getMessage());
        } finally {
            try{
                post.releaseConnection();
            } catch (Exception e) {
                logger.warn("Exception:" + e.getMessage());
            }finally {
                post = null;
            }
            return uploadResult;
        }
    }

    public UploadResult doUpload(InputStream fileInputStream, String fileName, String toPath, String toSection){
        UploadResult result = new UploadResult();

        HttpPost post = null;
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addBinaryBody(Constants.ParamFile, fileInputStream, ContentType.DEFAULT_BINARY, fileName);
            builder.addTextBody(Constants.ParamDescription, "cms", ContentType.TEXT_PLAIN);
            builder.addTextBody(Constants.ParamToPath, toPath, ContentType.TEXT_PLAIN);
            builder.addTextBody(Constants.ParamToSection, toSection, ContentType.TEXT_PLAIN);

            HttpEntity multipartForm = builder.build();

            post = new HttpPost(this.uploadUrl);
            post.setEntity(multipartForm);

            HttpResponse response = client.execute(post);
            HttpEntity body = response.getEntity();

            UploadResult uploadResult = mapper.readValue(body.getContent(), UploadResult.class);
            logger.debug("url:" + uploadResult.getUrl());

            result.setErrorMsg(uploadResult.getErrorMsg());
            result.setStatus(uploadResult.getStatus());
            result.setUrl(uploadResult.getUrl());
        } catch (Exception e){
            String errorMsg = "Exception:" + e.getMessage();
            logger.error(errorMsg);

            result.setStatus(APIStatus.FAIL);
            result.setErrorMsg(errorMsg);
        } finally {
            try{
                post.releaseConnection();
            } catch (Exception e) {
                logger.warn("Exception:" + e.getMessage());
            }finally {
                post = null;
            }
            return result;
        }
    }

    public UploadResult doDomainUpload(InputStream fileInputStream, String fileName,
                                       String toPath, String toSection, Domain domain){
        UploadResult result = new UploadResult();

        HttpPost post = null;
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addBinaryBody(Constants.ParamFile, fileInputStream, ContentType.DEFAULT_BINARY, fileName);
            builder.addTextBody(Constants.ParamDescription, "cms", ContentType.TEXT_PLAIN);
            builder.addTextBody(Constants.ParamToPath, toPath, ContentType.TEXT_PLAIN);
            builder.addTextBody(Constants.ParamToSection, toSection, ContentType.TEXT_PLAIN);
            builder.addTextBody(Constants.ParamDomain, domain.name(), ContentType.TEXT_PLAIN);

            HttpEntity multipartForm = builder.build();

            post = new HttpPost(this.domainUploadUrl);
            post.setEntity(multipartForm);

            HttpResponse response = client.execute(post);
            HttpEntity body = response.getEntity();

            UploadResult uploadResult = mapper.readValue(body.getContent(), UploadResult.class);
            logger.debug("url:" + uploadResult.getUrl());

            result.setErrorMsg(uploadResult.getErrorMsg());
            result.setStatus(uploadResult.getStatus());
            result.setUrl(uploadResult.getUrl());
        } catch (Exception e){
            String errorMsg = "Exception:" + e.getMessage();
            logger.error(errorMsg);

            result.setStatus(APIStatus.FAIL);
            result.setErrorMsg(errorMsg);
        } finally {
            try{
                post.releaseConnection();
            } catch (Exception e) {
                logger.warn("Exception:" + e.getMessage());
            }finally {
                post = null;
            }
            return result;
        }
    }

    public PublishResult doNewsPublish(String newsId){
        PublishResult publishResult = new PublishResult();

        HttpGet get = null;
        try {
            String newsPubUrl = this.newsPublishUrl + "/" + newsId;

            get = new HttpGet(newsPubUrl);
            HttpEntity body = get(get);
            PublishResult result = mapper.readValue(body.getContent(), PublishResult.class);

            publishResult.setStatus(result.getStatus());
            publishResult.setErrorMsg(result.getErrorMsg());
        } catch (Exception e){
            String errorMsg = "Exception:" + e.getMessage();
            logger.error(errorMsg);

            publishResult.setStatus(APIStatus.FAIL);
            publishResult.setErrorMsg(errorMsg);
        } finally {
            try{
                get.releaseConnection();
            } catch (Exception e) {
                logger.warn("Exception:" + e.getMessage());
            }finally {
                get = null;
            }
            return publishResult;
        }
    }

    private HttpEntity get(HttpGet get) throws IOException {
        HttpResponse response = client.execute(get);
        return response.getEntity();
    }

    private <T> HttpEntity post(HttpPost post, T parameters) throws IOException {
        String json = mapper.writeValueAsString(parameters);

        StringEntity request = new StringEntity(json, StandardCharsets.UTF_8);
        request.setContentType(ContentType.APPLICATION_JSON.toString());

        post.setEntity(request);

        HttpResponse response = client.execute(post);
        return response.getEntity();
    }
}
