package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.Expression;

public class Assignment
    extends Statement
{
  protected String leftRegister;
  protected SixteenHighKeywordCode keyword;
  protected Expression expression;

  public Assignment(Code code,
                    int index,
                    String leftRegister,
                    SixteenHighKeywordCode keyword,
                    Expression expression)
  {
    super(code, index);
    this.leftRegister = leftRegister;
    this.keyword = keyword;
    this.expression = expression;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return leftRegister + " " + sixteenHighKeywords.getKeyword(keyword);
  }
}

