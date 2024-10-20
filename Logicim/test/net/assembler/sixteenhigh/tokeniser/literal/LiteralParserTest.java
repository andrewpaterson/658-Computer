package net.assembler.sixteenhigh.tokeniser.literal;

import net.common.logger.Logger;
import net.common.parser.TextParser;

import static net.common.parser.TextParser.*;
import static net.common.parser.Tristate.*;
import static net.logicim.assertions.Validator.*;

public abstract class LiteralParserTest
{
  protected static void testIntegerLiterals()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("5"));
    LiteralResult integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(5, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());
    validateTrue(integerLiteral.getInt().isValid());

    literalParser = new LiteralParser(createTextParser("+5"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(5, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());
    validateTrue(integerLiteral.getInt().isValid());

    literalParser = new LiteralParser(createTextParser("-5"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(5, integerLiteral.getInt().rawValue);
    validateFalse(integerLiteral.getInt().isPositive());
    validateTrue(integerLiteral.getInt().isValid());

    literalParser = new LiteralParser(createTextParser("−2147483648"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(2147483648L, integerLiteral.getInt().rawValue);
    validateFalse(integerLiteral.getInt().isPositive());
    validateTrue(integerLiteral.getInt().isValid());

    literalParser = new LiteralParser(createTextParser("−9223372036854775808LL"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTLong.class, integerLiteral.getLiteral().getClass());
    validate(-9223372036854775808L, integerLiteral.getLong().rawValue);
    validateFalse(integerLiteral.getLong().isPositive());
    validateTrue(integerLiteral.getLong().isValid());

    literalParser = new LiteralParser(createTextParser("9223372036854775807LL"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTLong.class, integerLiteral.getLiteral().getClass());
    validate(9223372036854775807L, integerLiteral.getLong().rawValue);
    validateTrue(integerLiteral.getLong().isPositive());
    validateTrue(integerLiteral.getLong().isValid());

    literalParser = new LiteralParser(createTextParser("18446744073709551615LL"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTLong.class, integerLiteral.getLiteral().getClass());
    validate(-1, integerLiteral.getLong().rawValue);
    validateTrue(integerLiteral.getLong().isPositive());
    validateTrue(integerLiteral.getLong().isValid());

    literalParser = new LiteralParser(createTextParser("18446744073709551615ll"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTLong.class, integerLiteral.getLiteral().getClass());
    validate(-1, integerLiteral.getLong().rawValue);
    validateTrue(integerLiteral.getLong().isPositive());
    validateTrue(integerLiteral.getLong().isValid());

    literalParser = new LiteralParser(createTextParser("2147483648"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validateFalse(integerLiteral.getInt().isValid());

    literalParser = new LiteralParser(createTextParser("5.0"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateFalse(integerLiteral.state);

    literalParser = new LiteralParser(createTextParser("5."));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateFalse(integerLiteral.state);

    literalParser = new LiteralParser(createTextParser("5e10"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateFalse(integerLiteral.state);

    literalParser = new LiteralParser(createTextParser("-5-"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.getInt().isValid());
    validate(-5, integerLiteral.getInt().getValue());

    literalParser = new LiteralParser(createTextParser("-5)"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.getInt().isValid());
    validate(-5, integerLiteral.getInt().getValue());

    literalParser = new LiteralParser(createTextParser("-5X"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateFalse(integerLiteral.state);

    literalParser = new LiteralParser(createTextParser("-5 "));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.getInt().isValid());
    validate(-5, integerLiteral.getInt().getValue());

    literalParser = new LiteralParser(createTextParser("0xA"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getInt().isValid());
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(0xA, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());

    literalParser = new LiteralParser(createTextParser("0xALL"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getLong().isValid());
    validateTrue(integerLiteral.getLong().isPositive());
    validate(CTLong.class, integerLiteral.getLiteral().getClass());
    validate(0xA, integerLiteral.getLong().rawValue);

    literalParser = new LiteralParser(createTextParser("-0xALL"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getLong().isValid());
    validate(CTLong.class, integerLiteral.getLiteral().getClass());
    validate(0xA, integerLiteral.getLong().rawValue);
    validateFalse(integerLiteral.getLong().isPositive());

    literalParser = new LiteralParser(createTextParser("07"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getInt().isValid());
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(7, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());

    literalParser = new LiteralParser(createTextParser("08"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validate(ERROR, integerLiteral.state);

    literalParser = new LiteralParser(createTextParser("-031245"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getInt().isValid());
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(12965, integerLiteral.getInt().rawValue);
    validateFalse(integerLiteral.getInt().isPositive());

    literalParser = new LiteralParser(createTextParser("00"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getInt().isValid());
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(0, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());

    literalParser = new LiteralParser(createTextParser("0b11111111_11111111_11111111_11111111"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_UNDERSCORE);
    validateTrue(integerLiteral.state);
    validateFalse(integerLiteral.getInt().isValid());
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(4294967295L, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());

    literalParser = new LiteralParser(createTextParser("0b11111111_11111111_11111111_11111111u"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_UNDERSCORE);
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getInt().isValid());
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(4294967295L, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());

    literalParser = new LiteralParser(createTextParser("0b11111111'11111111'11111111'11111111"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_APOSTROPHE);
    validateTrue(integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(4294967295L, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());
    validateFalse(integerLiteral.getInt().isValid());

    literalParser = new LiteralParser(createTextParser("1F"));
    validate(0, literalParser.getPosition());
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateFalse(integerLiteral.state);
    validate(0, literalParser.getPosition());

    literalParser = new LiteralParser(createTextParser("0 "));
    validate(0, literalParser.getPosition());
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(1, literalParser.getPosition());

    literalParser = new LiteralParser(createTextParser("0"));
    validate(0, literalParser.getPosition());
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(1, literalParser.getPosition());
    validateFalse(integerLiteral.getInt().isUnsigned());

    literalParser = new LiteralParser(createTextParser("0LL"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validate(CTLong.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.state);
    validateFalse(integerLiteral.getLong().isUnsigned());

    literalParser = new LiteralParser(createTextParser("0U"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getInt().isValid());
    validateTrue(integerLiteral.getInt().isUnsigned());

    literalParser = new LiteralParser(createTextParser("-("));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateFalse(integerLiteral.state);

    literalParser = new LiteralParser(createTextParser("-b"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateFalse(integerLiteral.state);

    literalParser = new LiteralParser(createTextParser("+ull"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validateFalse(integerLiteral.state);

    literalParser = new LiteralParser(createTextParser("32767s"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validate(CTShort.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getShort().isValid());
    validateFalse(integerLiteral.getShort().isUnsigned());
    validate(32767, integerLiteral.getShort().rawValue);
    validateTrue(integerLiteral.getShort().isPositive());

    literalParser = new LiteralParser(createTextParser("-32768s"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validate(CTShort.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getShort().isValid());
    validateFalse(integerLiteral.getShort().isUnsigned());
    validate(32768, integerLiteral.getShort().rawValue);
    validateFalse(integerLiteral.getShort().isPositive());

    literalParser = new LiteralParser(createTextParser("65535us"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validate(CTShort.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getShort().isValid());
    validateTrue(integerLiteral.getShort().isUnsigned());
    validate(65535, integerLiteral.getShort().rawValue);
    validateTrue(integerLiteral.getShort().isPositive());

    literalParser = new LiteralParser(createTextParser("127b"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validate(CTChar.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getChar().isValid());
    validateFalse(integerLiteral.getChar().isUnsigned());
    validate(127, integerLiteral.getChar().rawValue);
    validateTrue(integerLiteral.getChar().isPositive());

    literalParser = new LiteralParser(createTextParser("-128b"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validate(CTChar.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getChar().isValid());
    validateFalse(integerLiteral.getChar().isUnsigned());
    validate(128, integerLiteral.getChar().rawValue);
    validateFalse(integerLiteral.getChar().isPositive());

    literalParser = new LiteralParser(createTextParser("256ub"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validate(CTChar.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.state);
    validateFalse(integerLiteral.getChar().isValid());
    validateTrue(integerLiteral.getChar().isUnsigned());
    validate(256, integerLiteral.getChar().rawValue);
    validateTrue(integerLiteral.getChar().isPositive());

    literalParser = new LiteralParser(createTextParser("+0b11111111ub"));
    integerLiteral = literalParser.getIntegerLiteral(NUMBER_SEPARATOR_NONE);
    validate(CTChar.class, integerLiteral.getLiteral().getClass());
    validateTrue(integerLiteral.state);
    validateTrue(integerLiteral.getChar().isValid());
    validateTrue(integerLiteral.getChar().isUnsigned());
    validate(255, integerLiteral.getChar().rawValue);
    validateTrue(integerLiteral.getChar().isPositive());
  }

  private static void testFloatingLiteral()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("5.3"));
    LiteralResult floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTDouble.class, floatingLiteral.getLiteral().getClass());
    validate(5.3, floatingLiteral.getDouble().getValue());
    validateTrue(floatingLiteral.getDouble().isValid());

    literalParser = new LiteralParser(createTextParser("5.-3"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validate(ERROR, floatingLiteral.state);

    literalParser = new LiteralParser(createTextParser(".2"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTDouble.class, floatingLiteral.getLiteral().getClass());
    validate(.2, floatingLiteral.getDouble().getValue());
    validateTrue(floatingLiteral.getDouble().isValid());

    //Don't use Math.pow() to do digit shifting.  Use the method outlined in the site below.
    //https://binary-system.base-conversion.ro/real-number-converted-from-decimal-system-to-32bit-single-precision-IEEE754-binary-floating-point.php?decimal_number_base_ten=0.3
//    literalParser = new LiteralParser(createTextParser(".3"));
//    floatingLiteral = literalParser.getFloatingLiteral();
//    validateTrue(floatingLiteral.state);
//    validate(CTDouble.class, floatingLiteral.getLiteral().getClass());
//    validate(.3, floatingLiteral.getDouble().getValue());
//    validateTrue(floatingLiteral.getDouble().isValid());

    literalParser = new LiteralParser(createTextParser(".2F"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(.2, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser(".2f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(.2, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("-.2f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(-.2, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("-2.f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(-2.f, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("+.2f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(+.2, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("+2.f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(+2.f, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("1e10"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTDouble.class, floatingLiteral.getLiteral().getClass());
    validate(1e10, floatingLiteral.getDouble().getValue());
    validateTrue(floatingLiteral.getDouble().isValid());
    //
    literalParser = new LiteralParser(createTextParser("1e10f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(1e10f, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("1e+10f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(1e10f, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("1e-2f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(0.01, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("3.4028235e+37f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(3.4028235e37, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("3.4028235e+38f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(3.4028235000000003e38, floatingLiteral.getFloat().getValue());
    validateFalse(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("3.40282345e+38f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(3.40282345e38, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

//    literalParser = new LiteralParser(createTextParser("0x1.fffffeP+127f"));
//    floatingLiteral = literalParser.getFloatingLiteral();
//    validateTrue(floatingLiteral.state);
//    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
//    validate(3.40282345e38, floatingLiteral.getFloat().getValue());
//    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("0.0"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTDouble.class, floatingLiteral.getLiteral().getClass());
    validate(0.0, floatingLiteral.getDouble().getValue());
    validateTrue(floatingLiteral.getDouble().isValid());

    literalParser = new LiteralParser(createTextParser("0.0f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(0.0, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("0f"));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateTrue(floatingLiteral.state);
    validate(CTFloat.class, floatingLiteral.getLiteral().getClass());
    validate(0.0, floatingLiteral.getFloat().getValue());
    validateTrue(floatingLiteral.getFloat().isValid());

    literalParser = new LiteralParser(createTextParser("-("));
    floatingLiteral = literalParser.getFloatingLiteral();
    validateFalse(floatingLiteral.state);
  }

  private static void testBooleanLiteral()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("whee"));
    LiteralResult booleanLiteral = literalParser.getBooleanLiteral();
    validateFalse(booleanLiteral.state);

    literalParser = new LiteralParser(createTextParser("true"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validateTrue(booleanLiteral.state);
    validate(CTBoolean.class, booleanLiteral.getLiteral().getClass());
    validate(true, booleanLiteral.getBoolean().getValue());

    literalParser = new LiteralParser(createTextParser("false"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validateTrue(booleanLiteral.state);
    validate(CTBoolean.class, booleanLiteral.getLiteral().getClass());
    validate(false, booleanLiteral.getBoolean().getValue());

    literalParser = new LiteralParser(createTextParser("True"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validateFalse(booleanLiteral.state);

    literalParser = new LiteralParser(createTextParser("falsE"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validateFalse(booleanLiteral.state);

    literalParser = new LiteralParser(createTextParser("false)"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validateTrue(booleanLiteral.state);
    validate(CTBoolean.class, booleanLiteral.getLiteral().getClass());
    validate(false, booleanLiteral.getBoolean().getValue());

    literalParser = new LiteralParser(createTextParser("false/"));
    booleanLiteral = literalParser.getBooleanLiteral();
    validateTrue(booleanLiteral.state);
    validate(CTBoolean.class, booleanLiteral.getLiteral().getClass());
    validate(false, booleanLiteral.getBoolean().getValue());

    literalParser = new LiteralParser(createTextParser("false "));
    booleanLiteral = literalParser.getBooleanLiteral();
    validateTrue(booleanLiteral.state);
    validate(CTBoolean.class, booleanLiteral.getLiteral().getClass());
    validate(false, booleanLiteral.getBoolean().getValue());
  }

  private static void testCharacterLiteral()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("'c'"));
    LiteralResult characterLiteral = literalParser.getCharacterLiteral();
    validateTrue(characterLiteral.state);
    validate(CTChar.class, characterLiteral.getLiteral().getClass());
    validate('c', characterLiteral.getChar().rawValue);
    validateTrue(characterLiteral.getChar().isValid());

    literalParser = new LiteralParser(createTextParser("'\t'"));
    characterLiteral = literalParser.getCharacterLiteral();
    validateTrue(characterLiteral.state);
    validate(CTChar.class, characterLiteral.getLiteral().getClass());
    validate(9, characterLiteral.getChar().rawValue);
    validateTrue(characterLiteral.getChar().isValid());

    literalParser = new LiteralParser(createTextParser("L'tt'"));
    characterLiteral = literalParser.getCharacterLiteral();
    validateTrue(characterLiteral.state);
    validate(CTShort.class, characterLiteral.getLiteral().getClass());
    validate(29812, characterLiteral.getShort().rawValue);
    validateTrue(characterLiteral.getShort().isValid());
  }

  private static void testStringLiteral()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("\"Hello\""));
    LiteralResult characterLiteral = literalParser.getStringLiteral();
    validateTrue(characterLiteral.state);
    validate(CTString.class, characterLiteral.getLiteral().getClass());
    validate("Hello", characterLiteral.getString().value);

    literalParser = new LiteralParser(createTextParser("\"\""));
    characterLiteral = literalParser.getStringLiteral();
    validateTrue(characterLiteral.state);
    validate(CTString.class, characterLiteral.getLiteral().getClass());
    validate("", characterLiteral.getString().value);

    literalParser = new LiteralParser(createTextParser("\"Q\tQ\""));
    characterLiteral = literalParser.getStringLiteral();
    validateTrue(characterLiteral.state);
    validate(CTString.class, characterLiteral.getLiteral().getClass());
    validate("Q\tQ", characterLiteral.getString().value);
  }

  private static void testAnyLiteral()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("\"Hello\""));
    LiteralResult literalResult = literalParser.parseLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(literalResult.state);
    validate(CTString.class, literalResult.getLiteral().getClass());
    validate("Hello", literalResult.getString().value);

    literalParser = new LiteralParser(createTextParser("345"));
    literalResult = literalParser.parseLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(literalResult.state);
    validate(CTInt.class, literalResult.getLiteral().getClass());
    validate(345, literalResult.getInt().getValue());

    literalParser = new LiteralParser(createTextParser("-0x345"));
    literalResult = literalParser.parseLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(literalResult.state);
    validate(CTInt.class, literalResult.getLiteral().getClass());
    validate(-0x345, literalResult.getInt().getValue());

    literalParser = new LiteralParser(createTextParser("'X'"));
    literalResult = literalParser.parseLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(literalResult.state);
    validate(CTChar.class, literalResult.getLiteral().getClass());
    validate(88, literalResult.getChar().getValue());

    literalParser = new LiteralParser(createTextParser("0b1 "));
    literalResult = literalParser.parseLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(literalResult.state);
    validate(CTInt.class, literalResult.getLiteral().getClass());
    validate(1, literalResult.getInt().getValue());

    literalParser = new LiteralParser(createTextParser("1F"));
    literalResult = literalParser.parseLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(literalResult.state);
    validate(CTFloat.class, literalResult.getLiteral().getClass());
    validate(1, literalResult.getFloat().getValue());

    literalParser = new LiteralParser(createTextParser("false;"));
    literalResult = literalParser.parseLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(literalResult.state);
    validate(CTBoolean.class, literalResult.getLiteral().getClass());
    validate(false, literalResult.getBoolean().getValue());
  }

  private static void testIntegerComma()
  {
    LiteralParser literalParser = new LiteralParser(createTextParser("5,5"));
    LiteralResult integerLiteral = literalParser.parseLiteral(NUMBER_SEPARATOR_NONE);
    validateTrue(integerLiteral.state);
    validate(CTInt.class, integerLiteral.getLiteral().getClass());
    validate(5, integerLiteral.getInt().rawValue);
    validateTrue(integerLiteral.getInt().isPositive());
    validateTrue(integerLiteral.getInt().isValid());
  }

  public static void test()
  {
    testIntegerLiterals();
    testFloatingLiteral();
    testBooleanLiteral();
    testCharacterLiteral();
    testStringLiteral();
    testAnyLiteral();
    testIntegerComma();
  }

  protected static TextParser createTextParser(String contents)
  {
    return new TextParser(contents, new Logger(), "");
  }
}

