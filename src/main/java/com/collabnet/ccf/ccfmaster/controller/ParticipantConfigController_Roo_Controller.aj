// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect ParticipantConfigController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String ParticipantConfigController.create(@Valid ParticipantConfig participantConfig, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("participantConfig", participantConfig);
            return "participantconfigs/create";
        }
        uiModel.asMap().clear();
        participantConfig.persist();
        return "redirect:/participantconfigs/" + encodeUrlPathSegment(participantConfig.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String ParticipantConfigController.createForm(Model uiModel) {
        uiModel.addAttribute("participantConfig", new ParticipantConfig());
        List dependencies = new ArrayList();
        if (Participant.countParticipants() == 0) {
            dependencies.add(new String[]{"participant", "participants"});
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "participantconfigs/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String ParticipantConfigController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("participantconfig", ParticipantConfig.findParticipantConfig(id));
        uiModel.addAttribute("itemId", id);
        return "participantconfigs/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String ParticipantConfigController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("participantconfigs", ParticipantConfig.findParticipantConfigEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) ParticipantConfig.countParticipantConfigs() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("participantconfigs", ParticipantConfig.findAllParticipantConfigs());
        }
        return "participantconfigs/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String ParticipantConfigController.update(@Valid ParticipantConfig participantConfig, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("participantConfig", participantConfig);
            return "participantconfigs/update";
        }
        uiModel.asMap().clear();
        participantConfig.merge();
        return "redirect:/participantconfigs/" + encodeUrlPathSegment(participantConfig.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String ParticipantConfigController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("participantConfig", ParticipantConfig.findParticipantConfig(id));
        return "participantconfigs/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String ParticipantConfigController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ParticipantConfig.findParticipantConfig(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/participantconfigs";
    }
    
    @ModelAttribute("participants")
    public Collection<Participant> ParticipantConfigController.populateParticipants() {
        return Participant.findAllParticipants();
    }
    
    @ModelAttribute("participantconfigs")
    public java.util.Collection<ParticipantConfig> ParticipantConfigController.populateParticipantConfigs() {
        return ParticipantConfig.findAllParticipantConfigs();
    }
    
    String ParticipantConfigController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        }
        catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
