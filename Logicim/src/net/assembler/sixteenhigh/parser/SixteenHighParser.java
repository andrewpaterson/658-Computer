package net.assembler.sixteenhigh.parser;

import net.assembler.sixteenhigh.parser.literal.LiteralParser;
import net.assembler.sixteenhigh.parser.literal.LiteralResult;
import net.assembler.sixteenhigh.parser.statment.*;
import net.assembler.sixteenhigh.parser.statment.expression.Expression;
import net.assembler.sixteenhigh.parser.statment.expression.LiteralExpression;
import net.assembler.sixteenhigh.parser.statment.expression.OperandExpression;
import net.assembler.sixteenhigh.parser.statment.expression.RegisterExpression;
import net.common.SimulatorException;
import net.common.parser.StringZero;
import net.common.parser.TextParser;
import net.common.parser.TextParserPosition;
import net.common.parser.Tristate;
import net.common.parser.primitive.IntegerPointer;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static net.assembler.sixteenhigh.parser.SixteenHighKeywordCode.*;
import static net.common.parser.TextParser.NUMBER_SEPARATOR_APOSTROPHE;
import static net.common.parser.Tristate.*;

public class SixteenHighParser
{
  protected SixteenHighKeywords keywords;
  protected TextParser textParser;
  protected int statementIndex;
  protected Code code;
  protected String filename;
  protected SixteenHighContext context;
  protected LiteralParser literalParser;
  protected int allowedSeparator;

  public SixteenHighParser(TextParserLog log,
                           String filename,
                           SixteenHighContext context,
                           String source)
  {
    this.filename = filename;
    this.context = context;
    this.keywords = new SixteenHighKeywords();
    this.textParser = new TextParser(source, log, filename);
    this.literalParser = new LiteralParser(textParser);

    this.code = context.addCode(filename);
    this.statementIndex = 0;
    this.allowedSeparator = NUMBER_SEPARATOR_APOSTROPHE;
  }

  protected ParseResult parse()
  {
    boolean canParseInterStatement = false;
    while (textParser.hasMoreText())
    {
      if (canParseInterStatement)
      {
        if (parseInterStatement() == ERROR)
        {
          return _error();
        }
        else
        {
          canParseInterStatement = false;
          continue;
        }
      }

      ParseResult parseResult;
      parseResult = parseDirective();
      if (parseResult.isTrue())
      {
        canParseInterStatement = true;
        continue;
      }
      else if (parseResult.isError())
      {
        return parseResult;
      }

      parseResult = parseEnd();
      if (parseResult.isTrue())
      {
        canParseInterStatement = true;
        continue;
      }
      else if (parseResult.isError())
      {
        return parseResult;
      }

      parseResult = parseLabel();
      if (parseResult.isTrue())
      {
        canParseInterStatement = true;
        continue;
      }
      else if (parseResult.isError())
      {
        return parseResult;
      }

      parseResult = parseStatement();
      if (parseResult.isTrue())
      {
        canParseInterStatement = true;
        continue;
      }
      else if (parseResult.isError())
      {
        return parseResult;
      }
      else
      {
        return _true();
      }
    }
    return _true();
  }

  private ParseResult parseEnd()
  {
    Tristate result = textParser.getExactIdentifier(keywords.end(), true);
    if (result == TRUE)
    {
      result = end(end);
      return parseResult(result);
    }
    else if (result == ERROR)
    {
      return _error();
    }
    else
    {
      return _false();
    }
  }

  private Tristate end(SixteenHighKeywordCode keyword)
  {
    if (keyword == end)
    {
      code.addEnd();
      return TRUE;
    }
    else
    {
      return FALSE;
    }
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

  private ParseResult parseDirective()
  {
    IntegerPointer index = new IntegerPointer();
    Tristate result = textParser.getIdentifier(keywords.directiveIdentifiers, index, true);
    if (result == TRUE)
    {
      SixteenHighKeywordCode keyword = keywords.getKeyword(keywords.directiveIdentifiers, index);
      Tristate state = startAddress(keyword);
      if (state == TRUE)
      {
        return _true();
      }
      else if (state == ERROR)
      {
        return _error();
      }

      state = endAddress(keyword);
      if (state == TRUE)
      {
        return _true();
      }
      else if (state == ERROR)
      {
        return _error();
      }

      state = accessMode(keyword);
      if (state == TRUE)
      {
        return _true();
      }
      else if (state == ERROR)
      {
        return _error();
      }

      state = accessTime(keyword);
      if (state == TRUE)
      {
        return _true();
      }
      else if (state == ERROR)
      {
        return _error();
      }

      return _false();
    }
    else if (result == ERROR)
    {
      return _error();
    }
    else
    {
      return _false();
    }
  }

  private Tristate startAddress(SixteenHighKeywordCode keyword)
  {
    if (keyword == start_address)
    {
      LiteralResult integerLiteral = literalParser.getIntegerLiteral(allowedSeparator);
      if (integerLiteral.isTrue())
      {
        code.addStartAddress((int) integerLiteral.getIntegerLiteral().getValue());
        return TRUE;
      }
      else if (integerLiteral.isError())
      {
        return ERROR;
      }
      else
      {
        return ERROR;
      }
    }
    return FALSE;
  }

  private Tristate endAddress(SixteenHighKeywordCode keyword)
  {
    if (keyword == end_address)
    {
      LiteralResult integerLiteral = literalParser.getIntegerLiteral(allowedSeparator);
      if (integerLiteral.isTrue())
      {
        code.addEndAddress((int) integerLiteral.getIntegerLiteral().getValue());
        return TRUE;
      }
      else if (integerLiteral.isError())
      {
        return ERROR;
      }
      else
      {
        return ERROR;
      }
    }
    return FALSE;
  }

  private Tristate accessMode(SixteenHighKeywordCode keyword)
  {
    if (keyword == access_mode)
    {
      IntegerPointer index = new IntegerPointer();
      Tristate state = textParser.getIdentifier(keywords.accessModes, index, true);
      if (state == TRUE)
      {
        SixteenHighKeywordCode accessMode = keywords.getKeyword(keywords.accessModes, index);
        code.addAccessMode(accessMode);
        return TRUE;
      }
      else if (state == ERROR)
      {
        return ERROR;
      }
      else
      {
        return ERROR;
      }
    }
    return FALSE;
  }

  private Tristate accessTime(SixteenHighKeywordCode keyword)
  {
    if (keyword == access_time)
    {
      LiteralResult integerLiteral = literalParser.getIntegerLiteral(allowedSeparator);
      if (integerLiteral.isTrue())
      {
        code.addAccessTime((int) integerLiteral.getIntegerLiteral().getValue());
        return TRUE;
      }
      else if (integerLiteral.isError())
      {
        return ERROR;
      }
      else
      {
        return ERROR;
      }
    }
    return FALSE;
  }

  private ParseResult pushStatement(SixteenHighKeywordCode keyword)
  {
    if (keyword == push)
    {
      StringZero stringZero = new StringZero();
      Tristate state = textParser.getIdentifier(stringZero, true);
      if (state == ERROR)
      {
        return _error();
      }
      else if (state == FALSE)
      {
        return _error("Expected identifier.");
      }
      else
      {
        code.addPush(stringZero.toString());
        return _true();
      }
    }
    else
    {
      return _false();
    }
  }

  private ParseResult pullStatement(SixteenHighKeywordCode keyword)
  {
    if (keyword == pull)
    {
      StringZero stringZero = new StringZero();
      Tristate state = textParser.getIdentifier(stringZero, true);
      if (state == ERROR)
      {
        return _error();
      }
      else if (state == FALSE)
      {
        return _error("Expected identifier.");
      }
      else
      {
        code.addPull(stringZero.toString());
        return _true();
      }
    }
    else
    {
      return _false();
    }
  }

  private ParseResult ifStatement(SixteenHighKeywordCode keyword)
  {
    if (keywords.getIfs().contains(keyword))
    {
      If anIf = code.addIf(keyword);
      Tristate state = textParser.getExactIdentifier(keywords.go(), true);
      if (state == ERROR)
      {
        return _error();
      }
      else if (state == FALSE)
      {
        return _error("Expected keyword 'go'.");
      }

      ParseResult parseResult = flowStatement(keywords.getKeyword(keywords.go()));
      if (parseResult.isError())
      {
        return parseResult;
      }
      else if (parseResult.isFalse())
      {
        return _error("Expected flow statement.");
      }
      anIf.setGo((Go) code.getLast());
      return _true();
    }
    return _false();
  }

  private ParseResult returnStatement(SixteenHighKeywordCode keyword)
  {
    if (keyword == ret)
    {
      code.addReturn();
      return _true();
    }
    else
    {
      return _false();
    }
  }

  private ParseResult flowStatement(SixteenHighKeywordCode keyword)
  {
    if (keyword == go)
    {
      StringZero goLabel = new StringZero();
      Tristate state = textParser.getIdentifier(goLabel, true);
      if (state == ERROR)
      {
        return _error();
      }
      else if (state == FALSE)
      {
        return _error("Expected identifier.");
      }
      else
      {
        code.addGo(goLabel.toString());
        return _true();
      }
    }
    else if (keyword == gosub)
    {
      StringZero stringZero = new StringZero();
      ParseResult parseResult = blockIdentifier(stringZero);
      if (parseResult.isError())
      {
        return parseResult;
      }
      else if (parseResult.isFalse())
      {
        return _error("Expected block identifier");
      }
      else
      {
        code.addGosub(stringZero.toString());
        return _true();
      }
    }
    else
    {
      return _false();
    }
  }

  private ParseResult registerDeclaration(SixteenHighKeywordCode keyword)
  {
    if (keywords.getRegisterTypes().contains(keyword))
    {
      int asteriskCount = 0;
      for (; ; )
      {
        Tristate result = textParser.getExactCharacter('*', true);
        if (result == TRUE)
        {
          asteriskCount++;
        }
        else
        {
          break;
        }
      }

      StringZero registerNameZero = new StringZero();
      ParseResult parseResult = blockIdentifier(registerNameZero);
      if (parseResult.isFalseOrError())
      {
        return parseResult;
      }

      String registerName = registerNameZero.toString();
      if (registerName.startsWith("@@"))
      {
        GlobalVariable globalVariable = code.addGlobalVariable(keyword, registerName, asteriskCount);
        context.addGlobalVariable(globalVariable);
      }
      else if (registerName.startsWith("@"))
      {
        code.addFileVariable(keyword, registerName, asteriskCount);
      }
      else
      {
        code.addLocalVariable(keyword, registerName, asteriskCount);
      }
      return _true();
    }
    return _false();
  }

  private ParseResult parseLabel()
  {
    TextParserPosition position = textParser.saveSettings();
    StringZero zeroIdentifier = new StringZero();
    ParseResult parseResult = blockIdentifier(zeroIdentifier);
    if (parseResult.isFalseOrError())
    {
      return parseResult;
    }

    Tristate state = textParser.getExactCharacter(':');
    if (state == ERROR)
    {
      return _error();
    }
    else if (state == FALSE)
    {
      textParser.loadSettings(position);
      return _false();
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
    return _true();
  }

  public ParseResult _error()
  {
    return new ParseResult(ERROR);
  }

  private ParseResult _error(String error, Object... parameters)
  {
    textParser.getLog().logError(filename, textParser.getPosition(), String.format(error, parameters));
    return new ParseResult(ERROR);
  }

  private ParseResult _false()
  {
    return new ParseResult(FALSE);
  }

  private ParseResult _true()
  {
    return new ParseResult(TRUE);
  }

  private ParseResult blockIdentifier(StringZero fullIdentifier)
  {
    int at = 0;
    Tristate state = textParser.getExactCharacter('@');
    if (state == ERROR)
    {
      return _error();
    }
    if (state == TRUE)
    {
      at++;
    }

    state = textParser.getExactCharacter('@');
    if (state == ERROR)
    {
      return _error();
    }
    if (state == TRUE)
    {
      at++;
    }

    StringZero identifier = new StringZero();
    state = textParser.getIdentifier(identifier, true);
    if (state == ERROR)
    {
      return _error();
    }
    else if (state == FALSE)
    {
      if (at != 0)
      {
        return _error("Expected identifier.");
      }
      return _false();
    }
    if (at == 0)
    {
      fullIdentifier.set(identifier.toString());
      return _true();
    }
    else if (at == 1)
    {
      fullIdentifier.set("@" + identifier.toString());
      return _true();
    }
    else if (at == 2)
    {
      fullIdentifier.set("@@" + identifier.toString());
      return _true();
    }
    else
    {
      return _error("Expected identifier.");
    }
  }

  private ParseResult parseStatement()
  {
    IntegerPointer index = new IntegerPointer();
    Tristate result = textParser.getIdentifier(keywords.leadingIdentifiers, index, true);
    if (result == TRUE)
    {
      SixteenHighKeywordCode keyword = keywords.getKeyword(keywords.leadingIdentifiers, index);
      ParseResult parseResult;
      parseResult = registerDeclaration(keyword);
      if (parseResult.isTrue())
      {
        parseResult = parseRegisterInitialiser();
        if (parseResult.isFalse() || parseResult.isTrue())
        {
          return _true();
        }
        return parseResult;
      }
      if (parseResult.isError())
      {
        return parseResult;
      }

      parseResult = ifStatement(keyword);
      if (parseResult.isTrueOrError())
      {
        return parseResult;
      }

      parseResult = flowStatement(keyword);
      if (parseResult.isTrueOrError())
      {
        return parseResult;
      }

      parseResult = returnStatement(keyword);
      if (parseResult.isTrueOrError())
      {
        return parseResult;
      }

      parseResult = pushStatement(keyword);
      if (parseResult.isTrueOrError())
      {
        return parseResult;
      }

      return _false();
    }
    else if (result == ERROR)
    {
      return _error();
    }
    else
    {
      return parseStatementStartingWithRegister();
    }
  }

  private ParseResult parseRegisterInitialiser()
  {
    Tristate result = textParser.getExactCharacterSequence(keywords.assign(), true);
    if (result == TRUE)
    {
      ParseResult parseResult = parseArrayInitialiser();
      if (parseResult.isTrue())
      {
        return parseResult;
      }
      else if (parseResult.isError())
      {
        return parseResult;
      }

      parseResult = parseInitialExpression(new Expression());
      return parseResult;
    }
    else if (result == ERROR)
    {
      return _error();
    }
    else
    {
      return _false();
    }

  }

  private ParseResult parseInitialExpression(Expression expression)
  {
    Tristate result = textParser.getExactCharacterSequence(keywords.openRound());
    if (result == TRUE)
    {
      ParseResult parseResult = parseExpression(new Expression());
      if (parseResult.isError())
      {
        return parseResult;
      }

      result = textParser.getExactCharacterSequence(keywords.closeRound());
      if (result == TRUE)
      {
        return _true();
      }
      else if (result == ERROR)
      {
        return _error();
      }
      else
      {
        return _error("Expected ')'.");
      }
    }
    else if (result == FALSE)
    {
      LiteralResult literalResult = literal();
      if (literalResult.isTrue())
      {
        expression.add(new LiteralExpression(literalResult.getLiteral()));
        return _true();
      }
      else if (literalResult.isError())
      {
        return _error();
      }

      IntegerPointer index = new IntegerPointer();
      result = textParser.getIdentifier(keywords.unaryOperators, index, true);
      SixteenHighKeywordCode keyword;
      if (result == TRUE)
      {
        keyword = keywords.getKeyword(keywords.unaryOperators, index);
        Expression plusMinusExpression = new Expression();
        plusMinusExpression.add(new OperandExpression(keyword));
        ParseResult parseResult = parseExpression(plusMinusExpression);
        if (parseResult.isError())
        {
          return parseResult;
        }
      }
      else if (result == ERROR)
      {
        return _error();
      }
      else
      {
        keyword = UNKNOWN;
      }

      if (keyword != UNKNOWN)
      {
        expression.add(new OperandExpression(keyword));
      }

      StringZero registerName = new StringZero();
      result = textParser.getIdentifier(registerName, true);
      if (result == FALSE)
      {
        return _error("Expected register name.");
      }
      else if (result == ERROR)
      {
        return _error();
      }
      expression.add(new RegisterExpression(registerName.toString()));
      return _true();
    }
    else
    {
      return _error();
    }
  }

  private ParseResult parseExpression(Expression expression)
  {
    Tristate result = textParser.getExactCharacterSequence(keywords.openRound());
    if (result == TRUE)
    {
      ParseResult parseResult = parseExpression(new Expression());
      if (parseResult.isError())
      {
        return parseResult;
      }

      result = textParser.getExactCharacterSequence(keywords.closeRound());
      if (result == TRUE)
      {
        return _true();
      }
      else if (result == ERROR)
      {
        return _error();
      }
      else
      {
        return _error("Expected ')'.");
      }
    }
    else if (result == FALSE)
    {
      LiteralResult literalResult = literal();
      if (literalResult.isTrue())
      {
        expression.add(new LiteralExpression(literalResult.getLiteral()));
      }
      else if (literalResult.isError())
      {
        return _error();
      }
      else
      {
        return _false();
      }
    }
    else
    {
      return _error();
    }

    throw new SimulatorException("Should not get able to get here.");
  }

  private ParseResult parseResult(Tristate result)
  {
    if (result == TRUE)
    {
      return _true();
    }
    else if (result == ERROR)
    {
      return _error();
    }
    else
    {
      return _false();
    }
  }

  private ParseResult parseArrayInitialiser()
  {
    Tristate result = textParser.getExactCharacterSequence(keywords.openSquare());
    if (result == TRUE)
    {

    }
    else if (result == ERROR)
    {
      return _error();
    }
    else
    {

    }
    return _false();
  }

  private ParseResult parseStatementStartingWithRegister()
  {
    StringZero registerName = new StringZero();
    Tristate result = textParser.getIdentifier(registerName, true);
    if (result == ERROR)
    {
      return _error();
    }
    else if (result == FALSE)
    {
      return _false();
    }

    String identifier = registerName.toString();
    IntegerPointer index = new IntegerPointer();
    result = textParser.getIdentifier(keywords.followingIdentifiers, index, true);
    if (result == TRUE)
    {
      SixteenHighKeywordCode keyword = keywords.getKeyword(keywords.followingIdentifiers, index);
      ParseResult parseResult;
      parseResult = bitCompare(identifier, keyword);
      if (parseResult.isTrue())
      {
        return _true();
      }
      else if (parseResult.isError())
      {
        return _error("Expected %s.", getKeywordNames(keywords.getBitCompares()));
      }

      parseResult = crement(identifier, keyword);
      if (parseResult.isTrue())
      {
        return _true();
      }
      else if (parseResult.isError())
      {
        return _error("Expected %s.", getKeywordNames(keywords.getCrements()));
      }

//      parseResult = openSquare(identifier, keyword);
//      if (parseResult.isTrue())
//      {
//        return _true();
//      }
//      else if (parseResult.isError())
//      {
//        return _error("Expected %s.", getKeywordNames(keywords.getCrements()));
//      }

      parseResult = numberCompare(identifier, keyword);
      if (parseResult.isTrue())
      {
        return _true();
      }
      else if (parseResult.isError())
      {
        return _error("Expected %s.", getKeywordNames(keywords.getNumberCompares()));
      }

      parseResult = assignmentOperator(identifier, keyword);
      return parseResult;
    }
    else if (result == ERROR)
    {
      return _error();
    }
    else
    {
      return _false();
    }
  }

  private String getKeywordNames(List<SixteenHighKeywordCode> keywordCodes)
  {
    List<String> strings = getKeywordNameList(keywordCodes);
    return StringUtil.commaSeparateList(strings, "or");
  }

  private List<String> getKeywordNameList(List<SixteenHighKeywordCode> keywordCodes)
  {
    List<String> strings = new ArrayList<>();
    for (SixteenHighKeywordCode keywordCode : keywordCodes)
    {
      String name = keywords.getKeyword(keywordCode);
      strings.add(name);
    }
    return strings;
  }

  private LiteralResult literal()
  {
    return literalParser.parseLiteral(allowedSeparator);
  }

  private ParseResult assignmentOperator(String leftIdentifier, SixteenHighKeywordCode keyword)
  {
    if (keywords.getAssignments().contains(keyword))
    {
      Expression expression = new Expression();
      ParseResult parseResult = parseInitialExpression(expression);
      if (parseResult.isTrue())
      {
        code.addAssignment(leftIdentifier, keyword, expression);
        return _true();
      }
      else if (parseResult.isError())
      {
        return _error();
      }
      else
      {
        return _error("Expected expression.");
      }
    }
    return _false();
  }

  private ParseResult bitCompare(String identifier, SixteenHighKeywordCode keyword)
  {
    if (keywords.getBitCompares().contains(keyword))
    {
      code.addBitCompare(identifier, keyword);
      return _true();
    }
    return _false();
  }

  private ParseResult crement(String identifier, SixteenHighKeywordCode keyword)
  {
    if (keywords.getCrements().contains(keyword))
    {
      code.addCrement(identifier, keyword);
      return _true();
    }
    return _false();
  }

  private ParseResult numberCompare(String leftIdentifier, SixteenHighKeywordCode keyword)
  {
    if (keywords.getNumberCompares().contains(keyword))
    {
      StringZero registerName = new StringZero();
      Tristate result = textParser.getIdentifier(registerName, true);
      if (result == ERROR)
      {
        return _error();
      }
      else if (result == FALSE)
      {
        return _error("Expected identifier.");
      }

      String rightIdentifier = registerName.toString();
      code.addNumberCompare(leftIdentifier, rightIdentifier, keyword);
      return _true();
    }
    return _false();
  }

  public Code getCode()
  {
    return code;
  }

  public SixteenHighKeywords getKeywords()
  {
    return keywords;
  }
}

