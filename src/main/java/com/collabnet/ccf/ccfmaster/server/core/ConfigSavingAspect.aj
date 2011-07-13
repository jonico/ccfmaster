package com.collabnet.ccf.ccfmaster.server.core;

import org.springframework.beans.factory.annotation.Autowired;

import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;

privileged public aspect ConfigSavingAspect {

	// couldn't figure out how to make this generic - type erasure seemed to get in the way :(.
	
	public Persister<ParticipantConfig> ParticipantConfig.getPersister() {
		return persisterFactory.get(participant);
	}

	@Autowired
	private transient ParticipantConfigPersisterFactory ParticipantConfig.persisterFactory;
	
	
	public Persister<DirectionConfig> DirectionConfig.getPersister() {
		return persisterFactory.get(direction);
	}
	
	@Autowired
	private transient DirectionConfigPersisterFactory DirectionConfig.persisterFactory;
	
	public Persister<LandscapeConfig> LandscapeConfig.getPersister() {
		return persisterFactory.get(landscape);
	}

	@Autowired
	private transient LandscapeConfigPersisterFactory LandscapeConfig.persisterFactory;
	
	
		
	void around(ParticipantConfig cfg) : execution(void ParticipantConfig.persist()) && target(cfg) {
		proceed(cfg);
		Persister<ParticipantConfig> strategy = cfg.getPersister();
		strategy.save(cfg);
	}

	ParticipantConfig around(ParticipantConfig cfg) : execution(ParticipantConfig ParticipantConfig.merge()) && target(cfg) {
		ParticipantConfig ret = proceed(cfg);
		Persister<ParticipantConfig> strategy = cfg.getPersister();
		strategy.save(ret);
		return ret;
	}

	void around(ParticipantConfig cfg) : execution(void ParticipantConfig.remove()) && target(cfg) {
		proceed(cfg);
		Persister<ParticipantConfig> strategy = cfg.getPersister();
		strategy.delete(cfg);
	}

	void around(DirectionConfig cfg) : execution(void DirectionConfig.persist()) && target(cfg) {
		proceed(cfg);
		Persister<DirectionConfig> strategy = cfg.getPersister();
		strategy.save(cfg);
	}

	DirectionConfig around(DirectionConfig cfg) : execution(DirectionConfig DirectionConfig.merge()) && target(cfg) {
		DirectionConfig ret = proceed(cfg);
		Persister<DirectionConfig> strategy = cfg.getPersister();
		strategy.save(ret);
		return ret;
	}

	void around(DirectionConfig cfg) : execution(void DirectionConfig.remove()) && target(cfg) {
		proceed(cfg);
		Persister<DirectionConfig> strategy = cfg.getPersister();
		strategy.delete(cfg);
	}


	void around(LandscapeConfig cfg) : execution(void LandscapeConfig.persist()) && target(cfg) {
		proceed(cfg);
		Persister<LandscapeConfig> strategy = cfg.getPersister();
		strategy.save(cfg);
	}

	LandscapeConfig around(LandscapeConfig cfg) : execution(LandscapeConfig LandscapeConfig.merge()) && target(cfg) {
		LandscapeConfig ret = proceed(cfg);
		Persister<LandscapeConfig> strategy = cfg.getPersister();
		strategy.save(ret);
		return ret;
	}

	void around(LandscapeConfig cfg) : execution(void LandscapeConfig.remove()) && target(cfg) {
		proceed(cfg);
		Persister<LandscapeConfig> strategy = cfg.getPersister();
		strategy.delete(cfg);
	}
}
