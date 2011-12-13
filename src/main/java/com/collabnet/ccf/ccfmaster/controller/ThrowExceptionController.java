package com.collabnet.ccf.ccfmaster.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.authentication.TFSessionExpiredException;

@Controller
@RequestMapping("/chuck")
public class ThrowExceptionController {
	
	@RequestMapping("/")
	public String chuck(@RequestParam String className, @RequestParam(required=false) String message) throws Exception {
		Class<?> cls = Class.forName(className);
		if (Exception.class.isAssignableFrom(cls)) {
			if (message == null) {
				throw (Exception) cls.getConstructor().newInstance();
			}
			else {
				throw (Exception) cls.getConstructor(String.class).newInstance(message);
			}
		}
			
		return null;
	}
	
	@RequestMapping("/timeout")
	public String timeout() {
		throw new TFSessionExpiredException("timeout");
	}
	
	@RequestMapping("/killSession")
	public String killSession(HttpSession session) {
		session.invalidate();
		return "redirect:/project/";
	}
}
