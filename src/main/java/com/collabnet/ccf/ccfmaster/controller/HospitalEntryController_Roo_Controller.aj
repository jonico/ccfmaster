// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect HospitalEntryController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String HospitalEntryController.create(@Valid HospitalEntry hospitalEntry, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("hospitalEntry", hospitalEntry);
            addDateTimeFormatPatterns(uiModel);
            return "hospitalentrys/create";
        }
        uiModel.asMap().clear();
        hospitalEntry.persist();
        return "redirect:/hospitalentrys/" + encodeUrlPathSegment(hospitalEntry.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String HospitalEntryController.createForm(Model uiModel) {
        uiModel.addAttribute("hospitalEntry", new HospitalEntry());
        addDateTimeFormatPatterns(uiModel);
        List dependencies = new ArrayList();
        if (RepositoryMappingDirection.countRepositoryMappingDirections() == 0) {
            dependencies.add(new String[]{"repositorymappingdirection", "repositorymappingdirections"});
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "hospitalentrys/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String HospitalEntryController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("hospitalentry", HospitalEntry.findHospitalEntry(id));
        uiModel.addAttribute("itemId", id);
        return "hospitalentrys/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String HospitalEntryController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("hospitalentrys", HospitalEntry.findHospitalEntryEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) HospitalEntry.countHospitalEntrys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("hospitalentrys", HospitalEntry.findAllHospitalEntrys());
        }
        addDateTimeFormatPatterns(uiModel);
        return "hospitalentrys/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String HospitalEntryController.update(@Valid HospitalEntry hospitalEntry, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("hospitalEntry", hospitalEntry);
            addDateTimeFormatPatterns(uiModel);
            return "hospitalentrys/update";
        }
        uiModel.asMap().clear();
        hospitalEntry.merge();
        return "redirect:/hospitalentrys/" + encodeUrlPathSegment(hospitalEntry.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String HospitalEntryController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("hospitalEntry", HospitalEntry.findHospitalEntry(id));
        addDateTimeFormatPatterns(uiModel);
        return "hospitalentrys/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String HospitalEntryController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        HospitalEntry.findHospitalEntry(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/hospitalentrys";
    }
    
    @ModelAttribute("hospitalentrys")
    public Collection<HospitalEntry> HospitalEntryController.populateHospitalEntrys() {
        return HospitalEntry.findAllHospitalEntrys();
    }
    
    @ModelAttribute("repositorymappingdirections")
    public java.util.Collection<RepositoryMappingDirection> HospitalEntryController.populateRepositoryMappingDirections() {
        return RepositoryMappingDirection.findAllRepositoryMappingDirections();
    }
    
    void HospitalEntryController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("hospitalEntry_targetlastmodificationtime_date_format", DateTimeFormat.patternForStyle("SS", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("hospitalEntry_sourcelastmodificationtime_date_format", DateTimeFormat.patternForStyle("SS", LocaleContextHolder.getLocale()));
    }
    
    String HospitalEntryController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
