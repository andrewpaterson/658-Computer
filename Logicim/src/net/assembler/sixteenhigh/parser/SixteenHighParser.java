package net.assembler.sixteenhigh.parser;

import net.common.parser.StringZero;
import net.common.parser.TextParser;

public class SixteenHighParser
{
  protected SixteenHighKeywords keywords;

  public SixteenHighParser(String source)
  {
    keywords = new SixteenHighKeywords();
    TextParser textParser = new TextParser(source);
    textParser.getIdentifier(new StringZero());
  }
}
