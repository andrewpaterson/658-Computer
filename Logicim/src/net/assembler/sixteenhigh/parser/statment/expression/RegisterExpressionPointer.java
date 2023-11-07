package net.assembler.sixteenhigh.parser.statment.expression;

public class RegisterExpressionPointer
{
  public RegisterExpression registerExpression;

  public RegisterExpressionPointer()
  {
    this.registerExpression = null;
  }

  public void setRegisterExpression(RegisterExpression registerExpression)
  {
    this.registerExpression = registerExpression;
  }
}
