package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.RegisterExpression;

public class Push
    extends Statement
{
  protected RegisterExpression register;

  public Push(Code code, int index, RegisterExpression register)
  {
    super(code, index);
    this.register = register;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "> " + register.print(sixteenHighKeywords) + semicolon();
  }
}

