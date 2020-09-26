package org.yafa.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @see <a href="https://zenidas.wordpress.com/recipes/configurable-date-format-in-jax-rs-as-queryparam/">tutorial
 * on custom JAX RS QueryParam</a>
 */
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
public @interface DateFormat {

  String DEFAULT_DATE = "yyyy-MM-dd";

  String value() default DEFAULT_DATE;
}