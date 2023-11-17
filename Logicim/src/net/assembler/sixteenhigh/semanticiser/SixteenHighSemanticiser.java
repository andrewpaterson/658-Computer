package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.assembler.sixteenhigh.definition.SixteenHighDefinition;
import net.assembler.sixteenhigh.semanticiser.directive.AccessMode;
import net.assembler.sixteenhigh.semanticiser.directive.DirectiveBlock;
import net.assembler.sixteenhigh.semanticiser.types.StructDefinition;
import net.assembler.sixteenhigh.tokeniser.statment.RoutineStatement;
import net.assembler.sixteenhigh.tokeniser.statment.Statement;
import net.assembler.sixteenhigh.tokeniser.statment.directive.*;
import net.common.SimulatorException;
import net.common.logger.Logger;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static net.assembler.sixteenhigh.semanticiser.LogResult.success;

public class SixteenHighSemanticiser
{
  protected List<Statements> statementsList;
  protected SixteenHighDefinition sixteenHighDefinition;
  protected SixteenHighKeywords keywords;
  protected Logger logger;

  public SixteenHighSemanticiser(SixteenHighDefinition sixteenHighDefinition, Statements statements, SixteenHighKeywords keywords)
  {
    this.sixteenHighDefinition = sixteenHighDefinition;
    this.keywords = keywords;
    this.statementsList = new ArrayList<>();
    this.statementsList.add(statements);
    this.logger = new Logger();
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

  private boolean parseStatements(List<Statement> statements, String filename)
  {
    SixteenHighSemanticiserContext context = new SixteenHighSemanticiserContext(sixteenHighDefinition, filename);

    boolean previousDirectiveStatement = true;
    context.setCurrentDirectiveBlock(new DirectiveBlock());
    for (Statement statement : statements)
    {
      if (statement.isDirective())
      {
        if (previousDirectiveStatement)
        {
          DirectiveBlock directiveBlock = context.getCurrentDirectiveBlock();
          LogResult logResult = set(directiveBlock, (DirectiveStatement) statement, keywords);
          if (logResult.isFailure())
          {
            if (!logger.log(filename, statement.getIndex(), logResult))
            {
              return false;
            }
          }
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
    return true;
  }

  public LogResult set(DirectiveBlock directiveBlock, DirectiveStatement directiveStatement, SixteenHighKeywords sixteenHighKeywords)
  {
    if (directiveStatement instanceof AccessModeStatement)
    {
      AccessMode accessMode = ((AccessModeStatement) directiveStatement).getAccessMode();
      if (accessMode == null)
      {
        return new LogResult((directiveStatement).print(sixteenHighKeywords) + " not allowed.");
      }
      directiveBlock.setAccessMode(accessMode);
      return success();
    }
    else if (directiveStatement instanceof AccessTime)
    {
      int accessTime = ((AccessTime) directiveStatement).getTime();
      if ((accessTime <= 0) || (accessTime > 32000))
      {
        return new LogResult((directiveStatement).print(sixteenHighKeywords) + " out of bounds.  Expected (> 0, <= 32000).");
      }
      directiveBlock.setAccessTime(accessTime);
      return success();
    }
    else if (directiveStatement instanceof StartAddress)
    {
      int address = ((StartAddress) directiveStatement).getAddress();
      if (address < 0)
      {
        return new LogResult((directiveStatement).print(sixteenHighKeywords) + " out of bounds.  Expected >= 0.");
      }
      directiveBlock.setStartAddress(address);
      return success();
    }
    else if (directiveStatement instanceof EndAddress)
    {
      int address = ((EndAddress) directiveStatement).getAddress();
      if (address < 0)
      {
        return new LogResult((directiveStatement).print(sixteenHighKeywords) + " out of bounds.  Expected >= 0.");
      }
      directiveBlock.setEndAddress(address);
      return success();
    }
    else
    {
      throw new SimulatorException(StringUtil.getClassSimpleName(directiveStatement) + " unexpected.");
    }
  }

  private void parseNonDirectiveStatements(Statement statement, SixteenHighSemanticiserContext context)
  {
    if (statement.isRoutine())
    {
      RoutineStatement routineStatement = (RoutineStatement) statement;
      parseRoutineStatement(routineStatement, context);
    }
    else if (statement.isStruct())
    {
      context.setCurrentStruct(new StructDefinition());
    }
    else if (statement.isEnd())
    {
      context.setEnd();
    }
  }

  private void parseRoutineStatement(RoutineStatement routineStatement, SixteenHighSemanticiserContext context)
  {
    if (context.currentRoutine == null)
    {
      if (routineStatement.getScope() == VariableScope.file)
      {
        Routine existingRoutine = context.getRoutine(routineStatement.getName());
        Routine routine = new Routine();
        context.setCurrentRoutine(routine);
      }
    }

  }
}

