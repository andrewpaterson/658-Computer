package net.assembler.sixteenhigh.parser;

import net.common.parser.Tristate;
import net.logicim.ui.Edit;

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
}

