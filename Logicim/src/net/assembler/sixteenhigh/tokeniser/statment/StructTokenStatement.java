package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;

public class StructTokenStatement
    extends TokenStatement
{
  public String structName;

  public StructTokenStatement(TokenUnit statements, int index, String structName)
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

  public String getName()
  {
    return structName;
  }
}

