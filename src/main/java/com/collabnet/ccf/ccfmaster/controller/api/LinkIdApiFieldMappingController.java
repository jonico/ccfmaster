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
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.LINKID_FIELD_MAPPING)
public class LinkIdApiFieldMappingController extends AbstractApiLinkIdController<FieldMapping> {
	public LinkIdApiFieldMappingController() {
		log.debug("creating LIAFieldMappingController");
	}

	@Override
	public @ResponseBody FieldMapping create(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @RequestBody FieldMapping requestBody, HttpServletResponse response) {
		validateFieldMapping(requestBody);
		requestBody.persist();
		setLocationHeader(response, Paths.LINKID_FIELD_MAPPING.replace("{linkId}", ea.getLinkId()) + "/" + requestBody.getId());
		return requestBody;
	}

	@Override
	public @ResponseBody FieldMappingList list(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
		return new FieldMappingList(FieldMapping.findFieldMappingsByExternalApp(ea).getResultList());
	}

	@Override
	public @ResponseBody FieldMapping show(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id) {
		FieldMapping rm = FieldMapping.findFieldMapping(id);
		validateFieldMapping(rm);
		return rm;
	}

	@Override
	public void update(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, @RequestBody FieldMapping requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		validateFieldMapping(requestBody);
		requestBody.merge();
	}

	@Override
	public void delete(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, HttpServletResponse response) {
		FieldMapping rm = FieldMapping.findFieldMapping(id);
		validateFieldMapping(rm);
		rm.remove();
	}
	
	private void validateRequestBody(Long id, FieldMapping requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != requestBody.id (%s)", id, requestBody.getId()));
		}
		// now retrieve original object to figure out whether reparenting attempt took place
		FieldMapping original = FieldMapping.findFieldMapping(id);
		if (!original.getParent().getRepositoryMapping().getExternalApp().equals(requestBody.getParent().getRepositoryMapping().getExternalApp())) {
			throw new AccessDeniedException("requestBody.externalApp != original entity's external app.");
		}
	}

	private void validateFieldMapping(FieldMapping requestBody) {
		if (requestBody == null) {
			throw new DataRetrievalFailureException("requested entity not found.");
		}
		if (!externalApp.equals(requestBody.getParent().getRepositoryMapping().getExternalApp())) {
			throw new AccessDeniedException("requestBody.externalApp != current external app.");
		}
	}

}
