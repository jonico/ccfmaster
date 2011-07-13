package com.collabnet.ccf.ccfmaster.server.domain;

import org.springframework.util.Assert;


public aspect RepositoryMappingDirectionModificationAspect {
	before(RepositoryMappingDirection rmd) : execution(RepositoryMappingDirection RepositoryMappingDirection.merge()) && target(rmd) {
		if (rmd.getActiveFieldMapping() != null) {
			Assert.isTrue(rmd.getId().equals(rmd.getActiveFieldMapping().getParent().getId()), 
			"fieldMapping has different parent");
		}
	}
}
