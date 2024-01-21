package net.assembler.sixteenhigh.tokeniser.precedence;

import net.assembler.sixteenhigh.semanticiser.expression.operator.OperatorCode;
import net.assembler.sixteenhigh.semanticiser.expression.operator.SixteenHighOperatorMap;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.BinaryTokenExpression;
import net.assembler.sixteenhigh.tokeniser.statment.expression.OperandTokenExpression;
import net.assembler.sixteenhigh.tokeniser.statment.expression.TokenExpression;
import net.assembler.sixteenhigh.tokeniser.statment.expression.TokenExpressionList;
import net.common.SimulatorException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TokenPrecedence
{
  protected SixteenHighOperatorMap codeMap;
  protected Map<OperatorCode, Integer> operatorPrecedence;
  protected int lowestPrecedence;

  private static TokenPrecedence instance;

  public TokenPrecedence()
  {
    codeMap = SixteenHighOperatorMap.getInstance();

    createOperatorPrecedenceMap();

    calculateLowestOperatorPrecedence();
  }

  public static TokenPrecedence getInstance()
  {
    if (instance == null)
    {
      instance = new TokenPrecedence();
    }
    return instance;
  }

  protected void createOperatorPrecedenceMap()
  {
    operatorPrecedence = new LinkedHashMap<>();
    operatorPrecedence.put(OperatorCode.increment, 0);
    operatorPrecedence.put(OperatorCode.decrement, 0);
    operatorPrecedence.put(OperatorCode.equalTo, 6);
    operatorPrecedence.put(OperatorCode.notEqualTo, 6);
    operatorPrecedence.put(OperatorCode.greaterThanEqualTo, 5);
    operatorPrecedence.put(OperatorCode.lessThanEqualTo, 5);
    operatorPrecedence.put(OperatorCode.logicalOr, 11);
    operatorPrecedence.put(OperatorCode.logicalAnd, 10);
    operatorPrecedence.put(OperatorCode.shiftLeft, 4);
    operatorPrecedence.put(OperatorCode.shiftRight, 4);
    operatorPrecedence.put(OperatorCode.ushiftRight, 4);
    operatorPrecedence.put(OperatorCode.add, 3);
    operatorPrecedence.put(OperatorCode.subtract, 3);
    operatorPrecedence.put(OperatorCode.multiply, 2);
    operatorPrecedence.put(OperatorCode.divide, 2);
    operatorPrecedence.put(OperatorCode.modulus, 2);
    operatorPrecedence.put(OperatorCode.logicalNot, 1);
    operatorPrecedence.put(OperatorCode.bitwiseAnd, 7);
    operatorPrecedence.put(OperatorCode.bitwiseOr, 9);
    operatorPrecedence.put(OperatorCode.bitwiseXor, 8);
    operatorPrecedence.put(OperatorCode.lessThan, 5);
    operatorPrecedence.put(OperatorCode.greaterThan, 5);
    operatorPrecedence.put(OperatorCode.bitwiseNot, 1);
  }

  protected void calculateLowestOperatorPrecedence()
  {
    lowestPrecedence = -1;
    for (Integer value : operatorPrecedence.values())
    {
      if (value > lowestPrecedence)
      {
        lowestPrecedence = value;
      }
    }
    lowestPrecedence++;
  }

  public TokenExpression orderByPrecedence(TokenExpressionList tokenExpressionList)
  {
    List<TokenExpression> expressions = tokenExpressionList.getExpressions();
    return orderByPrecedence(expressions);
  }

  protected TokenExpression orderByPrecedence(List<TokenExpression> expressions)
  {
    int lowestPrecedenceIndex = getLowestPrecedenceIndex(expressions);

    if (lowestPrecedenceIndex != -1)
    {
      SplitTokenList splitTokenList = new SplitTokenList(expressions, lowestPrecedenceIndex);
      TokenExpression left;
      if (splitTokenList.left.size() > 1)
      {
        left = orderByPrecedence(splitTokenList.left);
      }
      else if (splitTokenList.left.size() == 1)
      {
        left = splitTokenList.left.get(0);
      }
      else
      {
        throw new SimulatorException("Less than one Left tokens returned.");
      }

      if (left.isList())
      {
        TokenExpressionList tokenExpressionList = (TokenExpressionList) left;
        left = orderByPrecedence(tokenExpressionList.getExpressions());
      }

      TokenExpression right;
      if (splitTokenList.right.size() > 1)
      {
        right = orderByPrecedence(splitTokenList.right);
      }
      else if (splitTokenList.right.size() == 1)
      {
        right = splitTokenList.right.get(0);
      }
      else
      {
        throw new SimulatorException("Less than one Right tokens returned.");
      }

      if (right.isList())
      {
        TokenExpressionList tokenExpressionList = (TokenExpressionList) right;
        right = orderByPrecedence(tokenExpressionList.getExpressions());
      }

      OperandTokenExpression operandTokenExpression = (OperandTokenExpression) expressions.get(lowestPrecedenceIndex);
      return new BinaryTokenExpression(left, operandTokenExpression.getOperand(), right);
    }
    else if (expressions.size() == 1)
    {
      return expressions.get(0);
    }
    else if (expressions.size() > 1)
    {
      return new TokenExpressionList(expressions);
    }
    else
    {
      throw new SimulatorException("Zero expressions in expression list.");
    }
  }

  protected int getLowestPrecedenceIndex(List<TokenExpression> expressions)
  {
    int leastPrecedence = -1;
    int leastPrecedenceIndex = Integer.MAX_VALUE;
    for (int i = expressions.size() - 1; i >= 0; i--)
    {
      TokenExpression tokenExpression = expressions.get(i);
      if (tokenExpression.isOperand())
      {
        OperandTokenExpression operandTokenExpression = (OperandTokenExpression) tokenExpression;
        SixteenHighKeywordCode operand = operandTokenExpression.getOperand();
        OperatorCode operatorCode = codeMap.get(operand);
        int precedence = operatorPrecedence.get(operatorCode);
        if (precedence > leastPrecedence)
        {
          leastPrecedence = precedence;
          leastPrecedenceIndex = i;
        }
      }
    }

    if (leastPrecedenceIndex != Integer.MAX_VALUE)
    {
      return leastPrecedenceIndex;
    }
    else
    {
      return -1;
    }
  }
}

