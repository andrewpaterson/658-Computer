package net.assembler.sixteenhigh.parser;

import net.common.util.FileUtil;

import java.io.File;

public class AssemblerTest
{
  protected static void testSimple()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    SixteenHighParser parser = createParser("Simple.16h", log, context);
    parser.parse();
  }

  protected static void testPointers()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    SixteenHighParser parser = createParser("Pointer.16h", log, context);
    parser.parse();
  }

  private static SixteenHighParser createParser(String filename, TextParserLog log, SixteenHighContext context)
  {
    String contents = FileUtil.readFile(new File(filename));
    return new SixteenHighParser(log, filename, context, contents);
  }

  public static void main(String[] args)
  {
    testSimple();
    testPointers();
  }
}

