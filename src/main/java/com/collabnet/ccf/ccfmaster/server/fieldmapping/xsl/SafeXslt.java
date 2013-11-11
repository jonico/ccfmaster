package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

@Target(value = { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = XsltValidator.class)
@NotNull
public @interface SafeXslt {
    public static final String NOT_A_SAFE_XSLT = "Not a safe XSLT";

    String[] allowedFunctionNames() default { "encodeHTMLToEntityReferences",
            "stripHTML" };

    Class<?>[] groups() default {};

    String message() default NOT_A_SAFE_XSLT;

    Class<? extends Payload>[] payload() default {};
}
