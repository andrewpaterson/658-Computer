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
    SixteenHighSemanticiser semanticiser = createSixteenHighSemanticiser("int32 @a = 5;");
    semanticiser.parse();
  }

  protected static void testComplex()
  {
    SixteenHighSemanticiser semanticiser = createSixteenHighSemanticiser("int32 @a = (+c * 3 + (x - y));");
    semanticiser.parse();
  }

  protected static void testArray()
  {
    SixteenHighSemanticiser semanticiser = createSixteenHighSemanticiser("@a[(c * (b + 0x3L))] = (5 + b * c + 99 * 1.05);");
    semanticiser.parse();
  }

  private static SixteenHighSemanticiser createSixteenHighSemanticiser(String s)
  {
    SixteenHighKeywords keywords = new SixteenHighKeywords();
    SixteenHighTokeniser tokeniser = createParser(s, keywords);
    ParseResult parseResult = tokeniser.parse();
    validateTrue(parseResult.isTrue());
    SixteenHighDefinition sixteenHighDefinition = new SixteenHighDefinition();
    return new SixteenHighSemanticiser(sixteenHighDefinition, tokeniser.getUnit(), keywords);
  }

  private static SixteenHighTokeniser createParser(String contents, SixteenHighKeywords keywords)
  {
    return new SixteenHighTokeniser(new Logger(),
                                    keywords,
                                    "",
                                    new TokenUnit(""),
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
//    testSimple();
    testComplex();
    testArray();
  }
}

