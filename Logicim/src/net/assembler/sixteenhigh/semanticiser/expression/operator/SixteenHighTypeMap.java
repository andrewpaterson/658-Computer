package net.assembler.sixteenhigh.semanticiser.expression.operator;

import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;

import java.util.LinkedHashMap;
import java.util.Map;

public class SixteenHighTypeMap
{
  protected Map<SixteenHighKeywordCode, PrimitiveTypeCode> keywordToType;

  private static SixteenHighTypeMap instance;

  public SixteenHighTypeMap()
  {
    createKeywordToOperatorMap();
  }

  public static SixteenHighTypeMap getInstance()
  {
    if (instance == null)
    {
      instance = new SixteenHighTypeMap();
    }
    return instance;
  }

  protected void createKeywordToOperatorMap()
  {
    keywordToType = new LinkedHashMap<>();
    keywordToType.put(SixteenHighKeywordCode.int8, PrimitiveTypeCode.int8);
    keywordToType.put(SixteenHighKeywordCode.uint8, PrimitiveTypeCode.uint8);
    keywordToType.put(SixteenHighKeywordCode.int16, PrimitiveTypeCode.int16);
    keywordToType.put(SixteenHighKeywordCode.uint16, PrimitiveTypeCode.uint16);
    keywordToType.put(SixteenHighKeywordCode.int24, PrimitiveTypeCode.int24);
    keywordToType.put(SixteenHighKeywordCode.uint24, PrimitiveTypeCode.uint24);
    keywordToType.put(SixteenHighKeywordCode.int32, PrimitiveTypeCode.int32);
    keywordToType.put(SixteenHighKeywordCode.uint32, PrimitiveTypeCode.uint32);
    keywordToType.put(SixteenHighKeywordCode.int64, PrimitiveTypeCode.int64);
    keywordToType.put(SixteenHighKeywordCode.uint64, PrimitiveTypeCode.uint64);
    keywordToType.put(SixteenHighKeywordCode.float8, PrimitiveTypeCode.float8);
    keywordToType.put(SixteenHighKeywordCode.float16, PrimitiveTypeCode.float16);
    keywordToType.put(SixteenHighKeywordCode.float32, PrimitiveTypeCode.float32);
    keywordToType.put(SixteenHighKeywordCode.float64, PrimitiveTypeCode.float64);
    keywordToType.put(SixteenHighKeywordCode.float128, PrimitiveTypeCode.float128);
  }

  public PrimitiveTypeCode get(SixteenHighKeywordCode sixteenHighKeywordCode)
  {
    return keywordToType.get(sixteenHighKeywordCode);
  }
}

