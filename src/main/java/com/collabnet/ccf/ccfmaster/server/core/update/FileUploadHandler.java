package com.collabnet.ccf.ccfmaster.server.core.update;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageResolver;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.config.Version;
import com.collabnet.ccf.ccfmaster.server.core.SingleLandscapeCCFCoreInteractionStrategy;
import com.collabnet.ccf.ccfmaster.server.core.StartCoresOnBootBean;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class FileUploadHandler extends MultiAction implements Serializable {

	private static final String DATE_FORMAT_STRING = "%1$tY%1$tm%1$td-%1$tH%1$tM%1$tS";

	private static final String CORE_ZIP_FILE_CONTEXT_ATTR = "coreZipFile";
	
	private static final Logger log = LoggerFactory.getLogger(FileUploadHandler.class);

	@Autowired
	transient StartCoresOnBootBean startCoresOnBootBean;
	
	@Autowired
	transient CCFRuntimePropertyHolder ccfRuntimePropertyHolder;
	
	private CoreProperties coreProperties;
	private File landscapeDirectory;
	

	
	private static final long serialVersionUID = 1L;
	/*
	 * transient because of https://jira.springsource.org/browse/SWF-1083
	 * and http://forum.springsource.org/showthread.php?54466-Multipart-File-Upload/page2
	 * If the file is large enough to not be stored in RAM, web flow removes the temp file
	 * before serializing the object and then can't restore it later. Since we call transferTo()
	 * inside CoreZipFile.fromMultipartFile(file), we don't need the object after the first transition
	 * anyway.
	 */
	private transient MultipartFile file;
	private boolean needDefaultCoreToRun;
	private List<Long> runningCoreIds = ImmutableList.of();

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		final List<Landscape> landscapes = Landscape.findAllLandscapes();
		if (landscapes.isEmpty()) {
			throw new IllegalStateException("no landscape created");
		}
		Landscape landscape = landscapes.get(0);
		landscapeDirectory = new File(ccfHome(), "landscape" + landscape.getId());
		coreProperties = CoreProperties.ofDirectory(landscapeDirectory);
	}
	
	public Event coreZipFile(RequestContext context) {
		czf = null;
		try {
			if(needDefaultCoreToRun && file.isEmpty()){
				ServletContext webappContext = (ServletContext)context.getExternalContext().getNativeContext();
				String filePath =webappContext.getRealPath(SingleLandscapeCCFCoreInteractionStrategy.CCFCORE_ZIP);
				if(!filePath.isEmpty()){
					czf = CoreZipFile.fromServerInstance(new File(filePath));
				}
			}else{
				czf = CoreZipFile.fromMultipartFile(file);
			}
		} catch (IOException ignored) {
		}
		if (czf != null && czf.validate()) {
			context.getFlowScope().put(CORE_ZIP_FILE_CONTEXT_ATTR, czf);
			return success();
		} else {
			final MessageResolver msg = new MessageBuilder()
				.error()
				.defaultText("invalid zip file.")
				.code("core.zip.invalid")
				.build();
			context.getMessageContext().addMessage(msg);
			return error();
		}
	}
	
	public Event checkVersion(RequestContext context) {
		Version coreVersion = czf.getVersion();
		Version landscapeVersion = CoreProperties.ofDirectory(landscapeDirectory).getVersion();
		if (coreVersion.isNewerThan(landscapeVersion)) {
			return success();
		} else {
			final MessageResolver msg = new MessageBuilder()
				.error()
				.defaultText("zip file too old.")
				.code("core.zip.older")
				.arg(coreVersion)
				.arg(landscapeVersion)
				.build();
			context.getMessageContext().addMessage(msg);
			return error();
		}
	}
	
	public Event stopCores(RequestContext context) {
		final Iterable<CcfCoreStatus> allCores = Iterables.transform(Direction.findAllDirections(), direction2coreStatus);

		// a previous run that went wrong might have stopped cores
		// so they won't be running anymore.
		if (runningCoreIds.isEmpty()) {
			final Iterable<CcfCoreStatus> runningCores = Iterables.filter(allCores, isRunning);
			runningCoreIds = ImmutableList.copyOf(Iterables.transform(runningCores, core2Id));
		}
		
		startCoresOnBootBean.shutdown();
		if (startCoresOnBootBean.allCoresStopped(allCores)) { 
			return success();
		} else {
			final MessageResolver msg = new MessageBuilder()
				.warning()
				.defaultText("failed to stop cores, please try again.")
				.code("core.stop.failure")
				.build();
			context.getMessageContext().addMessage(msg);
			return error();
		}
	}
	
	public Event createBackup(RequestContext context) {
		Event ret = error();
		final File archiveDir = new File(ccfHome(), "archive");
		// e.g. 20110931-235959-landscape27
		String backupDirName = timestamp() +"-"+ landscapeDirectory.getName();
		final File backupDir = new File(archiveDir, backupDirName);
		try {
			Preconditions.checkState(!backupDir.exists(), "core.backup.exists");
			Preconditions.checkState(backupDir.mkdirs(), "core.backup.create");
			FileUtils.copyDirectory(landscapeDirectory, backupDir);
			ret = success();
		} catch (IllegalStateException e) {
			final MessageResolver msg = new MessageBuilder()
				.error()
				.code(e.getMessage())
				.arg(backupDir)
				.build();
			context.getMessageContext().addMessage(msg);
		} catch (IOException e) {
			final MessageResolver msg = new MessageBuilder()
				.error()
				.code("core.backup.failure")
				.arg(e.getLocalizedMessage())
				.build();
			context.getMessageContext().addMessage(msg);
		}
		return ret;
	}

	/**
	 * @return a timestamp string in the format yyyymmdd-hhmmss
	 */
	String timestamp() {
		return String.format(DATE_FORMAT_STRING, new GregorianCalendar());
	}
	
	public Event performUpdate(RequestContext context) {
		Event result = error();
		try {
			czf.unzipTo(landscapeDirectory);
			result = success();
		} catch (Exception e) {
			log.error("error unzipping core update: " + czf.getFile(), e);
			final MessageResolver msg = new MessageBuilder()
				.error()
				.defaultText("Error updating cores: " + e.getMessage())
				.build();
			context.getMessageContext().addMessage(msg);
		}
		return result;
	}

	/**
	 * @return
	 */
	File ccfHome() {
		File ccfHome = new File(ccfRuntimePropertyHolder.getCcfHome());
		return ccfHome;
	}
	
	public Event startCores(RequestContext context) {
		Preconditions.checkState(runningCoreIds != null, "cores were null");
		
		for (Long directionId : runningCoreIds) {
			CcfCoreStatus core = CcfCoreStatus.findCcfCoreStatus(directionId);
			core.setExecutedCommand(ExecutedCommand.START);
			core.merge();
		}
		return success();
	}
	
	static Function<CcfCoreStatus, Long> core2Id = new Function<CcfCoreStatus, Long>() {
		@Override
		public Long apply(CcfCoreStatus input) {
			return input.getId();
		}
	};
	
	static Function<Direction, CcfCoreStatus> direction2coreStatus = new Function<Direction, CcfCoreStatus>() {
		@Override
		public CcfCoreStatus apply(Direction input) {
			return CcfCoreStatus.findCcfCoreStatus(input.getId());
		}
	};

	static Predicate<CcfCoreStatus> isRunning = new Predicate<CcfCoreStatus>() {
		@Override
		public boolean apply(CcfCoreStatus ccs) {
			return ccs.getCurrentStatus() == CoreState.STARTED;
		}
	};

	private CoreZipFile czf;

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	public Version getCurrentVersion() {
		return coreProperties.getVersion();
	}
	
	public String getCurrentDescription() {
		return coreProperties.getDescription();
	}
	
	public String getSaasMode(){
		return  ccfRuntimePropertyHolder.getSaasMode().toString();
	}

	/**
	 * @return the needDefaultCoreToRun
	 */
	public boolean getNeedDefaultCoreToRun() {
		return needDefaultCoreToRun;
	}

	/**
	 * @param needDefaultCoreToRun the needDefaultCoreToRun to set
	 */
	public void setNeedDefaultCoreToRun(boolean needDefaultCoreToRun) {
		this.needDefaultCoreToRun = needDefaultCoreToRun;
	}
	
}
