package com.collabnet.ccf.ccfmaster.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;

//@RooWebScaffold(path = "linkid/repositorymappingdirections", formBackingObject = RepositoryMappingDirection.class)
@RequestMapping("/linkid/{linkId}/repositorymappingdirections")
@Controller
public class LinkIdRepositoryMappingDirectionController extends AbstractLinkIdController {

	@PreAuthorize("#ea == #repositoryMappingDirection.repositoryMapping.externalApp")
	@RequestMapping(method = RequestMethod.POST)
    public String create(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		@Valid RepositoryMappingDirection repositoryMappingDirection,
    		BindingResult result,
    		Model model,
    		HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("repositoryMappingDirection", repositoryMappingDirection);
            addDateTimeFormatPatterns(model);
            return "linkid/repositorymappingdirections/create";
        }
        repositoryMappingDirection.persist();
        return "redirect:/linkid/" + ea.getLinkId() + "/repositorymappingdirections/" + encodeUrlPathSegment(repositoryMappingDirection.getId().toString(), request);
    }

	@SuppressWarnings("unchecked")
	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		Model model) {
        model.addAttribute("repositoryMappingDirection", new RepositoryMappingDirection());
        addDateTimeFormatPatterns(model);
		@SuppressWarnings("rawtypes")
		List dependencies = new ArrayList();
        if (RepositoryMapping.countRepositoryMappingsByExternalApp(ea) == 0) {
            dependencies.add(new String[]{"repositoryMapping", "repositorymappings"});
        }
        model.addAttribute("dependencies", dependencies);
        return "linkid/repositorymappingdirections/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		@PathVariable("id") Long id,
    		Model model) {
		final RepositoryMappingDirection rmd = RepositoryMappingDirection.findRepositoryMappingDirection(id);
		validateRepositoryMapping(rmd.getRepositoryMapping(), ea);
        addDateTimeFormatPatterns(model);
        model.addAttribute("repositorymappingdirection", rmd);
        model.addAttribute("itemId", id);
        return "linkid/repositorymappingdirections/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, 
    		@RequestParam(value = "page", required = false) Integer page,
    		@RequestParam(value = "size", required = false) Integer size,
    		Model model) {
        final TypedQuery<RepositoryMappingDirection> query = RepositoryMappingDirection.findRepositoryMappingDirectionsByExternalApp(ea);
		if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            query.setFirstResult(firstResult);
            query.setMaxResults(sizeNo);
            float nrOfPages = (float) RepositoryMappingDirection.countRepositoryMappingDirectionsByExternalApp(ea) / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        }
        model.addAttribute("repositorymappingdirections", query.getResultList());
        addDateTimeFormatPatterns(model);
        return "linkid/repositorymappingdirections/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
	@PreAuthorize("#ea == #repositoryMappingDirection.repositoryMapping.externalApp")
    public String update(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, 
    		@Valid RepositoryMappingDirection repositoryMappingDirection,
    		BindingResult result, 
    		Model model, 
    		HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("repositoryMappingDirection", repositoryMappingDirection);
            addDateTimeFormatPatterns(model);
            return "linkid/repositorymappingdirections/update";
        }
        repositoryMappingDirection.merge();
        return "redirect:/linkid/" + ea.getLinkId() + "/repositorymappingdirections/" + encodeUrlPathSegment(repositoryMappingDirection.getId().toString(), request);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, 
    		@PathVariable("id") Long id,
    		Model model) {
        final RepositoryMappingDirection rmd = RepositoryMappingDirection.findRepositoryMappingDirection(id);
        validateRepositoryMapping(rmd.getRepositoryMapping(), ea);
		model.addAttribute("repositoryMappingDirection", rmd);
        addDateTimeFormatPatterns(model);
        return "linkid/repositorymappingdirections/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, 
    		@PathVariable("id") Long id, 
    		@RequestParam(value = "page", required = false) Integer page, 
    		@RequestParam(value = "size", required = false) Integer size, 
    		Model model) {
        final RepositoryMappingDirection rmd = RepositoryMappingDirection.findRepositoryMappingDirection(id);
        validateRepositoryMapping(rmd.getRepositoryMapping(), ea);
		rmd.remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/linkid/" + ea.getLinkId() + "/repositorymappingdirections?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
	
    @ModelAttribute("repositorymappings")
    public Collection<RepositoryMapping> populateRepositoryMappings(@PathVariable("linkId") ExternalApp ea) {
        return RepositoryMapping.findRepositoryMappingsByExternalApp(ea).getResultList();
    }
    
    Collection<RepositoryMapping> populateRepositoryMappings() {
        throw new UnsupportedOperationException();
    }


    String create(@Valid RepositoryMappingDirection repositoryMappingDirection, BindingResult result, Model model, HttpServletRequest request) {
    	throw new UnsupportedOperationException();
    }

    public String update(@Valid RepositoryMappingDirection repositoryMappingDirection, BindingResult result, Model model, HttpServletRequest request) {
    	throw new UnsupportedOperationException();
    }
}
