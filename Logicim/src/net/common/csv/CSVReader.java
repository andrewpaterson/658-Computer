package net.common.csv;

public interface CSVReader
{
  CSVHeader getHeader();

  CSVRow readCSVRow();
}

