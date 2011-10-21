package com.collabnet.ccf.ccfmaster.server.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class StartCoresOnBootBean {

	private int pollCount = 10;
	private int delayBetweenPollMillis = 1000;
	
	private final Logger log = LoggerFactory.getLogger(StartCoresOnBootBean.class);
	
	public void boot() {
		final List<Direction> allDirections = Direction.findAllDirections();
		final int numDirections = allDirections.size();
		// sanity check
		Assert.isTrue(numDirections <= 2, String.format("found too many (%d) directions, bailing out.", numDirections));
		for (Direction dir : allDirections) {
			if (dir.getShouldStartAutomatically() != null && dir.getShouldStartAutomatically())
				bootCore(dir);
		}
	}

	public void shutdown() {
		List<CcfCoreStatus> cores = shutdownAllCores();
		for (int i = 0; i < getPollCount() && !allCoresStopped(cores); i++) {
			try {
				Thread.sleep(getDelayBetweenPollMillis());
				log.info("Waiting for CCF Cores to shut down ...");
			} catch (InterruptedException e) {
				log.error("Waiting for cores to stop was interrupted during shutdown, quitting now.");
				break;
			}
		}
	}

	CcfCoreStatus bootCore(Direction dir) {
		log.info("core for Direction ({}, {}) is configured to start automatically, booting it now...", dir.getDescription(), dir.getDirection());
		final Long id = dir.getId();
		CcfCoreStatus ccs = CcfCoreStatus.findCcfCoreStatus(id);
		ccs.setExecutedCommand(ExecutedCommand.START);
		return ccs.merge();
	}
	
	public boolean allCoresStopped(Iterable<CcfCoreStatus> cores) {
		return Iterables.all(cores, isStopped);
	}

	List<CcfCoreStatus> shutdownAllCores() {
		final List<Direction> allDirections = Direction.findAllDirections();
		final int numDirections = allDirections.size();
		// sanity check
		Assert.isTrue(numDirections <= 2, String.format("found too many (%d) directions, bailing out.", numDirections));
		ImmutableList.Builder<CcfCoreStatus> cores = ImmutableList.builder();
		for (Direction dir : allDirections) {
			CcfCoreStatus core = shutdownCore(dir);
			cores.add(core);
		}
		return cores.build();
	}

	CcfCoreStatus shutdownCore(Direction dir) {
		log.info("About to shut down core for Direction ({}, {})...", dir.getDescription(), dir.getDirection());
		CcfCoreStatus ccs = CcfCoreStatus.findCcfCoreStatus(dir.getId());
		ccs.setExecutedCommand(ExecutedCommand.STOP);
		CcfCoreStatus shutDownCore = ccs.merge();
		log.info("Sent stop command to core for Direction ({}, {}).", dir.getDescription(), dir.getDirection());
		return shutDownCore;
	}

	static final Predicate<CcfCoreStatus> isStopped = new Predicate<CcfCoreStatus>() {
	
		@Override
		public boolean apply(CcfCoreStatus core) {
			return core.getCurrentStatus() == CoreState.STOPPED;
		}
	};

	public void setPollCount(int pollCount) {
		Assert.isTrue(pollCount >= 0);
		this.pollCount = pollCount;
	}

	public int getPollCount() {
		return pollCount;
	}

	public void setDelayBetweenPollMillis(int delayBetweenPollMillis) {
		Assert.isTrue(delayBetweenPollMillis >= 0);
		this.delayBetweenPollMillis = delayBetweenPollMillis;
	}

	public int getDelayBetweenPollMillis() {
		return delayBetweenPollMillis;
	}

}
