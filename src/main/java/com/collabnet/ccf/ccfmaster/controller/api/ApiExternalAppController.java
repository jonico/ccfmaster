package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.EXTERNAL_APP)
public class ApiExternalAppController extends AbstractApiController<ExternalApp> {
	
	private static final Logger log = LoggerFactory.getLogger(ApiExternalAppController.class);

	@Override
	public @ResponseBody ExternalApp create(@RequestBody ExternalApp requestBody, HttpServletResponse response) {
		requestBody.persist();
		setLocationHeader(response, Paths.EXTERNAL_APP + "/" + requestBody.getId());
		return requestBody;
	}
	
	@Override
	public @ResponseBody ExternalAppList list() {
		return new ExternalAppList(ExternalApp.findAllExternalApps());
	}
	
	@Override
	public @ResponseBody ExternalApp show(@PathVariable("id") ExternalApp id) {
		return super.show(id);
	}
	
	@Override
	public void update(@PathVariable("id") Long id, @RequestBody ExternalApp requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		requestBody.merge();
	}

	private void validateRequestBody(Long id, ExternalApp requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != requestBody.id (%s)", id, requestBody.getId()));
		}
	}

	@Override
	public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
		ExternalApp.findExternalApp(id).remove();
	}
}
