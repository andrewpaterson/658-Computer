package net.common.csv;

import net.common.SimulatorException;

import java.io.IOException;
import java.io.StringReader;

public class StringCharReader
    implements CSVCharReader
{
  private StringReader stringReader;

  public StringCharReader(String string)
  {
    stringReader = new StringReader(string);
  }

  @Override
  public int readUnit()
  {
    try
    {
      return stringReader.read();
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  @Override
  public void close()
  {
    stringReader.close();
  }
}

