package com.collabnet.ccf.ccfmaster.rest;

import javax.servlet.ServletException;

import org.junit.BeforeClass;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.DispatcherServlet;

@ContextConfiguration(locations = {
        "classpath*:/META-INF/spring/applicationContext.xml",
        "classpath*:/META-INF/spring/applicationContext-test-contentresolver.xml",
        "classpath*:/META-INF/spring/applicationContext-test-ccfruntimeproperties.xml" })
public abstract class AbstractAPIIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    public static String               ccfAPIUrl = "http://localhost:9090/CCFMaster/api";

    protected static RestTemplate      restTemplate;
    protected static DispatcherServlet servlet;

    @BeforeClass
    public static void initServlet() throws ServletException {
        servlet = new DispatcherServlet();
        servlet.setContextConfigLocation("classpath*:/WEB-INF/spring/webmvc-config-test.xml");
        servlet.init(new MockServletConfig());
        restTemplate = new RestTemplate(
                new MockServletClientHttpRequestFactory(servlet, "CCFMaster"));
    }
}
