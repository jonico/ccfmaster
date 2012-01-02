package com.collabnet.ccf.ccfmaster.server.core.update;

import org.junit.Test;

import com.collabnet.ccf.ccfmaster.config.Version;

import static org.junit.Assert.*;

public class CoreVersionTest {

	@Test
	public void everythingIsGreaterThanNull() {
		Version v = new Version(0,0,0,"");
		assertTrue("any version should be newer than null", v.isNewerThan(null));
	}

	@Test
	public void majorTrumpsMinor() {
		Version v1 = new Version(1,0,0,"");
		Version v2 = new Version(0,1,0,"");
		assertTrue("major should trump minor version", v1.isNewerThan(v2));
	}

	@Test
	public void minorDecidesEqualMajor() {
		Version v1 = new Version(1,1,0,"");
		Version v2 = new Version(1,0,0,"");
		assertTrue("minor decides when major versions are equal", v1.isNewerThan(v2));
	}

	@Test
	public void minorTrumpsPatch() {
		Version v1 = new Version(0,1,0,"");
		Version v2 = new Version(0,0,1,"");
		assertTrue("minor should trump patch version", v1.isNewerThan(v2));
	}

	@Test
	public void patchDecidesEqualMinor() {
		Version v1 = new Version(0,1,1,"");
		Version v2 = new Version(0,1,0,"");
		assertTrue("patch decides when minor versions are equal", v1.isNewerThan(v2));
	}
}
