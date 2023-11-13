package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.ParseResult;
import net.assembler.sixteenhigh.tokeniser.SixteenHighTokeniser;
import net.assembler.sixteenhigh.tokeniser.TextParserLog;

import static net.logicim.assertions.Validator.*;

public abstract class SemanticiserTest
{
  protected static void testSimple()
  {
    SixteenHighKeywords keywords = new SixteenHighKeywords();
    SixteenHighTokeniser tokeniser = createParser("int32 a = 5;", keywords);
    ParseResult parseResult = tokeniser.parse();
    validateTrue(parseResult.isTrue());
    SixteenHighSemanticiser semanticiser = new SixteenHighSemanticiser(tokeniser.getStatements(), keywords);
    semanticiser.parse();
  }

  private static SixteenHighTokeniser createParser(String contents, SixteenHighKeywords keywords)
  {
    return new SixteenHighTokeniser(new TextParserLog(),
                                    keywords,
                                    "",
                                    new Statements(""),
                                    contents);
  }

  public static void test()
  {
    testSimple();
  }
}

