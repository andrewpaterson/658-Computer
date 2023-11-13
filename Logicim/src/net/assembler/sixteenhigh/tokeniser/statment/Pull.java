package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.statment.expression.PullExpression;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableExpression;

public class Pull
    extends Statement
{
  protected VariableExpression variableExpression;
  protected PullExpression pull;

  public Pull(Statements statements, int index, VariableExpression variableExpression, PullExpression pull)
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

