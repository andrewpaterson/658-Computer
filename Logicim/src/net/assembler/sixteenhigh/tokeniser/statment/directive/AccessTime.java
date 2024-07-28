package net.assembler.sixteenhigh.tokeniser.statment.directive;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;

public class AccessTime
    extends DirectiveTokenStatement
{
  protected int cycles;

  public AccessTime(TokenUnit statements, int index, int cycles)
  {
    super(statements, index);
    this.cycles = cycles;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "$access_time " + cycles + semicolon();
  }

  public int getTime()
  {
    return cycles;
  }
}

