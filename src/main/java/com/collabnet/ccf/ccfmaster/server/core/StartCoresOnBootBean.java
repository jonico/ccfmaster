package com.collabnet.ccf.ccfmaster.server.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class StartCoresOnBootBean {

	private int pollCount = 50;  //increased to make shutdown work properly
	private int delayBetweenPollMillis = 1000;
	
	private static final Logger log = LoggerFactory.getLogger(StartCoresOnBootBean.class);

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
	
	public void boot() {
		final List<Direction> allDirections = Direction.findAllDirections();
		final int numDirections = allDirections.size();
		// sanity check
		Assert.isTrue(numDirections <= 2, String.format("found too many (%d) directions, bailing out.", numDirections));
		for (Direction dir : allDirections) {
			if (dir.getShouldStartAutomatically() != null && dir.getShouldStartAutomatically()) {
				log.info("core for Direction ({}, {}) is configured to start automatically, booting it now...", dir.getDescription(), dir.getDirection());
				bootCore(dir);
			}
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
	
	public boolean allCoresStopped() {
		return Iterables.all(getCores(), isStopped);
	}
	
	public boolean allCoresStopped(Iterable<CcfCoreStatus> cores) {
		return Iterables.all(cores, isStopped);
	}
	
	public void boot(List<Long> runningCoreIds){
		for(Long id: runningCoreIds){
			CcfCoreStatus ccs = CcfCoreStatus.findCcfCoreStatus(id);
			ccs.setExecutedCommand(ExecutedCommand.START);
			ccs.merge();
		}
	}

	public List<Long> getRunningCoreIds() {
		List<Long> runningCoreIds = ImmutableList.of();
		if (runningCoreIds.isEmpty()) {
			final Iterable<CcfCoreStatus> runningCores = Iterables.filter(getCores(), isRunning);
			runningCoreIds = ImmutableList.copyOf(Iterables.transform(runningCores, core2Id));
		}
		return runningCoreIds;
	}
	
	private Iterable<CcfCoreStatus> getCores() {
		return Iterables.transform(Direction.findAllDirections(), direction2coreStatus);
	}

	CcfCoreStatus bootCore(Direction dir) {
		final Long id = dir.getId();
		CcfCoreStatus ccs = CcfCoreStatus.findCcfCoreStatus(id);
		ccs.setExecutedCommand(ExecutedCommand.START);
		return ccs.merge();
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
		log.debug("About to shut down core for Direction ({}, {})...", dir.getDescription(), dir.getDirection());
		CcfCoreStatus ccs = CcfCoreStatus.findCcfCoreStatus(dir.getId());
		ccs.setExecutedCommand(ExecutedCommand.STOP);
		CcfCoreStatus shutDownCore = ccs.merge();
		log.info("Sent stop command to core for Direction ({}, {}).", dir.getDescription(), dir.getDirection());
		return shutDownCore;
	}
	
	static final Function<CcfCoreStatus, Long> core2Id = new Function<CcfCoreStatus, Long>() {
		@Override
		public Long apply(CcfCoreStatus input) {
			return input.getId();
		}
	};
	
	static final Function<Direction, CcfCoreStatus> direction2coreStatus = new Function<Direction, CcfCoreStatus>() {
		@Override
		public CcfCoreStatus apply(Direction input) {
			return CcfCoreStatus.findCcfCoreStatus(input.getId());
		}
	};

	static final Predicate<CcfCoreStatus> isRunning = new Predicate<CcfCoreStatus>() {
		@Override
		public boolean apply(CcfCoreStatus ccs) {
			return ccs.getCurrentStatus().equals(CoreState.STARTED);
		}
	};

	static final Predicate<CcfCoreStatus> isStopped = new Predicate<CcfCoreStatus>() {
		@Override
		public boolean apply(CcfCoreStatus core) {
			return core.getCurrentStatus().equals(CoreState.STOPPED);
		}
	};

}
