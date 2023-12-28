package net.assembler.sixteenhigh.tokeniser;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.literal.CTInt;
import net.assembler.sixteenhigh.tokeniser.literal.CTLiteral;
import net.assembler.sixteenhigh.tokeniser.statment.AssignmentTokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.TokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.expression.*;
import net.common.logger.Logger;
import net.common.parser.Tristate;
import net.common.reflect.ClassInspector;
import net.common.util.EnvironmentInspector;
import net.common.util.FileUtil;
import net.logicim.assertions.ValidationException;

import java.io.File;
import java.util.List;

import static net.logicim.assertions.Validator.*;

public abstract class TokeniserTest
{
  protected static void testStatementAssignment1()
  {
    SixteenHighTokeniser parser = createParser("i = 0");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("i = 0", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testStatementAssignment2()
  {
    SixteenHighTokeniser parser = createParser("i = (c * 3)");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("i = (c * 3)", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());

    List<TokenStatement> statements = unit.getStatements();
    validate(1, statements.size());
    TokenStatement tokenStatement = statements.get(0);
    validateClass(AssignmentTokenStatement.class, tokenStatement);
    AssignmentTokenStatement assignmentTokenStatement = (AssignmentTokenStatement) tokenStatement;

    VariableTokenExpression leftExpression = assignmentTokenStatement.getLeftVariableExpression();
    validateEquals(0, leftExpression.getDereferenceCount());
    validateFalse(leftExpression.isReference());
    List<VariableMember> members = leftExpression.getMembers();
    validateEquals(1, members.size());
    VariableMember variableMember = members.get(0);
    validateEquals("i", variableMember.getIdentifier());
    validateEquals(0, variableMember.getArrayIndices().size());

    validateEquals(SixteenHighKeywordCode.assign, assignmentTokenStatement.getKeyword());

    TokenExpressionList rightExpressions = assignmentTokenStatement.getRightExpressions();
    List<TokenExpression> expressions = rightExpressions.getExpressions();
    validateEquals(3, expressions.size());

    TokenExpression firstRightExpression = expressions.get(0);
    validateClass(VariableTokenExpression.class, firstRightExpression);
    VariableTokenExpression variableTokenExpression = (VariableTokenExpression) firstRightExpression;
    validateEquals(0, variableTokenExpression.getDereferenceCount());
    validateFalse(variableTokenExpression.isReference());
    List<VariableMember> members2 = variableTokenExpression.getMembers();
    validateEquals(1, members2.size());
    VariableMember variableMember2 = members2.get(0);
    validateEquals("c", variableMember2.getIdentifier());
    validateEquals(0, variableMember2.getArrayIndices().size());

    TokenExpression secondRightExpression = expressions.get(1);
    validateClass(OperandTokenExpression.class, secondRightExpression);
    OperandTokenExpression operandTokenExpression = (OperandTokenExpression) secondRightExpression;
    validateEquals(SixteenHighKeywordCode.multiply, operandTokenExpression.getOperand());

    TokenExpression thirdRightExpression = expressions.get(2);
    validateClass(LiteralTokenExpression.class, thirdRightExpression);
    LiteralTokenExpression literalTokenExpression = (LiteralTokenExpression) thirdRightExpression;
    CTLiteral literal = literalTokenExpression.getLiteral();
    validateClass(CTInt.class, literal);
    CTInt ctInt = (CTInt) literal;
    validateEquals(3L, ctInt.getValue());
  }

  protected static void testStatementAssignment3()
  {
    SixteenHighTokeniser parser = createParser("i = -(0x230L * +((-.4f) % 3))");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("i = -(560 * +(-0.4F % 3))", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testStatementAssignment4()
  {
    SixteenHighTokeniser parser = createParser("c = -d");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("c = -d", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testStatementAssignment5()
  {
    SixteenHighTokeniser parser = createParser("c = ~(5)");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("c = ~(5)", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testPlusExpression()
  {
    SixteenHighTokeniser parser = createParser("x = (5 + ~d)");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("x = (5 + ~d)", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testPull()
  {
    SixteenHighTokeniser parser = createParser("x< *p[2]< z<;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("x<\n" +
             "*p[2]<\n" +
             "z<;\n", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testPush()
  {
    SixteenHighTokeniser parser = createParser(">x >*p[2] >y >1;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("> x\n" +
             "> *p[2]\n" +
             "> y\n" +
             "> 1;\n", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testUnsignedShiftRightExpression()
  {
    SixteenHighTokeniser parser = createParser("x = (5 + >>>d)");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("x = (5 + >>>d)", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testStatementDeclaration()
  {
    SixteenHighTokeniser parser = createParser("int16 i; int16 number;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("int16 i;\nint16 number;\n", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testStatementDirective()
  {
    SixteenHighTokeniser parser = createParser("$access_mode read-write; $access_time 0x3");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("$access_mode read-write\n" +
             "$access_time 3\n", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testArrayInitialisation()
  {
    SixteenHighTokeniser parser = createParser("int8[6] a1d = [2,3,1,3,1,2]");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("int8[6] a1d = [2, 3, 1, 3, 1, 2]", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testArrayIndices()
  {
    SixteenHighTokeniser parser = createParser("a[2] = b[3]");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("a[2] = b[3]", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testSingleInitialisation()
  {
    SixteenHighTokeniser parser = createParser("int8 i = 5");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("int8 i = 5", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testComplexArrayIndices()
  {
    SixteenHighTokeniser parser = createParser("int32 c = (a2d[y][a1d[y]] * 3);");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("int32 c = (a2d[y][a1d[y]] * 3);", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testIdentifierContainingNumber()
  {
    SixteenHighTokeniser parser = createParser("a1d[2] =66;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("a1d[2] = 66;", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testGlobalAndFileVariables()
  {
    SixteenHighTokeniser parser = createParser("int8*\t@hello;int8\t@@world;");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("int8* @hello;\n" +
             "int8 @@world;\n", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testReferenceOperator()
  {
    SixteenHighTokeniser parser = createParser("p = &p[5]");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("p = &p[5]", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testPullAfterRegisterDeclaration()
  {
    SixteenHighTokeniser parser = createParser("int8* address <");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("int8* address<", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testStructSimpleDeclaration()
  {
    SixteenHighTokeniser parser = createParser("struct @party int8* animal end");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("struct @party\n" +
             "   int8* animal\n" +
             "end\n", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  private static void testStructFieldSelection()
  {
    SixteenHighTokeniser parser = createParser("@party_address.line[1] = @hello");
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validate("@party_address.line[1] = @hello", unit.print(parser.getKeywords()));
    validateTrue(parser.isCompleted());
  }

  protected static void testSimple()
  {
    Logger log = new Logger();
    SixteenHighTokeniser parser = createParser("Simple.16h", log);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validateTrue(parser.isCompleted());
    String s = unit.print(parser.getKeywords());
    validate("@sum_even_and_odd:\n" +
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
             "@@main:\n" +
             "   gosub @sum_even_and_odd\n" +
             "   return\n" +
             "end\n", s);
  }

  protected static void testPointers()
  {
    Logger log = new Logger();
    SixteenHighTokeniser parser = createParser("Pointer.16h", log);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validateTrue(parser.isCompleted());
    String s = unit.print(parser.getKeywords());
    validate("@routine:\n" +
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
             "@another:\n" +
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
             "@@main:\n" +
             "   gosub @routine\n" +
             "   gosub @another\n" +
             "   return\n" +
             "end\n", s);
  }

  private static void testArrayDeclaration()
  {
    Logger log = new Logger();
    SixteenHighTokeniser parser = createParser("Array.16h", log);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validateTrue(parser.isCompleted());
    String s = unit.print(parser.getKeywords());
    validate("@@main:\n" +
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
    Logger log = new Logger();
    SixteenHighTokeniser parser = createParser("Expressions.16h", log);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validateTrue(parser.isCompleted());
    String s = unit.print(parser.getKeywords());
    validate("@@main:\n" +
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
             "@array:\n" +
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
    Logger log = new Logger();
    SixteenHighTokeniser parser = createParser("Stack.16h", log);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validateTrue(parser.isCompleted());
    String s = unit.print(parser.getKeywords());
    validate("$start_address 0x800\n" +
             "\n" +
             "@count_pointless:\n" +
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
             "@@main:\n" +
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

  private static void testStruct()
  {
    Logger log = new Logger();
    SixteenHighTokeniser parser = createParser("Struct.16h", log);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validateTrue(parser.isCompleted());
    String s = unit.print(parser.getKeywords());
    validate("$start_address 0x800\n" +
             "\n" +
             "struct @party_address\n" +
             "   int8* line1\n" +
             "   int8* line2\n" +
             "   int16 country_number\n" +
             "end\n" +
             "\n" +
             "int8* @hello = \"Hello\"\n" +
             "@i_use_structs:\n" +
             "   @party_address address = 0;\n" +
             "   @party_address.line1 = @hello\n" +
             "end\n" +
             "\n" +
             "struct @@person\n" +
             "   int8* name\n" +
             "   int8 age\n" +
             "   @party_address address\n" +
             "   int32[20] guid\n" +
             "end\n" +
             "\n" +
             "int8* henry_name = \"Henry\"\n" +
             "@@person[3] @three_people\n" +
             "@@person* henry = &three_people[0]\n" +
             "henry.age = 6\n" +
             "henry.name = henry_name\n" +
             "@run_me:\n" +
             "   @@person* person = henry\n" +
             "   int8* address_line;\n" +
             "   address_line = person.address.line1\n" +
             "   @@person george\n" +
             "   person = &george\n" +
             "end\n" +
             "\n" +
             "@@main:\n" +
             "   gosub @run_me\n" +
             "   return\n" +
             "end\n", s);
  }

  private static void testDirectives()
  {
    Logger log = new Logger();
    SixteenHighTokeniser parser = createParser("Directive.16h", log);
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    validateNotNull(unit);
    validateTrue(parser.isCompleted());
    String s = unit.print(parser.getKeywords());
    validate("$start_address 0x800\n" +
             "$end_address 0xfff\n" +
             "$access_mode read-write\n" +
             "$access_time 1\n" +
             "\n" +
             "@@main:\n" +
             "end\n" +
             "\n" +
             "$start_address 0x1000\n" +
             "$end_address 0x7fff\n" +
             "$access_mode read-only\n" +
             "$access_time 1\n" +
             "\n" +
             "@@uint8[8192] lookup_table = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]\n", s);
  }

  private static SixteenHighTokeniser createParser(String filename, Logger log)
  {
    ClassInspector classInspector = ClassInspector.forClass(TokeniserTest.class);
    String packageName = classInspector.getPackageName();
    String programDir = EnvironmentInspector.getProgramDir();
    String fullFilename = programDir.replace('\\', '/') + "/test/" + packageName.replace('.', '/') + "/" + filename;
    String contents = FileUtil.readFile(new File(fullFilename));
    return new SixteenHighTokeniser(log,
                                    new SixteenHighKeywords(),
                                    filename,
                                    new TokenUnit(filename),
                                    contents);
  }

  private static SixteenHighTokeniser createParser(String contents)
  {
    return new SixteenHighTokeniser(new Logger(),
                                    new SixteenHighKeywords(),
                                    "",
                                    new TokenUnit(""),
                                    contents);
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
    testStatementDirective();
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
    testStructSimpleDeclaration();
    testStructFieldSelection();

    testSimple();
    testArrayDeclaration();
    testPointers();
    testExpressions();
    testStack();
    testStruct();
    testDirectives();
  }
}

