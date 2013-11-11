package com.collabnet.ccf.ccfmaster.controller.web;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.collabnet.ccf.ccfmaster.authentication.IafUserDetails;
import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;
import com.collabnet.ccf.ccfmaster.controller.web.project.ProjectIndexController;

@Controller
public class IndexController {

    /**
     * Redirect the user to the appropriate view. The view is determined as
     * follows:
     * <ul>
     * <li>If the user is logged in via IAF, redirect to the project view</li>
     * <li>else if the user is logged in as a TF admin, redirect to the</li>
     * <li>else throw a {@link BadCredentialsException}, which causes Spring
     * security to redirect to the login window.</li>
     * </ul>
     * 
     * @throws BadCredentialsException
     *             if user not authorized sufficiently.
     * @return redirect location
     */
    //	@PreAuthorize("isAuthenticated() and hasRole('ROLE_TF_USER')")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (auth == null)
            throw new AccessDeniedException("Not authenticated at all");
        Object principal = auth.getPrincipal();
        if (principal instanceof IafUserDetails)
            return "redirect:" + ProjectIndexController.PROJECT_INDEX_PATH;
        else if (principal instanceof TFUserDetails
                && auth.getAuthorities().contains(TFUserDetails.SUPER_USER))
            return "redirect:" + UIPathConstants.CREATELANDSCAPE_CCFMASTER;
        else
            throw new AccessDeniedException(
                    "Not authenticated as a TeamForge Site Admin or IAF user with sufficient permissions.");
    }

}
