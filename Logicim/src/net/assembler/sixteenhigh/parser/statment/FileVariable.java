package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.statment.expression.BaseExpression;

import java.util.List;

public class FileVariable
    extends Variable
{
  public FileVariable(Code code,
                      int index,
                      SixteenHighKeywordCode type,
                      String name,
                      List<Long> arrayMatrix,
                      int pointerCount,
                      BaseExpression initialiserExpression)
  {
    super(code,
          index,
          type,
          name,
          arrayMatrix,
          pointerCount,
          initialiserExpression);
  }
}

