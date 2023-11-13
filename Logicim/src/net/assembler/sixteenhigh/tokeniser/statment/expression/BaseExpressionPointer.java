package net.assembler.sixteenhigh.tokeniser.statment.expression;

public class BaseExpressionPointer
{
  public BaseExpression expression;

  public BaseExpressionPointer()
  {
    expression = null;
  }

  public void setExpression(BaseExpression expression)
  {
    this.expression = expression;
  }
}

