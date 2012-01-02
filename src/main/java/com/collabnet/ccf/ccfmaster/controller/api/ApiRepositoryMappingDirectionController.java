package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.REPOSITORY_MAPPING_DIRECTION)
public class ApiRepositoryMappingDirectionController extends AbstractApiController<RepositoryMappingDirection> {
	
	@Override
	public @ResponseBody RepositoryMappingDirection create(@RequestBody RepositoryMappingDirection requestBody, HttpServletResponse response) {
		requestBody.persist();
		setLocationHeader(response, Paths.REPOSITORY_MAPPING_DIRECTION + "/" + requestBody.getId());
		return requestBody;
	}
	
	@Override
	public @ResponseBody RepositoryMappingDirectionList list() {
		return new RepositoryMappingDirectionList(RepositoryMappingDirection.findAllRepositoryMappingDirections());
	}
	
	@Override
	public @ResponseBody RepositoryMappingDirection show(@PathVariable("id") RepositoryMappingDirection id) {
		return super.show(id);
	}
	
	@Override
	public void update(@PathVariable("id") Long id, @RequestBody RepositoryMappingDirection requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		requestBody.merge();
	}

	private void validateRequestBody(Long id, RepositoryMappingDirection requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != requestBody.id (%s)", id, requestBody.getId()));
		}
	}

	@Override
	public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
		RepositoryMappingDirection.findRepositoryMappingDirection(id).remove();
	}
	
	@RequestMapping(value = "/{direction}/")
	public @ResponseBody RepositoryMappingDirectionList repositoryMappingDirections(@PathVariable("direction") Directions direction) {
		return new RepositoryMappingDirectionList(RepositoryMappingDirection.findRepositoryMappingDirectionsByDirection(direction).getResultList());
	}
}
