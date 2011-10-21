package com.collabnet.ccf.ccfmaster.controller.api;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.core.QCMetaDataProvider;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;

/**
 * This controller handles all communications with HP QC / HP ALM It can be used
 * to retrieve available projects, domains, requirement types and repository
 * layout information
 * 
 * @author jnicolai
 * 
 */
@Controller
@RequestMapping(value = Paths.LANDSCAPE + "/{landscapeId}/" + "qcmetadata")
public class ApiQCMetaDataController {

	private static final String CCF_PARTICIPANT_QC_URL = "ccf.participant.qc.url";
	private @Autowired
	QCMetaDataProvider qcMetaDataProvider;

	@ModelAttribute
	Landscape populateLandscape(
			@PathVariable("landscapeId") Landscape landscape,
			@PathVariable("landscapeId") String landscapeId, Model uiModel) {
		if (landscape == null) {
			throw new BadRequestException(String.format(
					"Could not find landscape with id (%s)", landscapeId));
		}
		// now examine participant
		Participant participant = landscape.getParticipant();
		if (participant.getSystemKind() != SystemKind.QC) {
			throw new BadRequestException(String.format(
					"Landscape with id (%s) does not talk to a QC system", landscapeId));
		}
		// now retrieve qcURL
		ParticipantConfig qcURL = null;
		try {
			qcURL = ParticipantConfig.findParticipantConfigsByParticipantAndName(participant, CCF_PARTICIPANT_QC_URL).getSingleResult();
		} catch (Exception e) {
			throw new BadRequestException(String.format(
					"Landscape with id (%s) does not have a proper QC URL configured", landscapeId));
		}
		uiModel.addAttribute("qcURL", qcURL.getVal());
		return landscape;
	}

	@RequestMapping(value = "/domains/", method = GET)
	public @ResponseBody
	String showVisibleDomains(
			@ModelAttribute Landscape landscape,
			@ModelAttribute("qcURL") String qcURL,
			@RequestParam(value = "qcUser", required = true) String qcUser,
			@RequestParam(value = "qcPassword", required = true) String qcPassword)
			throws IOException {
		return qcMetaDataProvider.showVisibleDomains(landscape, qcURL, qcUser,
				qcPassword);
	}

	@RequestMapping(value = "/domains/{domainName}/projects/", method = GET)
	public @ResponseBody
	String showVisibleProjectsInDomain(
			@ModelAttribute Landscape landscape,
			@NotNull @PathVariable("domainName") String domain,
			@ModelAttribute("qcURL") String qcURL,
			@RequestParam(value = "qcUser", required = true) String qcUser,
			@RequestParam(value = "qcPassword", required = true) String qcPassword)
			throws IOException {
		return qcMetaDataProvider.showVisibleProjectsInDomain(landscape,
				domain, qcURL, qcUser, qcPassword);
	}

	@RequestMapping(value = "/domains/{domainName}/projects/{projectName}", method = GET)
	public @ResponseBody
	String validateDomainAndProject(
			@ModelAttribute Landscape landscape,
			@NotNull @PathVariable("domainName") String domain,
			@NotNull @PathVariable("projectName") String project,
			@ModelAttribute("qcURL") String qcURL,
			@RequestParam(value = "qcUser", required = true) String qcUser,
			@RequestParam(value = "qcPassword", required = true) String qcPassword)
			throws IOException {
		return qcMetaDataProvider.validateDomainAndProject(landscape, domain,
				project, qcURL, qcUser, qcPassword);
	}

	@RequestMapping(value = "/domains/{domainName}/projects/{projectName}/defectFields/", method = GET)
	public @ResponseBody
	String showDefectFields(
			@ModelAttribute Landscape landscape,
			@NotNull @PathVariable("domainName") String domain,
			@NotNull @PathVariable("projectName") String project,
			@ModelAttribute("qcURL") String qcURL,
			@RequestParam(value = "qcUser", required = true) String qcUser,
			@RequestParam(value = "qcPassword", required = true) String qcPassword)
			throws IOException {
		return qcMetaDataProvider.showDefectFields(landscape, domain, project,
				qcURL, qcUser, qcPassword);
	}

	@RequestMapping(value = "/domains/{domainName}/projects/{projectName}/requirementTypes/", method = GET)
	public @ResponseBody
	String showRequirementTypes(
			@ModelAttribute Landscape landscape,
			@NotNull @PathVariable("domainName") String domain,
			@NotNull @PathVariable("projectName") String project,
			@ModelAttribute("qcURL") String qcURL,
			@RequestParam(value = "qcUser", required = true) String qcUser,
			@RequestParam(value = "qcPassword", required = true) String qcPassword)
			throws IOException {
		return qcMetaDataProvider.showRequirementTypes(landscape, domain,
				project, qcURL, qcUser, qcPassword);
	}

	@RequestMapping(value = "/domains/{domainName}/projects/{projectName}/requirementTypes/{requirementTypeName}/requirementFields/", method = GET)
	public @ResponseBody
	String showRequirementFields(
			@ModelAttribute Landscape landscape,
			@NotNull @PathVariable("domainName") String domain,
			@NotNull @PathVariable("projectName") String project,
			@NotNull @PathVariable("requirementTypeName") String requirementType,
			@ModelAttribute("qcURL") String qcURL,
			@RequestParam(value = "qcUser", required = true) String qcUser,
			@RequestParam(value = "qcPassword", required = true) String qcPassword)
			throws IOException {
		return qcMetaDataProvider.showRequirementFields(landscape, domain,
				project, requirementType, qcURL, qcUser, qcPassword);
	}

	@RequestMapping(value = "/domains/{domainName}/projects/{projectName}/requirementTypes/{requirementTypeName}", method = GET)
	public @ResponseBody
	String validateDomainAndProjectAndRequirementType(
			@ModelAttribute Landscape landscape,
			@NotNull @PathVariable("domainName") String domain,
			@NotNull @PathVariable("projectName") String project,
			@NotNull @PathVariable("requirementTypeName") String requirementType,
			@ModelAttribute("qcURL") String qcURL,
			@RequestParam(value = "qcUser", required = true) String qcUser,
			@RequestParam(value = "qcPassword", required = true) String qcPassword)
			throws IOException {
		return qcMetaDataProvider.validateDomainAndProjectAndRequirementType(
				landscape, domain, project, requirementType, qcURL, qcUser,
				qcPassword);
	}
}
