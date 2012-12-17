package com.collabnet.ccf.ccfmaster.web.helper;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.main.ProjectDO;
import com.collabnet.teamforge.api.main.ProjectList;
import com.collabnet.teamforge.api.main.ProjectRow;
import com.collabnet.teamforge.api.pluggable.IntegratedApplicationClient;
import com.collabnet.teamforge.api.tracker.TrackerList;
import com.collabnet.teamforge.api.tracker.TrackerRow;

/**
 * TeamForgeMetadataHelper- provides util methods to retrieve TeamForge project and tracker metadata information
 * 
 * @author kbalaji
 *
 */
public class TeamForgeMetadataHelper {
	
	private TeamForgeMetadataHelper(){	}
	
	
	public static Map<String,String> getAllTeamForgeProjects() throws RemoteException{
		Map<String,String> projectInfoMap = new LinkedHashMap<String,String>();
		Connection connection;
		connection = TeamForgeConnectionHelper.teamForgeConnection();
		ProjectList projectList = connection.getTeamForgeClient().getProjectList();
		ProjectRow [] projectRows = projectList.getDataRows();
		Arrays.sort(projectRows, new Comparator<ProjectRow>() {
			public int compare(ProjectRow p1, ProjectRow p2) {
				return p1.getTitle().toLowerCase().compareTo(p2.getTitle().toLowerCase());
			}
		});
		for(ProjectRow project: projectRows){
			projectInfoMap.put(project.getId(),project.getTitle());
		}
		return projectInfoMap;
	
	}
	
	
	public static List<Map<String,String>> getAllTrackersOfProject(String projectId) throws RemoteException{
		List<Map<String,String>> projectCollection = new ArrayList<Map<String,String>>();
		Connection connection = TeamForgeConnectionHelper.teamForgeConnection();
		TrackerList trackerList = connection.getTrackerClient().getTrackerList(projectId);
		TrackerRow[] trackerRow =  trackerList.getDataRows();
		for(TrackerRow tracker: trackerRow){			 
			Map<String,String> trackerInfoMap = new HashMap<String,String>();
			trackerInfoMap.put("title",tracker.getTitle());
			trackerInfoMap.put("id",tracker.getId());
			projectCollection.add(trackerInfoMap);
		}
		return projectCollection;
	}
	
	public static String getTFLinkId(Connection connection,String plugId,String projectId){
		try {
			IntegratedApplicationClient client = connection.getIntegratedAppClient(true);
			return client.getLinkPlugIdByPlugId(projectId, plugId);
		} catch (RemoteException e) {
			return null;
		}
	}
	
	public static ProjectDO getTFProjectDetails(Connection connection,String projectId) throws RemoteException{
		ProjectDO project = connection.getTeamForgeClient().getProjectData(projectId);
		return project;
	}
	
	

}
