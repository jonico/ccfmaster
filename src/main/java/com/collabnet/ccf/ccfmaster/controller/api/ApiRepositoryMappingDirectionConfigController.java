package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfigList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.REPOSITORY_MAPPING_DIRECTION_CONFIG)
public class ApiRepositoryMappingDirectionConfigController extends AbstractApiController<RepositoryMappingDirectionConfig> {

    @Override
    public @ResponseBody
    RepositoryMappingDirectionConfig create(
            @RequestBody RepositoryMappingDirectionConfig requestBody,
            HttpServletResponse response) {
        requestBody.persist();
        setLocationHeader(response, Paths.REPOSITORY_MAPPING_DIRECTION_CONFIG
                + "/" + requestBody.getId());
        return requestBody;
    }

    @Override
    public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
        RepositoryMappingDirectionConfig.findRepositoryMappingDirectionConfig(
                id).remove();
    }

    @Override
    public @ResponseBody
    RepositoryMappingDirectionConfigList list() {
        return new RepositoryMappingDirectionConfigList(
                RepositoryMappingDirectionConfig
                        .findAllRepositoryMappingDirectionConfigs());
    }

    @RequestMapping(value = "/{repositoryMappingDirection}/")
    public @ResponseBody
    RepositoryMappingDirectionConfigList repositoryMappingDirectionConfigs(
            @PathVariable("repositoryMappingDirection") RepositoryMappingDirection repositoryMappingDirection) {
        return new RepositoryMappingDirectionConfigList(
                RepositoryMappingDirectionConfig
                        .findRepositoryMappingDirectionConfigsByRepositoryMappingDirection(
                                repositoryMappingDirection).getResultList());
    }

    @Override
    public @ResponseBody
    RepositoryMappingDirectionConfig show(
            @PathVariable("id") RepositoryMappingDirectionConfig id) {
        return super.show(id);
    }

    @Override
    public void update(@PathVariable("id") Long id,
            @RequestBody RepositoryMappingDirectionConfig requestBody,
            HttpServletResponse response) {
        validateRequestBody(id, requestBody);
        requestBody.merge();
    }

    private void validateRequestBody(Long id,
            RepositoryMappingDirectionConfig requestBody) {
        if (id == null || !id.equals(requestBody.getId())) {
            throw new BadRequestException(String.format(
                    "id (%s) != requestBody.id (%s)", id, requestBody.getId()));
        }
    }
}
