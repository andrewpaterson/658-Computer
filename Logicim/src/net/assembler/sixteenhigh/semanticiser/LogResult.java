package net.assembler.sixteenhigh.semanticiser;

import net.common.logger.Logger;

public class LogResult
{
  private static LogResult success = new LogResult();

  public String message;
  public String level;

  public LogResult()
  {
  }

  public LogResult(String message)
  {
    this.message = message;
    this.level = Logger.ERROR;
  }

  public LogResult(String level, String message)
  {
    this.message = message;
    this.level = level;
  }

  public boolean isFailure()
  {
    return message != null;
  }

  public static LogResult success()
  {
    return success;
  }

  public boolean is(String level)
  {
    return this.level.equals(level);
  }
}

