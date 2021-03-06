// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import java.lang.String;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ExternalApp_Roo_Finder {
    
    public static TypedQuery<ExternalApp> ExternalApp.findExternalAppsByLinkIdEquals(String linkId) {
        if (linkId == null || linkId.length() == 0) throw new IllegalArgumentException("The linkId argument is required");
        EntityManager em = ExternalApp.entityManager();
        TypedQuery<ExternalApp> q = em.createQuery("SELECT o FROM ExternalApp AS o WHERE o.linkId = :linkId", ExternalApp.class);
        q.setParameter("linkId", linkId);
        return q;
    }
    
}
