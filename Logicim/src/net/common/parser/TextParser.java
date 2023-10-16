package net.common.parser;

import net.assembler.sixteenhigh.parser.TextParserLog;
import net.common.SimulatorException;
import net.common.parser.primitive.CharPointer;
import net.common.parser.primitive.FloatPointer;
import net.common.parser.primitive.IntegerPointer;
import net.common.parser.primitive.LongPointer;
import net.common.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.common.parser.TextParseError.*;
import static net.common.parser.Tristate.*;

public class TextParser
{
  public static final int NUMBER_PREFIX_DEFAULT = 0x00;
  public static final int NUMBER_PREFIX_BINARY = 0x01;
  public static final int NUMBER_PREFIX_OCTAL = 0x02;
  public static final int NUMBER_PREFIX_HEXADECIMAL = 0x04;

  public static final int INTEGER_PREFIX_ALL = (NUMBER_PREFIX_BINARY | NUMBER_PREFIX_OCTAL | NUMBER_PREFIX_HEXADECIMAL);
  public static final int FLOAT_PREFIX_ALL = NUMBER_PREFIX_HEXADECIMAL;

  public static final int INTEGER_SUFFIX_NONE = 0x0000;
  public static final int INTEGER_SUFFIX_L = 0x0100;
  public static final int INTEGER_SUFFIX_LL = 0x0200;
  public static final int INTEGER_SUFFIX_U = 0x0400;
  public static final int INTEGER_SUFFIX_UL = 0x0800;
  public static final int INTEGER_SUFFIX_ULL = 0x1000;
  public static final int INTEGER_SUFFIX_CPP = (INTEGER_SUFFIX_L | INTEGER_SUFFIX_LL | INTEGER_SUFFIX_U | INTEGER_SUFFIX_UL | INTEGER_SUFFIX_ULL);
  public static final int INTEGER_SUFFIX_JAVA = INTEGER_SUFFIX_L;

  public static final int FLOAT_SUFFIX_NONE = 0x0000;
  public static final int FLOAT_SUFFIX_F = 0x0100;
  public static final int FLOAT_SUFFIX_D = 0x0200;
  public static final int FLOAT_SUFFIX_L = 0x0400;

  public static final int FLOAT_SUFFIX_CPP = (FLOAT_SUFFIX_F | FLOAT_SUFFIX_D | FLOAT_SUFFIX_L);
  public static final int FLOAT_SUFFIX_JAVA = (FLOAT_SUFFIX_F | FLOAT_SUFFIX_D);

  public static final int FLOAT_EXPONENT_DEFAULT = 0x000000;
  public static final int FLOAT_EXPONENT_DECIMAL = 0x100000;
  public static final int FLOAT_EXPONENT_BINARY = 0x200000;
  public static final int FLOAT_EXPONENT_ALL = (FLOAT_EXPONENT_DECIMAL | FLOAT_EXPONENT_BINARY);

  public static final int NUMBER_SEPARATOR_UNDERSCORE = 0x10000000;
  public static final int NUMBER_SEPARATOR_APOSTROPHE = 0x20000000;
  public static final int NUMBER_SEPARATOR_NONE = 0x00000000;

  private StringZero text;
  private int position;
  private boolean outsideText;
  private List<Integer> mcPositions;
  private TextParserLog log;
  private String filename;
  private int miLine;
  private int miColumn;
  private TextParseError meError;

  public TextParser(File file, TextParserLog log) throws IOException
  {
    FileReader fileReader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    text = new StringZero((int) file.length() + 1);
    int read = bufferedReader.read(text.getValues());
    if (read == -1)
    {
      text.setEnd((int) file.length());
      initialise(log, file.getName());
    }
    else
    {
      throw new SimulatorException("Could not fully read file [%s].", file.getName());
    }
  }

  public TextParser(char[] text, TextParserLog log, String filename)
  {
    this.text = new StringZero(text);
    initialise(log, filename);
  }

  public TextParser(String s, TextParserLog log, String filename)
  {
    text = new StringZero(s);
    initialise(log, filename);
  }

  private void initialise(TextParserLog log, String filename)
  {
    this.meError = NotSet;
    this.miLine = 0;
    this.miColumn = 0;
    this.position = 0;
    this.log = log;
    this.filename = filename;
    this.mcPositions = new ArrayList<>();
  }

  private int textLength()
  {
    return text.length();
  }

  private void stepRight()
  {
    //Can only move right if we are not sitting in the last character.
    if (position <= textLength())
    {
      if (text.get(position) == '\n')
      {
        miLine++;
        miColumn = 0;
      }
      else
      {
        miColumn++;
      }
      outsideText = false;
      position++;
    }
    testEnd();
  }

  private void stepLeft()
  {
    //Can only move right if we are not sitting in the last character.
    if (position >= 0)
    {
      position--;
      if (text.get(position) == '\n')
      {
        miLine--;

        int szSearchPos = position - 1;
        miColumn = 0;
        if (szSearchPos > 0)
        {
          while ((text.get(szSearchPos) != '\n') && (szSearchPos != 0))
          {
            miColumn++;
            szSearchPos--;
          }
        }
      }
      else
      {
        miColumn--;
      }
    }
    testEnd();
  }

  private void testEnd()
  {
    if ((position >= 0) && (position <= textLength()))
    {
      outsideText = false;
      return;
    }
    outsideText = true;
  }

  private boolean isWhiteSpace(char cCurrent)
  {
    return ((cCurrent == ' ') || (cCurrent == '\n') || (cCurrent == '\t'));
  }

  public void skipWhiteSpace()
  {
    skipWhiteSpace(true);
  }

  public void skipWhiteSpace(boolean bSkipComments)
  {
    char cCurrent;

    for (; ; )
    {
      if (outsideText)
      {
        return;
      }

      cCurrent = text.get(position);

      //Nice clean white space...
      if (isWhiteSpace(cCurrent))
      {
        stepRight();
      }

      //Possibly nasty comments...
      else if (cCurrent == '/')
      {
        if (bSkipComments)
        {
          stepRight();

          if (!outsideText)
          {
            cCurrent = text.get(position);
            if (cCurrent == '*')
            {
              //Put the parser back where it was.
              stepLeft();
              if (!skipCStyleComment())
              {
                break;
              }
            }
            else if (cCurrent == '/')
            {
              //Put the parser back where it was.
              stepLeft();
              if (!skipCPPStyleComment())
              {
                break;
              }
            }
            else
            {
              //Was something other than white-space starting with /
              stepLeft();
              break;
            }
          }
        }
        else
        {
          //Not skipping comments.
          break;
        }
      }
      else
      {
        //Was not white-space at all.
        break;
      }
    }
  }

  private boolean skipCStyleComment()
  {
    return skipCStyleComment(null, null);
  }

  private boolean skipCStyleComment(IntegerPointer begin)
  {
    return skipCStyleComment(begin, null);
  }

  private boolean skipCStyleComment(IntegerPointer begin, IntegerPointer end)
  {
    char cCurrent;
    int iDepth;

    iDepth = 0;
    safeAssign(begin, -1);
    safeAssign(end, -1);

    pushPosition();
    for (; ; )
    {
      if (outsideText)
      {
        passPosition();
        return true;
      }

      cCurrent = text.get(position);
      if (cCurrent == '/')
      {
        stepRight();
        if (!outsideText)
        {
          cCurrent = text.get(position);
          if (cCurrent == '*')
          {
            iDepth++;
            if (iDepth == 1)
            {
              safeAssign(begin, position + 1);
            }
          }
          else
          {
            //Wasn't a comment start... step back.
            stepLeft();
          }
        }
        else
        {
          passPosition();
          return true;
        }
      }
      else if (cCurrent == '*')
      {
        stepRight();
        if (!outsideText)
        {
          cCurrent = text.get(position);
          if (cCurrent == '/')
          {
            safeAssign(end, position - 2);
            stepRight();
            return true;
          }
          else
          {
            //Wasn't the end of a comment... step back...
            stepLeft();
          }
        }
        else
        {
          passPosition();
          return true;
        }
      }

      if (iDepth == 0)
      {
        //No more nested comments...  bail..
        return true;
      }
      stepRight();
    }
  }

  private boolean skipCPPStyleComment()
  {
    return skipCStyleComment(null, null);
  }

  private boolean skipCPPStyleComment(IntegerPointer begin)
  {
    return skipCStyleComment(begin, null);
  }

  private boolean skipCPPStyleComment(IntegerPointer begin, IntegerPointer end)
  {
    char cCurrent;
    int iCount;

    if (outsideText)
    {
      return true;
    }

    pushPosition();
    cCurrent = text.get(position);

    safeAssign(begin, -1);
    safeAssign(end, -1);

    if (cCurrent == '/')
    {
      stepRight();
      if (!outsideText)
      {
        cCurrent = cCurrent = text.get(position);
        if (cCurrent == '/')
        {
          safeAssign(begin, position + 1);

          for (iCount = 0; ; iCount++)
          {
            stepRight();
            if (!outsideText)
            {
              cCurrent = text.get(position);

              if (cCurrent == '\n')
              {
                safeAssign(end, position - 1);

                //This is the end of the line and the end of the comment.
                stepRight();
                passPosition();
                return true;
              }
            }
            else
            {
              safeAssign(end, textLength());

              passPosition();
              return true;
            }
          }
        }
        else
        {
          popPosition();
          return true;
        }
      }
      else
      {
        //Wasn't a comment.
        stepLeft();
        return true;
      }
    }
    popPosition();
    return true;
  }

  public Tristate getExactCharacter(char c)
  {
    return getExactCharacter(c, true);
  }

  public Tristate getExactCharacter(char c, boolean skipWhiteSpace)
  {
    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }
    if (!outsideText)
    {
      if (text.get(position) == c)
      {
        stepRight();
        return TRUE;
      }
      return FALSE;
    }
    else
    {
      return ERROR;
    }
  }

  public Tristate getCharacter(StringZero pc)
  {
    if (!outsideText)
    {
      pc.set(0, text.get(position));
      stepRight();
      return TRUE;
    }
    else
    {
      return ERROR;
    }
  }

  public Tristate getExactCaseInsensitiveCharacter(char c, boolean skipWhiteSpace)
  {
    char c1, c2;

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    if (!outsideText)
    {
      c1 = Character.toUpperCase(text.get(position));
      c2 = Character.toUpperCase(c);
      if (c1 == c2)
      {
        stepRight();
        return TRUE;
      }
      return FALSE;
    }
    else
    {
      setErrorEndOfFile();
      return ERROR;
    }
  }

  public Tristate getEnumeratedCharacter(String szCharacters, CharPointer c, boolean skipWhiteSpace)
  {
    int iNumCharacters;
    char cCurrent;
    Tristate tResult;

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    iNumCharacters = szCharacters.length();
    for (int i = 0; i < iNumCharacters; i++)
    {
      cCurrent = szCharacters.charAt(i);
      tResult = getExactCharacter(cCurrent, false);
      if (tResult == ERROR)
      {
        return ERROR;
      }
      else if (tResult == TRUE)
      {
        if (c != null)
        {
          c.value = cCurrent;
          return TRUE;
        }
      }
    }
    return FALSE;
  }

  public Tristate getExactCharacterSequence(String szSequence)
  {
    return getExactCharacterSequence(szSequence, true);
  }

  public Tristate getExactCharacterSequence(String szSequence, boolean skipWhiteSpace)
  {
    char cCurrent;
    int iPos;

    iPos = 0;
    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    //Make sure we're not off the end of the file.
    if (outsideText)
    {
      popPosition();
      setErrorEndOfFile();
      return ERROR;
    }

    for (; ; )
    {
      if (iPos == szSequence.length())
      {
        //Got all the way to the null character.
        passPosition();
        return TRUE;
      }

      if (!outsideText)
      {
        cCurrent = text.get(position);
        if (cCurrent == szSequence.charAt(iPos))
        {
          stepRight();
          iPos++;
        }
        else
        {
          //Put the parser back where it was.
          popPosition();
          return FALSE;
        }
      }
      else
      {
        //Put the parser back where it was.
        popPosition();
        return FALSE;
      }
    }
  }

  public Tristate getExactCaseInsensitiveCharacterSequence(String szSequence, boolean skipWhiteSpace)
  {
    int iPos;
    char c1, c2;

    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    iPos = 0;

    //Make sure we're not off the end of the file.
    if (outsideText)
    {
      popPosition();
      setErrorEndOfFile();
      return ERROR;
    }

    for (; ; )
    {
      if (iPos == szSequence.length())
      {
        //Got all the way to the null character.
        passPosition();
        return TRUE;
      }
      if (!outsideText)
      {
        c1 = Character.toUpperCase(text.get(position));
        c2 = Character.toUpperCase(szSequence.charAt(iPos));
        if (c1 == c2)
        {
          stepRight();
          iPos++;
        }
        else
        {
          //Put the parser back where it was.
          popPosition();
          return FALSE;
        }
      }
      else
      {
        //Put the parser back where it was.
        popPosition();
        return FALSE;
      }
    }
  }

  public Tristate getDebugCharacterSequence(String szSequence)
  {
    Tristate tResult;

    pushPosition();
    tResult = getExactCharacterSequence(szSequence);
    popPosition();
    return tResult;
  }

  public Tristate getIdentifierCharacter(CharPointer pc, boolean bFirst)
  {
    char cCurrent;

    if (!outsideText)
    {
      cCurrent = text.get(position);
      pc.value = cCurrent;
      //The first character of an identifier must be one of these...
      if (((cCurrent >= 'a') && (cCurrent <= 'z')) || ((cCurrent >= 'A') && (cCurrent <= 'Z')) || (cCurrent == '_'))
      {
        stepRight();
        return TRUE;
      }

      //Additional characters can also be...
      if (!bFirst)
      {
        if ((cCurrent >= '0') && (cCurrent <= '9'))
        {
          stepRight();
          return TRUE;
        }
      }
      return FALSE;
    }
    else
    {
      return ERROR;
    }
  }

  public Tristate getExactIdentifier(String identifier, boolean skipWhiteSpace)
  {
    if (identifier == null)
    {
      throw new SimulatorException("Identifier may not be null.");
    }
    CharPointer cCurrent = new CharPointer();
    int iPos;
    Tristate tResult;

    iPos = 0;
    pushPosition();
    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    //Make sure we're not off the end of the file.
    if (outsideText)
    {
      popPosition();
      log.logFatal(filename, position, "Expected exact identifier %s.  Instead outside text.", identifier);
      return ERROR;
    }

    for (; ; )
    {
      if (!outsideText)
      {
        cCurrent.value = text.get(position);
        if (iPos == identifier.length())
        {
          //Got all the way to the null character.
          //If there are additional identifier characters then we do not have the right identifier.
          tResult = getIdentifierCharacter(cCurrent, iPos == 0);
          if (tResult == TRUE)
          {
            //Put the parser back where it was.
            popPosition();
            return FALSE;
          }
          passPosition();
          return TRUE;
        }
        if (cCurrent.value == identifier.charAt(iPos))
        {
          stepRight();
          iPos++;
        }
        else
        {
          //Put the parser back where it was.
          popPosition();
          return FALSE;
        }
      }
      else
      {
        //Put the parser back where it was.
        popPosition();
        return FALSE;
      }
    }
  }

  public Tristate getIdentifier(StringZero identifier, boolean skipWhiteSpace)
  {
    CharPointer c = new CharPointer();
    boolean bFirst;
    int iPos;

    bFirst = true;
    iPos = 0;
    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    //Make sure we're not off the end of the file.
    if (outsideText)
    {
      popPosition();
      return ERROR;
    }

    for (; ; )
    {
      if (!outsideText)
      {
        if (getIdentifierCharacter(c, bFirst) != TRUE)
        {
          if (bFirst)
          {
            identifier.setEnd(iPos);
            popPosition();
            return FALSE;
          }
          else
          {
            identifier.setEnd(iPos);
            passPosition();
            return TRUE;
          }
        }
        else
        {
          identifier.set(iPos, c.value);
        }
      }
      else
      {
        if (bFirst)
        {
          popPosition();
          return ERROR;
        }
        else
        {
          identifier.setEnd(iPos);
          passPosition();
          return TRUE;
        }
      }
      bFirst = false;
      iPos++;
    }
  }

  public Tristate getIdentifier(List<String> allowedIdentifiers, IntegerPointer index, boolean skipWhiteSpace)
  {
    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    for (int i = 0; i < allowedIdentifiers.size(); i++)
    {
      String identifier = allowedIdentifiers.get(i);
      Tristate state = getExactIdentifier(identifier, false);
      if (state == TRUE)
      {
        index.value = i;
        return TRUE;
      }
      else if (state == ERROR)
      {
        return ERROR;
      }
    }
    return FALSE;
  }

  public Tristate getQuotedCharacterSequence(char openQuote,
                                             char closeQuote,
                                             StringZero stringZero,
                                             IntegerPointer length,
                                             boolean passOnTest,
                                             boolean skipWhiteSpace,
                                             boolean allowEscapeCharacters)
  {
    int iPos;
    char cCurrent;
    Tristate tReturn;
    Tristate tResult;
    CharPointer cEscape = new CharPointer();

    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    if (!outsideText)
    {
      tResult = getExactCharacter(openQuote, false);
      if (tResult == TRUE)
      {
        iPos = 0;
        for (; ; )
        {
          if (!outsideText)
          {
            cCurrent = text.get(position);
            if (cCurrent == closeQuote)
            {
              if (stringZero != null)
              {
                stringZero.set(iPos, '\0');
              }

              stepRight();

              if (stringZero != null || passOnTest)
              {
                passPosition();
              }
              else
              {
                popPosition();
              }

              safeAssign(length, iPos);
              return TRUE;
            }
            //We have an escape character...
            else if (cCurrent == '\\' && allowEscapeCharacters)
            {
              stepRight();
              tReturn = getEscapeCode(cEscape);
              if (stringZero != null)
              {
                stringZero.set(iPos, cEscape.value);
              }

              iPos++;
              if (tReturn == FALSE)
              {
                popPosition();
                setErrorSyntaxError();
                return ERROR;
              }
              else if (tReturn == ERROR)
              {
                popPosition();
                //Don't set the error here, it's already been set by getEscapeCode
                return ERROR;
              }
            }
            else if (cCurrent == '\n')
            {
              //Just ignore new lines.
              stepRight();
            }
            else
            {
              if (stringZero != null)
              {
                stringZero.set(iPos, cCurrent);
              }

              iPos++;
              stepRight();
            }
          }
          else
          {
            //This has never been tested.
            popPosition();
            setErrorSyntaxError();
            return ERROR;
          }
        }
      }
      else
      {
        //No quote so not a string.
        popPosition();
        return tResult;
      }
    }
    else
    {
      popPosition();
      setErrorEndOfFile();
      return ERROR;
    }
  }

  public Tristate getString(StringZero szString)
  {
    int iPos;
    char cCurrent;
    Tristate tReturn;

    pushPosition();
    skipWhiteSpace();

    if (!outsideText)
    {
      if (getExactCharacter('\"', false) == TRUE)
      {
        iPos = 0;
        for (; ; )
        {
          if (!outsideText)
          {
            cCurrent = text.get(position);
            switch (cCurrent)
            {
              case '\"':
                szString.setEnd(iPos);
                stepRight();
                passPosition();
                return TRUE;

              //We have an escape character...
              case '\\':
                tReturn = getEscapeCode();
                iPos++;
                if ((tReturn == FALSE) || (tReturn == ERROR))
                {
                  popPosition();
                  return ERROR;
                }
                break;
              case '\n':
                //Just ignore new lines.
                stepRight();
                break;
              case '\r':
                //Just ignore carriage returns.
                stepRight();
                break;
              default:
                szString.set(iPos, cCurrent);
                iPos++;
                stepRight();
                break;
            }
          }
        }
      }
      else
      {
        //No quote so not a string.
        popPosition();
        return FALSE;
      }
    }
    else
    {
      popPosition();
      return ERROR;
    }
  }

  public Tristate getEscapeCode()
  {
    return getEscapeCode(null);
  }

  public Tristate getEscapeCode(CharPointer c)
  {
    char cCurrent;

    if (!outsideText)
    {
      if (text.get(position) == '\\')
      {
        stepRight();
        if (!outsideText)
        {
          cCurrent = text.get(position);
          switch (cCurrent)
          {
            case 'n':
              safeAssign(c, '\n');
              break;
            case '\\':
              safeAssign(c, '\\');
              break;
            case '\"':
              safeAssign(c, '\"');
              break;
            default:
              return ERROR;
          }
          stepRight();
          return TRUE;
        }
        else
        {
          return ERROR;
        }
      }
      else
      {
        return ERROR;
      }
    }
    else
    {
      return ERROR;
    }
  }

  public Tristate getDigit(IntegerPointer pi)
  {
    char cCurrent;

    if (!outsideText)
    {
      cCurrent = text.get(position);
      if ((cCurrent >= '0') && (cCurrent <= '9'))
      {
        pi.value = cCurrent - '0';
        stepRight();
        return TRUE;
      }
      else
      {
        return FALSE;
      }
    }
    else
    {
      return ERROR;
    }
  }

  private Tristate getSign(IntegerPointer pi)
  {
    char cCurrent;

    if (!outsideText)
    {
      pi.value = 1;
      cCurrent = text.get(position);
      if (cCurrent == '-')
      {
        pi.value = -1;
        stepRight();
        return TRUE;
      }
      else if (cCurrent == '+')
      {
        stepRight();
        return TRUE;
      }
      else
      {
        return FALSE;
      }
    }
    else
    {
      setErrorEndOfFile();
      return ERROR;
    }
  }

  public Tristate getDigits(IntegerPointer pi, IntegerPointer iNumDigits)
  {
    int iNum;
    IntegerPointer iSign = new IntegerPointer();
    IntegerPointer iTemp = new IntegerPointer();
    Tristate tReturn;
    boolean bFirstDigit;
    int i;

    pushPosition();
    skipWhiteSpace();

    pi.value = 0;
    i = 0;
    if (!outsideText)
    {
      iNum = 0;

      getSign(iSign);
      bFirstDigit = true;
      for (; ; )
      {
        if (!outsideText)
        {
          tReturn = getDigit(iTemp);
          if (tReturn == TRUE)
          {
            i++;
            iNum *= 10;
            iNum += iTemp.value;
          }
          else if ((tReturn == FALSE) || (tReturn == ERROR))
          {
            if (bFirstDigit)
            {
              //might already have got a sign...  so reset the parser.
              popPosition();
              return FALSE;
            }
            iNum *= iSign.value;
            pi.value = iNum;
            if (iNumDigits != null)
            {
              iNumDigits.value = i;
            }
            passPosition();
            return TRUE;
          }
          bFirstDigit = false;
        }
        else
        {
          //Got only a sign then end of file.
          popPosition();
          return ERROR;
        }
      }
    }
    else
    {
      popPosition();
      return ERROR;
    }
  }

  public Tristate getFloat(FloatPointer pf)
  {
    IntegerPointer iLeft = new IntegerPointer();
    IntegerPointer iRight = new IntegerPointer();
    Tristate tReturn;
    IntegerPointer iNumDecimals = new IntegerPointer();
    double fLeft;
    double fRight;
    double fTemp;
    boolean bLeft;

    pushPosition();
    skipWhiteSpace();

    pf.value = 0.0;
    if (!outsideText)
    {
      //Try and get the mantissa.
      tReturn = getDigits(iLeft, null);
      bLeft = true;

      //Just return on errors an non-numbers.
      if (tReturn == FALSE)
      {
        //There may still be a decimal point...
        iLeft.value = 0;
        bLeft = false;
      }
      else if (tReturn == ERROR)
      {
        popPosition();
        return ERROR;
      }

      fLeft = iLeft.value;
      if (!outsideText)
      {
        tReturn = getExactCharacter('.', false);
        if (tReturn == TRUE)
        {
          tReturn = getDigits(iRight, iNumDecimals);
          if (tReturn == TRUE)
          {
            fRight = iRight.value;
            fTemp = Math.pow(10.0f, (-iNumDecimals.value));
            fRight *= fTemp;

            pf.value = fLeft + fRight;
            passPosition();
            return TRUE;
          }
          else
          {
            //A decimal point must be followed by a number.
            popPosition();
            return ERROR;
          }
        }
        else
        {
          //No decimal point...
          if (!bLeft)
          {
            //No digits and no point...
            popPosition();
            return FALSE;
          }
          else
          {
            pf.value = fLeft;
            passPosition();
            return TRUE;
          }
        }
      }
      else
      {
        //No decimal point...
        if (!bLeft)
        {
          //No digits and no point...
          popPosition();
          return FALSE;
        }
        else
        {
          pf.value = fLeft;
          passPosition();
          return TRUE;
        }
      }
    }
    else
    {
      popPosition();
      return ERROR;
    }
  }

  public TextParserPosition saveSettings()
  {
    TextParserPosition textPosition = new TextParserPosition();
    textPosition.position = position;
    textPosition.positions = new ArrayList<>(mcPositions);
    return textPosition;
  }

  public void loadSettings(TextParserPosition textPosition)
  {
    position = textPosition.position;
    mcPositions = new ArrayList<>(textPosition.positions);
    testEnd();
  }

  public Tristate findExactIdentifier(String identifier)
  {
    int szPosition;
    Tristate result;

    pushPosition();
    skipWhiteSpace();

    for (; ; )
    {
      szPosition = position;
      result = getExactIdentifier(identifier, false);
      if (result == ERROR)
      {
        //We've reached the end of the file without finding the identifier.
        popPosition();
        return FALSE;
      }
      else if (result == FALSE)
      {
        //Try the next actual character along.
        stepRight();
        skipWhiteSpace();
      }
      else if (result == TRUE)
      {
        position = szPosition;
        passPosition();
        return TRUE;
      }
    }
  }

  public Tristate findExactCharacterSequence(String szSequence)
  {
    int szPosition;
    Tristate result;

    pushPosition();
    skipWhiteSpace();

    for (; ; )
    {
      szPosition = position;
      result = getExactCharacterSequence(szSequence);
      if (result == ERROR)
      {
        //We've reached the end of the file without finding the identifier.
        popPosition();
        return FALSE;
      }
      else if (result == FALSE)
      {
        //Try the next actual character along.
        stepRight();
        skipWhiteSpace();
      }
      else if (result == TRUE)
      {
        position = szPosition;
        passPosition();
        return TRUE;
      }
    }
  }

  public void restart()
  {
    initialise(log, filename);
    pushPosition();
    testEnd();
  }

  public Tristate findStartOfLine()
  {
    char cCurrent;

    pushPosition();

    //If we're off the end of the file we can't return the beginning of the line.
    if (outsideText)
    {
      popPosition();
      return ERROR;
    }

    for (; ; )
    {
      stepLeft();

      //If we have no more text then the start of the line is the start of the text.
      if (outsideText)
      {
        position = 0;
        passPosition();
        return TRUE;
      }

      //If we get find an end of line character we've gone to far, go right again.
      cCurrent = text.get(position);
      if ((cCurrent == '\n') || (cCurrent == '\r'))
      {
        stepRight();
        passPosition();
        return TRUE;
      }
    }
  }

  public void pushPosition()
  {
    mcPositions.add(position);
  }

  public void popPosition()
  {
    position = mcPositions.get(mcPositions.size() - 1);
    mcPositions.remove(mcPositions.size() - 1);
    testEnd();
  }

  public void passPosition()
  {
    mcPositions.remove(mcPositions.size() - 1);
  }

  @Override
  public String toString()
  {
    String s = text.toString();
    s = s.replaceAll("\\r\\n|\\r|\\n", " ");
    s = s + "\n";
    s = s + StringUtil.pad(" ", position - 1);
    s = s + "^";
    return "position [" + position + "] " + "text: \n" + s + "";
  }

  public TextParserLog getLog()
  {
    return log;
  }

  public int getPosition()
  {
    return position;
  }

  public boolean hasMoreText()
  {
    return !outsideText;
  }

  private Tristate getDigit(IntegerPointer pi, int iBase)
  {
    char cCurrent;
    int iDigit;

    if (!outsideText)
    {
      cCurrent = text.get(position);
      iDigit = getDigit(cCurrent, iBase);
      if (iDigit != -1)
      {
        pi.value = iDigit;
        stepRight();
        return TRUE;
      }
      else
      {
        return FALSE;
      }
    }
    else
    {
      setErrorEndOfFile();
      return ERROR;
    }
  }

  private int getDigit(char cCurrent, int iBase)
  {
    if (iBase <= 10)
    {
      if ((cCurrent >= '0') && (cCurrent <= ('0' + iBase - 1)))
      {
        return cCurrent - '0';
      }
      else
      {
        return -1;
      }
    }
    else
    {
      cCurrent = Character.toUpperCase(cCurrent);

      if ((cCurrent >= '0') && (cCurrent <= '9'))
      {
        return cCurrent - '0';
      }
      else if ((cCurrent >= 'A') && (cCurrent <= ('A' + iBase - 11)))
      {
        return cCurrent - 'A' + 10;
      }
      else
      {
        return -1;
      }
    }
  }

  private boolean isDigit(char cCurrent, int iBase)
  {
    if (iBase <= 10)
    {
      if ((cCurrent >= '0') && (cCurrent <= ('0' + iBase - 1)))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else
    {
      cCurrent = Character.toUpperCase(cCurrent);

      if ((cCurrent >= '0') && (cCurrent <= '9'))
      {
        return true;
      }
      else if ((cCurrent >= 'A') && (cCurrent <= ('A' + iBase - 11)))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
  }

  private Tristate getIntegerSuffix(IntegerPointer piSuffix, int iAllowedSuffix)
  {
    char cCurrent;
    boolean bU;
    boolean bL;

    if (!outsideText)
    {
      bU = false;
      bL = false;

      pushPosition();

      cCurrent = text.get(position);
      if (((iAllowedSuffix & INTEGER_SUFFIX_ULL) != 0) || ((iAllowedSuffix & INTEGER_SUFFIX_UL) != 0) || ((iAllowedSuffix & INTEGER_SUFFIX_U) != 0))
      {
        if ((cCurrent == 'u') || (cCurrent == 'U'))
        {
          bU = true;
          stepRight();

          if (outsideText)
          {
            piSuffix.value = INTEGER_SUFFIX_U;
            passPosition();
            return TRUE;
          }
        }
        else if ((cCurrent == 'l') || (cCurrent == 'L'))
        {
        }
        else
        {
          popPosition();
          return FALSE;
        }
      }

      cCurrent = text.get(position);
      if (((iAllowedSuffix & INTEGER_SUFFIX_ULL) != 0) || ((iAllowedSuffix & INTEGER_SUFFIX_UL) != 0) || ((iAllowedSuffix & INTEGER_SUFFIX_LL) != 0) || ((iAllowedSuffix & INTEGER_SUFFIX_L) != 0))
      {
        if ((cCurrent == 'l') || (cCurrent == 'L'))
        {
          bL = true;
          stepRight();

          if (outsideText)
          {
            if (bU)
            {
              piSuffix.value = INTEGER_SUFFIX_UL;
            }
            else
            {
              piSuffix.value = INTEGER_SUFFIX_L;
            }
            passPosition();
            return TRUE;
          }
        }
        else
        {
          if (bU && bL)
          {
            piSuffix.value = INTEGER_SUFFIX_UL;
          }
          else if (bL && !bU)
          {
            piSuffix.value = INTEGER_SUFFIX_L;
          }
          else if (!bL && !bU)
          {
            piSuffix.value = INTEGER_SUFFIX_NONE;
          }
          passPosition();
          return TRUE;
        }
      }

      cCurrent = text.get(position);
      if (((iAllowedSuffix & INTEGER_SUFFIX_ULL) != 0) || ((iAllowedSuffix & INTEGER_SUFFIX_LL) != 0))
      {
        if ((cCurrent == 'l') || (cCurrent == 'L'))
        {
          stepRight();

          if (outsideText)
          {
            if (bU && bL)
            {
              piSuffix.value = INTEGER_SUFFIX_ULL;
            }
            else if (!bU && bL)
            {
              piSuffix.value = INTEGER_SUFFIX_LL;
            }
            else
            {
              popPosition();
              return FALSE;
            }
            passPosition();
            return TRUE;
          }
          else
          {
            if (bU && bL)
            {
              piSuffix.value = INTEGER_SUFFIX_ULL;
            }
            else if (!bU && bL)
            {
              piSuffix.value = INTEGER_SUFFIX_LL;
            }
            else
            {
              popPosition();
              return FALSE;
            }
            passPosition();
            return TRUE;
          }
        }
        else
        {
          if (bU && bL)
          {
            piSuffix.value = INTEGER_SUFFIX_UL;
          }
          else if (!bU && bL)
          {
            piSuffix.value = INTEGER_SUFFIX_L;
          }
          else
          {
            popPosition();
            return FALSE;
          }
          passPosition();
          return TRUE;
        }
      }
      else
      {
        if (bU && bL)
        {
          piSuffix.value = INTEGER_SUFFIX_UL;
        }
        else if (!bU && bL)
        {
          piSuffix.value = INTEGER_SUFFIX_L;
        }
        else
        {
          popPosition();
          return FALSE;
        }
        passPosition();
        return TRUE;
      }
    }
    else
    {
      setErrorEndOfFile();
      return ERROR;
    }
  }

  private Tristate getFloatSuffix(IntegerPointer piSuffix, int iAllowedSuffix)
  {
    char cCurrent;

    if (!outsideText)
    {
      pushPosition();

      cCurrent = text.get(position);
      if (((iAllowedSuffix & FLOAT_SUFFIX_F) != 0) && ((cCurrent == 'f') || (cCurrent == 'F')))
      {
        stepRight();

        piSuffix.value = FLOAT_SUFFIX_F;
        passPosition();
        return TRUE;
      }

      if (((iAllowedSuffix & FLOAT_SUFFIX_D) != 0) && ((cCurrent == 'd') || (cCurrent == 'D')))
      {
        stepRight();

        piSuffix.value = FLOAT_SUFFIX_D;
        passPosition();
        return TRUE;
      }

      if (((iAllowedSuffix & FLOAT_SUFFIX_L) != 0) && ((cCurrent == 'l') || (cCurrent == 'L')))
      {
        stepRight();

        piSuffix.value = FLOAT_SUFFIX_D;
        passPosition();
        return TRUE;
      }

      popPosition();
      return FALSE;
    }
    else
    {
      setErrorEndOfFile();
      return ERROR;
    }
  }

  public Tristate getInteger(LongPointer integerPointer, IntegerPointer signPointer, IntegerPointer numDigitsPointer, boolean skipWhiteSpace)
  {
    Tristate tResult;

    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    //Make sure we're not off the end of the file.
    if (outsideText)
    {
      popPosition();
      setErrorEndOfFile();
      return ERROR;
    }

    tResult = getDigits(integerPointer, signPointer, numDigitsPointer, skipWhiteSpace);
    if (tResult == TRUE)
    {
      //Make sure there are no decimals.
      if (text.get(position) == '.')
      {
        popPosition();
        return FALSE;
      }

      passPosition();
      return TRUE;
    }
    popPosition();
    return tResult;
  }

  public Tristate getInteger(IntegerPointer integerPointer, IntegerPointer numDigitsPointer, boolean skipWhiteSpace)
  {
    LongPointer tempPointer = new LongPointer();
    Tristate tReturn;
    IntegerPointer iSign = new IntegerPointer();

    tReturn = getInteger(tempPointer, iSign, numDigitsPointer, skipWhiteSpace);
    integerPointer.value = (int) (tempPointer.value * iSign.value);
    return tReturn;
  }

  public Tristate getInteger(IntegerPointer integerPointer, IntegerPointer numDigitsPointer)
  {
    Tristate tResult;

    pushPosition();
    skipWhiteSpace();

    //Make sure we're not off the end of the file.
    if (outsideText)
    {
      popPosition();
      return ERROR;
    }

    tResult = getDigits(integerPointer, numDigitsPointer);
    if (tResult == TRUE)
    {
      //Make sure there are no decimals.
      if (text.get(position) == '.')
      {
        popPosition();
        return FALSE;
      }

      passPosition();
      return TRUE;
    }
    popPosition();
    return tResult;
  }

  public Tristate getIntegerLiteral(LongPointer integerValue,
                                    int iAllowedPrefix,
                                    IntegerPointer base,
                                    int iAllowedSuffix,
                                    IntegerPointer suffix,
                                    int iAllowedSeparator,
                                    IntegerPointer numDigits,
                                    boolean skipWhiteSpace)
  {
    char cCurrent;
    char cNext;
    boolean bFirstZero;
    int iBase;
    boolean bDone;
    Tristate tResult;
    IntegerPointer iSuffix;
    boolean bSeparator;

    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    iBase = 10;
    bDone = false;

    if (!outsideText)
    {
      cCurrent = text.get(position);

      stepRight();

      if (outsideText)
      {
        safeAssign(suffix, INTEGER_SUFFIX_NONE);
        return getSingleInteger(cCurrent, integerValue, base, numDigits);
      }
      else
      {
        bFirstZero = cCurrent == '0';

        cNext = text.get(position);

        if (bFirstZero)
        {
          if (((cNext == 'x') || (cNext == 'X')) && ((iAllowedPrefix & NUMBER_PREFIX_HEXADECIMAL) != 0))
          {
            iBase = 16;
          }
          else if (((cNext == 'b') || (cNext == 'B')) && ((iAllowedPrefix & NUMBER_PREFIX_BINARY) != 0))
          {
            iBase = 2;
          }
        }

        bSeparator = (((cNext == '_') && ((iAllowedSeparator & NUMBER_SEPARATOR_UNDERSCORE) != 0)) ||
                      ((cNext == '\'') && ((iAllowedSeparator & NUMBER_SEPARATOR_APOSTROPHE) != 0)));

        if (!isDigit(cNext, iBase) && iBase == 10 && !bSeparator)
        {
          iSuffix = new IntegerPointer(INTEGER_SUFFIX_NONE);
          tResult = getIntegerSuffix(iSuffix, iAllowedSuffix);
          if (tResult == TRUE || tResult == FALSE)
          {
            safeAssign(suffix, iSuffix.value);
            return getSingleInteger(cCurrent, integerValue, base, numDigits);
          }
          else
          {
            passPosition();
            return FALSE;
          }
        }

        if (bFirstZero)
        {
          if (iBase == 10)
          {
            if (cNext >= '0' && cNext <= '7')
            {
              iBase = 8;
            }
          }
          else
          {
            stepRight();
          }
        }
        else
        {
          stepLeft();
        }

        tResult = getDigits(integerValue, null, numDigits, false, false, iBase, iAllowedSeparator);
        if (tResult == TRUE)
        {
          iSuffix = new IntegerPointer(INTEGER_SUFFIX_NONE);
          if (!outsideText)
          {
            tResult = getIntegerSuffix(iSuffix, iAllowedSuffix);
            if (tResult == ERROR)
            {
              passPosition();
              return ERROR;
            }
          }

          passPosition();
          safeAssign(suffix, iSuffix.value);
          safeAssign(base, iBase);
          return TRUE;
        }
        else
        {
          popPosition();
          return tResult;
        }
      }
    }
    else
    {
      popPosition();
      setErrorEndOfFile();
      return ERROR;
    }
  }

  private void safeAssign(IntegerPointer pointer, int value)
  {
    if (pointer != null)
    {
      pointer.value = value;
    }
  }

  private void safeAssign(LongPointer pointer, long value)
  {
    if (pointer != null)
    {
      pointer.value = value;
    }
  }

  private void safeAssign(FloatPointer pointer, double value)
  {
    if (pointer != null)
    {
      pointer.value = value;
    }
  }

  private void safeAssign(CharPointer pointer, char value)
  {
    if (pointer != null)
    {
      pointer.value = value;
    }
  }

  public Tristate getFloatLiteral(FloatPointer pldf,
                                  int iAllowedPrefix,
                                  IntegerPointer piBase,
                                  int iAllowedSuffix,
                                  IntegerPointer piSuffix,
                                  int iAllowedExponent,
                                  IntegerPointer piExponent,
                                  int iAllowedSeparator,
                                  IntegerPointer piNumWholeDigits,
                                  IntegerPointer piNumDecinalDigits,
                                  IntegerPointer piNumExponentDigits,
                                  boolean skipWhiteSpace)
  {
    char cCurrent;
    char cNext;
    boolean bFirstZero;
    int iBase;
    boolean bDone;
    Tristate tResult;
    IntegerPointer iSuffix = new IntegerPointer();
    int iExponent;
    boolean bSeparator;
    LongPointer ulliWholeNumber = new LongPointer();
    LongPointer ulliDecimalNumber = new LongPointer();
    LongPointer lliExponentNumber = new LongPointer();
    double ldf;
    IntegerPointer iNumWholeDigits = new IntegerPointer();
    IntegerPointer iNumDecimalDigits = new IntegerPointer();
    IntegerPointer iNumExponentDigits = new IntegerPointer();
    IntegerPointer iSign = new IntegerPointer();

    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    iBase = 10;
    bDone = false;

    if (!outsideText)
    {
      cCurrent = text.get(position);

      stepRight();

      if (outsideText)
      {
        popPosition();
        return FALSE;
      }
      else
      {
        bFirstZero = cCurrent == '0';

        cNext = text.get(position);

        if (bFirstZero)
        {
          if (((cNext == 'x') || (cNext == 'X')) && ((iAllowedPrefix & NUMBER_PREFIX_HEXADECIMAL) != 0))
          {
            iBase = 16;
            stepRight();
            cNext = text.get(position);
          }
        }

        bSeparator = (((cNext == '_') && ((iAllowedSeparator & NUMBER_SEPARATOR_UNDERSCORE) != 0)) ||
                      ((cNext == '\'') && ((iAllowedSeparator & NUMBER_SEPARATOR_APOSTROPHE) != 0)));

        if (iBase != 16)
        {
          stepLeft();
        }

        tResult = getDigits(ulliWholeNumber, null, iNumWholeDigits, false, false, iBase, iAllowedSeparator);
        if (tResult == TRUE)
        {
          cCurrent = text.get(position);
          if (cCurrent == '.')
          {
            stepRight();

            if (outsideText)
            {
              passPosition();
              iExponent = 0;
              iNumDecimalDigits.value = 0;
              ulliDecimalNumber.value = 0;
              lliExponentNumber.value = 0;
              iNumExponentDigits.value = 0;
              iSuffix.value = FLOAT_SUFFIX_NONE;
              ldf = MakeLongDouble(iBase, ulliWholeNumber.value, ulliDecimalNumber.value, iNumDecimalDigits.value, lliExponentNumber.value);
              safeAssign(pldf, ldf);
              safeAssign(piExponent, iExponent);
              safeAssign(piNumWholeDigits, iNumWholeDigits.value);
              safeAssign(piNumDecinalDigits, iNumDecimalDigits.value);
              safeAssign(piNumExponentDigits, iNumExponentDigits.value);
              safeAssign(piSuffix, iSuffix.value);
              safeAssign(piBase, iBase);
              return TRUE;
            }

            iNumDecimalDigits.value = 0;
            ulliDecimalNumber.value = 0;
            tResult = getDigits(ulliDecimalNumber, null, iNumDecimalDigits, false, false, iBase, iAllowedSeparator);
            if (tResult == ERROR)
            {
              passPosition();
              return ERROR;
            }

            iExponent = FLOAT_EXPONENT_DEFAULT;
            if (iBase == 10)
            {
              cCurrent = text.get(position);
              if (cCurrent == 'E' || cCurrent == 'e')
              {
                iExponent = FLOAT_EXPONENT_DECIMAL;
                stepRight();

                if (!outsideText)
                {
                  tResult = getDigits(lliExponentNumber, iSign, iNumExponentDigits, false, true, iBase, iAllowedSeparator);
                  if (tResult == ERROR)
                  {
                    passPosition();
                    return ERROR;
                  }
                }
                else
                {
                  passPosition();
                  return ERROR;
                }
              }
              else
              {
                lliExponentNumber.value = 0;
                iNumExponentDigits.value = 0;
              }
            }
            else if (iBase == 16)
            {
              cCurrent = text.get(position);
              if (cCurrent == 'P' || cCurrent == 'p')
              {
                iExponent = FLOAT_EXPONENT_BINARY;
                stepRight();

                if (!outsideText)
                {
                  tResult = getDigits(lliExponentNumber, iSign, iNumExponentDigits, false, true, 10, iAllowedSeparator);
                  if (tResult == ERROR)
                  {
                    passPosition();
                    return ERROR;
                  }
                  lliExponentNumber.value *= iSign.value;
                }
                else
                {
                  passPosition();
                  return ERROR;
                }
              }
              else
              {
                lliExponentNumber.value = 0;
                iNumExponentDigits.value = 0;
                passPosition();
                return ERROR;
              }
            }

            iSuffix.value = FLOAT_SUFFIX_NONE;
            if (!outsideText)
            {
              tResult = getFloatSuffix(iSuffix, iAllowedSuffix);
              if (tResult == ERROR)
              {
                passPosition();
                return ERROR;
              }
            }

            passPosition();
            ldf = MakeLongDouble(iBase, ulliWholeNumber.value, ulliDecimalNumber.value, iNumDecimalDigits.value, lliExponentNumber.value);
            safeAssign(pldf, ldf);
            safeAssign(piExponent, iExponent);
            safeAssign(piNumWholeDigits, iNumWholeDigits.value);
            safeAssign(piNumDecinalDigits, iNumDecimalDigits.value);
            safeAssign(piNumExponentDigits, iNumExponentDigits.value);
            safeAssign(piSuffix, iSuffix.value);
            safeAssign(piBase, iBase);
            return TRUE;
          }
          else
          {
            popPosition();
            return FALSE;
          }
        }
        else
        {
          popPosition();
          return tResult;
        }

      }
    }
    else
    {
      popPosition();
      setErrorEndOfFile();
      return ERROR;
    }
  }

  private double MakeLongDouble(int iBase, long ulliWholeNumber, long ulliDecimalNumber, int iNumDecimalDigits, long lliExponentNumber)
  {
    double ldf;
    double ldfPow;
    double ldfExp;

    if (iBase == 10)
    {
      ldf = ulliWholeNumber;
      ldfPow = 1 / Math.pow(10, iNumDecimalDigits);
      ldf += ulliDecimalNumber * ldfPow;
      if (lliExponentNumber > 0)
      {
        ldfExp = Math.pow(10, lliExponentNumber);
        ldf *= ldfExp;
      }
      else if (lliExponentNumber < 0)
      {
        ldfExp = 1 / Math.pow(10, -((double) lliExponentNumber));
        ldf *= ldfExp;
      }
      return ldf;
    }
    else if (iBase == 16)
    {
      ldf = ulliWholeNumber;
      ldfPow = 1 / Math.pow(16, iNumDecimalDigits);
      ldf += ulliDecimalNumber * ldfPow;
      if (lliExponentNumber > 0)
      {
        ldfExp = Math.pow(16, lliExponentNumber);
        ldf *= ldfExp;
      }
      else if (lliExponentNumber < 0)
      {
        ldfExp = 1 / Math.pow(16, -(lliExponentNumber));
        ldf *= ldfExp;
      }
      return ldf;
    }
    else
    {
      log.logError(filename, position, "Cannot make double with base [%s].", iBase);
      return 0;
    }
  }

  private Tristate getSingleInteger(char cCurrent, LongPointer pulli, IntegerPointer piBase, IntegerPointer piNumDigits)
  {
    if (cCurrent >= '0' && cCurrent <= '9')
    {
      passPosition();
      safeAssign(pulli, cCurrent - '0');
      safeAssign(piBase, 10);
      safeAssign(piNumDigits, 1);
      return TRUE;
    }
    else
    {
      popPosition();
      return FALSE;
    }
  }

  public Tristate getDigits(LongPointer pulli, IntegerPointer piSign, IntegerPointer piNumDigits, boolean skipWhiteSpace, boolean bTestSign, int iBase)
  {
    return getDigits(pulli, piSign, piNumDigits, skipWhiteSpace, bTestSign, iBase, NUMBER_SEPARATOR_NONE);
  }

  public Tristate getDigits(LongPointer pulli, IntegerPointer piSign, IntegerPointer piNumDigits, boolean skipWhiteSpace, boolean bTestSign)
  {
    return getDigits(pulli, piSign, piNumDigits, skipWhiteSpace, bTestSign, 10, NUMBER_SEPARATOR_NONE);
  }

  public Tristate getDigits(LongPointer pulli, IntegerPointer piSign, IntegerPointer piNumDigits, boolean skipWhiteSpace)
  {
    return getDigits(pulli, piSign, piNumDigits, skipWhiteSpace, true, 10, NUMBER_SEPARATOR_NONE);
  }

  public Tristate getDigits(LongPointer pulli, IntegerPointer piSign, IntegerPointer piNumDigits)
  {
    return getDigits(pulli, piSign, piNumDigits, true, true, 10, NUMBER_SEPARATOR_NONE);
  }

  public Tristate getDigits(LongPointer pulli, IntegerPointer piSign, IntegerPointer piNumDigits, boolean skipWhiteSpace, boolean bTestSign, int iBase, int iAllowedSeparator)
  {
    LongPointer ulliValue = new LongPointer();
    IntegerPointer iSign = new IntegerPointer();
    IntegerPointer iTemp = new IntegerPointer();
    Tristate tReturn;
    boolean bFirstDigit;
    int i;

    //This still needs to be failed on the case where the number is larger than MAX_ULONG.
    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    pulli.value = 0;
    i = 0;
    if (!outsideText)
    {
      ulliValue.value = 0;

      if (bTestSign)
      {
        getSign(iSign);
      }
      else
      {
        iSign.value = 1;
      }

      bFirstDigit = true;
      boolean bSeparator = false;
      for (; ; )
      {
        if (!outsideText)
        {
          tReturn = getDigit(iTemp, iBase);
          if (tReturn == TRUE)
          {
            i++;
            ulliValue.value *= iBase;
            ulliValue.value += iTemp.value;
            bSeparator = false;
          }
          else if (tReturn == FALSE)
          {
            if (!bSeparator)
            {
              tReturn = getIntegerSeparator(iAllowedSeparator);
              if ((tReturn == FALSE) || (tReturn == ERROR))
              {
                break;
              }
              else
              {
                bSeparator = true;
              }
            }
            else
            {
              break;
            }
          }
          else if (tReturn == ERROR)
          {
            break;
          }
          bFirstDigit = false;
        }
        else
        {
          break;
        }
      }

      if (bSeparator)
      {
        popPosition();
        return FALSE;
      }

      if (bFirstDigit)
      {
        popPosition();
        return FALSE;
      }
      else
      {
        passPosition();
        safeAssign(piSign, iSign.value);
        safeAssign(pulli, ulliValue.value);
        safeAssign(piNumDigits, i);
        return TRUE;
      }
    }
    else
    {
      popPosition();
      setErrorEndOfFile();
      return ERROR;
    }
  }

  private Tristate getIntegerSeparator(int iAllowedSeparator)
  {
    char cCurrent;

    if (!outsideText)
    {
      if ((iAllowedSeparator & NUMBER_SEPARATOR_APOSTROPHE) != 0)
      {
        cCurrent = text.get(position);
        if (cCurrent == '\'')
        {
          stepRight();
          return TRUE;
        }
        else
        {
          return FALSE;
        }
      }
      if ((iAllowedSeparator & NUMBER_SEPARATOR_UNDERSCORE) != 0)
      {
        cCurrent = text.get(position);
        if (cCurrent == '_')
        {
          stepRight();
          return TRUE;
        }
        else
        {
          return FALSE;
        }
      }
      else
      {
        return FALSE;
      }
    }
    else
    {
      setErrorEndOfFile();
      return ERROR;
    }
  }

  public Tristate getHexadecimal(LongPointer longPointer, IntegerPointer signPointer, IntegerPointer piNumDigits, boolean skipWhiteSpace)
  {
    Tristate tReturn;

    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    tReturn = getExactCharacter('0', false);
    if (tReturn != TRUE)
    {
      popPosition();
      return tReturn;
    }

    tReturn = getExactCaseInsensitiveCharacter('X', false);
    if (tReturn != TRUE)
    {
      popPosition();
      return tReturn;
    }

    tReturn = getHexadecimalPart(longPointer, piNumDigits);
    if (tReturn != TRUE)
    {
      popPosition();
    }
    else
    {
      passPosition();
    }
    return tReturn;
  }

  private Tristate getHexadecimalPart(LongPointer pulli, IntegerPointer piNumDigits)
  {
    return getHexadecimalPart(pulli, piNumDigits, 16);
  }

  private Tristate getHexadecimalPart(LongPointer pulli, IntegerPointer piNumDigits, int iMaxDigits)
  {
    long iNum;
    IntegerPointer iTemp = new IntegerPointer();
    Tristate tReturn;
    int i;

    pulli.value = 0;
    if (!outsideText)
    {
      iNum = 0;
      i = 0;
      for (; ; )
      {
        if (!outsideText)
        {
          tReturn = getDigit(iTemp, 16);
          if (tReturn == TRUE)
          {
            i++;
            iNum *= 16;
            iNum += iTemp.value;
            if (i == iMaxDigits)
            {
              pulli.value = iNum;
              safeAssign(piNumDigits, i);
              return TRUE;
            }
          }
          else if ((tReturn == FALSE) || (tReturn == ERROR))
          {
            safeAssign(piNumDigits, i);
            return TRUE;
          }
        }
        else
        {
          if (i > 0)
          {
            safeAssign(piNumDigits, i);
            return TRUE;
          }

          setErrorSyntaxError();
          return ERROR;
        }
      }
    }
    else
    {
      setErrorEndOfFile();
      return ERROR;
    }
  }

  public Tristate getOctal(LongPointer pulli, IntegerPointer signPointer, IntegerPointer piNumDigits, boolean skipWhiteSpace)
  {
    long iNum;
    IntegerPointer iTemp = new IntegerPointer();
    Tristate tReturn;
    int i;

    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    tReturn = getExactCharacter('0', false);
    if (tReturn != TRUE)
    {
      popPosition();
      return tReturn;
    }

    pulli.value = 0;
    i = 0;
    if (!outsideText)
    {
      iNum = 0;

      for (; ; )
      {
        if (!outsideText)
        {
          tReturn = getDigit(iTemp, 8);
          if (tReturn == TRUE)
          {
            i++;
            iNum *= 8;
            iNum += iTemp.value;
          }
          else if ((tReturn == FALSE) || (tReturn == ERROR))
          {
            pulli.value = iNum;
            safeAssign(piNumDigits, i);
            passPosition();
            return TRUE;
          }
        }
        else
        {
          if (i > 0)
          {
            pulli.value = iNum;
            safeAssign(piNumDigits, i);
            passPosition();
            return TRUE;
          }

          popPosition();
          setErrorSyntaxError();
          return ERROR;
        }
      }
    }
    else
    {
      popPosition();
      setErrorEndOfFile();
      return ERROR;
    }
  }

  public Tristate getFloat(FloatPointer pf, boolean skipWhiteSpace)
  {
    LongPointer ulliLeft = new LongPointer();
    LongPointer ulliRight = new LongPointer();
    Tristate tReturn;
    IntegerPointer iNumDecimals = new IntegerPointer();
    double fLeft;
    double fRight;
    double fTemp;
    boolean bLeft;
    IntegerPointer iSign = new IntegerPointer();

    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    pf.value = 0.0f;
    if (!outsideText)
    {
      //Try and get the mantissa.
      tReturn = getDigits(ulliLeft, iSign, null);
      bLeft = true;

      //Just return on errors an non-numbers.
      if (tReturn == FALSE)
      {
        //There may still be a decimal point...
        ulliLeft.value = 0;
        bLeft = false;
      }
      else if (tReturn == ERROR)
      {
        popPosition();
        setErrorSyntaxError();
        return ERROR;
      }

      fLeft = ((double) ulliLeft.value) * iSign.value;
      tReturn = getExactCharacter('.', false);
      if (tReturn == TRUE)
      {
        tReturn = getDigits(ulliRight, iSign, iNumDecimals);
        if (tReturn == TRUE)
        {
          if (iSign.value <= 0)
          {
            //Cant have: 34.-342 but -34.342 would be fine.
            popPosition();
            setErrorSyntaxError();
            return ERROR;
          }

          fRight = (double) ulliRight.value;
          fTemp = Math.pow(10.0, -iNumDecimals.value);
          fRight *= fTemp;

          if (fLeft >= 0)
          {
            pf.value = fLeft + fRight;
          }
          else
          {
            pf.value = fLeft - fRight;
          }
          passPosition();
          return TRUE;
        }
        else
        {
          //A decimal point must be followed by a number.
          popPosition();
          setErrorSyntaxError();
          return ERROR;
        }
      }
      else
      {
        //No decimal point...
        if (!bLeft)
        {
          //No digits and no point...
          popPosition();
          return FALSE;
        }
        else
        {
          pf.value = fLeft;
          passPosition();
          return TRUE;
        }
      }
    }
    else
    {
      popPosition();
      setErrorEndOfFile();
      return ERROR;
    }
  }

  void setErrorEndOfFile()
  {
    meError = EndOfFile;
  }

  void setErrorSyntaxError()
  {
    meError = SyntaxError;
  }

// ----------------------- Helper Functions ------------------------------

  public Tristate getHFExactIdentifierAndInteger(String identifier, IntegerPointer piInt)
  {
    Tristate tReturn;

    pushPosition();

    tReturn = getExactIdentifier(identifier, true);
    if ((tReturn == ERROR) || (tReturn == FALSE))
    {
      popPosition();
      return tReturn;
    }
    tReturn = getInteger(piInt, null);
    if ((tReturn == ERROR) || (tReturn == FALSE))
    {
      popPosition();
      return tReturn;
    }

    passPosition();
    return TRUE;
  }

  public Tristate getHFExactIdentifierAndString(String identifier, StringZero szString)
  {
    Tristate tReturn;

    pushPosition();

    tReturn = getExactIdentifier(identifier, true);
    if ((tReturn == ERROR) || (tReturn == FALSE))
    {
      popPosition();
      return tReturn;
    }
    tReturn = getString(szString);
    if ((tReturn == ERROR) || (tReturn == FALSE))
    {
      popPosition();
      return tReturn;
    }

    passPosition();
    return TRUE;
  }

  public Tristate getHFDotDelimitedIdentifier(StringBuffer identifier)
  {
    Tristate tReturn;
    StringZero stringZero = new StringZero();

    skipWhiteSpace();
    for (int i = 0; ; i++)
    {
      tReturn = getIdentifier(stringZero, false);
      if (tReturn == ERROR)
      {
        return ERROR;
      }
      if (tReturn == FALSE)
      {
        if (i == 0)
        {
          return FALSE;
        }
        return TRUE;
      }
      identifier.append(stringZero.toString());

      tReturn = getExactCharacter('.', false);
      if (tReturn == ERROR)
      {
        return ERROR;
      }
      if (tReturn == FALSE)
      {
        return TRUE;
      }
      identifier.append('.');
    }
  }

  public Tristate getHFCharacterSequenceBetweenTags(StringZero destination, String startTag, String endTag)
  {
    Tristate result;

    result = findExactCharacterSequence(startTag);
    if (result.equals(TRUE))
    {
      TextParserPosition start = saveSettings();
      result = findExactCharacterSequence(endTag);
      if (result.equals(TRUE))
      {
        TextParserPosition end = saveSettings();

        destination.copy(text, start.position, end.position + endTag.length());
        return TRUE;
      }
    }
    return result;
  }
}

