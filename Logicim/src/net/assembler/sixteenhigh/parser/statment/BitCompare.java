package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;

public class BitCompare
    extends Statement
{
  protected String register;
  protected SixteenHighKeywordCode keyword;

  public BitCompare(Code code, int index, String register, SixteenHighKeywordCode keyword)
  {
    super(code, index);
    this.register = register;
    this.keyword = keyword;
  }
}

