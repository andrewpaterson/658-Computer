package net.logicim.common.util;

import net.logicim.common.SimulatorException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.toHexString;

public class StringUtil
{
  public static String rightJustify(String source, int width, String padCharacter)
  {
    return pad(width - source.length(), padCharacter) + source;
  }

  public static String leftJustify(String source, int width, String padCharacter)
  {
    return source + pad(width - source.length(), padCharacter);
  }

  public static String centerJustify(String source, int width, String padCharacter)
  {
    width = width - source.length();
    if (width > 0)
    {
      int left = width / 2;
      int right = width / 2 + width % 2;
      return pad(left, padCharacter) + source + pad(right, padCharacter);
    }
    else
    {
      return source;
    }
  }

  public static String pad(int width, String padCharacter)
  {
    StringBuilder stringBuilder = new StringBuilder();
    while (width > 0)
    {
      stringBuilder.append(padCharacter);
      width--;
    }
    return stringBuilder.toString();
  }

  public static String to16BitHex(int value)
  {
    return rightJustify(toHexString(value), 4, "0");
  }

  public static String to12BitHex(int value)
  {
    return rightJustify(toHexString(value), 3, "0");
  }

  public static String to8BitHex(int value)
  {
    return rightJustify(toHexString(value), 2, "0");
  }

  public static String to4BitHex(int value)
  {
    return toHexString(value);
  }

  public static String getByteStringHex(int value)
  {
    return "0x" + to8BitHex(value);
  }

  public static String getWordStringHex(int value)
  {
    return "0x" + to16BitHex(value);
  }

  public static String getNybbleStringHex(int value)
  {
    return "0x" + to4BitHex(value);
  }

  public static String toEnumString(Enum<?> anEnum)
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

  public static List<String> split(String string, String separator)
  {
    List<String> strings = new ArrayList<>();

    if (separator.length() > 1)
    {
      throw new SimulatorException("Separator[%s] must be one character long", separator);
    }

    char charSeparator = separator.charAt(0);

    int index = 0;
    for (int i = 0; i < string.length(); i++)
    {
      char c = string.charAt(i);
      if (charSeparator == c)
      {
        strings.add(string.substring(index, i));
        index = i + 1;
      }
    }

    int length = string.length();
    if (index > length)
    {
      strings.add("");
    }
    else
    {
      strings.add(string.substring(index, length));
    }

    return strings;
  }

  public static String stripSurroundingWhitespace(String value)
  {
    return stripSurroundingCharacters(value, ' ');
  }

  public static String stripSurroundingCharacters(String string, char match)
  {
    if (string == null)
    {
      return null;
    }

    if (StringUtil.containsOnly(string, match))
    {
      return "";
    }

    int start = leadingCharacterEnd(string, match);
    int end = trailingCharacterStart(string, match);

    return string.substring(start, end);
  }

  public static boolean containsOnly(String string, char match)
  {
    for (int i = 0; i < string.length(); i++)
    {
      char c = string.charAt(i);
      if (c != match)
      {
        return false;
      }
    }
    return true;
  }

  public static int leadingCharacterEnd(String string, char match)
  {
    int endOfWhiteSpace = string.length();

    for (int i = 0; i < string.length(); i++)
    {
      char c = string.charAt(i);

      if (c != match)
      {
        endOfWhiteSpace = i;
        break;
      }
    }
    return endOfWhiteSpace;
  }

  public static int trailingCharacterStart(String string, char match)
  {
    int startOfWhiteSpace = 0;
    int end = string.length() - 1;

    for (int i = end; i >= 0; i--)
    {
      char c = string.charAt(i);
      if (c != match)
      {
        startOfWhiteSpace = i + 1;
        break;
      }
    }
    return startOfWhiteSpace;
  }

  public static List<String> splitAndTrim(String string, String separator)
  {
    if (string == null)
    {
      return new ArrayList<>();
    }
    if (string.trim().isEmpty())
    {
      return new ArrayList<>();
    }

    List<String> tokens = StringUtil.split(string, separator);
    for (int i = 0; i < tokens.size(); i++)
    {
      String token = tokens.get(i);
      token = token.trim();
      tokens.set(i, token);
    }
    return tokens;
  }
}

