package net.assembler.sixteenhigh.tokeniser.statment.directive;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;

public class AccessTime
    extends DirectiveStatement
{
  protected int cycles;

  public AccessTime(Statements statements, int index, int cycles)
  {
    super(statements, index);
    this.cycles = cycles;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "$access_time " + cycles;
  }

  public int getTime()
  {
    return cycles;
  }
}
