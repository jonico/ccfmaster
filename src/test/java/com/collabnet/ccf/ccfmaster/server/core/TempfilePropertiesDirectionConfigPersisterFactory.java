package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;

/**
 * produces {@link PropertiesConfigItemPersister} instances that save data to a
 * temporary file that is deleted on JVM exit.
 * 
 * @author ctaylor
 * 
 */
public class TempfilePropertiesDirectionConfigPersisterFactory implements DirectionConfigPersisterFactory {

    ConcurrentHashMap<Direction, File> fileCache = new ConcurrentHashMap<Direction, File>();

    @Override
    public Persister<DirectionConfig> get(Direction direction) {
        try {
            File newFile = File.createTempFile("test-" + direction.getId(),
                    ".properties");
            newFile.deleteOnExit();
            File cacheFile = fileCache.putIfAbsent(direction, newFile);
            if (cacheFile == null) {
                return new PropertiesConfigItemPersister<DirectionConfig>(
                        newFile);
            } else {
                // a file for this direction was in cache already, use it and
                // delete the unused replacement.
                newFile.delete();
                return new PropertiesConfigItemPersister<DirectionConfig>(
                        cacheFile);
            }
        } catch (IOException e) {
            throw new CoreConfigurationException(e);
        }
    }

}
