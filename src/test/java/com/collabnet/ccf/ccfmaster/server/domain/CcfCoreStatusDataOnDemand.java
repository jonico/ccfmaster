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
    
    /*
     * the @SuppressWarnings("unused") are there so Roo doesn't think it's responsible
     */
    
    @SuppressWarnings("unused")
	private List<CcfCoreStatus> data;
    

	@SuppressWarnings("unused")
	private void setCurrentStatus(CcfCoreStatus obj, int index) {
        obj.setCurrentStatus(CoreState.STOPPED);
    }

	@SuppressWarnings("unused")
	private void setDirection(CcfCoreStatus obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.Direction direction = directionDataOnDemand.getSpecificDirection(index);
        obj.setDirection(direction);
    }

	public void init() {
		throw new UnsupportedOperationException("init not supported");
	}

	public CcfCoreStatus getNewTransientCcfCoreStatus(int index) {
		Direction dir = directionDataOnDemand.getNewTransientDirection(index);
        CcfCoreStatus obj = CcfCoreStatus.findCcfCoreStatus(dir.getId());
        return obj;
    }

	@SuppressWarnings("unused")
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
