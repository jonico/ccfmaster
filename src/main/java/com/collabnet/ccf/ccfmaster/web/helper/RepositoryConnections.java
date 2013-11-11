package com.collabnet.ccf.ccfmaster.web.helper;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.main.TeamForgeClient;
import com.collabnet.teamforge.api.planning.PlanningClient;
import com.collabnet.teamforge.api.planning.PlanningFolderDO;
import com.collabnet.teamforge.api.tracker.ArtifactDO;
import com.collabnet.teamforge.api.tracker.TrackerClient;
import com.collabnet.teamforge.api.tracker.TrackerDO;
import com.google.common.base.Function;
import com.google.common.collect.ComputationException;
import com.google.common.collect.MapMaker;

public class RepositoryConnections {

    public static final class ArtifactDetail {
        private final String icon;
        private final String description;
        private final String artifactId;

        public ArtifactDetail(String icon, String description, String artifactId) {
            this.icon = icon;
            this.description = description;
            this.artifactId = artifactId;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

    public static final class RepositoryDetail {
        private final String icon;
        private final String description;
        private final String repositoryId;

        public RepositoryDetail(String icon, String description,
                String repositoryId) {
            this.icon = icon;
            this.description = description;
            this.repositoryId = repositoryId;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

        public String getRepositoryId() {
            return repositoryId;
        }
    }

    static final class ArtifactDetailFinder implements Function<String, ArtifactDetail> {
        @Override
        public ArtifactDetail apply(String artifactId) {
            try {
                Connection connection = TeamForgeConnectionHelper
                        .teamForgeConnection();
                return detailsForArtifactId(connection, artifactId);
            } catch (RemoteException e) {
                throw new ComputationException(e);
            }
        }

        static ArtifactDetail detailsForArtifactId(
                Connection teamforgeConnection, String artifactId)
                throws RemoteException {
            ArtifactDetail artifactDetail = null;
            final TrackerClient trackerClient = teamforgeConnection
                    .getTrackerClient();
            if (isArtifact(artifactId)) {
                String folderid = trackerClient.getArtifactDataFull(artifactId)
                        .getFolderId();
                final TrackerDO trackerData = trackerClient
                        .getTrackerData(folderid);
                final ArtifactDO artifactData = trackerClient
                        .getArtifactData(artifactId);
                artifactDetail = new ArtifactDetail(trackerData.getIcon(),
                        artifactData.getTitle(), artifactId);
            } else if (isTrackerRepository(artifactId)) {
                final TrackerDO trackerData = trackerClient
                        .getTrackerData(artifactId);
                artifactDetail = new ArtifactDetail(trackerData.getIcon(),
                        trackerData.getTitle(), artifactId);
            } else if (isPlan(artifactId)) {
                final PlanningClient planningClient = teamforgeConnection
                        .getPlanningClient();
                final PlanningFolderDO planningdata = planningClient
                        .getPlanningFolderData(artifactId);
                artifactDetail = new ArtifactDetail(FOLDER_CLOSED_GIF,
                        planningdata.getTitle(), artifactId);
            } else {
                artifactDetail = new ArtifactDetail("null", "null", artifactId);
            }
            return artifactDetail;
        }

    }

    static final class RepositoryDetailFinder implements Function<String, RepositoryDetail> {
        @Override
        public RepositoryDetail apply(String repositoryId) {
            try {
                Connection connection = TeamForgeConnectionHelper
                        .teamForgeConnection();
                return detailsFor(connection, repositoryId);
            } catch (RemoteException e) {
                throw new ComputationException(e);
            }
        }

        static RepositoryDetail detailsFor(Connection teamforgeConnection,
                String tfRepositoryId) throws RemoteException {
            RepositoryDetail repositoryDetail = null;
            final TrackerClient trackerClient = teamforgeConnection
                    .getTrackerClient();
            if (isTrackerRepository(tfRepositoryId)) {
                final TrackerDO trackerData = trackerClient
                        .getTrackerData(tfRepositoryId);
                repositoryDetail = new RepositoryDetail(trackerData.getIcon(),
                        trackerData.getTitle(), tfRepositoryId);
            } else if (isPlanningFolderRepository(tfRepositoryId)) {
                String sourceProjectId = extractProjectFromRepositoryId(tfRepositoryId);
                final TeamForgeClient teamForgeClient = teamforgeConnection
                        .getTeamForgeClient();
                repositoryDetail = new RepositoryDetail(FOLDER_CLOSED_GIF,
                        teamForgeClient.getProjectData(sourceProjectId)
                                .getTitle(), sourceProjectId);
            } else if (isTrackerMetaDataRepository(tfRepositoryId)) {
                String sourceProjectId = extractTrackerFromMetaDataRepositoryId(tfRepositoryId);
                final TrackerDO trackerData = trackerClient
                        .getTrackerData(sourceProjectId);
                repositoryDetail = new RepositoryDetail(trackerData.getIcon(),
                        trackerData.getTitle() + METADATA, sourceProjectId);
            } else {
                repositoryDetail = new RepositoryDetail("null", "null",
                        tfRepositoryId);
            }
            return repositoryDetail;
        }
    }

    private static final String                                  METADATA              = " : Metadata";

    private static final String                                  FOLDER_CLOSED_GIF     = "folder_closed.gif";

    private static final Logger                                  log                   = LoggerFactory
                                                                                               .getLogger(RepositoryConnections.class);

    private static final ConcurrentMap<String, RepositoryDetail> repositoryDetailCache = new MapMaker()
                                                                                               .maximumSize(
                                                                                                       1000)
                                                                                               .expireAfterWrite(
                                                                                                       20,
                                                                                                       TimeUnit.MINUTES)
                                                                                               .softValues()
                                                                                               .makeComputingMap(
                                                                                                       new RepositoryDetailFinder());

    private static final ConcurrentMap<String, ArtifactDetail>   artifactDetailCache   = new MapMaker()
                                                                                               .maximumSize(
                                                                                                       1000)
                                                                                               .expireAfterWrite(
                                                                                                       20,
                                                                                                       TimeUnit.MINUTES)
                                                                                               .softValues()
                                                                                               .makeComputingMap(
                                                                                                       new ArtifactDetailFinder());

    // prevent instantiation
    private RepositoryConnections() {
    }

    public static ArtifactDetail detailsForArtifact(String tfRepositoryId) {
        try {
            return artifactDetailCache.get(tfRepositoryId);
        } catch (ComputationException e) {
            log.info("caught RemoteException while getting details for "
                    + tfRepositoryId + ", returning dummy.", e);
            return new ArtifactDetail("null", "null", tfRepositoryId);
        }
    }

    public static RepositoryDetail detailsForRepository(String tfRepositoryId) {
        try {
            return repositoryDetailCache.get(tfRepositoryId);
        } catch (ComputationException e) {
            log.info("caught RemoteException while getting details for "
                    + tfRepositoryId + ", returning dummy.", e);
            return new RepositoryDetail("null", "null", tfRepositoryId);
        }
    }

    /**
     * If the planning folder repository id contains the project id this will be
     * returned
     * 
     * @param repositoryId
     * @return project id
     */
    public static String extractProjectFromRepositoryId(String repositoryId) {
        if (repositoryId != null) {
            String[] splitRepo = repositoryId.split("-");
            if (splitRepo != null) {
                if (splitRepo.length != 2) {
                    throw new IllegalArgumentException(
                            "Repository id is not valid.");
                } else {
                    return splitRepo[0];
                }
            }
        }
        throw new IllegalArgumentException("Repository id is not valid.");
    }

    /**
     * If the meta data repository id contains the tracker id this will be
     * returned
     * 
     * @param repositoryId
     * @return tracker id
     */
    public static String extractTrackerFromMetaDataRepositoryId(
            String repositoryId) {
        if (repositoryId != null) {
            String[] splitRepo = repositoryId.split("-");
            if (splitRepo != null) {
                if (splitRepo.length != 2) {
                    throw new IllegalArgumentException(
                            "MetaData Repository id is not valid.");
                } else {
                    return splitRepo[0];
                }
            }
        }
        throw new IllegalArgumentException(
                "Meta Data Repository id is not valid.");
    }

    /**
     * @param repositoryId
     * @return
     */
    public static boolean isArtifact(String repositoryId) {
        if (repositoryId.startsWith("artf"))
            return true;
        else
            return false;
    }

    /**
     * @param repositoryId
     * @return
     */
    public static boolean isPlan(String repositoryId) {
        if (repositoryId.startsWith("plan"))
            return true;
        else
            return false;
    }

    /**
     * Returns whether this repository id belongs to a planning folder
     * repository
     * 
     * @param repositoryId
     *            repositoryId
     * @return true if repository id belongs to a planning folder
     */
    public static boolean isPlanningFolderRepository(String repositoryId) {
        return repositoryId.startsWith("proj")
                && repositoryId.endsWith("planningFolders");
    }

    /**
     * Returns whether this repository id belongs to a tracker meta data
     * repository
     * 
     * @param repositoryId
     *            repositoryId
     * @return true if repository id belongs to a tracker meta data repository
     */
    public static boolean isTrackerMetaDataRepository(String repositoryId) {
        return repositoryId.startsWith("tracker")
                && repositoryId.endsWith("MetaData");
    }

    /**
     * Returns whether this repository id belongs to a tracker
     * 
     * @param repositoryId
     *            repositoryId
     * @return true if repository id belongs to a tracker
     */
    public static boolean isTrackerRepository(String repositoryId) {
        return repositoryId.startsWith("tracker")
                && !repositoryId.endsWith("MetaData");
    }

}
