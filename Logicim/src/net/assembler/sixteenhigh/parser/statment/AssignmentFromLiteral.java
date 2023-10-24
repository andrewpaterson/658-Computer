package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.literal.CTLiteral;

public class AssignmentFromLiteral
    extends Statement
{
  protected String leftRegister;
  protected CTLiteral rightLiteral;
  protected SixteenHighKeywordCode keyword;

  public AssignmentFromLiteral(Code code,
                               int index,
                               String leftRegister,
                               CTLiteral rightLiteral,
                               SixteenHighKeywordCode keyword)
  {
    super(code, index);
    this.leftRegister = leftRegister;
    this.rightLiteral = rightLiteral;
    this.keyword = keyword;
  }
}

