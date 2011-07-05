package com.collabnet.ccf.ccfmaster.server.core;

import org.springframework.beans.factory.annotation.Autowired;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;

privileged public aspect CCFCoreInteractionAspect {
	
	@Autowired
	private transient LandscapeCCFCoreInteractionStrategy Landscape.ccfCoreInteractionStrategy;
	
	@Autowired
	private transient DirectionCCFCoreInteractionStrategy Direction.ccfCoreInteractionStrategy;
	
	public LandscapeCCFCoreInteractionStrategy Landscape.getCCFCoreInteractionStratey () {
		return ccfCoreInteractionStrategy;
	}
	
	public DirectionCCFCoreInteractionStrategy Direction.getCCFCoreInteractionStratey () {
		return ccfCoreInteractionStrategy;
	}
	
	public void Landscape.setCCFCoreInteractionStrategy (LandscapeCCFCoreInteractionStrategy strategy) {
		ccfCoreInteractionStrategy = strategy;
	}
	
	pointcut landscapeCreate(): execution(void Landscape.persist());
	pointcut landscapeDelete(): execution(void Landscape.remove());
	pointcut landscapeUpdate(): execution(Landscape Landscape.merge());
	
	pointcut directionCreate(): execution(void Direction.persist());
	pointcut directionDelete(): execution(void Direction.remove());
	pointcut directionUpdate(): execution(Direction Direction.merge());
	
	void around(Landscape landscape) : landscapeCreate () && target(landscape) {
		proceed(landscape);
		landscape.getCCFCoreInteractionStratey().create(landscape);
	}

	void around(Landscape landscape) : landscapeDelete () && target(landscape) {
		proceed(landscape);
		landscape.getCCFCoreInteractionStratey().delete(landscape);
	}
	
	Landscape around(Landscape landscape) : landscapeUpdate () && target(landscape) {
		Landscape l = proceed(landscape);
		landscape.getCCFCoreInteractionStratey().update(l);
		return l;
	}
	
	void around(Direction direction) : directionCreate () && target(direction) {
		proceed(direction);
		direction.getCCFCoreInteractionStratey().create(direction);
	}

	void around(Direction direction) : directionDelete () && target(direction) {
		proceed(direction);
		direction.getCCFCoreInteractionStratey().delete(direction);
	}
	
	Direction around(Direction direction) : directionUpdate () && target(direction) {
		Direction l = proceed(direction);
		direction.getCCFCoreInteractionStratey().update(l);
		return l;
	}

}
