package net.assembler.sixteenhigh.parser.literal;

import net.assembler.sixteenhigh.parser.TextParserLog;
import net.common.parser.TextParser;

import static net.common.parser.Tristate.TRUE;
import static net.logicim.assertions.Validator.validate;

public class LiteralParserTest
{
  public static void test()
  {
    testIntegerLiterals();
  }

  protected static void testIntegerLiterals()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("5"));
    LiteralResult integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(5, integerLiteral.getInt().value);

    literalParser = new LiteralParser(createTextParser("5L"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(5, integerLiteral.getInt().value);

    literalParser = new LiteralParser(createTextParser("-5"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(-5, integerLiteral.getInt().value);
  }

  protected static TextParser createTextParser(String contents)
  {
    return new TextParser(contents, new TextParserLog(), "");
  }
}
