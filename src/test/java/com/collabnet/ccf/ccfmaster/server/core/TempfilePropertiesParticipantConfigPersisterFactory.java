package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;

/**
 * produces {@link PropertiesConfigItemPersister} instances that save data
 * to a temporary file that is deleted on JVM exit.
 * 
 * @author ctaylor
 * 
 */
public class TempfilePropertiesParticipantConfigPersisterFactory implements
		ParticipantConfigPersisterFactory {

	ConcurrentHashMap<Participant, File> fileCache = new ConcurrentHashMap<Participant, File>();
	
	@Override
	public Persister<ParticipantConfig> get(Participant participant) {
		try {
			File newFile = File.createTempFile("test-" + participant.getSystemKind(), ".properties");
			newFile.deleteOnExit();
			File cacheFile = fileCache.putIfAbsent(participant, newFile);
			if (cacheFile == null) {
				return new PropertiesConfigItemPersister<ParticipantConfig>(newFile);
			} else {
				// a file for this participant was in cache already, use it and
				// delete the unused replacement.
				newFile.delete();
				return new PropertiesConfigItemPersister<ParticipantConfig>(cacheFile);
			}
		} catch (IOException e) {
			throw new CoreConfigurationException(e);
		}
	}

}
