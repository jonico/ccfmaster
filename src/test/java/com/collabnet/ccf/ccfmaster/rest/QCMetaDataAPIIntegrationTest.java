package com.collabnet.ccf.ccfmaster.rest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;


public class QCMetaDataAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private LandscapeDataOnDemand dod;
	
	private String qcURL = "http://localhost:9090/qcbin/";
	private String domain ="DEFAULT";
	private String qcUser = "admin";
	private String qcPassword = "admin";
	private String project = "CCFDemo";
	private String requirementType = "Functional";
	
	private Landscape landscape;
	
	@Before
	public void prepareLandscape() {
		landscape = dod.getRandomLandscape();
		Participant participant = landscape.getParticipant();
		participant.setSystemKind(SystemKind.QC);
		participant.merge();
		ParticipantConfig pc = new ParticipantConfig();
		pc.setParticipant(participant);
		pc.setVal(qcURL);
		pc.setName("ccf.participant.qc.url");
		pc.merge();
	}
	
	@Test
	public void testShowVisibleDomains() {
		org.junit.Assert.assertNotNull("Data on demand for 'Landscape' failed to initialize correctly", landscape);
		String apiString = "/landscapes/" + landscape.getPlugId() + "/qcmetadata/domains/" + "?qcUser=" + qcUser + "&qcPassword=" + qcPassword;
		String result = restTemplate.getForObject(ccfAPIUrl + apiString, String.class);
		org.junit.Assert.assertEquals("returned string did not match expectations", "showVisibleDomains"+landscape + qcURL + qcUser + qcPassword, result);
	}
	
	@Test
	public void testShowVisibleProjectsInDomain() {
		org.junit.Assert.assertNotNull("Data on demand for 'Landscape' failed to initialize correctly", landscape);
		String apiString = "/landscapes/" + landscape.getPlugId() + "/qcmetadata/domains/" + domain + "/projects/?qcUser=" + qcUser + "&qcPassword=" + qcPassword;
		String result = restTemplate.getForObject(ccfAPIUrl + apiString, String.class);
		org.junit.Assert.assertEquals("returned string did not match expectations", "showVisibleProjectsInDomain"+landscape+domain+qcURL+qcUser+qcPassword, result);
	}
	
	@Test
	public void testValidateDomainAndProject() {
		org.junit.Assert.assertNotNull("Data on demand for 'Landscape' failed to initialize correctly", landscape);
		String apiString = "/landscapes/" + landscape.getPlugId() + "/qcmetadata/domains/" + domain + "/projects/" + project + "?qcUser=" + qcUser + "&qcPassword=" + qcPassword;
		String result = restTemplate.getForObject(ccfAPIUrl + apiString, String.class);
		org.junit.Assert.assertEquals("returned string did not match expectations", "validateDomainAndProject"+landscape+domain+project+qcURL+qcUser+qcPassword, result);
	}
	
	@Test
	public void testShowDefectFields() {
		org.junit.Assert.assertNotNull("Data on demand for 'Landscape' failed to initialize correctly", landscape);
		String apiString = "/landscapes/" + landscape.getPlugId() + "/qcmetadata/domains/" + domain + "/projects/" + project + "/defectFields/?qcUser=" + qcUser + "&qcPassword=" + qcPassword;
		String result = restTemplate.getForObject(ccfAPIUrl + apiString, String.class);
		org.junit.Assert.assertEquals("returned string did not match expectations", "showDefectFields"+landscape+domain+project+qcURL+qcUser+qcPassword, result);
	}
	
	@Test
	public void testShowRequirementTypes() {
		org.junit.Assert.assertNotNull("Data on demand for 'Landscape' failed to initialize correctly", landscape);
		String apiString = "/landscapes/" + landscape.getPlugId() + "/qcmetadata/domains/" + domain + "/projects/" + project + "/requirementTypes/?qcUser=" + qcUser + "&qcPassword=" + qcPassword;
		String result = restTemplate.getForObject(ccfAPIUrl + apiString, String.class);
		org.junit.Assert.assertEquals("returned string did not match expectations", "showRequirementTypes"+landscape+domain+project+qcURL+qcUser+qcPassword, result);
	}
	
	@Test
	public void testShowRequirementFields() {
		org.junit.Assert.assertNotNull("Data on demand for 'Landscape' failed to initialize correctly", landscape);
		String apiString = "/landscapes/" + landscape.getPlugId() + "/qcmetadata/domains/" + domain + "/projects/" + project + "/requirementTypes/" + requirementType + "/requirementFields/?qcUser=" + qcUser + "&qcPassword=" + qcPassword;
		String result = restTemplate.getForObject(ccfAPIUrl + apiString, String.class);
		org.junit.Assert.assertEquals("returned string did not match expectations", "showRequirementFields"+landscape+domain+project+requirementType+qcURL+qcUser+qcPassword, result);
	}
	
	@Test
	public void testValidateDomainAndProjectAndRequirementType() {
		org.junit.Assert.assertNotNull("Data on demand for 'Landscape' failed to initialize correctly", landscape);
		String apiString = "/landscapes/" + landscape.getPlugId() + "/qcmetadata/domains/" + domain + "/projects/" + project + "/requirementTypes/" + requirementType + "?qcUser=" + qcUser + "&qcPassword=" + qcPassword;
		String result = restTemplate.getForObject(ccfAPIUrl + apiString, String.class);
		org.junit.Assert.assertEquals("returned string did not match expectations", "validateDomainAndProjectAndRequirementType"+landscape+domain+project+requirementType+qcURL+qcURL+qcPassword, result);
	}

}
