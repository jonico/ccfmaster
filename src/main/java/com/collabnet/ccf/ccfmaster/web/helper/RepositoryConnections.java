package com.collabnet.ccf.ccfmaster.web.helper;

import java.rmi.RemoteException;

import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.main.TeamForgeClient;
import com.collabnet.teamforge.api.planning.PlanningClient;
import com.collabnet.teamforge.api.planning.PlanningFolderDO;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.collabnet.teamforge.api.tracker.TrackerClient;
import com.collabnet.teamforge.api.tracker.TrackerDO;





public class RepositoryConnections {

	
	/**
	 * Returns whether this repository id belongs to a tracker
	 * @param repositoryId repositoryId
	 * @return true if repository id belongs to a tracker
	 */
	public static boolean isTrackerRepository(String repositoryId) {
		return repositoryId.startsWith("tracker") && !repositoryId.endsWith("MetaData");
	}
	
	/**
	 * Returns whether this repository id belongs to a planning folder repository
	 * @param repositoryId repositoryId
	 * @return true if repository id belongs to a planning folder
	 */
	public static boolean isPlanningFolderRepository(String repositoryId) {
		return repositoryId.startsWith("proj") && repositoryId.endsWith("planningFolders");
	}
	
	/**
	 * Returns whether this repository id belongs to a tracker meta data repository
	 * @param repositoryId repositoryId
	 * @return true if repository id belongs to a tracker meta data repository
	 */
	public static boolean isTrackerMetaDataRepository(String repositoryId) {
		return repositoryId.startsWith("tracker") && repositoryId.endsWith("MetaData");
	}
	
	
	
	/**
	 * @param repositoryId
	 * @return
	 */
	public static boolean isArtifact(String repositoryId) {
		if(repositoryId.startsWith("artf"))return true;
		else return false;
	}
	
	/**
	 * @param repositoryId
	 * @return
	 */
	public static boolean isPlan(String repositoryId) {
		if(repositoryId.startsWith("plan"))return true;
		else return false;
	}
	
	/**
	 * If the planning folder repository id contains the project id this will be returned
	 * @param repositoryId
	 * @return project id
	 */
	public static String extractProjectFromRepositoryId(String repositoryId) {
		if(repositoryId != null){
			String[] splitRepo = repositoryId.split("-");
			if(splitRepo != null){
				if(splitRepo.length != 2){
					throw new IllegalArgumentException("Repository id is not valid.");
				}
				else {
					return splitRepo[0];
				}
			}
		}
		throw new IllegalArgumentException("Repository id is not valid.");
	}
	
	/**
	 * If the meta data repository id contains the tracker id this will be returned
	 * @param repositoryId
	 * @return tracker id
	 */
	public static String extractTrackerFromMetaDataRepositoryId(String repositoryId) {
		if(repositoryId != null){
			String[] splitRepo = repositoryId.split("-");
			if(splitRepo != null){
				if(splitRepo.length != 2){
					throw new IllegalArgumentException("MetaData Repository id is not valid.");
				}
				else {
					return splitRepo[0];
				}
			}
		}
		throw new IllegalArgumentException("Meta Data Repository id is not valid.");
	}

	private static final String METADATA = " : Metadata";
	private static final String FOLDER_CLOSED_GIF = "folder_closed.gif";

	public static RepositoryDetail detailsFor(Connection teamforgeConnection,
			String tfRepositoryId) throws RemoteException {
		RepositoryDetail repositoryDetail=null;
		final TrackerClient trackerClient = teamforgeConnection.getTrackerClient();
		if(isTrackerRepository(tfRepositoryId)){
			final TrackerDO trackerData = trackerClient.getTrackerData(tfRepositoryId);
			repositoryDetail=new RepositoryDetail(trackerData.getIcon(), trackerData.getTitle(), tfRepositoryId);
		}
		else if (isPlanningFolderRepository(tfRepositoryId)) {
			String sourceProjectId=extractProjectFromRepositoryId(tfRepositoryId);
			final TeamForgeClient teamForgeClient = teamforgeConnection.getTeamForgeClient();
			repositoryDetail=new RepositoryDetail(FOLDER_CLOSED_GIF, teamForgeClient.getProjectData(sourceProjectId).getTitle(), sourceProjectId);
		}
		else if (isTrackerMetaDataRepository(tfRepositoryId)) {
			String sourceProjectId=extractTrackerFromMetaDataRepositoryId(tfRepositoryId);
			final TrackerDO trackerData = trackerClient.getTrackerData(sourceProjectId);
			repositoryDetail=new RepositoryDetail(trackerData.getIcon(), trackerData.getTitle()+METADATA, sourceProjectId);
		}
		else{
			repositoryDetail=new RepositoryDetail("null", "null", tfRepositoryId);
		}
		
		
		
		return repositoryDetail;
	}
	
	/**
	 * returns a String, consisting of three dash-separated values:
	 * <ul>
	 * <li>an icon filename for this repository id</li>
	 * <li>a description</li>
	 * <li>a repository (tracker/project) Id
	 * <li>
	 * </ul>
	 * @param teamforgeConnection
	 * @param sourceRepositoryId a TeamForge ID for a tracker, a project, or tracker metadata.
	 * @return the string described above, or null if sourceRepositoryId is not valid.
	 * @throws RemoteException if communication with CTF fails
	 */
	public static String repositoryDetail(Connection teamforgeConnection,
			String sourceRepositoryId) throws RemoteException {
		RepositoryDetail detail = detailsFor(teamforgeConnection, sourceRepositoryId);
		if (detail == null) return null;
		return String.format("%s-%s-%s", detail.getIcon(), detail.getDescription(), detail.getRepositoryId());
	}
	
	public static final class RepositoryDetail {
		private final String icon;
		private final String description;
		private final String repositoryId;
		
		public RepositoryDetail(String icon, String description, String repositoryId) {
			this.icon = icon;
			this.description = description;
			this.repositoryId = repositoryId;
		}

		public String getIcon() {
			return icon;
		}

		public String getDescription() {
			return description;
		}

		public String getRepositoryId() {
			return repositoryId;
		}
	}

	
	public static final class ArtifactDetail {
		private final String icon;
		private final String description;
		private final String artifactId;
		
		public ArtifactDetail(String icon, String description, String artifactId) {
			this.icon = icon;
			this.description = description;
			this.artifactId = artifactId;
		}

		public String getIcon() {
			return icon;
		}

		public String getDescription() {
			return description;
		}

		public String getArtifactId() {
			return artifactId;
		}
	}
	
	public static RepositoryDetail detailsFor(String tfRepositoryId){
			try{ 
				Connection connection = TeamForgeConnectionHelper.teamForgeConnection();
				return detailsFor(connection, tfRepositoryId);
			} catch(RemoteException remoteException) {
	//			log.info("caught RemoteException while getting details for " + tfRepositoryId + ", returning dummy.", remoteException);
				return new RepositoryDetail("null", "null", tfRepositoryId);
			}
		}
	
	public static ArtifactDetail detailsForArtifact(String tfRepositoryId){
		try{ 
			Connection connection = TeamForgeConnectionHelper.teamForgeConnection();
			return detailsForArtifactData(connection, tfRepositoryId);
		} catch(RemoteException remoteException) {
//			log.info("caught RemoteException while getting details for " + tfRepositoryId + ", returning dummy.", remoteException);
			return new ArtifactDetail("null", "null", tfRepositoryId);
		}  
	}
	
	public static ArtifactDetail detailsForArtifactData(Connection teamforgeConnection,
			String artifactId) throws RemoteException {
		ArtifactDetail artifactDetail=null; 
		final TrackerClient trackerClient = teamforgeConnection.getTrackerClient();
		if(isArtifact(artifactId)){
			String folderid=trackerClient.getArtifactDataFull(artifactId).getFolderId();
			final TrackerDO trackerData = trackerClient.getTrackerData(folderid);
			final ArtifactDO artifactData=trackerClient.getArtifactData(artifactId);
			artifactDetail=new ArtifactDetail(trackerData.getIcon(), artifactData.getTitle(), artifactId);
		}
		else if(isTrackerRepository(artifactId)){
			final TrackerDO trackerData = trackerClient.getTrackerData(artifactId);
			artifactDetail=new ArtifactDetail(trackerData.getIcon(), trackerData.getTitle(), artifactId);
		}
		else if(isPlan(artifactId)){
			final PlanningClient planningClient = teamforgeConnection.getPlanningClient();
			final PlanningFolderDO planningdata=planningClient.getPlanningFolderData(artifactId);
			artifactDetail=new ArtifactDetail(FOLDER_CLOSED_GIF, planningdata.getTitle(), artifactId);
		}
		else{
			artifactDetail=new ArtifactDetail("null", "null", artifactId);
		}
		return artifactDetail;
	}

}
