package net.assembler.sixteenhigh.tokeniser.precedence;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.ParseResult;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.SixteenHighTokeniser;
import net.assembler.sixteenhigh.tokeniser.literal.CTInt;
import net.assembler.sixteenhigh.tokeniser.literal.CTLiteral;
import net.assembler.sixteenhigh.tokeniser.literal.CTString;
import net.assembler.sixteenhigh.tokeniser.statment.AssignmentTokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.TokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.expression.*;
import net.common.logger.Logger;
import net.common.parser.Tristate;
import net.logicim.assertions.ValidationException;

import java.util.List;

import static net.logicim.assertions.Validator.*;

public class TokenPrecedenceTest
{
  protected static void testSingleLiteral()
  {
    SixteenHighTokeniser parser = createParser("i = \"ABC\"");
    TokenStatement tokenStatement = getTokenStatement(parser);
    AssignmentTokenStatement assignmentTokenStatement = (AssignmentTokenStatement) tokenStatement;
    TokenExpressionList expressionList = assignmentTokenStatement.getRightExpressions();

    TokenExpression expression = TokenPrecedence.getInstance().orderByPrecedence(expressionList);
    validateLiteral("ABC", expression);
  }

  protected static void testSingleBracketedLiteral()
  {
    SixteenHighTokeniser parser = createParser("i = (\"XYZ\")");
    TokenStatement tokenStatement = getTokenStatement(parser);
    AssignmentTokenStatement assignmentTokenStatement = (AssignmentTokenStatement) tokenStatement;
    TokenExpressionList expressionList = assignmentTokenStatement.getRightExpressions();

    TokenExpression expression = TokenPrecedence.getInstance().orderByPrecedence(expressionList);
    validateLiteral("XYZ", expression);
  }

  protected static void testMultiplyThenAdd()
  {
    SixteenHighTokeniser parser = createParser("i = (c * 3 + (x - y))");
    TokenStatement tokenStatement = getTokenStatement(parser);
    AssignmentTokenStatement assignmentTokenStatement = (AssignmentTokenStatement) tokenStatement;
    TokenExpressionList expressionList = assignmentTokenStatement.getRightExpressions();

    TokenExpression expression = TokenPrecedence.getInstance().orderByPrecedence(expressionList);
    validateTrue(expression.isBinary());
    BinaryTokenExpression binaryTokenExpression1 = (BinaryTokenExpression) expression;
    validateEquals(SixteenHighKeywordCode.add, binaryTokenExpression1.getOperator());

    TokenExpression leftExpression = binaryTokenExpression1.getLeftExpression();
    validateTrue(leftExpression.isBinary());
    TokenExpression rightExpression = binaryTokenExpression1.getRightExpression();
    validateTrue(rightExpression.isBinary());

    BinaryTokenExpression binaryTokenExpression2 = (BinaryTokenExpression) leftExpression;
    validateVariable("c", binaryTokenExpression2.getLeftExpression());
    validateEquals(SixteenHighKeywordCode.multiply, binaryTokenExpression2.getOperator());
    validateLiteral(3, binaryTokenExpression2.getRightExpression());

    BinaryTokenExpression binaryTokenExpression3 = (BinaryTokenExpression) rightExpression;
    validateVariable("x", binaryTokenExpression3.getLeftExpression());
    validateEquals(SixteenHighKeywordCode.subtract, binaryTokenExpression3.getOperator());
    validateVariable("y", binaryTokenExpression3.getRightExpression());
  }

  protected static void testAddThenMultiplyThenDivide()
  {
    SixteenHighTokeniser parser = createParser("i = (3 + 4 * 5 / 6)");
    TokenStatement tokenStatement = getTokenStatement(parser);
    AssignmentTokenStatement assignmentTokenStatement = (AssignmentTokenStatement) tokenStatement;
    TokenExpressionList expressionList = assignmentTokenStatement.getRightExpressions();

    TokenExpression expression = TokenPrecedence.getInstance().orderByPrecedence(expressionList);
    validateTrue(expression.isBinary());
    BinaryTokenExpression binaryTokenExpression1 = (BinaryTokenExpression) expression;
    validateEquals(SixteenHighKeywordCode.add, binaryTokenExpression1.getOperator());

    validateLiteral(3, binaryTokenExpression1.getLeftExpression());
    TokenExpression rightExpression = binaryTokenExpression1.getRightExpression();
    validateTrue(rightExpression.isBinary());

    BinaryTokenExpression binaryTokenExpression3 = (BinaryTokenExpression) rightExpression;
    TokenExpression leftExpression = binaryTokenExpression3.getLeftExpression();
    validateTrue(leftExpression.isBinary());
    BinaryTokenExpression binaryTokenExpression2 = (BinaryTokenExpression) leftExpression;
    validateLiteral(4, binaryTokenExpression2.getLeftExpression());
    validateEquals(SixteenHighKeywordCode.divide, binaryTokenExpression3.getOperator());
    validateLiteral(5, binaryTokenExpression2.getRightExpression());

    validateLiteral(6, binaryTokenExpression3.getRightExpression());
  }

  private static void validateVariable(String expectedVariable, TokenExpression expression)
  {
    validateTrue(expression.isVariable());
    VariableTokenExpression variableTokenExpression = (VariableTokenExpression) expression;
    validateEquals(expectedVariable, variableTokenExpression.toString());
  }

  private static void validateLiteral(int expectedValue, TokenExpression expression)
  {
    validateTrue(expression.isLiteral());
    LiteralTokenExpression literalTokenExpression = (LiteralTokenExpression) expression;
    CTLiteral literal = literalTokenExpression.getLiteral();
    validateEquals(CTInt.class, literal.getClass());
    CTInt anInt = (CTInt) literal;
    validateTrue(anInt.isValid());
    validateEquals(expectedValue, (int) anInt.getValue());
  }

  private static void validateLiteral(String expectedValue, TokenExpression expression)
  {
    validateTrue(expression.isLiteral());
    LiteralTokenExpression literalTokenExpression = (LiteralTokenExpression) expression;
    CTLiteral literal = literalTokenExpression.getLiteral();
    validateEquals(CTString.class, literal.getClass());
    CTString aString = (CTString) literal;
    validateEquals(expectedValue, aString.getValue());
  }

  private static TokenStatement getTokenStatement(SixteenHighTokeniser parser)
  {
    ParseResult parseResult = parser.parse();
    Tristate result = parseResult.getState();
    validateNoError(result, parser.getError());
    TokenUnit unit = parser.getUnit();
    List<TokenStatement> statements = unit.getStatements();
    validate(1, statements.size());
    return statements.get(0);
  }

  private static void validateNoError(Tristate result, String error)
  {
    if (result != Tristate.TRUE)
    {
      throw new ValidationException(error);
    }
  }

  private static SixteenHighTokeniser createParser(String contents)
  {
    return new SixteenHighTokeniser(new Logger(),
                                    new SixteenHighKeywords(),
                                    "",
                                    new TokenUnit(""),
                                    contents);
  }

  public static void test()
  {
    testSingleLiteral();
    testSingleBracketedLiteral();
    testMultiplyThenAdd();
    testAddThenMultiplyThenDivide();
  }
}

