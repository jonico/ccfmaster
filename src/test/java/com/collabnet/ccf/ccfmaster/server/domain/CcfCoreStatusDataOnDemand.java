package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand;

@RooDataOnDemand(entity = CcfCoreStatus.class)
public class CcfCoreStatusDataOnDemand {
    @Autowired
    private DirectionDataOnDemand directionDataOnDemand;
    
    private List<CcfCoreStatus> data;
    

	private void setCurrentStatus(CcfCoreStatus obj, int index) {
        obj.setCurrentStatus(CoreState.STOPPED);
    }

	private void setDirection(CcfCoreStatus obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.Direction direction = directionDataOnDemand.getSpecificDirection(index);
        obj.setDirection(direction);
    }

	public void init() {

		throw new UnsupportedOperationException("init not supported");
/*        if (data != null && !data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus>();
        for (int i = 0; i < 10; i++) {
            CcfCoreStatus obj = getNewTransientCcfCoreStatus(i);
//            obj.persist();
//            obj.flush();
            data.add(obj);
        }
*/    }

	public CcfCoreStatus getNewTransientCcfCoreStatus(int index) {
		Direction dir = directionDataOnDemand.getNewTransientDirection(index);
        CcfCoreStatus obj = CcfCoreStatus.findCcfCoreStatus(dir.getId());
        return obj;
    }

	private void setExecutedCommand(CcfCoreStatus obj, int index) {
        obj.setExecutedCommand(ExecutedCommand.NONE);
    }

	public CcfCoreStatus getRandomCcfCoreStatus() {
		Direction dir = directionDataOnDemand.getRandomDirection();
		return CcfCoreStatus.findCcfCoreStatus(dir.getId());
    }

    public CcfCoreStatus getSpecificCcfCoreStatus(int index) {
    	Direction dir = directionDataOnDemand.getSpecificDirection(index);
    	return CcfCoreStatus.findCcfCoreStatus(dir.getId());
    }
}
