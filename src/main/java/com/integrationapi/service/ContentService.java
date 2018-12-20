package com.integrationapi.service;

import java.io.IOException;

import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {
    @Autowired
    private IntegrationService integrationService;

    public String getContents(String fromPath) {
        try {
            return integrationService.getContents(fromPath);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String getDownloadLink(String fromPath) {
        return integrationService.downloadContent(fromPath);
    }
}
