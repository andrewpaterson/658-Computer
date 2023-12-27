package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.statment.expression.BaseTokenExpression;
import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.common.util.StringUtil;

import java.util.List;

public abstract class VariableTokenStatement
    extends TokenStatement
{
  protected String name;
  protected VariableScope scope;
  protected List<Long> arrayMatrix;
  protected int pointerCount;
  protected BaseTokenExpression initialiserExpression;

  public VariableTokenStatement(TokenUnit statements,
                                int index,
                                String name,
                                VariableScope scope,
                                List<Long> arrayMatrix,
                                int pointerCount,
                                BaseTokenExpression initialiserExpression)
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

  public boolean hasInitialiser()
  {
    return initialiserExpression != null;
  }

  public String getName()
  {
    return name;
  }

  public VariableScope getScope()
  {
    return scope;
  }
}

