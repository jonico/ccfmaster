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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
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
	private boolean changed = false;
	
	public static abstract class TransitionObserver {

		private static final Logger log = LoggerFactory.getLogger(TransitionObserver.class);

		public void onUpdate(CoreStateMachine o, CcfCoreStatus statusToUpdate, ExecutedCommand command) {
			// important: avoid getters because they need to be overridden 
			// for Roo comatibility.
			CoreState       oldState   = statusToUpdate.currentStatus();
			ExecutedCommand oldCommand = statusToUpdate.executedCommand();
			handleTransition(statusToUpdate, command);
			CoreState       newState   = statusToUpdate.currentStatus();
			ExecutedCommand newCommand = statusToUpdate.executedCommand();
			if (newState != oldState || newCommand != oldCommand) {
				o.setChanged();
				log.debug("Transition: ({},{}) -{}-> ({},{})", new Object[]{oldState, oldCommand, command, newState, newCommand});
			} else {
				log.debug("No change on command {}: ({},{})", new Object[]{command, oldState, oldCommand});
			}
		}
		
		abstract void handleTransition(CcfCoreStatus statusToUpdate, ExecutedCommand command);
	}
	
	static abstract class ProcessTransitionObserver extends TransitionObserver {
		
		private String ccfHome;
		
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
		
//						removed because we don't know whether readline can be relied upon because a line might
//						be bigger than the OS buffer for stdout/err, causing the CCF core to block when printing
//						a big artifact to stdout
				
//						new Thread(
//								outputLogger(script.getName() + "-stdout", proc.getInputStream()),
//								script.getName() + " standard output").run();
//						new Thread(
//								outputLogger(script.getName() + "-stderr", proc.getErrorStream()),
//								script.getName() + " standard output").run();

						return proc;
				}
		
		protected void stopCore(CcfCoreStatus status) {
			// TODO use JMX to stop a core
			
		}

//		removed because we don't know whether readline can be relied upon because a line might
//		be bigger than the OS buffer for stdout/err, causing the CCF core to block when printing
//		a big artifact to stdout.
//		TODO: Arguably this kind of logging shouldn't go to stdout, anyway.
	
		private static Runnable outputLogger(final String loggerName, final InputStream is) {
			return new Runnable() {
				private final Logger log = LoggerFactory.getLogger(loggerName);
				
				@Override
				public void run() {
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					String line;
					try {
						while((line = reader.readLine()) != null) {
							log.info(line);
						}
					} catch (IOException e) {
						log.error("outputLogger caught IOException.", e);
					}
				}
			};
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

		public void setCcfHome(String ccfHome) {
			this.ccfHome = ccfHome;
		}

		public String getCcfHome() {
			return ccfHome;
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
				} catch (IOException e) {
					throw new CoreConfigurationException("error starting core: " + e.getMessage(),e);
				}
			}
		}

	}
	
	public static class StopCoreTransitionObserver extends ProcessTransitionObserver {
		private static final Logger log = LoggerFactory.getLogger(StopCoreTransitionObserver.class);
		private int jmxForwardPort, jmxReversePort;
		@Override
		void handleTransition(CcfCoreStatus status, ExecutedCommand command) {
			final CoreState state = status.currentStatus();
			if (state == STARTED && (command == STOP || command == RESTART)) {
				int jmxPort = calculateJmxPort(status);
				JmxUtil.executeMethod(jmxPort, "openadaptor:id=ServiceWrapperBean", "stop", new Object[]{0}, new String[]{"int"});
				status.setCurrentStatus(STOPPING);
				status.setExecutedCommand(command);
			}
		}
		
		private int calculateJmxPort(CcfCoreStatus status) {
			Direction direction = status.getDirection();
			int port = direction.getDirection() == Directions.FORWARD ? getJmxForwardPort() : getJmxReversePort();
			log.debug("JMX port set to " + port);
			return port;
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
					status.setCurrentStatus(STARTING);
					status.setExecutedCommand(command);
					status.coreProcess(proc);
					break;
				case STOP:
					kill(proc);
					stopCore(status);
					status.setCurrentStatus(STOPPING);
					status.setExecutedCommand(command);
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
	
	static final class JmxUtil {
		private static final String JMX_URL_TEMPLATE = "service:jmx:rmi:///jndi/rmi://localhost:%d/jmxrmi";
		private static final Logger log = LoggerFactory.getLogger(JmxUtil.class);

		private JmxUtil() {
			// prevent instantiation
		}
		
		public static Object executeMethod(int jmxPort, String beanName, String methodName, Object[] params, String[] signatures) {
			JMXConnector connector = null;
			Object result = null;
			try {
				String serviceUrl = urlForPort(jmxPort);
				JMXServiceURL url = new JMXServiceURL(serviceUrl);
				connector = JMXConnectorFactory.connect(url);
				MBeanServerConnection connection = connector.getMBeanServerConnection();
				result = connection.invoke(new ObjectName(beanName), methodName, params, signatures);
//				connection.getMBeanCount();
			} catch (IOException e) {
				log.debug("IOEexception: " + e.getMessage(), e);
			} catch (JMException e) {
				log.debug("JMX exception: " + e.getMessage(), e);
			} finally {
				try {
					if (connector != null)
						connector.close();
				} catch (IOException e) {
					// shouldn't happen.
					throw new IllegalStateException("error closing JMX connection: " + e.getMessage(),e);
				}
			}
			return result;
		}
		
		public static boolean canConnect(int jmxPort) {
			String serviceURL = urlForPort(jmxPort);
			JMXConnector connector = null;
			try {
				JMXServiceURL url = new JMXServiceURL(serviceURL);
				connector = JMXConnectorFactory.connect(url);
				MBeanServerConnection connection = connector.getMBeanServerConnection();
				connection.getAttribute(new ObjectName("openadaptor:id=SystemUtil"), "Memory");
//				connection.getMBeanCount();
				return true;
			} catch (IOException e) {
//				log.debug("IOEexception: " + e.getMessage(), e);
				return false;
			} catch (JMException e) {
//				log.debug("JMX exception: " + e.getMessage(), e);
				return false;
			} finally {
				try {
					if (connector != null)
						connector.close();
				} catch (IOException e) {
					// shouldn't happen.
					throw new IllegalStateException("error closing JMX connection: " + e.getMessage(),e);
				}
			}
		}

		public static String urlForPort(int jmxPort) {
			String serviceURL = String.format(JMX_URL_TEMPLATE, jmxPort);
			return serviceURL;
		}

	}
	
	public static class MonitorCoreStartingObserver extends TransitionObserver {
		private static Logger log = LoggerFactory.getLogger(MonitorCoreStartingObserver.class);
		private int jmxForwardPort;
		private int jmxReversePort;
		private long timeoutSeconds = 10;

		private final class JmxPoller implements Runnable {
			private int jmxPort;
			public JmxPoller(int port) {
				this.jmxPort = port;
			}
			@Override
			public void run() {
				while(!Thread.interrupted()) {
					try {
						Thread.sleep(1000);
						if (JmxUtil.canConnect(jmxPort))
							return;
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}

		private final class Monitor implements Runnable {
			private final CcfCoreStatus statusToUpdate;
			private final Runnable jmxPoller;

			public Monitor(CcfCoreStatus statusToUpdate, Runnable jmxPoller) {
				this.statusToUpdate = statusToUpdate;
				this.jmxPoller = jmxPoller;
			}
			
			@Override
			public void run() {
				Future<?> res = executor.submit(jmxPoller);
				try {
					res.get(getTimeoutSeconds(), TimeUnit.SECONDS);
					statusToUpdate.setCurrentStatus(STARTED);
				} catch (TimeoutException e) {
					res.cancel(true);
					statusToUpdate.setCurrentStatus(NOT_RESPONDING);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					final Throwable cause = e.getCause();
					throw new CoreConfigurationException("error polling: " + cause.getMessage(), cause);
				} 
				statusToUpdate.setExecutedCommand(NONE);
				stateMachine.notifyObservers(statusToUpdate, NONE);
			}
		}

		@Autowired ExecutorService executor;
		private CoreStateMachine stateMachine;

		@Override
		public void onUpdate(CoreStateMachine stateMachine, CcfCoreStatus statusToUpdate, ExecutedCommand command) {
			this.stateMachine = stateMachine;
			super.onUpdate(stateMachine, statusToUpdate, command);
		}
		
		private int calculateJmxPort(CcfCoreStatus statusToUpdate) {
			Direction direction = statusToUpdate.getDirection();
			int port = direction.getDirection() == Directions.FORWARD ? getJmxForwardPort() : getJmxReversePort();
			log.debug("JMX port set to " + port);
			return port;
		}

		@Override
		void handleTransition(CcfCoreStatus statusToUpdate, ExecutedCommand command) {
			if (statusToUpdate.currentStatus() == STARTING &&
					(command == START || command == RESTART)) {
				executor.execute(new Monitor(statusToUpdate, new JmxPoller(calculateJmxPort(statusToUpdate))));
			}
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

		public void setTimeoutSeconds(long timeoutSeconds) {
			this.timeoutSeconds = timeoutSeconds;
		}

		public long getTimeoutSeconds() {
			return timeoutSeconds;
		}
	}
	
	public static class MonitorCoreStoppingObserver extends ProcessTransitionObserver {
		private static final Logger log = LoggerFactory.getLogger(MonitorCoreStoppingObserver.class);
		private int jmxForwardPort;
		private int jmxReversePort;
		private long timeoutSeconds = 45;

		private final class JmxPoller implements Runnable {
			private int jmxPort;

			public JmxPoller(int port) {
				this.jmxPort = port;
			}

			@Override
			public void run() {
				while(!Thread.interrupted()) {
					try {
						Thread.sleep(1000);
						if (!JmxUtil.canConnect(jmxPort))
							return;
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}

		private final class Monitor implements Runnable {
			private final Runnable jmxPoller;
			private final CcfCoreStatus statusToUpdate;

			public Monitor(CcfCoreStatus statusToUpdate, Runnable jmxPoller) {
				this.statusToUpdate = statusToUpdate;
				this.jmxPoller = jmxPoller;
			}
			@Override
			public void run() {
				Future<?> res = executor.submit(jmxPoller);
				try {
					res.get(getTimeoutSeconds(), TimeUnit.SECONDS);
					statusToUpdate.setCurrentStatus(STOPPED);
					if (statusToUpdate.executedCommand() == RESTART) {
						Process proc = startCore(statusToUpdate);
						statusToUpdate.setCurrentStatus(STARTING);
						statusToUpdate.coreProcess(proc);
					} else {
						statusToUpdate.setExecutedCommand(NONE);
					}
				} catch (TimeoutException e) {
					res.cancel(true);
					statusToUpdate.setCurrentStatus(NOT_RESPONDING);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					final Throwable cause = e.getCause();
					throw new CoreConfigurationException("error polling: " + cause.getMessage(), cause);
				} catch (IOException e) {
					statusToUpdate.setExecutedCommand(NONE);
					throw new CoreConfigurationException("error restarting core: " + e.getMessage(), e);
				}
				stateMachine.notifyObservers(statusToUpdate, statusToUpdate.executedCommand());
			}
		}

		@Autowired ExecutorService executor;
		private CoreStateMachine stateMachine;

		@Override
		public void onUpdate(CoreStateMachine stateMachine, CcfCoreStatus statusToUpdate, ExecutedCommand command) {
			this.stateMachine = stateMachine;
			super.onUpdate(stateMachine, statusToUpdate, command);
			
		}

		private int calculateJmxPort(CcfCoreStatus statusToUpdate) {
			Direction direction = statusToUpdate.getDirection();
			int port = direction.getDirection() == Directions.FORWARD ? getJmxForwardPort() : getJmxReversePort();
			log.debug("JMX port set to " + port);
			return port;
		}

		@Override
		void handleTransition(CcfCoreStatus statusToUpdate, ExecutedCommand command) {
			if (statusToUpdate.currentStatus() == STOPPING &&
					(command == STOP || command == RESTART)) {
				executor.execute(new Monitor(statusToUpdate, new JmxPoller(calculateJmxPort(statusToUpdate))));
			}
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

		public void setTimeoutSeconds(long timeoutSeconds) {
			this.timeoutSeconds = timeoutSeconds;
		}

		public long getTimeoutSeconds() {
			return timeoutSeconds;
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
/*
		try {
			// can't use getter because we need to override it to accommodate Roo's controller :(.
			switch (status.currentStatus()) {
			case STOPPED:
				if (newCommand == START) {
					status.setExecutedCommand(newCommand);
					status.setCurrentStatus(STARTING);
					startCore(status); // transitions to STARTED or NOT_RESPONDING eventually
				}
				break;
			case STARTED:
				if (newCommand == STOP || newCommand == RESTART) {
					status.setExecutedCommand(newCommand);
					status.setCurrentStatus(STOPPING);
					stopCore(status); // transitions to STOPPED, STARTING or NOT_RESPONDING eventually
				}
				break;
			case NOT_RESPONDING:
				if (newCommand != NONE)
					killCore(status);
				status.setExecutedCommand(newCommand);

				switch (newCommand) {
				case START:
					startCore(status);
					break;

				case RESTART:
				case STOP:
					stopCore(status);
					break;
				}
				break;
			}
		} catch (IOException ioe) {
			throw new CoreConfigurationException("couldn't start/stop core: " + ioe.getMessage(), ioe);
		}
		return status;
		*/
	}

	public void notifyObservers(final CcfCoreStatus status, final ExecutedCommand newCommand) {
		do {
			clearChanged();
			for (TransitionObserver observer : getObservers()) {
				observer.onUpdate(this, status, newCommand);
			}
		} while(hasChanged());
	}
	
	public void setChanged() { changed = true; }
	public boolean hasChanged() { return changed; }
	public void clearChanged() { changed = false; }
	

	
//removed because we don't know whether readline can be relied upon because a line might
//be bigger than the OS buffer for stdout/err, causing the CCF core to block when printing
//a big artifact to stdout.
//TODO: Arguably this kind of logging shouldn't go to stdout, anyway.

//	private static Runnable outputLogger(final String loggerName, final InputStream is) {
//		return new Runnable() {
//			private final Logger log = LoggerFactory.getLogger(loggerName);
//			
//			@Override
//			public void run() {
//				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//				String line;
//				try {
//					while((line = reader.readLine()) != null) {
//						log.info(line);
//					}
//				} catch (IOException e) {
//					log.error("outputLogger caught IOException.", e);
//				}
//			}
//		};
//	}

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
			if (JmxUtil.canConnect(jmxPort)) {
				status.lastKnownStatus(STARTED);
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
