package org.yafa.annotations;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @see https://zenidas.wordpress.com/recipes/configurable-date-format-in-jax-rs-as-queryparam/
 */
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
public @interface DateTimeFormat {

  String DEFAULT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ";

  String value() default DEFAULT_DATE_TIME;
}