package net.common.csv;

import java.io.File;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class UTF8BufferedStreamReader
    extends BufferedStreamReader
{
  public UTF8BufferedStreamReader(String fileName)
  {
    this(new File(fileName));
  }

  public UTF8BufferedStreamReader(File file)
  {
    this(file, 8192);
  }

  public UTF8BufferedStreamReader(String fileName, int capacity)
  {
    this(new File(fileName), capacity);
  }

  public UTF8BufferedStreamReader(File file, int capacity)
  {
    super(file, capacity, StandardCharsets.UTF_8);
  }

  public UTF8BufferedStreamReader(Reader reader)
  {
    super(reader);
  }
}

