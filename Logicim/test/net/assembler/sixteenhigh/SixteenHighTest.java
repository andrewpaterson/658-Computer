package net.assembler.sixteenhigh;

import net.assembler.sixteenhigh.semanticiser.SemanticiserTest;
import net.assembler.sixteenhigh.tokeniser.TokeniserTest;
import net.assembler.sixteenhigh.tokeniser.literal.LiteralParserTest;

public class SixteenHighTest
{
  public static void main(String[] args)
  {
    LiteralParserTest.test();
    TokeniserTest.test();
    SemanticiserTest.test();
  }
}

