package com.collabnet.ccf.ccfmaster.gp.validator.custom;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.framework.configuration.entities.ProjectCollectionEntity;
import com.microsoft.tfs.core.clients.framework.configuration.entities.TeamFoundationServerEntity;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.project.ProjectCollection;
import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;
import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemTypeCollection;


/**
 * TFSMetadataHelper- used to retrieve TFS metadata information like - collection, project , work item
 * 
 * @author kbalaji
 *
 */
public class TFSMetadataHelper {
	
	private String url;
	
	private String userName;
	
	private String password;	
	
	public String getUrl() {
		return url;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public TFSMetadataHelper(){
		
	}
	
	public List<String> getTFSCollectionList(){		
		TFSTeamProjectCollection  tfsTeamProjectCollection = null;
		List<String> tfsCollectionList = new ArrayList<String>();
		try {
			// TODO: domain is null, if needed in future we need to pass it 
			tfsTeamProjectCollection = new TFSTeamProjectCollection(getUrl(),getUserName(), null, getPassword());
			TeamFoundationServerEntity tfsEntity = tfsTeamProjectCollection.getConfigurationSession(true).getOrganizationalRoot().getTeamFoundationServer();
			ProjectCollectionEntity[] projectList = tfsEntity.getProjectCollections();
			for(ProjectCollectionEntity entity :projectList){
				tfsCollectionList.add(entity.getDisplayName());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(tfsTeamProjectCollection != null) tfsTeamProjectCollection.close();
		}
		return tfsCollectionList;
		
	}
	
	public List<String> getTFSProjectList(String collectionName){		
		TFSTeamProjectCollection  tfsTeamProjectCollection = null;
		List<String> tfsCollectionList = new ArrayList<String>();
		try{
			String collectionSpecificUrl = String.format("%s%s",getUrl(),collectionName);
			// TODO: domain is null, if needed in future we need to pass it 
			tfsTeamProjectCollection = new TFSTeamProjectCollection(collectionSpecificUrl,getUserName(), null, getPassword()); 
			ProjectCollection projects = tfsTeamProjectCollection.getWorkItemClient().getProjects();
			for (Project project : projects) {
				tfsCollectionList.add(project.getName());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(tfsTeamProjectCollection != null) tfsTeamProjectCollection.close();
		}
		return tfsCollectionList;
		
	}
	
	public List<String> getTFSWorkItemList(String collectionName, String projectName){		
		TFSTeamProjectCollection  tfsTeamProjectCollection = null;
		List<String> tfsWorkItemList = new ArrayList<String>();
		try{
			String collectionSpecificUrl = String.format("%s%s",getUrl(),collectionName);
			// TODO: domain is null, if needed in future we need to pass it 
			tfsTeamProjectCollection = new TFSTeamProjectCollection(collectionSpecificUrl,getUserName(), null, getPassword()); 
			WorkItemTypeCollection workItemTypes = tfsTeamProjectCollection.getWorkItemClient().getProjects().get(projectName).getWorkItemTypes();
			for (WorkItemType type : workItemTypes) {
				tfsWorkItemList.add(type.getName());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(tfsTeamProjectCollection != null) tfsTeamProjectCollection.close();
		}
		return tfsWorkItemList;
		
	}
	
/*	public static void main(String[] args) {
		TFSMetadataHelper metadataHelper1 = new TFSMetadataHelper("http://cu104.cloud.sp.collab.net:8080/tfs/", "kabalaji", "Collabnet1!");
		System.out.println(metadataHelper1.getTFSCollectionList().toString());
		//[ccf, ccf22, ccf23, DefaultCollection]
		System.out.println(metadataHelper1.getTFSProjectList("ccf22").toString());
		//[CCF21Project, CCF2Project]
		System.out.println(metadataHelper1.getTFSWorkItemList("ccf22", "CCF21Project").toString());
	}*/

}
