package com.collabnet.ccf.ccfmaster.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfigList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.LINKID_REPOSITORY_MAPPING_DIRECTION_CONFIG)
public class LinkIdApiRepositoryMappingDirectionConfigController extends AbstractApiLinkIdController<RepositoryMappingDirectionConfig> {

    @Override
    public @ResponseBody
    RepositoryMappingDirectionConfig create(
            @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
            @RequestBody RepositoryMappingDirectionConfig requestBody,
            HttpServletResponse response) {
        validateRepositoryMappingDirectionConfig(requestBody);
        requestBody.persist();
        setLocationHeader(
                response,
                Paths.LINKID_REPOSITORY_MAPPING_DIRECTION_CONFIG.replace(
                        "{linkId}", ea.getLinkId()) + "/" + requestBody.getId());
        return requestBody;
    }

    @Override
    public void delete(
            @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
            @PathVariable("id") Long id, HttpServletResponse response) {
        RepositoryMappingDirectionConfig repositoryMappingDirectionConfig = RepositoryMappingDirectionConfig
                .findRepositoryMappingDirectionConfig(id);
        validateRepositoryMappingDirectionConfig(repositoryMappingDirectionConfig);
        repositoryMappingDirectionConfig.remove();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<RepositoryMappingDirectionConfig> list(
            @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
        return new RepositoryMappingDirectionConfigList(
                RepositoryMappingDirectionConfig
                        .findRepositoryMappingDirectionConfigByExternalApp(ea)
                        .getResultList());
    }

    @Override
    public @ResponseBody
    RepositoryMappingDirectionConfig show(
            @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
            @PathVariable("id") Long id) {
        RepositoryMappingDirectionConfig rm = RepositoryMappingDirectionConfig
                .findRepositoryMappingDirectionConfig(id);
        validateRepositoryMappingDirectionConfig(rm);
        return rm;
    }

    @Override
    public void update(
            @ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
            @PathVariable("id") Long id,
            @RequestBody RepositoryMappingDirectionConfig requestBody,
            HttpServletResponse response) {
        validateRequestBody(id, requestBody);
        validateRepositoryMappingDirectionConfig(requestBody);
        requestBody.merge();
    }

    private void validateRepositoryMappingDirectionConfig(
            RepositoryMappingDirectionConfig requestBody) {
        if (requestBody == null) {
            throw new DataRetrievalFailureException(
                    "requested entity not found.");
        }
        if (!externalApp.equals(requestBody.getRepositoryMappingDirection()
                .getRepositoryMapping().getExternalApp())) {
            throw new AccessDeniedException(
                    "requestBody.externalApp != current external app.");
        }
    }

    private void validateRequestBody(Long id,
            RepositoryMappingDirectionConfig requestBody) {
        if (id == null || !id.equals(requestBody.getId())) {
            throw new BadRequestException(String.format(
                    "id (%s) != requestBody.id (%s)", id, requestBody.getId()));
        }
        // now retrieve original object to figure out whether reparenting attempt took place
        RepositoryMappingDirectionConfig original = RepositoryMappingDirectionConfig
                .findRepositoryMappingDirectionConfig(id);
        if (!original.getRepositoryMappingDirection().getId()
                .equals(requestBody.getRepositoryMappingDirection().getId())) {
            throw new AccessDeniedException(
                    "requestBody.repositoryMappingDirection != original entity's repository mapping direction.");
        }
    }

}
