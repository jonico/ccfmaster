package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionList;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppList;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryList;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMappingList;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfigList;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeList;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionList;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.LANDSCAPE)
public class ApiLandscapeController extends AbstractApiController<Landscape>{
	
	private static final Logger log = LoggerFactory.getLogger(ApiLandscapeController.class);
		
	@Override	
	public @ResponseBody Landscape create(@RequestBody Landscape requestBody, HttpServletResponse response) {
		requestBody.persist();
		setLocationHeader(response, Paths.LANDSCAPE + "/" + requestBody.getId());
		return requestBody;
	}
	
	@Override	
	public @ResponseBody LandscapeList list() {
		return new LandscapeList(Landscape.findAllLandscapes());
	}
	
	@RequestMapping(value = "/{id}/landscapeconfigs", method=RequestMethod.GET)
	public @ResponseBody LandscapeConfigList landscapeConfigs(@PathVariable("id") Landscape landscape) {
		return new LandscapeConfigList(LandscapeConfig.findLandscapeConfigsByLandscape(landscape).getResultList());
	}

	@RequestMapping(value = "/{id}/externalapps", method=RequestMethod.GET)
	public @ResponseBody ExternalAppList externalapps(@PathVariable("id") Landscape landscape) {
		return new ExternalAppList(ExternalApp.findExternalAppsByLandscape(landscape).getResultList());
	}
	
	@RequestMapping(value = "/{id}/repositorymappings", method=RequestMethod.GET)
	public @ResponseBody RepositoryMappingList repositoryMappings(@PathVariable("id") Landscape landscape) {
		return new RepositoryMappingList(RepositoryMapping.findRepositoryMappingsByLandscape(landscape).getResultList());
	}
	
	@RequestMapping(value = "/{id}/repositorymappingdirections", method=RequestMethod.GET)
	public @ResponseBody RepositoryMappingDirectionList repositoryMappingDirections(@PathVariable("id") Landscape landscape) {
		return new RepositoryMappingDirectionList(RepositoryMappingDirection.findRepositoryMappingDirectionsByLandscape(landscape).getResultList());
	}
	
	@RequestMapping(value = "/{id}/repositorymappingdirections/{direction}", method=RequestMethod.GET)
	public @ResponseBody RepositoryMappingDirectionList repositoryMappingDirections(@PathVariable("id") Landscape landscape, @PathVariable("direction") Directions direction) {
		//log.debug("direction: {} landscape: {}", direction, landscape);
		return new RepositoryMappingDirectionList(RepositoryMappingDirection.findRepositoryMappingDirectionsByLandscapeAndDirection(landscape, direction).getResultList());
	}
	
	@RequestMapping(value = "/{id}/hospitalentrys", method=RequestMethod.GET)
	public @ResponseBody HospitalEntryList hospitalEntrys(@PathVariable("id") Landscape landscape) {
		return new HospitalEntryList(HospitalEntry.findHospitalEntrysByLandscape(landscape).getResultList());
	}
	
	@RequestMapping(value = "/{id}/hospitalentrys/count", method=RequestMethod.GET)
	public @ResponseBody String hospitalEntrysCount(@PathVariable("id") Landscape landscape) {
		return Long.toString(HospitalEntry.countHospitalEntrysByLandscape(landscape));
	}
	
	@RequestMapping(value = "/{id}/hospitalentrys/{direction}", method=RequestMethod.GET)
	public @ResponseBody HospitalEntryList hospitalEntrys(@PathVariable("id") Landscape landscape, @PathVariable("direction") Directions direction) {
		//log.debug("direction: {} landscape: {}", direction, landscape);
		return new HospitalEntryList(HospitalEntry.findHospitalEntrysByLandscapeAndDirection(landscape, direction).getResultList());
	}
	
	@RequestMapping(value = "/{id}/hospitalentrys/{direction}/count", method=RequestMethod.GET)
	public @ResponseBody String hospitalEntrysCount(@PathVariable("id") Landscape landscape, @PathVariable("direction") Directions direction) {
		//log.debug("direction: {} landscape: {}", direction, landscape);
		return Long.toString(HospitalEntry.countHospitalEntrysByLandscapeAndDirection(landscape, direction));
	}
	
	@RequestMapping(value = "/{id}/identitymappings", method=RequestMethod.GET)
	public @ResponseBody IdentityMappingList identityMappings(@PathVariable("id") Landscape landscape) {
		return new IdentityMappingList(IdentityMapping.findIdentityMappingsByLandscape(landscape).getResultList());
	}
		
	@RequestMapping(value = "/{id}/directions", method=RequestMethod.GET)
	public @ResponseBody DirectionList directions(@PathVariable("id") Landscape landscape) {
		return new DirectionList(Direction.findDirectionsByLandscapeEquals(landscape).getResultList());
	}
	
	@RequestMapping(value = "/{id}/directions/{direction}", method=RequestMethod.GET)
	public @ResponseBody DirectionList directions(@PathVariable("id") Landscape landscape, @PathVariable("direction") Directions direction) {
		log.debug("direction: {} landscape: {}", direction, landscape);
		return new DirectionList(Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, direction).getResultList());
	}
	
	@Override	
	public @ResponseBody Landscape show(@PathVariable("id") Landscape id) {
		return super.show(id);
	}
	
	@Override	
	public void update(@PathVariable("id") Long id, @RequestBody Landscape requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		requestBody.merge();
	}

	@Override
	public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
		Landscape.findLandscape(id).remove();
	}

	private void validateRequestBody(Long id, Landscape requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != landscape.id (%s)", id, requestBody.getId()));
		}
	}
}
