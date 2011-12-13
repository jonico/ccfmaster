package com.collabnet.ccf.ccfmaster.selenium;

import java.text.DateFormat;
import java.util.Date;

import org.junit.Assert;

import com.collabnet.ccf.ccfmaster.selenium.project.LoginInfo;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.thoughtworks.selenium.Selenium;



public class MockDataUtil {
	
	// default formatting is "9/2/2011 12:00 AM";
	static final DateFormat dateFormat = DateFormat.getInstance();

	public static void createExternalApp(Selenium selenium) {
		final LoginInfo loginInfo = LoginInfo.projectScopeFromSystemProperties();
		// implicitly creates external app object
		loginInfo.login(selenium);

		selenium.open("/CCFMaster/externalapps/");
		selenium.waitForPageToLoad("30000");
		Assert.assertFalse(selenium.isTextPresent("No External App found."));

/*		log.info("screenshot before create external app:\n" +
				  "data:image/png;base64,{}", 
				  selenium.captureScreenshotToString());
		selenium.open("/CCFMaster/externalapps/?form");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=_linkId_id", linkId);
		final String projectPath = "projects.brokerage_system_sample";
		selenium.type("id=_projectPath_id", projectPath);
		selenium.type("id=_projectTitle_id", "external app");
		selenium.click("id=proceed");
		selenium.waitForPageToLoad("30000");
*/		
	}
		
	public static void createRepositoryMappingAndRepositoryMappingDirection(Selenium selenium) {
		final String description = "Repo 1";
		final String tfRepoId = "tracker1068";
		final String partRepoId = "12";
		createRepositoryMapping(selenium, description, tfRepoId, partRepoId);
		
		final Directions direction = Directions.FORWARD;
		final String lastSourceArtifactVersion = "132";
		final String lastSourceArtifactId = "artf1788";
		final Date lastSourceArtifactModificationDate = new Date();
		createRepositoryMappingDirection(selenium, direction,
				lastSourceArtifactVersion, lastSourceArtifactId, lastSourceArtifactModificationDate);
	}


	static void createRepositoryMappingDirection(Selenium selenium,
			Directions direction,
			final String lastSourceArtifactVersion,
			final String lastSourceArtifactId, final Date lastSourceArtifactModificationDate) {
		selenium.open("/CCFMaster/repositorymappingdirections/?form");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=_direction_id", direction.toString());
		String dateStr = dateFormat.format(lastSourceArtifactModificationDate);
		selenium.type("id=_lastSourceArtifactModificationDate_id", dateStr);
//		selenium.click("//table[@id='_lastSourceArtifactModificationDate_id_popup']/tbody/tr[3]/td[3]/span");
		selenium.click("id=_lastSourceArtifactVersion_id");
		selenium.type("id=_lastSourceArtifactVersion_id", lastSourceArtifactVersion);
		selenium.type("id=_lastSourceArtifactId_id", lastSourceArtifactId);
		selenium.click("id=proceed");
		selenium.waitForPageToLoad("30000");
	}

	static void createRepositoryMapping(Selenium selenium,
			final String description, final String tfRepoId,
			final String partRepoId) {
		selenium.open("/CCFMaster/repositorymappings/?form");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=_description_id");
		selenium.type("id=_description_id", description);
		selenium.type("id=_teamForgeRepositoryId_id", tfRepoId);
		selenium.type("id=_participantRepositoryId_id", partRepoId);
		selenium.click("id=proceed");
		selenium.waitForPageToLoad("30000");
	}

		
	public static void createFailedShipments(Selenium selenium) {
		final String description = "sample";
		final String exceptionClassName = "sample class";
		final String exceptionMessage = "sample message";
		final String causeExceptionClassName = "sample exception class name";
		final String causeExceptionMessage = "sample cause exception msg";
		final String stackTrace = "stack trace";
		final String adaptorName = "TFwriter";
		final String originatingComponent = "QCwriter";
		final String dataType = "String";
		final String data = "plain data";
		final String sourceArtifactId = "artf1785";
		final String targetArtifactId = "12";
		final String errorCode = "404 error";
		final Date sourceLastModificationTime = new Date();
		final Date targetLastModificationTime = new Date();
		final String sourceArtifactVersion = "123";
		final String targetArtifactVersion = "124";
		final String artifactType = "attachment";
		final String genericArtifact = "<xml></xml>";
		final String timestamp = "2011-08-22 11:22:33 PM IST";

		createFailedShipment(selenium, description, exceptionClassName,
				exceptionMessage, causeExceptionClassName,
				causeExceptionMessage, stackTrace, adaptorName,
				originatingComponent, dataType, data, sourceArtifactId,
				targetArtifactId, errorCode, sourceLastModificationTime,
				targetLastModificationTime, sourceArtifactVersion,
				targetArtifactVersion, artifactType, genericArtifact, timestamp);

	}

	static void createFailedShipment(Selenium selenium,
			final String description, final String exceptionClassName,
			final String exceptionMessage,
			final String causeExceptionClassName,
			final String causeExceptionMessage, final String stackTrace,
			final String adaptorName, final String originatingComponent,
			final String dataType, final String data,
			final String sourceArtifactId, final String targetArtifactId,
			final String errorCode, final Date sourceLastModificationTime,
			final Date targetLastModificationTime,
			final String sourceArtifactVersion,
			final String targetArtifactVersion, final String artifactType,
			final String genericArtifact, final String timestamp) {
		selenium.open("/CCFMaster/hospitalentrys/?form");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=_description_id", description);
		selenium.type("id=_exceptionClassName_id", exceptionClassName);
		selenium.type("id=_exceptionMessage_id", exceptionMessage);
		selenium.type("id=_causeExceptionClassName_id", causeExceptionClassName);
		selenium.type("id=_causeExceptionMessage_id", causeExceptionMessage);
		selenium.type("id=_stackTrace_id", stackTrace);
		selenium.type("id=_adaptorName_id", adaptorName);
		selenium.type("id=_originatingComponent_id", originatingComponent);
		selenium.type("id=_dataType_id", dataType);
		selenium.type("id=_data_id", data);
		selenium.type("id=_sourceArtifactId_id", sourceArtifactId);
		selenium.type("id=_targetArtifactId_id", targetArtifactId);
		selenium.type("id=_errorCode_id", errorCode);
		String sourceLastModificationTimeStr = dateFormat.format(sourceLastModificationTime );
		selenium.type("id=_sourceLastModificationTime_id", sourceLastModificationTimeStr);
//		selenium.click("//table[@id='_sourceLastModificationTime_id_popup']/tbody/tr[3]/td[3]/span");
		String targetLastModificationTimeStr = dateFormat.format(targetLastModificationTime);
		selenium.type("id=_targetLastModificationTime_id", targetLastModificationTimeStr);
//		selenium.click("//table[@id='_targetLastModificationTime_id_popup']/tbody/tr[3]/td[3]/span");
//		selenium.click("id=_sourceArtifactVersion_id");
		selenium.type("id=_sourceArtifactVersion_id", sourceArtifactVersion);
		selenium.type("id=_targetArtifactVersion_id", targetArtifactVersion);
		selenium.type("id=_artifactType_id", artifactType);
		selenium.type("id=_genericArtifact_id", genericArtifact);
		selenium.type("id=_timestamp_id", timestamp);
		selenium.click("id=proceed");
		selenium.waitForPageToLoad("30000");
	}

}
