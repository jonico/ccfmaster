package com.collabnet.ccf.core.utils;

/**
 * No-Op implementation of the {@link GATransformerUtil} class that ships with
 * CCF core.
 * 
 * We need this to be able to validate stylesheets that reference methods on
 * this class. Ideally, we'd want to remove this class and turn on secure
 * processing for the validation, because currently, users can call arbitrary
 * code in CCFMaster by uploading a malicious stylesheet.
 */
public class GATransformerUtil {
    public static String encodeHTMLToEntityReferences(String html) {
        throw new UnsupportedOperationException();
    }

    public static String stripHTML(String original) {
        throw new UnsupportedOperationException();
    }

    public static String trim(String stringToTrim) {
        throw new UnsupportedOperationException();
    }
}
