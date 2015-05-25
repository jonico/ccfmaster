package com.collabnet.ccf.ccfmaster.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingUtil {

    private static final Logger log = LoggerFactory
                                            .getLogger(LoggingUtil.class);

    @After("within(com.collabnet.ccf.ccfmaster.controller.web..*) && within(com.collabnet.ccf.ccfmaster..*..*)")
    public void afterLoggingAdvice(JoinPoint joinPoint) {
        log.debug("After method " + joinPoint.getSignature().toString()
                + " invoked");
    }

    @Before("within(com.collabnet.ccf.ccfmaster.controller.web..*) && within(com.collabnet.ccf.ccfmaster..*..*)")
    public void beforeLoggingAdvice(JoinPoint joinPoint) {
        log.debug("On method " + joinPoint.getSignature().toString()
                + " invoke");
    }

}
