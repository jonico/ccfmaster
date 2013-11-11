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
import org.springframework.web.servlet.support.RequestContext;
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
    public static final String       PROJECT_HOSPITAL_NAME     = "project/hospitalentrys";
    public static final String       PROJECT_HOSPITAL_PATH     = "/"
                                                                       + PROJECT_HOSPITAL_NAME;

    private static final String      HOSPITAL_ID_REQUEST_PARAM = "hospitalid";
    private static final String      DIRECTION_REQUEST_PARAM   = "direction";
    private static final String      RMD_ID_REQUEST_PARAM      = "rmdid";
    private static final String      RM_ID_REQUEST_PARAM       = "rmid";
    private static final String      FROM_EXTERNAL_FUNCTION    = "fromExternalFunction";
    private static final String      TARGET_FILTER_ARTIFACT_ID = "targetfilterartifactid";
    private static final String      SOURCE_FILTER_ARTIFACT_ID = "sourcefilterartifactid";

    @Autowired
    private CCFRuntimePropertyHolder ccfRuntimePropertyHolder;

    @RequestMapping(value = "/applyfilter")
    public String applyFilterForHospital(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions directions,
            @RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
            @RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
            Model model, HttpServletRequest request, HttpSession session) {
        String fromExternalFunction = (String) session
                .getAttribute(FROM_EXTERNAL_FUNCTION);
        String sourceFilterArtifactId = null;
        String targetFilterArtifactId = null;
        if ("true".equals(fromExternalFunction)) {
            sourceFilterArtifactId = (String) session
                    .getAttribute("sourceFilterArtifactId");
            targetFilterArtifactId = (String) session
                    .getAttribute("targetFilterArtifactId");
        } else if (page != null && size != null
                && request.getParameter(SOURCE_FILTER_ARTIFACT_ID) != null
                && request.getParameter(TARGET_FILTER_ARTIFACT_ID) != null) {
            sourceFilterArtifactId = request
                    .getParameter(SOURCE_FILTER_ARTIFACT_ID);
            targetFilterArtifactId = request
                    .getParameter(TARGET_FILTER_ARTIFACT_ID);
            page = (Integer) session
                    .getAttribute(ControllerConstants.PAGE_IN_SESSION);
            size = (Integer) session
                    .getAttribute(ControllerConstants.SIZE_IN_SESSION);
        } else if (page != null && size != null
                && session.getAttribute("sourceFilterArtifactId") != null
                && session.getAttribute("targetFilterArtifactId") != null) {
            sourceFilterArtifactId = (String) session
                    .getAttribute("sourceFilterArtifactId");
            targetFilterArtifactId = (String) session
                    .getAttribute("targetFilterArtifactId");
        } else {
            sourceFilterArtifactId = request
                    .getParameter(SOURCE_FILTER_ARTIFACT_ID);
            targetFilterArtifactId = request
                    .getParameter(TARGET_FILTER_ARTIFACT_ID);
        }
        List<HospitalEntry> filterHospitalEntrys = new ArrayList<HospitalEntry>();
        //if no input entered for source and target artifactid, and if filter is applied.display the default entries
        if ("".equals(sourceFilterArtifactId)
                && "".equals(targetFilterArtifactId)) {
            filterHospitalEntrys = paginate(
                    HospitalEntry.findHospitalEntrysByExternalAppAndDirection(
                            ea, directions),
                    HospitalEntry.countHospitalEntrysByExternalAppAndDirection(
                            ea, directions), page, size, model).getResultList();
        } else {
            List<HospitalEntry> hospitalEntry = paginate(
                    HospitalEntry.findHospitalEntrysByDirectionAndSourceArtifactIdOrTargetArtifactId(
                            directions, sourceFilterArtifactId,
                            targetFilterArtifactId),
                    HospitalEntry
                            .countHospitalEntrysByByDirectionAndSourceArtifactIdOrTargetArtifactId(
                                    directions, sourceFilterArtifactId,
                                    targetFilterArtifactId), page, size, model)
                    .getResultList();
            filterHospitalEntrys = filterEntrys(directions, ea, hospitalEntry);
        }
        session.setAttribute("sourceFilterArtifactId", sourceFilterArtifactId);
        session.setAttribute("targetFilterArtifactId", targetFilterArtifactId);
        model.addAttribute("sourceFilterArtifactId", sourceFilterArtifactId);
        model.addAttribute("targetFilterArtifactId", targetFilterArtifactId);
        return doList(filterHospitalEntrys, directions, model);

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(HOSPITAL_ID_REQUEST_PARAM) HospitalEntry[] entries,
            @RequestParam(RM_ID_REQUEST_PARAM) String rmid,
            @RequestParam(SOURCE_FILTER_ARTIFACT_ID) String source_filter_artifact_id,
            @RequestParam(TARGET_FILTER_ARTIFACT_ID) String target_filter_artifact_id,
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions direction,
            Model model, HttpSession session) {
        try {
            Iterable<HospitalEntry> validEntries = Iterables
                    .filter(Arrays.asList(entries),
                            isValidHospitalEntry(direction, ea));
            for (HospitalEntry entry : validEntries) {
                entry.remove();
            }
            FlashMap.setSuccessMessage(ControllerConstants.HOSPITALDELETESUCCESSMESSAGE);
        } catch (Exception e) {
            FlashMap.setErrorMessage(
                    ControllerConstants.HOSPITALDELETEFAILUREMESSAGE,
                    e.getMessage());
        }
        populatePageSizetoModel(direction, ea, model, session);
        return getNextView(rmid, source_filter_artifact_id,
                target_filter_artifact_id, direction, session);

    }

    @RequestMapping("/payload")
    public String examinePayload(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(HOSPITAL_ID_REQUEST_PARAM) HospitalEntry entry,
            @RequestParam(RM_ID_REQUEST_PARAM) String rmid, Model model,
            HttpServletRequest request, HttpSession session) {
        RequestContext ctx = new RequestContext(request);
        verifyHospitalEntry(ea, entry);
        String genericArtifact = "";
        try {
            String xml = entry.getGenericArtifact();
            Document document = XmlWebHelper.createDocument(xml);
            Element element = document.getDocumentElement();
            genericArtifact = XmlWebHelper.xmlToString(element);
        } catch (Exception e) {
            //FlashMap.setErrorMessage(ControllerConstants.EXAMINEHOSPITALFAILUREMESSAGE, e.getMessage());
            model.addAttribute(
                    "connectionerror",
                    ctx.getMessage(ControllerConstants.EXAMINEHOSPITALFAILUREMESSAGE)
                            + e.getMessage());
        }
        model.addAttribute("rmid", rmid);
        model.addAttribute("direction", entry.getRepositoryMappingDirection()
                .getDirection());
        model.addAttribute("genericArtifact", genericArtifact);
        populatePageSizetoModel(entry.getRepositoryMappingDirection()
                .getDirection(), ea, model, session);
        return PROJECT_HOSPITAL_NAME + "/payload";
    }

    @ResponseBody
    @RequestMapping(value = "/export")
    public HospitalEntryList export(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(HOSPITAL_ID_REQUEST_PARAM) HospitalEntry[] entries,
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions direction,
            HttpServletResponse response) {
        Iterable<HospitalEntry> validEntries = Iterables.filter(
                Arrays.asList(entries), isValidHospitalEntry(direction, ea));
        response.setHeader("Content-Disposition",
                "attachment; filename=\"export.xml\"");
        //		response.setContentType("text/xml; charset=utf-8");
        return new HospitalEntryList(ImmutableList.copyOf(validEntries));
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions direction,
            @RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
            @RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
            Model model, HttpSession session) {
        cleanSession(session);
        List<HospitalEntry> hospitalEntrys = paginate(
                HospitalEntry.findHospitalEntrysByExternalAppAndDirection(ea,
                        direction),
                HospitalEntry.countHospitalEntrysByExternalAppAndDirection(ea,
                        direction), page, size, model).getResultList();
        session.setAttribute(ControllerConstants.SIZE_IN_SESSION, size);
        session.setAttribute(ControllerConstants.PAGE_IN_SESSION, page);
        return doList(hospitalEntrys, direction, model);
    }

    @RequestMapping(method = RequestMethod.GET, params = RMD_ID_REQUEST_PARAM)
    public String listForRepositoryMappingDirection(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd,
            @RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
            @RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
            Model model, HttpSession session) {
        cleanSession(session);
        validateRepositoryMappingDirection(ea, rmd);
        Directions direction = rmd.getDirection();
        List<HospitalEntry> hospitalEntrys = paginate(
                HospitalEntry
                        .findHospitalEntrysByRepositoryMappingDirection(rmd),
                HospitalEntry
                        .countHospitalEntrysByRepositoryMappingDirection(rmd),
                page, size, model).getResultList();
        model.addAttribute("rmdid", rmd.getId());
        return doList(hospitalEntrys, direction, model);
    }

    @RequestMapping(value = "/replay", method = RequestMethod.POST)
    public String replay(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(HOSPITAL_ID_REQUEST_PARAM) HospitalEntry[] entries,
            @RequestParam(RM_ID_REQUEST_PARAM) String rmid,
            @RequestParam(SOURCE_FILTER_ARTIFACT_ID) String source_filter_artifact_id,
            @RequestParam(TARGET_FILTER_ARTIFACT_ID) String target_filter_artifact_id,
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions direction,
            Model model, HttpSession session) {
        try {
            Iterable<HospitalEntry> validEntries = Iterables
                    .filter(Arrays.asList(entries),
                            isValidHospitalEntry(direction, ea));
            for (HospitalEntry entry : validEntries) {
                entry.setErrorCode(LandscapeHospitalController.REPLAY);
                entry.merge();
            }
            FlashMap.setSuccessMessage(ControllerConstants.HOSPITALREPLAYSUCCESSMESSAGE);
        } catch (Exception e) {
            FlashMap.setErrorMessage(
                    ControllerConstants.HOSPITALREPLAYFAILUREMESSAGE,
                    e.getMessage());
        }
        populatePageSizetoModel(direction, ea, model, session);
        return getNextView(rmid, source_filter_artifact_id,
                target_filter_artifact_id, direction, session);

    }

    @RequestMapping("/details")
    public String showDetails(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(HOSPITAL_ID_REQUEST_PARAM) HospitalEntry entry,
            @RequestParam(RMD_ID_REQUEST_PARAM) String rmdid, Model model,
            HttpSession session) {
        verifyHospitalEntry(ea, entry);
        String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
        HospitalModel hospitalModel = LandscapeHospitalController.modelFor(
                entry, tfUrl, entry.getRepositoryMappingDirection()
                        .getDirection());
        model.addAttribute("rmdid", rmdid);
        model.addAttribute("direction", entry.getRepositoryMappingDirection()
                .getDirection());
        model.addAttribute("hospitalist", hospitalModel);
        populatePageSizetoModel(entry.getRepositoryMappingDirection()
                .getDirection(), ea, model, session);
        return PROJECT_HOSPITAL_NAME + "/details";
    }

    /**
     * @param session
     */
    private void cleanSession(HttpSession session) {
        session.removeAttribute("sourceFilterArtifactId");
        session.removeAttribute("targetFilterArtifactId");
        session.removeAttribute(FROM_EXTERNAL_FUNCTION);
    }

    /**
     * @param ea
     * @param identityMappingEntrys
     * @return
     */
    private List<HospitalEntry> filterEntrys(Directions directions,
            ExternalApp ea, List<HospitalEntry> hospitalEntrys) {
        Iterable<HospitalEntry> validEntries = Iterables.filter(hospitalEntrys,
                isValidHospitalEntry(directions, ea));
        List<HospitalEntry> filterHospitalEntrys = new ArrayList<HospitalEntry>();
        for (HospitalEntry entry : validEntries) {
            filterHospitalEntrys.add(entry);
        }
        return filterHospitalEntrys;
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
        if (rmid.equals("") && source_filter_artifact_id.equals("")
                && target_filter_artifact_id.equals("")) {
            return "redirect:" + PROJECT_HOSPITAL_PATH + "?direction="
                    + direction;
        } else if (!source_filter_artifact_id.equals("")
                || !target_filter_artifact_id.equals("")) {
            session.setAttribute(FROM_EXTERNAL_FUNCTION, "true");
            return "redirect:" + PROJECT_HOSPITAL_PATH + "/applyfilter";
        } else {
            return "redirect:" + PROJECT_HOSPITAL_PATH + "?rmdid=" + rmid
                    + "&direction=" + direction;
        }
    }

    String doList(List<HospitalEntry> hospitalEntrys, Directions direction,
            Model model) {
        String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
        List<HospitalModel> hospitalModels = LandscapeHospitalController
                .makeHospitalModel(hospitalEntrys, tfUrl, direction);
        model.addAttribute("hospitalmodel", hospitalModels);
        return PROJECT_HOSPITAL_NAME + "/" + direction;
    }

    void validateRepositoryMappingDirection(ExternalApp ea,
            RepositoryMappingDirection rmd) {
        if (!ea.getId().equals(
                rmd.getRepositoryMapping().getExternalApp().getId()))
            throw new BadRequestException("wrong external app.");
    }

    void verifyHospitalEntry(ExternalApp ea, HospitalEntry entry) {
        validateRepositoryMappingDirection(ea,
                entry.getRepositoryMappingDirection());
    }

    public static void populatePageSizetoModel(Directions direction,
            ExternalApp ea, Model model, HttpSession session) {
        Integer size = (Integer) session
                .getAttribute(ControllerConstants.SIZE_IN_SESSION) == null ? ControllerConstants.PAGINATION_SIZE
                : (Integer) session
                        .getAttribute(ControllerConstants.SIZE_IN_SESSION);
        float nrOfPages = (float) HospitalEntry
                .countHospitalEntrysByExternalAppAndDirection(ea, direction)
                / size.intValue();
        Integer page = (Integer) session
                .getAttribute(ControllerConstants.PAGE_IN_SESSION);
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

    static Predicate<HospitalEntry> isValidHospitalEntry(
            final Directions direction, final ExternalApp ea) {
        return new Predicate<HospitalEntry>() {
            @Override
            public boolean apply(HospitalEntry input) {
                if (input == null)
                    return false;
                RepositoryMappingDirection rmd = input
                        .getRepositoryMappingDirection();
                final ExternalApp externalApp = rmd.getRepositoryMapping()
                        .getExternalApp();
                return rmd.getDirection() == direction
                        && ea.getId().equals(externalApp.getId());
            }

        };
    }

}
