package net.common.util;

import net.common.SimulatorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.toHexString;

public abstract class StringUtil
{
  protected static final String separatorString = ",";
  protected static final String quoteString = "\"";
  protected static final String quoteQuoteString = quoteString + quoteString;
  protected static final String lineFeedString = "\n";

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
    if (anEnum != null)
    {
      String stringWithoutUnderscores = anEnum.toString().toLowerCase().replace('_', ' ');
      return capitaliseEachWord(stringWithoutUnderscores);
    }
    else
    {
      return "";
    }
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

  public static String separateList(List<String> strings, String separator, String lastSeparator)
  {
    if (strings == null || strings.size() == 0)
    {
      return "";
    }

    StringBuilder stringBuffer = new StringBuilder();
    for (int i = 0; i < strings.size(); i++)
    {
      String s = strings.get(i);
      stringBuffer.append(s);
      if (i < strings.size() - 2)
      {
        stringBuffer.append(separator);
      }
      else if (i == strings.size() - 2)
      {
        stringBuffer.append(lastSeparator);
      }
    }
    return stringBuffer.toString();
  }

  public static String separateArray(String[] strings, String separator)
  {
    List<String> list = Arrays.asList(strings);
    return separateList(list, separator);
  }

  public static String commaSeparateList(List<String> strings)
  {
    return commaSeparateList(strings, ", ");
  }

  public static String commaSeparateList(String... strings)
  {
    return commaSeparateList(CollectionUtil.newList(strings), ", ");
  }

  public static String commaSeparateList(List<String> strings, String lastSeparator)
  {
    return separateList(strings, ", ", lastSeparator);
  }

  public static String convertToCSVRow(List<String> strings)
  {
    List<String> result = new ArrayList<>(strings.size());
    for (String string : strings)
    {
      result.add(modifyForCSV(string));
    }
    return separateList(result, ",");
  }

  private static String modifyForCSV(String string) //also see BaseCSVFileWriter
  {
    if (string != null && string.length() > 0)
    {
      boolean containsQuote = string.contains(quoteString);
      boolean mustDelimit = mustDelimit(string, containsQuote);

      StringBuilder stringBuilder = new StringBuilder();
      if (mustDelimit)
      {
        stringBuilder.append(quoteString);
      }

      if (containsQuote)
      {
        string = string.replaceAll(quoteString, quoteQuoteString);
      }
      stringBuilder.append(string);

      if (mustDelimit)
      {
        stringBuilder.append(quoteString);
      }

      return stringBuilder.toString();
    }
    else
    {
      return "";
    }
  }

  private static boolean mustDelimit(String string, boolean containsQuote)
  {
    return containsQuote || string.contains(separatorString) || string.contains(lineFeedString);
  }

  public static String separateList(List<String> strings, String separator)
  {
    return separateList(strings, separator, separator);
  }

  public static String stripDoubleQuotes(String value)
  {
    return stripSurroundingCharacters(value, "\"");
  }

  public static String stripSingleQuotes(String value)
  {
    return stripSurroundingCharacters(value, "'");
  }

  private static String stripSurroundingCharacters(String value, String character)
  {
    if (value == null)
    {
      return null;
    }
    if (value.length() >= 2)
    {
      if ((value.startsWith(character)) && (value.endsWith(character)))
      {
        return value.substring(1, value.length() - 1);
      }
    }
    return value;
  }

  public static int occurrencesOf(String text, char c)
  {
    if (text == null)
    {
      return 0;
    }

    int count = 0;
    for (int i = 0; i < text.length(); i++)
    {
      if (text.charAt(i) == c)
      {
        count++;
      }
    }
    return count;
  }

  public static String javaNameToHumanReadable(String name)
  {
    return javaNameToHumanReadable(name, false);
  }

  public static String javaNameToHumanReadable(String name, boolean separateOutNumbers)
  {
    if (name == null)
    {
      return null;
    }
    if (name.equals(""))
    {
      return "";
    }

    StringBuilder buffer = new StringBuilder();
    name = name.replace('_', ' ');

    for (int i = 0; i < name.length(); i++)
    {
      char charCurr = name.charAt(i);
      char charOut;
      char charNext = 'Z';
      char charPrev = 'z';
      if (i < name.length() - 1)
      {
        charNext = name.charAt(i + 1);
      }
      if (i > 0)
      {
        charPrev = name.charAt(i - 1);
      }
      if (i == 0)
      {
        charOut = Character.toUpperCase(charCurr);
      }
      else
      {
        if (charIsUpperCase(charCurr) && charIsLetter(charNext) && charIsLowerCase(charNext))
        {
          if (charPrev != ' ')
          {
            buffer.append(' ');
          }
          charOut = Character.toLowerCase(charCurr);
        }
        else if (charIsLowerCase(charPrev) && charIsLetter(charPrev) && (charIsUpperCase(charCurr) || (separateOutNumbers && charIsNumeric(charCurr))))
        {
          buffer.append(' ');
          charOut = charCurr;
        }
        else
        {
          charOut = charCurr;
        }
      }
      buffer.append(charOut);
    }
    return buffer.toString();
  }

  public static boolean charIsLetter(char c)
  {
    return (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')));
  }

  private static boolean charIsUpperCase(char c)
  {
    return ((c >= 'A') && (c <= 'Z'));
  }

  private static boolean charIsLowerCase(char c)
  {
    return ((c >= 'a') && (c <= 'z'));
  }

  private static boolean charIsNumeric(char c)
  {
    return (c >= '0') && (c <= '9');
  }

  public static StringBuilder pad(String padder, int size)
  {
    StringBuilder padding = new StringBuilder();
    for (int index = 0; index < size; index++)
    {
      padding.append(padder);
    }
    return padding;
  }

  public static String getClassSimpleName(Object o)
  {
    if (o == null)
    {
      return "null";
    }
    else
    {
      return o.getClass().getSimpleName();
    }
  }
}

