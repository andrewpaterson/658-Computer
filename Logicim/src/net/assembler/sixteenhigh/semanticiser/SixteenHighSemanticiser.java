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
import net.assembler.sixteenhigh.semanticiser.triple.TripleValue;
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
    TripleVariablesStruct variablesStruct;
    if (scope == VariableScope.global)
    {
      variable = context.createGlobalVariable(name, typeDefinition);
      variablesStruct = new TripleVariablesStruct(name, context.getGlobalVariables());
    }
    else if (scope == VariableScope.unit)
    {
      UnitDefinition unit = context.getCurrentUnit();
      if (unit == null)
      {
        return error("Variable [%s] with unit scope cannot be added without a current Unit.", variable.getName());
      }
      variable = unit.createVariable(name, typeDefinition);
      variablesStruct = new TripleVariablesStruct(name, unit.getVariables());
    }
    else if (scope == VariableScope.routine)
    {
      RoutineDefinition routine = context.getCurrentRoutine();
      if (routine == null)
      {
        return error("Variable [%s] with routine scope cannot be added without a current Routine.", variable.getName());
      }
      variable = routine.createVariable(name, typeDefinition);
      variablesStruct = new TripleVariablesStruct(name, routine.getVariables());
    }
    else
    {
      throw new SimulatorException("Unknown scope [%s] when parsing variable definition statement.", scope);
    }

    if (variableStatement.hasInitialiser())
    {
      Block block = context.getCurrentBlock();

      List<Triple> triples = new ArrayList<>();
      LogResult result = parseTokenExpression(variableStatement.getInitialiserExpression(), triples, variablesStruct);
      if (result.isFailure())
      {
        return result;
      }

//      SixteenHighOperatorMap.getInstance().get(op)
//      triple.setOperator();
//
//      PrimitiveDefinition primitiveDefinition = (PrimitiveDefinition) typeDefinition;
//      triple.setLeft(new TripleVariable(primitiveDefinition.type, variable.name));

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
      PrimitiveTypeCode type = SixteenHighTypeMap.getInstance().getPrimitiveTypeForKeyword(typeCode);

      return getPrimitiveDefinition(primitiveStatement, type);
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

  private PrimitiveDefinition getPrimitiveDefinition(PrimitiveVariableTokenStatement primitiveStatement, PrimitiveTypeCode type)
  {
    return typeModifierMap.addPrimitiveDefinition(type, primitiveStatement.getArrayMatrix(), primitiveStatement.getPointerCount());
  }

  private PrimitiveDefinition getPrimitiveDefinition(PrimitiveTypeCode type)
  {
    return typeModifierMap.addPrimitiveDefinition(type, new ArrayList<>(), 0);
  }

  private LogResult parseTokenExpression(BaseTokenExpression expression, List<Triple> triples, TripleVariablesStruct variables)
  {
    if (expression.isArrayInitialiser())
    {
      throw new SimulatorException();
    }
    else if (expression.isList())
    {
      TokenExpressionList expressionList = (TokenExpressionList) expression;
      TokenExpression orderedExpression = TokenPrecedence.getInstance().orderByPrecedence(expressionList);

      TripleValue tripleValue = tokenExpression(orderedExpression, triples, variables);

      //You need to think about turning Token Expressions into Semantic Expressions.  And what to do about block.push.

      return success();
    }
    else if (expression.isPull())
    {
      return success();
    }
    else
    {
      throw new SimulatorException("Cannot parse unknown token expression [%s].", expression.getClass().getSimpleName());
    }
  }

  private TripleValue tokenExpression(TokenExpression expression, List<Triple> triples, TripleVariablesStruct variables)
  {
    if (expression.isBinary())
    {
      return binaryTokenExpression((BinaryTokenExpression) expression, triples, variables);
    }
    else if (expression.isLiteral())
    {
      return literalTokenExpression((LiteralTokenExpression) expression);
    }
    else if (expression.isVariable())
    {
      return variableTokenExpression((VariableTokenExpression) expression, triples, variables);
    }
    else if (expression.isUnary())
    {
      return unaryTokenExpression((UnaryTokenExpression) expression, triples, variables);
    }

    throw new SimulatorException();
  }

  private TripleValue binaryTokenExpression(BinaryTokenExpression expression, List<Triple> triples, TripleVariablesStruct variables)
  {
    TokenExpression leftExpression = expression.getLeftExpression();
    SixteenHighKeywordCode operator = expression.getOperator();
    TokenExpression rightExpression = expression.getRightExpression();

    TripleValue rightValue1 = tokenExpression(leftExpression, triples, variables);
    TripleValue rightValue2 = tokenExpression(rightExpression, triples, variables);

    PrimitiveTypeCode rightValue1Type = rightValue1.getTypeCode();
    PrimitiveTypeCode rightValue2Type = rightValue2.getTypeCode();

    PrimitiveDefinition leftPrimitiveDefinition = getPrimitiveDefinition(rightValue1Type);

    PrimitiveDefinition rightPrimitiveDefinition = getPrimitiveDefinition(rightValue2Type);
    VariableDefinition rightVariableDefinition = variables.create(rightPrimitiveDefinition);
    TripleVariable variable = new TripleVariable(rightVariableDefinition);

    OperatorCode operatorCode = SixteenHighOperatorMap.getInstance().get(operator);
    addTriple(triples, variable, rightValue1, operatorCode, rightValue2);
    return variable;
  }

  private TripleValue unaryTokenExpression(UnaryTokenExpression expression, List<Triple> triples, TripleVariablesStruct variables)
  {
    SixteenHighKeywordCode operator = expression.getOperator();
    TokenExpression rightExpression = expression.getExpression();

    TripleValue rightValue = tokenExpression(rightExpression, triples, variables);
    if (operator == SixteenHighKeywordCode.add)
    {
      return rightValue;
    }

    PrimitiveDefinition primitiveDefinition = getPrimitiveDefinition(rightValue.getTypeCode());
    VariableDefinition variableDefinition = variables.create(primitiveDefinition);
    TripleVariable variable = new TripleVariable(variableDefinition);

    OperatorCode operatorCode = SixteenHighOperatorMap.getInstance().get(operator);
    addTriple(triples, variable, operatorCode, rightValue);
    return variable;
  }

  public Triple addTriple(List<Triple> triples,
                          TripleValue left,
                          OperatorCode operator,
                          TripleValue right)
  {
    Triple triple = new Triple(left, operator, right);
    triples.add(triple);
    return triple;
  }

  public Triple addTriple(List<Triple> triples,
                          TripleValue left,
                          TripleValue rightOne,
                          OperatorCode operator,
                          TripleValue rightTwo)
  {
    Triple triple = new Triple(left, rightOne, operator, rightTwo);
    triples.add(triple);
    return triple;
  }

  private TripleValue literalTokenExpression(LiteralTokenExpression expression)
  {
    return new TripleLiteral(expression.getLiteral());
  }

  private TripleValue variableTokenExpression(VariableTokenExpression expression, List<Triple> triples, TripleVariablesStruct variables)
  {
    //Handle, &, *, x.y.z and [][]
    int dereferenceCount = expression.getDereferenceCount();
    boolean reference = expression.isReference();
    List<VariableMember> members = expression.getMembers();
    VariableDefinition firstVariableDefinition = null;
    for (int i = 0; i < members.size(); i++)
    {
      VariableMember member = members.get(0);
      List<TokenExpression> arrayIndices = member.getArrayIndices();
      String name = member.getIdentifier();
      if (i == 0)
      {
        VariableDefinition variableDefinition = getVariable(name);
        firstVariableDefinition = variableDefinition;
      }

    }
    return new TripleVariable(firstVariableDefinition);
  }

  private VariableDefinition getVariable(String name)
  {

    return null;
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

