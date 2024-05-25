package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.semanticiser.expression.operator.OperatorCode;
import net.assembler.sixteenhigh.semanticiser.expression.operator.SixteenHighOperatorMap;

public class Triple
{
  protected TripleValue left;
  protected TripleValue rightOne;
  protected OperatorCode operator;
  protected TripleValue rightTwo;

  public Triple()
  {
  }

  public Triple(TripleValue left,
                OperatorCode operator,
                TripleValue right)
  {
    this.left = left;
    this.rightOne = null;
    this.operator = operator;
    this.rightTwo = right;
  }

  public Triple(TripleValue left,
                TripleValue rightOne,
                OperatorCode operator,
                TripleValue rightTwo)
  {
    this.left = left;
    this.rightOne = rightOne;
    this.operator = operator;
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

  public String print()
  {
    if (rightOne != null)
    {
      return left.print() + " = " + rightOne.print() + " " + SixteenHighOperatorMap.getInstance().get(operator) + " " + rightTwo.print();
    }
    else
    {
      return left.print() + " = " + SixteenHighOperatorMap.getInstance().get(operator) + rightTwo.print();
    }
  }

  @Override
  public String toString()
  {
    return print();
  }
}

