package com.collabnet.ccf.ccfmaster.config;

import java.io.Serializable;
import java.util.Properties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

@XmlRootElement
public final class Version implements Comparable<Version>, Serializable {
    private static final long  serialVersionUID        = 1L;

    public static final String CCFCORE_REVISION_STRING = "ccfcore.revisionstring";
    public static final String CCFCORE_MAJOR_VERSION   = "ccfcore.major.version";
    public static final String CCFCORE_MINOR_VERSION   = "ccfcore.minor.version";
    public static final String CCFCORE_PATCH_VERSION   = "ccfcore.patch.version";

    private final int          major;
    private final int          minor;
    private final int          patch;
    private final String       revision;

    public Version(int major, int minor, int patch, String revision) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.revision = revision;
    }

    // for JAXB
    @SuppressWarnings("unused")
    private Version() {
        this(0, 0, 0, "");
    }

    @Override
    public int compareTo(Version o) {
        if (o == null)
            return 1; // FIXME: 
        return ComparisonChain.start().compare(getMajor(), o.getMajor())
                .compare(getMinor(), o.getMinor())
                .compare(getPatch(), o.getPatch())
                .compare(getRevision(), o.getRevision()).result();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Version))
            return false;
        Version ov = (Version) o;
        return getMajor() == ov.getMajor() && getMinor() == ov.getMinor()
                && getPatch() == ov.getPatch()
                && Objects.equal(getRevision(), ov.getRevision());
    }

    @XmlElement
    public int getMajor() {
        return major;
    }

    @XmlElement
    public int getMinor() {
        return minor;
    }

    @XmlElement
    public int getPatch() {
        return patch;
    }

    @XmlElement
    public String getRevision() {
        return revision;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMajor(), getMinor(), getPatch(),
                getRevision());

    }

    public boolean isNewerThan(Version o) {
        return this.compareTo(o) > 0;
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d (%s)", getMajor(), getMinor(),
                getPatch(), getRevision());
    }

    public static Version of(final Properties properties)
            throws NumberFormatException {
        try {
            int major = Integer.parseInt(properties
                    .getProperty(CCFCORE_MAJOR_VERSION));
            int minor = Integer.parseInt(properties
                    .getProperty(CCFCORE_MINOR_VERSION));
            int patch = Integer.parseInt(properties
                    .getProperty(CCFCORE_PATCH_VERSION));
            String revision = properties.getProperty(CCFCORE_REVISION_STRING);
            return new Version(major, minor, patch, revision);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}