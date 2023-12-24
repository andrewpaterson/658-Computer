package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.statment.expression.PullExpression;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableExpression;

public class PullTokenStatement
    extends TokenStatement
{
  protected VariableExpression variableExpression;
  protected PullExpression pull;

  public PullTokenStatement(TokenUnit statements, int index, VariableExpression variableExpression, PullExpression pull)
  {
    super(statements, index);
    this.variableExpression = variableExpression;
    this.pull = pull;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return variableExpression.print(sixteenHighKeywords) + pull.print(sixteenHighKeywords) + semicolon();
  }
}

