package name.bizna.util;

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

  public static String enumString(Enum anEnum)
  {
    String stringWithoutUnderscores = anEnum.toString().toLowerCase().replace('_', ' ');

    return capitaliseEachWord(stringWithoutUnderscores);
  }

  public static String capitaliseEachWord(String string)
  {
    if (string.equals(""))
    {
      return "";
    }
    StringBuilder stringBuffer = new StringBuilder(capitaliseFirstChar(string));
    for (int i = 1; i < stringBuffer.length(); i++)
    {
      char previousChar = stringBuffer.charAt(i - 1);
      if (previousChar == ' ' || previousChar == '-')
      {
        char currentChar = stringBuffer.charAt(i);
        stringBuffer.setCharAt(i, Character.toUpperCase(currentChar));
      }
    }
    return stringBuffer.toString();
  }

  public static String capitaliseFirstChar(String string)
  {
    StringBuilder stringBuffer = new StringBuilder(string.trim());
    if (stringBuffer.length() > 0)
    {
      char firstChar = stringBuffer.charAt(0);
      char upperCaseFirstChar = Character.toUpperCase(firstChar);
      stringBuffer.setCharAt(0, upperCaseFirstChar);
    }
    return stringBuffer.toString();
  }

  public static boolean isEmptyOrNull(String string)
  {
    return (string == null) || string.trim().isEmpty();
  }
}

