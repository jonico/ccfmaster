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
		session.setAttribute("sizesession", size);
		session.setAttribute("pagesession", page);
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
		populatePageandSizeInModel(model, session);
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
		populatePageandSizeInModel(model, session);
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
		populatePageandSizeInModel(model, session);
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
	
	
	private void populatePageandSizeInModel(Model model, HttpSession session) {
		model.addAttribute("page", (session.getAttribute(ControllerConstants.PAGE_IN_SESSION) == null) ? ControllerConstants.DEFAULT_PAGE : session.getAttribute(ControllerConstants.PAGE_IN_SESSION));
		model.addAttribute("size", (session.getAttribute(ControllerConstants.SIZE_IN_SESSION) == null) ? ControllerConstants.DEFAULT_PAGE_SIZE : session.getAttribute(ControllerConstants.SIZE_IN_SESSION));
	}
}
