package com.collabnet.ccf.ccfmaster.server.core;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;


public class PreventLandscapeModificationLandscapeModificationListenerFactory implements
		LandscapeModificationListenerFactory {

	@Override
	public LandscapeModificationListener get() {
		return new AbstractLandscapeModificationListener() {

			@Override
			public Landscape beforeMerge(Landscape landscape) {
				Landscape current = Landscape.findLandscape(landscape.getId());
				if (!current.getName().equals(landscape.getName())) {
					throw new CoreConfigurationException("modifying landscape.description is not allowed.");
				}
				return landscape;
			}

		};
	}

}
