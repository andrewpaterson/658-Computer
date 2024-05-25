package net.assembler.sixteenhigh.tokeniser;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.tokeniser.literal.LiteralParser;
import net.assembler.sixteenhigh.tokeniser.literal.LiteralResult;
import net.assembler.sixteenhigh.tokeniser.statment.ArrayDeclaration;
import net.assembler.sixteenhigh.tokeniser.statment.IfTokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.TokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.expression.*;
import net.common.logger.Logger;
import net.common.parser.StringZero;
import net.common.parser.TextParser;
import net.common.parser.TextParserPosition;
import net.common.parser.Tristate;
import net.common.parser.primitive.IntegerPointer;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode.*;
import static net.common.parser.TextParser.NUMBER_SEPARATOR_APOSTROPHE;
import static net.common.parser.Tristate.*;

public class SixteenHighTokeniser
{
  protected SixteenHighKeywords keywords;
  protected TextParser textParser;
  protected int statementIndex;
  protected TokenUnit unit;
  protected String filename;
  protected LiteralParser literalParser;
  protected int allowedSeparator;

  public SixteenHighTokeniser(Logger log,
                              SixteenHighKeywords keywords,
                              String filename,
                              TokenUnit unit,
                              String source)
  {
    this.filename = filename;
    this.keywords = keywords;
    this.textParser = new TextParser(source, log, filename);
    this.literalParser = new LiteralParser(textParser);

    this.unit = unit;
    this.statementIndex = 0;
    this.allowedSeparator = NUMBER_SEPARATOR_APOSTROPHE;
  }

  public ParseResult parse()
  {
    TokenStatement lastStatement = null;
    boolean canParseInterStatement = false;
    while (textParser.hasMoreText())
    {
      if (canParseInterStatement)
      {
        Tristate result = parseInterStatement();
        if (result == ERROR)
        {
          return _error();
        }
        if (result == TRUE)
        {
          if (lastStatement != null)
          {
            lastStatement.appendSemicolon();
          }
        }
      }

      lastStatement = null;

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

      parseResult = parseStruct();
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
        lastStatement = unit.getLast();
        canParseInterStatement = true;
        //noinspection UnnecessaryContinue
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
      unit.addEnd();
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

  private ParseResult parseStruct()
  {
    Tristate result = textParser.getExactIdentifier(keywords.struct(), true);
    if (result == TRUE)
    {
      StringZero structIdentifierZero = new StringZero();
      ParseResult parseResult = structIdentifier(structIdentifierZero);
      if (parseResult.isError())
      {
        return _error();
      }
      else if (parseResult.isFalse())
      {
        return _error("Expected identifier.");
      }

      unit.addStruct(structIdentifierZero.toString());
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

  private Tristate parseInterStatement()
  {
    int count = 0;
    for (; ; )
    {
      Tristate state = textParser.getExactCharacter(';', true);
      if (state == ERROR)
      {
        return ERROR;
      }
      else if (state == TRUE)
      {
        count++;
      }
      else if (state == FALSE)
      {
        if (count == 0)
        {
          return FALSE;
        }
        else
        {
          return TRUE;
        }
      }
    }
  }

  private ParseResult parseDirective()
  {
    IntegerPointer index = new IntegerPointer();
    Tristate result = textParser.getIdentifier(keywords.getDirectiveIdentifiers(), index, true);
    if (result == TRUE)
    {
      SixteenHighKeywordCode keyword = keywords.getKeyword(keywords.getDirectiveIdentifiers(), index);
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
        unit.addStartAddress((int) integerLiteral.getIntegerLiteral().getValue());
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
        unit.addEndAddress((int) integerLiteral.getIntegerLiteral().getValue());
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
      Tristate state = textParser.getIdentifier(keywords.getAccessModes(), index, true);
      if (state == TRUE)
      {
        SixteenHighKeywordCode accessMode = keywords.getKeyword(keywords.getAccessModes(), index);
        unit.addAccessMode(accessMode);
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
        unit.addAccessTime((int) integerLiteral.getIntegerLiteral().getValue());
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
      BaseTokenExpressionPointer expressionPointer = new BaseTokenExpressionPointer();
      ParseResult parseResult = parseInitialExpression(expressionPointer);
      if (parseResult.isTrue())
      {
        unit.addPush((TokenExpressionList) expressionPointer.expression);
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
    else
    {
      return _false();
    }
  }

  private ParseResult pullExpression(VariableTokenExpression variableExpression, SixteenHighKeywordCode keyword)
  {
    if (keyword == pull)
    {
      unit.addPull(variableExpression);
      return _true();
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
      IfTokenStatement ifStatement = unit.addIf(keyword);
      Tristate state = textParser.getExactIdentifier(keywords.go(), true);
      if (state == ERROR)
      {
        return _error();
      }
      else if (state == FALSE)
      {
        return _error("Expected keyword 'go'.");
      }

      FlowExpressionPointer expressionPointer = new FlowExpressionPointer();
      ParseResult parseResult = parseFlowExpression(keywords.getKeyword(keywords.go()), expressionPointer);
      if (parseResult.isError())
      {
        return parseResult;
      }
      else if (parseResult.isFalse())
      {
        return _error("Expected flow statement.");
      }
      ifStatement.setGo(expressionPointer.flowExpression);
      return _true();
    }
    return _false();
  }

  private ParseResult returnStatement(SixteenHighKeywordCode keyword)
  {
    if (keyword == ret)
    {
      unit.addReturn();
      return _true();
    }
    else
    {
      return _false();
    }
  }

  private ParseResult flowStatement(SixteenHighKeywordCode keyword)
  {
    FlowExpressionPointer expressionPointer = new FlowExpressionPointer();
    ParseResult parseResult = parseFlowExpression(keyword, expressionPointer);
    if (parseResult.isTrue())
    {
      unit.addFlow(expressionPointer.flowExpression);
      return parseResult;
    }
    else
    {
      return parseResult;
    }
  }

  private ParseResult parseFlowExpression(SixteenHighKeywordCode keyword, FlowExpressionPointer expressionPointer)
  {
    if (keyword == go)
    {
      StringZero goLabelZero = new StringZero();
      Tristate state = textParser.getIdentifier(goLabelZero, true);
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
        expressionPointer.setFlowExpression(new GoTokenExpression(goLabelZero.toString()));
        return _true();
      }
    }
    else if (keyword == gosub)
    {
      StringZero gosubLabelZero = new StringZero();
      ParseResult parseResult = blockIdentifier(gosubLabelZero, true);
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
        expressionPointer.setFlowExpression(new GosubTokenExpression(gosubLabelZero.toString()));
        return _true();
      }
    }
    else
    {
      return _false();
    }
  }

  private ParseResult primitiveDeclaration(SixteenHighKeywordCode keyword)
  {
    if (keywords.getPrimitiveTypes().contains(keyword))
    {
      ArrayDeclaration arrayDeclaration = new ArrayDeclaration();
      ParseResult parseResult = parseArrayDeclaration(arrayDeclaration);
      if (parseResult.isError())
      {
        return parseResult;
      }

      int pointerCount = asteriskCount();

      StringZero registerNameZero = new StringZero();
      parseResult = blockIdentifier(registerNameZero, true);
      if (parseResult.isFalseOrError())
      {
        return parseResult;
      }
      String registerName = registerNameZero.toString();

      BaseTokenExpressionPointer expressionPointer = new BaseTokenExpressionPointer();
      parseResult = parseVariableInitialiser(expressionPointer);
      if (parseResult.isError())
      {
        return parseResult;
      }

      if (registerName.startsWith("@@"))
      {
        unit.addPrimitiveVariable(keyword,
                                  registerName,
                                  Scope.global,
                                  arrayDeclaration.arrayMatrix,
                                  pointerCount,
                                  expressionPointer.expression);
      }
      else if (registerName.startsWith("@"))
      {
        unit.addPrimitiveVariable(keyword,
                                  registerName,
                                  Scope.unit,
                                  arrayDeclaration.arrayMatrix,
                                  pointerCount,
                                  expressionPointer.expression);
      }
      else
      {
        unit.addPrimitiveVariable(keyword,
                                  registerName,
                                  Scope.routine,
                                  arrayDeclaration.arrayMatrix,
                                  pointerCount,
                                  expressionPointer.expression);
      }
      return _true();
    }
    return _false();
  }

  private ParseResult structDeclaration()
  {
    StringZero structIdentifierZero = new StringZero();
    ParseResult parseResult = structIdentifier(structIdentifierZero);
    if (parseResult.isFalseOrError())
    {
      return parseResult;
    }

    ArrayDeclaration arrayDeclaration = new ArrayDeclaration();
    parseResult = parseArrayDeclaration(arrayDeclaration);
    if (parseResult.isError())
    {
      return parseResult;
    }

    int pointerCount = asteriskCount();

    StringZero registerNameZero = new StringZero();
    parseResult = blockIdentifier(registerNameZero, true);
    if (parseResult.isFalseOrError())
    {
      return parseResult;
    }
    String registerName = registerNameZero.toString();

    BaseTokenExpressionPointer expressionPointer = new BaseTokenExpressionPointer();
    parseResult = parseVariableInitialiser(expressionPointer);
    if (parseResult.isError())
    {
      return parseResult;
    }

    String structIdentifier = structIdentifierZero.toString();
    if (registerName.startsWith("@@"))
    {
      unit.addStructVariable(structIdentifier,
                             registerName,
                             Scope.global,
                             arrayDeclaration.arrayMatrix,
                             pointerCount,
                             expressionPointer.expression);
    }
    else if (registerName.startsWith("@"))
    {
      unit.addStructVariable(structIdentifier,
                             registerName,
                             Scope.unit,
                             arrayDeclaration.arrayMatrix,
                             pointerCount,
                             expressionPointer.expression);
    }
    else
    {
      unit.addStructVariable(structIdentifier,
                             registerName,
                             Scope.routine,
                             arrayDeclaration.arrayMatrix,
                             pointerCount,
                             expressionPointer.expression);
    }
    return _true();
  }

  private int asteriskCount()
  {
    textParser.skipWhiteSpace();

    int asteriskCount = 0;
    for (; ; )
    {
      Tristate result = textParser.getExactCharacter('*', false);
      if (result == TRUE)
      {
        asteriskCount++;
      }
      else
      {
        break;
      }
    }
    return asteriskCount;
  }

  private ParseResult parseLabel()
  {
    TextParserPosition position = textParser.saveSettings();
    StringZero zeroIdentifier = new StringZero();
    ParseResult parseResult = blockIdentifier(zeroIdentifier, true);
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
      unit.addRoutine(identifier, Scope.global);
    }
    else if (identifier.startsWith("@"))
    {
      unit.addRoutine(identifier, Scope.unit);
    }
    else
    {
      unit.addLocalLabel(identifier);
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

  private ParseResult blockIdentifier(StringZero fullIdentifier, boolean skipWhiteSpace)
  {
    if (skipWhiteSpace)
    {
      textParser.skipWhiteSpace();
    }

    int at = 0;
    Tristate state = textParser.getExactCharacter('@', false);
    if (state == ERROR)
    {
      return _error();
    }
    if (state == TRUE)
    {
      at++;
    }

    state = textParser.getExactCharacter('@', false);
    if (state == ERROR)
    {
      return _error();
    }
    if (state == TRUE)
    {
      at++;
    }

    StringZero identifier = new StringZero();
    state = textParser.getIdentifier(identifier, false);
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

  private ParseResult structIdentifier(StringZero fullIdentifier)
  {
    int at = 0;
    Tristate state = textParser.getExactCharacter('@', true);
    if (state == ERROR)
    {
      return _error();
    }
    else if (state == TRUE)
    {
      at++;
    }
    else
    {
      return _false();
    }

    state = textParser.getExactCharacter('@', false);
    if (state == ERROR)
    {
      return _error();
    }
    if (state == TRUE)
    {
      at++;
    }

    StringZero identifier = new StringZero();
    state = textParser.getIdentifier(identifier, false);
    if (state == ERROR)
    {
      return _error();
    }
    else if (state == FALSE)
    {
      return _error("Expected identifier.");
    }
    if (at == 1)
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

  public Tristate getIdentifier(List<String> allowedIdentifiers,
                                List<String> allowedStrings,
                                SixteenHighKeywordCodePointer keywordPointer)
  {
    IntegerPointer index = new IntegerPointer();
    Tristate result = textParser.getExactCharacterSequence(allowedStrings, index, true);
    if (result == TRUE)
    {
      SixteenHighKeywordCode keyword = keywords.getKeyword(allowedStrings, index);
      keywordPointer.setKeyword(keyword);
      return TRUE;
    }
    else if (result == ERROR)
    {
      return ERROR;
    }

    result = textParser.getIdentifier(allowedIdentifiers, index, true);
    if (result == TRUE)
    {
      SixteenHighKeywordCode keyword = keywords.getKeyword(allowedIdentifiers, index);
      keywordPointer.setKeyword(keyword);
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

  private ParseResult parseStatement()
  {
    SixteenHighKeywordCodePointer keywordPointer = new SixteenHighKeywordCodePointer();
    Tristate result = getIdentifier(keywords.getLeadingIdentifiers(), keywords.getLeadingStrings(), keywordPointer);
    if (result == TRUE)
    {
      return parseLeadingKeywordStatement(keywordPointer);
    }
    else if (result == ERROR)
    {
      return _error();
    }

    textParser.pushPosition();
    ParseResult parseResult = parseStatementStartingWithVariable();
    if (parseResult.isTrue())
    {
      textParser.passPosition();
      return parseResult;
    }
    else if (parseResult.isError())
    {
      textParser.passPosition();
      return parseResult;
    }
    textParser.popPosition();

    textParser.pushPosition();
    parseResult = structDeclaration();
    if (parseResult.isTrue())
    {
      textParser.passPosition();
      return parseResult;
    }
    else if (parseResult.isError())
    {
      textParser.passPosition();
      return parseResult;
    }
    textParser.popPosition();
    return parseResult;
  }

  private ParseResult parseLeadingKeywordStatement(SixteenHighKeywordCodePointer keywordPointer)
  {
    ParseResult parseResult;
    SixteenHighKeywordCode keyword = keywordPointer.keyword;
    parseResult = primitiveDeclaration(keyword);
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

  private ParseResult parseVariableInitialiser(BaseTokenExpressionPointer expressionPointer)
  {
    Tristate result = textParser.getExactCharacterSequence(keywords.assign());
    if (result == TRUE)
    {
      ArrayTokenExpressionInitialiser arrayExpressionInitialiser = new ArrayTokenExpressionInitialiser();
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

      BaseTokenExpressionPointer expressablePointer = new BaseTokenExpressionPointer();
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

    result = textParser.getExactCharacterSequence(keywords.pull());
    if (result == TRUE)
    {
      expressionPointer.setExpression(new PullTokenExpression());
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

  private ParseResult parseUnaryComponent(ExpressablePointer expressablePointer)
  {
    LiteralResult literalResult = literal();
    if (literalResult.isTrue())
    {
      expressablePointer.setExpression(new LiteralTokenExpression(literalResult.getLiteral()));
      return _true();
    }
    else if (literalResult.isError())
    {
      return _error();
    }

    IntegerPointer index = new IntegerPointer();
    Tristate result = textParser.getExactCharacterSequence(keywords.getUnaryStrings(), index, true);
    SixteenHighKeywordCode keyword;
    if (result == TRUE)
    {
      keyword = keywords.getKeyword(keywords.getUnaryStrings(), index);
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
        expressablePointer.setExpression(new UnaryTokenExpression(keyword, childExpressablePointer.expression));
      }
      else
      {
        expressablePointer.setExpression(childExpressablePointer.expression);
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

  private ParseResult parseInitialExpression(BaseTokenExpressionPointer expressionPointer)
  {
    ExpressablePointer expressablePointer = new ExpressablePointer();
    ParseResult parseResult = parseUnaryComponent(expressablePointer);
    if (parseResult.isError())
    {
      return parseResult;
    }
    else if (parseResult.isTrue())
    {
      if (expressablePointer.expression.isList())
      {
        expressionPointer.setExpression((BaseTokenExpression) expressablePointer.expression);
      }
      else
      {
        TokenExpressionList expressions = new TokenExpressionList();
        expressions.add(expressablePointer.expression);
        expressionPointer.setExpression(expressions);
      }
      return parseResult;
    }
    else
    {
      return parseResult;
    }
  }

  private ParseResult parseArrayDeclaration(ArrayDeclaration arrayDeclaration)
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
          arrayDeclaration.addArray(literal.getIntegerLiteral().getValue());
          return parseArrayDeclaration(arrayDeclaration);
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
    BaseTokenExpressionPointer expressionPointer = new BaseTokenExpressionPointer();
    ParseResult parseResult = parseExpression(expressionPointer);
    if (parseResult.isTrue())
    {
      expressablePointer.setExpression(expressionPointer.expression);
      return parseResult;
    }
    else if (parseResult.isError())
    {
      return parseResult;
    }

    VariableExpressionPointer variableExpressionPointer = new VariableExpressionPointer();
    parseResult = parseVariable(variableExpressionPointer);
    if (parseResult.isTrue())
    {
      expressablePointer.setExpression(variableExpressionPointer.variableExpression);
      return _true();
    }
    else
    {
      return parseResult;
    }
  }

  private ParseResult parseExpression(BaseTokenExpressionPointer expressionPointer)
  {
    // Component: Expresion | Register
    // UnaryComponent: Literal | UnaryOperator Component
    // Register: name { [ UnaryComponent ] ... { [ UnaryComponent ] } }
    // Expression: ( UnaryComponent { BinaryOperator ...  { UnaryComponent BinaryOperator } } UnaryComponent )

    Tristate result = textParser.getExactCharacterSequence(keywords.openRound());
    if (result == TRUE)
    {
      TokenExpressionList expressions = new TokenExpressionList();
      expressionPointer.setExpression(expressions);
      for (; ; )
      {
        ExpressablePointer childExpressablePointer = new ExpressablePointer();
        ParseResult parseResult = parseUnaryComponent(childExpressablePointer);
        if (parseResult.isTrue())
        {
          expressions.add(childExpressablePointer.expression);
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
          expressions.add(childExpressablePointer.expression);
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
    Tristate result = textParser.getExactCharacterSequence(keywords.getBinaryString(), index, true);
    SixteenHighKeywordCode keyword;
    if (result == TRUE)
    {
      keyword = keywords.getKeyword(keywords.getBinaryString(), index);
      expressablePointer.setExpression(new OperandTokenExpression(keyword));
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

  private ParseResult parseArrayInitialiser(ArrayTokenExpressionInitialiser arrayExpressionInitialiser)
  {
    Tristate result = textParser.getExactCharacterSequence(keywords.openSquare());
    if (result == TRUE)
    {
      for (; ; )
      {
        ArrayTokenExpressionInitialiser innerArrayExpressionInitialiser = new ArrayTokenExpressionInitialiser();
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

        BaseTokenExpressionPointer expressionPointer = new BaseTokenExpressionPointer();
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

  private ParseResult parseVariable(VariableExpressionPointer expressionPointer)
  {
    int dereferenceCount = asteriskCount();

    boolean reference = false;
    if (dereferenceCount == 0)
    {
      Tristate result = textParser.getExactCharacter('&', false);
      if (result == TRUE)
      {
        reference = true;
      }
    }

    StringZero registerNameZero = new StringZero();
    ParseResult parseResult = blockIdentifier(registerNameZero, false);
    if (parseResult.isFalseOrError())
    {
      return parseResult;
    }

    String registerName = registerNameZero.toString();
    VariableMember variableMember = new VariableMember(registerName);
    VariableTokenExpression variableExpression = new VariableTokenExpression(variableMember, dereferenceCount, reference);
    for (; ; )
    {
      for (; ; )
      {
        Tristate result = textParser.getExactCharacterSequence(keywords.openSquare());
        if (result == TRUE)
        {
          ExpressablePointer expressablePointer = new ExpressablePointer();
          parseResult = parseUnaryComponent(expressablePointer);
          if (parseResult.isTrue())
          {
            result = textParser.getExactCharacterSequence(keywords.closeSquare());
            if (result == TRUE)
            {
              variableMember.addArrayIndex(expressablePointer.expression);
            }
            else if (result == ERROR)
            {
              return _error();
            }
            else
            {
              return _error("Expected: '].");
            }
          }
          else if (parseResult.isError())
          {
            return parseResult;
          }
          else
          {
            return _error("Expected Expression.");
          }
        }
        else if (result == ERROR)
        {
          return _error();
        }
        else
        {
          break;
        }
      }

      Tristate result = textParser.getExactCharacter('.');
      if (result == TRUE)
      {
        StringZero memberNameZero = new StringZero();
        result = textParser.getIdentifier(memberNameZero, true);
        if (result == TRUE)
        {
          variableMember = new VariableMember(memberNameZero.toString());
          variableExpression.addMember(variableMember);
        }
        else if (result == ERROR)
        {
          return _error();
        }
        else
        {
          return _error("Expected identifier.");
        }
      }
      else if (result == ERROR)
      {
        return _error();
      }
      else
      {
        break;
      }
    }
    expressionPointer.setVariableExpression(variableExpression);
    return _true();
  }

  private ParseResult parseStatementStartingWithVariable()
  {
    VariableExpressionPointer expressionPointer = new VariableExpressionPointer();
    ParseResult parseResult = parseVariable(expressionPointer);
    if (parseResult.isFalseOrError())
    {
      return parseResult;
    }
    VariableTokenExpression variableExpression = expressionPointer.variableExpression;

    SixteenHighKeywordCodePointer keywordPointer = new SixteenHighKeywordCodePointer();
    Tristate result = getIdentifier(keywords.getFollowingIdentifiers(), keywords.getFollowingStrings(), keywordPointer);
    if (result == TRUE)
    {
      SixteenHighKeywordCode keyword = keywordPointer.keyword;
      parseResult = bitCompare(variableExpression, keyword);
      if (parseResult.isTrue())
      {
        return _true();
      }
      else if (parseResult.isError())
      {
        return _error("Expected %s.", getKeywordNames(keywords.getBitCompares()));
      }

      parseResult = crement(variableExpression, keyword);
      if (parseResult.isTrue())
      {
        return _true();
      }
      else if (parseResult.isError())
      {
        return _error("Expected %s.", getKeywordNames(keywords.getCrements()));
      }

      parseResult = numberCompare(variableExpression, keyword);
      if (parseResult.isTrue())
      {
        return _true();
      }
      else if (parseResult.isError())
      {
        return _error("Expected %s.", getKeywordNames(keywords.getNumberCompares()));
      }

      parseResult = pullExpression(variableExpression, keyword);
      if (parseResult.isTrue())
      {
        return _true();
      }
      else if (parseResult.isError())
      {
        return _error("Expected %s.", getKeywordNames(keywords.getNumberCompares()));
      }

      parseResult = assignmentOperator(variableExpression, keyword);
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

  private ParseResult assignmentOperator(VariableTokenExpression leftExpression, SixteenHighKeywordCode keyword)
  {
    if (keywords.getAssignments().contains(keyword))
    {
      BaseTokenExpressionPointer expressionPointer = new BaseTokenExpressionPointer();
      ParseResult parseResult = parseInitialExpression(expressionPointer);
      if (parseResult.isTrue())
      {
        unit.addAssignment(leftExpression, keyword, (TokenExpressionList) expressionPointer.expression);
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

  private ParseResult bitCompare(VariableTokenExpression variableExpression, SixteenHighKeywordCode keyword)
  {
    if (keywords.getBitCompares().contains(keyword))
    {
      unit.addBitCompare(variableExpression, keyword);
      return _true();
    }
    return _false();
  }

  private ParseResult crement(VariableTokenExpression variableExpression, SixteenHighKeywordCode keyword)
  {
    if (keywords.getCrements().contains(keyword))
    {
      unit.addCrement(variableExpression, keyword);
      return _true();
    }
    return _false();
  }

  private ParseResult numberCompare(VariableTokenExpression leftVariableExpression, SixteenHighKeywordCode keyword)
  {
    if (keywords.getNumberCompares().contains(keyword))
    {
      BaseTokenExpressionPointer expressionPointer = new BaseTokenExpressionPointer();
      ParseResult parseResult = parseInitialExpression(expressionPointer);

      if (parseResult.isError())
      {
        return parseResult;
      }
      else if (parseResult.isFalse())
      {
        return _error("Expected Expression.");
      }

      unit.addNumberCompare(leftVariableExpression, (TokenExpressionList) expressionPointer.expression, keyword);
      return _true();
    }
    return _false();
  }

  public TokenUnit getUnit()
  {
    return unit;
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
    StringBuilder pad = StringUtil.pad(" ", textParser.getPosition() - startOfLine);
    pad.append("^");

    return "Error at position [" + textParser.getPosition() + "]: Unexpected: " + errorPosition + "\n" + errorLine + "\n" + pad.toString();
  }

  public boolean isCompleted()
  {
    return textParser.getPositions().size() == 0;
  }
}

