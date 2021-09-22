package name.bizna.emu65816;

public class Binary
{
  public static byte lower8BitsOf(short value)
  {
    return ((byte) (value & 0xFF));
  }

  public static byte higher8BitsOf(short value)
  {
    return ((byte) ((value & 0xFF00) >> 8));
  }

  public static short lower16BitsOf(int value)
  {
    return ((short) (value & 0xFFFF));
  }

  public static boolean is8bitValueNegative(byte value)
  {
    return (value & 0x80) != 0;
  }

  public static boolean is16bitValueNegative(short value)
  {
    return (value & 0x8000) != 0;
  }

  public static boolean is8bitValueZero(byte value)
  {
    return (value == 0x00);
  }

  public static boolean is16bitValueZero(short value)
  {
    return (value == 0x0000);
  }

  public static short setLower8BitsOf16BitsValue(short destination, byte value)
  {
    destination &= 0xFF00;
    destination |= value;
    return destination;
  }

  public static byte setBitIn8BitValue(byte value, byte bitNumber)
  {
    byte mask = (byte) (1 << bitNumber);
    value = (byte) (value | mask);
    return value;
  }

  public static byte clearBitIn8BitValue(byte value, byte bitNumber)
  {
    byte mask = (byte) (1 << bitNumber);
    mask = (byte) (mask ^ 0xFF);
    value = (byte) (value & mask);
    return value;
  }

  public static short setBitIn16BitValue(short value, byte bitNumber)
  {
    short mask = (short) (1 << bitNumber);
    value = (short) (value | mask);
    return value;
  }

  public static short clearBitIn16BitValue(short value, byte bitNumber)
  {
    short mask = (short) (1 << bitNumber);
    mask = (short) (mask ^ 0xFFFF);
    value = (short) (value & mask);
    return value;
  }

  public static byte convert8BitToBcd(byte val)
  {
    byte value = val;
    byte result = 0;
    byte shiftLeft = 0;
    while (value > 0)
    {
      byte digit = (byte) (value % 10);
      result |= digit << shiftLeft;
      value /= 10;
      shiftLeft += 4;
    }

    return result;
  }

  public static short convert16BitToBcd(short val)
  {
    short value = val;
    short result = 0;
    short shiftLeft = 0;
    while (value > 0)
    {
      byte digit = (byte) (value % 10);
      result |= digit << shiftLeft;
      value /= 10;
      shiftLeft += 4;
    }

    return result;
  }

  public static BCD8BitResult bcdSum8Bit(byte bcdFirst, byte bcdSecond, byte result, boolean carry)
  {
    byte shift = 0;
    result = 0;

    while (shift < 8)
    {
      byte digitOfFirst = (byte) (bcdFirst & 0xF);
      byte digitOfSecond = (byte) (bcdSecond & 0xF);
      byte sumOfDigits = (byte) (digitOfFirst + digitOfSecond + (carry ? 1 : 0));
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

    return new BCD8BitResult(result, carry);
  }

  public static BCD8BitResult bcdSubtract8Bit(byte bcdFirst, byte bcdSecond, byte result, boolean borrow)
  {
    byte shift = 0;
    result = 0;

    while (shift < 8)
    {
      byte digitOfFirst = (byte) (bcdFirst & 0xF);
      byte digitOfSecond = (byte) (bcdSecond & 0xF);
      byte diffOfDigits = (byte) (digitOfFirst - digitOfSecond - (borrow ? 1 : 0));
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

    return new BCD8BitResult(result, borrow);
  }

  public static BCD16BitResult bcdSum16Bit(short bcdFirst, short bcdSecond, short result, boolean carry)
  {
    result = 0;
    byte shift = 0;
    while (shift < 16)
    {
      byte digitOfFirst = (byte) (bcdFirst & 0xFF);
      byte digitOfSecond = (byte) (bcdSecond & 0xFF);
      byte partialresult = 0;
      BCD8BitResult bcd8BitResult = bcdSum8Bit(digitOfFirst, digitOfSecond, partialresult, carry);
      carry = bcd8BitResult.carry;
      partialresult = bcd8BitResult.value;
      result |= partialresult << shift;
      shift += 8;
      bcdFirst >>= shift;
      bcdSecond >>= shift;
    }
    return new BCD16BitResult(result, carry);
  }

  public static BCD16BitResult bcdSubtract16Bit(short bcdFirst, short bcdSecond, short result, boolean borrow)
  {
    result = 0;
    byte shift = 0;
    while (shift < 16)
    {
      byte digitOfFirst = (byte) (bcdFirst & 0xFF);
      byte digitOfSecond = (byte) (bcdSecond & 0xFF);
      byte partialresult = 0;
      BCD8BitResult bcd8BitResult = bcdSubtract8Bit(digitOfFirst, digitOfSecond, partialresult, borrow);
      borrow = bcd8BitResult.carry;
      partialresult = bcd8BitResult.value;
      result |= partialresult << shift;
      shift += 8;
      bcdFirst >>= shift;
      bcdSecond >>= shift;
    }
    return new BCD16BitResult(result, borrow);
  }
}

