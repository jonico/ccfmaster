package com.collabnet.ccf.ccfmaster.server.domain;

import java.rmi.RemoteException;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.pluggable.IntegratedApplicationClient;

/**
 * This corresponds to an IAF integrated application.
 * TODO: rename this class to IntegratedApp
 * @author ctaylor
 *
 */
@RooJavaBean
@RooToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "LANDSCAPE", "PROJECTPATH" }))
@RooEntity(finders = { "findExternalAppsByLinkIdEquals", "findExternalAppsByLandscape" })
public class ExternalApp {

    private static Logger log = LoggerFactory.getLogger(ExternalApp.class);

    @NotNull
    @Pattern(regexp = "^prpl\\d+$")
    @Column(unique = true)
    private String linkId;

    @NotNull
    @Pattern(regexp = "^projects\\..*")
    @Index(name = "projectPathIndex")
    private String projectPath;

    @NotNull
    @XmlJavaTypeAdapter(Landscape.XmlAdapter.class)
    @ManyToOne(cascade = {  })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Landscape landscape;

    public static ExternalApp createNewExternalApp(String linkId, final Connection connection) throws RemoteException {
        final IntegratedApplicationClient integratedAppClient = connection.getIntegratedAppClient();
        String projectPath = integratedAppClient.getProjectPathByIntegratedAppId(linkId);
        String baseURL = integratedAppClient.getBaseUrlByLinkId(linkId);
        String plugId = integratedAppClient.getPlugIdByBaseUrl(baseURL);
        log.debug("auto-creating new ExternalApp with linkId={} and plugId={}", linkId, plugId);
        ExternalApp externalApp = new ExternalApp();
        externalApp.setLinkId(linkId);
        externalApp.setProjectPath(projectPath);
        externalApp.setLandscape(Landscape.findLandscapesByPlugIdEquals(plugId).getSingleResult());
        externalApp.persist();
        return externalApp;
    }

    public static class XmlAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<Long, ExternalApp> {

        @Override
        public ExternalApp unmarshal(Long v) throws Exception {
            return findExternalApp(v);
        }

        @Override
        public Long marshal(ExternalApp v) throws Exception {
            return v.getId();
        }
    }

    public static ExternalApp valueOf(String linkId) {
        return findExternalAppsByLinkIdEquals(linkId).getSingleResult();
    }

	public static TypedQuery<ExternalApp> findExternalAppsByLandscape(Landscape landscape) {
        if (landscape == null) throw new IllegalArgumentException("The landscape argument is required");
        EntityManager em = entityManager();
        TypedQuery<ExternalApp> q = em.createQuery("SELECT ExternalApp FROM ExternalApp AS externalapp WHERE externalapp.landscape = :landscape", ExternalApp.class);
        q.setParameter("landscape", landscape);
        return q;
    }
	
	public static long countExternalAppsByLandscape(Landscape landscape) {
        if (landscape == null) throw new IllegalArgumentException("The landscape argument is required");
        EntityManager em = entityManager();
        return em.createQuery("SELECT COUNT(externalapp) FROM ExternalApp AS externalapp WHERE externalapp.landscape = :landscape", Long.class).setParameter("landscape", landscape).getSingleResult();
    }
	
}
