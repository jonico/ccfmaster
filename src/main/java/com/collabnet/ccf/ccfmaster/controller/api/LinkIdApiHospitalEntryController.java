package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.LINKID_HOSPITAL_ENTRY)
public class LinkIdApiHospitalEntryController extends AbstractApiLinkIdController<HospitalEntry> {
	public LinkIdApiHospitalEntryController() {
		log.debug("creating LIARMC");
	}

	@Override
	public @ResponseBody HospitalEntry create(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @RequestBody HospitalEntry requestBody, HttpServletResponse response) {
		validateHospitalEntry(requestBody);
		requestBody.persist();
		setLocationHeader(response, Paths.LINKID_HOSPITAL_ENTRY.replace("{linkId}", ea.getLinkId()) + "/" + requestBody.getId());
		return requestBody;
	}

	@Override
	public @ResponseBody HospitalEntryList list(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
		return new HospitalEntryList(HospitalEntry.findHospitalEntrysByExternalApp(ea).getResultList());
	}

	@Override
	public @ResponseBody HospitalEntry show(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id) {
		HospitalEntry rm = HospitalEntry.findHospitalEntry(id);
		validateHospitalEntry(rm);
		return rm;
	}

	@Override
	public void update(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, @RequestBody HospitalEntry requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		validateHospitalEntry(requestBody);
		requestBody.merge();
	}

	@Override
	public void delete(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, HttpServletResponse response) {
		HospitalEntry rm = HospitalEntry.findHospitalEntry(id);
		validateHospitalEntry(rm);
		rm.remove();
	}
	
	@RequestMapping(value = "/{direction}/", method = RequestMethod.GET)
	public @ResponseBody HospitalEntryList hospitalEntrysDirectionScope(@PathVariable("direction") Directions direction, @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
		return new HospitalEntryList(HospitalEntry.findHospitalEntrysByExternalAppAndDirection(ea, direction).getResultList());
	}

	private void validateRequestBody(Long id, HospitalEntry requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != requestBody.id (%s)", id, requestBody.getId()));
		}
		// now retrieve original object to figure out whether reparenting attempt took place
		HospitalEntry original = HospitalEntry.findHospitalEntry(id);
		if (!original.getRepositoryMappingDirection().getRepositoryMapping().getExternalApp().equals(requestBody.getRepositoryMappingDirection().getRepositoryMapping().getExternalApp())) {
			throw new AccessDeniedException("requestBody.externalApp != original entity's external app.");
		}
	}

	private void validateHospitalEntry(HospitalEntry requestBody) {
		if (requestBody == null) {
			throw new DataRetrievalFailureException("requested entity not found.");
		}
		if (!externalApp.equals(requestBody.getRepositoryMappingDirection().getRepositoryMapping().getExternalApp())) {
			throw new AccessDeniedException("requestBody.externalApp != current external app.");
		}
	}

}
