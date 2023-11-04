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
    validate(Tristate.TRUE, result);
    Code code = parser.getCode();
    validateNotNull(code);
  }

  protected static void testStatementDeclaration()
  {
    SixteenHighParser parser = createParser("int16 i; int16 number;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validate(Tristate.TRUE, result);
    Code code = parser.getCode();
    validateNotNull(code);
  }

  protected static void testArrayInitialisation()
  {
    SixteenHighParser parser = createParser("int8[6] a1d = [2,3,1,3,1,2]");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    Code code = parser.getCode();
    validateNotNull(code);
    validate("int8[6] a1d = [2,3,1,3,1,2]", code.print(parser.getKeywords()));
  }

  private static void validateNoError(Tristate result, String error)
  {
    if (result != Tristate.TRUE)
    {
      throw new ValidationException(error);
    }
  }

  protected static void testSimple()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    SixteenHighParser parser = createParser("Simple.16h", log, context);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    Code code = parser.getCode();
    validateNotNull(code);
    validate(Tristate.TRUE, result);
  }

  protected static void testPointers()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    SixteenHighParser parser = createParser("Pointer.16h", log, context);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validate(Tristate.TRUE, result);
    Code code = parser.getCode();
    validateNotNull(code);
    code.dump(parser.getKeywords());
  }

  private static void testArrayDeclaration()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    SixteenHighParser parser = createParser("Array.16h", log, context);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    Code code = parser.getCode();
    validateNotNull(code);
    code.dump(parser.getKeywords());
    String error = parser.getError();
    System.out.println(error);
    validate(Tristate.TRUE, result);
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

  public static void test()
  {
//    testStatementAssignment();
//    testStatementDeclaration();
    testArrayInitialisation();

//    testSimple();
//    testArrayDeclaration();
//    testPointers();
  }
}

