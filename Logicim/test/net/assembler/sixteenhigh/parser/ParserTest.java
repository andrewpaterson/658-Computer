package net.assembler.sixteenhigh.parser;

import net.common.parser.Tristate;
import net.common.reflect.ClassInspector;
import net.common.util.EnvironmentInspector;
import net.common.util.FileUtil;
import net.logicim.assertions.ValidationException;

import java.io.File;

import static net.logicim.assertions.Validator.*;

public class ParserTest
{
  protected static void testStatementAssignment1()
  {
    SixteenHighParser parser = createParser("i = 0");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("i = 0", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testStatementAssignment2()
  {
    SixteenHighParser parser = createParser("i = (c * 3)");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("i = (c * 3)", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testStatementAssignment3()
  {
    SixteenHighParser parser = createParser("i = -(0x230L * +((-.4f) % 3))");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("i = -(560 * +(-0.4F % 3))", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testStatementAssignment4()
  {
    SixteenHighParser parser = createParser("c = -d");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("c = -d", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testStatementAssignment5()
  {
    SixteenHighParser parser = createParser("c = ~(5)");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("c = ~(5)", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testPlusExpression()
  {
    SixteenHighParser parser = createParser("x = (5 + ~d)");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("x = (5 + ~d)", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testPull()
  {
    SixteenHighParser parser = createParser("x< *p[2]< z<;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("x<\n" +
             "*p[2]<\n" +
             "z<;\n", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testPush()
  {
    SixteenHighParser parser = createParser(">x >*p[2] >y >1;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("> x\n" +
             "> *p[2]\n" +
             "> y\n" +
             "> 1;\n", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testUnsignedShiftRightExpression()
  {
    SixteenHighParser parser = createParser("x = (5 + >>>d)");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("x = (5 + >>>d)", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testStatementDeclaration()
  {
    SixteenHighParser parser = createParser("int16 i; int16 number;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("int16 i;\nint16 number;\n", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testArrayInitialisation()
  {
    SixteenHighParser parser = createParser("int8[6] a1d = [2,3,1,3,1,2]");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("int8[6] a1d = [2, 3, 1, 3, 1, 2]", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testArrayIndices()
  {
    SixteenHighParser parser = createParser("a[2] = b[3]");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("a[2] = b[3]", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testSingleInitialisation()
  {
    SixteenHighParser parser = createParser("int8 i = 5");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("int8 i = 5", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testComplexArrayIndices()
  {
    SixteenHighParser parser = createParser("int32 c = (a2d[y][a1d[y]] * 3);");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("int32 c = (a2d[y][a1d[y]] * 3);", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testIdentifierContainingNumber()
  {
    SixteenHighParser parser = createParser("a1d[2] =66;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("a1d[2] = 66;", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testGlobalAndFileVariables()
  {
    SixteenHighParser parser = createParser("int8*\t@hello;int8\t@@world;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("int8* @hello;\n" +
             "int8 @@world;\n", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testReferenceOperator()
  {
    SixteenHighParser parser = createParser("p = &p[5]");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("p = &p[5]", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testPullAfterRegisterDeclaration()
  {
    SixteenHighParser parser = createParser("int8* address <");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("int8* address<", code.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testSimple()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    SixteenHighParser parser = createParser("Simple.16h", log, context);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validateTrue(parser.isCompleted());
    String s = code.print(parser.getKeywords());
    validate("@sum_even_and_odd\n" +
             "   int16 i;\n" +
             "   int16 number;\n" +
             "   int16 even;\n" +
             "   int16 odd\n" +
             "   number = 10;\n" +
             "   i = 0;\n" +
             "   even = 0;\n" +
             "   odd = 0\n" +
             "   bool b\n" +
             "loop:\n" +
             "   b = i\n" +
             "   b %= 2\n" +
             "   b ?\n" +
             "   if= go even_\n" +
             "   odd += i\n" +
             "   go done\n" +
             "even_:\n" +
             "   even += i\n" +
             "done:\n" +
             "   i++;\n" +
             "   number ?- i\n" +
             "   if> go loop\n" +
             "   > odd\n" +
             "   > even\n" +
             "   gosub @print_pair\n" +
             "   return\n" +
             "end\n" +
             "\n" +
             "@@main\n" +
             "   gosub @sum_even_and_odd\n" +
             "   return\n" +
             "end\n", s);
  }

  protected static void testPointers()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    SixteenHighParser parser = createParser("Pointer.16h", log, context);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validateTrue(parser.isCompleted());
    String s = code.print(parser.getKeywords());
    validate("@routine\n" +
             "   int16 a;\n" +
             "   int16* b;\n" +
             "   int8 d;\n" +
             "   uint32 e = 85;\n" +
             "   d = 5;\n" +
             "   a = 10\n" +
             "   b = 4096\n" +
             "   int8 c\n" +
             "   c = 0\n" +
             "label:\n" +
             "   b[c] = a\n" +
             "   c++\n" +
             "   c ?- d\n" +
             "   if< go label\n" +
             "   b = 0\n" +
             "   b[2] = 7\n" +
             "end\n" +
             "\n" +
             "int8* @hello = \"Hello\"\n" +
             "int8* @ten_numbers = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]\n" +
             "int8* @vid_mem = 49152\n" +
             "@another\n" +
             "   int8* p;\n" +
             "   p = ten_numbers\n" +
             "   p = &p[5]\n" +
             "   int8 a\n" +
             "   a = p[0]\n" +
             "   a = *p\n" +
             "   int32 b\n" +
             "   b = ten_numbers\n" +
             "end\n" +
             "\n" +
             "@@main\n" +
             "   gosub @routine\n" +
             "   gosub @another\n" +
             "   return\n" +
             "end\n", s);
  }

  private static void testArrayDeclaration()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    SixteenHighParser parser = createParser("Array.16h", log, context);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validateTrue(parser.isCompleted());
    String s = code.print(parser.getKeywords());
    validate("@@main\n" +
             "   uint16 x = 3\n" +
             "   uint16 y = 2\n" +
             "   uint8[3][5] a2d = [[0, 0, 0, 0, 0][1, 1, 1, 1, 1][2, 2, 2, 2, 2]]\n" +
             "   int8[6] a1d = [2, 3, 1, 3, 1, 2]\n" +
             "   int32 c = (a2d[y][a1d[y]] * 3);\n" +
             "   a1d[2] = 66;\n" +
             "   a2d[y][x] = 22;\n" +
             "end\n", s);
  }

  private static void testExpressions()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    SixteenHighParser parser = createParser("Expressions.16h", log, context);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validateTrue(parser.isCompleted());
    String s = code.print(parser.getKeywords());
    validate("@@main\n" +
             "   uint16 c;\n" +
             "   uint16 d\n" +
             "   int32 x = (c + d)\n" +
             "   int64 @@q\n" +
             "   x += ~(5)\n" +
             "   c = -d\n" +
             "   c++;\n" +
             "   x = ~x\n" +
             "   x = (5 + ~d)\n" +
             "   x = (5 + >>>d)\n" +
             "   d <<= d\n" +
             "   c >>>= d;\n" +
             "   @@q = (d * c)\n" +
             "   d = (c + (5 * x) - 1)\n" +
             "   > x\n" +
             "   > c\n" +
             "   > d\n" +
             "   return\n" +
             "end\n" +
             "\n" +
             "@array\n" +
             "   uint8* xxx\n" +
             "   int32 y = 3\n" +
             "   y = (y + y - y + y - y)\n" +
             "   xxx[0] = 1\n" +
             "   xxx[1] = 2\n" +
             "   xxx[2] = 3\n" +
             "   xxx[3] = 0\n" +
             "   xxx[(xxx[y] + y)] = 2\n" +
             "end\n", s);
  }

  private static void testStack()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    SixteenHighParser parser = createParser("Stack.16h", log, context);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validateTrue(parser.isCompleted());
    String s = code.print(parser.getKeywords());
    validate("$start_address 0x800\n" +
             "@count_pointless\n" +
             "   int16 start\n" +
             "   int16 _end\n" +
             "   int8 value\n" +
             "   start<\n" +
             "   _end<\n" +
             "   int8* address<\n" +
             "   int16 i = start;\n" +
             "loop:\n" +
             "   address[i] = value;\n" +
             "   i++\n" +
             "   i ?- (_end + 3)\n" +
             "   if<= go loop\n" +
             "   i -= start\n" +
             "   > i\n" +
             "   return\n" +
             "end\n" +
             "\n" +
             "@@main\n" +
             "   > 9\n" +
             "   > 23\n" +
             "   > 0\n" +
             "   gosub count_pointless\n" +
             "   int16 count<\n" +
             "   > 9\n" +
             "   > 23\n" +
             "   > 0\n" +
             "   gosub count_pointless\n" +
             "   count<\n" +
             "   int32 number;\n" +
             "   int32 another\n" +
             "   number = 5\n" +
             "   another = 6\n" +
             "   number = (number + ((3 + 6) * another) / 2)\n" +
             "   number = (number + temp1)\n" +
             "end\n", s);
  }

  private static SixteenHighParser createParser(String filename, TextParserLog log, SixteenHighContext context)
  {
    ClassInspector classInspector = ClassInspector.forClass(ParserTest.class);
    String packageName = classInspector.getPackageName();
    String programDir = EnvironmentInspector.getProgramDir();
    String fullFilename = programDir.replace('\\', '/') + "/test/" + packageName.replace('.', '/') + "/" + filename;
    String contents = FileUtil.readFile(new File(fullFilename));
    return new SixteenHighParser(log, filename, context, contents);
  }

  private static SixteenHighParser createParser(String contents)
  {
    return new SixteenHighParser(new TextParserLog(), "", new SixteenHighContext(), contents);
  }

  private static void validateNoError(Tristate result, String error)
  {
    if (result != Tristate.TRUE)
    {
      throw new ValidationException(error);
    }
  }

  public static void test()
  {
    testStatementAssignment1();
    testStatementAssignment2();
    testStatementAssignment3();
    testStatementAssignment4();
    testStatementAssignment5();
    testStatementDeclaration();
    testSingleInitialisation();
    testArrayInitialisation();
    testArrayIndices();
    testComplexArrayIndices();
    testIdentifierContainingNumber();
    testGlobalAndFileVariables();
    testReferenceOperator();
    testPush();
    testPull();
    testPlusExpression();
    testUnsignedShiftRightExpression();
    testPullAfterRegisterDeclaration();

    testSimple();
    testArrayDeclaration();
    testPointers();
    testExpressions();
    testStack();
  }
}

