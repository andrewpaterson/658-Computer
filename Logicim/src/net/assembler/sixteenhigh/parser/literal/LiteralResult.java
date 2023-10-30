package net.assembler.sixteenhigh.parser.literal;

import net.common.parser.Tristate;

import static net.common.parser.Tristate.*;

public class LiteralResult
{
  protected Tristate state;
  protected CTLiteral literal;

  public LiteralResult(Tristate state)
  {
    this.state = state;
    this.literal = null;
  }

  public LiteralResult(CTLiteral literal)
  {
    this.state = TRUE;
    this.literal = literal;
  }

  public boolean isTrue()
  {
    return state == TRUE;
  }

  public boolean isFalse()
  {
    return state == FALSE;
  }

  public boolean isError()
  {
    return state == ERROR;
  }

  public CTLiteral getLiteral()
  {
    return literal;
  }

  public CTInt getInt()
  {
    return (CTInt) literal;
  }

  public CTChar getChar()
  {
    return (CTChar) literal;
  }

  public CTShort getShort()
  {
    return (CTShort) literal;
  }

  public CTLong getLong()
  {
    return (CTLong) literal;
  }

  public CTDouble getDouble()
  {
    return (CTDouble) literal;
  }

  public CTFloat getFloat()
  {
    return (CTFloat) literal;
  }

  public CTBoolean getBoolean()
  {
    return (CTBoolean) literal;
  }

  public CTString getString()
  {
    return (CTString) literal;
  }

  public CTIntegerLiteral getIntegerLiteral()
  {
    return (CTIntegerLiteral) literal;
  }

  public boolean isTrueOrError()
  {
    return isTrue() || isError();
  }
}

