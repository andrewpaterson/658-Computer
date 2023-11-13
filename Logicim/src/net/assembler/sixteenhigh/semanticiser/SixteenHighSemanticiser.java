package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.parser.statment.Statement;

import java.util.ArrayList;
import java.util.List;

public class SixteenHighSemanticiser
{
  private List<Statements> statementsList;

  public SixteenHighSemanticiser(Statements statements)
  {
    statementsList = new ArrayList<>();
    statementsList.add(statements);
  }

  public SixteenHighSemanticiser(List<Statements> statementsList)
  {
    this.statementsList = statementsList;
  }

  public void parse()
  {
    for (Statements statements : statementsList)
    {
      parseStatements(statements);
    }
  }

  private void parseStatements(Statements statements)
  {
    parseStatements(statements.getStatements(), statements.getFilename());
  }

  private void parseStatements(List<Statement> statements, String filename)
  {
    for (Statement statement : statements)
    {
      statement.isDirective();
    }
  }
}

