package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.FIELD_MAPPING)
public class ApiFieldMappingController extends AbstractApiController<FieldMapping> {

    @Override
    public @ResponseBody
    FieldMapping create(@RequestBody FieldMapping requestBody,
            HttpServletResponse response) {
        requestBody.persist();
        setLocationHeader(response,
                Paths.FIELD_MAPPING + "/" + requestBody.getId());
        return requestBody;
    }

    @Override
    public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
        FieldMapping.findFieldMapping(id).remove();
    }

    @Override
    public @ResponseBody
    FieldMappingList list() {
        return new FieldMappingList(FieldMapping.findAllFieldMappings());
    }

    @Override
    public @ResponseBody
    FieldMapping show(@PathVariable("id") FieldMapping id) {
        return super.show(id);
    }

    @Override
    public void update(@PathVariable("id") Long id,
            @RequestBody FieldMapping requestBody, HttpServletResponse response) {
        validateRequestBody(id, requestBody);
        requestBody.merge();
    }

    private void validateRequestBody(Long id, FieldMapping requestBody) {
        if (id == null || !id.equals(requestBody.getId())) {
            throw new BadRequestException(String.format(
                    "id (%s) != requestBody.id (%s)", id, requestBody.getId()));
        }
    }

}
