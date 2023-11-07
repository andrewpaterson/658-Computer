package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.RegisterExpression;

public class Crement
    extends Statement
{
  protected RegisterExpression register;
  protected SixteenHighKeywordCode keyword;

  public Crement(Code code,
                 int index,
                 RegisterExpression register,
                 SixteenHighKeywordCode keyword)
  {
    super(code, index);
    this.register = register;
    this.keyword = keyword;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return register.print(sixteenHighKeywords) + sixteenHighKeywords.getKeyword(keyword) + semicolon();
  }
}

