package com.collabnet.ccf.ccfmaster.controller.web;

import java.util.ArrayList;
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
import org.springframework.web.servlet.support.RequestContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryList;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.ArtifactDetail;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.RepositoryDetail;
import com.collabnet.ccf.ccfmaster.web.helper.XmlWebHelper;
import com.collabnet.ccf.ccfmaster.web.model.HospitalModel;

@RequestMapping("/admin/**")
@Controller
public class LandscapeHospitalController extends AbstractLandscapeController {

    private static final String     FROM_EXTERNAL_FUNCTION    = "fromExternalFunction";
    private static final String     TARGET_FILTER_ARTIFACT_ID = "targetfilterartifactid";
    private static final String     SOURCE_FILTER_ARTIFACT_ID = "sourcefilterartifactid";
    private static final String     FORWARD                   = "FORWARD";
    public static final String      REPLAY                    = "replay";
    private static final String     HOSPITALID                = "hospitalid";
    private static final String     RMD_ID_REQUEST_PARAM      = "rmdid";

    @Autowired
    public CCFRuntimePropertyHolder ccfRuntimePropertyHolder;

    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_APPLY_FILTER_IN_HOSPITAL)
    public String applyFilterForHospital(
            @RequestParam(ControllerConstants.DIRECTION) Directions directions,
            @RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
            @RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
            Model model, HttpServletRequest request, HttpSession session) {
        String fromExternalFunction = (String) session
                .getAttribute(FROM_EXTERNAL_FUNCTION);
        String sourceFilterArtifactId = null;
        String targetFilterArtifactId = null;
        // from other function other than apply filter button get filterids from session
        if ("true".equals(fromExternalFunction)) {
            sourceFilterArtifactId = (String) session
                    .getAttribute("sourceFilterArtifactId");
            targetFilterArtifactId = (String) session
                    .getAttribute("targetFilterArtifactId");
        }
        //if filter got applied with paging get the filter ids from request, page and size from session
        else if (page != null && size != null
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
        }
        //if filter got applied with paging and while navigating thru pages get the old filter ids from the session
        else if (page != null && size != null
                && session.getAttribute("sourceFilterArtifactId") != null
                && session.getAttribute("targetFilterArtifactId") != null) {
            sourceFilterArtifactId = (String) session
                    .getAttribute("sourceFilterArtifactId");
            targetFilterArtifactId = (String) session
                    .getAttribute("targetFilterArtifactId");
        }
        //Default search filter get filterids from the request
        else {
            sourceFilterArtifactId = request
                    .getParameter(SOURCE_FILTER_ARTIFACT_ID);
            targetFilterArtifactId = request
                    .getParameter(TARGET_FILTER_ARTIFACT_ID);
        }
        List<HospitalEntry> hospitalEntry = new ArrayList<HospitalEntry>();
        //if no input entered for source and target artifactid, and if filter is applied.display the default entries
        if ("".equals(sourceFilterArtifactId)
                && "".equals(targetFilterArtifactId)) {
            hospitalEntry = paginate(
                    HospitalEntry.findHospitalEntrysByDirection(directions),
                    HospitalEntry.countHospitalEntrysByDirection(directions),
                    page, size, model).getResultList();
        } else {
            hospitalEntry = paginate(
                    HospitalEntry.findHospitalEntrysByDirectionAndSourceArtifactIdOrTargetArtifactId(
                            directions, sourceFilterArtifactId,
                            targetFilterArtifactId),
                    HospitalEntry
                            .countHospitalEntrysByByDirectionAndSourceArtifactIdOrTargetArtifactId(
                                    directions, sourceFilterArtifactId,
                                    targetFilterArtifactId), page, size, model)
                    .getResultList();
            //hospitalEntry=HospitalEntry.findHospitalEntrysByDirectionAndSourceArtifactIdOrTargetArtifactId(directions, sourceFilterArtifactId, targetFilterArtifactId).getResultList();
        }
        session.setAttribute("sourceFilterArtifactId", sourceFilterArtifactId);
        session.setAttribute("targetFilterArtifactId", targetFilterArtifactId);
        model.addAttribute("sourceFilterArtifactId", sourceFilterArtifactId);
        model.addAttribute("targetFilterArtifactId", targetFilterArtifactId);
        return doList(hospitalEntry, directions, model);

    }

    /**
     * Controller method to delete hospital
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_DELETEHOSPITAL, method = RequestMethod.POST)
    public String deleteHospital(
            @RequestParam(ControllerConstants.DIRECTION) Directions directions,
            @RequestParam(SOURCE_FILTER_ARTIFACT_ID) String source_filter_artifact_id,
            @RequestParam(TARGET_FILTER_ARTIFACT_ID) String target_filter_artifact_id,
            @RequestParam(RMD_ID_REQUEST_PARAM) String rmdid, Model model,
            HttpServletRequest request, HttpSession session) {
        String[] items = request.getParameterValues(HOSPITALID);
        if (items == null)
            items = new String[0];
        try {
            for (String hospitalId : items) {
                HospitalEntry hospitalEntry = HospitalEntry
                        .findHospitalEntry(new Long(hospitalId));
                hospitalEntry.remove();
            }
            FlashMap.setSuccessMessage(ControllerConstants.HOSPITALDELETESUCCESSMESSAGE);
        } catch (Exception exception) {
            FlashMap.setErrorMessage(
                    ControllerConstants.HOSPITALDELETEFAILUREMESSAGE,
                    exception.getMessage());
        }
        model.asMap().clear();
        populatePageSizetoModel(directions, model, session);
        populateHospitalModel(model, directions, request, null);
        return getNextView(directions, source_filter_artifact_id,
                target_filter_artifact_id, rmdid, session);
    }

    /**
     * Controller method to display hospital entries for Participant to TF
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_DISPLAYHOSPITALPARTTOTF, method = RequestMethod.GET)
    public String displayhospitalparttotf(
            @RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
            @RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
            Model model, HttpServletRequest request, HttpSession session) {
        cleanSession(session);
        session.setAttribute(ControllerConstants.SIZE_IN_SESSION, size);
        session.setAttribute(ControllerConstants.PAGE_IN_SESSION, page);
        //	populatePageSizetoModel(Directions.REVERSE,model, session);
        populateHospitalPagedModel(model, Directions.REVERSE, request, null,
                page, size, session);
        return UIPathConstants.LANDSCAPESETTINGS_DISPLAYHOSPITALPARTTOTF;
    }

    /**
     * Controller method to display hospital entries for TF to Participant
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_DISPLAYHOSPITALTFTOPART, method = RequestMethod.GET)
    public String displayhospitaltftopart(
            @RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
            @RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
            Model model, HttpServletRequest request, HttpSession session) {
        cleanSession(session);
        session.setAttribute(ControllerConstants.SIZE_IN_SESSION, size);
        session.setAttribute(ControllerConstants.PAGE_IN_SESSION, page);
        //		populatePageSizetoModel(Directions.FORWARD,model, session);
        populateHospitalPagedModel(model, Directions.FORWARD, request, null,
                page, size, session);
        return UIPathConstants.LANDSCAPESETTINGS_DISPLAYHOSPITALTFTOPART;
    }

    /**
     * Controller method to examine hospital
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_EXAMINEHOSPITAL, method = RequestMethod.POST)
    public String examineHospital(
            @RequestParam(ControllerConstants.DIRECTION) Directions directions,
            @RequestParam(RMD_ID_REQUEST_PARAM) String rmdid, Model model,
            HttpServletRequest request, HttpServletResponse response,
            HttpSession session) {
        RequestContext ctx = new RequestContext(request);
        Landscape landscape = ControllerHelper.findLandscape();
        String hospitalId = request.getParameter(HOSPITALID);
        HospitalEntry hospitalEntryWithId = HospitalEntry
                .findHospitalEntry(new Long(hospitalId));
        String genericArtifact = hospitalEntryWithId.getGenericArtifact();
        String result = null;
        try {
            Document docuemnt = XmlWebHelper.createDocument(genericArtifact);
            Element element = docuemnt.getDocumentElement();
            result = XmlWebHelper.xmlToString(element);
        } catch (Exception exception) {
            model.addAttribute(
                    "connectionerror",
                    ctx.getMessage(ControllerConstants.EXAMINEHOSPITALFAILUREMESSAGE)
                            + exception.getMessage());
        }
        response.setContentType("text/xml; charset=utf-8");
        model.addAttribute("rmdid", rmdid);
        model.addAttribute("genericArtifact", result);
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("landscape", landscape);
        model.addAttribute("direction", directions.name());
        model.addAttribute("selectedLink", "hospital");
        populatePageSizetoModel(directions, model, session);
        return UIPathConstants.LANDSCAPESETTINGS_DISPLAYEXAMINEHOSPITAL;
    }

    /**
     * Controller method to export hospital entry
     * 
     */
    @RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_EXPORTHOSPITALENTRY, method = RequestMethod.POST)
    public void exportHospitalEntry(
            @ModelAttribute("hospitalmodel") HospitalModel hospitalModel,
            Model model, HttpServletRequest request,
            HttpServletResponse response) {
        String[] items = request.getParameterValues(HOSPITALID);
        HospitalEntryList hospitalEntryList = new HospitalEntryList();
        List<HospitalEntry> hospitalList = new ArrayList<HospitalEntry>();
        try {
            for (String hospitalId : items) {
                HospitalEntry hospitalEntry = HospitalEntry
                        .findHospitalEntry(new Long(hospitalId));
                hospitalList.add(hospitalEntry);
            }
            hospitalEntryList.setHospitalEntry(hospitalList);
            XmlWebHelper.createJAXBExport(response, "failedshipment.xml",
                    hospitalEntryList, HospitalEntryList.class);
        } catch (Exception exception) {
            FlashMap.setErrorMessage(
                    ControllerConstants.HOSPITALEXPORTFAILUREMESSAGE,
                    exception.getMessage());
        }
    }

    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_DISPLAY_HOSPITAL_BY_RMD, method = RequestMethod.GET)
    public String listForRepositoryMappingDirection(
            @RequestParam(ControllerConstants.DIRECTION) Directions directions,
            @RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd,
            @RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
            @RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
            Model model, HttpSession session) {
        cleanSession(session);
        List<HospitalEntry> hospitalEntrys = paginate(
                HospitalEntry
                        .findHospitalEntrysByRepositoryMappingDirection(rmd),
                HospitalEntry
                        .countHospitalEntrysByRepositoryMappingDirection(rmd),
                page, size, model).getResultList();
        model.addAttribute("rmdid", rmd.getId());
        return doList(hospitalEntrys, directions, model);

    }

    /**
     * Helper method to populate Hospital Model
     * 
     */
    public void populateHospitalModel(Model model, Directions directions,
            HttpServletRequest request, String hospitalId) {
        Landscape landscape = ControllerHelper.findLandscape();
        String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
        List<HospitalEntry> hospitalEntry = new ArrayList<HospitalEntry>();
        if (hospitalId == null) {
            hospitalEntry = HospitalEntry.findHospitalEntrysByDirection(
                    directions).getResultList();
        } else {
            HospitalEntry hospitalEntryWithId = HospitalEntry
                    .findHospitalEntry(new Long(hospitalId));
            hospitalEntry.add(hospitalEntryWithId);
        }
        List<HospitalModel> hospitalModel = makeHospitalModel(hospitalEntry,
                tfUrl, directions);
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("landscape", landscape);
        model.addAttribute("selectedLink", "hospital");
        model.addAttribute("direction", directions.name());
        model.addAttribute("hospitalmodel", hospitalModel);

    }

    /**
     * Helper method to populate Hospital Model
     * 
     */
    public void populateHospitalPagedModel(Model model, Directions directions,
            HttpServletRequest request, String hospitalId, Integer page,
            Integer size, HttpSession session) {
        Landscape landscape = ControllerHelper.findLandscape();
        String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
        List<HospitalEntry> hospitalEntry = new ArrayList<HospitalEntry>();
        if (hospitalId == null) {
            hospitalEntry = pageHospitalEntries(model, directions, page, size,
                    session);
        } else {
            HospitalEntry hospitalEntryWithId = HospitalEntry
                    .findHospitalEntry(new Long(hospitalId));
            hospitalEntry.add(hospitalEntryWithId);
        }
        List<HospitalModel> hospitalModel = makeHospitalModel(hospitalEntry,
                tfUrl, directions);
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("landscape", landscape);
        model.addAttribute("selectedLink", "hospital");
        model.addAttribute("direction", directions.name());
        model.addAttribute("hospitalmodel", hospitalModel);

    }

    /**
     * Controller method to replay hospital
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_REPLAYHOSPITAL, method = RequestMethod.POST)
    public String replayHospital(
            @RequestParam(ControllerConstants.DIRECTION) Directions directions,
            @RequestParam(RMD_ID_REQUEST_PARAM) String rmdid,
            @RequestParam(SOURCE_FILTER_ARTIFACT_ID) String source_filter_artifact_id,
            @RequestParam(TARGET_FILTER_ARTIFACT_ID) String target_filter_artifact_id,
            Model model, HttpServletRequest request, HttpSession session) {
        String[] items = request.getParameterValues(HOSPITALID);
        try {
            for (String hospitalId : items) {
                HospitalEntry hospitalEntry = HospitalEntry
                        .findHospitalEntry(new Long(hospitalId));
                hospitalEntry.setErrorCode(REPLAY);
                hospitalEntry.merge();
            }
            FlashMap.setSuccessMessage(ControllerConstants.HOSPITALREPLAYSUCCESSMESSAGE);
        } catch (Exception exception) {
            FlashMap.setErrorMessage(
                    ControllerConstants.HOSPITALREPLAYFAILUREMESSAGE,
                    exception.getMessage());
        }
        populatePageSizetoModel(directions, model, session);
        populateHospitalModel(model, directions, request, null);
        return getNextView(directions, source_filter_artifact_id,
                target_filter_artifact_id, rmdid, session);
    }

    /**
     * Controller method to view hospital details
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_DISPLAYHOSPITALINDETAIL, method = RequestMethod.POST)
    public String viewHospital(
            @RequestParam(ControllerConstants.DIRECTION) Directions directions,
            @RequestParam(RMD_ID_REQUEST_PARAM) String rmdid, Model model,
            HttpServletRequest request, HttpSession session) {
        RequestContext ctx = new RequestContext(request);
        String hospitalId = request.getParameter(HOSPITALID);
        try {
            model.addAttribute("rmdid", rmdid);
            populateHospitalModel(model, directions, request, hospitalId);
        } catch (Exception exception) {
            model.addAttribute(
                    "connectionerror",
                    ctx.getMessage(ControllerConstants.VIEWHOSPITALFAILUREMESSAGE)
                            + exception.getMessage());
        }
        populatePageSizetoModel(directions, model, session);
        return UIPathConstants.LANDSCAPESETTINGS_DISPLAYHOSPITALINDETAIL;

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
     * @param directions
     * @param source_filter_artifact_id
     * @param target_filter_artifact_id
     * @param rmdid
     * @param session
     * @return
     */
    private String getNextView(Directions directions,
            String source_filter_artifact_id, String target_filter_artifact_id,
            String rmdid, HttpSession session) {
        //if request is not from apply filter or  failed shipment count link in repository mappings
        if (rmdid.equals("") && source_filter_artifact_id.equals("")
                && target_filter_artifact_id.equals("")) {
            if (FORWARD.equals(directions.name())) {
                return "redirect:/"
                        + UIPathConstants.LANDSCAPESETTINGS_DISPLAYHOSPITALTFTOPART;
            } else {
                return "redirect:/"
                        + UIPathConstants.LANDSCAPESETTINGS_DISPLAYHOSPITALPARTTOTF;
            }
        }
        //if the request is from apply filter redirect to LANDSCAPESETTINGS_APPLY_FILTER_IN_HOSPITAL
        else if (!source_filter_artifact_id.equals("")
                || !target_filter_artifact_id.equals("")) {
            session.setAttribute(FROM_EXTERNAL_FUNCTION, "true");
            return "redirect:"
                    + UIPathConstants.LANDSCAPESETTINGS_APPLY_FILTER_IN_HOSPITAL;
        }
        //if the request is from failed shipment count link in repository mappings, redirect to LANDSCAPESETTINGS_DISPLAY_HOSPITAL_BY_RMD
        else {
            return "redirect:/"
                    + UIPathConstants.LANDSCAPESETTINGS_DISPLAY_HOSPITAL_BY_RMD
                    + "?rmdid=" + rmdid;
        }
    }

    /**
     * @param model
     * @param directions
     * @param page
     * @param size
     * @return
     */
    private List<HospitalEntry> pageHospitalEntries(Model model,
            Directions directions, Integer page, Integer size,
            HttpSession session) {
        List<HospitalEntry> hospitalEntry;
        int sizeNo = size == null ? ControllerConstants.PAGINATION_SIZE : size
                .intValue();
        hospitalEntry = HospitalEntry
                .findHospitalEntrysByDirection(directions)
                .setFirstResult(
                        page == null ? 0 : (page.intValue() - 1) * sizeNo)
                .setMaxResults(sizeNo).getResultList();
        float nrOfPages = (float) HospitalEntry
                .countHospitalEntrysByDirection(directions) / sizeNo;
        model.addAttribute(
                "maxPages",
                (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
                        : nrOfPages));
        return hospitalEntry;
    }

    String doList(List<HospitalEntry> hospitalEntrys, Directions direction,
            Model model) {
        String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
        List<HospitalModel> hospitalModels = makeHospitalModel(hospitalEntrys,
                tfUrl, direction);
        Landscape landscape = ControllerHelper.findLandscape();
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("landscape", landscape);
        model.addAttribute("selectedLink", "hospital");
        model.addAttribute("direction", direction.name());
        model.addAttribute("hospitalmodel", hospitalModels);
        if (direction.name().equals(FORWARD)) {
            return UIPathConstants.LANDSCAPESETTINGS_DISPLAYHOSPITALTFTOPART;
        } else {
            return UIPathConstants.LANDSCAPESETTINGS_DISPLAYHOSPITALPARTTOTF;
        }
    }

    public static List<HospitalModel> makeHospitalModel(
            List<HospitalEntry> hospitalEntry, String tfUrl,
            Directions directions) {
        List<HospitalModel> hospitalModelList = new ArrayList<HospitalModel>(
                hospitalEntry.size());
        for (HospitalEntry entry : hospitalEntry) {
            HospitalModel hModel = modelFor(entry, tfUrl, directions);
            hospitalModelList.add(hModel);
        }
        return hospitalModelList;
    }

    public static HospitalModel modelFor(HospitalEntry entry, String tfUrl,
            Directions directions) {
        HospitalModel hModel = new HospitalModel();
        ArtifactDetail artifactDetail = null;
        RepositoryDetail repositoryDetail = RepositoryConnections
                .detailsForRepository(entry.getRepositoryMappingDirection()
                        .getRepositoryMapping().getTeamForgeRepositoryId());
        if (directions.name().equals(FORWARD)) {
            artifactDetail = RepositoryConnections.detailsForArtifact(entry
                    .getSourceArtifactId());
        } else {
            artifactDetail = RepositoryConnections.detailsForArtifact(entry
                    .getTargetArtifactId());
        }
        hModel.setArtifactDetail(artifactDetail);
        hModel.setRepositoryDetail(repositoryDetail);
        hModel.setTfUrl(tfUrl);
        hModel.setHospitalEntry(entry);
        return hModel;
    }

    public static void populatePageSizetoModel(Directions directions,
            Model model, HttpSession session) {
        Integer size = (Integer) session
                .getAttribute(ControllerConstants.SIZE_IN_SESSION) == null ? ControllerConstants.PAGINATION_SIZE
                : (Integer) session
                        .getAttribute(ControllerConstants.SIZE_IN_SESSION);
        float nrOfPages = (float) HospitalEntry
                .countHospitalEntrysByDirection(directions) / size.intValue();
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

}
