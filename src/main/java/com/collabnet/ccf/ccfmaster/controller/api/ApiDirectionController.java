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
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfigList;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionList;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;

@Controller
@Scope("request")
@RequestMapping(value = Paths.DIRECTION)
public class ApiDirectionController extends AbstractApiController<Direction> {
	
	private static final Logger log = LoggerFactory.getLogger(ApiDirectionController.class);

	@Override
	public @ResponseBody Direction create(@RequestBody Direction requestBody, HttpServletResponse response) {
		requestBody.persist();
		setLocationHeader(response, Paths.DIRECTION + "/" + requestBody.getId());
		return requestBody;
	}
	
	@Override
	public @ResponseBody DirectionList list() {
		return new DirectionList(Direction.findAllDirections());
	}
	
	@Override
	public @ResponseBody Direction show(@PathVariable("id") Direction id) {
		return super.show(id);
	}
	
	@RequestMapping(value = "/{id}/directionconfigs", method=RequestMethod.GET)
	public @ResponseBody DirectionConfigList directionConfigs(@PathVariable("id") Direction direction) {
		return new DirectionConfigList(DirectionConfig.findDirectionConfigsByDirection(direction).getResultList());
	}

	@Override
	public void update(@PathVariable("id") Long id, @RequestBody Direction requestBody, HttpServletResponse response) {
		validateRequestBody(id, requestBody);
		requestBody.merge();
	}

	private void validateRequestBody(Long id, Direction requestBody) {
		if (id == null || !id.equals(requestBody.getId())) {
			throw new BadRequestException(String.format("id (%s) != requestBody.id (%s)", id, requestBody.getId()));
		}
	}

	@Override
	public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
		Direction.findDirection(id).remove();
	}
	
	@RequestMapping(value = "/{direction}/")
	public @ResponseBody DirectionList repositoryMappingDirections(@PathVariable("direction") Directions direction) {
		return new DirectionList(Direction.findDirectionsByDirection(direction).getResultList());
	}
}
