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
import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingList;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryList;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.LINKID_REPOSITORY_MAPPING_DIRECTION)
public class LinkIdApiRepositoryMappingDirectionController extends AbstractApiLinkIdController<RepositoryMappingDirection> {
	public LinkIdApiRepositoryMappingDirectionController() {
		log.debug("creating LIARMDC");
	}

	@Override
	public @ResponseBody RepositoryMappingDirection create(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @RequestBody RepositoryMappingDirection requestBody, HttpServletResponse response) {
		validateRepositoryMappingDirection(requestBody);
		requestBody.persist();
		setLocationHeader(response, Paths.LINKID_REPOSITORY_MAPPING_DIRECTION.replace("{linkId}", ea.getLinkId()) + "/" + requestBody.getId());
		return requestBody;
	}

	@Override
	public @ResponseBody RepositoryMappingDirectionList list(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
		return new RepositoryMappingDirectionList(RepositoryMappingDirection.findRepositoryMappingDirectionsByExternalApp(ea).getResultList());
	}

	@Override
	public @ResponseBody RepositoryMappingDirection show(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id) {
		RepositoryMappingDirection rm = RepositoryMappingDirection.findRepositoryMappingDirection(id);
		validateRepositoryMappingDirection(rm);
		return rm;
	}

	@Override
	public void update(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, @RequestBody RepositoryMappingDirection requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		validateRepositoryMappingDirection(requestBody);
		requestBody.merge();
	}

	@Override
	public void delete(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, HttpServletResponse response) {
		RepositoryMappingDirection rm = RepositoryMappingDirection.findRepositoryMappingDirection(id);
		validateRepositoryMappingDirection(rm);
		rm.remove();
	}
	
	@RequestMapping(value = "/{direction}/", method = GET)
	public @ResponseBody RepositoryMappingDirectionList repositoryMappingDirectionDirectionScope(@PathVariable("direction") Directions direction, @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
		return new RepositoryMappingDirectionList(RepositoryMappingDirection.findRepositoryMappingDirectionsByExternalAppAndDirection(ea, direction).getResultList());
	}
	
	@RequestMapping(value = "/{id}/hospitalentrys", method = GET)
	public @ResponseBody HospitalEntryList showHospitalEntries(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id) {
		RepositoryMappingDirection rmd = RepositoryMappingDirection.findRepositoryMappingDirection(id);
		validateRepositoryMappingDirection(rmd);
		return new HospitalEntryList(HospitalEntry.findHospitalEntrysByRepositoryMappingDirection(rmd).getResultList());
	}

	@RequestMapping(value = "/{id}/fieldmappings", method = GET)
	public @ResponseBody FieldMappingList showFieldMappings(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id) {
		RepositoryMappingDirection rmd = RepositoryMappingDirection.findRepositoryMappingDirection(id);
		validateRepositoryMappingDirection(rmd);
		return new FieldMappingList(FieldMapping.findFieldMappingsByParent(rmd).getResultList());
	}

	private void validateRequestBody(Long id, RepositoryMappingDirection requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != requestBody.id (%s)", id, requestBody.getId()));
		}
		// now retrieve original object to figure out whether reparenting attempt took place
		RepositoryMappingDirection original = RepositoryMappingDirection.findRepositoryMappingDirection(id);
		if (!original.getRepositoryMapping().getId().equals(requestBody.getRepositoryMapping().getId())) {
			throw new AccessDeniedException("requestBody.repositoryMapping != original entity's repository mapping.");
		}
	}

	private void validateRepositoryMappingDirection(RepositoryMappingDirection requestBody) {
		if (requestBody == null) {
			throw new DataRetrievalFailureException("requested entity not found.");
		}
		if (!externalApp.equals(requestBody.getRepositoryMapping().getExternalApp())) {
			throw new AccessDeniedException("requestBody.externalApp != current external app.");
		}
	}

}
