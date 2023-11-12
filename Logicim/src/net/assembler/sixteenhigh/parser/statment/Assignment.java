package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.Expression;
import net.assembler.sixteenhigh.parser.statment.expression.RegisterExpression;

public class Assignment
    extends Statement
{
  protected RegisterExpression leftRegister;
  protected SixteenHighKeywordCode keyword;
  protected Expression right;

  public Assignment(Statements statements,
                    int index,
                    RegisterExpression leftRegister,
                    SixteenHighKeywordCode keyword,
                    Expression right)
  {
    super(statements, index);
    this.leftRegister = leftRegister;
    this.keyword = keyword;
    this.right = right;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return leftRegister.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(keyword) + " " + right.print(sixteenHighKeywords) + semicolon();
  }
}

