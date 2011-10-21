package com.collabnet.ccf.ccfmaster.web.model;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;

public class JsonView {

    public static ModelAndView render(Object model, HttpServletResponse response)
    {
        MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();

        MediaType jsonMimeType = MediaType.APPLICATION_JSON;


        try {
            jsonConverter.write(model, jsonMimeType, new ServletServerHttpResponse(response));
        } catch (HttpMessageNotWritableException e) {
            // we do not want a stack trace in our error log whenever a browser window closes
        	// e.printStackTrace();
        } catch (IOException e) {
        	// we do not want a stack trace in our error log whenever a browser window closes
            //e.printStackTrace();
        }

        return null;
    }
}