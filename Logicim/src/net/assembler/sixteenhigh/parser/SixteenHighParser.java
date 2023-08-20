package net.assembler.sixteenhigh.parser;

import net.assembler.sixteenhigh.parser.statment.Label;
import net.assembler.sixteenhigh.parser.statment.Statement;
import net.common.parser.StringZero;
import net.common.parser.TextParser;
import net.common.parser.TextParserPosition;
import net.common.parser.Tristate;
import net.common.parser.primitive.IntegerPointer;

import java.util.ArrayList;
import java.util.List;

import static net.common.parser.Tristate.*;

public class SixteenHighParser
{
  protected SixteenHighKeywords keywords;
  protected TextParser textParser;
  protected List<Statement> statements;
  protected int statementIndex;

  public SixteenHighParser(String source)
  {
    keywords = new SixteenHighKeywords();
    textParser = new TextParser(source);
    statements = new ArrayList<>();
    statementIndex = 0;
    for (; ; )
    {
      Tristate result;
      result = parseStatementStyle1();
      if (result == TRUE)
      {
        continue;
      }
      else if (result == ERROR)
      {
        break;
      }

      result = parseLabel();
      if (result == TRUE)
      {
        continue;
      }
      else if (result == ERROR)
      {
        break;
      }

      result = parseStatementStyle2();
      if (result == TRUE)
      {
        continue;
      }
      else if (result == ERROR)
      {
        break;
      }

    }
  }

  private Tristate parseStatementStyle1()
  {
    IntegerPointer index = new IntegerPointer();
    Tristate result = textParser.getIdentifier(keywords.firstIdentifiers, index);
    if (result == TRUE)
    {
      Tristate state;
      state = registerDeclaration(index);
      if (state == TRUE)
      {
        return TRUE;
      }
      else if (state == ERROR)
      {
        //error
        return ERROR;
      }

      state = ifStatement(index);
      if (state == TRUE)
      {
        return TRUE;
      }
      else if (state == ERROR)
      {
        //error
        return ERROR;
      }

      state = goStatement(index);
      if (state == TRUE)
      {
        return TRUE;
      }
      else if (state == ERROR)
      {
        //error
        return ERROR;
      }

      state = returnStatement(index);
      if (state == TRUE)
      {
        return TRUE;
      }
      else if (state == ERROR)
      {
        //error
        return ERROR;
      }
    }
    else if (result == ERROR)
    {
      return ERROR;
    }
    else
    {
      return FALSE;
    }
  }

  private Tristate parseLabel()
  {
    TextParserPosition position = textParser.saveSettings();
    StringZero identifier = new StringZero();
    Tristate state = textParser.getIdentifier(identifier);
    if (state == ERROR)
    {
      return ERROR;
    }
    else if (state == FALSE)
    {
      return FALSE;
    }

    state = textParser.getExactCharacter(':');
    if (state == ERROR)
    {
      return ERROR;
    }
    else if (state == FALSE)
    {
      textParser.loadSettings(position);
      return FALSE;
    }

    statements.add(new Label(statementIndex++, identifier.toString()));
    return TRUE;
  }

  private Tristate parseStatementStyle2()
  {
    return null;
  }
}

