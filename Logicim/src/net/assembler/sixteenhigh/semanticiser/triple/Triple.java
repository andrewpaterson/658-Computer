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

  public String printValidationCode(int index)
  {
    StringBuilder builder = new StringBuilder("validateTriple");
    boolean isRightOneVariable = rightOne == null || rightOne.getVariable() != null;
    boolean isRightTwoVariable = rightTwo == null || rightTwo.getVariable() != null;
    if (isRightOneVariable)
    {
      builder.append("Variable");
    }
    else
    {
      builder.append("Literal");
    }
    if (isRightTwoVariable)
    {
      builder.append("Variable");
    }
    else
    {
      builder.append("Literal");
    }

    builder.append("(triples.get(");
    builder.append(index);
    builder.append("), ");

    TripleVariable leftVariable = left.getVariable();
    builder.append(leftVariable.getVariableDefinition().getType().getType().toString());
    builder.append(", ");
    builder.append("\"");
    builder.append(leftVariable.getName());
    builder.append("\", ");

    if (rightOne == null)
    {
      builder.append("null, null, ");
    }
    else if (isRightOneVariable)
    {
      TripleVariable rightOneVariable = rightOne.getVariable();
      builder.append(rightOneVariable.getVariableDefinition().getType().getType().toString());
      builder.append(", ");
      builder.append("\"");
      builder.append(rightOneVariable.getName());
      builder.append("\", ");
    }
    else
    {
      TripleLiteral rightOneLiteral = rightOne.getLiteral();
      builder.append(rightOneLiteral.getCTLiteral().getPrimitiveTypeCode().toString());
      builder.append(", ");
      builder.append("\"");
      builder.append(rightOneLiteral.print());
      builder.append("\", ");
    }    
    
    if (isRightTwoVariable)
    {
      TripleVariable rightTwoVariable = rightTwo.getVariable();
      builder.append(rightTwoVariable.getVariableDefinition().getType().getType().toString());
      builder.append(", ");
      builder.append("\"");
      builder.append(rightTwoVariable.getName());
      builder.append("\"");
    }
    else
    {
      TripleLiteral rightTwoLiteral = rightTwo.getLiteral();
      builder.append(rightTwoLiteral.getCTLiteral().getPrimitiveTypeCode().toString());
      builder.append(", ");
      builder.append("\"");
      builder.append(rightTwoLiteral.print());
      builder.append("\"");
    }
    builder.append(");");

    //validateTripleVariableLiteral(triples.get(0), int8, "@a", int8, "-2", multiply, int8, "3");
    return builder.toString();
  }
}

