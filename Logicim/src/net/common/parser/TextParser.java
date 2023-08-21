package net.common.parser;

import net.assembler.sixteenhigh.parser.TextParserLog;
import net.common.SimulatorException;
import net.common.parser.primitive.CharPointer;
import net.common.parser.primitive.FloatPointer;
import net.common.parser.primitive.IntegerPointer;
import net.common.util.StringUtil;
import net.logicim.ui.Logicim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextParser
{
  private StringZero text;
  private int position;
  private boolean outsideText;
  private List<Integer> mcPositions;
  private TextParserLog log;
  private String filename;

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

  private void initialise(TextParserLog log, String name)
  {
    position = 0;
    this.log = log;
    this.filename = filename;
    mcPositions = new ArrayList<>();
  }

  private int textLength()
  {
    return text.length();
  }

  void stepRight()
  {
    //Can only move right if we are not sitting in the last character.
    if (position <= textLength())
    {
      outsideText = false;
      position++;
      return;
    }
    outsideText = true;
  }

  void stepLeft()
  {
    //Can only move right if we are not sitting in the last character.
    if (position >= 0)
    {
      outsideText = false;
      position--;
      return;
    }
    outsideText = true;
  }

  void testEnd()
  {
    if ((position >= 0) && (position <= textLength()))
    {
      outsideText = false;
      return;
    }
    outsideText = true;
  }

  void skipWhiteSpace()
  {
    char cCurrent;

    label:
    for (; ; )
    {
      if (outsideText)
      {
        return;
      }

      cCurrent = text.get(position);

      //Nice clean white space...
      switch (cCurrent)
      {
        case ' ':
        case '\r':
        case '\n':
        case '\t':
          stepRight();
          break;

        //Possibly nasty comments...
        case '/':
          stepRight();

          if (!outsideText)
          {
            cCurrent = text.get(position);
            switch (cCurrent)
            {
              case '*':
                //Put the parser back where it was.
                stepLeft();
                skipCStyleComment();
                break;
              case '/':
                //Put the parser back where it was.
                stepLeft();
                skipCPPStyleComment();
                break;
              default:
                //Was something other than white-space starting with /
                break label;
            }
          }
          break;
        default:
          //Was not white-space at all.
          break label;
      }
    }
  }

  void skipCStyleComment()
  {
    char cCurrent;
    int iDepth;

    iDepth = 0;

    pushPosition();
    for (; ; )
    {
      if (outsideText)
      {
        passPosition();
        return;
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
          return;
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
            iDepth--;
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
          return;
        }
      }

      if (iDepth == 0)
      {
        //No more nested comments...  bail..
        return;
      }
      stepRight();
    }
  }

  void skipCPPStyleComment()
  {
    char cCurrent;

    if (outsideText)
    {
      return;
    }

    pushPosition();
    cCurrent = text.get(position);

    if (cCurrent == '/')
    {
      stepRight();
      if (!outsideText)
      {
        cCurrent = text.get(position);
        if (cCurrent == '/')
        {
          for (; ; )
          {
            stepRight();
            if (!outsideText)
            {
              cCurrent = text.get(position);

              if (cCurrent == '\r')
              {
                stepRight();
                if (!outsideText)
                {
                  cCurrent = text.get(position);
                  if (cCurrent == '\n')
                  {
                    //This is the end of the line and the end of the comment.
                    stepRight();
                    passPosition();
                    return;
                  }
                  else
                  {
                    passPosition();
                    return;
                  }
                }
                else
                {
                  passPosition();
                  return;
                }
              }
              else if (cCurrent == '\n')
              {
                //This is the end of the line and the end of the comment.
                stepRight();
                passPosition();
                return;
              }
            }
            else
            {
              passPosition();
              return;
            }
          }
        }
        else
        {
          popPosition();
          return;
        }
      }
      else
      {
        //Wasn't a comment.
        stepLeft();
        return;
      }
    }
    popPosition();
  }

  public Tristate getExactCharacter(char c)
  {
    return getExactCharacter(c, true);
  }

  public Tristate getExactCharacter(char c, boolean bSkipWhiteSpace)
  {
    if (bSkipWhiteSpace)
    {
      skipWhiteSpace();
    }
    if (!outsideText)
    {
      if (text.get(position) == c)
      {
        stepRight();
        return Tristate.TRUE;
      }
      return Tristate.FALSE;
    }
    else
    {
      return Tristate.ERROR;
    }
  }

  Tristate getCharacter(StringZero pc)
  {
    if (!outsideText)
    {
      pc.set(0, text.get(position));
      stepRight();
      return Tristate.TRUE;
    }
    else
    {
      return Tristate.ERROR;
    }
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
        return Tristate.TRUE;
      }

      //Additional characters can also be...
      if (!bFirst)
      {
        if ((cCurrent >= '0') && (cCurrent <= '9'))
        {
          stepRight();
          return Tristate.TRUE;
        }
      }
      return Tristate.FALSE;
    }
    else
    {
      return Tristate.ERROR;
    }
  }

  public Tristate getExactCharacterSequence(String szSequence)
  {
    char cCurrent;
    int iPos;

    iPos = 0;
    pushPosition();
    skipWhiteSpace();

    //Make sure we're not off the end of the file.
    if (outsideText)
    {
      popPosition();
      return Tristate.ERROR;
    }

    for (; ; )
    {
      if (!outsideText)
      {
        cCurrent = text.get(position);
        if (iPos == szSequence.length())
        {
          //Got all the way to the NULL character.
          passPosition();
          return Tristate.TRUE;
        }
        if (cCurrent == szSequence.charAt(iPos))
        {
          stepRight();
          iPos++;
        }
        else
        {
          //Put the parser back where it was.
          popPosition();
          return Tristate.FALSE;
        }
      }
      else
      {
        //Put the parser back where it was.
        popPosition();
        return Tristate.FALSE;
      }
    }
  }

  public Tristate getExactIdentifier(String identifier)
  {
    CharPointer cCurrent = new CharPointer();
    int iPos;
    Tristate tResult;

    iPos = 0;
    pushPosition();
    skipWhiteSpace();

    //Make sure we're not off the end of the file.
    if (outsideText)
    {
      popPosition();
      log.logFatal(filename, position, "Expected exact identifier %s.  Instead outside text.", identifier);
      return Tristate.ERROR;
    }

    for (; ; )
    {
      if (!outsideText)
      {
        cCurrent.value = text.get(position);
        if (iPos == identifier.length())
        {
          //Got all the way to the NULL character.
          //If there are additional identifier characters then we do not have the right identifier.
          tResult = getIdentifierCharacter(cCurrent, iPos == 0);
          if (tResult == Tristate.TRUE)
          {
            //Put the parser back where it was.
            popPosition();
            return Tristate.FALSE;
          }
          passPosition();
          return Tristate.TRUE;
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
          return Tristate.FALSE;
        }
      }
      else
      {
        //Put the parser back where it was.
        popPosition();
        return Tristate.FALSE;
      }
    }
  }

  public Tristate getIdentifier(StringZero identifier)
  {
    CharPointer c = new CharPointer();
    boolean bFirst;
    int iPos;

    bFirst = true;
    iPos = 0;
    pushPosition();
    skipWhiteSpace();

    //Make sure we're not off the end of the file.
    if (outsideText)
    {
      popPosition();
      return Tristate.ERROR;
    }

    for (; ; )
    {
      if (!outsideText)
      {
        if (getIdentifierCharacter(c, bFirst) != Tristate.TRUE)
        {
          if (bFirst)
          {
            identifier.setEnd(iPos);
            popPosition();
            return Tristate.FALSE;
          }
          else
          {
            identifier.setEnd(iPos);
            passPosition();
            return Tristate.TRUE;
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
          return Tristate.ERROR;
        }
        else
        {
          identifier.setEnd(iPos);
          passPosition();
          return Tristate.TRUE;
        }
      }
      bFirst = false;
      iPos++;
    }
  }

  public Tristate getIdentifier(List<String> allowedIdentifiers, IntegerPointer index)
  {
    for (int i = 0; i < allowedIdentifiers.size(); i++)
    {
      String identifier = allowedIdentifiers.get(i);
      Tristate state = getExactIdentifier(identifier);
      if (state == Tristate.TRUE)
      {
        index.value = i;
        return Tristate.TRUE;
      }
      else if (state == Tristate.ERROR)
      {
        return Tristate.ERROR;
      }
    }
    return Tristate.FALSE;
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
      if (getExactCharacter('\"', false) == Tristate.TRUE)
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
                return Tristate.TRUE;

              //We have an escape character...
              case '\\':
                tReturn = getEscapeCode(new StringZero(szString, iPos));
                iPos++;
                if ((tReturn == Tristate.FALSE) || (tReturn == Tristate.ERROR))
                {
                  popPosition();
                  return Tristate.ERROR;
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
        return Tristate.FALSE;
      }
    }
    else
    {
      popPosition();
      return Tristate.ERROR;
    }
  }

  public Tristate getEscapeCode(StringZero c)
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
              c.set(0, '\n');
              break;
            case '\\':
              c.set(0, '\\');
              break;
            case '\"':
              c.set(0, '\"');
              break;
            default:
              return Tristate.ERROR;
          }
          stepRight();
          return Tristate.TRUE;
        }
        else
        {
          return Tristate.ERROR;
        }
      }
      else
      {
        return Tristate.ERROR;
      }
    }
    else
    {
      return Tristate.ERROR;
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
        return Tristate.TRUE;
      }
      else
      {
        return Tristate.FALSE;
      }
    }
    else
    {
      return Tristate.ERROR;
    }
  }

  public Tristate getSign(IntegerPointer pi)
  {
    char cCurrent;

    if (!outsideText)
    {
      pi.value = 1;
      cCurrent = text.get(position);
      switch (cCurrent)
      {
        case '-':
          pi.value = -1;
          stepRight();
          return Tristate.TRUE;
        case '+':
          stepRight();
          return Tristate.TRUE;
        default:
          return Tristate.FALSE;
      }
    }
    else
    {
      return Tristate.ERROR;
    }
  }

  public Tristate getInteger(IntegerPointer pi, IntegerPointer iNumDigits)
  {
    Tristate tResult;

    pushPosition();
    skipWhiteSpace();

    //Make sure we're not off the end of the file.
    if (outsideText)
    {
      popPosition();
      return Tristate.ERROR;
    }

    tResult = getDigits(pi, iNumDigits);
    if (tResult == Tristate.TRUE)
    {
      //Make sure there are no decimals.
      if (text.get(position) == '.')
      {
        popPosition();
        return Tristate.FALSE;
      }

      passPosition();
      return Tristate.TRUE;
    }
    popPosition();
    return tResult;
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
          if (tReturn == Tristate.TRUE)
          {
            i++;
            iNum *= 10;
            iNum += iTemp.value;
          }
          else if ((tReturn == Tristate.FALSE) || (tReturn == Tristate.ERROR))
          {
            if (bFirstDigit)
            {
              //might already have got a sign...  so reset the parser.
              popPosition();
              return Tristate.FALSE;
            }
            iNum *= iSign.value;
            pi.value = iNum;
            if (iNumDigits != null)
            {
              iNumDigits.value = i;
            }
            passPosition();
            return Tristate.TRUE;
          }
          bFirstDigit = false;
        }
        else
        {
          //Got only a sign then end of file.
          popPosition();
          return Tristate.ERROR;
        }
      }
    }
    else
    {
      popPosition();
      return Tristate.ERROR;
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
      if (tReturn == Tristate.FALSE)
      {
        //There may still be a decimal point...
        iLeft.value = 0;
        bLeft = false;
      }
      else if (tReturn == Tristate.ERROR)
      {
        popPosition();
        return Tristate.ERROR;
      }

      fLeft = iLeft.value;
      tReturn = getExactCharacter('.', false);
      if (tReturn == Tristate.TRUE)
      {
        tReturn = getDigits(iRight, iNumDecimals);
        if (tReturn == Tristate.TRUE)
        {
          fRight = iRight.value;
          fTemp = Math.pow(10.0f, (-iNumDecimals.value));
          fRight *= fTemp;

          pf.value = fLeft + fRight;
          passPosition();
          return Tristate.TRUE;
        }
        else
        {
          //A decimal point must be followed by a number.
          popPosition();
          return Tristate.ERROR;
        }
      }
      else
      {
        //No decimal point...
        if (!bLeft)
        {
          //No digits and no point...
          popPosition();
          return Tristate.FALSE;
        }
        else
        {
          pf.value = fLeft;
          passPosition();
          return Tristate.TRUE;
        }
      }
    }
    else
    {
      popPosition();
      return Tristate.ERROR;
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
      result = getExactIdentifier(identifier);
      if (result == Tristate.ERROR)
      {
        //We've reached the end of the file without finding the identifier.
        popPosition();
        return Tristate.FALSE;
      }
      else if (result == Tristate.FALSE)
      {
        //Try the next actual character along.
        stepRight();
        skipWhiteSpace();
      }
      else if (result == Tristate.TRUE)
      {
        position = szPosition;
        passPosition();
        return Tristate.TRUE;
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
      if (result == Tristate.ERROR)
      {
        //We've reached the end of the file without finding the identifier.
        popPosition();
        return Tristate.FALSE;
      }
      else if (result == Tristate.FALSE)
      {
        //Try the next actual character along.
        stepRight();
        skipWhiteSpace();
      }
      else if (result == Tristate.TRUE)
      {
        position = szPosition;
        passPosition();
        return Tristate.TRUE;
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
      return Tristate.ERROR;
    }

    for (; ; )
    {
      stepLeft();

      //If we have no more text then the start of the line is the start of the text.
      if (outsideText)
      {
        position = 0;
        passPosition();
        return Tristate.TRUE;
      }

      //If we get find an end of line character we've gone to far, go right again.
      cCurrent = text.get(position);
      if ((cCurrent == '\n') || (cCurrent == '\r'))
      {
        stepRight();
        passPosition();
        return Tristate.TRUE;
      }
    }
  }

  void pushPosition()
  {
    mcPositions.add(position);
  }

  void popPosition()
  {
    position = mcPositions.get(mcPositions.size() - 1);
    mcPositions.remove(mcPositions.size() - 1);
    testEnd();
  }

  void passPosition()
  {
    mcPositions.remove(mcPositions.size() - 1);
  }

// ----------------------- Helper Functions ------------------------------

  public Tristate getHFExactIdentifierAndInteger(String identifier, IntegerPointer piInt)
  {
    Tristate tReturn;

    pushPosition();

    tReturn = getExactIdentifier(identifier);
    if ((tReturn == Tristate.ERROR) || (tReturn == Tristate.FALSE))
    {
      popPosition();
      return tReturn;
    }
    tReturn = getInteger(piInt, null);
    if ((tReturn == Tristate.ERROR) || (tReturn == Tristate.FALSE))
    {
      popPosition();
      return tReturn;
    }

    passPosition();
    return Tristate.TRUE;
  }

  public Tristate getHFExactIdentifierAndString(String identifier, StringZero szString)
  {
    Tristate tReturn;

    pushPosition();

    tReturn = getExactIdentifier(identifier);
    if ((tReturn == Tristate.ERROR) || (tReturn == Tristate.FALSE))
    {
      popPosition();
      return tReturn;
    }
    tReturn = getString(szString);
    if ((tReturn == Tristate.ERROR) || (tReturn == Tristate.FALSE))
    {
      popPosition();
      return tReturn;
    }

    passPosition();
    return Tristate.TRUE;
  }

  public Tristate getHFDotDelimitedIdentifier(StringBuffer identifier)
  {
    Tristate tReturn;
    StringZero stringZero = new StringZero();

    for (int i = 0; ; i++)
    {
      tReturn = getIdentifier(stringZero);
      if (tReturn == Tristate.ERROR)
      {
        return Tristate.ERROR;
      }
      if (tReturn == Tristate.FALSE)
      {
        if (i == 0)
        {
          return Tristate.FALSE;
        }
        return Tristate.TRUE;
      }
      identifier.append(stringZero.toString());

      tReturn = getExactCharacter('.', false);
      if (tReturn == Tristate.ERROR)
      {
        return Tristate.ERROR;
      }
      if (tReturn == Tristate.FALSE)
      {
        return Tristate.TRUE;
      }
      identifier.append('.');
    }
  }

  public Tristate getHFCharacterSequenceBetweenTags(StringZero destination, String startTag, String endTag)
  {
    Tristate result;

    result = findExactCharacterSequence(startTag);
    if (result.equals(Tristate.TRUE))
    {
      TextParserPosition start = saveSettings();
      result = findExactCharacterSequence(endTag);
      if (result.equals(Tristate.TRUE))
      {
        TextParserPosition end = saveSettings();

        destination.copy(text, start.position, end.position + endTag.length());
        return Tristate.TRUE;
      }
    }
    return result;
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
}

