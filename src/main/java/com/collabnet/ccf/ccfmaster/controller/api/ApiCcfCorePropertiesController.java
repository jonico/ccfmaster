package com.collabnet.ccf.ccfmaster.controller.api;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.collabnet.ccf.ccfmaster.config.CoreConfigLoader;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyList;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
@Controller
@Scope("request")
@RequestMapping(value = Paths.CCF_CORE_PROPERTIES)
public class ApiCcfCorePropertiesController extends AbstractBaseApiController{
	
	@Autowired
	private CoreConfigLoader coreConfigLoader;

	@ResponseStatus(CREATED)
	@RequestMapping(method = POST, headers="Accept=application/xml")	
	public @ResponseBody CCFCorePropertyList create(@RequestBody CCFCorePropertyList requestBody, HttpServletResponse response) {
		throw new BadRequestException("create not supported");
	}
	
	@RequestMapping(method = GET, headers="Accept=application/xml")
	public @ResponseBody CCFCorePropertyList list() {
		throw new BadRequestException("list not supported");
	}
	
	@RequestMapping(value = "/{id}", method = GET, headers="Accept=application/xml")
	public @ResponseBody CCFCorePropertyList show(@PathVariable("id")Long id) {
		CCFCorePropertyList properties = new CCFCorePropertyList();
		try {
			Direction direction = getValidateDirection(id);
			if(direction == null){ 
				throw new DataRetrievalFailureException("For given id, Direction does not exist");
			}
			List<CCFCoreProperty> propertyList  = coreConfigLoader.getDefaultCCFCorePropertyList(direction);
			properties.setCcfCoreProperties(propertyList);
		} catch (JAXBException e) {
			throw new DataRetrievalFailureException("Could not parse the ccfcoredefaultconfig xml file: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new DataRetrievalFailureException("Could not read the ccfcoredefaultconfig xml file: " + e.getMessage(), e);
		}
		return properties;
	}
	
	@RequestMapping(value = "/{id}", method = PUT)	
	public void update(@PathVariable("id") Long id, @RequestBody CCFCorePropertyList requestBody, HttpServletResponse response) {
		throw new BadRequestException("update not supported.");
	}

	@ResponseStatus(NO_CONTENT)
	@RequestMapping(value = "/{id}", method = DELETE)
	public void delete(@PathVariable("id")Long id, HttpServletResponse response) {
		throw new BadRequestException("delete not supported.");
	}
	
	private Direction getValidateDirection(Long id) {
		if (id == null) {
			throw new DataRetrievalFailureException("Invalid id");
		}
		return Direction.findDirection(id);
	}
}
