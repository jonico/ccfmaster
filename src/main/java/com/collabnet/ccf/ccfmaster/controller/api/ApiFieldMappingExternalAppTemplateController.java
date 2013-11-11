package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplateList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.FIELD_MAPPING_EXTERNAL_APP_TEMPLATE)
public class ApiFieldMappingExternalAppTemplateController extends AbstractApiController<FieldMappingExternalAppTemplate> {

    @Override
    public @ResponseBody
    FieldMappingExternalAppTemplate create(
            @RequestBody FieldMappingExternalAppTemplate requestBody,
            HttpServletResponse response) {
        requestBody.persist();
        setLocationHeader(response, Paths.FIELD_MAPPING_EXTERNAL_APP_TEMPLATE
                + "/" + requestBody.getId());
        return requestBody;
    }

    @Override
    public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
        FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplate(id)
                .remove();
    }

    @Override
    public @ResponseBody
    FieldMappingExternalAppTemplateList list() {
        return new FieldMappingExternalAppTemplateList(
                FieldMappingExternalAppTemplate
                        .findAllFieldMappingExternalAppTemplates());
    }

    @Override
    public @ResponseBody
    FieldMappingExternalAppTemplate show(
            @PathVariable("id") FieldMappingExternalAppTemplate id) {
        return super.show(id);
    }

    @Override
    public void update(@PathVariable("id") Long id,
            @RequestBody FieldMappingExternalAppTemplate requestBody,
            HttpServletResponse response) {
        validateRequestBody(id, requestBody);
        requestBody.merge();
    }

    private void validateRequestBody(Long id,
            FieldMappingExternalAppTemplate requestBody) {
        if (id == null || !id.equals(requestBody.getId())) {
            throw new BadRequestException(String.format(
                    "id (%s) != requestBody.id (%s)", id, requestBody.getId()));
        }
    }

}
