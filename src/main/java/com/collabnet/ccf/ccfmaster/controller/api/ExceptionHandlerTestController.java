package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/api/test")
public class ExceptionHandlerTestController {
	private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerTestController.class);
	
	@ExceptionHandler(value = BadRequestException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public void exceptionHandler(BadRequestException ex, HttpServletResponse response) {
		log.error("caught exception", ex);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String get() {
		log.info("in get()");
		throw new BadRequestException("catch me if you can!");
	}
}
