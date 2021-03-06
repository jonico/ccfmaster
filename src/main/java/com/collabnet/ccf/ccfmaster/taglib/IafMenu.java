package com.collabnet.ccf.ccfmaster.taglib;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.collabnet.ccf.ccfmaster.authentication.IafUserDetails;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.main.TeamForgeClient;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public class IafMenu extends TagSupport {

    private static final long   serialVersionUID  = 1L;
    private static final Logger log               = LoggerFactory
                                                          .getLogger(IafMenu.class);
    private static final String CTF82_API_VERSION = "6.7.0.0";
    private static final String CTF71_API_VERSION = "6.3.0.0";
    private static final String IFRAME_BEGIN      = "<iframe id=\"myframe\" scrolling=\"no\" marginwidth=\"0\" marginheight=\"0\" cellspacing=\"0\" cellpadding=\"0\" frameborder=\"0\" vspace=\"0\" hspace=\"0\" style=\"overflow: hidden; width: 100%\" src=\"";
    private static final String IFRAME_END        = "</iframe>";
    private static final String CLOSE_TAG         = "\">";

    @Override
    public int doEndTag() throws JspException {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if (auth == null) {
            log.debug("Authentication from spring security was null.");
            return SKIP_PAGE;
        }
        try {
            IafUserDetails user = (IafUserDetails) auth.getPrincipal();
            HttpServletRequest request = (HttpServletRequest) pageContext
                    .getRequest();
            Connection connection = user.getConnection();
            String apiVersion = connection.getTeamForgeClient().getApiVersion();
            //From TF 7.2 use the default code implementation of topinclude (http://<>/sf/sfmain/do/topInclude/projects.xxxx?linkId=prplxxxx)
            if (apiVersion.compareTo(CTF71_API_VERSION) > 0) {
                pageContext.getOut().write(getCTFHeaderBar(user, request));
            } else {
                pageContext.getOut().write(getBanner(user, request));
            }
            pageContext.getOut().flush();
            return EVAL_PAGE;
        } catch (RemoteException e) {
            throw new JspException("Error communicating with TeamForge", e);
        } catch (URISyntaxException e) {
            throw new JspException("Bad TF URI: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new JspException("Error getting data from TeamForge: "
                    + e.getMessage(), e);
        }
    }

    @Override
    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    private URI constructURI(IafUserDetails user) throws IOException,
            URISyntaxException {
        final String serverUrl = user.getConnection().getServerUrl();
        final URI tfUri = new URI(serverUrl);
        final String linkId = user.getLinkId();
        final String projectPath = user.getProjectPath();
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("linkId", linkId));
        //added base parameter,needed on switching menubar icon for IE atleast
        qparams.add(new BasicNameValuePair("base", serverUrl));
        URI uri = URIUtils.createURI(tfUri.getScheme(), tfUri.getHost(),
                tfUri.getPort(), makePath(projectPath),
                URLEncodedUtils.format(qparams, "UTF-8"), null);
        return uri;
    }

    private String cssInclude(String serverUrl) {
        return String.format(
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\" />%n",
                cssUrl(serverUrl));
    }

    private String cssUrl(String serverUrl) {
        return serverUrl + "/css/styles_new.css";
    }

    private String getBanner(IafUserDetails user, HttpServletRequest request)
            throws ClientProtocolException, IOException, URISyntaxException {
        final Connection conn = user.getConnection();
        final String serverUrl = conn.getServerUrl();
        TeamForgeClient tfc = conn.getTeamForgeClient();
        final String sessionId = tfc.getWebSessionId();
        HttpClient httpclient = new DefaultHttpClient();
        URI uri = constructURI(user);
        HttpGet httpget = new HttpGet(uri);
        httpget.setHeaders(new Header[] {
                // new BasicHeader("View", lastSeenObject),
                new BasicHeader("Cookie", "JSESSIONID=" + sessionId),
                new BasicHeader("Accept-Language", request.getLocale()
                        .toString()),
                new BasicHeader("User-Agent",
                        "Mozilla/5.0 (Windows; U; WinNT4.0; en-US; rv:1.7.5) BloodRedSun/0.5"), });
        HttpResponse response = httpclient.execute(httpget);
        InputStream is = response.getEntity().getContent();

        InputStreamReader isr = new InputStreamReader(is, Charsets.UTF_8);
        StringBuffer buf = new StringBuffer();
        buf.append(IFRAME_BEGIN).append(uri).append(CLOSE_TAG);
        /*
         * the following hacky string replacements and additions are adopted
         * from com.collabnet.ce.soap50.integratedapps.taglib.ViewButtonBarTag
         * and com.collabnet.ce.soap50.integratedapps.taglib.RenderButtonBar.
         */
        buf.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        buf.append(String.format(
                "<html><head><base href=\"%s/sf\" target=\"_top\" />\n",
                serverUrl));
        buf.append(cssInclude(serverUrl));
        buf.append(jsIncludes(serverUrl));
        // hide jump box
        buf.append("<style type=\"text/css\">div.jumpbutton, div.jumpfield, td.jumpfieldcontainer, #quickJumpForm {display: none;}</style>");
        buf.append("</head><body>");
        for (String line : CharStreams.readLines(isr)) {
            buf.append(line
                    .replace("window.location=", "parent.window.location=")
                    .replace("<a class=\"yuimenubaritemlabel\"",
                            "<a class=\"yuimenubaritemlabel\" target=\"_top\"")
                    .replace("<a class=\"yuimensomeuitemlabel\"",
                            "<a class=\"yuimensomeuitemlabel\" target=\"_top\"")
                    .replace("<a class=\"yuimenuitemlabel\"",
                            "<a class=\"yuimenuitemlabel\" target=\"_top\"")
                    .replace("'/sf", "'" + serverUrl + "/sf")
                    .replace("\"/sf", "\"" + serverUrl + "/sf")
            //					.replaceAll("/sf/global/do/about", serverUrl + "/sf/global/do/about")
            );
            buf.append('\n');
        }
        buf.append("<script type='text/javascript'>\n")
                .append("document.quickJumpForm.target=\"_top\"; \n")
                .append("document.login.target=\"_top\";\n")
                .append("    if (!(typeof(YAHOO) == 'undefined')) {\n")
                .append("     YAHOO.util.Event.onContentReady(\"navLinks\", function () {\n")
                .append("        var oMenu = new YAHOO.widget.MenuBar(\"navLinks\", { \n")
                .append("                                position: \"static\",\n")
                .append("                                autosubmenudisplay: true, \n")
                .append("                                showdelay: 125,\n")
                .append("                                hidedelay:  750, \n")
                .append("                                lazyload: true });\n")
                .append("         oMenu.render();            \n")
                .append("     });\n")
                .append("    }\n")
                .append("if (typeof document.body.onselectstart != \"undefined\") ")
                .append("    document.body.onselectstart=function(){return false}")
                .append("</script>").toString();
        buf.append("</body></html>");
        buf.append(IFRAME_END);

        return buf.toString();

    }

    private String getCTFHeaderBar(IafUserDetails user,
            HttpServletRequest request) throws ClientProtocolException,
            IOException, URISyntaxException {
        final Connection conn = user.getConnection();
        String apiVersion = conn.getTeamForgeClient().getApiVersion();
        StringBuffer buf = new StringBuffer();
        /*
         * In TF8.1 and previous versions IAF apps use iframe in their base
         * pages/html and this iframe is rendered within every page of the iaf
         * application. From TF8.2 Instead of iframe, 2 html tags(<script> and
         * <div>) used in the same page/html/erb where the iframe is currently
         * embedded.
         */

        if (apiVersion.compareTo(CTF82_API_VERSION) >= 0) {
            final String serverUrl = conn.getServerUrl();
            buf.append("<script src=\"")
                    .append(serverUrl)
                    .append("/ctf/js/ctf-header.js\" data-ctf-header-include=\"")
                    .append(serverUrl)
                    .append("\" data-project-path=\"")
                    .append(user.getProjectPath())
                    .append("\" data-app-id=\"")
                    .append(user.getLinkId())
                    .append("\" data-app-base-url=\"")
                    .append(request.getContextPath())
                    .append("\" type=\"text/javascript\"></script>")
                    .append("<div class=\"ctf_7_x_styles\" saturn=\"\" ctf-url=\"")
                    .append(serverUrl)
                    .append("\"><div ng-bindable=\"\" class=\"widgets-header-include\"></div></div>");
        } else {
            buf.append(IFRAME_BEGIN).append(constructURI(user))
                    .append(CLOSE_TAG).append(IFRAME_END);
        }
        return buf.toString();

    }

    private String jsIncludes(String serverUrl) {
        String baseUrl = serverUrl + "/sf";
        StringBuilder htmlString = new StringBuilder();
        String[] scriptFiles = { "/js/sf_functions.js", "/js/jquery/jquery.js",
                "/js/jump_to.js", "/js/yui/yahoo-dom-event.js",
                "/js/yui/container_core-min.js", "/js/yui/menu-min.js" };
        for (String scriptFile : scriptFiles) {
            htmlString
                    .append(String
                            .format("<script src=\"%s%s\" type=\"text/javascript\"></script>%n",
                                    baseUrl, scriptFile));
        }
        return htmlString.toString();
    }

    private String makePath(String projectPath) {
        String res = "/sf/sfmain/do/topInclude";
        if (projectPath != null) {
            res += "/" + projectPath;
        }
        return res;
    }
}
