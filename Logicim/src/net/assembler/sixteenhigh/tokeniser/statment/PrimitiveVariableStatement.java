package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.BaseExpression;
import net.assembler.sixteenhigh.common.scope.VariableScope;

import java.util.List;

public class PrimitiveVariableStatement
    extends VariableStatement
{
  public SixteenHighKeywordCode type;

  public PrimitiveVariableStatement(Statements statements,
                                    int index,
                                    SixteenHighKeywordCode type,
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
    this.type = type;
  }

  protected String getIdentifierString(SixteenHighKeywords sixteenHighKeywords)
  {
    return sixteenHighKeywords.getKeyword(type);
  }
}

