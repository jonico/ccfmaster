package com.collabnet.ccf.ccfmaster.server.core.update;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.config.Version;
import com.collabnet.ccf.ccfmaster.server.core.SingleLandscapeCCFCoreInteractionStrategy;
import com.google.common.base.Preconditions;

public final class CoreZipFile implements Closeable, Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(CoreZipFile.class);
	
	private transient File file = null;
	private transient CoreProperties coreProperties = null;
	@Autowired
	transient CCFRuntimePropertyHolder ccfRuntimePropertyHolder;
	
	public static CoreZipFile fromMultipartFile(MultipartFile upload) throws ZipException, IOException {
		if (upload.isEmpty()) 
			return null;
		File tempFile = null;
		try {
			tempFile = File.createTempFile("core", "zip");
			upload.transferTo(tempFile);
			return new CoreZipFile(tempFile);
		} catch (IOException e) {
			cleanupTempFile(tempFile);
			throw e;
		}
	}

	public static CoreZipFile fromServerInstance(File resourceFile) throws ZipException, IOException {
		File tempFile = null;
		try {
			tempFile = File.createTempFile("core", "zip");
			FileUtils.copyFile(resourceFile, tempFile);
			return new CoreZipFile(tempFile);
		} catch (IOException e) {
			cleanupTempFile(tempFile);
			throw e;
		}
	}

	private static void cleanupTempFile(File tempFile) {
		// avoid having invalid uploaded files lying around
		if (tempFile != null && !tempFile.delete()) 
			tempFile.deleteOnExit();
	}

	public CoreZipFile() {
		// for spring compatibility, serialization
	}
	
	public CoreZipFile(File file) throws ZipException, IOException {
		this.setFile(file);
	}
	
	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		s.writeObject(file);
	}
	
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		final File f = (File) s.readObject();
		if (f != null)
			setFile(f);
	}
	
	/**
	 * can be used once to setup this CoreZipFile instance if the no-arg constructor was used.
	 * @throws IllegalStateException if a file is set already
	 */
	public void setFile(File file) throws ZipException, IOException {
		Preconditions.checkState(this.file == null, "Core file is already set", this.file);
		this.file = Preconditions.checkNotNull(file, "Core file can't be null");
		Preconditions.checkArgument(file.exists(), "Core file doesn't exist", file);
		this.coreProperties = CoreProperties.of(file);
	}
	
	public File getFile() {
		return this.file;
	}
	
	/**
	 * unzips this core file to {@code destinationDir}
	 * @param destinationDir
	 * @throws IOException if unzipping fails
	 * @throws CoreUpdateException if this core is not newer than the one at {@code destinationDir}
	 */
	public void unzipTo(File destinationDir) throws IOException {
		Preconditions.checkNotNull(destinationDir, "passed null destination");
		Preconditions.checkArgument(destinationDir.exists() && destinationDir.isDirectory(), "destination must be a directory that exists", destinationDir);
		Preconditions.checkState(this.file != null && this.file.exists(), "core zip file doesn't exist", this.file);
		Preconditions.checkState(this.validate(), "core zip file is invalid", this.file);
		final Version landscapeVersion = CoreProperties.ofDirectory(destinationDir).getVersion();
		final Version myVersion = getVersion();
		if (myVersion.isNewerThan(landscapeVersion)) {
			log.debug("unzipping {} (version {}) to {}", new Object[]{this.file, myVersion, destinationDir}); 
			SingleLandscapeCCFCoreInteractionStrategy.unzip(this.file, destinationDir);
		} else {
			throw new CoreUpdateException(String.format(
					"uploaded core (version %s) was not newer than existing landscape (version %s)",
					myVersion, landscapeVersion));
		}
	}
		
	public boolean validate(){
		return getDescription() != null && getVersion() != null; 
	}
	
	public String getDescription() {
		return coreProperties.getDescription();
	}
	
	public Version getVersion() {
		return coreProperties.getVersion();
	}

	public String getSaasMode(){
		return  ccfRuntimePropertyHolder.getSaasMode().toString();
	}
		
	
	/**
	 * deletes the temporary file.
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException {
		if (file != null)
			file.delete();
	}
}
