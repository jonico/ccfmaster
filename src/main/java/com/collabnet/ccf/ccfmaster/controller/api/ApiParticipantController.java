package com.collabnet.ccf.ccfmaster.controller.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfigList;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantList;

@Controller
@Scope("request")
@RequestMapping(value = Paths.PARTICIPANT)
public class ApiParticipantController extends AbstractApiController<Participant> {

    @Override
    public @ResponseBody
    Participant create(@RequestBody Participant requestBody,
            HttpServletResponse response) {
        requestBody.persist();
        setLocationHeader(response,
                Paths.PARTICIPANT + "/" + requestBody.getId());
        return requestBody;
    }

    @Override
    public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
        Participant.findParticipant(id).remove();
    }

    @Override
    public @ResponseBody
    ParticipantList list() {
        return new ParticipantList(Participant.findAllParticipants());
    }

    @RequestMapping(value = "/{id}/participantconfigs", method = RequestMethod.GET)
    public @ResponseBody
    ParticipantConfigList participantConfigs(
            @PathVariable("id") Participant participant) {
        return new ParticipantConfigList(ParticipantConfig
                .findParticipantConfigsByParticipant(participant)
                .getResultList());
    }

    @Override
    public @ResponseBody
    Participant show(@PathVariable("id") Participant id) {
        return super.show(id);
    }

    @Override
    public void update(@PathVariable("id") Long id,
            @RequestBody Participant requestBody, HttpServletResponse response) {
        validateRequestBody(id, requestBody);
        requestBody.merge();
    }

    private void validateRequestBody(Long id, Participant requestBody) {
        if (id == null || !id.equals(requestBody.getId())) {
            throw new BadRequestException(String.format(
                    "id (%s) != requestBody.id (%s)", id, requestBody.getId()));
        }
    }
}
