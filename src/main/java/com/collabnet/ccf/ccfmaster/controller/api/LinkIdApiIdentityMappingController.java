package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMappingList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.LINKID_IDENTITY_MAPPING)
public class LinkIdApiIdentityMappingController extends AbstractApiLinkIdController<IdentityMapping> {

    @Override
    public @ResponseBody
    IdentityMapping create(
            @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
            @RequestBody IdentityMapping requestBody,
            HttpServletResponse response) {
        validateIdentityMapping(requestBody);
        requestBody.persist();
        setLocationHeader(
                response,
                Paths.LINKID_IDENTITY_MAPPING.replace("{linkId}",
                        ea.getLinkId())
                        + "/" + requestBody.getId());
        return requestBody;
    }

    @Override
    public void delete(
            @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
            @PathVariable("id") Long id, HttpServletResponse response) {
        IdentityMapping rm = IdentityMapping.findIdentityMapping(id);
        validateIdentityMapping(rm);
        rm.remove();
    }

    @Override
    public @ResponseBody
    IdentityMappingList list(
            @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
        return new IdentityMappingList(IdentityMapping
                .findIdentityMappingsByExternalApp(ea).getResultList());
    }

    @Override
    public @ResponseBody
    IdentityMapping show(
            @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
            @PathVariable("id") Long id) {
        IdentityMapping rm = IdentityMapping.findIdentityMapping(id);
        validateIdentityMapping(rm);
        return rm;
    }

    @Override
    public void update(
            @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
            @PathVariable("id") Long id,
            @RequestBody IdentityMapping requestBody,
            HttpServletResponse response) {
        validateRequestBody(id, requestBody);
        validateIdentityMapping(requestBody);
        IdentityMapping original = IdentityMapping.findIdentityMapping(id);
        original.setVersion(requestBody.getVersion());
        original.setSourceArtifactVersion(requestBody
                .getSourceArtifactVersion());
        original.setSourceLastModificationTime(requestBody
                .getSourceLastModificationTime());
        original.setTargetLastModificationTime(requestBody
                .getTargetLastModificationTime());
        original.setTargetArtifactVersion(requestBody
                .getTargetArtifactVersion());
        original.merge();
    }

    private void validateIdentityMapping(IdentityMapping requestBody) {
        if (requestBody == null) {
            throw new DataRetrievalFailureException(
                    "requested entity not found.");
        }
        if (!externalApp.equals(requestBody.getRepositoryMapping()
                .getExternalApp())) {
            throw new AccessDeniedException(
                    "requestBody.externalApp != current external app.");
        }
    }

    private void validateRequestBody(Long id, IdentityMapping requestBody) {
        if (id == null || !id.equals(requestBody.getId())) {
            throw new BadRequestException(String.format(
                    "id (%s) != requestBody.id (%s)", id, requestBody.getId()));
        }
        // now retrieve original object to figure out whether reparenting attempt took place
        IdentityMapping original = IdentityMapping.findIdentityMapping(id);
        if (!original.getRepositoryMapping().equals(
                requestBody.getRepositoryMapping())) {
            throw new AccessDeniedException(
                    "requestBody.repositoryMapping != original entity's repository mapping.");
        }
    }

}
