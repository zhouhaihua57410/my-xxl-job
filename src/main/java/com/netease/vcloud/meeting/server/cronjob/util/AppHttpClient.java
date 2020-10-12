package com.netease.vcloud.meeting.server.cronjob.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;

public class AppHttpClient {
    private static Logger logger = LoggerFactory.getLogger(AppHttpClient.class);

    @Resource
    private HttpClient httpClient;

    @Resource
    private RequestConfig defaultRequestConfig;

    public AppHttpClient() {
    }

    public HttpClient getClient() {
        return httpClient;
    }

    public String execute(HttpUriRequest request) throws IOException {
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(request);
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            }

            logger.error("http execute failed, uri={}, statusCode={}", request.getURI(), statusCode);
            EntityUtils.consumeQuietly(response.getEntity());
            return null;
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    public String execute(HttpUriRequest request, int timeout) throws IOException {
        return execute(RequestBuilder.copy(request).setConfig(
                RequestConfig.copy(defaultRequestConfig).setSocketTimeout(timeout).build()).build());
    }
}
