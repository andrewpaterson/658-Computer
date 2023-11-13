package net.assembler.sixteenhigh.tokeniser;

import net.assembler.sixteenhigh.common.Statements;

import java.util.ArrayList;
import java.util.List;

public class SixteenHighContext
{
  protected Globals globals;
  protected List<Statements> statementsList;

  public SixteenHighContext()
  {
    globals = new Globals();
    statementsList = new ArrayList<>();
  }

  public Statements addCode(String filename)
  {
    Statements statements = new Statements(filename);
    statementsList.add(statements);
    return statements;
  }
}

