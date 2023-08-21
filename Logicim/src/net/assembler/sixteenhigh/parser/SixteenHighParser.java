package net.assembler.sixteenhigh.parser;

import net.assembler.sixteenhigh.parser.statment.GlobalVariable;
import net.assembler.sixteenhigh.parser.statment.Go;
import net.assembler.sixteenhigh.parser.statment.If;
import net.common.SimulatorException;
import net.common.parser.StringZero;
import net.common.parser.TextParser;
import net.common.parser.TextParserPosition;
import net.common.parser.Tristate;
import net.common.parser.primitive.IntegerPointer;

import static net.common.parser.Tristate.*;

public class SixteenHighParser
{
  protected SixteenHighKeywords keywords;
  protected TextParser textParser;
  protected int statementIndex;
  protected Code code;
  protected String filename;
  protected SixteenHighContext context;

  public SixteenHighParser(TextParserLog log, String filename, SixteenHighContext context, String source)
  {
    this.filename = filename;
    this.context = context;
    keywords = new SixteenHighKeywords();
    textParser = new TextParser(source, log, filename);

    code = context.addCode(filename);
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
    SixteenHighKeywordCode keyword = keywords.getKeyword(index);
    Tristate result = textParser.getIdentifier(keywords.firstIdentifiers, index);
    if (result == TRUE)
    {
      Tristate state;
      state = registerDeclaration(keyword);
      if (state == TRUE)
      {
        return TRUE;
      }
      else if (state == ERROR)
      {
        //error
        return ERROR;
      }

      state = ifStatement(keyword);
      if (state == TRUE)
      {
        return TRUE;
      }
      else if (state == ERROR)
      {
        //error
        return ERROR;
      }

      state = goStatement(keyword);
      if (state == TRUE)
      {
        return TRUE;
      }
      else if (state == ERROR)
      {
        //error
        return ERROR;
      }

      state = returnStatement(keyword);
      if (state == TRUE)
      {
        return TRUE;
      }
      else if (state == ERROR)
      {
        //error
        return ERROR;
      }

      return FALSE;
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

  private Tristate ifStatement(SixteenHighKeywordCode keyword)
  {
    switch (keyword)
    {
      case if_greater:
      case if_equals:
      case if_less:
      case if_greater_equals:
      case if_less_equals:
      case if_not_equals:
        If anIf = code.addIf(keyword);
        Tristate state = textParser.getExactIdentifier(keywords.go());
        if (state == ERROR)
        {
          //error
          return ERROR;
        }
        else if (state == FALSE)
        {
          return ERROR;
        }
        else
        {
          Tristate result = goStatement(keywords.getKeyword(keywords.go()));
          if (result == ERROR)
          {
            //error
            return ERROR;
          }
          else if (result == FALSE)
          {
            return ERROR;
          }
          else
          {
            anIf.setGo((Go) code.getLast());
          }
        }

      default:
        return FALSE;
    }
  }

  private Tristate returnStatement(SixteenHighKeywordCode keyword)
  {
    throw new SimulatorException();
  }

  private Tristate goStatement(SixteenHighKeywordCode keyword)
  {
    throw new SimulatorException();
  }

  private Tristate registerDeclaration(SixteenHighKeywordCode keyword)
  {
    switch (keyword)
    {
      case int8:
      case uint8:
      case int16:
      case uint16:
      case int24:
      case uint24:
      case int32:
      case uint32:
      case int64:
      case uint64:
      case float8:
      case float16:
      case float32:
      case float64:
      case float128:
      case bool:
        int at = 0;
        Tristate state = textParser.getExactCharacter('@');
        if (state == ERROR)
        {
          return ERROR;
        }
        if (state == TRUE)
        {
          at++;
        }

        state = textParser.getExactCharacter('@');
        if (state == ERROR)
        {
          return ERROR;
        }
        if (state == TRUE)
        {
          at++;
        }

        StringZero identifier = new StringZero();
        state = textParser.getIdentifier(identifier);
        if (state == ERROR)
        {
          return ERROR;
        }
        else if (state == FALSE)
        {
          if (at != 0)
          {
            textParser.getLog().logError(filename, textParser.getPosition(), "Expected identifier after @ or @@.");
            return ERROR;
          }
          return FALSE;
        }
        if (at == 0)
        {
          code.addLocalVariable(keyword, identifier.toString());
          return TRUE;
        }
        else if (at == 1)
        {
          code.addFileVariable(keyword, "@" + identifier.toString());
          return TRUE;
        }
        else if (at == 2)
        {
          GlobalVariable globalVariable = code.addGlobalVariable(keyword, "@@" + identifier.toString());
          context.addGlobalVariable(globalVariable);
          return TRUE;
        }
        else
        {
          throw new SimulatorException("Can't have more than two '@' symbols.");
        }

      default:
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

    code.addLocalLabel(identifier.toString());
    return TRUE;
  }

  private Tristate parseStatementStyle2()
  {
    return null;
  }
}

