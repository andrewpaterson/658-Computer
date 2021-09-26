package name.bizna.emu65816.util;

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
}

