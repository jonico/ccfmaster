package com.collabnet.ccf.ccfmaster.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.google.common.base.Preconditions;

@Controller
@RequestMapping("/" + UpdateCoreController.UPDATE_CORE_PREFIX + "/{more}")
public class UpdateCoreController {

    static final String UPDATE_CORE_PREFIX = "update-core";

    @RequestMapping(method = RequestMethod.GET)
    public String get(@PathVariable("more") String more) {
        return UPDATE_CORE_PREFIX + "/" + more;
    }

    @ModelAttribute("landscape")
    public Landscape populateLandscape() {
        List<Landscape> landscapes = Landscape.findAllLandscapes();
        Preconditions.checkState(!landscapes.isEmpty(), "no landscape exists");
        return landscapes.get(0);
    }

    @ModelAttribute("participant")
    public Participant populateParticipant() {
        Landscape landscape = populateLandscape();
        return landscape.getParticipant();
    }

    @ModelAttribute("selectedLink")
    public String populateSelectedLink() {
        return "status";
    }
}
