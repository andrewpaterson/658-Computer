package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;

public class NumberCompare
    extends Statement
{
  protected String leftRegister;
  protected String rightRegister;
  protected SixteenHighKeywordCode keyword;

  public NumberCompare(Code code, int index, String leftRegister, String rightRegister, SixteenHighKeywordCode keyword)
  {
    super(code, index);
    this.leftRegister = leftRegister;
    this.rightRegister = rightRegister;
    this.keyword = keyword;
  }
}

