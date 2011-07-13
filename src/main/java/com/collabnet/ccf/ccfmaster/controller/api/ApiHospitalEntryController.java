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

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.HOSPITALY_ENTRY)
public class ApiHospitalEntryController extends AbstractApiController<HospitalEntry> {
	
	private static final Logger log = LoggerFactory.getLogger(ApiHospitalEntryController.class);

	@Override
	public @ResponseBody HospitalEntry create(@RequestBody HospitalEntry requestBody, HttpServletResponse response) {
		requestBody.persist();
		setLocationHeader(response, Paths.HOSPITALY_ENTRY + "/" + requestBody.getId());
		return requestBody;
	}
	
	@Override
	public @ResponseBody HospitalEntryList list() {
		return new HospitalEntryList(HospitalEntry.findAllHospitalEntrys());
	}
	
	@Override
	public @ResponseBody HospitalEntry show(@PathVariable("id") HospitalEntry id) {
		return super.show(id);
	}
	
	@Override
	public void update(@PathVariable("id") Long id, @RequestBody HospitalEntry requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		requestBody.merge();
	}


	@Override
	public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
		HospitalEntry.findHospitalEntry(id).remove();
	}
	
	@RequestMapping(value = "/{direction}/")
	public @ResponseBody HospitalEntryList hospitalEntrys(@PathVariable("direction") Directions direction) {
		return new HospitalEntryList(HospitalEntry.findHospitalEntrysByDirection(direction).getResultList());
	}

	private void validateRequestBody(Long id, HospitalEntry requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != requestBody.id (%s)", id, requestBody.getId()));
		}
	}
}
