package net.util;

public class IntUtil
{
  public static int toShort(int value)
  {
    return value & 0xffff;
  }

  public static int toByte(int value)
  {
    return value & 0xff;
  }

  public static int toNybble(int value)
  {
    return value & 0xf;
  }

  public static int getLowByte(int value)
  {
    return toByte(value);
  }

  public static int getHighByte(int value)
  {
    return toByte((value & 0xFF00) >> 8);
  }

  public static int setLowByte(int variable, int data)
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

