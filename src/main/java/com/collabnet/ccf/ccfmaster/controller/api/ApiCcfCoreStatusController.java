package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatusList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.CCF_CORE_STATUS)
public class ApiCcfCoreStatusController extends AbstractApiController<CcfCoreStatus> {

    @Override
    public @ResponseBody
    CcfCoreStatus create(@RequestBody CcfCoreStatus requestBody,
            HttpServletResponse response) {
        throw new BadRequestException("create not supported");
    }

    @Override
    public void delete(Long id, HttpServletResponse response) {
        throw new BadRequestException("delete not supported.");
    }

    @Override
    public @ResponseBody
    CcfCoreStatusList list() {
        return new CcfCoreStatusList(CcfCoreStatus.findAllCcfCoreStatuses());
    }

    @Override
    public @ResponseBody
    CcfCoreStatus show(@PathVariable("id") CcfCoreStatus id) {
        return super.show(id);
    }

    @Override
    public void update(@PathVariable("id") Long id,
            @RequestBody CcfCoreStatus requestBody, HttpServletResponse response) {
        validateRequestBody(id, requestBody);
        requestBody.merge();
    }

    private void validateRequestBody(Long id, CcfCoreStatus requestBody) {
        if (id == null || !id.equals(requestBody.getId())) {
            throw new BadRequestException(
                    String.format("id (%s) != ccfCoreStatus.id (%s)", id,
                            requestBody.getId()));
        }
    }
}
