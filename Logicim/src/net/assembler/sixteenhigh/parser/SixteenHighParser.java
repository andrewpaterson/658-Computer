package net.assembler.sixteenhigh.parser;

import net.assembler.sixteenhigh.parser.literal.LiteralParser;
import net.assembler.sixteenhigh.parser.literal.LiteralResult;
import net.assembler.sixteenhigh.parser.statment.*;
import net.assembler.sixteenhigh.parser.statment.expression.*;
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
        boolean endOfFile = textParser.skipWhiteSpace();
        if (endOfFile)
        {
          return _true();
        }
        else
        {
          return _error();
        }
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
      RegisterArrayDeclaration arrayDeclaration = new RegisterArrayDeclaration();
      ParseResult parseResult = parseArrayDeclaration(arrayDeclaration);
      if (parseResult.isError())
      {
        return parseResult;
      }

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
      parseResult = blockIdentifier(registerNameZero);
      if (parseResult.isFalseOrError())
      {
        return parseResult;
      }

      BaseExpressionPointer expressionPointer = new BaseExpressionPointer();
      parseResult = parseRegisterInitialiser(expressionPointer);
      if (parseResult.isError())
      {
        return parseResult;
      }

      String registerName = registerNameZero.toString();
      if (registerName.startsWith("@@"))
      {
        GlobalVariable globalVariable = code.addGlobalVariable(keyword,
                                                               registerName,
                                                               arrayDeclaration.arrayMatrix,
                                                               asteriskCount,
                                                               expressionPointer.expression);
        context.addGlobalVariable(globalVariable);
      }
      else if (registerName.startsWith("@"))
      {
        code.addFileVariable(keyword,
                             registerName,
                             arrayDeclaration.arrayMatrix,
                             asteriskCount,
                             expressionPointer.expression);
      }
      else
      {
        code.addLocalVariable(keyword,
                              registerName,
                              arrayDeclaration.arrayMatrix,
                              asteriskCount,
                              expressionPointer.expression);
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
      if (parseResult.isTrueOrError())
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
      textParser.pushPosition();
      ParseResult parseResult = parseStatementStartingWithRegister();
      if (parseResult.isTrue())
      {
        textParser.passPosition();
        return parseResult;
      }
      else if (parseResult.isError())
      {
        textParser.popPosition();
        return parseResult;
      }
      else
      {
        textParser.popPosition();
        return parseResult;
      }
    }
  }

  private ParseResult parseRegisterInitialiser(BaseExpressionPointer expressionPointer)
  {
    Tristate result = textParser.getExactCharacterSequence(keywords.assign());
    if (result == TRUE)
    {
      ArrayExpressionInitialiser arrayExpressionInitialiser = new ArrayExpressionInitialiser();
      ParseResult parseResult = parseArrayInitialiser(arrayExpressionInitialiser);
      if (parseResult.isTrue())
      {
        expressionPointer.setExpression(arrayExpressionInitialiser);
        return parseResult;
      }
      else if (parseResult.isError())
      {
        return parseResult;
      }

      BaseExpressionPointer expressablePointer = new BaseExpressionPointer();
      parseResult = parseInitialExpression(expressablePointer);
      if (parseResult.isTrue())
      {
        expressionPointer.setExpression(expressablePointer.expression);
        return parseResult;
      }
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

  private ParseResult parseUnaryComponent(ExpressablePointer expressablePointer)
  {
    LiteralResult literalResult = literal();
    if (literalResult.isTrue())
    {
      expressablePointer.setExpressable(new LiteralExpression(literalResult.getLiteral()));
      return _true();
    }
    else if (literalResult.isError())
    {
      return _error();
    }

    IntegerPointer index = new IntegerPointer();
    Tristate result = textParser.getIdentifier(keywords.unaryOperators, index, true);
    SixteenHighKeywordCode keyword;
    if (result == TRUE)
    {
      keyword = keywords.getKeyword(keywords.unaryOperators, index);
    }
    else if (result == ERROR)
    {
      return _error();
    }
    else
    {
      keyword = UNKNOWN;
    }

    ExpressablePointer childExpressablePointer = new ExpressablePointer();
    ParseResult parseResult = parseComponent(childExpressablePointer);
    if (parseResult.isTrue())
    {
      if (keyword != UNKNOWN)
      {
        expressablePointer.setExpressable(new UnaryExpression(keyword, childExpressablePointer.expressable));
      }
      else
      {
        expressablePointer.setExpressable(childExpressablePointer.expressable);
      }
      return parseResult;
    }
    else if (parseResult.isError())
    {
      return parseResult;
    }
    else
    {
      return parseResult;
    }
  }

  private ParseResult parseInitialExpression(BaseExpressionPointer expressionPointer)
  {
    ExpressablePointer expressablePointer = new ExpressablePointer();
    ParseResult parseResult = parseUnaryComponent(expressablePointer);
    if (parseResult.isError())
    {
      return parseResult;
    }
    else if (parseResult.isTrue())
    {
      if (expressablePointer.expressable.isExpression())
      {
        expressionPointer.setExpression((BaseExpression) expressablePointer.expressable);
      }
      else
      {
        Expression expression = new Expression();
        expression.add(expressablePointer.expressable);
        expressionPointer.setExpression(expression);
      }
      return parseResult;
    }
    else
    {
      return parseResult;
    }
  }

  private ParseResult parseArrayDeclaration(RegisterArrayDeclaration registerArrayDeclaration)
  {
    Tristate result = textParser.getExactCharacterSequence(keywords.openSquare());
    if (result == TRUE)
    {
      LiteralResult literal = literalParser.getIntegerLiteral(allowedSeparator);
      if (literal.isTrue())
      {
        result = textParser.getExactCharacterSequence(keywords.closeSquare());
        if (result == TRUE)
        {
          registerArrayDeclaration.addArray(literal.getIntegerLiteral().getValue());
          return parseArrayDeclaration(registerArrayDeclaration);
        }
        else
        {
          return _error("Expected ']'.");
        }
      }
      else
      {
        return _error("Expected integer.");
      }
    }
    else if (result == ERROR)
    {
      return _false();
    }
    else
    {
      return _false();
    }
  }

  private ParseResult parseComponent(ExpressablePointer expressablePointer)
  {
    BaseExpressionPointer expressionPointer = new BaseExpressionPointer();
    ParseResult parseResult = parseExpression(expressionPointer);
    if (parseResult.isTrue())
    {
      expressablePointer.setExpressable(expressionPointer.expression);
      return parseResult;
    }
    else if (parseResult.isError())
    {
      return parseResult;
    }

    StringZero registerZero = new StringZero();
    Tristate result = textParser.getIdentifier(registerZero, true);
    if (result == TRUE)
    {
      expressablePointer.setExpressable(new RegisterExpression(registerZero.toString()));
      return _true();
    }
    else if (result == ERROR)
    {
      return _error();
    }
    return _false();
  }

  private ParseResult parseExpression(BaseExpressionPointer expressionPointer)
  {
    // Component: Expresion | Register
    // UnaryComponent: Literal | UnaryOperator Component
    // Expression: ( UnaryComponent BinaryOperator ...  UnaryComponent BinaryOperator ... UnaryComponent )

    Tristate result = textParser.getExactCharacterSequence(keywords.openRound());
    if (result == TRUE)
    {
      Expression expression = new Expression();
      expressionPointer.setExpression(expression);
      for (; ; )
      {
        ExpressablePointer childExpressablePointer = new ExpressablePointer();
        ParseResult parseResult = parseUnaryComponent(childExpressablePointer);
        if (parseResult.isTrue())
        {
          expression.add(childExpressablePointer.expressable);
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

        parseResult = parseBinaryOperator(childExpressablePointer);
        if (parseResult.isTrue())
        {
          expression.add(childExpressablePointer.expressable);
        }
        else if (parseResult.isError())
        {
          return _error();
        }
        else
        {
          return _error("Expected ')'.");
        }
      }
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

  private ParseResult parseBinaryOperator(ExpressablePointer expressablePointer)
  {
    IntegerPointer index = new IntegerPointer();
    Tristate result = textParser.getIdentifier(keywords.binaryOperators, index, true);
    SixteenHighKeywordCode keyword;
    if (result == TRUE)
    {
      keyword = keywords.getKeyword(keywords.binaryOperators, index);
      expressablePointer.setExpressable(new OperandExpression(keyword));
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

  private ParseResult parseArrayInitialiser(ArrayExpressionInitialiser arrayExpressionInitialiser)
  {
    Tristate result = textParser.getExactCharacterSequence(keywords.openSquare());
    if (result == TRUE)
    {
      for (; ; )
      {
        ArrayExpressionInitialiser innerArrayExpressionInitialiser = new ArrayExpressionInitialiser();
        ParseResult parseResult = parseArrayInitialiser(innerArrayExpressionInitialiser);
        if (parseResult.isTrue())
        {
          arrayExpressionInitialiser.add(innerArrayExpressionInitialiser);
          continue;
        }
        else if (parseResult.isError())
        {
          return parseResult;
        }

        BaseExpressionPointer expressionPointer = new BaseExpressionPointer();
        parseResult = parseInitialExpression(expressionPointer);
        if (parseResult.isTrue())
        {
          arrayExpressionInitialiser.add(expressionPointer.expression);
        }
        result = textParser.getExactCharacterSequence(",");
        if (result == ERROR)
        {
          return _error();
        }
        else if (result == FALSE)
        {
          result = textParser.getExactCharacterSequence(keywords.closeSquare());
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
            return _error("Expected ']'.");
          }
        }
      }
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
      BaseExpressionPointer expressionPointer = new BaseExpressionPointer();
      ParseResult parseResult = parseInitialExpression(expressionPointer);
      if (parseResult.isTrue())
      {
        code.addAssignment(leftIdentifier, keyword, (Expression) expressionPointer.expression);
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

  public String getError()
  {
    if (textParser.isOutsideText())
    {
      return "";
    }

    TextParserPosition textParserPosition = textParser.saveSettings();
    textParser.findStartOfLine();
    int startOfLine = textParser.getPosition();
    textParser.loadSettings(textParserPosition);
    textParserPosition = textParser.saveSettings();
    textParser.findEndOfLine();
    int endOfLine = textParser.getPosition();
    textParser.loadSettings(textParserPosition);
    String text = textParser.getText();
    String errorLine = text.substring(startOfLine, endOfLine + 1);
    errorLine = errorLine.replace("\t", "  ");
    String errorPosition = text.substring(textParser.getPosition(), endOfLine + 1);
    errorPosition = errorPosition.replace("\t", "  ");
    int index = errorPosition.indexOf(' ');
    if (index != -1)
    {
      errorPosition = errorPosition.substring(0, index);
    }
    StringBuilder pad = StringUtil.pad(" ", textParser.getPosition() - startOfLine + 1);
    pad.append("^");

    return "Error at position [" + textParser.getPosition() + "]: Unexpected: " + errorPosition + "\n" + errorLine + "\n" + pad.toString();
  }
}

