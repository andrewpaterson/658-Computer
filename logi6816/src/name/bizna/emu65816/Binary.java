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
    return (toByte(value) == 0);
  }

  public static boolean is16bitValueZero(int value)
  {
    return (toShort(value) == 0);
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

