package net.common.csv;

import net.common.SimulatorException;
import net.common.util.StringUtil;

import java.util.*;

public class CSVHeader
{
  protected List<String> columns;

  public CSVHeader(List<String> columns)
  {
    validateColumns(columns);
    this.columns = columns;
  }

  private void validateColumns(List<String> columns)
  {
    Set<String> existingColumnns = new LinkedHashSet<>();
    Set<String> duplicateColumns = new LinkedHashSet<>();
    for (String column : columns)
    {
      if (!StringUtil.isEmptyOrNull(column))
      {
        if (existingColumnns.contains(column))
        {
          duplicateColumns.add(column);
        }
        existingColumnns.add(column);
      }
    }
    if (duplicateColumns.size() > 0)
    {
      ArrayList<String> strings = new ArrayList<>(duplicateColumns);
      Collections.sort(strings);
      throw new SimulatorException("Duplicate column headings [%s].", StringUtil.commaSeparateList(strings));
    }
  }

  public List<String> getHeaders()
  {
    return columns;
  }
}

