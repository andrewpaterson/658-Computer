package net.common.csv;

public class CSVError
{
  public int row;
  public int column;
  public String rowText;
  public String error;

  public CSVError(int row, int column, String rowText, String error)
  {
    this.row = row;
    this.column = column;
    this.rowText = rowText;
    this.error = error;
  }

  public String toString()
  {
    if (column >= 0)
    {
      return row + 1 + ", " + column + ": " + error;
    }
    else
    {
      return row + 1 + ": " + error;
    }
  }
}

