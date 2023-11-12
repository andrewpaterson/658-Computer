package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.statment.expression.BaseExpression;
import net.assembler.sixteenhigh.parser.statment.scope.VariableScope;

import java.util.List;

public class PrimitiveVariable
    extends Variable
{
  public SixteenHighKeywordCode type;

  public PrimitiveVariable(Statements statements,
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

