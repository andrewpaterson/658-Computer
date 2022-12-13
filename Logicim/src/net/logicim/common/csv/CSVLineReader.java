package net.logicim.common.csv;

public class CSVLineReader
    extends BaseCSVLineReader
{
  public CSVLineReader(CSVCharReader charReader, CSVErrors errors, int separator, int delimiter)
  {
    super(charReader, errors, separator, delimiter);
  }

  @Override
  protected BaseCSVLine createLine()
  {
    return new CSVLine();
  }
}

