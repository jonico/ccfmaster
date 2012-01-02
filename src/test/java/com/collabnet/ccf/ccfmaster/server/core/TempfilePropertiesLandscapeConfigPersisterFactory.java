package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;

/**
 * produces {@link PropertiesConfigItemPersister} instances that save data
 * to a temporary file that is deleted on JVM exit.
 * 
 * @author ctaylor
 * 
 */
public class TempfilePropertiesLandscapeConfigPersisterFactory implements
		LandscapeConfigPersisterFactory {

	ConcurrentHashMap<Landscape, File> fileCache = new ConcurrentHashMap<Landscape, File>();
	
	@Override
	public Persister<LandscapeConfig> get(Landscape landscape) {
		try {
			File newFile = File.createTempFile("test-landscape", ".properties");
			newFile.deleteOnExit();
			File cacheFile = fileCache.putIfAbsent(landscape, newFile);
			if (cacheFile == null) {
				return new PropertiesConfigItemPersister<LandscapeConfig>(newFile);
			} else {
				// a file for this landscape was in cache already, use it and
				// delete the unused replacement.
				newFile.delete();
				return new PropertiesConfigItemPersister<LandscapeConfig>(cacheFile);
			}
		} catch (IOException e) {
			throw new CoreConfigurationException(e);
		}
	}

}
