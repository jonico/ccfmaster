package com.collabnet.ccf.ccfmaster.controller;

import java.util.Collection;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;

@RooWebScaffold(path = "linkid/hospitalentrys", formBackingObject = HospitalEntry.class)
@RequestMapping("/linkid/hospitalentrys")
@Controller
public class LinkIdHospitalEntryController extends AbstractLinkIdController {

	@PreAuthorize("#ea == #hospitalEntry.repositoryMappingDirection.repositoryMapping.externalApp")
	@RequestMapping(method = RequestMethod.POST)
    public String create(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @Valid HospitalEntry hospitalEntry, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("hospitalEntry", hospitalEntry);
            addDateTimeFormatPatterns(model);
            return "linkid/hospitalentrys/create";
        }
        hospitalEntry.persist();
        return "redirect:/linkid/" + ea.getLinkId() + "/hospitalentrys/" + encodeUrlPathSegment(hospitalEntry.getId().toString(), request);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, Model model) {
        addDateTimeFormatPatterns(model);
        final HospitalEntry hospitalEntry = HospitalEntry.findHospitalEntry(id);
        validateRepositoryMapping(hospitalEntry.getRepositoryMappingDirection().getRepositoryMapping(), ea);
		model.addAttribute("hospitalentry", hospitalEntry);
        model.addAttribute("itemId", id);
        return "linkid/hospitalentrys/show";
    }


	@RequestMapping(method = RequestMethod.GET)
    public String list(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
		TypedQuery<HospitalEntry> query = HospitalEntry.findHospitalEntrysByExternalApp(ea);
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            query.setFirstResult(firstResult);
            query.setMaxResults(sizeNo);
            float nrOfPages = (float) HospitalEntry.countHospitalEntrysByExternalApp(ea) / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        }
        model.addAttribute("hospitalentrys", query.getResultList());
        addDateTimeFormatPatterns(model);
        return "linkid/hospitalentrys/list";
    }

	@PreAuthorize("#ea == #hospitalEntry.repositoryMappingDirection.repositoryMapping.externalApp")
	@RequestMapping(method = RequestMethod.PUT)
    public String update(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @Valid HospitalEntry hospitalEntry, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("hospitalEntry", hospitalEntry);
            addDateTimeFormatPatterns(model);
            return "linkid/hospitalentrys/update";
        }
        hospitalEntry.merge();
        return "redirect:/linkid/" + ea.getLinkId() + "/hospitalentrys/" + encodeUrlPathSegment(hospitalEntry.getId().toString(), request);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, Model model) {
        final HospitalEntry hospitalEntry = HospitalEntry.findHospitalEntry(id);
        validateRepositoryMapping(hospitalEntry.getRepositoryMappingDirection().getRepositoryMapping(), ea);
		model.addAttribute("hospitalEntry", hospitalEntry);
        addDateTimeFormatPatterns(model);
        return "linkid/hospitalentrys/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        final HospitalEntry hospitalEntry = HospitalEntry.findHospitalEntry(id);
        validateRepositoryMapping(hospitalEntry.getRepositoryMappingDirection().getRepositoryMapping(), ea);
		hospitalEntry.remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/linkid/" + ea.getLinkId() + "/hospitalentrys?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }

	@ModelAttribute("repositorymappingdirections")
    public Collection<RepositoryMappingDirection> populateRepositoryMappingDirections(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
        return RepositoryMappingDirection.findRepositoryMappingDirectionsByExternalApp(ea).getResultList();
    }

    //Collection<RepositoryMappingDirection> populateRepositoryMappingDirections() {
    //	throw new UnsupportedOperationException();
    //}

    //String create(@Valid HospitalEntry hospitalEntry, BindingResult result, Model model, HttpServletRequest request) {
    //	throw new UnsupportedOperationException();
    //}

    //String update(@Valid HospitalEntry hospitalEntry, BindingResult result, Model model, HttpServletRequest request) {
    //	throw new UnsupportedOperationException();
    //}

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid HospitalEntry hospitalEntry, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		throw new UnsupportedOperationException();
    }

	//@ModelAttribute("repositorymappingdirections")
    public java.util.Collection<RepositoryMappingDirection> populateRepositoryMappingDirections() {
    	throw new UnsupportedOperationException();
    }

	//@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid HospitalEntry hospitalEntry, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	throw new UnsupportedOperationException();
    }
}
