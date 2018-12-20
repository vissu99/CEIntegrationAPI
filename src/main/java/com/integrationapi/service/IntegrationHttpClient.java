package com.integrationapi.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class IntegrationHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(IntegrationHttpClient.class);


    public CloseableHttpResponse doPut(Map<String, String> headers, String httpUrl, String body) {
        CloseableHttpResponse response = null;
        String url = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {

            //add query param
            url = addQueryParamFromThreadContext(httpUrl);

            HttpPut httpPut = new HttpPut(url);
            for (Entry<String, String> entry : headers.entrySet()) {
                httpPut.setHeader(entry.getKey(), entry.getValue());
            }
            StringEntity entity = new StringEntity(body);
            httpPut.setEntity(entity);
            response = httpClient.execute(httpPut, HttpClientContext.create());
        } catch (java.net.SocketTimeoutException e) {
            logger.error("SocketTimeoutException when connecting to url {} ", e, url);

        } catch (org.apache.http.conn.ConnectTimeoutException e) {
            logger.error("Connection Timeout when trying to reach url {} ", e, url);

        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException when connecting to url {} ", e, url);
        } catch (IOException e) {
            logger.error("IOException when connecting to url {} ", e, url);

        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
                logger.warn("Error when closing the httpClient connection to URL.", e);
            }
        }
        return response;
    }

    public String doPost(Map<String, String> headers, String httpUrl, String body) {
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = null;
        String response = null;

        try {

            //add query param
            url = addQueryParamFromThreadContext(httpUrl);

            HttpPost httpPost = new HttpPost(url);
            if (headers != null) {
                headers.entrySet().forEach(header -> {
                    httpPost.setHeader(header.getKey(), header.getValue());
                });
            }
            if (body != null) {
                StringEntity entity = new StringEntity(body);
                httpPost.setEntity(entity);
            }
            httpResponse = httpClient.execute(httpPost, HttpClientContext.create());
            response = EntityUtils.toString(httpResponse.getEntity());
        } catch (java.net.SocketTimeoutException e) {
            logger.error("SocketTimeoutException when connecting to url {} ", e, url);

        } catch (org.apache.http.conn.ConnectTimeoutException e) {
            logger.error("Connection Timeout when trying to reach url {} ", e, url);

        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException when connecting to url {} ", e, url);
        } catch (IOException e) {
            logger.error("IOException when connecting to url {} ", e, url);

        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
                logger.warn("Error when closing the httpClient connection to URL.", e);
            }
        }

        return response;
    }

    public String doPostMultiPart(MultipartFile multipartFile, Map<String, String> headers, String httpUrl) {
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = null;
        String response = null;
        
        try {

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            // This attaches the file to the POST:
            builder.addBinaryBody("file",
                                  multipartFile.getInputStream(),
                                  ContentType.MULTIPART_FORM_DATA,
                                  multipartFile.getOriginalFilename());
            logger.info("input stream {}, {}", multipartFile.getInputStream(), multipartFile.getSize());
            logger.info(multipartFile.getOriginalFilename());

            HttpEntity multipartEntity = builder.build();

            //add query param
            url = addQueryParamFromThreadContext(httpUrl);
            logger.info("url {}", url);

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(multipartEntity);
            if (headers != null) {
                headers.entrySet().forEach(header -> {
                    httpPost.setHeader(header.getKey(), header.getValue());
                });
            }
            httpPost.setHeader("Content-Type","multipart/form-data");

            logger.info("multipart post entity {}", httpPost);
            Arrays.asList(httpPost.getAllHeaders()).stream().forEach(System.out::println);

            httpResponse = httpClient.execute(httpPost, HttpClientContext.create());
            logger.info("httpResponse {}", httpResponse);
            response = EntityUtils.toString(httpResponse.getEntity());
        } catch (java.net.SocketTimeoutException e) {
            logger.error("SocketTimeoutException when connecting to url {} ", e, url);

        } catch (org.apache.http.conn.ConnectTimeoutException e) {
            logger.error("Connection Timeout when trying to reach url {} ", e, url);

        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException when connecting to url {} ", e, url);
        } catch (IOException e) {
            logger.error("IOException when connecting to url {} ", e, url);

        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
                logger.warn("Error when closing the httpClient connection to URL.", e);
            }
        }

        return response;
    }

    public String doGet(Map<String, String> headers, String httpUrl) {
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = null;
        String response = null;

        try {

            //add  query param
            url = addQueryParamFromThreadContext(httpUrl);

            HttpGet httpGet = new HttpGet(url);
            if (headers != null) {
                headers.entrySet().forEach(header -> {
                    httpGet.setHeader(header.getKey(), header.getValue());
                });
            }
            httpResponse = httpClient.execute(httpGet, HttpClientContext.create());
            response = EntityUtils.toString(httpResponse.getEntity());
        } catch (SocketTimeoutException e) {
            logger.error("SocketTimeoutException when connecting to url {} ", e, url);

        } catch (org.apache.http.conn.ConnectTimeoutException e) {
            logger.error("Connection Timeout when trying to reach url {} ", e, url);

        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException when connecting to url {} ", e, url);
        } catch (IOException e) {
            logger.error("IOException when connecting to url {} ", e, url);

        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
                logger.warn("Error when closing the httpClient connection to URL.", e);
            }
        }
        return response;
    }


    public CloseableHttpResponse doDelete(Map<String, String> headers, String httpUrl) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = null;

        try {

            //add  query param
            url = addQueryParamFromThreadContext(httpUrl);

            HttpDelete httpDelete = new HttpDelete(url);
            if (headers != null) {
                headers.entrySet().forEach(header -> {
                    httpDelete.setHeader(header.getKey(), header.getValue());
                });
            }
            response = httpClient.execute(httpDelete, HttpClientContext.create());
        } catch (SocketTimeoutException e) {
            logger.error("SocketTimeoutException when connecting to url {} ", e, url);
        } catch (org.apache.http.conn.ConnectTimeoutException e) {
            logger.error("Connection Timeout when trying to reach url {} ", e, url);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException when connecting to url {} ", e, url);
        } catch (IOException e) {
            logger.error("IOException when connecting to url {} ", e, url);

        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
                logger.warn("Error when closing the httpClient connection to URL.", e);
            }
        }
        return response;
    }

    private String addQueryParamFromThreadContext(String httpUrl) {

        if (httpUrl != null && httpUrl.contains("token.oauth")) {
            return httpUrl;
        }

        String url = null;
        try {
            URIBuilder builder = new URIBuilder(httpUrl);

            url = builder.build().toString();

            logger.info("Url with Query Param");

        } catch (URISyntaxException e) {
            logger.error("URISyntaxException when adding thread context headers in  url param", e);
        }
        return url;
    }
}