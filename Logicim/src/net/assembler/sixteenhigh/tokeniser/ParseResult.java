package net.assembler.sixteenhigh.tokeniser;

import net.common.parser.Tristate;

import static net.common.parser.Tristate.*;

public class ParseResult
{
  protected Tristate state;

  public ParseResult(Tristate state)
  {
    this.state = state;
  }

  public boolean isFalseOrError()
  {
    return state == FALSE ||
           state == ERROR;
  }

  public boolean isTrueOrError()
  {
    return state == TRUE ||
           state == ERROR;
  }

  public boolean isError()
  {
    return state == ERROR;
  }

  public boolean isFalse()
  {
    return state == FALSE;
  }

  public boolean isTrue()
  {
    return state == TRUE;
  }

  public Tristate getState()
  {
    return state;
  }

  @Override
  public String toString()
  {
    if (state == null)
    {
      return "null";
    }
    else if (state == TRUE)
    {
      return "true";
    }
    else if (state == FALSE)
    {
      return "false";
    }
    else if (state == ERROR)
    {
      return "error";
    }
    else
    {
      return "unknown";
    }
  }
}

