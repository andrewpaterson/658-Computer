package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.tokeniser.statment.expression.BaseTokenExpression;

import java.util.List;

public class RecordVariableTokenStatement
    extends VariableTokenStatement
{
  protected String recordIdentifier;

  public RecordVariableTokenStatement(TokenUnit statements,
                                      int index,
                                      String recordIdentifier,
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
    this.recordIdentifier = recordIdentifier;
  }

  @Override
  protected String getIdentifierString(SixteenHighKeywords sixteenHighKeywords)
  {
    return recordIdentifier;
  }

  @Override
  public boolean isRecordVariable()
  {
    return true;
  }

  @Override
  public boolean isVariableDefinition()
  {
    return true;
  }
}

