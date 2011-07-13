package com.collabnet.ccf.ccfmaster.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;

//@RooWebScaffold(path = "linkid/identitymappings", formBackingObject = IdentityMapping.class)
@RequestMapping(LinkIdIdentityMappingController.BASE_PATH)
@Controller
public class LinkIdIdentityMappingController extends AbstractLinkIdController {
	public static final String BASE_PATH = "/linkid/{linkId}/identitymappings";
	private static final Logger log = LoggerFactory.getLogger(LinkIdIdentityMappingController.class);
	
	@PreAuthorize("#ea == #identityMapping.repositoryMapping.externalApp")
	@RequestMapping(method = RequestMethod.POST)
    public String create(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		@Valid IdentityMapping identityMapping,
    		BindingResult result,
    		Model model,
    		HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("identityMapping", identityMapping);
            addDateTimeFormatPatterns(model);
            return "linkid/identitymappings/create";
        }
        identityMapping.persist();
        return "redirect:/linkid/" + ea.getLinkId() + "/identitymappings/" + encodeUrlPathSegment(identityMapping.getId().toString(), request);
    }

	@SuppressWarnings("unchecked")
	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		Model model) {
        model.addAttribute("identityMapping", new IdentityMapping());
        addDateTimeFormatPatterns(model);
        @SuppressWarnings("rawtypes")
		List dependencies = new ArrayList();
        if (RepositoryMapping.countRepositoryMappingsByExternalApp(ea) == 0) {
            dependencies.add(new String[]{"repositoryMapping", "repositorymappings"});
        }
        model.addAttribute("dependencies", dependencies);
        return "linkid/identitymappings/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea,
    		@PathVariable("id") Long id,
    		Model model) {
        final IdentityMapping im = IdentityMapping.findIdentityMapping(id);
        validateRepositoryMapping(im.getRepositoryMapping(), ea);
        addDateTimeFormatPatterns(model);
		model.addAttribute("identitymapping", im);
        model.addAttribute("itemId", id);
        return "linkid/identitymappings/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, 
    		@RequestParam(value = "page", required = false) Integer page,
    		@RequestParam(value = "size", required = false) Integer size,
    		Model model) {
		log.debug("in list()");
		final TypedQuery<IdentityMapping> query = IdentityMapping.findIdentityMappingsByExternalApp(ea);
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            query.setFirstResult(firstResult);
            query.setMaxResults(sizeNo);
            float nrOfPages = (float) IdentityMapping.countIdentityMappingsByExternalApp(ea) / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        }
		model.addAttribute("identitymappings", query.getResultList());
        addDateTimeFormatPatterns(model);
        return "linkid/identitymappings/list";
    }

	@PreAuthorize("#ea == #identityMapping.repositoryMapping.externalApp")
	@RequestMapping(method = RequestMethod.PUT)
    public String update(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, 
    		@Valid IdentityMapping identityMapping,
    		BindingResult result,
    		Model model,
    		HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("identityMapping", identityMapping);
            addDateTimeFormatPatterns(model);
            return "linkid/identitymappings/update";
        }
        identityMapping.merge();
        return "redirect:/linkid/" + ea.getLinkId() + "/identitymappings/" + encodeUrlPathSegment(identityMapping.getId().toString(), request);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, 
    		@PathVariable("id") Long id,
    		Model model) {
        final IdentityMapping im = IdentityMapping.findIdentityMapping(id);
        validateRepositoryMapping(im.getRepositoryMapping(), ea);
		model.addAttribute("identityMapping", im);
        addDateTimeFormatPatterns(model);
        return "linkid/identitymappings/update";
    }

	@PreAuthorize("#ea == #identityMapping.repositoryMapping.externalApp")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(
    		@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea, 
    		@PathVariable("id") Long id,
    		@RequestParam(value = "page", required = false) Integer page,
    		@RequestParam(value = "size", required = false) Integer size,
    		Model model) {
        final IdentityMapping im = IdentityMapping.findIdentityMapping(id);
        validateRepositoryMapping(im.getRepositoryMapping(), ea);
		im.remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/linkid/" + ea.getLinkId() + "/identitymappings?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }

	@ModelAttribute("repositorymappings")
    public Collection<RepositoryMapping> populateRepositoryMappings(@PathVariable("linkId") ExternalApp ea) {
        return RepositoryMapping.findRepositoryMappingsByExternalApp(ea).getResultList();
    }

	Collection<RepositoryMapping> populateRepositoryMappings() {
        return RepositoryMapping.findAllRepositoryMappings();
    }


    String create(@Valid IdentityMapping identityMapping, BindingResult result, Model model, HttpServletRequest request) {
    	throw new UnsupportedOperationException();
    }

    String update(@Valid IdentityMapping identityMapping, BindingResult result, Model model, HttpServletRequest request) {
    	throw new UnsupportedOperationException();
    }
}
