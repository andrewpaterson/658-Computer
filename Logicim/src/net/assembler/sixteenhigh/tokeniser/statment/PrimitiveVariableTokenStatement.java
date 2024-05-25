package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.BaseTokenExpression;

import java.util.List;

public class PrimitiveVariableTokenStatement
    extends VariableTokenStatement
{
  protected SixteenHighKeywordCode type;

  public PrimitiveVariableTokenStatement(TokenUnit statements,
                                         int index,
                                         SixteenHighKeywordCode type,
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
    this.type = type;
  }

  protected String getIdentifierString(SixteenHighKeywords sixteenHighKeywords)
  {
    return sixteenHighKeywords.getKeyword(type);
  }

  @Override
  public boolean isPrimitiveVariable()
  {
    return true;
  }

  public SixteenHighKeywordCode getType()
  {
    return type;
  }

  @Override
  public boolean isVariableDefinition()
  {
    return true;
  }
}

