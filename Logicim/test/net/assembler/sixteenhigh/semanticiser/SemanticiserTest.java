package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.definition.SixteenHighDefinition;
import net.assembler.sixteenhigh.semanticiser.expression.operator.OperatorCode;
import net.assembler.sixteenhigh.semanticiser.expression.operator.SixteenHighTypeMap;
import net.assembler.sixteenhigh.semanticiser.triple.Triple;
import net.assembler.sixteenhigh.semanticiser.triple.TripleLiteral;
import net.assembler.sixteenhigh.semanticiser.triple.TripleValue;
import net.assembler.sixteenhigh.semanticiser.triple.TripleVariable;
import net.assembler.sixteenhigh.tokeniser.ParseResult;
import net.assembler.sixteenhigh.tokeniser.SixteenHighTokeniser;
import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;
import net.common.logger.Logger;
import net.common.util.CollectionUtil;

import java.util.List;

import static net.assembler.sixteenhigh.common.scope.Scope.unit;
import static net.assembler.sixteenhigh.semanticiser.expression.operator.OperatorCode.*;
import static net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode.*;
import static net.logicim.assertions.Validator.*;

public abstract class SemanticiserTest
{
  protected static void testSimple1()
  {
    SixteenHighSemanticiser semanticiser = createSixteenHighSemanticiser("testSimple", "int32 @@a = 5;");
    validateTrue(semanticiser.parse());
    SixteenHighDefinition definition = semanticiser.getDefinition();
    UnitDefinition unitDefinition = definition.getUnit("testSimple");

    VariableDefinition globalVariable = definition.getGlobalVariable("@@a");
    validateNotNull(globalVariable);

    validateEquals(0, unitDefinition.numVariables(true));
    validateEquals(0, unitDefinition.numVariables(false));
    validateEquals(0, unitDefinition.numAutoVariables());

    List<Triple> triples = unitDefinition.getTriples();
    validateEquals(1, triples.size());

    validateTripleVariableLiteral(triples.get(0), int32, "@@a", null, null, null, int32, "5");
  }

  protected static void testSimple2()
  {
    SixteenHighSemanticiser semanticiser = createSixteenHighSemanticiser("testSimple", "int32 @@a = 2; @@a /= 5;");
    validateTrue(semanticiser.parse());
    SixteenHighDefinition definition = semanticiser.getDefinition();
    UnitDefinition unitDefinition = definition.getUnit("testSimple");

    VariableDefinition globalVariable = definition.getGlobalVariable("@@a");
    validateNotNull(globalVariable);

    validateEquals(0, unitDefinition.numVariables(true));
    validateEquals(0, unitDefinition.numVariables(false));
    validateEquals(0, unitDefinition.numAutoVariables());

    List<Triple> triples = unitDefinition.getTriples();
    validateEquals(1, triples.size());

    validateTripleVariableLiteral(triples.get(0), int32, "@@a", null, null, null, int32, "5");
  }

  protected static void testComplex1()
  {
    SixteenHighSemanticiser semanticiser = createSixteenHighSemanticiser("testComplex", "int16 @c; int8 @x; int8 @y; int32 @a = (+@c * 3b + (@x - @y));");
    validateTrue(semanticiser.parse());
    SixteenHighDefinition definition = semanticiser.getDefinition();
    UnitDefinition unitDefinition = definition.getUnit("testComplex");

    validateEquals(7, unitDefinition.numVariables(true));
    validateEquals(4, unitDefinition.numVariables(false));
    validateEquals(3, unitDefinition.numAutoVariables());

    validateVariable(unitDefinition.getVariable("@c"), "@c", unit, false, int16);
    validateVariable(unitDefinition.getVariable("@x"), "@x", unit, false, int8);
    validateVariable(unitDefinition.getVariable("@y"), "@y", unit, false, int8);
    validateVariable(unitDefinition.getVariable("@a"), "@a", unit, false, int32);
    validateVariable(unitDefinition.getVariable("@a~0"), "@a~0", unit, true, int16);
    validateVariable(unitDefinition.getVariable("@a~1"), "@a~1", unit, true, int8);
    validateVariable(unitDefinition.getVariable("@a~2"), "@a~2", unit, true, int16);

    List<Triple> triples = unitDefinition.getTriples();
    validateEquals(4, triples.size());
    validateTripleVariableLiteral(triples.get(0), int16, "@a~0", int16, "@c", multiply, int8, "3");
    validateTripleVariableVariable(triples.get(1), int8, "@a~1", int8, "@x", subtract, int8, "@y");
    validateTripleVariableVariable(triples.get(2), int16, "@a~2", int16, "@a~0", add, int8, "@a~1");
    validateTripleVariableVariable(triples.get(3), int32, "@a", null, null, null, int16, "@a~2");
  }

  protected static void testComplex2()
  {
    SixteenHighSemanticiser semanticiser = createSixteenHighSemanticiser("testComplex", "int8 @y; int32 @a = (-0b10b * 3b + @y % @y)");
    validateTrue(semanticiser.parse());
    SixteenHighDefinition definition = semanticiser.getDefinition();
    UnitDefinition unitDefinition = definition.getUnit("testComplex");

    validateEquals(5, unitDefinition.numVariables(true));
    validateEquals(2, unitDefinition.numVariables(false));
    validateEquals(3, unitDefinition.numAutoVariables());

    validateVariable(unitDefinition.getVariable("@y"), "@y", unit, false, int8);
    validateVariable(unitDefinition.getVariable("@a"), "@a", unit, false, int32);
    validateVariable(unitDefinition.getVariable("@a~0"), "@a~0", unit, true, int8);
    validateVariable(unitDefinition.getVariable("@a~1"), "@a~1", unit, true, int8);
    validateVariable(unitDefinition.getVariable("@a~2"), "@a~2", unit, true, int8);

    List<Triple> triples = unitDefinition.getTriples();
    validateEquals(4, triples.size());
    validateTripleLiteralLiteral(triples.get(0), int8, "@a~0", int8, "-2", multiply, int8, "3");
    validateTripleVariableVariable(triples.get(1), int8, "@a~1", int8, "@y", modulus, int8, "@y");
    validateTripleVariableVariable(triples.get(2), int8, "@a~2", int8, "@a~0", add, int8, "@a~1");
    validateTripleVariableVariable(triples.get(3), int32, "@a", null, null, null, int8, "@a~2");
  }

  protected static void testComplex3()
  {
    SixteenHighSemanticiser semanticiser = createSixteenHighSemanticiser("testComplex", "int32 @a = 5; int32 @b = (@a + 0x1us - 0); int16 @c = (0xABCDull + 1 - 2 * 5 / 7 % 9 + @a + @b); int @d /= @a;");
    validateTrue(semanticiser.parse());
    SixteenHighDefinition definition = semanticiser.getDefinition();
    UnitDefinition unitDefinition = definition.getUnit("testComplex");

    validateEquals(12, unitDefinition.numVariables(true));
    validateEquals(3, unitDefinition.numVariables(false));
    validateEquals(9, unitDefinition.numAutoVariables());

    validateVariable(unitDefinition.getVariable("@a"), "@a", unit, false, int32);
    validateVariable(unitDefinition.getVariable("@b"), "@b", unit, false, int32);
    validateVariable(unitDefinition.getVariable("@b~0"), "@b~0", unit, true, int32);
    validateVariable(unitDefinition.getVariable("@b~1"), "@b~1", unit, true, int32);
    validateVariable(unitDefinition.getVariable("@c"), "@c", unit, false, int16);
    validateVariable(unitDefinition.getVariable("@c~0"), "@c~0", unit, true, int64);
    validateVariable(unitDefinition.getVariable("@c~1"), "@c~1", unit, true, int32);
    validateVariable(unitDefinition.getVariable("@c~2"), "@c~2", unit, true, int32);
    validateVariable(unitDefinition.getVariable("@c~3"), "@c~3", unit, true, int32);
    validateVariable(unitDefinition.getVariable("@c~4"), "@c~4", unit, true, int64);
    validateVariable(unitDefinition.getVariable("@c~5"), "@c~5", unit, true, int64);
    validateVariable(unitDefinition.getVariable("@c~6"), "@c~6", unit, true, int64);

    List<Triple> triples = unitDefinition.getTriples();
    validateEquals(12, triples.size());
    for (int i = 0; i < triples.size(); i++)
    {
      Triple triple = triples.get(i);
      System.out.println(triple.printValidationCode(i));
    }
    validateTripleVariableLiteral(triples.get(0), int8, "@a", int8, "-2", multiply, int8, "3");
    validateTripleVariableVariable(triples.get(1), int8, "@a~1", int8, "@y", modulus, int8, "@y");
    validateTripleVariableVariable(triples.get(2), int8, "@a~2", int8, "@a~0", add, int8, "@a~1");
    validateTripleVariableVariable(triples.get(3), int32, "@a", null, null, null, int8, "@a~2");
  }

  private static void validateTripleVariableLiteral(Triple triple,
                                                    PrimitiveTypeCode leftType,
                                                    String leftVariableName,
                                                    PrimitiveTypeCode rightOneType,
                                                    String rightOneVariableName,
                                                    OperatorCode operator,
                                                    PrimitiveTypeCode rightTwoType,
                                                    String rightTwoLiteralString)
  {
    validateVariable(leftType, leftVariableName, triple.getLeft());
    validateVariableOrNull(rightOneType, rightOneVariableName, triple.getRightOne());
    validateOperatorOrNull(triple, operator);
    validateLiteral(rightTwoType, rightTwoLiteralString, triple.getRightTwo());
  }

  private static void validateTripleLiteralLiteral(Triple triple,
                                                    PrimitiveTypeCode leftType,
                                                    String leftVariableName,
                                                    PrimitiveTypeCode rightOneType,
                                                    String rightOneVariableName,
                                                    OperatorCode operator,
                                                    PrimitiveTypeCode rightTwoType,
                                                    String rightTwoLiteralString)
  {
    validateVariable(leftType, leftVariableName, triple.getLeft());
    validateLiteral(rightOneType, rightOneVariableName, triple.getRightOne());
    validateOperatorOrNull(triple, operator);
    validateLiteral(rightTwoType, rightTwoLiteralString, triple.getRightTwo());
  }

  private static void validateOperatorOrNull(Triple triple, OperatorCode operator)
  {
    if (operator != null)
    {
      validateEquals(operator, triple.getOperator());
    }
    else
    {
      validateNull(operator);
    }
  }

  private static void validateVariable(PrimitiveTypeCode type, String name, TripleValue tripleValue)
  {
    TripleVariable variable = tripleValue.getVariable();
    validateNotNull(variable);
    validateEquals(name, variable.getName());
    validateEquals(type, variable.getTypeCode());
  }

  private static void validateVariableOrNull(PrimitiveTypeCode rightOneType, String rightOneVariableName, TripleValue tripleValue)
  {
    if (rightOneVariableName != null)
    {
      validateVariable(rightOneType, rightOneVariableName, tripleValue);
    }
    else
    {
      validateNull(tripleValue);
    }
  }

  private static void validateLiteral(PrimitiveTypeCode type, String value, TripleValue tripleValue)
  {
    TripleLiteral literal = tripleValue.getLiteral();
    validateNotNull(literal);
    validateEquals(value, literal.print());
    validateEquals(type, literal.getTypeCode());
  }

  private static void validateTripleVariableVariable(Triple triple,
                                                     PrimitiveTypeCode leftType,
                                                     String leftVariableName,
                                                     PrimitiveTypeCode rightOneType,
                                                     String rightOneVariableName,
                                                     OperatorCode operator,
                                                     PrimitiveTypeCode rightTwoType,
                                                     String rightTwoVariableName)
  {
    validateVariable(leftType, leftVariableName, triple.getLeft());
    validateVariableOrNull(rightOneType, rightOneVariableName, triple.getRightOne());
    validateOperatorOrNull(triple, operator);
    validateVariable(rightTwoType, rightTwoVariableName, triple.getRightTwo());
  }

  private static void validateVariable(VariableDefinition variable,
                                       String name,
                                       Scope unit,
                                       boolean auto,
                                       PrimitiveTypeCode typeCode)
  {
    validateEquals(name, variable.name);
    validateEquals(unit, variable.scope);
    validateEquals(auto, variable.auto);
    validateEquals(typeCode, variable.type.getType());
  }

  protected static void testArray()
  {
    SixteenHighSemanticiser semanticiser = createSixteenHighSemanticiser("testArray", "int32 @[100]; int32 @b; int 32 @c; @a[(@c * (@b + 0x3L))] = (5 + @b * @c + 99 * 1.05);");
    validateTrue(semanticiser.parse());
  }

  private static SixteenHighSemanticiser createSixteenHighSemanticiser(String filename, String fileContents)
  {
    SixteenHighKeywords keywords = new SixteenHighKeywords();
    SixteenHighTokeniser tokeniser = createParser(filename, fileContents, keywords);
    ParseResult parseResult = tokeniser.parse();
    validateTrue(parseResult.isTrue());
    SixteenHighDefinition sixteenHighDefinition = new SixteenHighDefinition();
    return new SixteenHighSemanticiser(sixteenHighDefinition, tokeniser.getUnit(), keywords);
  }

  private static SixteenHighTokeniser createParser(String filename, String contents, SixteenHighKeywords keywords)
  {
    return new SixteenHighTokeniser(new Logger(),
                                    keywords,
                                    filename,
                                    new TokenUnit(filename),
                                    contents);
  }

  private static void testAutoCast()
  {
    List<PrimitiveTypeCode> primitiveTypeCodes = CollectionUtil.newList(int8,
                                                                        uint8,
                                                                        int16,
                                                                        uint16,
                                                                        int32,
                                                                        uint32,
                                                                        int64,
                                                                        uint64,
                                                                        float16,
                                                                        float32,
                                                                        float64,
                                                                        float128,
                                                                        bool);

    for (PrimitiveTypeCode i : primitiveTypeCodes)
    {
      for (PrimitiveTypeCode j : primitiveTypeCodes)
      {
        PrimitiveTypeCode code = SixteenHighTypeMap.getInstance().getAutoCast(i, j);
        validateNotNull(code);
      }
    }
  }

  public static void test()
  {
    testAutoCast();
    testSimple1();
    testSimple2();
    testComplex1();
    testComplex2();
    testComplex3();
    testArray();
  }
}

