package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.Statements;

public class StructStatement
    extends Statement
{
  public String structName;

  public StructStatement(Statements statements, int index, String structName)
  {
    super(statements, index);
    this.structName = structName;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "struct " + structName + semicolon();
  }

  @Override
  public boolean isStruct()
  {
    return true;
  }
}

