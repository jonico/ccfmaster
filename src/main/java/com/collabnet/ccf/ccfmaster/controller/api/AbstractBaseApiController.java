package com.collabnet.ccf.ccfmaster.controller.api;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.collabnet.ccf.ccfmaster.util.HttpUtils;

public class AbstractBaseApiController {

	private static final Logger log = LoggerFactory.getLogger(AbstractBaseApiController.class);
	
	@Autowired
	protected HttpServletRequest request;
	protected String contextPath;
	protected String contextUrl;

	
	/**
	 * because of Spring Bug https://jira.springsource.org/browse/SPR-3150 we
	 * provide this default constructor and rely on autowiring instead of
	 * constructor injection to set the request for us.
	 * 
	 * In addition, the @PostConstruct method setupContextUrl is
	 * responsible for setting up instance variables based on the request.
	 * 
	 * Who needs OO design and final fields when we've got DI and frameworks  :-(.
	 */
	public AbstractBaseApiController() {
		
	}

	public AbstractBaseApiController(HttpServletRequest request) {
		this.request = request;
		setupContextUrl();
	}
	
	@PostConstruct
	public void setupContextUrl() {
		log.debug("setupContextUrl called.");
		Assert.notNull(request);
		this.contextPath = request.getContextPath();
		this.contextUrl = HttpUtils.buildContextUrl(request);
	}

	@ExceptionHandler(value = { AccessDeniedException.class })
	@ResponseStatus(FORBIDDEN)
	public void permissionDenied(Exception ex, HttpServletResponse response) throws IOException {
		log.info("handling permission denied.", ex);
		ex.printStackTrace(response.getWriter());
	}

	@ExceptionHandler(value = {
			BadRequestException.class,
			DataAccessException.class,
			ConversionFailedException.class})
	@ResponseStatus(BAD_REQUEST)
	public void badRequest(Exception ex, HttpServletResponse response) throws IOException {
		log.info("handling bad request.", ex);
		ex.printStackTrace(response.getWriter());
	}

	protected void setLocationHeader(HttpServletResponse response, String contextRelativePath) {
		// FIXME: according to RFC1945, this should be an absolute URL.
		response.setHeader("Location", contextUrl + contextRelativePath);
	}

}