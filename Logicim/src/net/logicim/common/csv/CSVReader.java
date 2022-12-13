package net.logicim.common.csv;

public interface CSVReader
{
  CSVHeader getHeader();

  CSVRow readCSVRow();
}

