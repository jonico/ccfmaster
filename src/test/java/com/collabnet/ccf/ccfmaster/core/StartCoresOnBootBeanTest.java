package com.collabnet.ccf.ccfmaster.core;

import java.util.Arrays;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import com.collabnet.ccf.ccfmaster.server.core.StartCoresOnBootBean;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;

public class StartCoresOnBootBeanTest {

    @Mocked(methods = { "merge()", "findCcfCoreStatus(Long)",
            "getCurrentStatus()" })
    CcfCoreStatus ccs;
    @Mocked(methods = { "findAllDirections()" })
    Direction     dir;

    @Test
    public void bootCallsMergeIfShouldStartAutomatically() {
        final long id = 123L;
        dir.setId(id);
        dir.setShouldStartAutomatically(true);
        ccs.setDirection(dir);
        ccs.setId(id);
        new Expectations() {
            {
                Direction.findAllDirections();
                returns(Arrays.asList(dir));
                CcfCoreStatus.findCcfCoreStatus(id);
                returns(ccs);
                ccs.merge();
            }
        };
        new StartCoresOnBootBean().boot();
    }

    @Test
    public void bootDoesnotCallMergeIfNotShouldStartAutomatically() {
        final long id = 123L;
        dir.setId(id);
        dir.setShouldStartAutomatically(false);
        ccs.setDirection(dir);
        ccs.setId(id);
        new Expectations() {
            {
                Direction.findAllDirections();
                returns(Arrays.asList(dir));
                ccs.merge();
                times = 0;
            }
        };
        new StartCoresOnBootBean().boot();
    }

    @Test
    public void shutdownCallsMergeAndThenCallsGetCurrentStatusUntilPollCountReached() {
        final long id = 123L;
        dir.setId(id);
        ccs.setDirection(dir);
        ccs.setId(id);
        new Expectations() {
            {
                Direction.findAllDirections();
                returns(Arrays.asList(dir));
                CcfCoreStatus.findCcfCoreStatus(id);
                returns(ccs);
                ccs.merge();
                returns(ccs);
                ccs.getCurrentStatus();
                returns(Arrays.asList(CoreState.STOPPING, CoreState.STOPPING));
            }
        };
        final StartCoresOnBootBean scobb = new StartCoresOnBootBean();
        scobb.setPollCount(2);
        scobb.setDelayBetweenPollMillis(0);
        scobb.shutdown();
    }

    @Test
    public void shutdownCallsMergeAndThenCallsGetCurrentStatusUntilStopped() {
        final long id = 123L;
        dir.setId(id);
        ccs.setDirection(dir);
        ccs.setId(id);
        new Expectations() {
            {
                Direction.findAllDirections();
                returns(Arrays.asList(dir));
                CcfCoreStatus.findCcfCoreStatus(id);
                returns(ccs);
                ccs.merge();
                returns(ccs);
                ccs.getCurrentStatus();
                returns(Arrays.asList(CoreState.STOPPING, CoreState.STOPPING,
                        CoreState.STOPPED));
            }
        };
        final StartCoresOnBootBean scobb = new StartCoresOnBootBean();
        scobb.setDelayBetweenPollMillis(0);
        scobb.shutdown();
    }
}
