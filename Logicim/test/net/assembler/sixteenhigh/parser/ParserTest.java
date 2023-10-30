package net.assembler.sixteenhigh.parser;

import net.common.parser.Tristate;
import net.common.reflect.ClassInspector;
import net.common.util.EnvironmentInspector;
import net.common.util.FileUtil;

import java.io.File;

import static net.logicim.assertions.Validator.validate;
import static net.logicim.assertions.Validator.validateNotNull;

public class ParserTest
{
  protected static void testStatements()
  {
    SixteenHighParser parser = createParser("i = 0");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validate(Tristate.TRUE, result);
    Code code = parser.getCode();
    validateNotNull(code);
  }

  protected static void testSimple()
  {
    TextParserLog log = new TextParserLog();
    SixteenHighContext context = new SixteenHighContext();
    SixteenHighParser parser = createParser("Simple.16h", log, context);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validate(Tristate.TRUE, result);
    Code code = parser.getCode();
    validateNotNull(code);
    code.dump(parser.getKeywords());
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
    testStatements();
    testSimple();
    testPointers();
  }
}

