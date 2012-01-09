package com.collabnet.ccf.ccfmaster.controller.web.project;

import static com.google.common.collect.Iterables.filter;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.controller.web.LandscapeRepositoryMappingsController;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionStatus;
import com.collabnet.ccf.ccfmaster.web.model.RepositoryMappingsModel;
import com.google.common.base.Predicate;

@Controller
@RequestMapping(ProjectRepositoryMappingController.PROJECT_REPOSITORY_MAPPING_PATH)
public class ProjectRepositoryMappingController extends AbstractProjectController {
	private static final String RMD_ID_REQUEST_PARAM = "rmdid";
	private static final String DIRECTION_REQUEST_PARAM = "direction";
	public static final String PROJECT_REPOSITORY_MAPPING_NAME = "project/repositorymappings";
	public static final String PROJECT_REPOSITORY_MAPPING_PATH = "/" + PROJECT_REPOSITORY_MAPPING_NAME;
	private final String tfUrl;
	
	@Autowired 
	public ProjectRepositoryMappingController(CCFRuntimePropertyHolder ccfProps) {
		this.tfUrl = ccfProps.getTfUrl();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String list(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(value=DIRECTION_REQUEST_PARAM, defaultValue="FORWARD") Directions direction,
			@RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
			@RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
			Model model,HttpSession session) {
		List<RepositoryMappingDirection> rmds = paginate(
				RepositoryMappingDirection.findRepositoryMappingDirectionsByExternalAppAndDirection(ea, direction),
				RepositoryMappingDirection.countRepositoryMappingDirectionsByExternalAppAndDirection(ea, direction),
				page, size, model)
				.getResultList();
		List<RepositoryMappingsModel> rmmList = LandscapeRepositoryMappingsController.makeRepositoryMappingsModel(rmds, tfUrl);
		session.setAttribute(ControllerConstants.SIZE_IN_SESSION, size);
		session.setAttribute(ControllerConstants.PAGE_IN_SESSION, page);
		model.addAttribute("repositoryMappingsModel", rmmList);
		return PROJECT_REPOSITORY_MAPPING_NAME + "/" + direction;
	}
	
	@RequestMapping(value="/pause", method=RequestMethod.POST)
	public String pause(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(DIRECTION_REQUEST_PARAM) Directions direction,
			@RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection[] rmds, Model model,HttpSession session) {
		RepositoryMappingDirectionStatus status = RepositoryMappingDirectionStatus.PAUSED;
		Iterable<RepositoryMappingDirection> validRmds = filter(Arrays.asList(rmds), isValidRmd(ea, direction));
		setStatusForRmds(validRmds, status);
		populatePageSizetoModel(direction,ea,model, session);
		return redirectUrl(direction);
	}

	@RequestMapping(value="/resume", method=RequestMethod.POST)
	public String resume(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(DIRECTION_REQUEST_PARAM) Directions direction,
			@RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection[] rmds, Model model,HttpSession session) {
		RepositoryMappingDirectionStatus status = RepositoryMappingDirectionStatus.RUNNING;
		Iterable<RepositoryMappingDirection> validRmds = filter(Arrays.asList(rmds), isValidRmd(ea, direction));
		setStatusForRmds(validRmds, status);
		populatePageSizetoModel(direction,ea,model, session);
		return redirectUrl(direction);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(DIRECTION_REQUEST_PARAM) Directions direction,
			@RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection[] rmds, Model model,HttpSession session) {
		Iterable<RepositoryMappingDirection> validRmds = filter(Arrays.asList(rmds), isValidRmd(ea, direction));
		try {
			for (RepositoryMappingDirection rmd : validRmds) {
				rmd.remove();
			}
			FlashMap.setSuccessMessage(ControllerConstants.RMDDELETESUCCESSMESSAGE);
		} catch (Exception e) {
			FlashMap.setErrorMessage(ControllerConstants.RMDDELETEFAILUREMESSAGE, e.getMessage());
		}
		populatePageSizetoModel(direction,ea,model, session);
		return redirectUrl(direction);
	}

	private String redirectUrl(Directions direction) {
		return String.format("redirect:%s?%s=%s", PROJECT_REPOSITORY_MAPPING_PATH, DIRECTION_REQUEST_PARAM, direction);
	}

	private static void setStatusForRmds(Iterable<RepositoryMappingDirection> validRmds,
			RepositoryMappingDirectionStatus status) {
		try {
			for (RepositoryMappingDirection rmd : validRmds) {
				rmd.setStatus(status);
				rmd.merge();
			}
			String messageCode = (status == RepositoryMappingDirectionStatus.PAUSED) ? ControllerConstants.RMDPAUSESYNCSUCCESSMESSAGE : ControllerConstants.RMDRESUMESYNCSUCCESSMESSAGE;
			FlashMap.setSuccessMessage(messageCode);
		} catch (Exception exception) {
			String messageCode = (status == RepositoryMappingDirectionStatus.PAUSED) ? ControllerConstants.RMDPAUSESYNCFAILUREMESSAGE : ControllerConstants.RMDRESUMESYNCFAILUREMESSAGE;
			FlashMap.setErrorMessage(messageCode, exception.getMessage());
		}
	}

	private static Predicate<RepositoryMappingDirection> isValidRmd(final ExternalApp ea, final Directions direction) {
		return new Predicate<RepositoryMappingDirection>() {
			@Override
			public boolean apply(RepositoryMappingDirection rmd) {
				if (rmd == null) return false;
				final ExternalApp externalApp = rmd.getRepositoryMapping().getExternalApp();
				return direction == rmd.getDirection() &&
				       ea.getId().equals(externalApp.getId());
			}
		};
	}
	
	public static void populatePageSizetoModel(Directions direction,ExternalApp ea, Model model,
			HttpSession session) {
		Integer size = (Integer) session.getAttribute(ControllerConstants.SIZE_IN_SESSION) == null ? ControllerConstants.PAGINATION_SIZE: (Integer) session.getAttribute(ControllerConstants.SIZE_IN_SESSION);
		float nrOfPages = (float)RepositoryMappingDirection.countRepositoryMappingDirectionsByExternalAppAndDirection(ea, direction) / size.intValue();
		Integer page = (Integer) session.getAttribute(ControllerConstants.PAGE_IN_SESSION);
		// if page in session is null.get the default value of page
		if (page == null) {
			page = Integer.valueOf(ControllerConstants.DEFAULT_PAGE);
		} else if (page <= 0) {
			// in case if current page value is less than or equal to zero get
			// default value of page (on deleting the last record of the first
			// page)
			page = Integer.valueOf(ControllerConstants.DEFAULT_PAGE);
		} else if (Math.ceil(nrOfPages) != 0.0 && page >= Math.ceil(nrOfPages)) {
			// in case if current page value is greater than no of page (on
			// deleting last record from the current page.traverse to the
			// previous page)
			page = (int) Math.ceil(nrOfPages);
		}
		model.addAttribute("page", page);
		model.addAttribute("size", size);
	}
	
}
