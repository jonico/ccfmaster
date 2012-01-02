package com.collabnet.ccf.ccfmaster.controller.api;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.rmi.RemoteException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.teamforge.api.Connection;

public abstract class AbstractApiLinkIdController<T> extends AbstractBaseApiController {

	/**
	 * NOTE: this *must* match the value in *.tagx under WEB-INF
	 */
	public static final String EXTERNAL_APP_MODELATTRIBUTE_NAME = "currentExternalApp";
	public static final Logger log = LoggerFactory.getLogger(AbstractApiLinkIdController.class);
	protected ExternalApp externalApp;
	SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

	@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME)
	@PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN') or (principal.linkId == #linkId))")
	public ExternalApp populateExternalApp(@PathVariable("linkId") String linkId) throws RemoteException {
		log.debug("called populateExternalApp");
		ExternalApp externalApp = null;
		List<ExternalApp> externalApps = ExternalApp.findExternalAppsByLinkIdEquals(linkId).getResultList();
		if (externalApps.isEmpty()) {
			// we need to create the ExternalApp on the fly because we don't get
			// the IAF callbacks from TeamForge.
			Object user = securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
			Assert.isInstanceOf(TFUserDetails.class, user, "Cannot auto-create ExternalApps unless logged in via TeamForge.");
			final Connection connection = ((TFUserDetails) user).getConnection();
			externalApp = ExternalApp.createNewExternalApp(linkId, connection);
			
		} else {
			externalApp =  externalApps.get(0);
		}
		this.externalApp = externalApp;
		return externalApp;
	}
	
	@ResponseStatus(CREATED)
	@RequestMapping(method = POST)
	public abstract @ResponseBody T create(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @RequestBody T requestBody, HttpServletResponse response);
	
	@RequestMapping(method = GET)
	public abstract @ResponseBody List<T> list(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea);

	@RequestMapping(value = "/{id}", method = GET)
	public abstract T show(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id);

	/**
	 * 
	 * @param id
	 * @param requestBody
	 * @param response
	 *            this parameter is necessary to prevent Spring MVC from trying
	 *            to resolve a view. Alternatively, we could return @ResponseBody
	 *            String
	 */
	@RequestMapping(value = "/{id}", method = PUT)
	public abstract void update(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, @RequestBody T requestBody, HttpServletResponse response);
	
	@ResponseStatus(NO_CONTENT)
	@RequestMapping(value = "/{id}", method = DELETE)
	public abstract void delete(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, HttpServletResponse response);
}
