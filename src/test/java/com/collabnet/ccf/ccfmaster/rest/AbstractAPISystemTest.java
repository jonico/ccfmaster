package com.collabnet.ccf.ccfmaster.rest;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.collabnet.ccf.ccfmaster.server.domain.ConflictResolutionPolicy;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeList;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantList;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionStatus;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;

@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext-test-contentresolver.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractAPISystemTest {

    public static String                 ccfAPIUrl    = "http://localhost:9090/CCFMaster/api";
    protected RestTemplate               restTemplate = new RestTemplate();
    protected Participant                tf;
    protected Participant                qc;
    protected Landscape                  qctf;
    protected Direction                  qc2tf;
    protected Direction                  tf2qc;
    protected ExternalApp                ea;
    protected RepositoryMapping          rm;
    protected RepositoryMappingDirection rmd;
    protected LandscapeConfig            qctfconfig;
    protected DirectionConfig            qc2tfconfig;
    protected ParticipantConfig          qcconfig;

    @Before
    public void createTestData() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/xml");
        headers.add("Authorization", "Basic YWRtaW46YWRtaW4=");

        tf = new Participant();
        tf.setDescription("Description for TF landscape");
        tf.setSystemId("TeamForge");
        tf.setSystemKind(SystemKind.TF);
        tf.setTimezone(Timezone.Europe_SLASH_Belgrade);
        tf.setEncoding("normal");

        tf = restTemplate.postForObject(ccfAPIUrl + "/participants", tf,
                Participant.class);
        Map<String, String> vars = Collections.singletonMap("id", tf.getId()
                + "");
        //Headers headers = new Headers();

        tf.setTimezone(Timezone.Europe_SLASH_Paris);
        restTemplate.put(ccfAPIUrl + "/participants/{id}",
                new HttpEntity<Participant>(tf, headers), vars);

        //restTemplate.delete(ccfAPIUrl + "/participants/{id}", vars);

        qc = new Participant();
        qc.setDescription("QC");
        qc.setSystemId("QC");
        qc.setSystemKind(SystemKind.QC);
        qc.setTimezone(Timezone.Europe_SLASH_Paris);
        qc = restTemplate.postForObject(ccfAPIUrl + "/participants", qc,
                Participant.class);

        qcconfig = new ParticipantConfig();
        qcconfig.setParticipant(qc);
        qcconfig.setName("participant.foo");
        qcconfig.setVal("bar");
        qcconfig = restTemplate.postForObject(
                ccfAPIUrl + "/participantconfigs", qcconfig,
                ParticipantConfig.class);

        qctf = new Landscape();
        qctf.setTeamForge(tf);
        qctf.setParticipant(qc);
        qctf.setName("QCTF");
        qctf.setPlugId("plug1234");
        qctf = restTemplate.postForObject(ccfAPIUrl + "/landscapes",
                new HttpEntity<Landscape>(qctf, headers), Landscape.class);

        qctfconfig = new LandscapeConfig();
        qctfconfig.setLandscape(qctf);
        qctfconfig.setName("landscape.foo");
        qctfconfig.setVal("bar");
        qctfconfig = restTemplate.postForObject(
                ccfAPIUrl + "/landscapeconfigs", qctfconfig,
                LandscapeConfig.class);

        qc2tf = new Direction();
        qc2tf.setDescription("QC-TF");
        qc2tf.setLandscape(qctf);
        qc2tf.setDirection(Directions.REVERSE);
        qc2tf = restTemplate.postForObject(ccfAPIUrl + "/directions", qc2tf,
                Direction.class);

        qc2tfconfig = new DirectionConfig();
        qc2tfconfig.setDirection(qc2tf);
        qc2tfconfig.setName("direction.foo");
        qc2tfconfig.setVal("bar");
        qc2tfconfig = restTemplate.postForObject(ccfAPIUrl
                + "/directionconfigs", qc2tfconfig, DirectionConfig.class);

        tf2qc = new Direction();
        tf2qc.setDescription("TF-QC");
        tf2qc.setLandscape(qctf);
        tf2qc.setDirection(Directions.FORWARD);
        tf2qc = restTemplate.postForObject(ccfAPIUrl + "/directions", tf2qc,
                Direction.class);

        ea = new ExternalApp();
        ea.setLandscape(qctf);
        ea.setLinkId("prpl1043");
        ea.setProjectPath("projects.brokerage_system_sample");
        ea = restTemplate.postForObject(ccfAPIUrl + "/externalapps", ea,
                ExternalApp.class);

        rm = new RepositoryMapping();
        rm.setExternalApp(ea);
        rm.setDescription("Defects to tracker1234");
        rm.setTeamForgeRepositoryId("Defects");
        rm.setParticipantRepositoryId("tracker1234");
        rm = restTemplate.postForObject(ccfAPIUrl + "/repositorymappings", rm,
                RepositoryMapping.class);

        rmd = new RepositoryMappingDirection();
        rmd.setRepositoryMapping(rm);
        rmd.setDirection(qc2tf.getDirection());
        rmd.setStatus(RepositoryMappingDirectionStatus.RUNNING);
        rmd.setConflictResolutionPolicy(ConflictResolutionPolicy.alwaysOverrideAndIgnoreLocks);
        rmd.setLastSourceArtifactModificationDate(new Date());
        rmd = restTemplate.postForObject(ccfAPIUrl
                + "/repositorymappingdirections", rmd,
                RepositoryMappingDirection.class);
    }

    @After
    public void wipeTestData() {
        LandscapeList landscapes = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes", LandscapeList.class);
        for (Landscape landscape : landscapes) {
            restTemplate.delete(ccfAPIUrl + "/landscapes/" + landscape.getId());
        }

        ParticipantList participants = restTemplate.getForObject(ccfAPIUrl
                + "/participants", ParticipantList.class);
        for (Participant participant : participants) {
            restTemplate.delete(ccfAPIUrl + "/participants/"
                    + participant.getId());
        }
    }

}
