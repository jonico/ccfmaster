package com.collabnet.ccf.ccfmaster.web.helper;





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
}
