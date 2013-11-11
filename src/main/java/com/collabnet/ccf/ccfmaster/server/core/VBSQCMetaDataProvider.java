package com.collabnet.ccf.ccfmaster.server.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.util.Obfuscator;

public class VBSQCMetaDataProvider implements QCMetaDataProvider {

    private ExecutorService     executor;

    private static final String TIMEOUT                                            = "//T:30";
    //	private static final String UNICODE = "//U";
    private static final String BATCH_MODE                                         = "//B";
    private final static String GET_QC_DOMAINS                                     = "getQCDomains.vbs";
    private final static String GET_QC_PROJECTS                                    = "getQCProjects.vbs";
    private final static String GET_QC_REQUIREMENT_TYPES                           = "getQCRequirementTypes.vbs";
    private final static String VALIDATE_QC_DOMAIN_AND_PROJECT                     = "validateQCDomainAndProject.vbs";
    private final static String VALIDATE_QC_DOMAIN_AND_PROJECT_AND_REQUIREMENTTYPE = "validateQCDomainAndProjectAndRequirementType.vbs";
    private final static String GET_QC_DEFECT_FIELDS                               = "getQCDefectFields.vbs";
    private final static String GET_QC_REQUIREMENT_FIELDS                          = "getQCRequirementFields.vbs";

    private final static String CSCRIPT_EXE                                        = "cscript.exe";
    private final static String CSCRIPT_EXE_64                                     = "C:\\Windows\\SysWOW64\\cscript.exe";

    private static final String VBS_SCRIPT_DIRECTORY                               = "scripts";
    private String              ccfHome;

    public String getCcfHome() {
        return ccfHome;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public File getScriptDirectory(Landscape landscape) {
        return new File(ccfHome, "landscape" + landscape.getId()
                + File.separatorChar + VBS_SCRIPT_DIRECTORY);
    }

    public void setCcfHome(String ccfHome) {
        this.ccfHome = ccfHome;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public String showDefectFields(Landscape landscape, String domain,
            String project, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException {
        String scriptParms[] = { getCscriptExe(), BATCH_MODE, TIMEOUT,
                getShowDefectFieldsScript(landscape).getAbsolutePath(), qcURL,
                qcUser, Obfuscator.decodePassword(qcPassword), domain, project };
        try {
            return getProcessDocument(scriptParms);
        } catch (Exception e) {
            throw new CoreConfigurationException(
                    "Could not retrieve QC meta data: " + e.getMessage(), e);
        }
    }

    @Override
    public String showRequirementFields(Landscape landscape, String domain,
            String project, String requirementType, String qcURL,
            String qcUser, String qcPassword) throws CoreConfigurationException {
        String scriptParms[] = { getCscriptExe(), BATCH_MODE, TIMEOUT,
                getShowRequirementFieldsScript(landscape).getAbsolutePath(),
                qcURL, qcUser, Obfuscator.decodePassword(qcPassword), domain,
                project, requirementType };
        try {
            return getProcessDocument(scriptParms);
        } catch (Exception e) {
            throw new CoreConfigurationException(
                    "Could not retrieve QC meta data: " + e.getMessage(), e);
        }
    }

    @Override
    public String showRequirementTypes(Landscape landscape, String domain,
            String project, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException {
        String scriptParms[] = { getCscriptExe(), BATCH_MODE, TIMEOUT,
                getShowRequirementTypesScript(landscape).getAbsolutePath(),
                qcURL, qcUser, Obfuscator.decodePassword(qcPassword), domain,
                project };
        try {
            return getProcessDocument(scriptParms);
        } catch (Exception e) {
            throw new CoreConfigurationException(
                    "Could not retrieve QC meta data: " + e.getMessage(), e);
        }
    }

    @Override
    public String showVisibleDomains(Landscape landscape, String qcURL,
            String qcUser, String qcPassword) throws CoreConfigurationException {
        String scriptParms[] = { getCscriptExe(), BATCH_MODE, TIMEOUT,
                getGetDomainsScript(landscape).getAbsolutePath(), qcURL,
                qcUser, Obfuscator.decodePassword(qcPassword) };
        try {
            return getProcessDocument(scriptParms);
        } catch (Exception e) {
            throw new CoreConfigurationException(
                    "Could not retrieve QC meta data: " + e.getMessage(), e);
        }
    }

    @Override
    public String showVisibleProjectsInDomain(Landscape landscape,
            String domain, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException {
        String scriptParms[] = { getCscriptExe(), BATCH_MODE, TIMEOUT,
                getGetProjectsScript(landscape).getAbsolutePath(), qcURL,
                qcUser, Obfuscator.decodePassword(qcPassword), domain };
        try {
            return getProcessDocument(scriptParms);
        } catch (Exception e) {
            throw new CoreConfigurationException(
                    "Could not retrieve QC meta data: " + e.getMessage(), e);
        }
    }

    @Override
    public String validateDomainAndProject(Landscape landscape, String domain,
            String project, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException {
        String scriptParms[] = {
                getCscriptExe(),
                BATCH_MODE,
                TIMEOUT,
                getGetValidateDomainAndProjectScript(landscape)
                        .getAbsolutePath(), qcURL, qcUser,
                Obfuscator.decodePassword(qcPassword), domain, project };
        try {
            return getProcessDocument(scriptParms);
        } catch (Exception e) {
            throw new CoreConfigurationException(
                    "Could not retrieve QC meta data: " + e.getMessage(), e);
        }
    }

    @Override
    public String validateDomainAndProjectAndRequirementType(
            Landscape landscape, String domain, String project,
            String requirementType, String qcURL, String qcUser,
            String qcPassword) throws CoreConfigurationException {
        String scriptParms[] = {
                getCscriptExe(),
                BATCH_MODE,
                TIMEOUT,
                getValidateDomainAndProjectAndRequirementTypeScript(landscape)
                        .getAbsolutePath(), qcURL, qcUser,
                Obfuscator.decodePassword(qcPassword), domain, project,
                requirementType };
        try {
            return getProcessDocument(scriptParms);
        } catch (Exception e) {
            throw new CoreConfigurationException(
                    "Could not retrieve QC meta data: " + e.getMessage(), e);
        }
    }

    private File getGetDomainsScript(Landscape landscape) {
        return new File(getScriptDirectory(landscape), GET_QC_DOMAINS);
    }

    private File getGetProjectsScript(Landscape landscape) {
        return new File(getScriptDirectory(landscape), GET_QC_PROJECTS);
    }

    private File getGetValidateDomainAndProjectScript(Landscape landscape) {
        return new File(getScriptDirectory(landscape),
                VALIDATE_QC_DOMAIN_AND_PROJECT);
    }

    private String getProcessDocument(String[] scriptParms) throws Exception {
        // TODO: Should this method be synchronized to avoid QC locks?
        Process process = Runtime.getRuntime().exec(scriptParms);
        killProcessAfterTimeout(process, getExecutor(), 20000);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }
        br.close();
        process.getOutputStream().close();
        process.getInputStream().close();
        process.getErrorStream().close();
        process.waitFor();
        String processOutput = sb.toString();
        return processOutput;
    }

    private File getShowDefectFieldsScript(Landscape landscape) {
        return new File(getScriptDirectory(landscape), GET_QC_DEFECT_FIELDS);
    }

    private File getShowRequirementFieldsScript(Landscape landscape) {
        return new File(getScriptDirectory(landscape),
                GET_QC_REQUIREMENT_FIELDS);
    }

    private File getShowRequirementTypesScript(Landscape landscape) {
        return new File(getScriptDirectory(landscape), GET_QC_REQUIREMENT_TYPES);
    }

    private File getValidateDomainAndProjectAndRequirementTypeScript(
            Landscape landscape) {
        return new File(getScriptDirectory(landscape),
                VALIDATE_QC_DOMAIN_AND_PROJECT_AND_REQUIREMENTTYPE);
    }

    private void killProcessAfterTimeout(final Process process,
            ExecutorService executorService, final long timeout) {
        Runnable processKiller = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                }
                process.destroy();
            }
        };
        executorService.submit(processKiller);
    }

    private static String getCscriptExe() {
        File cscript64File = new File(CSCRIPT_EXE_64);
        if (cscript64File.exists()) {
            return CSCRIPT_EXE_64;
        } else {
            return CSCRIPT_EXE;
        }
    }

}