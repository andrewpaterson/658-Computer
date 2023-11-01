package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class Crement
    extends Statement
{
  protected String register;
  protected SixteenHighKeywordCode keyword;

  public Crement(Code code, int index, String register, SixteenHighKeywordCode keyword)
  {
    super(code, index);
    this.register = register;
    this.keyword = keyword;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return register + sixteenHighKeywords.getKeyword(keyword);
  }
}

