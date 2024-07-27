package net.common.csv;

public class CSVLine
    extends BaseCSVLine
{
  public CSVLine()
  {
    super();
    this.values = new CSVLineValues();
  }

  private CSVLineValues getCSVLineValues()
  {
    return (CSVLineValues) values;
  }

  @Override
  public void append(int character)
  {
    lastEmpty = false;
    getCSVLineValues().appendChar((char) character);
  }

  @Override
  public CSVLineValues cloneValues()
  {
    return getCSVLineValues().cloneValues(lastEmpty ? 1 : 0);
  }
}

