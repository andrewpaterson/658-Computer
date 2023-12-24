package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;

public class LabelStatement
    extends Statement
{
  protected String name;

  public LabelStatement(Statements statements, int index, String name)
  {
    super(statements, index);
    this.name = name;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return name + ":";
  }

  @Override
  public boolean isLabel()
  {
    return true;
  }
}

