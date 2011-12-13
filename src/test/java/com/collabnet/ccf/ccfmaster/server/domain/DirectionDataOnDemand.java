package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;

@RooDataOnDemand(entity = Direction.class)
public class DirectionDataOnDemand {

	private List<Direction> data;

	public void init() {
        data = com.collabnet.ccf.ccfmaster.server.domain.Direction.findDirectionEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'Direction' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.collabnet.ccf.ccfmaster.server.domain.Direction>();
        // unique key constraints on enum
        for (int i = 0; i < 2; i++) {
            com.collabnet.ccf.ccfmaster.server.domain.Direction obj = getNewTransientDirection(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }

	public Direction getNewTransientDirection(int index) {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = new com.collabnet.ccf.ccfmaster.server.domain.Direction();
        obj.setDescription("description_" + index);
        obj.setDirection(com.collabnet.ccf.ccfmaster.server.domain.Directions.class.getEnumConstants()[index % 2]);
        obj.setShouldStartAutomatically(false);
        obj.setLandscape(landscapeDataOnDemand.getRandomLandscape());
        return obj;
    }

	@Autowired
    private LandscapeDataOnDemand landscapeDataOnDemand;
}
