package net.assembler.sixteenhigh.parser.literal;

import net.assembler.sixteenhigh.parser.TextParserLog;
import net.common.parser.TextParser;

import static net.common.parser.Tristate.*;
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


    literalParser = new LiteralParser(createTextParser("18446744073709551615ll"));
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
    LiteralParser literalParser = new LiteralParser(createTextParser("5.3"));
    LiteralResult floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTDouble.class, floatingLiteral.getLiteral().getClass());
    validate(5.3, floatingLiteral.getDouble().getValue());
    validateTrue(floatingLiteral.getDouble().isValid());

    literalParser = new LiteralParser(createTextParser("5.-3"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(ERROR, floatingLiteral.state);

    literalParser = new LiteralParser(createTextParser(".2"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTDouble.class, floatingLiteral.getLiteral().getClass());
    validate(.2, floatingLiteral.getDouble().getValue());
    validateTrue(floatingLiteral.getDouble().isValid());

    //Don't use Math.pow() to do digit shifting.  Use the method outlined in the site below.
    //https://binary-system.base-conversion.ro/real-number-converted-from-decimal-system-to-32bit-single-precision-IEEE754-binary-floating-point.php?decimal_number_base_ten=0.3
//    literalParser = new LiteralParser(createTextParser(".3"));
//    floatingLiteral = literalParser.getFloatingLiteral();
//    validate(TRUE, floatingLiteral.state);
//    validate(CTDouble.class, floatingLiteral.getLiteral().getClass());
//    validate(.3, floatingLiteral.getDouble().getValue());
//    validateTrue(floatingLiteral.getDouble().isValid());

    literalParser = new LiteralParser(createTextParser(".2F"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(.2, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser(".2f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(.2, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());
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
