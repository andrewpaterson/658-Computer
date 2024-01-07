package net.assembler.sixteenhigh;

import net.assembler.sixteenhigh.semanticiser.SemanticiserTest;
import net.assembler.sixteenhigh.tokeniser.TokeniserTest;
import net.assembler.sixteenhigh.tokeniser.literal.LiteralParserTest;
import net.assembler.sixteenhigh.tokeniser.precedence.TokenPrecedenceTest;

public class SixteenHighTest
{
  public static void main(String[] args)
  {
    LiteralParserTest.test();
    TokeniserTest.test();
    TokenPrecedenceTest.test();
    SemanticiserTest.test();
  }
}

