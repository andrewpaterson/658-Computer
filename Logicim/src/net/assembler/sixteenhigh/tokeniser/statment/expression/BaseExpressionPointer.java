package net.assembler.sixteenhigh.tokeniser.statment.expression;

public class BaseExpressionPointer
{
  public BaseTokenExpression expression;

  public BaseExpressionPointer()
  {
    expression = null;
  }

  public void setExpression(BaseTokenExpression expression)
  {
    this.expression = expression;
  }
}

