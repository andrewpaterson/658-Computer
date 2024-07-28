package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;

public class RecordTokenStatement
    extends TokenStatement
{
  protected String recordName;

  public RecordTokenStatement(TokenUnit statements, int index, String recordName)
  {
    super(statements, index);
    this.recordName = recordName;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "rec " + recordName;
  }

  @Override
  public boolean isRecord()
  {
    return true;
  }

  public String getName()
  {
    return recordName;
  }
}

