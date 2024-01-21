package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.semanticiser.expression.operator.OperatorCode;

public class Triple
{
  protected TripleValue left;
  protected TripleValue right1;
  protected OperatorCode operator;
  protected TripleValue right2;

  public Triple()
  {
  }

  public void setLeft(TripleValue left)
  {
    this.left = left;
  }

  public void setRight1(TripleValue right1)
  {
    this.right1 = right1;
  }

  public void setOperator(OperatorCode operator)
  {
    this.operator = operator;
  }

  public void setRight2(TripleValue right2)
  {
    this.right2 = right2;
  }

  public TripleValue getLeft()
  {
    return left;
  }

  public TripleValue getRight1()
  {
    return right1;
  }

  public OperatorCode getOperator()
  {
    return operator;
  }

  public TripleValue getRight2()
  {
    return right2;
  }
}

