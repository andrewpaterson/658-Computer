package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.statment.expression.BaseExpression;

import java.util.List;

public class LocalVariable
    extends Variable
{
  public LocalVariable(Code code,
                       int index,
                       SixteenHighKeywordCode type,
                       String name,
                       List<Long> arrayMatrix,
                       int asteriskCount,
                       BaseExpression initialiserExpression)
  {
    super(code,
          index,
          type,
          name,
          arrayMatrix,
          asteriskCount,
          initialiserExpression);
  }
}

