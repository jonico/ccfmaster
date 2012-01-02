package com.collabnet.ccf.ccfmaster.controller;

import java.util.Collection;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//@RooWebScaffold(path = "linkid/repositorymappings", formBackingObject = RepositoryMapping.class)
@RequestMapping("/linkid/{linkId}/repositorymappings")
@Controller
public class LinkIdRepositoryMappingController extends AbstractLinkIdController {
	
	private static final Logger log = LoggerFactory.getLogger(LinkIdRepositoryMappingController.class);

	@RequestMapping(method = RequestMethod.GET)
    public String list(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		@RequestParam(value = "page", required = false) Integer page,
    		@RequestParam(value = "size", required = false) Integer size,
    		Model model) {
		TypedQuery<RepositoryMapping> query = RepositoryMapping.findRepositoryMappingsByExternalApp(ea);
		if (page != null || size != null) {
			int maxResults = size == null ? 10 : size.intValue();
			int firstResult = page == null ? 0 : (page.intValue() - 1) * maxResults;
			query = query.setFirstResult(firstResult).setMaxResults(maxResults);
            float nrOfPages = (float) RepositoryMapping.countRepositoryMappingsByExternalApp(ea) / maxResults;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		}
		model.addAttribute("repositorymappings", query.getResultList());
        return "linkid/repositorymappings/list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		@PathVariable("id") Long id,
    		Model model) {
        RepositoryMapping mapping = RepositoryMapping.findRepositoryMapping(id);
        validateRepositoryMapping(mapping, ea);
        model.addAttribute("repositorymapping", mapping);
        model.addAttribute("itemId", id);
        return "linkid/repositorymappings/show";
    }

    @PreAuthorize("(#ea == #repositoryMapping.externalApp)")
	@RequestMapping(method = RequestMethod.POST, headers="Content-Type!=application/xml" )
    public String create(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		@Valid RepositoryMapping repositoryMapping,
    		BindingResult result, 
    		Model model, 
    		HttpServletRequest request) {
    	if (result.hasErrors()) {
            model.addAttribute("repositoryMapping", repositoryMapping);
            return "linkid/repositorymappings/create";
        }
        repositoryMapping.persist();
        return "redirect:/linkid/" + ea.getLinkId() + "/repositorymappings/" + encodeUrlPathSegment(repositoryMapping.getId().toString(), request);
    }

    @RequestMapping(method = RequestMethod.PUT)
    //TODO: Should we allow updates that change repositoryMapping.externalApp? (currently: no)
    @PreAuthorize("#ea == #repositoryMapping.externalApp")
    public String update(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, @Valid RepositoryMapping repositoryMapping, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("repositoryMapping", repositoryMapping);
            return "linkid/repositorymappings/update";
        }
        repositoryMapping.merge();
        return "redirect:/linkid/" + ea.getLinkId() + "/repositorymappings/" + encodeUrlPathSegment(repositoryMapping.getId().toString(), request);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		Model model) {
        RepositoryMapping repositoryMapping = new RepositoryMapping();
        // hard-code the repositoryMapping here
        repositoryMapping.setExternalApp(ea);
		model.addAttribute("repositoryMapping", repositoryMapping);
        return "linkid/repositorymappings/create";
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		@PathVariable("id") Long id, 
    		Model model) {
        RepositoryMapping repositoryMapping = RepositoryMapping.findRepositoryMapping(id);
        validateRepositoryMapping(repositoryMapping, ea);
		model.addAttribute("repositoryMapping", repositoryMapping);
        return "linkid/repositorymappings/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		@PathVariable("id") Long id,
    		@RequestParam(value = "page", required = false) Integer page,
    		@RequestParam(value = "size", required = false) Integer size,
    		Model model) {
        RepositoryMapping repositoryMapping = RepositoryMapping.findRepositoryMapping(id);
        validateRepositoryMapping(repositoryMapping, ea);
		repositoryMapping.remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/linkid/" + ea.getLinkId() + "/repositorymappings?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }

	/*
	 * Dummy controller methods to prevent Roo from generating ambiguous controllers.
	 * 
	 * We can't put them into a generic AbstractLinkIdController<T> because Roo
	 * ignores the type parameter when it determines which methods to override
	 * :-(. See http://forum.springsource.org/showthread.php?t=101829 for
	 * details.
	 */

	String create(@Valid RepositoryMapping repositoryMapping, BindingResult result, Model model, HttpServletRequest request) {
		throw new UnsupportedOperationException();
	}

	String update(@Valid RepositoryMapping repositoryMapping, BindingResult result, Model model, HttpServletRequest request) {
		throw new UnsupportedOperationException();
	}

	@ModelAttribute("externalapps")
	public Collection<ExternalApp> populateExternalApps(@PathVariable String linkId) {
	    return ExternalApp.findExternalAppsByLinkIdEquals(linkId).getResultList();
	}
}
