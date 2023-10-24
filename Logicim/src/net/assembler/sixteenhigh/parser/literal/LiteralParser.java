package net.assembler.sixteenhigh.parser.literal;

import net.common.parser.StringZero;
import net.common.parser.TextParser;
import net.common.parser.Tristate;
import net.common.parser.primitive.FloatPointer;
import net.common.parser.primitive.IntegerPointer;
import net.common.parser.primitive.LongPointer;

import static net.common.parser.Tristate.*;

public class LiteralParser
{
  protected TextParser textParser;

  public LiteralParser(TextParser textParser)
  {
    this.textParser = textParser;
  }

  public LiteralResult doubleLiteral()
  {
    textParser.pushPosition();

    FloatPointer f = new FloatPointer();
    IntegerPointer exponent = new IntegerPointer();

    Tristate result = textParser.getFloat(f);
    if (result == TRUE)
    {
      result = textParser.getExactIdentifier("e", false);
      if (result == TRUE)
      {
        result = textParser.getInteger(exponent, null, true);
        if (result == TRUE)
        {
          double ten = 10.0;
          f.value = Math.pow(ten, exponent.value) * f.value;
          LiteralResult literalResult = doubleType(f.value);
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
        LiteralResult literalResult = doubleType(f.value);
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

  public LiteralResult booleanLiteral()
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
          character = new CTChar(stringZero.get(0), false);
          textParser.passPosition();
          return new LiteralResult(character);
        }
        else
        {
          shortInteger = new CTShort(stringZero.get(0), false);
          textParser.passPosition();
          return new LiteralResult(shortInteger);
        }
      }

      if (stringZero.length() == 2)
      {
        if (!shortValue)
        {
          integer = new CTInt(stringZero.get(1) + (stringZero.get(0) << 8), false);
          textParser.passPosition();
          return new LiteralResult(integer);
        }
        else
        {
          shortInteger = new CTShort(stringZero.get(1) + (stringZero.get(0) << 8), false);
          textParser.passPosition();
          return new LiteralResult(shortInteger);
        }
      }

      if (stringZero.length() == 3)
      {
        integer = new CTInt(stringZero.get(2) + (stringZero.get(1) << 8) + (stringZero.get(0) << 16), false);
        textParser.passPosition();
        return new LiteralResult(integer);
      }

      if (stringZero.length() == 4)
      {
        integer = new CTInt(stringZero.get(3) + (stringZero.get(2) << 8) + (stringZero.get(1) << 16) + (stringZero.get(1) << 24), false);
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

  private LiteralResult decimalInteger()
  {
    textParser.pushPosition();

    LongPointer longPointer = new LongPointer();
    IntegerPointer numDigitsPointer = new IntegerPointer();
    IntegerPointer signPointer = new IntegerPointer();
    Tristate result = textParser.getInteger(longPointer, signPointer, numDigitsPointer, true);
    if (result == TRUE)
    {
      LiteralResult literalResult = integerType(longPointer.value);
      textParser.passPosition();
      return literalResult;
    }
    else if (result == ERROR)
    {
      textParser.passPosition();
      return new LiteralResult(ERROR);

    }
    textParser.popPosition();
    return new LiteralResult(FALSE);
  }

  public LiteralResult getIntegerLiteral()
  {
    LiteralResult literalResult = decimalInteger();
    if (literalResult.isTrue())
    {
      return literalResult;
    }
    else
    {
      LongPointer longPointer = new LongPointer();
      IntegerPointer numDigitsPointer = new IntegerPointer();
      IntegerPointer signPointer = new IntegerPointer();
      Tristate result;
      result = textParser.getHexadecimal(longPointer, signPointer, numDigitsPointer, true);
      if (result == TRUE)
      {
        literalResult = integerType(longPointer.value);
        return literalResult;
      }
      else
      {
        result = textParser.getOctal(longPointer, signPointer, numDigitsPointer, true);
        if (result == TRUE)
        {
          literalResult = integerType(longPointer.value);
          return literalResult;
        }
        else
        {
          return new LiteralResult(result);
        }
      }
    }
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

  private LiteralResult integerType(long value)
  {
    Tristate result;
    boolean unsigned;
    boolean bLong;
    boolean bLongLong;
    CTInt pcInt;
    CTLong pcLongLong;

	/*
	U
	L
	UL
	LL
	ULL
	*/

    unsigned = false;
    bLongLong = false;
    bLong = false;

    result = textParser.getExactIdentifier("ULL", false);
    if (result == TRUE)
    {
      bLongLong = true;
      unsigned = true;
    }
    else if (result == ERROR)
    {
      return new LiteralResult(ERROR);
    }

    result = textParser.getExactIdentifier("UL", false);
    if (result == TRUE)
    {
      bLong = true;
      unsigned = true;
    }
    else if (result == ERROR)
    {
      return new LiteralResult(ERROR);
    }

    result = textParser.getExactIdentifier("LL", false);
    if (result == TRUE)
    {
      bLongLong = true;
    }
    else if (result == ERROR)
    {
      return new LiteralResult(ERROR);
    }

    result = textParser.getExactIdentifier("L", false);
    if (result == TRUE)
    {
      bLong = true;
    }
    else if (result == ERROR)
    {
      return new LiteralResult(ERROR);
    }

    result = textParser.getExactIdentifier("U", false);
    if (result == TRUE)
    {
      unsigned = true;
    }
    else if (result == ERROR)
    {
      return new LiteralResult(ERROR);
    }

    if (bLongLong)
    {
      return new LiteralResult(new CTLong(value, unsigned));
    }
    else
    {
      return new LiteralResult(new CTInt(value, unsigned));
    }
  }

  public LiteralResult doubleType(double ldValue)
  {
    Tristate bResult;

    bResult = textParser.getExactIdentifier("L", false);
    if (bResult == TRUE)
    {
      return new LiteralResult(new CTLongDouble(ldValue));
    }
    else if (bResult == FALSE)
    {
      bResult = textParser.getExactIdentifier("F", false);
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

  public LiteralResult parseLiteral()
  {
    return null;
  }
}

