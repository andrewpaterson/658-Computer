package net.assembler.sixteenhigh.parser.literal;

import net.common.parser.StringZero;
import net.common.parser.TextParser;
import net.common.parser.Tristate;
import net.common.parser.primitive.FloatPointer;
import net.common.parser.primitive.IntegerPointer;
import net.common.parser.primitive.LongPointer;
import net.logicim.data.family.Family;

import static net.common.parser.TextParser.NUMBER_SEPARATOR_APOSTROPHE;
import static net.common.parser.Tristate.*;

public class LiteralParser
{
  protected TextParser textParser;

  public LiteralParser(TextParser textParser)
  {
    this.textParser = textParser;
  }

  public LiteralResult getBooleanLiteral()
  {
    Tristate result;

    result = textParser.getExactIdentifier("true", true);
    if (result == TRUE)
    {
      return new LiteralResult(new CTBoolean(true));
    }
    else if (result == FALSE)
    {
      result = textParser.getExactIdentifier("false", false);
      if (result == TRUE)
      {
        return new LiteralResult(new CTBoolean(false));
      }
      else
      {
        return new LiteralResult(result);
      }
    }
    else
    {
      return new LiteralResult(ERROR);
    }
  }

  public LiteralResult singleQuotedLiteral(boolean shortValue)
  {
    Tristate result;
    CTChar character;
    CTInt integer;
    CTShort shortInteger;
    StringZero stringZero = new StringZero();

    textParser.pushPosition();
    result = textParser.getQuotedCharacterSequence('\'', '\'', stringZero, null, true, !shortValue, true);
    if (result == TRUE)
    {
      if ((stringZero.length() >= 5) || (shortValue && (stringZero.length() >= 3)))
      {
        error("Too many character in character literal");
        textParser.popPosition();
        return new LiteralResult(ERROR);
      }

      if (stringZero.length() == 0)
      {
        error("Empty character literal");
        textParser.popPosition();
        return new LiteralResult(ERROR);
      }

      if (stringZero.length() == 1)
      {
        if (!shortValue)
        {
          character = new CTChar(stringZero.get(0), false, false);
          textParser.passPosition();
          return new LiteralResult(character);
        }
        else
        {
          shortInteger = new CTShort(stringZero.get(0), false, false);
          textParser.passPosition();
          return new LiteralResult(shortInteger);
        }
      }

      if (stringZero.length() == 2)
      {
        if (!shortValue)
        {
          integer = new CTInt(stringZero.get(1) + (stringZero.get(0) << 8), false, false);
          textParser.passPosition();
          return new LiteralResult(integer);
        }
        else
        {
          shortInteger = new CTShort(stringZero.get(1) + (stringZero.get(0) << 8), false, false);
          textParser.passPosition();
          return new LiteralResult(shortInteger);
        }
      }

      if (stringZero.length() == 3)
      {
        integer = new CTInt(stringZero.get(2) + (stringZero.get(1) << 8) + (stringZero.get(0) << 16), false, false);
        textParser.passPosition();
        return new LiteralResult(integer);
      }

      if (stringZero.length() == 4)
      {
        integer = new CTInt(stringZero.get(3) + (stringZero.get(2) << 8) + (stringZero.get(1) << 16) + (stringZero.get(1) << 24), false, false);
        textParser.passPosition();
        return new LiteralResult(integer);
      }

      error("Should never fall through to this statement.");
      textParser.passPosition();
      return new LiteralResult(ERROR);
    }
    else
    {
      textParser.popPosition();
      return new LiteralResult(FALSE);
    }
  }

  private void error(String s)
  {
  }

  public LiteralResult stringLiteral()
  {
    StringZero stringZero = new StringZero();
    Tristate result = textParser.getQuotedCharacterSequence('\"', '"', stringZero, null, true, true, true);
    if (result == TRUE)
    {
      CTString stringLiteral = new CTString(stringZero.toString());
      return new LiteralResult(stringLiteral);
    }
    else
    {
      return new LiteralResult(FALSE);
    }
  }

  public LiteralResult wideStringLiteral()
  {
    StringZero stringZero = new StringZero();
    Tristate result = textParser.getQuotedCharacterSequence('\"', '"', stringZero, null, true, true, true);
    if (result == TRUE)
    {
      CTWideString stringLiteral = new CTWideString(stringZero.toString());
      return new LiteralResult(stringLiteral);
    }
    else
    {
      return new LiteralResult(FALSE);
    }
  }

  public LiteralResult getIntegerLiteral()
  {
    textParser.pushPosition();

    textParser.skipWhiteSpace();

    IntegerPointer signPointer = new IntegerPointer();
    Tristate result = textParser.getSign(signPointer);
    if (result == ERROR)
    {
      textParser.passPosition();
      return new LiteralResult(ERROR);
    }
    IntegerPointer basePointer = new IntegerPointer();
    result = integerPrefix(basePointer);
    if (result == ERROR)
    {
      textParser.passPosition();
      return new LiteralResult(ERROR);
    }
    else if (result == FALSE)
    {
      basePointer.value = 10;
    }

    LongPointer longPointer = new LongPointer();
    IntegerPointer numDigitsPointer = new IntegerPointer();

    result = textParser.getDigits(longPointer,
                                  null,
                                  numDigitsPointer,
                                  false,
                                  false,
                                  basePointer.value,
                                  NUMBER_SEPARATOR_APOSTROPHE);
    if (result == TRUE)
    {
      LiteralResult literalResult = integerType(longPointer.value, signPointer.value);
      textParser.popPosition();
      return literalResult;
    }
    else
    {
      textParser.passPosition();
      return new LiteralResult(ERROR);
    }
  }

  protected Tristate integerPrefix(IntegerPointer basePointer)
  {
    Tristate result = textParser.getExactCaseInsensitiveCharacterSequence("0x", false);
    if (result == TRUE)
    {
      basePointer.value = 16;
      return TRUE;
    }
    else if (result == ERROR)
    {
      return FALSE;
    }

    result = textParser.getExactCaseInsensitiveCharacterSequence("0b", false);
    if (result == TRUE)
    {
      basePointer.value = 2;
      return TRUE;
    }
    else if (result == ERROR)
    {
      return FALSE;
    }

    result = textParser.getExactCaseInsensitiveCharacterSequence("0", false);
    if (result == TRUE)
    {
      basePointer.value = 8;
      return TRUE;
    }
    else if (result == ERROR)
    {
      return FALSE;
    }
    return FALSE;
  }

  public LiteralResult getCharacterLiteral()
  {
    return singleQuotedLiteral(false);
  }

  public LiteralResult shortLiteral()
  {
    LiteralResult literalResult;
    Tristate result;

    textParser.pushPosition();

    result = textParser.getExactIdentifier("L", true);
    if (result == TRUE)
    {
      literalResult = singleQuotedLiteral(true);
      if (literalResult.isTrue())
      {
        textParser.passPosition();
        return literalResult;
      }
      else if (literalResult.isError())
      {
        textParser.passPosition();
        return literalResult;
      }

      literalResult = wideStringLiteral();
      if (literalResult.isTrue())
      {
        textParser.passPosition();
        return literalResult;
      }
      else if (literalResult.isError())
      {
        textParser.passPosition();
        return literalResult;
      }

      textParser.popPosition();
      return literalResult;
    }
    else if (result == ERROR)
    {
      textParser.passPosition();
      return new LiteralResult(ERROR);
    }
    else
    {
      textParser.popPosition();
      return new LiteralResult(FALSE);
    }
  }

  private LiteralResult integerType(long value, int sign)
  {
    Tristate result;
    boolean unsigned;
    boolean bLong;
    boolean bLongLong;

    unsigned = false;
    bLongLong = false;
    bLong = false;

    result = textParser.getExactCaseInsensitiveCharacterSequence("ULL", false);
    if (result == TRUE)
    {
      bLongLong = true;
      unsigned = true;
    }
    else if (result == ERROR)
    {
      return new LiteralResult(ERROR);
    }

    result = textParser.getExactCaseInsensitiveCharacterSequence("UL", false);
    if (result == TRUE)
    {
      bLong = true;
      unsigned = true;
    }
    else if (result == ERROR)
    {
      return new LiteralResult(ERROR);
    }

    result = textParser.getExactCaseInsensitiveCharacterSequence("LL", false);
    if (result == TRUE)
    {
      bLongLong = true;
    }
    else if (result == ERROR)
    {
      return new LiteralResult(ERROR);
    }

    result = textParser.getExactCaseInsensitiveCharacterSequence("L", false);
    if (result == TRUE)
    {
      bLong = true;
    }
    else if (result == ERROR)
    {
      return new LiteralResult(ERROR);
    }

    result = textParser.getExactCaseInsensitiveCharacterSequence("U", false);
    if (result == TRUE)
    {
      unsigned = true;
    }
    else if (result == ERROR)
    {
      return new LiteralResult(ERROR);
    }

    char c = textParser.peekCurrent();
    if (!textParser.isOutsideText())
    {
      if (!isValidCharacterFollowingInteger(c))
      {
        return new LiteralResult(FALSE);
      }
    }

    if (bLongLong)
    {
      return new LiteralResult(new CTLong(value, unsigned, sign < 0));
    }
    else
    {
      return new LiteralResult(new CTInt(value, unsigned, sign < 0));
    }
  }

  private boolean isValidCharacterFollowingInteger(char c)
  {
    if (TextParser.isWhiteSpace(c))
    {
      return true;
    }
    switch (c)
    {
      case '+':
      case '-':
      case '*':
      case '/':
      case '}':
      case ')':
      case ']':
      case ';':
      case '|':
      case '&':
        return true;
      default:
        return false;
    }
  }

  public LiteralResult doubleType(double ldValue)
  {
    Tristate bResult;

    bResult = textParser.getExactCaseInsensitiveCharacterSequence("L", false);
    if (bResult == TRUE)
    {
      return new LiteralResult(new CTLongDouble(ldValue));
    }
    else if (bResult == FALSE)
    {
      bResult = textParser.getExactCaseInsensitiveCharacterSequence("F", false);
      if (bResult == TRUE)
      {
        return new LiteralResult(new CTFloat(ldValue));
      }
      else if (bResult == FALSE)
      {
        return new LiteralResult(new CTDouble(ldValue));
      }
      else
      {
        return new LiteralResult(ERROR);
      }
    }
    else
    {
      return new LiteralResult(ERROR);
    }
  }

  public LiteralResult getFloatingLiteral()
  {
    textParser.pushPosition();

    FloatPointer number = new FloatPointer();
    Tristate result = textParser.getFloat(number);
    if (result == TRUE)
    {
      result = textParser.getExactCaseInsensitiveCharacterSequence("e", false);
      if (result == TRUE)
      {
        IntegerPointer exponent = new IntegerPointer();
        result = textParser.getInteger(exponent, null, 10, true);
        if (result == TRUE)
        {
          double ten = 10.0;
          number.value = Math.pow(ten, exponent.value) * number.value;
          LiteralResult literalResult = doubleType(number.value);
          textParser.passPosition();
          return literalResult;
        }
        else if (result == FALSE)
        {
          textParser.popPosition();
          return new LiteralResult(FALSE);
        }
        else
        {
          textParser.passPosition();
          return new LiteralResult(ERROR);
        }
      }
      else
      {
        LiteralResult literalResult = doubleType(number.value);
        textParser.passPosition();
        return literalResult;
      }
    }
    else if (result == FALSE)
    {
      textParser.popPosition();
      return new LiteralResult(FALSE);
    }
    else
    {
      textParser.passPosition();
      return new LiteralResult(ERROR);
    }
  }

  public LiteralResult parseLiteral()
  {
    return null;
  }
}

