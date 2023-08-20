package net.common.csv;

import net.common.SimulatorException;
import net.common.util.StringUtil;

import java.io.*;
import java.util.*;

public class CSVFileReader
    implements CSVReader
{
  protected BaseCSVLineReader lineReader;
  protected String filename;

  protected CSVHeader columns;
  protected boolean shouldStripWhitespace;
  protected boolean shouldStripQuotes;

  public CSVFileReader(String filename)
  {
    this(new File(filename), filename);
  }

  public CSVFileReader(File file)
  {
    this(file, file.getName());
  }

  public CSVFileReader(File file, boolean shouldStripWhitespace, boolean shouldStripQuotes)
  {
    this(new CSVLineReader(new UTF8BufferedStreamReader(file), new CSVErrors(), getBestSeparator(file), '"'), file.getName(), shouldStripWhitespace, shouldStripQuotes);
  }

  public CSVFileReader(File file, String filename)
  {
    this(file, filename, getBestSeparator(file), '"');
  }

  public CSVFileReader(File file, char separator, char delimiter)
  {
    this(new CSVLineReader(new UTF8BufferedStreamReader(file), new CSVErrors(), separator, delimiter), file.getName());
  }

  public CSVFileReader(File file, String filename, char separator, char delimiter)
  {
    this(new CSVLineReader(new UTF8BufferedStreamReader(file), new CSVErrors(), separator, delimiter), filename);
  }

  public CSVFileReader(Reader reader, String filename, char separator, char delimiter)
  {
    this(new CSVLineReader(new UTF8BufferedStreamReader(reader), new CSVErrors(), separator, delimiter), filename, true, true);
  }

  public CSVFileReader(BaseCSVLineReader lineReader, String filename)
  {
    this(lineReader, filename, true, true);
  }

  public CSVFileReader(BaseCSVLineReader lineReader, String filename, boolean shouldStripWhitespace, boolean shouldStripQuotes)
  {
    this.filename = filename;
    this.lineReader = lineReader;
    this.shouldStripWhitespace = shouldStripWhitespace;
    this.shouldStripQuotes = shouldStripQuotes;
    initialise();
  }

  protected void initialise()
  {
    columns = readColumns();
    throwErrors();
  }

  private CSVHeader readColumns()
  {
    return new CSVHeader(clean(lineReader.readStringValues(), true, true));
  }

  public void throwErrors()
  {
    if (!lineReader.hasErrors())
    {
      return;
    }

    Set<String> uniqueErrors = transformErrorsIntoUniqueErrors();
    if (uniqueErrors.size() == 1)
    {
      throw new SimulatorException("File [%s] %s", filename, uniqueErrors.iterator().next());
    }
    else
    {
      StringBuilder stringBuffer = new StringBuilder();
      stringBuffer.append("File[");
      stringBuffer.append(filename);
      stringBuffer.append("]\n");
      for (String csvError : uniqueErrors)
      {
        stringBuffer.append("+ ");
        stringBuffer.append(csvError);
        stringBuffer.append("\n");
      }
      throw new SimulatorException(stringBuffer.toString());
    }
  }

  private Set<String> transformErrorsIntoUniqueErrors()
  {
    HashSet<String> result = new HashSet<>();
    for (CSVError error : lineReader.getErrors().getErrors())
    {
      result.add(error.toString());
    }
    return result;
  }

  protected static char getBestSeparator(File file)
  {
    if (!file.exists())
    {
      throw new SimulatorException("File [%s] does not exist.", file.getName());
    }

    long longAmountToRead = file.length();
    int intAmountToRead;
    if (longAmountToRead > 200)
    {
      intAmountToRead = 200;
    }
    else
    {
      intAmountToRead = (int) longAmountToRead;
    }

    char[] chars = new char[intAmountToRead];

    try
    {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      reader.read(chars, 0, intAmountToRead);
      reader.close();
    }
    catch (FileNotFoundException e)
    {
      throw new SimulatorException("File [%s] does not exist.", file.getName());
    }
    catch (IOException e)
    {
      throw new SimulatorException("File [%s] could not be read.", file.getName());
    }

    String string = new String(chars);
    Character separator = getBestSeparator(string);
    if (separator != null)
    {
      return separator;
    }

    if (string.length() > 80)
    {
      string = string.substring(0, 80);
    }
    throw new SimulatorException("File[%s] is not a CSV file.  Contents [%s].", file.getName(), string);
  }

  public static Character getBestSeparator(String string)
  {
    int numberOfPipes = StringUtil.occurrencesOf(string, '|');
    int numberOfSemicolons = StringUtil.occurrencesOf(string, ';');
    int numberOfCommas = StringUtil.occurrencesOf(string, ',');
    int numberOfTabs = StringUtil.occurrencesOf(string, '\t');

    int max = Collections.max(Arrays.asList(numberOfPipes, numberOfCommas, numberOfTabs, numberOfSemicolons));
    if (max == numberOfTabs)
    {
      return '\t';
    }
    if (max == numberOfPipes)
    {
      return '|';
    }
    if (max == numberOfCommas)
    {
      return ',';
    }
    if (max == numberOfSemicolons)
    {
      return ';';
    }
    return null;
  }

  public String toDisplayString()
  {
    return "CSVReader: " + filename;
  }

  public CSVHeader getHeader()
  {
    return columns;
  }

  public CSVErrors getErrors()
  {
    return lineReader.getErrors();
  }

  protected List<String> clean(List<String> strings, boolean stripWhitespace, boolean shouldStripQuotes)
  {
    if (strings != null)
    {
      for (int i = 0; i < strings.size(); i++)
      {
        String s = strings.get(i);

        if (shouldStripQuotes)
        {
          s = StringUtil.stripDoubleQuotes(s);
        }

        if (stripWhitespace)
        {
          s = StringUtil.stripSurroundingWhitespace(s);
        }

        strings.set(i, s);
      }
      return strings;
    }
    else
    {
      return null;
    }
  }

  public static List<CSVRow> readRows(CSVFileReader reader)
  {
    List<CSVRow> rows = new ArrayList<>();

    CSVRow csvRow = reader.readCSVRow();
    while (csvRow != null)
    {
      rows.add(csvRow);
      csvRow = reader.readCSVRow();
    }
    return rows;
  }

  public CSVRow readCSVRow()
  {
    List<String> values = readStringValues();
    if (values != null)
    {
      HashMap<String, String> map = CSVRow.createMap(columns.getHeaders(), values);
      return new CSVRow(lineReader.getRow() - 1, map);
    }
    return null;
  }

  public List<String> readStringValues()
  {
    for (; ; )
    {
      List<String> values = lineReader.readStringValues();
      if (values == null)
      {
        return null;
      }
      else
      {
        values = processRow(lineReader.getRow() - 1, values, columns.getHeaders());
        if (values != null)
        {
          return values;
        }
      }
    }
  }

  public BaseCSVLineValues readCSVLine()
  {
    for (; ; )
    {
      BaseCSVLine line = lineReader.readCSVLine();
      if (line == null)
      {
        return null;
      }
      else
      {
        line = processRow(lineReader.getRow() - 1, line, columns.getHeaders());
        if (line != null)
        {
          return line.cloneValues();
        }
      }
    }
  }

  protected List<String> processRow(int row, List<String> values, List<String> expectedColumns)
  {
    if (values.size() == expectedColumns.size())
    {
      return clean(values, shouldStripWhitespace, shouldStripQuotes);
    }
    else
    {
      if ((values.size() == 1) && (values.get(0).length() == 0))
      {
        lineReader.getErrors().add(row,
                                   0,
                                   CSVUtil.toRowText(values),
                                   "Contains no values");
      }
      else if (values.size() != 0)
      {
        lineReader.getErrors().add(row,
                                   0,
                                   CSVUtil.toRowText(values),
                                   "Number of values doesn't match number of headers");
      }
    }
    return null;
  }

  protected BaseCSVLine processRow(int row, BaseCSVLine line, List<String> expectedColumns)
  {
    int numStrings = line.numStrings();
    int expectedStrings = expectedColumns.size();
    if (numStrings == expectedStrings)
    {
      return line;
    }
    else
    {
      if ((numStrings == 1) && (line.createString(0).length() == 0))
      {
        lineReader.getErrors().add(row,
                                   0,
                                   line.toRowText(),
                                   "Contains no values");
      }
      else if (numStrings != 0)
      {
        lineReader.getErrors().add(row,
                                   0,
                                   line.toRowText(),
                                   String.format("Number of values [%s] doesn't match number of headers [%s]", numStrings, expectedStrings));
      }
      return null;
    }
  }

  public int getRow()
  {
    return lineReader.getRow();
  }

  public void close()
  {
    lineReader.close();
  }

  public int getHeaderIndex(String name)
  {
    List<String> headers = getHeader().getHeaders();
    int size = headers.size();
    for (int i = 0; i < size; i++)
    {
      String headerName = headers.get(i);
      if (headerName.equals(name))
      {
        return i;
      }
    }
    return -1;
  }

  public List<String> getHeaders()
  {
    return columns.getHeaders();
  }

  public static CSVFileReader create(String... lines)
  {
    String fileContents = StringUtil.separateArray(lines, "\n");
    return new CSVFileReader(new CSVLineReader(new StringCharReader(fileContents), new CSVErrors(), getBestSeparator(lines[0]), '"'), "No File");
  }

  public static List<CSVRow> readRows(List<String> lines)
  {
    String fileContents = StringUtil.separateList(lines, "\n");
    CSVFileReader csvFileReader = new CSVFileReader(new CSVLineReader(new StringCharReader(fileContents), new CSVErrors(), getBestSeparator(lines.get(0)), '"'), "No File");
    return CSVFileReader.readRows(csvFileReader);
  }
}

