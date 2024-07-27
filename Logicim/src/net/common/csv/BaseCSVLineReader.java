package net.common.csv;

import net.common.SimulatorException;

import java.util.List;

import static net.common.csv.CSVParserState.*;

public abstract class BaseCSVLineReader
{
  public static int line_feed = '\n';
  public static int carriage_return = '\r';
  public static int END_OF_FILE = -1;

  protected BaseCSVReader baseCSVReader;
  protected CSVErrors errors;
  protected int separator;
  protected int delimiter;
  protected int row;
  protected boolean afterEndOfFile;
  protected boolean lineError;

  protected BaseCSVLine line;

  public BaseCSVLineReader(BaseCSVReader baseCSVReader, CSVErrors errors, int separator, int delimiter)
  {
    this.baseCSVReader = baseCSVReader;
    this.errors = errors;
    this.separator = separator;
    this.delimiter = delimiter;
    this.row = 0;
    this.afterEndOfFile = false;
    this.lineError = false;
    this.line = createLine();
  }

  @SuppressWarnings("Duplicates")
  public List<String> readStringValues()
  {
    BaseCSVLine baseCSVLine = readCSVLine();
    if (baseCSVLine == null)
    {
      return null;
    }

    return baseCSVLine.toStringArray();
  }

  @SuppressWarnings("Duplicates")
  public BaseCSVLine readCSVLine()
  {
    if (afterEndOfFile)
    {
      return null;
    }

    line.reset();
    lineError = false;

    CSVParserState state = initial_entry_state;
    int column = -1;

    for (; ; )
    {
      int unit = baseCSVReader.readUnit();
      if (unit == carriage_return)
      {
        continue;
      }

      column++;

      switch (state)
      {
        case unquoted_value_state:
          if (unit == separator)
          {
            line.mark();
            state = initial_entry_state;
            break;
          }
          else if (unit == line_feed)
          {
            state = end_of_line_state;
            break;
          }
          else if (unit == END_OF_FILE)
          {
            state = end_of_file_state;
            break;
          }
          else
          {
            line.append(unit);
            continue;
          }

        case one_quote_state:
          state = processOneQuoteState(unit, line, column);
          break;

        case initial_entry_state:
          state = processInitialEntryState(unit, line);
          break;

        case two_quotes_state:
          state = processTwoQuotesState(unit, line, column);
          break;

        default:
          throw new SimulatorException("Impossible State [" + state + "]");
      }

      if (state == end_of_line_state)
      {
        return endOfLine(line);
      }
      else if (state == end_of_file_state)
      {
        afterEndOfFile = true;
        return endOfLine(line);
      }
    }
  }

  private CSVParserState processTwoQuotesState(int unit, BaseCSVLine line, int column)
  {
    if (unit == delimiter)
    {
      line.append(delimiter);
      return one_quote_state;
    }
    else if (unit == separator)
    {
      line.mark();
      return initial_entry_state;
    }
    else if (unit == line_feed)
    {
      return end_of_line_state;
    }
    else if (unit == END_OF_FILE)
    {
      return end_of_file_state;
    }
    else if (unit == ' ')
    {
      addError(column,
               row,
               line.toRowText(),
               "Expected " + (char) separator + " after double quote.");
      return one_quote_state;
    }
    else
    {
      line.append(delimiter);
      line.append(unit);
      addError(column,
               row,
               line.toRowText(),
               "Unexpected double quote found in quoted value.");
      return one_quote_state;
    }
  }

  private void addError(int column, int row, String rowText, String error)
  {
    lineError = true;
    errors.add(row, column, rowText, error);
  }

  private CSVParserState processOneQuoteState(int unit, BaseCSVLine line, int column)
  {
    if (unit == delimiter)
    {
      return two_quotes_state;
    }
    else if (unit == END_OF_FILE)
    {
      addError(column,
               row,
               line.toRowText(),
               "No closing quote found before end of file.");
      return end_of_file_state;
    }
    else if (unit == line_feed)
    {
      if (!lineError)
      {
        line.appendLineFeed();
        return one_quote_state;
      }
      else
      {
        return end_of_line_state;
      }
    }
    else
    {
      line.append(unit);
      return one_quote_state;
    }
  }

  protected CSVParserState processInitialEntryState(int unit, BaseCSVLine line)
  {
    if (unit == delimiter)
    {
      return one_quote_state;
    }
    else if (unit == line_feed)
    {
      return end_of_line_state;
    }
    else if (unit == END_OF_FILE)
    {
      return end_of_file_state;
    }
    else if (unit == separator)
    {
      line.mark();
      return initial_entry_state;
    }
    else
    {
      line.append(unit);
      return unquoted_value_state;
    }
  }

  public int getRow()
  {
    return row;
  }

  public BaseCSVLine endOfLine(BaseCSVLine line)
  {
    if (line.numStrings() == 0 && line.isLastEmpty())
    {
      return line;
    }

    line.mark();
    row++;
    return line;
  }

  public CSVErrors getErrors()
  {
    return errors;
  }

  public boolean hasErrors()
  {
    return errors.size() > 0;
  }

  public void close()
  {
    baseCSVReader.close();
  }

  protected abstract BaseCSVLine createLine();
}
