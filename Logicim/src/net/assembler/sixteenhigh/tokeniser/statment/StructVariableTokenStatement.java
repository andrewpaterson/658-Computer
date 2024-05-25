package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.tokeniser.statment.expression.BaseTokenExpression;

import java.util.List;

public class StructVariableTokenStatement
    extends VariableTokenStatement
{
  protected String structIdentifier;

  public StructVariableTokenStatement(TokenUnit statements,
                                      int index,
                                      String structIdentifier,
                                      String name,
                                      Scope scope,
                                      List<Long> arrayMatrix,
                                      int pointerCount,
                                      BaseTokenExpression initialiserExpression)
  {
    super(statements,
          index,
          name,
          scope,
          arrayMatrix,
          pointerCount,
          initialiserExpression);
    this.structIdentifier = structIdentifier;
  }

  @Override
  protected String getIdentifierString(SixteenHighKeywords sixteenHighKeywords)
  {
    return structIdentifier;
  }

  @Override
  public boolean isStructVariable()
  {
    return true;
  }

  @Override
  public boolean isVariableDefinition()
  {
    return true;
  }
}

