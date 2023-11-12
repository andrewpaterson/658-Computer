package net.assembler.sixteenhigh.parser.statment.directive;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.Statement;

public class AccessTime
    extends Statement
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
}

