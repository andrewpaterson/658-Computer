package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.definition.SixteenHighDefinition;
import net.assembler.sixteenhigh.semanticiser.expression.operator.SixteenHighTypeMap;
import net.assembler.sixteenhigh.tokeniser.ParseResult;
import net.assembler.sixteenhigh.tokeniser.SixteenHighTokeniser;
import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;
import net.common.logger.Logger;
import net.common.util.CollectionUtil;

import java.util.List;

import static net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode.*;
import static net.logicim.assertions.Validator.validateNotNull;
import static net.logicim.assertions.Validator.validateTrue;

public abstract class SemanticiserTest
{
  protected static void testSimple()
  {
    SixteenHighSemanticiser semanticiser = createSixteenHighSemanticiser("testSimple", "int32 @@a = 5;");
    validateTrue(semanticiser.parse());
    SixteenHighDefinition definition = semanticiser.getDefinition();
    VariableDefinition globalVariable = definition.getGlobalVariable("@@a");
    validateNotNull(globalVariable);
  }

  protected static void testComplex()
  {
    SixteenHighSemanticiser semanticiser = createSixteenHighSemanticiser("testComplex", "int32 @c; int32 @x; int32 @y; int32 @a = (+@c * 3 + (@x - @y));");
    validateTrue(semanticiser.parse());
    SixteenHighDefinition definition = semanticiser.getDefinition();
    UnitDefinition unit = definition.getUnit("testComplex");
    unit.getVariable("@c");
    xxx
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
//    testAutoCast();
//    testSimple();
    testComplex();
//    testArray();
  }
}

