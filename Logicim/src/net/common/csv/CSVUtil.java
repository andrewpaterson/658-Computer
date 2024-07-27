package net.common.csv;

import java.util.Collection;

public class CSVUtil
{
  public static String toRowText(Collection<String> values)
  {
    StringBuilder buffer = new StringBuilder();

    for (String value : values)
    {
      buffer.append(value);
      buffer.append(", ");
    }

    buffer.delete(buffer.length() - 2, buffer.length());

    return buffer.toString();
  }
}

