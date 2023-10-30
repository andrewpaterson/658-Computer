package net.assembler.sixteenhigh.parser.statment.directive;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.Statement;

public class AccessTime
    extends Statement
{
  protected int cycles;

  public AccessTime(Code code, int index, int cycles)
  {
    super(code, index);
    this.cycles = cycles;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "$access_time " + cycles;
  }
}

