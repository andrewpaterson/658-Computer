package net.assembler.sixteenhigh.parser;

import net.common.util.FileUtil;

import java.io.File;

public class AssemblerTest
{
  protected static void testSimple()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    String contents = FileUtil.readFile(new File("Simple.16h"));
    SixteenHighParser parser = new SixteenHighParser(log, "testSimple", context, contents);
    parser.parse();
  }

  protected static void testPointers()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    new SixteenHighParser(log, "testPointers", context,
                          "@routine:\n" +
                          "       int16 a; ptr b; int8 d;\n" +
                          "\n" +
                          "       d = 5;\n" +
                          "       a = 10\n" +
                          "       b = 0x001000\n" +
                          "       int8 c\n" +
                          "       c = 0\n" +
                          "label:\n" +
                          "       b[c] = a\n" +
                          "       c++\n" +
                          "       c ?- d\n" +
                          "       if< go label\n" +
                          "\n" +
                          "       b = 0x000000\n" +
                          "       b[2] = (int8)7\n" +
                          "end\n" +
                          "\n" +
                          "$int8 hello = \"Hello\"\n" +
                          "$int8 ten_numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}\n" +
                          "\n" +
                          "@another\n" +
                          "       ptr p;\n" +
                          "       \n" +
                          "       p = ten_numbers\n" +
                          "       p = *p[5]\n" +
                          "@@main:\n" +
                          "       gosub @sum_even_and_odd\n" +
                          "       return 0\n" +
                          "end");
  }

  public static void main(String[] args)
  {
    testSimple();
    testPointers();
  }
}

