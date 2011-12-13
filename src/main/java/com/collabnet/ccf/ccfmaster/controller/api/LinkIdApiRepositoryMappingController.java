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
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryList;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMappingList;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionList;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.LINKID_REPOSITORY_MAPPING)
public class LinkIdApiRepositoryMappingController extends AbstractApiLinkIdController<RepositoryMapping> {

	@Override
	public @ResponseBody RepositoryMapping create(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @RequestBody RepositoryMapping requestBody, HttpServletResponse response) {
		validateRepositoryMapping(requestBody);
		requestBody.persist();
		setLocationHeader(response, Paths.LINKID_REPOSITORY_MAPPING.replace("{linkId}", ea.getLinkId()) + "/" + requestBody.getId());
		return requestBody;
	}

	@Override
	public @ResponseBody RepositoryMappingList list(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
		return new RepositoryMappingList(RepositoryMapping.findRepositoryMappingsByExternalApp(ea).getResultList());
	}

	@Override
	public @ResponseBody RepositoryMapping show(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id) {
		RepositoryMapping rm = RepositoryMapping.findRepositoryMapping(id);
		validateRepositoryMapping(rm);
		return rm;
	}
	
	@RequestMapping(value = "/{id}/identitymappings", method = GET)
	public @ResponseBody IdentityMappingList showIdentityMappings(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id) {
		RepositoryMapping rm = RepositoryMapping.findRepositoryMapping(id);
		validateRepositoryMapping(rm);
		return new IdentityMappingList(IdentityMapping.findIdentityMappingsByRepositoryMapping(rm).getResultList());
	}
	
	@RequestMapping(value = "/{id}/hospitalentrys", method = GET)
	public @ResponseBody HospitalEntryList showHospitalEntries(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id) {
		RepositoryMapping rm = RepositoryMapping.findRepositoryMapping(id);
		validateRepositoryMapping(rm);
		return new HospitalEntryList(HospitalEntry.findHospitalEntrysByRepositoryMapping(rm).getResultList());
	}
	
	@RequestMapping(value = "/{id}/hospitalentrys/count", method = GET)
	public @ResponseBody String showHospitalEntriesCount(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id) {
		RepositoryMapping rm = RepositoryMapping.findRepositoryMapping(id);
		validateRepositoryMapping(rm);
		return Long.toString(HospitalEntry.countHospitalEntrysByRepositoryMapping(rm));
	}
	
	@RequestMapping(value = "/{id}/repositorymappingdirections", method = GET)
	public @ResponseBody RepositoryMappingDirectionList showRepositoryMappingDirections(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id) {
		RepositoryMapping rm = RepositoryMapping.findRepositoryMapping(id);
		validateRepositoryMapping(rm);
		return new RepositoryMappingDirectionList(RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMapping(rm).getResultList());
	}
	
	@RequestMapping(value = "/{id}/repositorymappingdirections/{direction}", method = GET)
	public @ResponseBody RepositoryMappingDirectionList showRepositoryMappingDirectionsDirectionScope(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, @PathVariable("direction") Directions direction) {
		RepositoryMapping rm = RepositoryMapping.findRepositoryMapping(id);
		validateRepositoryMapping(rm);
		return new RepositoryMappingDirectionList(RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMappingAndDirection(rm, direction).getResultList());
	}
	
	@RequestMapping(value = "/{id}/hospitalentrys/{direction}", method = GET)
	public @ResponseBody HospitalEntryList showHospitalEntriesDirectionScope(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, @PathVariable("direction") Directions direction) {
		RepositoryMapping rm = RepositoryMapping.findRepositoryMapping(id);
		validateRepositoryMapping(rm);
		return new HospitalEntryList(HospitalEntry.findHospitalEntrysByRepositoryMappingAndDirection(rm, direction).getResultList());
	}
	
	@RequestMapping(value = "/{id}/hospitalentrys/{direction}/count", method = GET)
	public @ResponseBody String showHospitalEntriesDirectionScopeCount(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, @PathVariable("direction") Directions direction) {
		RepositoryMapping rm = RepositoryMapping.findRepositoryMapping(id);
		validateRepositoryMapping(rm);
		return Long.toString(HospitalEntry.countHospitalEntrysByRepositoryMappingAndDirection(rm, direction));
	}

	@Override
	public void update(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, @RequestBody RepositoryMapping requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		validateRepositoryMapping(requestBody);
		requestBody.merge();
	}

	@Override
	public void delete(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, HttpServletResponse response) {
		RepositoryMapping rm = RepositoryMapping.findRepositoryMapping(id);
		validateRepositoryMapping(rm);
		rm.remove();
	}

	private void validateRequestBody(Long id, RepositoryMapping requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != requestBody.id (%s)", id, requestBody.getId()));
		}
		// now retrieve original object to figure out whether reparenting attempt took place
		RepositoryMapping original = RepositoryMapping.findRepositoryMapping(id);
		if (!original.getExternalApp().equals(requestBody.getExternalApp())) {
			throw new AccessDeniedException("requestBody.externalApp != original entity's external app.");
		}
	}

	private void validateRepositoryMapping(RepositoryMapping requestBody) {
		if (requestBody == null) {
			throw new DataRetrievalFailureException("requested entity not found.");
		}
		if (!externalApp.equals(requestBody.getExternalApp())) {
			throw new AccessDeniedException("requestBody.externalApp != current external app.");
		}
	}

}
