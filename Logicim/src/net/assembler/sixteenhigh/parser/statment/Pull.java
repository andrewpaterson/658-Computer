package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.PullExpression;
import net.assembler.sixteenhigh.parser.statment.expression.RegisterExpression;

public class Pull
    extends Statement
{
  protected RegisterExpression register;
  protected PullExpression pull;

  public Pull(Statements statements, int index, RegisterExpression register, PullExpression pull)
  {
    super(statements, index);
    this.register = register;
    this.pull = pull;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return register.print(sixteenHighKeywords) + pull.print(sixteenHighKeywords) + semicolon();
  }
}

