package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.assembler.sixteenhigh.definition.SixteenHighDefinition;
import net.assembler.sixteenhigh.semanticiser.directive.AccessMode;
import net.assembler.sixteenhigh.semanticiser.directive.Directive;
import net.assembler.sixteenhigh.semanticiser.expression.block.Block;
import net.assembler.sixteenhigh.semanticiser.expression.evaluable.AddressableVariableExpression;
import net.assembler.sixteenhigh.semanticiser.types.StructDefinition;
import net.assembler.sixteenhigh.tokeniser.statment.RoutineTokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.StructTokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.TokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.VariableTokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.directive.*;
import net.assembler.sixteenhigh.tokeniser.statment.expression.BaseTokenExpression;
import net.common.SimulatorException;
import net.common.logger.Logger;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static net.assembler.sixteenhigh.semanticiser.LogResult.success;

public class SixteenHighSemanticiser
{
  protected List<TokenUnit> units;
  protected SixteenHighDefinition definition;
  protected SixteenHighKeywords keywords;
  protected Logger logger;

  public SixteenHighSemanticiser(SixteenHighDefinition definition,
                                 TokenUnit unit,
                                 SixteenHighKeywords keywords)
  {
    this.definition = definition;
    this.keywords = keywords;
    this.units = new ArrayList<>();
    this.units.add(unit);
    this.logger = new Logger();
  }

  public void parse()
  {
    for (TokenUnit unit : units)
    {
      parseUnit(unit);
    }
    units.clear();
  }

  private void parseUnit(TokenUnit unit)
  {
    String filename = unit.getFilename();
    SixteenHighSemanticiserContext context = new SixteenHighSemanticiserContext(filename);
    context.setCurrentUnit(new UnitDefinition());

    boolean previousDirectiveStatement = true;
    context.setCurrentDirective(new Directive());
    for (TokenStatement statement : unit.getStatements())
    {
      if (statement.isDirective())
      {
        if (!parseDirectiveStatement(filename, context, previousDirectiveStatement, statement))
        {
          return;
        }
      }
      else
      {
        if (!parseNonDirectiveStatements(statement, context))
        {
          return;
        }
        previousDirectiveStatement = true;
      }
    }
  }

  private boolean parseDirectiveStatement(String filename,
                                          SixteenHighSemanticiserContext context,
                                          boolean previousDirectiveStatement,
                                          TokenStatement statement)
  {
    if (previousDirectiveStatement)
    {
      Directive directive = context.getCurrentDirective();
      LogResult logResult = parseDirectiveStatement(directive, (DirectiveTokenStatement) statement, keywords);
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
      context.setCurrentDirective(new Directive(context.getCurrentDirective()));
    }
    return true;
  }

  public LogResult parseDirectiveStatement(Directive directive,
                                           DirectiveTokenStatement directiveStatement,
                                           SixteenHighKeywords sixteenHighKeywords)
  {
    if (directiveStatement instanceof AccessModeTokenStatement)
    {
      AccessMode accessMode = ((AccessModeTokenStatement) directiveStatement).getAccessMode();
      if (accessMode == null)
      {
        return new LogResult((directiveStatement).print(sixteenHighKeywords) + " not allowed.");
      }
      directive.setAccessMode(accessMode);
      return success();
    }
    else if (directiveStatement instanceof AccessTime)
    {
      int accessTime = ((AccessTime) directiveStatement).getTime();
      if ((accessTime <= 0) || (accessTime > 32000))
      {
        return new LogResult((directiveStatement).print(sixteenHighKeywords) + " out of bounds.  Expected (> 0, <= 32000).");
      }
      directive.setAccessTime(accessTime);
      return success();
    }
    else if (directiveStatement instanceof StartAddress)
    {
      int address = ((StartAddress) directiveStatement).getAddress();
      if (address < 0)
      {
        return new LogResult((directiveStatement).print(sixteenHighKeywords) + " out of bounds.  Expected >= 0.");
      }
      directive.setStartAddress(address);
      return success();
    }
    else if (directiveStatement instanceof EndAddress)
    {
      int address = ((EndAddress) directiveStatement).getAddress();
      if (address < 0)
      {
        return error((directiveStatement).print(sixteenHighKeywords) + " out of bounds.  Expected >= 0.");
      }
      directive.setEndAddress(address);
      return success();
    }
    else
    {
      throw new SimulatorException(StringUtil.getClassSimpleName(directiveStatement) + " unexpected.");
    }
  }

  private boolean parseNonDirectiveStatements(TokenStatement statement, SixteenHighSemanticiserContext context)
  {
    if (statement.isRoutine())
    {
      RoutineTokenStatement routineStatement = (RoutineTokenStatement) statement;
      LogResult logResult = parseRoutineStatement(routineStatement, context);
      return logResult.isFailure();
    }
    else if (statement.isStruct())
    {
      StructTokenStatement structStatement = (StructTokenStatement) statement;
      LogResult logResult = parseStructStatement(structStatement, context);
      return logResult.isFailure();
    }
    else if (statement.isEnd())
    {
      context.setEnd();
      return true;
    }
    else if (statement.isVariable())
    {
      VariableTokenStatement variableStatement = (VariableTokenStatement) statement;
      LogResult logResult = parseVariableStatement(variableStatement, context);
      return logResult.isFailure();
    }
    return false;
  }

  private LogResult parseVariableStatement(VariableTokenStatement variableStatement, SixteenHighSemanticiserContext context)
  {
    String name = variableStatement.getName();
    VariableDefinition variable = context.getVariable(name);
    if (variable != null)
    {
      return error("Variable [%s] already defined.", variable.getName());
    }

    VariableScope scope = variableStatement.getScope();
    variable = new VariableDefinition(name, scope);
    if (scope == VariableScope.global)
    {
      variable = context.createGlobalVariable(name);
    }
    else if (scope == VariableScope.unit)
    {
      UnitDefinition unit = context.getCurrentUnit();
      if (unit == null)
      {
        return error("Variable [%s] with unit scope cannot be added without a current Unit.", variable.getName());
      }
      variable = unit.createVariable(name);
    }
    else if (scope == VariableScope.routine)
    {
      RoutineDefinition routine = context.getCurrentRoutine();
      if (routine == null)
      {
        return error("Variable [%s] with routine scope cannot be added without a current Routine.", variable.getName());
      }
      variable = routine.createVariable(name);
    }

    if (variableStatement.hasInitialiser())
    {
      Block block = context.getCurrentBlock();
      AddressableVariableExpression addressableVariableExpression = new AddressableVariableExpression(variable);

      parseTokenExpression(variableStatement.getInitialiserExpression());

      block.pushAssignment(addressableVariableExpression);
    }

    return success();
  }

  private void parseTokenExpression(BaseTokenExpression tokenExpression)
  {

  }

  private LogResult parseStructStatement(StructTokenStatement structStatement, SixteenHighSemanticiserContext context)
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

  private LogResult parseRoutineStatement(RoutineTokenStatement routineStatement, SixteenHighSemanticiserContext context)
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

    if (routineScope == VariableScope.unit)
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

