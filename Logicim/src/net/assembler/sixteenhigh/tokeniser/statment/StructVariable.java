package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.statment.expression.BaseExpression;
import net.assembler.sixteenhigh.tokeniser.statment.scope.VariableScope;

import java.util.List;

public class StructVariable
    extends Variable
{
  public String structIdentifier;

  public StructVariable(Statements statements,
                        int index,
                        String structIdentifier,
                        String name,
                        VariableScope scope,
                        List<Long> arrayMatrix,
                        int pointerCount,
                        BaseExpression initialiserExpression)
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
}

