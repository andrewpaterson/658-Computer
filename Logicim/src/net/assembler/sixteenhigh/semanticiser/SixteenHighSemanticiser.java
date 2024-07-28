package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.common.scope.Scope;
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
import net.assembler.sixteenhigh.semanticiser.types.RecordDefinition;
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

  public boolean parse()
  {
    boolean success = true;
    for (TokenUnit unit : units)
    {
      boolean result = parseUnit(unit);
      if (!result)
      {
        success = false;
        break;
      }
    }
    units.clear();
    return success;
  }

  private boolean parseUnit(TokenUnit tokenUnit)
  {
    String filename = tokenUnit.getFilename();
    UnitDefinition unitDefinition = definition.createUnit(filename);
    SixteenHighSemanticiserContext context = new SixteenHighSemanticiserContext(unitDefinition, definition);

    boolean previousDirectiveStatement = true;
    context.setCurrentDirective(new Directive());
    for (TokenStatement statement : tokenUnit.getStatements())
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
                                          SixteenHighSemanticiserContext semanticContext,
                                          boolean previousDirectiveStatement,
                                          TokenStatement statement)
  {
    if (previousDirectiveStatement)
    {
      Directive directive = semanticContext.getCurrentDirective();
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
      semanticContext.setCurrentDirective(new Directive(semanticContext.getCurrentDirective()));
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

  private boolean parseNonDirectiveStatements(TokenStatement statement, SixteenHighSemanticiserContext semanticContext)
  {
    if (statement.isRoutine())
    {
      RoutineTokenStatement routineStatement = (RoutineTokenStatement) statement;
      LogResult logResult = parseRoutineStatement(routineStatement, semanticContext);
      return logResult.isSuccess();
    }
    else if (statement.isRecord())
    {
      RecordTokenStatement structStatement = (RecordTokenStatement) statement;
      LogResult logResult = parseRecordStatement(structStatement, semanticContext);
      return logResult.isSuccess();
    }
    else if (statement.isEnd())
    {
      semanticContext.setEnd();
      return true;
    }
    else if (statement.isVariableDefinition())
    {
      VariableTokenStatement variableStatement = (VariableTokenStatement) statement;
      LogResult logResult = parseVariableDefinitionStatement(variableStatement, semanticContext);
      return logResult.isSuccess();
    }
    else if (statement.isAssignment())
    {
      AssignmentTokenStatement assignmentStatement = (AssignmentTokenStatement) statement;
      LogResult logResult = parseAssignmentStatement(assignmentStatement, semanticContext);
      return logResult.isSuccess();
    }
    else
    {
      throw new SimulatorException();
    }
  }

  private LogResult parseAssignmentStatement(AssignmentTokenStatement variableStatement, SixteenHighSemanticiserContext semanticContext)
  {
    // &a[5].s[77] = 0;
    // *(*p[3])[5]

//    String name = variableStatement.getName();
//    Block block = semanticContext.getCurrentBlock();
//    VariableDefinition variable = semanticContext.getVariable(name);
//    if (variable == null)
//    {
//      return error("Variable [%s] not defined.", variable.getName());
//    }
//
//    AutoVariableContext variableContext = new AutoVariableContext(name, variable.getVariables());
//    List<Triple> triples = new ArrayList<>();
//    LogResult result = parseTokenExpression(variable,
//                                            variableStatement,
//                                            triples,
//                                            variableContext,
//                                            semanticContext);
//    if (result.isFailure())
//    {
//      return result;
//    }
//
//    block.pushTriples(triples);
    return success();
  }

  private LogResult parseVariableDefinitionStatement(VariableTokenStatement variableStatement, SixteenHighSemanticiserContext semanticContext)
  {
    String name = variableStatement.getName();
    VariableDefinition variable = semanticContext.getVariable(name);
    if (variable != null)
    {
      return error("Variable [%s] already defined.", variable.getName());
    }

    TypeDefinition typeDefinition = getTypeDefinition(variableStatement);

    Scope scope = variableStatement.getScope();
    if (scope == Scope.global)
    {
      variable = definition.createGlobalVariable(name, typeDefinition);
    }
    else if (scope == Scope.unit)
    {
      UnitDefinition unit = semanticContext.getUnit();
      if (unit == null)
      {
        return error("Variable [%s] with unit scope cannot be added without a current Unit.", variableStatement.getName());
      }
      variable = unit.createVariable(name, typeDefinition);
    }
    else if (scope == Scope.routine)
    {
      RoutineDefinition routine = semanticContext.getCurrentRoutine();
      if (routine == null)
      {
        return error("Variable [%s] with routine scope cannot be added without a current Routine.", variableStatement.getName());
      }
      variable = routine.createVariable(name, typeDefinition);
    }
    else
    {
      throw new SimulatorException("Unknown scope [%s] when parsing variable definition statement.", scope);
    }

    if (variableStatement.hasInitialiser())
    {
      AutoVariableContext variableContext = new AutoVariableContext(name, variable.getVariables());
      Block block = semanticContext.getCurrentBlock();

      List<Triple> triples = new ArrayList<>();
      LogResult result = parseTokenExpression(variable,
                                              variableStatement.getInitialiserExpression(),
                                              triples,
                                              variableContext,
                                              semanticContext);
      if (result.isFailure())
      {
        return result;
      }

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
    else if (variableStatement.isRecordVariable())
    {
      RecordVariableTokenStatement structStatement = (RecordVariableTokenStatement) variableStatement;
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

  private LogResult parseTokenExpression(VariableDefinition destinationVariable,
                                         BaseTokenExpression expression,
                                         List<Triple> triples,
                                         AutoVariableContext variables,
                                         SixteenHighSemanticiserContext semanticContext)
  {
    if (expression.isArrayInitialiser())
    {
      throw new SimulatorException();
    }
    else if (expression.isList())
    {
      TokenExpressionList expressionList = (TokenExpressionList) expression;
      TokenExpression orderedExpression = TokenPrecedence.getInstance().orderByPrecedence(expressionList);

      TripleValue expressionTripleValue = tokenExpression(orderedExpression, triples, variables, semanticContext);
      addTriple(triples, new TripleVariable(destinationVariable), null, expressionTripleValue);

      return success();
    }
    else
    {
      throw new SimulatorException("Cannot parse unknown token expression [%s].", expression.getClass().getSimpleName());
    }
  }

  private TripleValue tokenExpression(TokenExpression expression,
                                      List<Triple> triples,
                                      AutoVariableContext variables,
                                      SixteenHighSemanticiserContext semanticContext)
  {
    if (expression.isBinary())
    {
      return binaryTokenExpression((BinaryTokenExpression) expression, triples, variables, semanticContext);
    }
    else if (expression.isLiteral())
    {
      return literalTokenExpression((LiteralTokenExpression) expression);
    }
    else if (expression.isVariable())
    {
      return variableTokenExpression((VariableTokenExpression) expression, triples, variables, semanticContext);
    }
    else if (expression.isUnary())
    {
      return unaryTokenExpression((UnaryTokenExpression) expression, triples, variables, semanticContext);
    }

    throw new SimulatorException();
  }

  private TripleValue binaryTokenExpression(BinaryTokenExpression expression,
                                            List<Triple> triples,
                                            AutoVariableContext variables,
                                            SixteenHighSemanticiserContext semanticContext)
  {
    TokenExpression leftExpression = expression.getLeftExpression();
    SixteenHighKeywordCode operator = expression.getOperator();
    TokenExpression rightExpression = expression.getRightExpression();

    TripleValue rightValue1 = tokenExpression(leftExpression, triples, variables, semanticContext);
    TripleValue rightValue2 = tokenExpression(rightExpression, triples, variables, semanticContext);

    PrimitiveTypeCode rightValue1Type = rightValue1.getTypeCode();
    PrimitiveTypeCode rightValue2Type = rightValue2.getTypeCode();

    PrimitiveDefinition rightPrimitive1Definition = getPrimitiveDefinition(rightValue1Type);
    PrimitiveDefinition rightPrimitive2Definition = getPrimitiveDefinition(rightValue2Type);

    PrimitiveTypeCode primitiveTypeCode = SixteenHighTypeMap.getInstance().getAutoCast(rightPrimitive1Definition.getType(), rightPrimitive2Definition.getType());
    PrimitiveDefinition leftPrimitiveDefinition = typeModifierMap.addPrimitiveDefinition(primitiveTypeCode, new ArrayList<>(), 0);
    VariableDefinition rightVariableDefinition = variables.create(leftPrimitiveDefinition);
    TripleVariable variable = new TripleVariable(rightVariableDefinition);

    OperatorCode operatorCode = SixteenHighOperatorMap.getInstance().get(operator);
    addTriple(triples, variable, rightValue1, operatorCode, rightValue2);
    return variable;
  }

  private TripleValue unaryTokenExpression(UnaryTokenExpression expression,
                                           List<Triple> triples,
                                           AutoVariableContext variables,
                                           SixteenHighSemanticiserContext semanticContext)
  {
    SixteenHighKeywordCode operator = expression.getOperator();
    TokenExpression rightExpression = expression.getExpression();

    TripleValue rightValue = tokenExpression(rightExpression, triples, variables, semanticContext);
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

  private TripleValue variableTokenExpression(VariableTokenExpression expression,
                                              List<Triple> triples,
                                              AutoVariableContext variableContext,
                                              SixteenHighSemanticiserContext semanticContext)
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
        VariableDefinition variableDefinition = getVariable(semanticContext, name);
        firstVariableDefinition = variableDefinition;
      }

    }
    return new TripleVariable(firstVariableDefinition);
  }

  private VariableDefinition getVariable(SixteenHighSemanticiserContext semanticContext, String name)
  {
    return semanticContext.getVariable(name);
  }

  private LogResult parseRecordStatement(RecordTokenStatement recordStatement,
                                         SixteenHighSemanticiserContext semanticContext)
  {
    RecordDefinition structName = new RecordDefinition(recordStatement.getName());
    if (semanticContext.getCurrentRecord() != null)
    {
      return error("Struct [%s] nested in struct [%s] is not allowed.", structName, semanticContext.getCurrentRecord().getName());
    }
    if (semanticContext.getCurrentRoutine() != null)
    {
      return error("Struct [%s] nested in routine [%s] is not allowed.", structName, semanticContext.getCurrentRoutine().getName());
    }

    semanticContext.setCurrentRecord(structName);
    return success();
  }

  private LogResult parseRoutineStatement(RoutineTokenStatement routineStatement,
                                          SixteenHighSemanticiserContext semanticContext)
  {
    String routineName = routineStatement.getName();
    if (semanticContext.getCurrentRoutine() != null)
    {
      return error("Routine [%s] nested in routine [%s] is not allowed.", routineName, semanticContext.getCurrentRoutine().getName());
    }
    if (semanticContext.getCurrentRecord() != null)
    {
      return error("Routine [%s] nested in struct [%s] is not allowed.", routineName, semanticContext.getCurrentRecord().getName());
    }

    Scope routineScope = routineStatement.getScope();
    RoutineDefinition existingGlobalRoutine = definition.getGlobalRoutine(routineName);
    if (existingGlobalRoutine != null)
    {
      return error("A global routine [%s] is already defined.", routineName);
    }

    RoutineDefinition existingUnitRoutine = semanticContext.getUnit().getRoutine(routineName);
    if (existingUnitRoutine != null)
    {
      return error("A unit routine [%s] is already defined.", routineName);
    }
    if (routineScope == Scope.routine)
    {
      return error("Routine [%s] may not have [routine] scope.", routineName);
    }

    if (routineScope == Scope.unit)
    {
      RoutineDefinition routine = definition.createUnitRoutine(semanticContext.getUnit(), routineName, routineScope);
      semanticContext.setCurrentRoutine(routine);
      return success();
    }
    else if (routineScope == Scope.global)
    {
      RoutineDefinition routine = definition.createGlobalRoutine(routineName, routineScope);
      semanticContext.setCurrentRoutine(routine);
      return success();
    }
    else
    {
      throw new SimulatorException("Unknown Variable Scope [%s] for Routine [%s].", routineScope, routineName);
    }
  }

  public SixteenHighDefinition getDefinition()
  {
    return definition;
  }

  private LogResult error(String message, Object... parameters)
  {
    return new LogResult(String.format(message, parameters));
  }
}

