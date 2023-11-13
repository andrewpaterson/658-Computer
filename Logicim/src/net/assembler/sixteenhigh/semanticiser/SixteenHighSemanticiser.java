package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.semanticiser.directive.DirectiveBlock;
import net.assembler.sixteenhigh.tokeniser.statment.Statement;
import net.assembler.sixteenhigh.tokeniser.statment.directive.DirectiveStatement;
import net.common.parser.StringZero;

import java.util.ArrayList;
import java.util.List;

public class SixteenHighSemanticiser
{
  protected List<Statements> statementsList;
  protected SixteenHighKeywords keywords;

  public SixteenHighSemanticiser(Statements statements, SixteenHighKeywords keywords)
  {
    this.keywords = keywords;
    this.statementsList = new ArrayList<>();
    this.statementsList.add(statements);
  }

  public SixteenHighSemanticiser(List<Statements> statementsList, SixteenHighKeywords keywords)
  {
    this.keywords = keywords;
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
    SixteenHighSemanticiserContext context = new SixteenHighSemanticiserContext(filename);

    boolean previousDirectiveStatement = true;
    context.setCurrentDirectiveBlock(new DirectiveBlock());
    StringZero errorZero = new StringZero();
    for (Statement statement : statements)
    {
      if (statement.isDirective())
      {
        if (previousDirectiveStatement)
        {
          context.getCurrentDirectiveBlock().set((DirectiveStatement) statement, errorZero, keywords);
        }
        else
        {
          context.setCurrentDirectiveBlock(new DirectiveBlock(context.getCurrentDirectiveBlock()));
        }
      }
      else
      {
        parseNonDirectiveStatements(statement, context);
        previousDirectiveStatement = true;
      }
    }
  }

  private void parseNonDirectiveStatements(Statement statement, SixteenHighSemanticiserContext context)
  {
    if (statement.isRoutine())
    {
      context.setCurrentRoutine(new Routine());
    }
    else if (statement.isStruct())
    {
      context.setCurrentStruct(new Struct());
    }
  }
}

