package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfigList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.PARTICIPANT_CONFIG)
public class ApiParticipantConfigController extends AbstractApiController<ParticipantConfig> {
	
	@Override
	public @ResponseBody ParticipantConfig create(@RequestBody ParticipantConfig requestBody, HttpServletResponse response) {
		requestBody.persist();
		setLocationHeader(response, Paths.PARTICIPANT_CONFIG + "/" + requestBody.getId());
		return requestBody;
	}
	
	@Override
	public @ResponseBody ParticipantConfigList list() {
		return new ParticipantConfigList(ParticipantConfig.findAllParticipantConfigs());
	}
	
	@Override
	public @ResponseBody ParticipantConfig show(@PathVariable("id") ParticipantConfig id) {
		return super.show(id);
	}
	
	@Override
	public void update(@PathVariable("id") Long id, @RequestBody ParticipantConfig requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		requestBody.merge();
	}


	@Override
	public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
		ParticipantConfig.findParticipantConfig(id).remove();
	}


	private void validateRequestBody(Long id, ParticipantConfig requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != requestBody.id (%s)", id, requestBody.getId()));
		}
	}
}
