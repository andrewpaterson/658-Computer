package name.bizna.emu65816.util;

import static java.lang.Integer.toHexString;

public class StringUtil
{
  public static String rightJustify(String source, int width, String padCharacter)
  {
    StringBuilder s = new StringBuilder(source);
    while (s.length() < width)
    {
      s.insert(0, padCharacter);
    }
    return s.toString();
  }

  public static String to16BitHex(int value)
  {
    return rightJustify(toHexString(value), 4, "0");
  }

  public static String to8BitHex(int value)
  {
    return rightJustify(toHexString(value), 2, "0");
  }
}

