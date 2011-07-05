package com.collabnet.ccf.ccfmaster.controller.api;

/**
 * This class holds the base paths for all API URLs.
 * 
 * <ul>
 *   <li> GET $resource -- retrieves a list of all entities of the resource</li>
 *   <li> GET $resource/$id -- retrieve a single entity</li>
 *   <li> POST $resource -- create a new entity (for the XML to use in the request body, see the result of the GET above, but omit the &lt;id&gt; and &lt;version&gt; tags)</li>
 *   <li> PUT $resource/$id -- modify the entity (again, XML in the request body, but &lt;id&gt; and &lt;version&gt; need to be included, &lt;id&gt; must match the id in URL, &lt;version&gt; must be the current version)</li>
 *   <li> DELETE $resource/$id -- delete the entity
 * </ul>
 * @author ctaylor
 *
 */
public final class Paths {
	static final String API_PREFIX							= "/api";
	static final String LINKID_PREFIX						= API_PREFIX + "/linkid/{linkId}";

	static final String TIMEZONES							= API_PREFIX + "/timezones";
	static final String DIRECTION							= API_PREFIX + "/directions";
	static final String LOG_FILE							= DIRECTION  + "/{directionId}/logs";
	static final String DIRECTION_CONFIG					= API_PREFIX + "/directionconfigs";
	static final String EXTERNAL_APP						= API_PREFIX + "/externalapps";
	static final String IDENTITY_MAPPING					= API_PREFIX + "/identitymappings";
	static final String LANDSCAPE							= API_PREFIX + "/landscapes";
	static final String LANDSCAPE_CONFIG					= API_PREFIX + "/landscapeconfigs";
	static final String PARTICIPANT							= API_PREFIX + "/participants";
	static final String PARTICIPANT_CONFIG					= API_PREFIX + "/participantconfigs";
	static final String REPOSITORY_MAPPING					= API_PREFIX + "/repositorymappings";
	static final String REPOSITORY_MAPPING_DIRECTION		= API_PREFIX + "/repositorymappingdirections";
	static final String HOSPITALY_ENTRY						= API_PREFIX + "/hospitalentrys";
	static final String FIELD_MAPPING                       = API_PREFIX + "/fieldmappings";
	static final String FIELD_MAPPING_LANDSCAPE_TEMPLATE    = API_PREFIX + "/fieldmappinglandscapetemplates";
	static final String FIELD_MAPPING_EXTERNAL_APP_TEMPLATE	= API_PREFIX + "/fieldmappingexternalapptemplates";
	static final String CCF_CORE_STATUS						= API_PREFIX + "/ccfcorestatuses";
	
	static final String LINKID_IDENTITY_MAPPING						= LINKID_PREFIX + "/identitymappings";
	static final String LINKID_REPOSITORY_MAPPING					= LINKID_PREFIX + "/repositorymappings";
	static final String LINKID_REPOSITORY_MAPPING_DIRECTION			= LINKID_PREFIX + "/repositorymappingdirections";
	static final String LINKID_HOSPITAL_ENTRY						= LINKID_PREFIX + "/hospitalentrys";
	static final String LINKID_FIELD_MAPPING						= LINKID_PREFIX + "/fieldmappings";
	static final String LINKID_FIELD_MAPPING_EXTERNAL_APP_TEMPLATE	= LINKID_PREFIX + "/fieldmappingexternalapptemplates";

	/** prevent instantiation */
	private Paths() {}
}
