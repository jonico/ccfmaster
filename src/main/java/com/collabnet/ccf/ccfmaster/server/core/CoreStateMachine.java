package com.collabnet.ccf.ccfmaster.server.core;

import static com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState.NOT_RESPONDING;
import static com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState.STARTED;
import static com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState.STARTING;
import static com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState.STOPPED;
import static com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState.STOPPING;
import static com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand.NONE;
import static com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand.RESTART;
import static com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand.START;
import static com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand.STOP;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.core.JmxUtil.JmxStatus;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;

/**
 * this class implements part of the state diagram at
 * {@link http://oryx-project.org/oryx/editor;petrinet?stencilset=/stencilsets/petrinets/petrinet.json#/model/13150/}
 * 
 * Some of the state transitions are performed by separate threads that monitor launchin/stopping cores.
 * @author ctaylor
 *
 */
@Service
public class CoreStateMachine {
	private static final Logger log = LoggerFactory.getLogger(CoreStateMachine.class);
	private List<TransitionObserver> observers = new CopyOnWriteArrayList<TransitionObserver>();
	private int jmxForwardPort;
	private int jmxReversePort;
	private volatile boolean changed = false;
	
	public static abstract class TransitionObserver {

		private static final Logger log = LoggerFactory.getLogger(TransitionObserver.class);
		protected CoreStateMachine stateMachine;

		public void onUpdate(CoreStateMachine stateMachine, CcfCoreStatus statusToUpdate, ExecutedCommand command) {
			// important: avoid getters because they need to be overridden 
			// for Roo compatibility.
			CoreState       oldState   = statusToUpdate.currentStatus();
			ExecutedCommand oldCommand = statusToUpdate.executedCommand();
			this.stateMachine = stateMachine; // expose stateMachine to allow Observers to call notifyObservers()
			handleTransition(statusToUpdate, command);
			CoreState       newState   = statusToUpdate.currentStatus();
			ExecutedCommand newCommand = statusToUpdate.executedCommand();
			if (newState != oldState || newCommand != oldCommand) {
				stateMachine.setChanged();
				log.debug("{}: Transition: ({},{}) -{}-> ({},{})", new Object[]{getClass().getSimpleName(), oldState, oldCommand, command, newState, newCommand});
			} else {
//				log.debug("{}: No change on command {}: ({},{})", new Object[]{getClass().getSimpleName(), command, oldState, oldCommand});
			}
		}
		
		abstract void handleTransition(CcfCoreStatus statusToUpdate, ExecutedCommand command);
	}
	
	static abstract class ProcessTransitionObserver extends TransitionObserver {
		
		private String ccfHome;
		private int jmxForwardPort;
		private int jmxReversePort;
		
		@Autowired
		protected ExecutorService executor;
		
		protected Process startCore(final CcfCoreStatus status) throws IOException {
						final Direction direction = status.getDirection();
						final File workingDirectory = determineBaseDir(direction);
						final File script = determineExecutable(direction);
						final ProcessBuilder builder = new ProcessBuilder();
						final Process proc = builder
							.command(script.getPath())
							.directory(workingDirectory)
							.start();
						proc.getErrorStream().close();
						proc.getInputStream().close();
		
						return proc;
				}
		
		protected void stopCore(CcfCoreStatus status) {
			int jmxPort = determineJmxPort(status);
			JmxUtil.executeMethod(jmxPort, "openadaptor:id=ServiceWrapperBean", "stop", new Object[]{0}, new String[]{"int"});
		}


		private static String directionBaseName(Direction direction) {
			final Landscape landscape = direction.getLandscape();
			final SystemKind teamForgeSystemKind = landscape.getTeamForge().getSystemKind();
			final SystemKind participantSystemKind = landscape.getParticipant().getSystemKind();
			Assert.isTrue(teamForgeSystemKind == SystemKind.TF, "landscape.teamForge must be of Kind TF, was: " + teamForgeSystemKind);
			return String.format(
					direction.getDirection() == Directions.FORWARD ? "%1$s2%2$s" : "%2$s2%1$s",
					teamForgeSystemKind,
					participantSystemKind);
		}
	
		private static String determineExecutableName(Direction direction) {
			String baseName = directionBaseName(direction);
			if (SystemUtils.IS_OS_WINDOWS) {
				return baseName + "2.bat";
			} else if (SystemUtils.IS_OS_UNIX) {
				return baseName + "2.sh";
			} else {
				throw new IllegalArgumentException("unknown OS: " + SystemUtils.OS_NAME);
			}
		
		}
	
		protected File determineBaseDir(Direction direction) {
			final Landscape landscape = direction.getLandscape();
			final SystemKind teamForgeSystemKind = landscape.getTeamForge().getSystemKind();
			final SystemKind participantSystemKind = landscape.getParticipant().getSystemKind();
			Assert.isTrue(teamForgeSystemKind == SystemKind.TF, "landscape.teamForge must be of Kind TF, was: " + teamForgeSystemKind);
		
			// handle inconsistent directory names
			String scenario = null;
			switch (participantSystemKind) {
			case QC:
				scenario = "QCTF";
				break;
			case SWP:
				scenario = "TFSWP";
				break;
			default:
				throw new IllegalArgumentException("CCF doesn't support mapping TF <-> " + participantSystemKind);
			}
			String baseName = directionBaseName(direction);
			File res = new File(getCcfHome(), String.format("landscape%d/samples/%s/%s", landscape.getId(), scenario, baseName));
			Assert.isTrue(res.exists(), "Scenario base directory doesn't exist: " + res);
			return res;
		}
	
		protected File determineExecutable(Direction direction) {
			File baseDir = determineBaseDir(direction);
			final String executableName = determineExecutableName(direction);
			return new File(baseDir, executableName);
		}
		
		protected int determineJmxPort(CcfCoreStatus status) {
			Directions direction = status.getDirection().getDirection();
			return direction == Directions.REVERSE ? jmxReversePort : jmxForwardPort;
		}

		public void setCcfHome(String ccfHome) {
			this.ccfHome = ccfHome;
		}

		public String getCcfHome() {
			return ccfHome;
		}

		public void setJmxForwardPort(int jmxForwardPort) {
			this.jmxForwardPort = jmxForwardPort;
		}

		public int getJmxForwardPort() {
			return jmxForwardPort;
		}

		public void setJmxReversePort(int jmxReversePort) {
			this.jmxReversePort = jmxReversePort;
		}

		public int getJmxReversePort() {
			return jmxReversePort;
		}
	}

	public static class StartCoreTransitionObserver extends ProcessTransitionObserver {

		@Override
		void handleTransition(CcfCoreStatus status, ExecutedCommand command) {
			final CoreState state = status.currentStatus();
			if (state == CoreState.STOPPED && (command == START || command == RESTART)) {
				try {
					Process proc = startCore(status);
					status.setCurrentStatus(STARTING);
					status.setExecutedCommand(command);
					status.coreProcess(proc);
					Runnable monitor = CoreMonitor
						.builder(determineJmxPort(status), status, stateMachine)
						.startingMonitor();
					executor.execute(monitor);
				} catch (IOException e) {
					throw new CoreConfigurationException("error starting core: " + e.getMessage(),e);
				}
			}
		}

	}
	
	public static class StopCoreTransitionObserver extends ProcessTransitionObserver {

		@Override
		void handleTransition(CcfCoreStatus status, ExecutedCommand command) {
			final CoreState state = status.currentStatus();
			if (state == STARTED && (command == STOP || command == RESTART)) {
				stopCore(status);
				status.setCurrentStatus(STOPPING);
				status.setExecutedCommand(command);
				Runnable monitor = CoreMonitor
					.builder(determineJmxPort(status), status, stateMachine)
					.connectionAttempts(45)
					.stoppingMonitor();
				executor.execute(monitor);
			}
		}

	}

	public static class NotRespondingTransitionObserver extends ProcessTransitionObserver {

		@Override
		void handleTransition(CcfCoreStatus status, ExecutedCommand command) {
			if (status.currentStatus() != CoreState.NOT_RESPONDING) {
				// not interested
				return;
			}
			try {
				Process proc = status.coreProcess();
				switch (command) {
				case START:
					kill(proc);
					proc = startCore(status);
					status.coreProcess(proc);
					status.setCurrentStatus(STARTING);
					status.setExecutedCommand(command);
					final Runnable startingMonitor = CoreMonitor
						.builder(determineJmxPort(status), status, stateMachine)
						.startingMonitor();
					executor.execute(startingMonitor);
					break;
				case STOP:
					kill(proc);
					stopCore(status);
					status.setCurrentStatus(STOPPING);
					status.setExecutedCommand(command);
					final Runnable stoppingMonitor = CoreMonitor
						.builder(determineJmxPort(status), status, stateMachine)
						.connectionAttempts(45)
						.stoppingMonitor();
					executor.execute(stoppingMonitor);
					break;
				default:
					// not interested
					break;
				}
			} catch (IOException e) {
				throw new CoreConfigurationException("error starting core: " + e.getMessage(), e);
			}
		}

		public void kill(final Process proc) {
			if (proc != null) {
				proc.destroy();
				try {
					proc.waitFor();
				} catch (InterruptedException e) {
					throw new CoreConfigurationException("interrupted while waiting for core to die: " + e.getMessage(), e);
				}
			}
		}

	}
	
	public static class CoreMonitor {
		TimeUnit delayUnit = TimeUnit.SECONDS;
		private long delayUnitCount = 1;
		long pollCount = 10;
		private final int jmxPort;
		private final CcfCoreStatus statusToUpdate;
		private final CoreStateMachine stateMachine;
		
		private CoreMonitor(int jmxPort, CcfCoreStatus statusToUpdate, CoreStateMachine stateMachine) {
			this.jmxPort = jmxPort;
			this.statusToUpdate = statusToUpdate;
			this.stateMachine = stateMachine;
			
		}
		
		public CoreMonitor connectionAttempts(long numberOfTries) {
			this.pollCount = numberOfTries;
			return this;
		}
		
		public CoreMonitor delayBetweenConnectionAttempts(long count, TimeUnit timeUnit) {
			Assert.isTrue(count > 0);
			this.delayUnitCount = count;
			this.delayUnit = timeUnit;
			return this;
		}
		
		public Runnable startingMonitor() {
			return new Runnable() {
				@Override
				public void run() {
					try {
						CcfCoreStatus.CoreState state = NOT_RESPONDING;
						for (long i = 0; i < pollCount; i++, delayUnit.sleep(delayUnitCount)) {
							if (JmxUtil.canConnect(jmxPort).equals(JmxStatus.CONNECTED)) {
								state = STARTED;
								break;
							}
						}
						statusToUpdate.setCurrentStatus(state);
						statusToUpdate.setExecutedCommand(NONE);
						stateMachine.notifyObservers(statusToUpdate, NONE);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				
			};
		}
		
		public Runnable stoppingMonitor() {
			return new Runnable() {
	
				@Override
				public void run() {
					try {
						// if things go wrong, we want the core to be not responding, with no pending command
						CcfCoreStatus.CoreState state = NOT_RESPONDING;
						CcfCoreStatus.ExecutedCommand command = NONE;
						
						for (int i = 0; i < pollCount; i++, delayUnit.sleep(delayUnitCount)) {
							if (!JmxUtil.canConnect(jmxPort).equals(JmxStatus.CONNECTED)) {
								state = STOPPED;
								command = (statusToUpdate.executedCommand() == RESTART) ? RESTART : command;
								break;
							}
						}
						statusToUpdate.setCurrentStatus(state);
						statusToUpdate.setExecutedCommand(command);
						stateMachine.notifyObservers(statusToUpdate, command);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				
			};
		}
		
		public static CoreMonitor builder(int jmxPort, CcfCoreStatus statusToUpdate, CoreStateMachine stateMachine) {
			return new CoreMonitor(jmxPort, statusToUpdate, stateMachine);
		}
	
	}

	public CcfCoreStatus merge(CcfCoreStatus newStatus) {
		final Long id = newStatus.getId();
		// ensures the status is cached
		final CcfCoreStatus status = CcfCoreStatus.findCcfCoreStatus(id);
		
//		final ExecutedCommand oldCommand = status.getExecutedCommand();
		final ExecutedCommand newCommand = newStatus.getExecutedCommand();
		// before we do anything, figure out our real state.
		status.setCurrentStatus(determineCurrentStatus(status, 0));
		notifyObservers(status, newCommand);
		return status;
	}

	public void notifyObservers(final CcfCoreStatus status, final ExecutedCommand newCommand) {
		log.debug("in notifyObservers");
		do {
			log.debug("notifying all observers");
			clearChanged();
			for (TransitionObserver observer : getObservers()) {
				observer.onUpdate(this, status, newCommand);
			}
		} while(hasChanged());
	}
	
	public void setChanged() { changed = true; }
	public boolean hasChanged() { return changed; }
	public void clearChanged() { changed = false; }
	

	public CoreState determineCurrentStatus(CcfCoreStatus status, long toleratedPollingInterval) {
		final CoreState currentStatus = status.currentStatus();
		final int jmxPort = status.getDirection().getDirection() == Directions.FORWARD ?
				getJmxForwardPort() : getJmxReversePort();
		if (currentStatus == STARTING || currentStatus == STOPPING) {
			return currentStatus;
		} else {
			long currentTime = System.currentTimeMillis(); 
			if (currentTime - status.lastStatusUpdate() < toleratedPollingInterval) {
				return status.lastKnownStatus();
			}
			status.lastStatusUpdate(currentTime);
			JmxStatus jmxStatus = JmxUtil.canConnect(jmxPort); 
			if (jmxStatus.equals(JmxStatus.CONNECTED)) {
				status.lastKnownStatus(STARTED);
			} else if (jmxStatus.equals(JmxStatus.NOT_RESPONDING)) {
				status.lastKnownStatus(NOT_RESPONDING);
			} else if (canConnectToPort(jmxPort)) {
				status.lastKnownStatus(NOT_RESPONDING);
			} else {
				status.lastKnownStatus(STOPPED);
			}
			return status.lastKnownStatus();
		}
	}

	private boolean canConnectToPort(int jmxPort) {
		Socket socket = null;
		try {
			socket = new Socket(InetAddress.getByName(null), jmxPort);
			return true;
		} catch (UnknownHostException e) {
			throw new IllegalStateException("couldn't resolve localhost: " + e.getMessage(), e);
		} catch (IOException e) {
			return false;
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					log.warn("error closing socket", e);
				}
		}
	}

	public void setObservers(List<TransitionObserver> observers) {
		this.observers = observers;
	}

	public List<TransitionObserver> getObservers() {
		return observers;
	}

	public void setJmxForwardPort(int jmxForwardPort) {
		this.jmxForwardPort = jmxForwardPort;
	}

	public int getJmxForwardPort() {
		return jmxForwardPort;
	}

	public void setJmxReversePort(int jmxReversePort) {
		this.jmxReversePort = jmxReversePort;
	}

	public int getJmxReversePort() {
		return jmxReversePort;
	}
	
}
