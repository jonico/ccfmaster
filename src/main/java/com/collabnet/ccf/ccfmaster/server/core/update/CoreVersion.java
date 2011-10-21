package com.collabnet.ccf.ccfmaster.server.core.update;

import java.io.Serializable;
import java.util.Properties;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

final class CoreVersion implements Comparable<CoreVersion>, Serializable {
	private static final long serialVersionUID = 1L;
	
	static final String CCFCORE_REVISION_STRING = "ccfcore.revisionstring";
	static final String CCFCORE_MAJOR_VERSION = "ccfcore.major.version";
	static final String CCFCORE_MINOR_VERSION = "ccfcore.minor.version";
	static final String CCFCORE_PATCH_VERSION = "ccfcore.patch.version";

	private final int major;
	private final int minor;
	private final int patch;
	private final String revision;

	public static CoreVersion of(final Properties properties) throws NumberFormatException {
		try {
			int major = Integer.parseInt(properties.getProperty(CCFCORE_MAJOR_VERSION));
			int minor = Integer.parseInt(properties.getProperty(CCFCORE_MINOR_VERSION));
			int patch = Integer.parseInt(properties.getProperty(CCFCORE_PATCH_VERSION));
			String revision = properties.getProperty(CCFCORE_REVISION_STRING);
			return new CoreVersion(major, minor, patch, revision);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public CoreVersion(int major, int minor, int patch, String revision){
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.revision = revision;
	}
	
	public boolean isNewerThan(CoreVersion o) {
		return this.compareTo(o) > 0;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getPatch() {
		return patch;
	}

	public String getRevision() {
		return revision;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CoreVersion)) return false;
		CoreVersion ov = (CoreVersion) o;
		return 
			getMajor() == ov.getMajor() &&
			getMinor() == ov.getMinor() &&
			getPatch() == ov.getPatch() &&
			Objects.equal(getRevision(), ov.getRevision());
	}
	
	@Override 
	public int hashCode() {
		return Objects.hashCode(getMajor(), getMinor(), getPatch(), getRevision());
		
	}
	
	@Override
	public String toString() {
		return String.format("%d.%d.%d (%s)", getMajor(), getMinor(), getPatch(), getRevision());
	}

	@Override
	public int compareTo(CoreVersion o) {
		if (o == null) return 1; // FIXME: 
		return ComparisonChain.start()
			.compare(getMajor(), o.getMajor())
			.compare(getMinor(), o.getMinor())
			.compare(getPatch(), o.getPatch())
			.compare(getRevision(), o.getRevision())
			.result();
	}
	
}