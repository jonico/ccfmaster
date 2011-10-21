package com.collabnet.ccf.ccfmaster.controller.web.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.controller.api.BadRequestException;
import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.controller.web.LandscapeHospitalController;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryList;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.web.helper.XmlWebHelper;
import com.collabnet.ccf.ccfmaster.web.model.HospitalModel;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

@Controller
@RequestMapping(ProjectHospitalController.PROJECT_HOSPITAL_PATH)
public class ProjectHospitalController extends AbstractProjectController {
	public static final String PROJECT_HOSPITAL_NAME = "project/hospitalentrys";
	public static final String PROJECT_HOSPITAL_PATH = "/" + PROJECT_HOSPITAL_NAME;

	private static final String HOSPITAL_ID_REQUEST_PARAM = "hospitalid";
	private static final String DIRECTION_REQUEST_PARAM = "direction";
	private static final String RMD_ID_REQUEST_PARAM = "rmdid";
	private static final String RM_ID_REQUEST_PARAM = "rmid";
	private static final String FROM_EXTERNAL_FUNCTION = "fromExternalFunction";
	private static final String TARGET_FILTER_ARTIFACT_ID = "targetfilterartifactid";
	private static final String SOURCE_FILTER_ARTIFACT_ID = "sourcefilterartifactid";
	
	@Autowired
	private CCFRuntimePropertyHolder ccfRuntimePropertyHolder;
	
	@RequestMapping(method=RequestMethod.GET)
	public String list(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(value=DIRECTION_REQUEST_PARAM, defaultValue="FORWARD") Directions direction,
			@RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
			@RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
			Model model,
			HttpSession session) {
		cleanSession(session);
		List<HospitalEntry> hospitalEntrys = paginate(
				HospitalEntry.findHospitalEntrysByExternalAppAndDirection(ea, direction),
				HospitalEntry.countHospitalEntrysByExternalAppAndDirection(ea, direction),
				page, size, model).getResultList();
		return doList(hospitalEntrys, direction, model);
	}
	
	/**
	 * @param session
	 */
	private void cleanSession(HttpSession session) {
		session.removeAttribute("sourceFilterArtifactId");
		session.removeAttribute("targetFilterArtifactId");
		session.removeAttribute(FROM_EXTERNAL_FUNCTION);
	}

	@RequestMapping(method=RequestMethod.GET, params=RMD_ID_REQUEST_PARAM)
	public String listForRepositoryMappingDirection(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd,
			@RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
			@RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
			Model model,HttpSession session) {
		cleanSession(session);
		validateRepositoryMappingDirection(ea, rmd);
		Directions direction = rmd.getDirection();
		List<HospitalEntry> hospitalEntrys = paginate(
				HospitalEntry.findHospitalEntrysByRepositoryMappingDirection(rmd),
				HospitalEntry.countHospitalEntrysByRepositoryMappingDirection(rmd),
				page, size, model)
				.getResultList();
		model.addAttribute("rmdid", rmd.getId());
		return doList(hospitalEntrys, direction, model);
	}

	String doList(List<HospitalEntry> hospitalEntrys, Directions direction, Model model) {
		String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
		List<HospitalModel> hospitalModels = LandscapeHospitalController.makeHospitalModel(hospitalEntrys, tfUrl, direction);
		model.addAttribute("hospitalmodel", hospitalModels);
		return PROJECT_HOSPITAL_NAME + "/" + direction;
	}
	
	@RequestMapping("/details")
	public String showDetails(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(HOSPITAL_ID_REQUEST_PARAM) HospitalEntry entry,
			@RequestParam(RMD_ID_REQUEST_PARAM) String rmdid,
			Model model) {
		verifyHospitalEntry(ea, entry);
		String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
		HospitalModel hospitalModel = LandscapeHospitalController.modelFor(entry, tfUrl,entry.getRepositoryMappingDirection().getDirection());
		model.addAttribute("rmdid", rmdid);
		model.addAttribute("direction", entry.getRepositoryMappingDirection().getDirection());
		model.addAttribute("hospitalist", hospitalModel);
		return PROJECT_HOSPITAL_NAME + "/details";
	}
	
	@RequestMapping("/payload")
	public String examinePayload(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(HOSPITAL_ID_REQUEST_PARAM) HospitalEntry entry,
			@RequestParam(RM_ID_REQUEST_PARAM) String rmid,
			Model model) {
		verifyHospitalEntry(ea, entry);
		String genericArtifact = "";
		try {
			String xml = entry.getGenericArtifact();
			Document document = XmlWebHelper.createDocument(xml);
			Element element = document.getDocumentElement();
			genericArtifact = XmlWebHelper.xmlToString(element);
		} catch (Exception e) {
			FlashMap.setErrorMessage(ControllerConstants.EXAMINEHOSPITALFAILUREMESSAGE, e.getMessage());
		}
		model.addAttribute("rmid", rmid);
		model.addAttribute("direction", entry.getRepositoryMappingDirection().getDirection());
		model.addAttribute("genericArtifact", genericArtifact);
		return PROJECT_HOSPITAL_NAME + "/payload";
	}
	
	@RequestMapping(value="/replay", method=RequestMethod.POST)
	public String replay(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(HOSPITAL_ID_REQUEST_PARAM) HospitalEntry[] entries,
			@RequestParam(RM_ID_REQUEST_PARAM) String rmid,
			@RequestParam(SOURCE_FILTER_ARTIFACT_ID) String  source_filter_artifact_id,
			@RequestParam(TARGET_FILTER_ARTIFACT_ID) String  target_filter_artifact_id,
			@RequestParam(value=DIRECTION_REQUEST_PARAM, defaultValue="FORWARD") Directions direction,HttpSession session) {
		try {
			Iterable<HospitalEntry> validEntries = Iterables.filter(Arrays.asList(entries), isValidHospitalEntry(direction, ea));
			for (HospitalEntry entry : validEntries) {
				entry.setErrorCode(LandscapeHospitalController.REPLAY);
				entry.merge();
			}
			FlashMap.setSuccessMessage(ControllerConstants.HOSPITALREPLAYSUCCESSMESSAGE);
		} catch (Exception e) {
			FlashMap.setErrorMessage(ControllerConstants.HOSPITALREPLAYFAILUREMESSAGE, e.getMessage());
		}
		return getNextView(rmid, source_filter_artifact_id,
				target_filter_artifact_id, direction, session);
		
	}

	@ResponseBody
	@RequestMapping(value="/export")
	public HospitalEntryList export(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(HOSPITAL_ID_REQUEST_PARAM) HospitalEntry[] entries,
			@RequestParam(value=DIRECTION_REQUEST_PARAM, defaultValue="FORWARD") Directions direction,
			HttpServletResponse response) {
		Iterable<HospitalEntry> validEntries = Iterables.filter(Arrays.asList(entries), isValidHospitalEntry(direction, ea));
		response.setHeader("Content-Disposition", "attachment; filename=\"export.xml\"");
//		response.setContentType("text/xml; charset=utf-8");
		return new HospitalEntryList(ImmutableList.copyOf(validEntries));
	}

	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(HOSPITAL_ID_REQUEST_PARAM) HospitalEntry[] entries,
			@RequestParam(RM_ID_REQUEST_PARAM) String rmid,
			@RequestParam(SOURCE_FILTER_ARTIFACT_ID) String  source_filter_artifact_id,
			@RequestParam(TARGET_FILTER_ARTIFACT_ID) String  target_filter_artifact_id,
			@RequestParam(value=DIRECTION_REQUEST_PARAM, defaultValue="FORWARD") Directions direction,HttpSession session) {
		try {
			Iterable<HospitalEntry> validEntries = Iterables.filter(Arrays.asList(entries), isValidHospitalEntry(direction, ea));
			for (HospitalEntry entry : validEntries) {
				entry.remove();
			}
			FlashMap.setSuccessMessage(ControllerConstants.HOSPITALDELETESUCCESSMESSAGE);
		} catch (Exception e) {
			FlashMap.setErrorMessage(ControllerConstants.HOSPITALDELETEFAILUREMESSAGE, e.getMessage());
		}
		return getNextView(rmid, source_filter_artifact_id,
				target_filter_artifact_id, direction, session);
		
	}

	/**
	 * @param rmid
	 * @param source_filter_artifact_id
	 * @param target_filter_artifact_id
	 * @param direction
	 * @param session
	 * @return
	 */
	private String getNextView(String rmid, String source_filter_artifact_id,
			String target_filter_artifact_id, Directions direction,
			HttpSession session) {
		if(rmid.equals("")&& source_filter_artifact_id.equals("")&& target_filter_artifact_id.equals("")){
			return "redirect:" + PROJECT_HOSPITAL_PATH + "?direction=" + direction;
		}
		else if(!source_filter_artifact_id.equals("")||!target_filter_artifact_id.equals("") ){
			session.setAttribute(FROM_EXTERNAL_FUNCTION, "true");
			return  "redirect:"+ PROJECT_HOSPITAL_PATH + "/applyfilter";
		}
		else{
			return "redirect:" + PROJECT_HOSPITAL_PATH + "?rmdid="+rmid+"&direction="+ direction;
		}
	}
	
	static Predicate<HospitalEntry> isValidHospitalEntry(final Directions direction, final ExternalApp ea) {
		return new Predicate<HospitalEntry>() {
			@Override
			public boolean apply(HospitalEntry input) {
				if (input == null) return false;
				RepositoryMappingDirection rmd = input.getRepositoryMappingDirection();
				final ExternalApp externalApp = rmd.getRepositoryMapping().getExternalApp();
				return rmd.getDirection() == direction && ea.getId().equals(externalApp.getId());
			}
			
		};
	}

	void validateRepositoryMappingDirection(ExternalApp ea, RepositoryMappingDirection rmd) {
		if (!ea.getId().equals(rmd.getRepositoryMapping().getExternalApp().getId()))
			throw new BadRequestException("wrong external app.");
	}

	void verifyHospitalEntry(ExternalApp ea, HospitalEntry entry) {
		validateRepositoryMappingDirection(ea, entry.getRepositoryMappingDirection());
	}

	@RequestMapping(value = "/applyfilter")
	public String applyFilterForHospital(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(value=DIRECTION_REQUEST_PARAM, defaultValue="FORWARD") Directions directions,
			@RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
			@RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
			Model model,HttpServletRequest request,HttpSession session) {
		String fromExternalFunction=(String)session.getAttribute(FROM_EXTERNAL_FUNCTION);
		String sourceFilterArtifactId=null;
		String targetFilterArtifactId=null;
		if("true".equals(fromExternalFunction)|| page != null || size != null){
			sourceFilterArtifactId=(String)session.getAttribute("sourceFilterArtifactId");
			targetFilterArtifactId=(String)session.getAttribute("targetFilterArtifactId");
		}
		else{
			sourceFilterArtifactId=request.getParameter(SOURCE_FILTER_ARTIFACT_ID);
			targetFilterArtifactId=request.getParameter(TARGET_FILTER_ARTIFACT_ID);
		}
		List<HospitalEntry> filterHospitalEntrys = new ArrayList<HospitalEntry>();
		//if no input entered for source and target artifactid, and if filter is applied.display the default entries
		if("".equals(sourceFilterArtifactId)&&"".equals(targetFilterArtifactId)){
			filterHospitalEntrys =paginate(
					HospitalEntry.findHospitalEntrysByExternalAppAndDirection(ea,directions),
					HospitalEntry.countHospitalEntrysByExternalAppAndDirection(ea,directions),
					page, size, model).getResultList();
			}
		else{
			List<HospitalEntry> hospitalEntry =paginate(
					HospitalEntry.findHospitalEntrysByDirectionAndSourceArtifactIdOrTargetArtifactId(directions, sourceFilterArtifactId, targetFilterArtifactId),
					HospitalEntry.countHospitalEntrysByByDirectionAndSourceArtifactIdOrTargetArtifactId(directions, sourceFilterArtifactId, targetFilterArtifactId),
					page, size, model).getResultList();
			filterHospitalEntrys = filterEntrys(directions,ea,hospitalEntry);
		}
		session.setAttribute("sourceFilterArtifactId", sourceFilterArtifactId);
		session.setAttribute("targetFilterArtifactId", targetFilterArtifactId);
		model.addAttribute("sourceFilterArtifactId", sourceFilterArtifactId);
		model.addAttribute("targetFilterArtifactId", targetFilterArtifactId);
		return doList(filterHospitalEntrys, directions, model);

	}
	
	/**
	 * @param ea
	 * @param identityMappingEntrys
	 * @return
	 */
	private List<HospitalEntry> filterEntrys(Directions directions,ExternalApp ea,
			List<HospitalEntry> hospitalEntrys) {
		Iterable<HospitalEntry> validEntries = Iterables.filter(hospitalEntrys, isValidHospitalEntry(directions, ea));
		List<HospitalEntry> filterHospitalEntrys=new ArrayList<HospitalEntry>();
		for (HospitalEntry entry : validEntries) {
			filterHospitalEntrys.add(entry);
		}
		return filterHospitalEntrys;
	}
}
