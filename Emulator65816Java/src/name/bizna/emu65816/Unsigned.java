package name.bizna.emu65816;

public class Unsigned
{
  public static int toShort(int value)
  {
    return value & 0xffff;
  }

  public static int toByte(int value)
  {
    return value & 0xff;
  }
}
