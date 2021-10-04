package name.bizna.emu65816;

import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class Binary
{
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
      int partialResult = bcd8BitResult.value;
      result = toShort(result | (partialResult << shift));
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
      int partialResult = bcd8BitResult.value;
      result = toShort(result | (partialResult << shift));
      shift += 8;
      bcdFirst = toShort(bcdFirst >> shift);
      bcdSecond = toShort(bcdSecond >> shift);
    }
    return new BCDResult(result, borrow);
  }

  public static int getLowByte(int value)
  {
    return toByte(value);
  }

  public static int getHighByte(int value)
  {
    return toByte((value & 0xFF00) >> 8);
  }

  public static int setLowByte(int variable,  int data)
  {
    return (variable & 0xFF00) | toByte(data);
  }

  public static int setHighByte(int variable, int data)
  {
    return (variable & 0xFF) | (toByte(data) << 8);
  }

  public static void assert8Bit(int value, String variable)
  {
    if ((value < 0) || (value > 0xFF))
    {
      String error = variable + "value [0x" + Integer.toHexString(value) + "] must in the range 0...0xFF.";
      throw new EmulatorException(error);
    }
  }

  public static void assert16Bit(int value, String variable)
  {
    if ((value < 0) || (value > 0xFFFF))
    {
      String error = variable + "value [0x" + Integer.toHexString(value) + "] must in the range 0...0xFFFF.";
      throw new EmulatorException(error);
    }
  }
}

