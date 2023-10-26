package net.assembler.sixteenhigh.parser.literal;

import net.assembler.sixteenhigh.parser.TextParserLog;
import net.common.parser.TextParser;

import static net.common.parser.Tristate.FALSE;
import static net.common.parser.Tristate.TRUE;
import static net.logicim.assertions.Validator.*;

public class LiteralParserTest
{
  protected static void testIntegerLiterals()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("5"));
    LiteralResult integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(5, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());
    validateTrue(integerLiteral.getInt().isValid());

    literalParser = new LiteralParser(createTextParser("5L"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(5, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());
    validateTrue(integerLiteral.getInt().isValid());

    literalParser = new LiteralParser(createTextParser("-5"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(5, integerLiteral.getInt().rawValue);
    validateFalse(integerLiteral.getInt().isPositive());
    validateTrue(integerLiteral.getInt().isValid());

    literalParser = new LiteralParser(createTextParser("−2147483648"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(2147483648L, integerLiteral.getInt().rawValue);
    validateFalse(integerLiteral.getInt().isPositive());
    validateTrue(integerLiteral.getInt().isValid());

    literalParser = new LiteralParser(createTextParser("−9223372036854775808LL"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTLong.class, integerLiteral.getLiteral().getClass());
    validate(-9223372036854775808L, integerLiteral.getLong().rawValue);
    validateFalse(integerLiteral.getLong().isPositive());
    validateTrue(integerLiteral.getLong().isValid());

    literalParser = new LiteralParser(createTextParser("9223372036854775807LL"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTLong.class, integerLiteral.getLiteral().getClass());
    validate(9223372036854775807L, integerLiteral.getLong().rawValue);
    validateTrue(integerLiteral.getLong().isPositive());
    validateTrue(integerLiteral.getLong().isValid());

    literalParser = new LiteralParser(createTextParser("18446744073709551615LL"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTLong.class, integerLiteral.getLiteral().getClass());
    validate(-1, integerLiteral.getLong().rawValue);
    validateTrue(integerLiteral.getLong().isPositive());
    validateTrue(integerLiteral.getLong().isValid());

    literalParser = new LiteralParser(createTextParser("2147483648"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validateFalse(integerLiteral.getInt().isValid());

    literalParser = new LiteralParser(createTextParser("5.0"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(FALSE, integerLiteral.state);

    literalParser = new LiteralParser(createTextParser("5."));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(FALSE, integerLiteral.state);

    literalParser = new LiteralParser(createTextParser("5e10"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(FALSE, integerLiteral.state);
  }

  private static void testFloatingLiterals()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("5.0"));
    LiteralResult floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTInt.class, floatingLiteral.getLiteral().getClass());
    validate(5, floatingLiteral.getInt().rawValue);
    validateTrue(floatingLiteral.getInt().isPositive());
    validateTrue(floatingLiteral.getInt().isValid());
  }

  public static void test()
  {
    testIntegerLiterals();
    testFloatingLiterals();
  }

  protected static TextParser createTextParser(String contents)
  {
    return new TextParser(contents, new TextParserLog(), "");
  }
}
