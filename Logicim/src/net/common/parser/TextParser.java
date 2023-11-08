package net.common.parser;

import net.assembler.sixteenhigh.parser.TextParserLog;
import net.common.SimulatorException;
import net.common.parser.primitive.CharPointer;
import net.common.parser.primitive.FloatPointer;
import net.common.parser.primitive.IntegerPointer;
import net.common.parser.primitive.LongPointer;

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
      if ((position >= 0) && (text.get(position) == '\n'))
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
    if ((position >= 0) && (position < textLength()))
    {
      outsideText = false;
      return;
    }
    outsideText = true;
  }

  public static boolean isWhiteSpace(char cCurrent)
  {
    return ((cCurrent == ' ') || (cCurrent == '\n') || (cCurrent == '\t'));
  }

  public boolean skipWhiteSpace()
  {
    return skipWhiteSpace(true);
  }

  public boolean skipWhiteSpace(boolean bSkipComments)
  {
    char cCurrent;

    for (; ; )
    {
      if (outsideText)
      {
        return true;
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
    return false;
  }

  private boolean skipCStyleComment()
  {
    return skipCStyleComment(null, null);
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
      return FALSE;
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
      return FALSE;
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

  public Tristate getExactCharacterSequence(List<String> strings, IntegerPointer index, boolean skipWhiteSpace)
  {
    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    for (int i = 0; i < strings.size(); i++)
    {
      String string = strings.get(i);
      Tristate result = getExactCharacterSequence(string, false);
      if (result == TRUE)
      {
        index.value = i;
        return TRUE;
      }
      else if (result == ERROR)
      {
        return ERROR;
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
      return FALSE;
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

  public Tristate getExactCaseInsensitiveCharacterSequence(String identifier, boolean skipWhiteSpace)
  {
    if (identifier == null)
    {
      throw new SimulatorException("Identifier may not be null.");
    }

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
      return FALSE;
    }

    for (; ; )
    {
      if (iPos == identifier.length())
      {
        //Got all the way to the null character.
        passPosition();
        return TRUE;
      }
      if (!outsideText)
      {
        c1 = Character.toUpperCase(text.get(position));
        c2 = Character.toUpperCase(identifier.charAt(iPos));
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

    pushPosition();
    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    //Make sure we're not off the end of the file.
    if (outsideText)
    {
      popPosition();
      return FALSE;
    }

    int iPos = 0;
    for (; ; )
    {
      if (!outsideText)
      {
        cCurrent.value = text.get(position);
        if (iPos == identifier.length())
        {
          //Got all the way to the null character.
          //If there are additional identifier characters then we do not have the right identifier.
          Tristate tResult = getIdentifierCharacter(cCurrent, iPos == 0);
          if (tResult == TRUE)
          {
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
          popPosition();
          return FALSE;
        }
      }
      else
      {
        if (iPos == identifier.length())
        {
          passPosition();
          return TRUE;
        }
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
      return FALSE;
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
          return FALSE;
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

  public Tristate getIdentifier(String identifier, boolean skipWhiteSpace)
  {
    return getExactIdentifier(identifier, skipWhiteSpace);
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

  public Tristate getSign(IntegerPointer pi)
  {
    char cCurrent;

    if (!outsideText)
    {
      pi.value = 1;
      cCurrent = text.get(position);
      if (cCurrent == '-' || cCurrent == '−')
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

  public Tristate getDigits(IntegerPointer integerPointer, IntegerPointer iNumDigits, IntegerPointer signPointer)
  {
    int iNum;
    IntegerPointer iTemp = new IntegerPointer();
    Tristate tReturn;
    boolean bFirstDigit;
    int i;

    pushPosition();

    integerPointer.value = 0;
    i = 0;
    if (!outsideText)
    {
      iNum = 0;

      tReturn = getSign(signPointer);
      if (tReturn == ERROR)
      {
        passPosition();
        return ERROR;
      }
      boolean signSupplied = tReturn == TRUE;

      bFirstDigit = true;
      for (; ; )
      {
        if (!outsideText)
        {
          tReturn = getDigit(iTemp);
          if (tReturn == ERROR)
          {
            passPosition();
            return ERROR;
          }

          if (tReturn == TRUE)
          {
            i++;
            iNum *= 10;
            iNum += iTemp.value;
          }
          else if (tReturn == FALSE)
          {
            if (bFirstDigit && !signSupplied)
            {
              popPosition();
              return FALSE;
            }
            iNum *= signPointer.value;
            integerPointer.value = iNum;
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
          if (bFirstDigit)
          {
            popPosition();
            return FALSE;
          }
          else
          {
            iNum *= signPointer.value;
            integerPointer.value = iNum;
            if (iNumDigits != null)
            {
              iNumDigits.value = i;
            }
            passPosition();
            return TRUE;
          }
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
    double characteristic;
    double mantissa;
    double fTemp;
    boolean bLeft;

    pushPosition();
    skipWhiteSpace();

    pf.value = 0.0;
    if (!outsideText)
    {
      IntegerPointer characteristicPointer = new IntegerPointer();
      IntegerPointer numCharacteristicsPointer = new IntegerPointer();
      IntegerPointer signPointer = new IntegerPointer();
      Tristate tReturn = getDigits(characteristicPointer, numCharacteristicsPointer, signPointer);
      bLeft = true;

      //Just return on errors and non-numbers.
      if (tReturn == FALSE)
      {
        //There may still be a decimal point...
        characteristicPointer.value = 0;
        bLeft = false;
      }
      else if (tReturn == ERROR)
      {
        passPosition();
        return ERROR;
      }

      characteristic = characteristicPointer.value;
      if (!outsideText)
      {
        tReturn = getExactCharacter('.', false);
        if (tReturn == TRUE)
        {
          IntegerPointer mantissaPointer = new IntegerPointer();
          IntegerPointer mantissaSignPointer = new IntegerPointer();
          IntegerPointer numMantissasPointer = new IntegerPointer();
          tReturn = getDigits(mantissaPointer, numMantissasPointer, mantissaSignPointer);
          if (tReturn == TRUE)
          {
            if (mantissaPointer.value < 0)
            {
              passPosition();
              return ERROR;
            }

            mantissa = mantissaPointer.value;
            fTemp = Math.pow(10.0f, (-numMantissasPointer.value));
            mantissa *= fTemp;

            pf.value = characteristic + mantissa;
            if ((signPointer.value == -1) && (pf.value > 0))
            {
              pf.value *= -1;
            }
            passPosition();
            return TRUE;
          }
          else
          {
            pf.value = characteristic;
            passPosition();
            return TRUE;
          }
        }
        else
        {
          //No decimal point...
          if (!bLeft || (numCharacteristicsPointer.value == 0))
          {
            //No digits and no point...
            popPosition();
            return FALSE;
          }
          else
          {
            pf.value = characteristic;
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
          pf.value = characteristic;
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

  public Tristate findEndOfLine()
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
      stepRight();

      //If we have no more text then the start of the line is the start of the text.
      if (outsideText)
      {
        position = text.length() - 1;
        passPosition();
        return TRUE;
      }

      //If we get find an end of line character we've gone to far, go right again.
      cCurrent = text.get(position);
      if ((cCurrent == '\n') || (cCurrent == '\r'))
      {
        stepLeft();
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
    String substring = text.toString().substring(position);
    substring = substring.substring(0, Math.min(substring.length(), 100));
    return "position [" + position + "] " + "text: " + substring;
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

  public Tristate getInteger(LongPointer integerPointer,
                             IntegerPointer signPointer,
                             IntegerPointer numDigitsPointer,
                             int base,
                             boolean skipWhiteSpace)
  {
    Tristate tResult;

    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    if (outsideText)
    {
      popPosition();
      setErrorEndOfFile();
      return ERROR;
    }

    tResult = getDigits(integerPointer,
                        signPointer,
                        numDigitsPointer,
                        skipWhiteSpace,
                        true,
                        base,
                        NUMBER_SEPARATOR_NONE,
                        true);
    if (tResult == TRUE)
    {
      passPosition();
      return TRUE;
    }
    popPosition();
    return tResult;
  }

  public Tristate getInteger(IntegerPointer integerPointer,
                             IntegerPointer numDigitsPointer,
                             int base,
                             boolean skipWhiteSpace)
  {
    LongPointer tempPointer = new LongPointer();
    IntegerPointer iSign = new IntegerPointer();

    Tristate tReturn = getInteger(tempPointer, iSign, numDigitsPointer, base, skipWhiteSpace);
    integerPointer.value = (int) (tempPointer.value * iSign.value);
    return tReturn;
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

  private void safeAssign(CharPointer pointer, char value)
  {
    if (pointer != null)
    {
      pointer.value = value;
    }
  }

  public Tristate getDigits(LongPointer resultPointer,
                            IntegerPointer piSign,
                            IntegerPointer piNumDigits,
                            boolean skipWhiteSpace,
                            boolean bTestSign,
                            int iBase,
                            int iAllowedSeparator,
                            boolean errorOnEndOfFile)
  {
    LongPointer longPointer = new LongPointer();
    IntegerPointer signPointer = new IntegerPointer();
    IntegerPointer tempPointer = new IntegerPointer();

    pushPosition();

    if (skipWhiteSpace)
    {
      skipWhiteSpace();
    }

    resultPointer.value = 0;
    int i = 0;
    if (!outsideText)
    {
      longPointer.value = 0;

      if (bTestSign)
      {
        getSign(signPointer);
      }
      else
      {
        signPointer.value = 1;
      }

      boolean bFirstDigit = true;
      boolean bSeparator = false;
      for (; ; )
      {
        if (!outsideText)
        {
          Tristate tReturn = getDigit(tempPointer, iBase);
          if (tReturn == TRUE)
          {
            i++;
            longPointer.value *= iBase;
            longPointer.value += tempPointer.value;
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
        safeAssign(piSign, signPointer.value);
        safeAssign(resultPointer, longPointer.value);
        safeAssign(piNumDigits, i);
        return TRUE;
      }
    }
    else
    {
      if (errorOnEndOfFile)
      {
        passPosition();
        setErrorEndOfFile();
        return ERROR;
      }
      else
      {
        popPosition();
        return FALSE;
      }
    }
  }

  private Tristate getIntegerSeparator(int allowedSeparator)
  {
    char cCurrent;

    if (!outsideText)
    {
      if ((allowedSeparator & NUMBER_SEPARATOR_APOSTROPHE) != 0)
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
      if ((allowedSeparator & NUMBER_SEPARATOR_UNDERSCORE) != 0)
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

  void setErrorEndOfFile()
  {
    meError = EndOfFile;
  }

  void setErrorSyntaxError()
  {
    meError = SyntaxError;
  }

  public boolean isOutsideText()
  {
    return outsideText;
  }

  public char peekCurrent()
  {
    return text.get(position);
  }

  public String getText()
  {
    return text.toString();
  }

  public List<Integer> getPositions()
  {
    return mcPositions;
  }
}

