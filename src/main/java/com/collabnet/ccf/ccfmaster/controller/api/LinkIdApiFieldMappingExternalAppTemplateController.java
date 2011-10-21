package com.collabnet.ccf.ccfmaster.controller.api;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplateList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.LINKID_FIELD_MAPPING_EXTERNAL_APP_TEMPLATE)
public class LinkIdApiFieldMappingExternalAppTemplateController extends AbstractApiLinkIdController<FieldMappingExternalAppTemplate> {
	public LinkIdApiFieldMappingExternalAppTemplateController() {
		log.debug("creating LIAFieldMappingExternalAppTemplateController");
	}

	@Override
	public @ResponseBody FieldMappingExternalAppTemplate create(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @RequestBody FieldMappingExternalAppTemplate requestBody, HttpServletResponse response) {
		validateFieldMappingExternalAppTemplate(requestBody);
		requestBody.persist();
		setLocationHeader(response, Paths.LINKID_FIELD_MAPPING_EXTERNAL_APP_TEMPLATE.replace("{linkId}", ea.getLinkId()) + "/" + requestBody.getId());
		return requestBody;
	}

	@Override
	public @ResponseBody FieldMappingExternalAppTemplateList list(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
		return new FieldMappingExternalAppTemplateList(FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplatesByParent(ea).getResultList());
	}

	@RequestMapping(value = "/{direction}/", method = GET)
	public @ResponseBody FieldMappingExternalAppTemplateList fieldMappingExternalAppTemplateDirectionScope(@PathVariable("direction") Directions direction, @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
		return new FieldMappingExternalAppTemplateList(FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplatesByParentAndDirection(ea, direction).getResultList());
	}

	@Override
	public @ResponseBody FieldMappingExternalAppTemplate show(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id) {
		FieldMappingExternalAppTemplate rm = FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplate(id);
		validateFieldMappingExternalAppTemplate(rm);
		return rm;
	}

	@Override
	public void update(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, @RequestBody FieldMappingExternalAppTemplate requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		validateFieldMappingExternalAppTemplate(requestBody);
		requestBody.merge();
	}

	@Override
	public void delete(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, HttpServletResponse response) {
		FieldMappingExternalAppTemplate rm = FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplate(id);
		validateFieldMappingExternalAppTemplate(rm);
		rm.remove();
	}
	
	private void validateRequestBody(Long id, FieldMappingExternalAppTemplate requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != requestBody.id (%s)", id, requestBody.getId()));
		}
		// now retrieve original object to figure out whether reparenting attempt took place
		FieldMappingExternalAppTemplate original = FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplate(id);
		if (!original.getParent().equals(requestBody.getParent())) {
			throw new AccessDeniedException("requestBody.parent != original entity's external app.");
		}
	}

	private void validateFieldMappingExternalAppTemplate(FieldMappingExternalAppTemplate requestBody) {
		if (requestBody == null) {
			throw new DataRetrievalFailureException("requested entity not found.");
		}
		if (!externalApp.equals(requestBody.getParent())) {
			throw new AccessDeniedException("requestBody.externalApp != current external app.");
		}
	}

}
