package com.collabnet.ccf.ccfmaster.controller.web.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.controller.api.BadRequestException;
import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.ArtifactDetail;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.RepositoryDetail;
import com.collabnet.ccf.ccfmaster.web.model.IdentityMappingsModel;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@Controller
@RequestMapping(ProjectIdentityMappingsController.PROJECT_IDENTITY_MAPPINGS_PATH)
public class ProjectIdentityMappingsController extends AbstractProjectController {

    protected static final String    PAGE_SIZE_REQUEST_PARAM           = "size";
    private static final String      EXTERNAL_LINK                     = "externalLink";
    public static final String       PROJECT_IDENTITY_MAPPINGS_NAME    = "project/identitymappings";
    public static final String       PROJECT_IDENTITY_MAPPINGS_PATH    = "/"
                                                                               + PROJECT_IDENTITY_MAPPINGS_NAME;
    private static final String      MAPPING_ID_REQUEST_PARAM          = "mappingid";
    private static final String      RMD_ID_REQUEST_PARAM              = "rmdid";
    private static final String      IDENTITY_MAPPING_ID_REQUEST_PARAM = "idmapping.id";
    private static final String      TARGET_FILTER_ARTIFACT_ID         = "targetfilterartifactid";
    private static final String      SOURCE_FILTER_ARTIFACT_ID         = "sourcefilterartifactid";

    private static final Logger      log                               = LoggerFactory
                                                                               .getLogger(ProjectIdentityMappingsController.class);

    @Autowired
    private CCFRuntimePropertyHolder ccfRuntimePropertyHolder;

    @RequestMapping("/applyfilter")
    public String applyFilter(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
            @RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
            Model model, HttpServletRequest request, HttpSession session) {
        String fromExternalLink = (String) session.getAttribute(EXTERNAL_LINK);
        String sourceFilterArtifactId = null;
        String targetFilterArtifactId = null;
        //to check the request is after deleting identity mapping or  return from identity mapping details screen.get the artifactids to repopulate
        if ("true".equals(fromExternalLink)) {
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
        List<IdentityMapping> filterIdentityMappingEntrys = new ArrayList<IdentityMapping>();
        //if no input entered for source and target artifactid, and if filter is applied.display the default entries
        if ("".equals(sourceFilterArtifactId)
                && "".equals(targetFilterArtifactId)) {
            filterIdentityMappingEntrys = paginate(
                    IdentityMapping.findIdentityMappingsByExternalApp(ea),
                    IdentityMapping.countIdentityMappingsByExternalApp(ea),
                    page, size, model).getResultList();
        } else {
            List<IdentityMapping> identityMappingEntrys = paginate(
                    IdentityMapping.findIdentityMappingsBySourceArtifactIdOrTargetArtifactId(
                            sourceFilterArtifactId, targetFilterArtifactId),
                    IdentityMapping
                            .countIdentityMappingsByExternalAppAndSourceArtifactIdOrTargetArtifactId(
                                    ea, sourceFilterArtifactId,
                                    targetFilterArtifactId), page, size, model)
                    .getResultList();
            filterIdentityMappingEntrys = filterEntrys(ea,
                    identityMappingEntrys);
        }
        session.setAttribute("sourceFilterArtifactId", sourceFilterArtifactId);
        session.setAttribute("targetFilterArtifactId", targetFilterArtifactId);
        model.addAttribute("sourceFilterArtifactId", sourceFilterArtifactId);
        model.addAttribute("targetFilterArtifactId", targetFilterArtifactId);
        return doList(filterIdentityMappingEntrys, model);

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(RMD_ID_REQUEST_PARAM) String rmdid,
            @RequestParam(SOURCE_FILTER_ARTIFACT_ID) String source_filter_artifact_id,
            @RequestParam(TARGET_FILTER_ARTIFACT_ID) String target_filter_artifact_id,
            @RequestParam(MAPPING_ID_REQUEST_PARAM) IdentityMapping[] entries,
            Model model, HttpSession session) {
        try {
            Iterable<IdentityMapping> validEntries = Iterables.filter(
                    Arrays.asList(entries), isValidIdentityMappingEntry(ea));
            for (IdentityMapping entry : validEntries) {
                entry.remove();
            }
            FlashMap.setSuccessMessage(ControllerConstants.IDENTITY_DELETE_SUCCESS_MESSAGE);
        } catch (Exception e) {
            FlashMap.setErrorMessage(
                    ControllerConstants.IDENTITY_DELETE_FAILURE_MESSAGE,
                    e.getMessage());
        }
        populatePageSizetoModel(ea, model, session);
        return getNextView(rmdid, source_filter_artifact_id,
                target_filter_artifact_id, session);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
            @RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
            Model model, HttpSession session) {
        cleanSession(session);
        session.setAttribute(ControllerConstants.SIZE_IN_SESSION, size);
        session.setAttribute(ControllerConstants.PAGE_IN_SESSION, page);
        List<IdentityMapping> identityMappingEntrys = paginate(
                IdentityMapping.findIdentityMappingsByExternalApp(ea),
                IdentityMapping.countIdentityMappingsByExternalApp(ea), page,
                size, model).getResultList();
        return doList(identityMappingEntrys, model);

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
        final RepositoryMapping repositoryMapping = rmd.getRepositoryMapping();
        List<IdentityMapping> identityMappingEntrys = paginate(
                IdentityMapping
                        .findIdentityMappingsByRepositoryMapping(repositoryMapping),
                IdentityMapping
                        .countIdentityMappingsByRepositoryMapping(repositoryMapping),
                page, size, model).getResultList();
        model.addAttribute("rmdid", rmd.getId());
        return doList(identityMappingEntrys, model);
    }

    @RequestMapping("/save")
    public String saveDetails(
            @RequestParam(IDENTITY_MAPPING_ID_REQUEST_PARAM) String identityMappingId,
            @RequestParam(RMD_ID_REQUEST_PARAM) String rmdid,
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            IdentityMappingsModel identityMappingsModel, Model model,
            HttpServletRequest request, HttpSession session) {
        String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
        try {
            mergeIdentitymappingdetails(identityMappingId,
                    identityMappingsModel);
            FlashMap.setSuccessMessage(ControllerConstants.IDENTITY_MAPPING_SAVE_SUCCESS_MESSAGE);
        } catch (Exception exception) {
            log.debug(
                    "Error updating identity mapping details: "
                            + exception.getMessage(), exception);
            FlashMap.setErrorMessage(
                    ControllerConstants.IDENTITY_MAPPING_SAVE_FAIL_MESSAGE,
                    exception.getMessage());
        }
        List<IdentityMapping> identityMappingEntrys = IdentityMapping
                .findIdentityMappingsByExternalApp(ea).getResultList();
        List<IdentityMappingsModel> identitymappingsmodel = makeIdentityMappingModel(
                identityMappingEntrys, tfUrl);
        populateIdentityMappingModel(model, identitymappingsmodel);
        model.addAttribute("rmdid", rmdid);
        model.addAttribute("mappingid", identityMappingId);
        populatePageSizetoModel(ea, model, session);
        //model.asMap().clear();
        return "redirect:" + PROJECT_IDENTITY_MAPPINGS_PATH + "/details";
    }

    @RequestMapping("/details")
    public String showDetails(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(MAPPING_ID_REQUEST_PARAM) IdentityMapping entry,
            @RequestParam(RMD_ID_REQUEST_PARAM) String rmdid, Model model,
            HttpSession session) {
        String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
        IdentityMappingsModel identityMappingsModel = modelFor(entry, tfUrl);
        model.addAttribute("rmdid", rmdid);
        model.addAttribute("identitymappingsmodel", identityMappingsModel);
        model.addAttribute("idmappingversion", identityMappingsModel
                .getIdentityMappingEntry().getVersion());
        model.addAttribute("idmappingid", identityMappingsModel
                .getIdentityMappingEntry().getId());
        populatePageSizetoModel(ea, model, session);
        return PROJECT_IDENTITY_MAPPINGS_NAME + "/details";
    }

    /**
     * @param session
     */
    private void cleanSession(HttpSession session) {
        session.removeAttribute("sourceFilterArtifactId");
        session.removeAttribute("targetFilterArtifactId");
        session.removeAttribute(EXTERNAL_LINK);
    }

    /**
     * @param ea
     * @param identityMappingEntrys
     * @return
     */
    private List<IdentityMapping> filterEntrys(ExternalApp ea,
            List<IdentityMapping> identityMappingEntrys) {
        Iterable<IdentityMapping> validEntries = Iterables.filter(
                identityMappingEntrys, isValidIdentityMappingEntry(ea));
        List<IdentityMapping> filterIdentityMappingEntrys = new ArrayList<IdentityMapping>();
        for (IdentityMapping entry : validEntries) {
            filterIdentityMappingEntrys.add(entry);
        }
        return filterIdentityMappingEntrys;
    }

    /**
     * @param rmdid
     * @param source_filter_artifact_id
     * @param target_filter_artifact_id
     * @param session
     * @return
     */
    private String getNextView(String rmdid, String source_filter_artifact_id,
            String target_filter_artifact_id, HttpSession session) {
        if (rmdid.equals("") && source_filter_artifact_id.equals("")
                && target_filter_artifact_id.equals("")) {
            return "redirect:" + PROJECT_IDENTITY_MAPPINGS_PATH;
        } else if (!source_filter_artifact_id.equals("")
                || !target_filter_artifact_id.equals("")) {
            session.setAttribute(EXTERNAL_LINK, "true");
            return "redirect:" + PROJECT_IDENTITY_MAPPINGS_PATH
                    + "/applyfilter";
        } else {
            return "redirect:" + PROJECT_IDENTITY_MAPPINGS_PATH + "?rmdid="
                    + rmdid;
        }
    }

    /**
     * @param identityMappingId
     * @param identityMappingsModel
     */
    private void mergeIdentitymappingdetails(String identityMappingId,
            IdentityMappingsModel identityMappingsModel) {
        IdentityMapping identityMappingEntry = identityMappingsModel
                .getIdentityMappingEntry();
        IdentityMapping identityMappingEntryForMerge = IdentityMapping
                .findIdentityMapping(new Long(identityMappingId));
        identityMappingEntryForMerge
                .setSourceArtifactVersion(identityMappingEntry
                        .getSourceArtifactVersion());
        identityMappingEntryForMerge
                .setTargetArtifactVersion(identityMappingEntry
                        .getTargetArtifactVersion());
        identityMappingEntryForMerge.merge();
    }

    /**
     * @param model
     * @param identitymappingsmodel
     */
    private void populateIdentityMappingModel(Model model,
            List<IdentityMappingsModel> identitymappingsmodel) {
        Landscape landscape = ControllerHelper.findLandscape();
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("landscape", landscape);
        model.addAttribute("selectedLink", "identitymappings");
        model.addAttribute("identitymappingsmodel", identitymappingsmodel);
    }

    String doList(List<IdentityMapping> identityMappingEntrys, Model model) {
        String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
        List<IdentityMappingsModel> identitymappingsmodel = makeIdentityMappingModel(
                identityMappingEntrys, tfUrl);
        populateIdentityMappingModel(model, identitymappingsmodel);
        return PROJECT_IDENTITY_MAPPINGS_NAME;
    }

    void validateRepositoryMappingDirection(ExternalApp ea,
            RepositoryMappingDirection rmd) {
        if (!ea.getId().equals(
                rmd.getRepositoryMapping().getExternalApp().getId()))
            throw new BadRequestException("wrong external app.");
    }

    public static List<IdentityMappingsModel> makeIdentityMappingModel(
            List<IdentityMapping> identityMappingEntrys, String tfUrl) {
        List<IdentityMappingsModel> identityMappingsModelList = new ArrayList<IdentityMappingsModel>(
                identityMappingEntrys.size());
        for (IdentityMapping entry : identityMappingEntrys) {
            IdentityMappingsModel identityMappingsModel = modelFor(entry, tfUrl);
            identityMappingsModelList.add(identityMappingsModel);
        }
        return identityMappingsModelList;
    }

    public static IdentityMappingsModel modelFor(IdentityMapping entry,
            String tfUrl) {
        IdentityMappingsModel idmModel = new IdentityMappingsModel();
        ArtifactDetail sourceArtifactDetail = null;
        ArtifactDetail targetArtifactDetail = null;
        RepositoryDetail repositoryDetail = RepositoryConnections
                .detailsForRepository(entry.getRepositoryMapping()
                        .getTeamForgeRepositoryId());
        sourceArtifactDetail = RepositoryConnections.detailsForArtifact(entry
                .getSourceArtifactId());
        targetArtifactDetail = RepositoryConnections.detailsForArtifact(entry
                .getTargetArtifactId());
        idmModel.setSourceArtifactDetail(sourceArtifactDetail);
        idmModel.setTargetArtifactDetail(targetArtifactDetail);
        idmModel.setRepositoryDetail(repositoryDetail);
        idmModel.setTfUrl(tfUrl);
        idmModel.setIdentityMappingEntry(entry);
        return idmModel;
    }

    public static void populatePageSizetoModel(ExternalApp ea, Model model,
            HttpSession session) {
        Integer size = (Integer) session
                .getAttribute(ControllerConstants.SIZE_IN_SESSION) == null ? ControllerConstants.PAGINATION_SIZE
                : (Integer) session
                        .getAttribute(ControllerConstants.SIZE_IN_SESSION);
        float nrOfPages = (float) IdentityMapping
                .countIdentityMappingsByExternalApp(ea) / size.intValue();
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

    static Predicate<IdentityMapping> isValidIdentityMappingEntry(
            final ExternalApp ea) {
        return new Predicate<IdentityMapping>() {
            @Override
            public boolean apply(IdentityMapping input) {
                if (input == null)
                    return false;
                final ExternalApp externalApp = input.getRepositoryMapping()
                        .getExternalApp();
                return ea.getId().equals(externalApp.getId());
            }

        };
    }

}
