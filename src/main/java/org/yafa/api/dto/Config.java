package org.yafa.api.dto;

import java.time.format.DateTimeFormatter;

public class Config {

  /** equivalent to DateTimeFormatter.ISO_OFFSET_DATE_TIME */
  public static final String TIME_STAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSz";

  public static final DateTimeFormatter TIME_STAMP_FORMATTER =
      DateTimeFormatter.ISO_OFFSET_DATE_TIME;
}
