package com.collabnet.ccf.ccfmaster.server.core.update;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

public class CoreVersionTest {

	@Test
	public void everythingIsGreaterThanNull() {
		CoreVersion v = new CoreVersion(0,0,0,"");
		assertTrue("any version should be newer than null", v.isNewerThan(null));
	}

	@Test
	public void majorTrumpsMinor() {
		CoreVersion v1 = new CoreVersion(1,0,0,"");
		CoreVersion v2 = new CoreVersion(0,1,0,"");
		assertTrue("major should trump minor version", v1.isNewerThan(v2));
	}

	@Test
	public void minorDecidesEqualMajor() {
		CoreVersion v1 = new CoreVersion(1,1,0,"");
		CoreVersion v2 = new CoreVersion(1,0,0,"");
		assertTrue("minor decides when major versions are equal", v1.isNewerThan(v2));
	}

	@Test
	public void minorTrumpsPatch() {
		CoreVersion v1 = new CoreVersion(0,1,0,"");
		CoreVersion v2 = new CoreVersion(0,0,1,"");
		assertTrue("minor should trump patch version", v1.isNewerThan(v2));
	}

	@Test
	public void patchDecidesEqualMinor() {
		CoreVersion v1 = new CoreVersion(0,1,1,"");
		CoreVersion v2 = new CoreVersion(0,1,0,"");
		assertTrue("patch decides when minor versions are equal", v1.isNewerThan(v2));
	}
}
