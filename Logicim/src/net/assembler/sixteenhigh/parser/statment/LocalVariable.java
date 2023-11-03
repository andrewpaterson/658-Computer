package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class LocalVariable
    extends Variable
{
  public LocalVariable(Code code,
                       int index,
                       SixteenHighKeywordCode type,
                       String name,
                       int asteriskCount)
  {
    super(code,
          index,
          type,
          name,
          asteriskCount);
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "" + super.print(sixteenHighKeywords);
  }
}

