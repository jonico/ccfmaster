package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfigList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.DIRECTION_CONFIG)
public class ApiDirectionConfigController extends AbstractApiController<DirectionConfig> {
	
	@Override
	public @ResponseBody DirectionConfig create(@RequestBody DirectionConfig requestBody, HttpServletResponse response) {
		requestBody.persist();
		setLocationHeader(response, Paths.DIRECTION_CONFIG + "/" + requestBody.getId());
		return requestBody;
	}
	
	@Override
	public @ResponseBody DirectionConfigList list() {
		return new DirectionConfigList(DirectionConfig.findAllDirectionConfigs());
	}
	
	@Override
	public @ResponseBody DirectionConfig show(@PathVariable("id") DirectionConfig id) {
		return super.show(id);
	}
	
	@Override
	public void update(@PathVariable("id") Long id, @RequestBody DirectionConfig requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		requestBody.merge();
	}


	@Override
	public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
		DirectionConfig.findDirectionConfig(id).remove();
	}


	private void validateRequestBody(Long id, DirectionConfig requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != requestBody.id (%s)", id, requestBody.getId()));
		}
	}
}
