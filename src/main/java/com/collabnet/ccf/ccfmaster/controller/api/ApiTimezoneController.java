package com.collabnet.ccf.ccfmaster.controller.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import com.collabnet.ccf.ccfmaster.server.domain.TimezoneList;

@Controller
@RequestMapping(Paths.TIMEZONES)
public class ApiTimezoneController {


	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody List<Timezone> list() {
		return new TimezoneList(Arrays.asList(Timezone.values()));
	}
}
