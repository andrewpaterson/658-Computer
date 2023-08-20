package net.common.csv;

import net.common.SimulatorException;

import java.io.*;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

public abstract class BufferedStreamReader
    implements CSVCharReader
{
  private Reader reader;

  public BufferedStreamReader(File file, int capacity, Charset charset)
  {
    try
    {
      CharsetDecoder decoder = charset.newDecoder();
      decoder.onMalformedInput(CodingErrorAction.REPORT);
      decoder.onUnmappableCharacter(CodingErrorAction.REPORT);

      reader = new InputStreamReader(new BufferedInputStream(new FileInputStream(file), capacity), decoder);
    }
    catch (FileNotFoundException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public BufferedStreamReader(Reader reader)
  {
    this.reader = reader;
  }

  @Override
  public int readUnit()
  {
    try
    {
      return reader.read();
    }
    catch (CharacterCodingException e)
    {
      if (reader instanceof InputStreamReader)
      {
        throw new SimulatorException("Improper character encoding - expected encoding to be [%s]. Detail [%s][%s].",
                                     ((InputStreamReader) reader).getEncoding(),
                                     e.getClass().getName(),
                                     e.getMessage());
      }
      else
      {
        throw new SimulatorException(e.getMessage());
      }
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  @Override
  public void close()
  {
    try
    {
      reader.close();
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }
}

