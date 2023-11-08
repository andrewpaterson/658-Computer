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
    validate("", code.print(parser.getKeywords()));
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
    testStatementDeclaration();
    testSingleInitialisation();
    testArrayInitialisation();
    testArrayIndices();
    testComplexArrayIndices();
    testIdentifierContainingNumber();
    testGlobalAndFileVariables();
    testReferenceOperator();

    testSimple();
    testArrayDeclaration();
    testPointers();
  }
}

