package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.REPOSITORY_MAPPING)
public class ApiRepositoryMappingController extends AbstractApiController<RepositoryMapping> {

    @Override
    public @ResponseBody
    RepositoryMapping create(@RequestBody RepositoryMapping requestBody,
            HttpServletResponse response) {
        requestBody.persist();
        setLocationHeader(response, Paths.REPOSITORY_MAPPING + "/"
                + requestBody.getId());
        return requestBody;
    }

    @Override
    public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
        RepositoryMapping.findRepositoryMapping(id).remove();
    }

    @Override
    public @ResponseBody
    RepositoryMappingList list() {
        return new RepositoryMappingList(
                RepositoryMapping.findAllRepositoryMappings());
    }

    @Override
    public @ResponseBody
    RepositoryMapping show(@PathVariable("id") RepositoryMapping id) {
        return super.show(id);
    }

    @Override
    public void update(@PathVariable("id") Long id,
            @RequestBody RepositoryMapping requestBody,
            HttpServletResponse response) {
        validateRequestBody(id, requestBody);
        requestBody.merge();
    }

    private void validateRequestBody(Long id, RepositoryMapping requestBody) {
        if (id == null || !id.equals(requestBody.getId())) {
            throw new BadRequestException(String.format(
                    "id (%s) != requestBody.id (%s)", id, requestBody.getId()));
        }
    }
}
