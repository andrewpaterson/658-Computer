package net.assembler.sixteenhigh.semanticiser.expression.operator;

import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;

import java.util.LinkedHashMap;
import java.util.Map;

public class SixteenHighOperatorMap
{
  protected Map<SixteenHighKeywordCode, OperatorCode> keywordToOperator;

  private static SixteenHighOperatorMap instance;

  public SixteenHighOperatorMap()
  {
    createKeywordToOperatorMap();
  }

  public static SixteenHighOperatorMap getInstance()
  {
    if (instance == null)
    {
      instance = new SixteenHighOperatorMap();
    }
    return instance;
  }

  protected void createKeywordToOperatorMap()
  {
    keywordToOperator = new LinkedHashMap<>();
    keywordToOperator.put(SixteenHighKeywordCode.add, OperatorCode.add);
    keywordToOperator.put(SixteenHighKeywordCode.subtract, OperatorCode.subtract);
    keywordToOperator.put(SixteenHighKeywordCode.multiply, OperatorCode.multiply);
    keywordToOperator.put(SixteenHighKeywordCode.divide, OperatorCode.divide);
    keywordToOperator.put(SixteenHighKeywordCode.modulus, OperatorCode.modulus);
    keywordToOperator.put(SixteenHighKeywordCode.shift_left, OperatorCode.shiftLeft);
    keywordToOperator.put(SixteenHighKeywordCode.shift_right, OperatorCode.shiftRight);
    keywordToOperator.put(SixteenHighKeywordCode.ushift_right, OperatorCode.ushiftRight);
    keywordToOperator.put(SixteenHighKeywordCode.and, OperatorCode.bitwiseAnd);
    keywordToOperator.put(SixteenHighKeywordCode.or, OperatorCode.bitwiseOr);
    keywordToOperator.put(SixteenHighKeywordCode.xor, OperatorCode.bitwiseXor);
    keywordToOperator.put(SixteenHighKeywordCode.not, OperatorCode.bitwiseNot);
    keywordToOperator.put(SixteenHighKeywordCode.increment, OperatorCode.increment);
    keywordToOperator.put(SixteenHighKeywordCode.decrement, OperatorCode.decrement);
  }

  public OperatorCode get(SixteenHighKeywordCode sixteenHighKeywordCode)
  {
    return keywordToOperator.get(sixteenHighKeywordCode);
  }
}

