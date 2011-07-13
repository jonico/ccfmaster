/**
 * 
 */
package com.collabnet.ccf.ccfmaster.controller.web;

/**
 * @author selvakumar
 *
 */
public class ControllerConstants {

	
	
	static final String PARTICIPANTHIDDEN = "participanthidden";
	static final String PARTICIPANTEXISTMESSAGE = "participantexistmessage";
	static final String TEAMFORGE = "teamforge";
	static final String TFCONNECTIONSUCCESSMESSAGE = "tfconnectionsuccessmessage";
	
	public static final String PLUGID = "plug496";
	public static final String SW_PDESCRIPTION = "ScrumWorks Pro";
	public static final String SWP = "SWP";
	public static final String QCDESCRIPTION = "HP Quality Center";
	public static final String QC = "QC";
	public static final String TFDESCRIPTION = "TeamForge";
	public static final String TF = "TF";

	// Landscape validator constants
	public static final String VALIDATESWPURL = "wsdl";
	public static final String VALIDATEQCURL = "qcbin/";
	public static final String ERROR_SWPURL_VALIDATE = "error.swpurl.validate";
	public static final String ERROR_QCURL_VALIDATE = "error.qcurl.validate";
	public static final String ERROR_SWPRESYNCUSERNAME_VALIDATE = "NotNull.participantResyncUserNameLandscapeConfig.val";
	public static final String ERROR_SWPRESYNCPASSWORD_VALIDATE = "NotNull.participantResyncPasswordLandscapeConfig.val";
	public static final String ERROR_SWPPASSWORD_VALIDATE = "NotBlank.participantPasswordLandscapeConfig.val";
	public static final String ERROR_PARTICIPANTPASSWORD_VALIDATE = "NotBlank.participantUserNameLandscapeConfig.val";
	public static final String ERROR_TFUSERNAME_VALIDATE = "NotBlank.tfUserNameLandscapeConfig.val";
	public static final String ERROR_TFPASSWORD_VALIDATE = "NotBlank.tfPasswordLandscapeConfig.val";
	
	//Constants to persist landscape
	public static final String DEFAULTLOGTEMPLATE = "An Artifact has been quarantined.\r\n\n"+  
	"SOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>"+ 
	"SOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>"+ 
	 "SOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>" +
	 "TARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>"+ 
	 "TARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>"+ 
	 "TARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>"+ 
	 "ERROR_CODE: <ERROR_CODE>"+ 
	 "TIMESTAMP: <TIMESTAMP>"+ 
	 "EXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>";
	
	public static final String CCF_DIRECTION_TF_MAX_ATTACHMENTSIZE = "ccf.direction.tf.max.attachmentsize";
	public static final String CCF_DIRECTION_SWP_MAX_ATTACHMENTSIZE = "ccf.direction.swp.max.attachmentsize";
	public static final String MAXATTACH_SIZEVAL = "10485760";
	public static final String CCF_DIRECTION_QC_MAX_ATTACHMENTSIZE = "ccf.direction.qc.max.attachmentsize";
	public static final String CCF_DIRECTION_LOGMESSAGETEMPLATE = "ccf.direction.logmessagetemplate";
	public static final String QCTOTFDIRECTIONDESCRIPTION = "QctoTFDirectionDescription";
	public static final String TFTOQCDIRECTIONDESCRIPTION = "TFtoQCDirectionDescription";
	public static final String SWPTOTFDIRECTIONDESCRIPTION = "SWPtoTFDirectionDescription";
	public static final String TFTOSWP_DIRECTION_DESCRIPTION = "TFtoSWPDirectionDescription";
	public static final String CCF_PARTICIPANT_TF_URL = "ccf.participant.tf.url";
	
	//Participant Settings Constants
	public static final String QCSETTINGS = "qcsettings";
	public static final String CCF_LANDSCAPE_SWP_RESYNC_PASSWORD = "ccf.landscape.swp.resync.password";
	public static final String CCF_LANDSCAPE_SWP_RESYNC_USERNAME = "ccf.landscape.swp.resync.username";
	public static final String CCF_LANDSCAPE_SWP_PASSWORD = "ccf.landscape.swp.password";
	public static final String CCF_LANDSCAPE_SWP_USERNAME = "ccf.landscape.swp.username";
	public static final String CCF_PARTICIPANT_SWP_URL = "ccf.participant.swp.url";
	public static final String CCF_LANDSCAPE_QC_PASSWORD = "ccf.landscape.qc.password";
	public static final String CCF_LANDSCAPE_QC_USERNAME = "ccf.landscape.qc.username";
	public static final String CCF_PARTICIPANT_QC_URL = "ccf.participant.qc.url";

	// Persist participant constants
	public static final String PARTICIPANTSAVESUCCESSMESSAGE = "participantsavesuccessmessage";
	public static final String MAXATTACHMENTSIZE_VERSION = "maxattachmentsize.version";
	public static final String MAXATTACHMENTSIZE_ID = "maxattachmentsize.id";
	public static final String RESYNCPASSWORD_VERSION = "resyncpassword.version";
	public static final String RESYNCPASSWORD_ID = "resyncpassword.id";
	public static final String RESYNCUSERNAME_VERSION = "resyncusername.version";
	public static final String RESYNCUSERNAME_ID = "resyncusername.id";
	public static final String PASSWORD_VERSION = "password.version";
	public static final String PASSWORD_ID = "password.id";
	public static final String USERNAME_VERSION = "username.version";
	public static final String USERNAME_ID = "username.id";
	public static final String LANDSCAPE_VERSION = "landscape.version";
	public static final String LANDSCAPE_ID = "landscape.id";
	public static final String URLPARTICIPANTCONFIG_VERSION = "urlparticipantconfig.version";
	public static final String URLPARTICIPANTCONFIG_ID = "urlparticipantconfig.id";
	public static final String PARTICIPANT_VERSION = "participant.version";
	public static final String PARTICIPANT_ID = "participant.id";
	
	//TFsettings  Constants
	public static final String TFUSERNAME_VERSION = "tfusername.version";
	public static final String TFUSERNAME_ID = "tfusername.id";
	public static final String TFSAVESUCCESSMESSAGE = "tfsavesuccessmessage";
	public static final String TFMAXATTACHMENTSIZE_VERSION = "tfmaxattachmentsize.version";
	public static final String TFMAXATTACHMENTSIZE_ID = "tfmaxattachmentsize.id";
	public static final String TFPASSWORD_VERSION = "tfpassword.version";
	public static final String TFPASSWORD_ID = "tfpassword.id";
	public static final String TEAMFORGE_VERSION = "teamforge.version";
	public static final String TEAMFORGE_ID = "teamforge.id";
	public static final String CCF_LANDSCAPE_TF_PASSWORD = "ccf.landscape.tf.password";
	public static final String CCF_LANDSCAPE_TF_USERNAME = "ccf.landscape.tf.username";
	public static final String TFSETTINGS = "tfsettings";
	
	//CCF Properties Constants
	public static final String CCFPROPERTIES = "ccfproperties";
	public static final String LOGTEMPLATESAVEFAILMESSAGE = "logtemplatesavefailmessage";
	public static final String LOGTEMPLATESAVESUCCESSMESSAGE = "logtemplatesavesuccessmessage";
	public static final String DIRECTIONCONFIG_VERSION = "directionconfig.version";
	public static final String DIRECTIONCONFIG_ID = "directionconfig.id";
	public static final String DIRECTION_VERSION = "direction.version";
	public static final String DIRECTION_ID = "direction.id";
	public static final String SYNCSAVEFAILMESSAGE = "syncsavefailmessage";
	public static final String SYNCSAVESUCCESSMESSAGE = "syncsavesuccessmessage";
	
	//Status Constants
	public static final String STATUS = "status";
	public static final String CCF_CORE_STATUS_VERSION = "ccfCoreStatus.version";
	public static final String CCF_CORE_STAUS_ID = "ccfCoreStaus.id";

	//Logs Constants
	public static final String LOGLISTFAILMESSAGE = "loglistfailmessage";
	public static final String DIRECTION = "direction";
	public static final String FILENAME = "filename";
	
	// Repository Mapping Directions Constants
	public static final String RMDRESUMESYNCSUCCESSMESSAGE="rmdresumesyncsuccessmessage";
	public static final String RMDRESUMESYNCFAILUREMESSAGE="rmdresumesyncfailuremessage";
	public static final String RMDPAUSESYNCSUCCESSMESSAGE="rmdpausesyncsuccessmessage";
	public static final String RMDPAUSESYNCFAILUREMESSAGE="rmdpausesyncfailuremessage";
	public static final String RMDDELETESUCCESSMESSAGE="rmddeletesuccessmessage";
	public static final String RMDDELETEFAILUREMESSAGE="rmddeletefailuremessage";
	
	/**
	 * 
	 */
	public ControllerConstants() {
		// TODO Auto-generated constructor stub
	}

}
