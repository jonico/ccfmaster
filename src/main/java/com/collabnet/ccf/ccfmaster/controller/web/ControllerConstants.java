/**
 * 
 */
package com.collabnet.ccf.ccfmaster.controller.web;

/**
 * @author selvakumar
 *
 */
public class ControllerConstants {

	
	
	public static final String PARTICIPANTHIDDEN = "participanthidden";
	public static final String PARTICIPANTEXISTMESSAGE = "participantexistmessage";
	public static final String TEAMFORGE = "teamforge";
	//public static final String TF_CONNECTION_SUCCESS_MESSAGE = "tfconnectionsuccessmessage";
	public static final String TF_CONNECTION_FAILURE_MESSAGE = "tfconnectionfailuremessage";
	
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
	public static final String CCF_MIS_CONFIGURATION_ERROR_MESSAGE = "ccfmisconfigurationerrormessage";
	public static final String TEAMFORGE_ERROR_MESSAGE = "teamforgeerrormessage";
	public static final String SCRUM_WORKS_PRO_ERROR_MESSAGE = "scrumworksproerrormessage";
	public static final String QUALITY_CENTER_ERROR_MESSAGE = "qualitycentererrormessage";
	public static final String DIRECTION_ERROR_MESSAGE = "directionerrormessage";
	public static final String DIRECTION_CONFIG_ERROR_MESSAGE = "directionconfigerrormessage";
	
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
	public static final String MAX_ATTACHMENT_CATEGORY = "coreSettings";
	public static final String MAX_ATTACHMENT_LABEL_NAME = "Max Attachment Size";
	public static final String MAX_ATTACHMENT_TOOLTIP = "Maximum attachment size that can be shipped for an artifact";
	
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
	public static final String PARTICIPANT_SAVE_FAIL_MESSAGE = "participantsavefailmessage";
	public static final String PARTICIPANT_RESTART_SUCCESS_MESSAGE = "participantrestartmessage";
	
	// Persist participant constants
	public static final String PARTICIPANT_SAVE_SUCCESS_MESSAGE = "participantsavesuccessmessage";
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
	public static final String QC_CONNECTION_SUCCESS_MESSAGE="qcconnectionsuccessmessage";
	public static final String QC_CONNECTION_FAILURE_MESSAGE="qcconnectionfailuremessage";
	public static final String SWP_CONNECTION_SUCCESS_MESSAGE="swpconnectionsuccessmessage";
	public static final String SWP_CONNECTION_FAILURE_MESSAGE="swpconnectionfailuremessage";
	
	
	
	//TFsettings  Constants
	public static final String TFUSERNAME_VERSION = "tfusername.version";
	public static final String TFUSERNAME_ID = "tfusername.id";
	public static final String TF_SAVE_SUCCESS_MESSAGE = "tfsavesuccessmessage";
	public static final String TFMAXATTACHMENTSIZE_VERSION = "tfmaxattachmentsize.version";
	public static final String TFMAXATTACHMENTSIZE_ID = "tfmaxattachmentsize.id";
	public static final String TFPASSWORD_VERSION = "tfpassword.version";
	public static final String TFPASSWORD_ID = "tfpassword.id";
	public static final String TEAMFORGE_VERSION = "teamforge.version";
	public static final String TEAMFORGE_ID = "teamforge.id";
	public static final String CCF_LANDSCAPE_TF_PASSWORD = "ccf.landscape.tf.password";
	public static final String CCF_LANDSCAPE_TF_USERNAME = "ccf.landscape.tf.username";
	public static final String TFSETTINGS = "tfsettings";
	public static final String TF_SAVE_FAIL_MESSAGE = "tfsavefailmessage";
	public static final String TF_CONNECTION_SUCCESS_MESSAGE = "tfconnectionsuccessmessage";
	public static final String TF_SAVE_RESTART_SUCCESS_MESSAGE = "tfsaverestartsuccessmessage";
	
	//CCF Properties Constants
	public static final String CCF_PROPERTIES = "ccfproperties";
	public static final String LOG_TEMPLATE_SAVE_FAIL_MESSAGE = "logtemplatesavefailmessage";
	public static final String LOG_TEMPLATE_SAVE_SUCCESS_MESSAGE = "logtemplatesavesuccessmessage";
	public static final String LOG_TEMPLATE_RESTART_SUCCESS_MESSAGE = "logtemplaterestartsuccessmessage";
	public static final String DIRECTION_CONFIG_VERSION = "directionconfig.version";
	public static final String DIRECTION_CONFIG_ID = "directionconfig.id";
	public static final String DIRECTION_VERSION = "direction.version";
	public static final String DIRECTION_ID = "direction.id";
	public static final String SYNC_SAVE_FAIL_MESSAGE = "syncsavefailmessage";
	public static final String SYNC_SAVE_SUCCESS_MESSAGE = "syncsavesuccessmessage";
	public static final String SYNC_SAVE_RESTART_SUCCESS_MESSAGE="syncsaverestartsuccessmessage";
	public static final String CCF_CORE_SWP_TO_TF_START_AUTOMATICALLY_MESSAGE = "ccfcoreswptotfstartautomaticallymessage";
	public static final String CCF_CORE_QC_TO_TF_START_AUTOMATICALLY_MESSAGE = "ccfcoreqctotfstartautomaticallymessage";
	public static final String CCF_CORE_TF_TO_SWP_START_AUTOMATICALLY_MESSAGE = "ccfcoretftoswpstartautomaticallymessage";
	public static final String CCF_CORE_TF_TO_QC_START_AUTOMATICALLY_MESSAGE = "ccfcoretftoqcstartautomaticallymessage";
	public static final String RESTORE_SUCCESS_MESSSAGE = "restoresucessmessage";
	public static final String VALIDATION_ERROR_MESSSAGE = "validationerrormessage";
	public static final String VALIDATE_TYPE_MIS_MATCH_CCFCOREPROPERTIES_NUMERIC = "TypeMisMatch.ccfcoreproperties.numeric";
	public static final String VALIDATE_NOT_EMPTY_CCFCOREPROPERTIES_VALUE = "NotEmpty.ccfcoreproperties.value";	
	public static final String DEFAULT_ERRORMSG_NOT_EMPTY_VALUE = "Blank value not accepted";
	
	
	
	
	//Status Constants
	public static final String STATUS = "status";
	public static final String CCF_CORE_STATUS_VERSION = "ccfCoreStatus.version";
	public static final String CCF_CORE_STAUS_ID = "ccfCoreStaus.id";
	public static final String CCF_CORE_STATUS_MESSAGE="ccfcorestatusmessage";
	public static final String CCF_CORE_RESTART_STATUS_MESSAGE="ccfcorerestartmessage";
	public static final String CCF_BACKUP_ERROR_MESSAGE="ccfbackuperrormessage";
	public static final String CCF_BACKUP_SUCCESS_MESSAGE="ccfbackupsuccessmessage";
	 
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
	
	// Field Mapping Template Constants
	public static final String FMTXPORTSUCCESSMESSAGE="fmtexportsuccessmessage";
	public static final String FMTEXPORTFAILUREMESSAGE="fmtexportfailuremessage";
	public static final String FMTIMPORTSUCCESSMESSAGE="fmtimportsuccessmessage";
	public static final String FMTIMPORTFAILUREMESSAGE="fmtimportfailuremessage";
	public static final String FMTDELETESUCCESSMESSAGE="fmtdeletesuccessmessage";
	public static final String FMTDELETEFAILUREMESSAGE="fmtdeletefailuremessage";
	public static final String FMTDUPLICATENAMEMESSAGE="fmtduplicatenamemessage";
	public static final String RMDSETSTATUS = "rmdsetstatus";

	
	//Hospital Constants
	public static final String HOSPITALDELETESUCCESSMESSAGE="hospitaldeletesuccessmessage";
	public static final String HOSPITALDELETEFAILUREMESSAGE="hospitaldeletefailuremessage";
	public static final String HOSPITALREPLAYSUCCESSMESSAGE="hospitalreplaysuccessmessage";
	public static final String HOSPITALREPLAYFAILUREMESSAGE="hospitalreplayfailuremessage";
	public static final String EXAMINEHOSPITALFAILUREMESSAGE="hospitalexaminefailuremessage";
	public static final String VIEWHOSPITALFAILUREMESSAGE="hospitalviewfailuremessage";
	public static final String HOSPITALEXPORTFAILUREMESSAGE="hospitalexportfailuremessage";
	
	//Identity Mapping Constants
	public static final String IDENTITY_DELETE_SUCCESS_MESSAGE="identitydeletesuccessmessage";
	public static final String IDENTITY_DELETE_FAILURE_MESSAGE="identitydeletefailuremessage";
	public static final String IDENTITY_MAPPING_SAVE_SUCCESS_MESSAGE="identitymappingsavesuccessmessage";
	public static final String IDENTITY_MAPPING_SAVE_FAIL_MESSAGE="identitymappingsavefailuremessage";
	
	//Pagination 
	public static final int PAGINATION_SIZE = 15;
	public static final String PAGE_IN_SESSION = "pagesession";
	public static final String SIZE_IN_SESSION = "sizesession";
	public static final String DEFAULT_PAGE_SIZE = "15";
	public static final String DEFAULT_PAGE = "1";
	
	public static final String FORWARD = "forward";
	
	/**
	 * 
	 */
	public ControllerConstants() {
		// TODO Auto-generated constructor stub
	}

}
