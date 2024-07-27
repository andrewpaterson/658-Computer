package net.common.csv;

public interface CSVCharReader
    extends BaseCSVReader
{
  int readUnit();

  void close();
}

