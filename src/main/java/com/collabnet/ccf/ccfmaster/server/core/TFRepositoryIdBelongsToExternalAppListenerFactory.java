package com.collabnet.ccf.ccfmaster.server.core;

import java.rmi.RemoteException;

import org.springframework.security.core.context.SecurityContextHolder;

import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.main.ProjectDO;
import com.collabnet.teamforge.api.tracker.TrackerDO;

public class TFRepositoryIdBelongsToExternalAppListenerFactory implements RepositoryIdBelongsToExternalAppListenerFactory {

	@Override
	public RepositoryIdBelongsToExternalAppListener get() {
		return new RepositoryIdBelongsToExternalAppListener() {

			@Override
			public void beforeCreate(RepositoryMapping rMapping) {
				try{
					verifyRepositoryIdBelongsToExternalApp(rMapping);
				}
				catch (Exception e){
					throw new CoreConfigurationException(e);
				}
			}

			@Override
			public void beforeMerge(RepositoryMapping rMapping) {
				try{
					verifyRepositoryIdBelongsToExternalApp(rMapping);
				}
				catch (Exception e){
					throw new CoreConfigurationException(e);
				}
			}

		};
	}

	
	/**
	 * @param rMapping
	 * @throws RemoteException
	 */
	private void verifyRepositoryIdBelongsToExternalApp(
			RepositoryMapping rMapping) throws RemoteException {
		Connection teamforgeConnection = getTeamForgeConnection();
		String externalAppProjectId = getProjectIdFromExternalApp(rMapping, teamforgeConnection);
		String projectId = getProjectIdFromObjectId(rMapping,teamforgeConnection);
		if (!externalAppProjectId.equals(projectId)) {
			throw new CoreConfigurationException("TeamForge Repository Id does not belong to the External App.");
		}
	}
	
	
	/**
	 * @param rMapping
	 * @param teamforgeConnection
	 * @return
	 * @throws RemoteException
	 */
	private String getProjectIdFromObjectId(RepositoryMapping rMapping,
			Connection teamforgeConnection) throws RemoteException {
			String projectId=null; 
		String tfRepositoryId = extractObjectIdFromTFRespositoryId(rMapping);  
		if(RepositoryConnections.isTrackerRepository(tfRepositoryId)){
			TrackerDO trackerData =teamforgeConnection.getTrackerClient().getTrackerData(tfRepositoryId);
			projectId=trackerData.getProjectId();
		}
		else{
			projectId=tfRepositoryId;
		}
		return projectId;
	}

	/**
	 * @param rMapping
	 * @param teamforgeConnection
	 * @return
	 * @throws RemoteException
	 */
	private String getProjectIdFromExternalApp(
			RepositoryMapping rMapping, Connection teamforgeConnection)throws RemoteException {
		ProjectDO projectDO=teamforgeConnection.getTeamForgeClient().getProjectDataByPath(rMapping.getExternalApp().getProjectPath());
		return projectDO.getId();
	}

	
	/**
	 * @param repositoryMappingDirection
	 * @return
	 */
	private String extractObjectIdFromTFRespositoryId(RepositoryMapping rMapping) {
		String sourceRepositoryId =rMapping.getTeamForgeRepositoryId(); 
		String tfRepositoryId=null;
		if(RepositoryConnections.isTrackerRepository(sourceRepositoryId)){
			tfRepositoryId=sourceRepositoryId;
		}
		else if (RepositoryConnections.isPlanningFolderRepository(sourceRepositoryId)) {
			tfRepositoryId=RepositoryConnections.extractProjectFromRepositoryId(sourceRepositoryId);
		}
		else if (RepositoryConnections.isTrackerMetaDataRepository(sourceRepositoryId)) {
			tfRepositoryId=RepositoryConnections.extractTrackerFromMetaDataRepositoryId(sourceRepositoryId);
		}
		else{
			tfRepositoryId=sourceRepositoryId;
		}
		return tfRepositoryId;
	}


	public Connection getTeamForgeConnection() throws RemoteException {
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		TFUserDetails tfUser=(TFUserDetails)user;
		Connection teamforgeConnection=tfUser.getConnection();
		return teamforgeConnection;
	}


}

