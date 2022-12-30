package net.logicim.common.csv;

import java.util.*;

public class CSVRow
{
  private Map<String, String> values;
  private int row;

  public CSVRow(int row, List<String> columns, List<String> values)
  {
    this.values = createMap(columns, values);
    this.row = row;
  }

  public CSVRow(int row, Map<String, String> values)
  {
    this.values = values;
    this.row = row;
  }

  public static HashMap<String, String> createMap(List<String> columns, List<String> values)
  {
    HashMap<String, String> map = new LinkedHashMap<>();

    for (int i = 0; i < columns.size(); i++)
    {
      map.put(columns.get(i), values.get(i));
    }

    return map;
  }

  public String toDisplayString()
  {
    return getClass().getSimpleName();
  }

  public Collection<String> getColumns()
  {
    return values.keySet();
  }

  public String getValue(String column)
  {
    return values.get(column);
  }

  public String getValueOrEmptyStringIfNull(String column)
  {
    String s = values.get(column);
    if (s == null)
    {
      return "";
    }
    return s;
  }

  public String getValueOrNullIfEmptyString(String column)
  {
    String s = values.get(column);
    if (s == null)
    {
      return null;
    }
    if (s.trim().isEmpty())
    {
      return null;
    }
    return s;
  }

  public Map<String, String> getValues()
  {
    return values;
  }

  public int getRow()
  {
    return row;
  }

  public String toRowText()
  {
    return CSVUtil.toRowText(values.values());
  }

  @Override
  public String toString()
  {
    return toRowText();
  }

  public void remove(String columnName)
  {
    values.remove(columnName);
  }
}

