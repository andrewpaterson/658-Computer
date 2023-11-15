package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.statment.expression.BaseExpression;
import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.common.util.StringUtil;

import java.util.List;

public abstract class VariableStatement
    extends Statement
{
  public String name;
  public VariableScope scope;
  public List<Long> arrayMatrix;
  public int pointerCount;
  public BaseExpression initialiserExpression;

  public VariableStatement(Statements statements,
                           int index,
                           String name,
                           VariableScope scope,
                           List<Long> arrayMatrix,
                           int pointerCount,
                           BaseExpression initialiserExpression)
  {
    super(statements, index);
    this.name = name;
    this.scope = scope;
    this.arrayMatrix = arrayMatrix;
    this.pointerCount = pointerCount;
    this.initialiserExpression = initialiserExpression;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder arrays = new StringBuilder();
    for (Long arrayLength : arrayMatrix)
    {
      arrays.append("[");
      arrays.append(arrayLength.longValue());
      arrays.append("]");
    }
    StringBuilder asterisks = StringUtil.pad("*", pointerCount);

    StringBuilder initialiser = new StringBuilder();
    if (initialiserExpression != null)
    {
      if (initialiserExpression.isAssignment())
      {
        initialiser.append(" = ");
      }
      initialiser.append(initialiserExpression.print(sixteenHighKeywords));
    }

    return getIdentifierString(sixteenHighKeywords) + arrays.toString() + asterisks.toString() + " " + name + initialiser.toString() + semicolon();
  }

  protected abstract String getIdentifierString(SixteenHighKeywords sixteenHighKeywords);
}

