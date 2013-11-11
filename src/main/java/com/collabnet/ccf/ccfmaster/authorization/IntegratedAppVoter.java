package com.collabnet.ccf.ccfmaster.authorization;

import static com.collabnet.ccf.ccfmaster.controller.api.Paths.API_PREFIX;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.authentication.IafUserDetails;
import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;

public class IntegratedAppVoter implements AccessDecisionVoter {

    public static final String   INTEGRATED_APPLICATION_CHECK = "INTEGRATED_APPLICATION_CHECK";
    private static final Logger  log                          = LoggerFactory
                                                                      .getLogger(IntegratedAppVoter.class);
    private static final Pattern linkIdPattern                = Pattern
                                                                      .compile("^/linkid/(prpl\\d+)/");

    /**
     * the protected object must be a {@link FilterInvocation}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(FilterInvocation.class);
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
     * If the secured object is marked with INTEGRATED_APPLICATION_CHECK, this
     * voter will check that:
     * <ul>
     * <li>the current principal is an instance of IafUserDetails and</li>
     * <li>the request URL contains the principal's linkId attribute at the
     * right place;</li>
     * </ul>
     * or else that:
     * <ul>
     * <li>the current principal is an instance of {@link TFUserDetails} and
     * <li>the current principal's authorities include
     * {@link TFUserDetails.SUPER_USER}.
     * </ul>
     * 
     * @return If either of these conditions are met, returns ACCESS_GRANTED,
     *         otherwise returns ACCESS_DENIED. If the resource is not marked
     *         with INTEGRATED_APPLICATION_CHECK, returns ACCESS_ABSTAIN.
     */
    @Override
    public int vote(Authentication authentication, Object object,
            Collection<ConfigAttribute> attributes) {
        Assert.notNull(authentication);
        Assert.notNull(object);
        Assert.notNull(attributes);
        log.debug("checking if linkId is correct");
        // logic inspired by RoleVoter
        int result = ACCESS_ABSTAIN;
        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;
                if (authentication.getPrincipal() instanceof IafUserDetails) {
                    IafUserDetails user = (IafUserDetails) authentication
                            .getPrincipal();
                    FilterInvocation fi = (FilterInvocation) object;
                    if (checkAccess(user, fi.getRequestUrl())) {
                        log.debug("granting access");
                        return ACCESS_GRANTED;
                    }
                } else if (authentication.getPrincipal() instanceof TFUserDetails) {
                    TFUserDetails user = (TFUserDetails) authentication
                            .getPrincipal();
                    if (user.getAuthorities()
                            .contains(TFUserDetails.SUPER_USER)) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }
        log.debug(result == ACCESS_ABSTAIN ? "abstaining" : "denying access");
        return result;
    }

    private boolean checkAccess(IafUserDetails user, String requestUrl) {
        String linkId = findLinkIdInUrl(requestUrl);
        return (linkId != null && linkId.equals(user.getLinkId()));
    }

    /**
     * checks the URL for the presence of a linkId
     * 
     * @param requestUrl
     *            the url to check
     * @return the linkId in the URL or null if not found
     */
    static String findLinkIdInUrl(String requestUrl) {
        if (requestUrl.startsWith(API_PREFIX))
            requestUrl = requestUrl.substring(API_PREFIX.length()); // cut off "/api" prefix
        Matcher m = linkIdPattern.matcher(requestUrl);
        return m.find() ? m.group(1) : null;
    }

}
