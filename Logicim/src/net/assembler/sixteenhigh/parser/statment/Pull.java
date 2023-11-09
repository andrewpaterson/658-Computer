package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.PullExpression;
import net.assembler.sixteenhigh.parser.statment.expression.RegisterExpression;

public class Pull
    extends Statement
{
  protected RegisterExpression register;
  protected PullExpression pull;

  public Pull(Code code, int index, RegisterExpression register, PullExpression pull)
  {
    super(code, index);
    this.register = register;
    this.pull = pull;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return register.print(sixteenHighKeywords) + pull.print(sixteenHighKeywords) + semicolon();
  }
}

