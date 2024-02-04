package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.assembler.sixteenhigh.definition.SixteenHighDefinition;
import net.assembler.sixteenhigh.semanticiser.directive.AccessMode;
import net.assembler.sixteenhigh.semanticiser.directive.Directive;
import net.assembler.sixteenhigh.semanticiser.expression.block.Block;
import net.assembler.sixteenhigh.semanticiser.expression.operator.OperatorCode;
import net.assembler.sixteenhigh.semanticiser.expression.operator.SixteenHighOperatorMap;
import net.assembler.sixteenhigh.semanticiser.expression.operator.SixteenHighTypeMap;
import net.assembler.sixteenhigh.semanticiser.triple.Triple;
import net.assembler.sixteenhigh.semanticiser.triple.TripleLiteral;
import net.assembler.sixteenhigh.semanticiser.triple.TripleResult;
import net.assembler.sixteenhigh.semanticiser.triple.TripleVariable;
import net.assembler.sixteenhigh.semanticiser.types.PrimitiveDefinition;
import net.assembler.sixteenhigh.semanticiser.types.StructDefinition;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;
import net.assembler.sixteenhigh.tokeniser.precedence.TokenPrecedence;
import net.assembler.sixteenhigh.tokeniser.statment.*;
import net.assembler.sixteenhigh.tokeniser.statment.directive.*;
import net.assembler.sixteenhigh.tokeniser.statment.expression.*;
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
  protected TypeModifierMap typeModifierMap;
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
    this.typeModifierMap = new TypeModifierMap();
  }

  public SixteenHighDefinition getDefinition()
  {
    return definition;
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
    else if (statement.isVariableDefinition())
    {
      VariableTokenStatement variableStatement = (VariableTokenStatement) statement;
      LogResult logResult = parseVariableDefinitionStatement(variableStatement, context);
      return logResult.isFailure();
    }
    else if (statement.isAssignment())
    {
      int xxx = 0;
      return false;
    }
    else
    {
      throw new SimulatorException();
    }
  }

  private LogResult parseVariableDefinitionStatement(VariableTokenStatement variableStatement, SixteenHighSemanticiserContext context)
  {
    String name = variableStatement.getName();
    VariableDefinition variable = context.getVariable(name);
    if (variable != null)
    {
      return error("Variable [%s] already defined.", variable.getName());
    }

    TypeDefinition typeDefinition = getTypeDefinition(variableStatement);

    VariableScope scope = variableStatement.getScope();
    variable = null;
    if (scope == VariableScope.global)
    {
      variable = context.createGlobalVariable(name, typeDefinition);
    }
    else if (scope == VariableScope.unit)
    {
      UnitDefinition unit = context.getCurrentUnit();
      if (unit == null)
      {
        return error("Variable [%s] with unit scope cannot be added without a current Unit.", variable.getName());
      }
      variable = unit.createVariable(name, typeDefinition);
    }
    else if (scope == VariableScope.routine)
    {
      RoutineDefinition routine = context.getCurrentRoutine();
      if (routine == null)
      {
        return error("Variable [%s] with routine scope cannot be added without a current Routine.", variable.getName());
      }
      variable = routine.createVariable(name, typeDefinition);
    }

    if (variableStatement.hasInitialiser())
    {
      Block block = context.getCurrentBlock();

      List<Triple> triples = new ArrayList<>();
      TripleResult tripleResult = parseTokenExpression(variableStatement.getInitialiserExpression(), triples);
      if (tripleResult.logResult.isFailure())
      {
        return tripleResult.logResult;
      }

      Triple triple = tripleResult.triple;
      if (triple.getLeft() != null)
      {
        throw new SimulatorException("Triple already has Left operand [%s] set.", triple.getLeft().toString());
      }

      triple.setOperator(OperatorCode.none);

      PrimitiveDefinition primitiveDefinition = (PrimitiveDefinition) typeDefinition;
      triple.setLeft(new TripleVariable(primitiveDefinition.type, variable.name));

      block.pushTriples(triples);
    }

    return success();
  }

  private TypeDefinition getTypeDefinition(VariableTokenStatement variableStatement)
  {
    if (variableStatement.isPrimitiveVariable())
    {
      PrimitiveVariableTokenStatement primitiveStatement = (PrimitiveVariableTokenStatement) variableStatement;
      SixteenHighKeywordCode typeCode = primitiveStatement.getType();
      PrimitiveTypeCode type = SixteenHighTypeMap.getInstance().get(typeCode);

      return typeModifierMap.addPrimitiveDefinition(type, primitiveStatement.getArrayMatrix(), primitiveStatement.getPointerCount());
    }
    else if (variableStatement.isStructVariable())
    {
      StructVariableTokenStatement structStatement = (StructVariableTokenStatement) variableStatement;
      String name = structStatement.getName();
      throw new SimulatorException();
    }
    else
    {
      throw new SimulatorException("Cannot get VariableDefinition for unknown VariableStatement class [%s].", variableStatement.getClass().getSimpleName());
    }
  }

  private TripleResult parseTokenExpression(BaseTokenExpression baseTokenExpression, List<Triple> triples)
  {
    if (baseTokenExpression.isArrayInitialiser())
    {
      return new TripleResult(null, success());
    }
    else if (baseTokenExpression.isList())
    {
      TokenExpressionList expressionList = (TokenExpressionList) baseTokenExpression;
      TokenExpression expression = TokenPrecedence.getInstance().orderByPrecedence(expressionList);

      Triple triple = recurseTokenExpression(expression, triples);

      //You need to think about turning Token Expressions into Semantic Expressions.  And what to do about block.push.

      return new TripleResult(triple, success());
    }
    else if (baseTokenExpression.isPull())
    {
      return new TripleResult(null, success());
    }
    else
    {
      throw new SimulatorException("Cannot parse unknown token expression [%s].", baseTokenExpression.getClass().getSimpleName());
    }
  }

  private Triple recurseTokenExpression(TokenExpression expression, List<Triple> triples)
  {
    if (expression.isBinary())
    {
      BinaryTokenExpression binaryTokenExpression = (BinaryTokenExpression) expression;
      TokenExpression leftExpression = binaryTokenExpression.getLeftExpression();
      if (leftExpression.isLiteral())
      {
        LiteralTokenExpression literalTokenExpression = (LiteralTokenExpression) leftExpression;
        Triple triple = new Triple();
        triple.setRightOne(new TripleLiteral(literalTokenExpression.getLiteral()));
        OperatorCode operator = SixteenHighOperatorMap.getInstance().get(binaryTokenExpression.getOperator());
        triple.setOperator(operator);

        // Do Better.

        triples.add(triple);
        return triple;
      }
      else if (leftExpression.isVariable())
      {
        VariableTokenExpression variableTokenExpression = (VariableTokenExpression) leftExpression;
        recurseVariableExpression(variableTokenExpression, triples);
      }
    }
    else if (expression.isLiteral())
    {
      LiteralTokenExpression literalTokenExpression = (LiteralTokenExpression) expression;
      Triple triple = new Triple();
      triple.setRightOne(new TripleLiteral(literalTokenExpression.getLiteral()));
      triple.setOperator(OperatorCode.none);

      triples.add(triple);
      return triple;
    }
    throw new SimulatorException();
  }

  private void recurseVariableExpression(VariableTokenExpression variableTokenExpression, List<Triple> triples)
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

