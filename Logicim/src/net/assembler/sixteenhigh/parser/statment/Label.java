package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class Label
    extends Statement
{
  protected String name;

  public Label(Statements statements, int index, String name)
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

