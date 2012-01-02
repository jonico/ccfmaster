package com.collabnet.ccf.ccfmaster.server.domain;

import static com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState.STOPPED;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FlushModeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.OneToOne;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collabnet.ccf.ccfmaster.server.core.CoreStateMachine;
import com.google.common.collect.Lists;

/**
 * 
 * @author jonico
 *
 */
@RooJavaBean
@RooEntity(finders={"findCcfCoreStatusesByDirection"})
@Component
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CcfCoreStatus {
	/**
	 * maps the direction's Id to an instance. Used instead of a Database table.
	 * Because create() shouldn't be supported, we create new instances on
	 * demand when findCcfCoreStatus(id) is called. We could update the cache
	 * from the constructor if spring didn't force us to have a zero-argument
	 * constructor.
	 */
	private static ConcurrentHashMap<Long, CcfCoreStatus> cache = new ConcurrentHashMap<Long, CcfCoreStatus>();

	@NotNull
	@OneToOne
	@XmlJavaTypeAdapter(Direction.XmlAdapter.class)
	private Direction direction;

	@Enumerated(EnumType.STRING)
	@NotNull
	private CoreState currentStatus;
	
	// this property is used to cache the status if determineCurrentStatus is called more frequently than the tolerated polling interval
	private transient CoreState lastKnownStatus;
	
	public CoreState lastKnownStatus() {
		return lastKnownStatus;
	}
	
	public void lastKnownStatus(CoreState status) {
		this.lastKnownStatus = status;
	}

	@Enumerated(EnumType.STRING)
	@NotNull
	private ExecutedCommand executedCommand;
	
	@Autowired transient private CoreStateMachine doStuffStrategy;
	
	transient private long lastStatusUpdate = 0; 
	
	public void lastStatusUpdate (long statusUpdate) {
		this.lastStatusUpdate = statusUpdate;
	}
	
	public long lastStatusUpdate () {
		return lastStatusUpdate;
	}

	transient private Process coreProcess;

	public CcfCoreStatus merge() {
	    return doStuffStrategy.merge(this);
	}

	public CoreState getCurrentStatus() {
		// 3 second threshold to prevent CCFMaster from doing nothing but checking JMX
	    return doStuffStrategy.determineCurrentStatus(this, 3000);
	}

	/** because getCurrentStatus must be overridden for Roo compatibility, use this for direct access to the field. */
	public CoreState currentStatus() { return currentStatus; }

	/** provided for symmetry with currentStatus()/getCurrentStatus() */
	public ExecutedCommand executedCommand() { return executedCommand; }

	

	public static CcfCoreStatus findCcfCoreStatus(Long id) {
		final Direction direction = Direction.findDirection(id);
		CcfCoreStatus ccs = null, res = null;
		if (direction != null) {
			ccs = new CcfCoreStatus();
			ccs.setId(id);
			ccs.setCurrentStatus(STOPPED);
			ccs.setDirection(direction);
			res = cache.putIfAbsent(id, ccs);
		}
		// should we update our current status here?
		return res == null ? ccs : res; 
	}

	public static enum CoreState {
		STARTING,
		STARTED,
		NOT_RESPONDING,
		STOPPING,
		STOPPED
	}
	public static enum ExecutedCommand {
		START,
		STOP,
		RESTART,
		NONE
	}
	

	@PersistenceContext
    transient EntityManager entityManager;

	@Transactional
    public void persist() {
		throw new UnsupportedOperationException("persist not supported");
    }

	public static final EntityManager entityManager() {
		throw new UnsupportedOperationException("entityManager not supported");
    }

	public static long countCcfCoreStatuses() {
        return cache.size();
    }

	public static List<CcfCoreStatus> findAllCcfCoreStatuses() {
        return Lists.newArrayList(cache.values());
    }

	public static List<CcfCoreStatus> findCcfCoreStatusEntries(int firstResult, int maxResults) {
        final List<CcfCoreStatus> allCcfCoreStatuses = findAllCcfCoreStatuses();
        final int n = allCcfCoreStatuses.size();
        if (n < firstResult)
        	return Collections.emptyList();
        final int lastResult = Math.min(firstResult + maxResults, n);
		return allCcfCoreStatuses.subList(firstResult, lastResult);
    }

	@Transactional
    public void clear() {
		throw new UnsupportedOperationException("clear not supported");
    }

	@Transactional
    public void flush() {
		throw new UnsupportedOperationException("flush not supported");
    }

	public Long getId() {
        return this.id;
    }

	public Integer getVersion() {
        return this.version;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Transactional
    public void remove() {
		cache.remove(direction.getId());
    }

	public void setId(Long id) {
        this.id = id;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Version
    @Column(name = "version")
    private Integer version;

	public static TypedQuery<CcfCoreStatus> findCcfCoreStatusesByDirection(final Direction direction) {
		return new TypedQuery<CcfCoreStatus>() {
			
			@Override
			public <T> T unwrap(Class<T> cls) {
				return null;
			}
			
			@Override
			public boolean isBound(Parameter<?> param) {
				return false;
			}
			
			@Override
			public Set<Parameter<?>> getParameters() {
				return null;
			}
			
			@Override
			public Object getParameterValue(int position) {
				return null;
			}
			
			@Override
			public Object getParameterValue(String name) {
				return null;
			}
			
			@Override
			public <T> T getParameterValue(Parameter<T> param) {
				return null;
			}
			
			@Override
			public <T> Parameter<T> getParameter(int position, Class<T> type) {
				return null;
			}
			
			@Override
			public <T> Parameter<T> getParameter(String name, Class<T> type) {
				return null;
			}
			
			@Override
			public Parameter<?> getParameter(int position) {
				return null;
			}
			
			@Override
			public Parameter<?> getParameter(String name) {
				return null;
			}
			
			@Override
			public int getMaxResults() {
				return 0;
			}
			
			@Override
			public LockModeType getLockMode() {
				return null;
			}
			
			@Override
			public Map<String, Object> getHints() {
				return Collections.emptyMap();
			}
			
			@Override
			public FlushModeType getFlushMode() {
				return null;
			}
			
			@Override
			public int getFirstResult() {
				return 0;
			}
			
			@Override
			public int executeUpdate() {
				return 0;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setParameter(int position, Date value,
					TemporalType temporalType) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setParameter(int position, Calendar value,
					TemporalType temporalType) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setParameter(String name, Date value,
					TemporalType temporalType) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setParameter(String name, Calendar value,
					TemporalType temporalType) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setParameter(Parameter<Date> param,
					Date value, TemporalType temporalType) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setParameter(Parameter<Calendar> param,
					Calendar value, TemporalType temporalType) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setParameter(int position, Object value) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setParameter(String name, Object value) {
				return this;
			}
			
			@Override
			public <T> TypedQuery<CcfCoreStatus> setParameter(Parameter<T> param,
					T value) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setMaxResults(int maxResult) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setLockMode(LockModeType lockMode) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setHint(String hintName, Object value) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setFlushMode(FlushModeType flushMode) {
				return this;
			}
			
			@Override
			public TypedQuery<CcfCoreStatus> setFirstResult(int startPosition) {
				return this;
			}
			
			@Override
			public CcfCoreStatus getSingleResult() {
					final List<CcfCoreStatus> resultList = getResultList();
					if (resultList.isEmpty())
						throw new NoResultException();
					return resultList.get(0);
			}
			
			@Override
			public List<CcfCoreStatus> getResultList() {
				CcfCoreStatus res = findCcfCoreStatus(direction.getId());
				if (res == null)
					return Collections.emptyList();
				else 
					return Arrays.asList(res) ;
			}
		};
    }

	/* !@#$ AspectJ tooling can't see these methods outside this class when they're in a Roo ITD :-/ */
	
	public ExecutedCommand getExecutedCommand() {
        return this.executedCommand;
    }

	public void setCurrentStatus(CoreState currentStatus) {
        this.currentStatus = currentStatus;
    }

	public void setExecutedCommand(ExecutedCommand executedCommand) {
        this.executedCommand = executedCommand;
    }

	/* non-javabean getter and setter to prevent Roo from exposing this in the scaffolds >:-( */
	public void coreProcess(Process coreProcess) {
		this.coreProcess = coreProcess;
	}

	public Process coreProcess() {
		return coreProcess;
	}

	@XmlTransient
	public Direction getDirection() {
        return this.direction;
    }

	public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
