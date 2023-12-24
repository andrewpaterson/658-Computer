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
import net.assembler.sixteenhigh.tokeniser.statment.StructStatement;
import net.assembler.sixteenhigh.tokeniser.statment.VariableStatement;
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
  protected SixteenHighDefinition definition;
  protected SixteenHighKeywords keywords;
  protected Logger logger;

  public SixteenHighSemanticiser(SixteenHighDefinition definition, Statements statements, SixteenHighKeywords keywords)
  {
    this.definition = definition;
    this.keywords = keywords;
    this.statementsList = new ArrayList<>();
    this.statementsList.add(statements);
    this.logger = new Logger();
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
    SixteenHighSemanticiserContext context = new SixteenHighSemanticiserContext(filename);

    boolean previousDirectiveStatement = true;
    context.setCurrentDirectiveBlock(new DirectiveBlock());
    for (Statement statement : statements)
    {
      if (statement.isDirective())
      {
        if (!parseDirectiveStatement(filename, context, previousDirectiveStatement, statement))
        {
          return false;
        }
      }
      else
      {
        if (!parseNonDirectiveStatements(statement, context))
        {
          return false;
        }
        previousDirectiveStatement = true;
      }
    }
    return true;
  }

  private boolean parseDirectiveStatement(String filename,
                                          SixteenHighSemanticiserContext context,
                                          boolean previousDirectiveStatement,
                                          Statement statement)
  {
    if (previousDirectiveStatement)
    {
      DirectiveBlock directiveBlock = context.getCurrentDirectiveBlock();
      LogResult logResult = parseDirectiveStatement(directiveBlock, (DirectiveStatement) statement, keywords);
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
    return true;
  }

  public LogResult parseDirectiveStatement(DirectiveBlock directiveBlock,
                                           DirectiveStatement directiveStatement,
                                           SixteenHighKeywords sixteenHighKeywords)
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
        return error((directiveStatement).print(sixteenHighKeywords) + " out of bounds.  Expected >= 0.");
      }
      directiveBlock.setEndAddress(address);
      return success();
    }
    else
    {
      throw new SimulatorException(StringUtil.getClassSimpleName(directiveStatement) + " unexpected.");
    }
  }

  private boolean parseNonDirectiveStatements(Statement statement, SixteenHighSemanticiserContext context)
  {
    if (statement.isRoutine())
    {
      RoutineStatement routineStatement = (RoutineStatement) statement;
      LogResult logResult = parseRoutineStatement(routineStatement, context);
      return logResult.isFailure();
    }
    else if (statement.isStruct())
    {
      StructStatement structStatement = (StructStatement) statement;
      LogResult logResult = parseStructStatement(structStatement, context);
      return logResult.isFailure();
    }
    else if (statement.isEnd())
    {
      context.setEnd();
    }
    else if (statement.isVariable())
    {
      VariableStatement variableStatement = (VariableStatement) statement;
      LogResult logResult = parseVariableStatement(variableStatement, context);
      return logResult.isFailure();
    }
    return false;
  }

  private LogResult parseVariableStatement(VariableStatement variableStatement, SixteenHighSemanticiserContext context)
  {
    return null;
  }

  private LogResult parseStructStatement(StructStatement structStatement, SixteenHighSemanticiserContext context)
  {
    StructDefinition structName = new StructDefinition(structStatement.getName());
    if (context.getCurrentStruct() != null)
    {
      return error("Struct [%s] nested in struct [%s] is not allowed.", structName, context.getCurrentStruct().getName());
    }
    if (context.getCurrentRoutine() != null)
    {
      return error("Struct [%s] nested in routine [%s] is not allowed.", structName, context.getCurrentRoutine().getName());
    }

    context.setCurrentStruct(structName);
    return success();
  }

  private LogResult parseRoutineStatement(RoutineStatement routineStatement, SixteenHighSemanticiserContext context)
  {
    String routineName = routineStatement.getName();
    if (context.getCurrentRoutine() != null)
    {
      return error("Routine [%s] nested in routine [%s] is not allowed.", routineName, context.getCurrentRoutine().getName());
    }
    if (context.getCurrentStruct() != null)
    {
      return error("Routine [%s] nested in struct [%s] is not allowed.", routineName, context.getCurrentStruct().getName());
    }

    VariableScope routineScope = routineStatement.getScope();
    RoutineDefinition existingGlobalRoutine = definition.getGlobalRoutine(routineName);
    if (existingGlobalRoutine != null)
    {
      return error("A global routine [%s] is already defined.", routineName);
    }

    RoutineDefinition existingUnitRoutine = context.getCurrentUnit().getRoutine(routineName);
    if (existingUnitRoutine != null)
    {
      return error("A unit routine [%s] is already defined.", routineName);
    }
    if (routineScope == VariableScope.routine)
    {
      return error("Routine [%s] may not have [routine] scope.", routineName);
    }

    if (routineScope == VariableScope.file)
    {
      RoutineDefinition routine = definition.createUnitRoutine(context.getCurrentUnit(), routineName, routineScope);
      context.setCurrentRoutine(routine);
      return success();
    }
    else if (routineScope == VariableScope.global)
    {
      RoutineDefinition routine = definition.createGlobalRoutine(routineName, routineScope);
      context.setCurrentRoutine(routine);
      return success();
    }
    else
    {
      throw new SimulatorException("Unknown Variable Scope [%s] for Routine [%s].", routineScope, routineName);
    }
  }

  private LogResult error(String message, Object... parameters)
  {
    return new LogResult(String.format(message, parameters));
  }
}

