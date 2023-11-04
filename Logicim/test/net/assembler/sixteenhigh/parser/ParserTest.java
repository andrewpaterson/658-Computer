package net.assembler.sixteenhigh.parser;

import net.common.parser.Tristate;
import net.common.reflect.ClassInspector;
import net.common.util.EnvironmentInspector;
import net.common.util.FileUtil;
import net.logicim.assertions.ValidationException;

import java.io.File;

import static net.logicim.assertions.Validator.validate;
import static net.logicim.assertions.Validator.validateNotNull;

public class ParserTest
{
  protected static void testStatementAssignment()
  {
    SixteenHighParser parser = createParser("i = 0");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("i = 0", code.print(parser.getKeywords()));
  }

  protected static void testStatementDeclaration()
  {
    SixteenHighParser parser = createParser("int16 i; int16 number;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("int16 i\nint16 number\n", code.print(parser.getKeywords()));
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
    testStatementAssignment();
    testStatementDeclaration();
    testSingleInitialisation();
    testArrayInitialisation();

    testSimple();
    testArrayDeclaration();
    testPointers();
  }
}

