package com.collabnet.ccf.ccfmaster.controller.web.project;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.collabnet.ccf.ccfmaster.authentication.IafUserDetails;
import com.collabnet.ccf.ccfmaster.controller.api.AbstractApiLinkIdController;
import com.collabnet.ccf.ccfmaster.controller.web.AbstractLandscapeController;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;

/**
 * abstract base class for project-scope controllers. Ensures we're
 * authenticated via IAF and makes the current {@link ExternalApp} available in
 * the model.
 */
public abstract class AbstractProjectController extends AbstractLandscapeController {
	
	protected static final String EXTERNAL_APP_MODEL_ATTRIBUTE = AbstractApiLinkIdController.EXTERNAL_APP_MODELATTRIBUTE_NAME;
	//	@Autowired
	protected SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

	/**
	 * places the current External App in the model under the key
	 * {@link AbstractApiLinkIdController#EXTERNAL_APP_MODELATTRIBUTE_NAME}.
	 * 
	 * @throws AccessDeniedException
	 *             if not authenticated via IAF
	 * @throws RemoteException
	 *             if ExternalApp doesn't exist and creating a new one goes
	 *             wrong.
	 * @return the ExternalApp entity for the current user
	 */
	@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE)
	public ExternalApp currentExternalApp() throws RemoteException {
		final Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
		if (authentication == null)
			throw new AccessDeniedException("Not authenticated at all.");
		Object user = authentication.getPrincipal();
		if (user == null || !(user instanceof IafUserDetails))
			throw new AccessDeniedException("Not logged in via IAF, cannot determine current external app.");
		IafUserDetails iafUser = (IafUserDetails) user;
		String linkId = iafUser.getLinkId();
		List<ExternalApp> externalApps = ExternalApp.findExternalAppsByLinkIdEquals(linkId).getResultList();
		if (externalApps.isEmpty()) {
			return ExternalApp.createNewExternalApp(linkId, iafUser.getConnection());
		} else {
			return externalApps.get(0);
		}
	}
	
	@ModelAttribute("menuItems")
	public List<MenuItem> populateMenu() {
		return Arrays.asList(
				MenuItem.of(
						ProjectIndexController.PROJECT_INDEX_PATH,
						"index",
						"Home",
						"ROLE_IAF_USER"),
				MenuItem.of(
						ProjectRepositoryMappingController.PROJECT_REPOSITORY_MAPPING_PATH,
						"repositorymappings",
						"Repository Mappings",
						"ROLE_REPOSITORY_MAPPINGS"),
				MenuItem.of(
						ProjectFieldMappingController.PROJECT_FIELD_MAPPING_TEMPLATES_PATH,
						"fieldmappingtemplates",
						"Field Mapping Templates",
						"ROLE_MAPPING_RULE_TEMPLATES"),						
				MenuItem.of(
						ProjectHospitalController.PROJECT_HOSPITAL_PATH,
						"hospitalentrys",
						"Failed Shipments",
						"ROLE_HOSPITAL"),
				MenuItem.of(
						ProjectIdentityMappingsController.PROJECT_IDENTITY_MAPPINGS_PATH,
						"identitymappings",
						"Identity Mappings",
						"ROLE_IDENTITY_MAPPINGS")
				);
	}

	public static final class MenuItem {
		private final String value;
		private final String tooltip;
		private final String link;
		private final String role;
		
		private MenuItem(String link, String value, String tooltip, String role) {
			this.value = value;
			this.tooltip = tooltip;
			this.link = link;
			this.role = role == null ? "ROLE_USER" : role;
		}
		
		public static MenuItem of(String link, String value, String tooltip, String role) {
			return new MenuItem(link, value, tooltip, role);
		}

		public String getValue() {
			return value;
		}

		public String getTooltip() {
			return tooltip;
		}

		public String getLink() {
			return link;
		}

		public String getRole() {
			return role;
		}
	}
}
