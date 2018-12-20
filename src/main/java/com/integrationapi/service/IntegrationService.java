package com.integrationapi.service;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.integrationapi.config.ApplicationProperties;

@Service
public class IntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(IntegrationService.class);

    @Autowired
    private IntegrationHttpClient integrationHttpClient;

    @Autowired
    ApplicationProperties applicationProperties;

    public String getContents(String fromPath) throws Exception {

        String httpUrl = applicationProperties.getEndpoint() + "folders/contents?path=";
        if (!StringUtils.isEmpty(fromPath)) {
            httpUrl = httpUrl + fromPath;
        } else {
            httpUrl = httpUrl + "/";
        }

        StringJoiner stringJoiner = new StringJoiner(",");
        stringJoiner.add(applicationProperties.getUser());
        stringJoiner.add(applicationProperties.getOrg());
        stringJoiner.add(applicationProperties.getGoogle());

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", stringJoiner.toString());
        return integrationHttpClient.doGet(header, httpUrl);

    }

    public String uploadContent(String toPath, MultipartFile multipartFile) {

        String httpUrl = applicationProperties.getEndpoint() + "files?path=/";
        if (!StringUtils.isEmpty(toPath)) {
            httpUrl = httpUrl + toPath;
        }

        String fileName = multipartFile.getOriginalFilename();

        httpUrl = httpUrl + "/" + fileName;

        StringJoiner stringJoiner = new StringJoiner(",");
        stringJoiner.add(applicationProperties.getUser());
        stringJoiner.add(applicationProperties.getOrg());
        stringJoiner.add(applicationProperties.getGoogle());

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", stringJoiner.toString());
        String response = integrationHttpClient.doPostMultiPart(multipartFile, headers, httpUrl);
        logger.info("response {} ", response);
        return response;
        
    }

    public String downloadContent(String fromPath) {

        String httpUrl = applicationProperties.getEndpoint() + "files/links?path=";
        if (!StringUtils.isEmpty(fromPath)) {
            httpUrl = httpUrl + fromPath;
        } else {
            httpUrl = httpUrl + "/";
        }

        StringJoiner stringJoiner = new StringJoiner(",");
        stringJoiner.add(applicationProperties.getUser());
        stringJoiner.add(applicationProperties.getOrg());
        stringJoiner.add(applicationProperties.getGoogle());

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", stringJoiner.toString());
        String response = integrationHttpClient.doGet(headers, httpUrl);
        logger.info("response {} ", response);
        return response;
    }

    public void deleteContent(String fromPath, String fileName) {

    }

}
