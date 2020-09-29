package org.yafa.annotations;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ParamConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateParameterConverter implements ParamConverter<ZonedDateTime> {

  public static final String DEFAULT_FORMAT = DateTimeFormat.DEFAULT_DATE_TIME;
  public static final DateTimeFormatter DEFAULT_FORMATTER =
      DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
  private DateTimeFormat customDateTimeFormat;
  private DateTimeFormatter customDateFormatter;

  public void setCustomDateTimeFormat(DateTimeFormat customDateTimeFormat) {
    this.customDateTimeFormat = customDateTimeFormat;
    this.customDateFormatter = DateTimeFormatter.ofPattern(customDateTimeFormat.value());
    this.customDateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
  }

  @Override
  public ZonedDateTime fromString(String string) {
    DateTimeFormatter formatter = DEFAULT_FORMATTER;
    if (customDateFormatter != null) {
      formatter = customDateFormatter;
    }
    try {
      return ZonedDateTime.from(formatter.parse(string));
    } catch (DateTimeParseException ex) {
      log.error("error parsing datetime: {}", string);
      throw new ClientErrorException(
          String.format("error parsing datetime: %s", string), Status.fromStatusCode(422));
    }
  }

  @Override
  public String toString(ZonedDateTime timestamp) {
    DateTimeFormatter formatter = DEFAULT_FORMATTER;
    if (customDateFormatter != null) {
      formatter = customDateFormatter;
    }
    try {
      return timestamp.format(formatter);
    } catch (DateTimeParseException ex) {
      throw new WebApplicationException(ex);
    }
  }
}
