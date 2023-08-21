package net.assembler.sixteenhigh.parser;

import java.util.ArrayList;
import java.util.List;

public class TextParserLog
{
  protected static final String FATAL = "FATAL";
  protected static final String ERROR = "ERROR";
  protected static final String WARNING = "WARNING";
  protected static final String INFO = "INFO";

  public List<String> messages;
  public int errors;
  public int warnings;

  public int maxErrors;

  public TextParserLog()
  {
    messages = new ArrayList<>();
    errors = 0;
    warnings = 0;
    maxErrors = 10;
  }

  public boolean logFatal(String filename, int lineNumber, String message, Object... p)
  {
    logMessage(FATAL, filename, lineNumber, message, p);
    return false;
  }

  public boolean logError(String filename, int lineNumber, String message, Object... p)
  {
    logMessage(ERROR, filename, lineNumber, message, p);
    errors++;
    if (errors >= maxErrors)
    {
      return false;
    }
    return true;
  }

  public void logWarning(String filename, int lineNumber, String message, Object... p)
  {
    logMessage(WARNING, filename, lineNumber, message, p);
    warnings++;
  }

  public void logInfo(String filename, int lineNumber, String message, Object... p)
  {
    logMessage(INFO, filename, lineNumber, message, p);
  }

  protected void logMessage(String level, String filename, int lineNumber, String message, Object... p)
  {
    String formattedMessage = String.format(message, p);
    String line = String.format("%s %s [%s]: %s", level, filename, lineNumber, formattedMessage);
    messages.add(line);
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    for (String message : messages)
    {
      builder.append(message);
      builder.append("\n");
    }
    return builder.toString();
  }
}

