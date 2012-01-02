package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

@Target(value={METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=XsltValidator.class)
@NotNull
public @interface SafeXslt {
	String message() default "{com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.SafeXslt.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	String[] allowedFunctionNames() default {"encodeHTMLToEntityReferences", "stripHTML"};
}
