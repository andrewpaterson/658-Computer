package net.assembler.sixteenhigh.tokeniser.statment.expression;

public class BaseTokenExpressionPointer
{
  public BaseTokenExpression expression;

  public BaseTokenExpressionPointer()
  {
    expression = null;
  }

  public void setExpression(BaseTokenExpression expression)
  {
    this.expression = expression;
  }
}

