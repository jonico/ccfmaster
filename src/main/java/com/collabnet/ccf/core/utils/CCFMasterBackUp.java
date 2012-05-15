package com.collabnet.ccf.core.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	private File [] backupFiles = null; // files to be backed up
	
	private JdbcTemplate jdbcTemplate;
	
	private StartCoresOnBootBean startCoresOnBootBean;
	
	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
		loadRequiredDbFiles();
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
		setBackupFilePath(destDir.getAbsolutePath());
		doLandscapeBackup(destDir);
		doDBBackup(destDir);
	}

	protected synchronized void doDBBackup(File destDir) {
		try {
			this.jdbcTemplate.execute(CHECKPOINT_SQL_STRING);
			for (File backupFile : backupFiles) {
				if (backupFile.exists())
					FileUtils.copyFileToDirectory(backupFile, destDir);
			}
		} catch (DataAccessException e) {
			throw new CoreConfigurationException("Accessed denied to Backup Files", e);
		} catch (IOException e) {
			throw new CoreConfigurationException("Backup failed", e);
		}

	}

	protected synchronized void doLandscapeBackup(File destDir) {
		try {
			// TODO: Need to check whether cores are shutdown are not... I guess
			// shutdown internally take cares of it
			startCoresOnBootBean.shutdown();// shutdowns all the cores
			String landscapeDir = ControllerHelper.landscapeDirName(ccfHome);
			FileUtils.copyDirectoryToDirectory(new File(landscapeDir), destDir);
		} catch (IOException e) {
			throw new CoreConfigurationException("Backup failed", e);
		}

	}
	
	private void loadRequiredDbFiles() {
		File dbDir = new File(dbPath);
		File backupFileHolder = dbDir.getAbsoluteFile().getParentFile();
		this.backupFiles = new File[] { 
				new File(backupFileHolder, dbDir.getName() + ".properties"), //required files
				new File(backupFileHolder, dbDir.getName() + ".script"), //required files
	            new File(backupFileHolder, dbDir.getName() + ".backup"),
	            new File(backupFileHolder, dbDir.getName() + ".data"),
	            new File(backupFileHolder, dbDir.getName() + ".log"),
	            new File(backupFileHolder, dbDir.getName() + ".lobs")};

	}
	
	private File createBackupDir() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmssS");
		String dirName = String.format("backup-%s",dateFormat.format(new Date())); 
		return new File(archiveLocation,dirName);//backup dir will be named like - backup-2012-05-15 021000549
	}
	
}
