package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.semanticiser.expression.operator.OperatorCode;

public class Triple
{
  protected TripleValue left;
  protected TripleValue rightOne;
  protected OperatorCode operator;
  protected TripleValue rightTwo;

  public Triple()
  {
  }

  public void setLeft(TripleValue left)
  {
    this.left = left;
  }

  public void setRightOne(TripleValue right1)
  {
    this.rightOne = right1;
  }

  public void setOperator(OperatorCode operator)
  {
    this.operator = operator;
  }

  public void setRightTwo(TripleValue rightTwo)
  {
    this.rightTwo = rightTwo;
  }

  public TripleValue getLeft()
  {
    return left;
  }

  public TripleValue getRightOne()
  {
    return rightOne;
  }

  public OperatorCode getOperator()
  {
    return operator;
  }

  public TripleValue getRightTwo()
  {
    return rightTwo;
  }
}

