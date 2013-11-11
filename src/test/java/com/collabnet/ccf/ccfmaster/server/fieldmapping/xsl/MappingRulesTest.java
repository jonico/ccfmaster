package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import static org.junit.Assert.fail;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import junit.framework.Assert;

import org.junit.Test;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules;

public class MappingRulesTest {

    @Test
    public void testSafeXSLT() {
        FieldMapping mapping = new FieldMapping();
        mapping.setKind(FieldMappingKind.MAPPING_RULES);
        MappingRules emptyMapping = new MappingRules(mapping);
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<MappingRules>> errors = validator
                .validate(emptyMapping);
        // two non null, two safe xslt violations
        Assert.assertEquals(4, errors.size());
        int numberOfNonNull = 0;
        int numberOfSafeXsltViolatios = 0;
        for (ConstraintViolation<MappingRules> constraintViolation : errors) {
            String message = constraintViolation.getMessage();
            if ("may not be null".equals(message)) {
                ++numberOfNonNull;
            } else if (SafeXslt.NOT_A_SAFE_XSLT.equals(message)) {
                ++numberOfSafeXsltViolatios;
            } else {
                fail("Unexpected violation message: " + message);
            }
        }
        Assert.assertEquals(2, numberOfNonNull);
        Assert.assertEquals(2, numberOfSafeXsltViolatios);
    }

}
