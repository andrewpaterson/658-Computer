package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class GlobalVariable
    extends Variable
{
  public GlobalVariable(Code code, int index, SixteenHighKeywordCode type, String name)
  {
    super(code, index, type, name);
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "@@" + super.print(sixteenHighKeywords);
  }
}

