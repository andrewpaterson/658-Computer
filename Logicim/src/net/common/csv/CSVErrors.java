package net.common.csv;

import java.util.ArrayList;
import java.util.List;

public class CSVErrors
{
  protected List<CSVError> errors;

  public CSVErrors()
  {
    this.errors = new ArrayList<>();
  }

  public int size()
  {
    return errors.size();
  }

  public List<CSVError> getErrors()
  {
    return errors;
  }

  public void add(int row, int column, String rowText, String error)
  {
    errors.add(new CSVError(row, column, rowText, error));
  }

  public void clear()
  {
    errors.clear();
  }

  public boolean hasErrors()
  {
    return errors.size() > 0;
  }

  @Override
  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    for (CSVError error : errors)
    {
      stringBuilder.append(error.toString());
      stringBuilder.append("\n");
    }

    if (stringBuilder.length() > 1)
    {
      stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
    }
    return stringBuilder.toString();
  }

  public CSVError getError(int i)
  {
    return errors.get(i);
  }
}

