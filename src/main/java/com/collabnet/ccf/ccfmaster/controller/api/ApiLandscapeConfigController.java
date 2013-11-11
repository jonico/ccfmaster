package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfigList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.LANDSCAPE_CONFIG)
public class ApiLandscapeConfigController extends AbstractApiController<LandscapeConfig> {

    @Override
    public @ResponseBody
    LandscapeConfig create(@RequestBody LandscapeConfig requestBody,
            HttpServletResponse response) {
        requestBody.persist();
        setLocationHeader(response,
                Paths.LANDSCAPE_CONFIG + "/" + requestBody.getId());
        return requestBody;
    }

    @Override
    public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
        LandscapeConfig.findLandscapeConfig(id).remove();
    }

    @Override
    public @ResponseBody
    LandscapeConfigList list() {
        return new LandscapeConfigList(
                LandscapeConfig.findAllLandscapeConfigs());
    }

    @Override
    public @ResponseBody
    LandscapeConfig show(@PathVariable("id") LandscapeConfig id) {
        return super.show(id);
    }

    @Override
    public void update(@PathVariable("id") Long id,
            @RequestBody LandscapeConfig requestBody,
            HttpServletResponse response) {
        validateRequestBody(id, requestBody);
        requestBody.merge();
    }

    private void validateRequestBody(Long id, LandscapeConfig requestBody) {
        if (id == null || !id.equals(requestBody.getId())) {
            throw new BadRequestException(String.format(
                    "id (%s) != requestBody.id (%s)", id, requestBody.getId()));
        }
    }
}
