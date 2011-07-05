package com.collabnet.ccf.ccfmaster.authorization;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import com.collabnet.ccf.ccfmaster.authentication.IafUserDetails;
import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;

public class IntegratedAppVoter implements AccessDecisionVoter {

	private static final String INTEGRATED_APPLICATION_CHECK = "INTEGRATED_APPLICATION_CHECK";
	private static final Logger log = LoggerFactory.getLogger(IntegratedAppVoter.class);

	/**
	 * If the secured object is marked with INTEGRATED_APPLICATION_CHECK, this
	 * voter will check that
	 * <ul>
	 * <li>the current principal is an instance of IafUserDetails</li>
	 * <li>the request URL starts with the principal's linkId attribute</li>
	 * </ul>
	 * 
	 * @return If these conditions are met, returns ACCESS_GRANTED, otherwise
	 *         returns ACCESS_DENIED. If the resource is not marked, returns
	 *         ACCESS_ABSTAIN.
	 */
	@Override
	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
        assert authentication != null;
        assert object != null;
        assert attributes != null;
        log.debug("checking if linkId is correct");
        // logic inspired by RoleVoter
        int result = ACCESS_ABSTAIN;
        for (ConfigAttribute attribute : attributes) {
        	if (this.supports(attribute)) {
        		result = ACCESS_DENIED;
        		if (authentication.getPrincipal() instanceof IafUserDetails) {
                    IafUserDetails user = (IafUserDetails) authentication.getPrincipal();
                    FilterInvocation fi = (FilterInvocation)object;
                    if (checkAccess(user, fi.getRequestUrl())) {
                    	log.debug("granting access");
                    	return ACCESS_GRANTED;
                    }
    	        } else if (authentication.getPrincipal() instanceof TFUserDetails) {
    	        	TFUserDetails user = (TFUserDetails) authentication.getPrincipal();
    	        	if (user.getAuthorities().contains(TFUserDetails.SUPER_USER)) {
    	        		return ACCESS_GRANTED;
    	        	}
    	        }
        	}
        }
        log.debug(result == ACCESS_ABSTAIN ? "abstaining" : "denying access");
        return result;
	}

	private boolean checkAccess(IafUserDetails user, String requestUrl) {
        String linkId = findLinkIdInUrl(requestUrl, user);
        return (linkId != null && linkId.equals(user.getLinkId()));
	}

	private String findLinkIdInUrl(String requestUrl, IafUserDetails user) {
		String prefix = "/linkid/prpl";
		if (requestUrl.startsWith(prefix)) {
			return requestUrl.split("/", 4)[2];
		}
		return null;
	}

	/**
	 * IntegratedAppVoter supports resources marked with
	 * INTEGRATED_APPLICATION_CHECK
	 */
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return INTEGRATED_APPLICATION_CHECK.equals(attribute.getAttribute());
	}

	/**
	 * the protected object must be a {@link FilterInvocation}
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(FilterInvocation.class);
	}

}
