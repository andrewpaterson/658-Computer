package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.statment.expression.PullTokenExpression;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableTokenExpression;

public class PullTokenStatement
    extends TokenStatement
{
  protected VariableTokenExpression variableExpression;
  protected PullTokenExpression pull;

  public PullTokenStatement(TokenUnit statements, int index, VariableTokenExpression variableExpression, PullTokenExpression pull)
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

