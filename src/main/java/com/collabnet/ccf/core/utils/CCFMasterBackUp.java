package com.collabnet.ccf.core.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.core.StartCoresOnBootBean;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;


/**
 * CCFMasterBackUp backups DB and landscape
 * 
 * @author kbalaji
 *
 */
public class CCFMasterBackUp {
	
	private static final String CHECKPOINT_SQL_STRING = "CHECKPOINT";

	private String ccfHome;
	
	private String dbPath;

	private String archiveLocation;
	
	private String backupFilePath;
	
	private JdbcTemplate jdbcTemplate;
	
	private StartCoresOnBootBean startCoresOnBootBean;
	
	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	public void setCcfHome(String ccfHome) {
		this.ccfHome = ccfHome;
	}
	
	public void setArchiveLocation(String archiveLocation) {
		this.archiveLocation = archiveLocation;
	}

	public String getBackupFilePath() {
		return backupFilePath;
	}

	public void setBackupFilePath(String backupFilePath) {
		this.backupFilePath = backupFilePath;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setStartCoresOnBootBean(StartCoresOnBootBean startCoresOnBootBean) {
		this.startCoresOnBootBean = startCoresOnBootBean;
	}
	
	public synchronized void doFullBackUp(){
		File destDir = createBackupDir();
		List<Long> runningCoreIds = startCoresOnBootBean.getRunningCoreIds();
		startCoresOnBootBean.shutdown();// shutdowns all the cores
		setBackupFilePath(destDir.getAbsolutePath());
		doLandscapeBackup(destDir);
		doDBBackup(destDir);
		if(!runningCoreIds.isEmpty() && startCoresOnBootBean.allCoresStopped())
			startCoresOnBootBean.boot(runningCoreIds);// starts already started cores
		
	}

	protected synchronized void doDBBackup(File destDir) {
		try {
			this.jdbcTemplate.execute(CHECKPOINT_SQL_STRING);
			File [] backupFiles = loadRequiredDbFiles();
			for (File backupFile : backupFiles) {
				if (backupFile.exists())
					FileUtils.copyFileToDirectory(backupFile, destDir);
			}
		} catch (DataAccessException e) {
			throw new CoreConfigurationException("Accessed denied to Backup Files", e);
		} catch (IOException e) {
			throw new CoreConfigurationException("Backup operation failed- "+e.getMessage(), e);
		}

	}

	protected synchronized void doLandscapeBackup(File destDir) {
		try {
			String landscapeDir = ControllerHelper.landscapeDirName(ccfHome);
			FileUtils.copyDirectoryToDirectory(new File(landscapeDir), destDir);
			File [] participantPropetiesFiles = loadParticipantPropFiles();
			for(File propertyFile: participantPropetiesFiles){
				if (propertyFile.exists())
				FileUtils.copyFileToDirectory(propertyFile, destDir);
			}
		} catch (IOException e) {
			throw new CoreConfigurationException("Backup operation failed- "+e.getMessage(), e);
		}

	}
	
	private File[] loadRequiredDbFiles() {
		File dbDir = new File(this.dbPath);
		File backupFileHolder = dbDir.getAbsoluteFile().getParentFile();
		return new File[] { 
				new File(backupFileHolder, dbDir.getName() + ".properties"), //required files
				new File(backupFileHolder, dbDir.getName() + ".script"), //required files
	            new File(backupFileHolder, dbDir.getName() + ".backup"),
	            new File(backupFileHolder, dbDir.getName() + ".data"),
	            new File(backupFileHolder, dbDir.getName() + ".log"),
	            new File(backupFileHolder, dbDir.getName() + ".lobs")};

	}
	
	private File[] loadParticipantPropFiles(){
		File dir = new File(this.ccfHome);
		return dir.listFiles(new FilenameFilter() {
			private String[] regexs = {"^.*-participant\\.properties","^.*runtimeconfig\\.properties"};
			
	        @Override
	        public boolean accept(File dir, String name) {
	            return findMatch(name);
	        }
	    	
	    	public boolean findMatch( String value){
	    		boolean isValueMatched = false;
	    		for(String regex : this.regexs){
		    		isValueMatched = Pattern.matches(regex, value);
		    		if(isValueMatched) break;
	    		}
				return isValueMatched;
	    	}
	    });
	}
	
	private File createBackupDir() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmssS");
		String dirName = String.format("backup-%s",dateFormat.format(new Date())); 
		return new File(archiveLocation,dirName);//backup dir will be named like - backup-2012-05-15 021000549
	}
	
}
