package org.yafa.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.yafa.api.dto.Config;

/**
 * @see <a
 *     href="https://zenidas.wordpress.com/recipes/configurable-date-format-in-jax-rs-as-queryparam/">tutorial
 *     on custom JAX RS QueryParam</a>
 */
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
public @interface DateTimeFormat {

  String DEFAULT_DATE_TIME = Config.TIME_STAMP_PATTERN;

  String value() default DEFAULT_DATE_TIME;
}
