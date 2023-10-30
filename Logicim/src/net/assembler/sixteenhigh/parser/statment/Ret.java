package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class Ret
    extends Statement
{
  public Ret(Code code, int index)
  {
    super(code, index);
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "return";
  }
}

