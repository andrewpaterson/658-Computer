package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.definition.SixteenHighDefinition;
import net.assembler.sixteenhigh.tokeniser.ParseResult;
import net.assembler.sixteenhigh.tokeniser.SixteenHighTokeniser;
import net.common.logger.Logger;

import static net.logicim.assertions.Validator.validateTrue;

public abstract class SemanticiserTest
{
  protected static void testSimple()
  {
    SixteenHighKeywords keywords = new SixteenHighKeywords();
    SixteenHighTokeniser tokeniser = createParser("int32 @a = 5;", keywords);
    ParseResult parseResult = tokeniser.parse();
    validateTrue(parseResult.isTrue());
    SixteenHighDefinition sixteenHighDefinition = new SixteenHighDefinition();
    SixteenHighSemanticiser semanticiser = new SixteenHighSemanticiser(sixteenHighDefinition, tokeniser.getUnit(), keywords);
    semanticiser.parse();
  }

  protected static void testComplex()
  {
    SixteenHighKeywords keywords = new SixteenHighKeywords();
    SixteenHighTokeniser tokeniser = createParser("int32 @a = (+c * 3 + (x - y));", keywords);
    ParseResult parseResult = tokeniser.parse();
    validateTrue(parseResult.isTrue());
    SixteenHighDefinition sixteenHighDefinition = new SixteenHighDefinition();
    SixteenHighSemanticiser semanticiser = new SixteenHighSemanticiser(sixteenHighDefinition, tokeniser.getUnit(), keywords);
    semanticiser.parse();
  }

  private static SixteenHighTokeniser createParser(String contents, SixteenHighKeywords keywords)
  {
    return new SixteenHighTokeniser(new Logger(),
                                    keywords,
                                    "",
                                    new TokenUnit(""),
                                    contents);
  }

  public static void test()
  {
//    testSimple();
    testComplex();
  }
}

