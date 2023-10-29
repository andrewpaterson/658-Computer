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

    literalParser = new LiteralParser(createTextParser("-5-"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(-5, integerLiteral.getInt().getValue());

    literalParser = new LiteralParser(createTextParser("-5)"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(-5, integerLiteral.getInt().getValue());

    literalParser = new LiteralParser(createTextParser("-5X"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(FALSE, integerLiteral.state);

    literalParser = new LiteralParser(createTextParser("-5 "));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(-5, integerLiteral.getInt().getValue());


    literalParser = new LiteralParser(createTextParser("0xA"));
    integerLiteral = literalParser.getIntegerLiteral();
    validate(TRUE, integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(0xA, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());
    validateTrue(integerLiteral.getInt().isValid());
  }

  private static void testFloatingLiteral()
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

    literalParser = new LiteralParser(createTextParser("1e10"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTDouble.class, floatingLiteral.getLiteral().getClass());
    validate(1e10, floatingLiteral.getDouble().getValue());
    validateTrue(floatingLiteral.getDouble().isValid());
    //
    literalParser = new LiteralParser(createTextParser("1e10f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(1e10f, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("1e+10f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(1e10f, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("1e-2f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(0.01, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("3.4028235e+37f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(3.4028235e37, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("3.4028235e+38f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(3.4028235000000003e38, floatingLiteral.getFloat().getValue());
    validateFalse(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("3.40282345e+38f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(TRUE, floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(3.40282345e38, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

//    literalParser = new LiteralParser(createTextParser("0x1.fffffeP+127f"));
//    floatingLiteral = literalParser.getFloatingLiteral();
//    validate(TRUE, floatingLiteral.state);
//    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
//    validate(3.40282345e38, floatingLiteral.getFloat().getValue());
//    validateTrue(floatingLiteral.getFloat().isValid());
  }

  private static void testBooleanLiteral()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("whee"));
    LiteralResult booleanLiteral = literalParser.getBooleanLiteral();
    validate(FALSE, booleanLiteral.state);

    literalParser = new LiteralParser(createTextParser("true"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validate(TRUE, booleanLiteral.state);
    validate(CTBoolean.class, booleanLiteral.getLiteral().getClass());
    validate(true, booleanLiteral.getBoolean().getValue());

    literalParser = new LiteralParser(createTextParser("false"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validate(TRUE, booleanLiteral.state);
    validate(CTBoolean.class, booleanLiteral.getLiteral().getClass());
    validate(false, booleanLiteral.getBoolean().getValue());

    literalParser = new LiteralParser(createTextParser("True"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validate(FALSE, booleanLiteral.state);

    literalParser = new LiteralParser(createTextParser("falsE"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validate(FALSE, booleanLiteral.state);

    literalParser = new LiteralParser(createTextParser("false)"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validate(TRUE, booleanLiteral.state);
    validate(CTBoolean.class, booleanLiteral.getLiteral().getClass());
    validate(false, booleanLiteral.getBoolean().getValue());

    literalParser = new LiteralParser(createTextParser("false/"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validate(TRUE, booleanLiteral.state);
    validate(CTBoolean.class, booleanLiteral.getLiteral().getClass());
    validate(false, booleanLiteral.getBoolean().getValue());

    literalParser = new LiteralParser(createTextParser("false "));
    booleanLiteral = literalParser.getBooleanLiteral();
    validate(TRUE, booleanLiteral.state);
    validate(CTBoolean.class, booleanLiteral.getLiteral().getClass());
    validate(false, booleanLiteral.getBoolean().getValue());
  }

  private static void testCharacterLiteral()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("'c'"));
    LiteralResult characterLiteral = literalParser.getCharacterLiteral();
    validate(TRUE, characterLiteral.state);
    validate(CTChar.class, characterLiteral.getLiteral().getClass());
    validate('c', characterLiteral.getChar().rawValue);
    validateTrue(characterLiteral.getChar().isValid());
  }

  public static void test()
  {
    testIntegerLiterals();
    testFloatingLiteral();
    testBooleanLiteral();
    testCharacterLiteral();
  }

  protected static TextParser createTextParser(String contents)
  {
    return new TextParser(contents, new TextParserLog(), "");
  }
}
