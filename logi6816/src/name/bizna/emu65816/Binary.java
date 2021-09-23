package name.bizna.emu65816;

import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class Binary
{
  public static int lower8BitsOf(int value)
  {
    return toByte(value);
  }

  public static int higher8BitsOf(int value)
  {
    return toByte(((value & 0xFF00) >> 8));
  }

  public static int lower16BitsOf(int value)
  {
    return toShort(value);
  }

  public static boolean is8bitValueNegative(int value)
  {
    return (value & 0x80) != 0;
  }

  public static boolean is16bitValueNegative(int value)
  {
    return (value & 0x8000) != 0;
  }

  public static boolean is8bitValueZero(int value)
  {
    return (value == 0x00);
  }

  public static boolean is16bitValueZero(int value)
  {
    return (value == 0x0000);
  }

  public static int setLower8BitsOf16BitsValue(int destination, int value)
  {
    destination &= 0xFF00;
    destination |= toByte(value);
    return destination;
  }

  public static int setBitIn8BitValue(int value, int bitNumber)
  {
    int mask = (1 << bitNumber);
    value = (value | mask);
    return toByte(value);
  }

  public static int clearBitIn8BitValue(int value, int bitNumber)
  {
    int mask = (1 << bitNumber);
    mask = (mask ^ 0xFF);
    value = (value & mask);
    return toByte(value);
  }

  public static int setBitIn16BitValue(int value, int bitNumber)
  {
    int mask = (1 << bitNumber);
    value = (value | mask);
    return toShort(value);
  }

  public static int clearBitIn16BitValue(int value, int bitNumber)
  {
    int mask = (1 << bitNumber);
    mask = (mask ^ 0xFFFF);
    value = (value & mask);
    return toShort(value);
  }

  public static BCDResult bcdSum8Bit(int bcdFirst, int bcdSecond, boolean carry)
  {
    int shift = 0;
    int result = 0;

    while (shift < 8)
    {
      int digitOfFirst = (bcdFirst & 0xF);
      int digitOfSecond = (bcdSecond & 0xF);
      int sumOfDigits = toByte(digitOfFirst + digitOfSecond + (carry ? 1 : 0));
      carry = sumOfDigits > 9;
      if (carry)
      {
        sumOfDigits += 6;
      }
      sumOfDigits &= 0xF;
      result |= sumOfDigits << shift;

      shift += 4;
      bcdFirst >>= shift;
      bcdSecond >>= shift;
    }

    return new BCDResult(result, carry);
  }

  public static BCDResult bcdSubtract8Bit(int bcdFirst, int bcdSecond, boolean borrow)
  {
    int shift = 0;
    int result = 0;

    while (shift < 8)
    {
      int digitOfFirst = (bcdFirst & 0xF);
      int digitOfSecond = (bcdSecond & 0xF);
      int diffOfDigits = toByte(digitOfFirst - digitOfSecond - (borrow ? 1 : 0));
      borrow = diffOfDigits > 9;
      if (borrow)
      {
        diffOfDigits -= 6;
      }
      diffOfDigits &= 0xF;
      result |= diffOfDigits << shift;

      shift += 4;
      bcdFirst >>= shift;
      bcdSecond >>= shift;
    }

    return new BCDResult(result, borrow);
  }

  public static BCDResult bcdSum16Bit(int bcdFirst, int bcdSecond, boolean carry)
  {
    int result = 0;
    int shift = 0;
    while (shift < 16)
    {
      int digitOfFirst = (bcdFirst & 0xFF);
      int digitOfSecond = (bcdSecond & 0xFF);
      BCDResult bcd8BitResult = bcdSum8Bit(digitOfFirst, digitOfSecond, carry);
      carry = bcd8BitResult.carry;
      int partialresult = bcd8BitResult.value;
      result = toShort(result | (partialresult << shift));
      shift += 8;
      bcdFirst = toShort(bcdFirst >> shift);
      bcdSecond = toShort(bcdSecond >> shift);
    }
    return new BCDResult(result, carry);
  }

  public static BCDResult bcdSubtract16Bit(int bcdFirst, int bcdSecond, boolean borrow)
  {
    int result = 0;
    int shift = 0;
    while (shift < 16)
    {
      int digitOfFirst = (bcdFirst & 0xFF);
      int digitOfSecond = (bcdSecond & 0xFF);
      BCDResult bcd8BitResult = bcdSubtract8Bit(digitOfFirst, digitOfSecond, borrow);
      borrow = bcd8BitResult.carry;
      int partialresult = bcd8BitResult.value;
      result = toShort(result | (partialresult << shift));
      shift += 8;
      bcdFirst = toShort(bcdFirst >> shift);
      bcdSecond = toShort(bcdSecond >> shift);
    }
    return new BCDResult(result, borrow);
  }
}

