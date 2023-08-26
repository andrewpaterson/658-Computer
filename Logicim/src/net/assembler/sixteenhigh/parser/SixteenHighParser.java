package net.assembler.sixteenhigh.parser;

import net.assembler.sixteenhigh.parser.statment.*;
import net.common.SimulatorException;
import net.common.parser.StringZero;
import net.common.parser.TextParser;
import net.common.parser.TextParserPosition;
import net.common.parser.Tristate;
import net.common.parser.primitive.IntegerPointer;
import net.common.parser.primitive.LongPointer;

import static net.assembler.sixteenhigh.parser.SixteenHighKeywordCode.*;
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
  }

  protected Tristate parse()
  {
    boolean canParseInterStatement = false;
    while (textParser.hasMoreText())
    {
      if (canParseInterStatement)
      {
        if (parseInterStatement() == ERROR)
        {
          return ERROR;
        }
        else
        {
          canParseInterStatement = false;
          continue;
        }
      }

      Tristate result;
      result = parseDirective();
      if (result == TRUE)
      {
        canParseInterStatement = true;
        continue;
      }
      else if (result == ERROR)
      {
        return ERROR;
      }

      result = parseStatementBeginningWithIdentifier();
      if (result == TRUE)
      {
        canParseInterStatement = true;
        continue;
      }
      else if (result == ERROR)
      {
        return ERROR;
      }

      result = parseLabel();
      if (result == TRUE)
      {
        canParseInterStatement = true;
        continue;
      }
      else if (result == ERROR)
      {
        return ERROR;
      }

      result = parseStatementStyle2();
      if (result == TRUE)
      {
        canParseInterStatement = true;
        continue;
      }
      else if (result == ERROR)
      {
        return ERROR;
      }
      else
      {
        return TRUE;
      }
    }
    return TRUE;
  }

  private Tristate parseInterStatement()
  {
    for (; ; )
    {
      Tristate state = textParser.getExactCharacter(';', true);
      if (state == ERROR)
      {
        return ERROR;
      }
      else if (state == FALSE)
      {
        return TRUE;
      }
    }
  }

  private Tristate parseDirective()
  {
    IntegerPointer index = new IntegerPointer();
    Tristate result = textParser.getIdentifier(keywords.directiveIdentifiers, index);
    if (result == TRUE)
    {
      SixteenHighKeywordCode keyword = keywords.getKeyword(index);
      Tristate state = startAddress(keyword);
    }
    else if (result == ERROR)
    {
      return ERROR;
    }
    else
    {
      return FALSE;
    }
    return null;
  }

  private Tristate startAddress(SixteenHighKeywordCode keyword)
  {
    if (keyword == start_address)
    {
      LongPointer integerValue = new LongPointer();
      IntegerPointer base = new IntegerPointer();
      IntegerPointer suffix = new IntegerPointer();
      IntegerPointer numDigits = new IntegerPointer();
      textParser.getIntegerLiteral(integerValue,
                                   TextParser.INTEGER_PREFIX_ALL,
                                   base,
                                   TextParser.INTEGER_SUFFIX_CPP,
                                   suffix,
                                   TextParser.NUMBER_SEPARATOR_APOSTROPHE,
                                   numDigits,
                                   true);
      return TRUE;
    }
    return FALSE;
  }

  private Tristate parseStatementBeginningWithIdentifier()
  {
    IntegerPointer index = new IntegerPointer();
    Tristate result = textParser.getIdentifier(keywords.firstIdentifiers, index);
    if (result == TRUE)
    {
      SixteenHighKeywordCode keyword = keywords.getKeyword(index);
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

      state = flowStatement(keyword);
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

      state = pushPullStatement(keyword);
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

  private Tristate pushPullStatement(SixteenHighKeywordCode keyword)
  {
    if (keyword == push)
    {
      StringZero stringZero = new StringZero();
      Tristate state = textParser.getIdentifier(stringZero);
      if (state == ERROR)
      {
        return ERROR;
      }
      else if (state == FALSE)
      {
        return ERROR;
      }
      else
      {
        code.addPush(stringZero.toString());
        return TRUE;
      }
    }
    else if (keyword == pull)
    {
      StringZero stringZero = new StringZero();
      Tristate state = textParser.getIdentifier(stringZero);
      if (state == ERROR)
      {
        return ERROR;
      }
      else if (state == FALSE)
      {
        return ERROR;
      }
      else
      {
        code.addPull(stringZero.toString());
        return TRUE;
      }
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
          Tristate result = flowStatement(keywords.getKeyword(keywords.go()));
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
    if (keyword == ret)
    {
      code.addReturn();
      return TRUE;
    }
    else
    {
      return FALSE;
    }
  }

  private Tristate flowStatement(SixteenHighKeywordCode keyword)
  {
    if (keyword == go)
    {
      StringZero stringZero = new StringZero();
      Tristate state = textParser.getIdentifier(stringZero);
      if (state == ERROR)
      {
        return ERROR;
      }
      else if (state == FALSE)
      {
        return ERROR;
      }
      else
      {
        code.addGo(stringZero.toString());
        return TRUE;
      }
    }
    else if (keyword == gosub)
    {
      StringZero stringZero = new StringZero();
      Tristate state = blockIdentifier(stringZero);
      if (state == ERROR)
      {
        return ERROR;
      }
      else if (state == FALSE)
      {
        return ERROR;
      }
      else
      {
        code.addGosub(stringZero.toString());
        return TRUE;
      }
    }
    else
    {
      return FALSE;
    }
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
        StringZero zeroIdentifier = new StringZero();
        Tristate state = blockIdentifier(zeroIdentifier);
        if (state == FALSE)
        {
          return FALSE;
        }
        else if (state == ERROR)
        {
          return ERROR;
        }
        else
        {
          String identifier = zeroIdentifier.toString();
          if (identifier.startsWith("@@"))
          {
            GlobalVariable globalVariable = code.addGlobalVariable(keyword, identifier);
            context.addGlobalVariable(globalVariable);
          }
          else if (identifier.startsWith("@"))
          {
            code.addFileVariable(keyword, identifier);
          }
          else
          {
            code.addLocalVariable(keyword, identifier);
          }
          return TRUE;
        }

      default:
        return FALSE;
    }
  }

  private Tristate parseLabel()
  {
    TextParserPosition position = textParser.saveSettings();
    StringZero zeroIdentifier = new StringZero();
    Tristate state = blockIdentifier(zeroIdentifier);
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

    String identifier = zeroIdentifier.toString();
    if (identifier.startsWith("@@"))
    {
      if (identifier.equalsIgnoreCase("@@main"))
      {
        MainRoutine mainRoutine = code.addMainRoutine(identifier);
        context.addMainRoutine(mainRoutine);
      }
      else
      {
        GlobalSubroutine globalSubroutine = code.addGlobalSubroutine(identifier);
        context.addGlobalSubroutine(globalSubroutine);
      }
    }
    else if (identifier.startsWith("@"))
    {
      code.addLocalSubroutine(identifier);
    }
    else
    {
      code.addLocalLabel(identifier);
    }
    return TRUE;
  }

  private Tristate blockIdentifier(StringZero fullIdentifier)
  {
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
      fullIdentifier.set(identifier.toString());
      return TRUE;
    }
    else if (at == 1)
    {
      fullIdentifier.set("@" + identifier.toString());
      return TRUE;
    }
    else if (at == 2)
    {
      fullIdentifier.set("@@" + identifier.toString());
      return TRUE;
    }
    else
    {
      throw new SimulatorException("Can't have more than two '@' symbols.");
    }
  }

  private Tristate parseStatementStyle2()
  {
    StringZero zeroIdentifier = new StringZero();
    Tristate result = textParser.getIdentifier(zeroIdentifier);
    if (result == ERROR)
    {
      return ERROR;
    }
    else if (result == FALSE)
    {
      return FALSE;
    }

    String identifier = zeroIdentifier.toString();
    IntegerPointer index = new IntegerPointer();
    result = textParser.getIdentifier(keywords.secondIdentifiers, index);
    SixteenHighKeywordCode keyword = keywords.getKeyword(index);
    if (result == TRUE)
    {
      Tristate state;
      state = bitCompare(identifier, keyword);
      if (state == TRUE)
      {
        return TRUE;
      }
      else if (state == ERROR)
      {
        //error
        return ERROR;
      }

      state = numberCompare(identifier, keyword);
      if (state == TRUE)
      {
        return TRUE;
      }
      else if (state == ERROR)
      {
        //error
        return ERROR;
      }

      state = assignmentOperator(identifier, keyword);
      if (state == TRUE)
      {
        return TRUE;
      }
      else if (state == ERROR)
      {
        //error
        return ERROR;
      }

      return TRUE;
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

  private Tristate assignmentOperator(String leftIdentifier, SixteenHighKeywordCode keyword)
  {
    switch (keyword)
    {
      case add_assign:
      case subtract_assign:
      case multiply_assign:
      case divide_assign:
      case modulus_assign:
      case shift_left_assign:
      case shift_right_assign:
      case ushift_right_assign:
      case and_assign:
      case or_assign:
      case xor_assign:
      case not_assign:
        StringZero zeroIdentifier = new StringZero();
        Tristate result = textParser.getIdentifier(zeroIdentifier);
        if (result == ERROR)
        {
          return ERROR;
        }
        else if (result == FALSE)
        {
          return ERROR;
        }

        String rightIdentifier = zeroIdentifier.toString();
        code.addAssignmentOperator(leftIdentifier, rightIdentifier, keyword);
        return TRUE;

      default:
        return FALSE;
    }

  }

  private Tristate bitCompare(String identifier, SixteenHighKeywordCode keyword)
  {
    switch (keyword)
    {
      case is_true:
      case is_false:
        code.addBitCompare(identifier, keyword);
        return TRUE;

      default:
        return FALSE;
    }
  }

  private Tristate numberCompare(String leftIdentifier, SixteenHighKeywordCode keyword)
  {
    switch (keyword)
    {
      case subtract_compare:
      case and_compare:
        StringZero zeroIdentifier = new StringZero();
        Tristate result = textParser.getIdentifier(zeroIdentifier);
        if (result == ERROR)
        {
          return ERROR;
        }
        else if (result == FALSE)
        {
          return ERROR;
        }

        String rightIdentifier = zeroIdentifier.toString();
        code.addNumberCompare(leftIdentifier, rightIdentifier, keyword);
        return TRUE;

      default:
        return FALSE;
    }
  }
}

