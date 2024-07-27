package net.common.logger;

import net.assembler.sixteenhigh.semanticiser.LogResult;
import net.common.SimulatorException;

import java.util.ArrayList;
import java.util.List;

public class Logger
{
  public static final String FATAL = "FATAL";
  public static final String ERROR = "ERROR";
  public static final String WARNING = "WARNING";
  public static final String INFO = "INFO";

  public List<String> messages;
  public int errors;
  public int warnings;

  public int maxErrors;

  public Logger()
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

  public boolean log(String filename, int index, LogResult logResult)
  {
    if (logResult.is(FATAL))
    {
      return logFatal(filename, index, logResult.message);
    }
    else if (logResult.is(ERROR))
    {
      return logError(filename, index, logResult.message);
    }
    else if (logResult.is(WARNING))
    {
      logWarning(filename, index, logResult.message);
      return true;
    }
    else if (logResult.is(INFO))
    {
      logInfo(filename, index, logResult.message);
      return true;
    }
    else
    {
      throw new SimulatorException("Unknown log level [%s].", logResult.level);
    }
  }
}

