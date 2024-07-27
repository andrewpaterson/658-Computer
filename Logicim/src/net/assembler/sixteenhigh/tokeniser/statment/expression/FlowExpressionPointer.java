package net.assembler.sixteenhigh.tokeniser.statment.expression;

public class FlowExpressionPointer
{
  public FlowTokenExpression flowExpression;

  public FlowExpressionPointer()
  {
    flowExpression = null;
  }

  public void setFlowExpression(FlowTokenExpression flowExpression)
  {
    this.flowExpression = flowExpression;
  }
}

