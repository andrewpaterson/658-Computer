package net.common.parser;

public class EscapeCodes
{
  public static char GetEscapeCode(char cCurrent)
  {
    if (cCurrent == 'n')
    {
      return '\n';
    }
    else if (cCurrent == '\\')
    {
      return '\\';
    }
    else if (cCurrent == '\"')
    {
      return '\"';
    }
    else if (cCurrent == '\'')
    {
      return '\'';
    }
    else if (cCurrent == '0')
    {
      return '\0';
    }
    else if (cCurrent == 'b')
    {
      return '\b';
    }
    else if (cCurrent == 'f')
    {
      return '\f';
    }
    else if (cCurrent == 'r')
    {
      return '\r';
    }
    else if (cCurrent == 't')
    {
      return '\t';
    }

    //If @ is returned there was an error.
    return '@';
  }

  char GetHexChar(char c4Bit)
  {
    if (c4Bit < 10)
    {
      return (char) ('0' + c4Bit);
    }
    else if (c4Bit < 16)
    {
      return (char) ('a' + c4Bit - 10);
    }
    else
    {
      return '?';
    }
  }

  String StrEscapeUnicode(char c)
  {
    char c1;
    char c2;
    char c3;
    char c4;

    c1 = (char) (c & 0xf);
    c2 = (char) ((c & 0xf0) >> 4);
    c3 = (char) ((c & 0xf00) >> 8);
    c4 = (char) ((c & 0xf000) >> 12);

    return "\\" + 'u' +
           GetHexChar(c4) +
           GetHexChar(c3) +
           GetHexChar(c2) +
           GetHexChar(c1);
  }

  String StrEscapeHex(char c)
  {
    char cRight;
    char cLeft;

    cRight = (char) (c & 0xf);
    cLeft = (char) ((c & 0xf0) >> 4);

    return "\\" +
           'x' +
           GetHexChar(cLeft) +
           GetHexChar(cRight);
  }

  String StrEscapeChar(char c)
  {
    return "\\" + c;
  }

  String GetEscapeString(char cCurrent)
  {
    if (cCurrent == '\\')
    {
      return StrEscapeChar('\\');
    }
    else if (cCurrent == '\n')
    {
      return StrEscapeChar('n');
    }
    else if (cCurrent == '\"')
    {
      return StrEscapeChar('\"');
    }
    else if (cCurrent == '\'')
    {
      return StrEscapeChar('\'');
    }
    else if (cCurrent == '\0')
    {
      return StrEscapeChar('0');
    }
    else if (cCurrent == '\b')
    {
      return StrEscapeChar('b');
    }
    else if (cCurrent == '\f')
    {
      return StrEscapeChar('f');
    }
    else if (cCurrent == '\r')
    {
      return StrEscapeChar('r');
    }
    else if (cCurrent == '\t')
    {
      return StrEscapeChar('t');
    }
    else if (((cCurrent >= 32) && (cCurrent <= 126)) || (cCurrent == 128) || ((cCurrent >= 130) && (cCurrent <= 254)))
    {
      return "" + cCurrent;
    }
    else
    {
      return StrEscapeHex(cCurrent);
    }
  }

//  char*  GetEscapeString(char16 cCurrent, char*szDest)
//  {
//    StrEscapeUnicode(cCurrent, szDest);
//    return szDest;
//  }
}
