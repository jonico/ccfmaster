package com.collabnet.ccf.ccfmaster.controller;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;
import com.collabnet.ccf.ccfmaster.controller.api.AbstractApiLinkIdController;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.teamforge.api.Connection;

@Controller
abstract class AbstractLinkIdController {
	static final String DUMMY_REQUEST_MAPPING = "/prevent/roo/from/regenerating/this/controller";
	
	private static final Logger log = LoggerFactory.getLogger(AbstractLinkIdController.class);
	
	//@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
	
	/**
	 * NOTE: this *must* match the value in *.tagx under WEB-INF
	 */
	public static final String EXTERNAL_APP_MODELATTRIBUTE_NAME = AbstractApiLinkIdController.EXTERNAL_APP_MODELATTRIBUTE_NAME;

	
	@ExceptionHandler({
		AccessDeniedException.class,
		RemoteException.class
		})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	void handleAccessDenied() {
		// do nothing; Response status is set by @ResponseStatus annotation.
	}
	
	@PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN') or (principal.linkId == #linkId))")
	@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME)
	ExternalApp populateLinkId(@PathVariable("linkId") String linkId) throws RemoteException {
		List<ExternalApp> externalApps = ExternalApp.findExternalAppsByLinkIdEquals(linkId).getResultList();
		if (externalApps.isEmpty()) {
			// we need to create the ExternalApp on the fly because we don't get
			// the IAF callbacks from TeamForge.
			Object user = securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
			Assert.isInstanceOf(TFUserDetails.class, user, "Cannot auto-create ExternalApps unless logged in via TeamForge.");
			final Connection connection = ((TFUserDetails) user).getConnection();
			return ExternalApp.createNewExternalApp(linkId, connection);
			
		} else {
			return externalApps.get(0);
		}
	}


	
	/**
	 * 
	 * @param repositoryMapping
	 * @param ea
	 * @throws AccessDeniedException if ea != repositoryMapping.externalApp
	 */
	static void validateRepositoryMapping(RepositoryMapping repositoryMapping, ExternalApp ea) {
		Assert.notNull(ea);
		if (!ea.equals(repositoryMapping.getExternalApp())) {
        	throw new AccessDeniedException(String.format("Repository Mapping %d is not part of External App %s.",repositoryMapping.getId(), ea.getLinkId()));
        }
	}
	
	static <T> ResponseEntity<T> responseEntity(String locationUri, HttpStatus status) {
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(locationUri));
		return new ResponseEntity<T>(headers, status);
	}

	/*
	 * Dummy controller methods to prevent Roo from generating ambiguous
	 * controllers.
	 * 
	 * NOTE: create and update methods need to be stubbed in each controller
	 * because Roo ignores generics. See
	 * http://forum.springsource.org/showthread.php?t=101829 for details.
	 */

	String show(@PathVariable("id") Long id, Model model) {
		throw new UnsupportedOperationException();
	}

	String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
		throw new UnsupportedOperationException();
	}

	String updateForm(@PathVariable("id") Long id, Model model) {
		throw new UnsupportedOperationException();
	}

	String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
		throw new UnsupportedOperationException();
	}

	String createForm(Model model) {
		throw new UnsupportedOperationException();
	}

	Collection<ExternalApp> populateExternalApps() {
		throw new UnsupportedOperationException();
	}

}
