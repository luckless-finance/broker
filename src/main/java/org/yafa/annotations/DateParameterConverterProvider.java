package org.yafa.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class DateParameterConverterProvider implements ParamConverterProvider {

  @SuppressWarnings("unchecked")
  @Override
  public <T> ParamConverter<T> getConverter(
      final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
    if (ZonedDateTime.class.equals(rawType)) {
      final DateParameterConverter dateParameterConverter = new DateParameterConverter();

      for (Annotation annotation : annotations) {
        if (DateTimeFormat.class.equals(annotation.annotationType())) {
          dateParameterConverter.setCustomDateTimeFormat((DateTimeFormat) annotation);
        }
      }
      return (ParamConverter<T>) dateParameterConverter;
    }
    return null;
  }
}
