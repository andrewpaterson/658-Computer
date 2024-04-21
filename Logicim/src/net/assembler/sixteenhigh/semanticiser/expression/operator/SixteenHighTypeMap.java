package net.assembler.sixteenhigh.semanticiser.expression.operator;

import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;
import net.common.SimulatorException;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode.*;

public class SixteenHighTypeMap
{
  protected Map<SixteenHighKeywordCode, PrimitiveTypeCode> keywordToType;
  protected Map<PrimitiveTypeCode, Map<PrimitiveTypeCode, PrimitiveTypeCode>> autoCast;

  private static SixteenHighTypeMap instance;

  public SixteenHighTypeMap()
  {
    createKeywordToOperatorMap();
    createAutoCastMap();
  }

  private void createAutoCastMap()
  {
    autoCast = new LinkedHashMap<>();
    createAutoCast(int8, int8, int8);
    createAutoCast(int8, uint8, int8);
    createAutoCast(int8, int16, int16);
    createAutoCast(int8, uint16, int16);
    createAutoCast(int8, int32, int32);
    createAutoCast(int8, uint32, int32);
    createAutoCast(int8, int64, int64);
    createAutoCast(int8, uint64, int64);
    createAutoCast(int8, float16, float16);
    createAutoCast(int8, float32, float32);
    createAutoCast(int8, float64, float64);
    createAutoCast(int8, float128, float128);
    createAutoCast(int8, bool, int8);

    createAutoCast(uint8, uint8, uint8);
    createAutoCast(uint8, int16, int16);
    createAutoCast(uint8, uint16, uint16);
    createAutoCast(uint8, int32, int32);
    createAutoCast(uint8, uint32, uint32);
    createAutoCast(uint8, int64, int64);
    createAutoCast(uint8, uint64, uint64);
    createAutoCast(uint8, float16, float16);
    createAutoCast(uint8, float32, float32);
    createAutoCast(uint8, float64, float64);
    createAutoCast(uint8, float128, float128);
    createAutoCast(uint8, bool, uint8);

    createAutoCast(int16, int16, int16);
    createAutoCast(int16, uint16, int16);
    createAutoCast(int16, int32, int32);
    createAutoCast(int16, uint32, int32);
    createAutoCast(int16, int64, int64);
    createAutoCast(int16, uint64, int64);
    createAutoCast(int16, float16, float16);
    createAutoCast(int16, float32, float32);
    createAutoCast(int16, float64, float64);
    createAutoCast(int16, float128, float128);
    createAutoCast(int16, bool, int16);

    createAutoCast(uint16, uint16, uint16);
    createAutoCast(uint16, int32, int32);
    createAutoCast(uint16, uint32, uint32);
    createAutoCast(uint16, int64, int64);
    createAutoCast(uint16, uint64, uint64);
    createAutoCast(uint16, float16, float16);
    createAutoCast(uint16, float32, float32);
    createAutoCast(uint16, float64, float64);
    createAutoCast(uint16, float128, float128);
    createAutoCast(uint16, bool, uint16);

    createAutoCast(int32, int32, int32);
    createAutoCast(int32, uint32, int32);
    createAutoCast(int32, int64, int64);
    createAutoCast(int32, uint64, int64);
    createAutoCast(int32, float16, float16);
    createAutoCast(int32, float32, float32);
    createAutoCast(int32, float64, float64);
    createAutoCast(int32, float128, float128);
    createAutoCast(int32, bool, int32);

    createAutoCast(uint32, uint32, uint32);
    createAutoCast(uint32, int64, int64);
    createAutoCast(uint32, uint64, uint64);
    createAutoCast(uint32, float16, float16);
    createAutoCast(uint32, float32, float32);
    createAutoCast(uint32, float64, float64);
    createAutoCast(uint32, float128, float128);
    createAutoCast(uint32, bool, uint32);

    createAutoCast(int64, int64, int64);
    createAutoCast(int64, uint64, int64);
    createAutoCast(int64, float16, float16);
    createAutoCast(int64, float32, float32);
    createAutoCast(int64, float64, float64);
    createAutoCast(int64, float128, float128);
    createAutoCast(int64, bool, int64);

    createAutoCast(uint64, uint64, uint64);
    createAutoCast(uint64, float16, float16);
    createAutoCast(uint64, float32, float32);
    createAutoCast(uint64, float64, float64);
    createAutoCast(uint64, float128, float128);
    createAutoCast(uint64, bool, uint64);

    createAutoCast(float16, float16, float16);
    createAutoCast(float16, float32, float32);
    createAutoCast(float16, float64, float64);
    createAutoCast(float16, float128, float128);
    createAutoCast(float16, bool, float16);

    createAutoCast(float32, float32, float32);
    createAutoCast(float32, float64, float64);
    createAutoCast(float32, float128, float128);
    createAutoCast(float32, bool, float32);

    createAutoCast(float64, float64, float64);
    createAutoCast(float64, float128, float128);
    createAutoCast(float64, bool, float64);

    createAutoCast(float128, float128, float128);
    createAutoCast(float128, bool, float64);

    createAutoCast(bool, bool, bool);
  }

  public PrimitiveTypeCode getAutoCast(PrimitiveTypeCode left, PrimitiveTypeCode right)
  {
    Map<PrimitiveTypeCode, PrimitiveTypeCode> map = autoCast.get(left);
    if (map == null)
    {
      throw new SimulatorException("No auto cast found for [%s].", left);
    }
    PrimitiveTypeCode code = map.get(right);
    if (map == null)
    {
      throw new SimulatorException("No auto cast found for [%s, %s].", left, right);
    }
    return code;
  }

  private void createAutoCast(PrimitiveTypeCode left, PrimitiveTypeCode right, PrimitiveTypeCode cast)
  {
    _createAutoCast(left, right, cast);
    if (left != right)
    {
      _createAutoCast(right, left, cast);
    }
  }

  private void _createAutoCast(PrimitiveTypeCode left, PrimitiveTypeCode right, PrimitiveTypeCode cast)
  {
    Map<PrimitiveTypeCode, PrimitiveTypeCode> map = autoCast.get(left);
    if (map == null)
    {
      map = new LinkedHashMap<>();
      autoCast.put(left, map);
    }
    PrimitiveTypeCode primitiveTypeCode = map.get(right);
    if (primitiveTypeCode != null)
    {
      throw new SimulatorException("createAutoCast(%s, %s, xx) has already been called.", left, right);
    }
    map.put(right, cast);
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
    keywordToType.put(SixteenHighKeywordCode.int8, int8);
    keywordToType.put(SixteenHighKeywordCode.uint8, uint8);
    keywordToType.put(SixteenHighKeywordCode.int16, int16);
    keywordToType.put(SixteenHighKeywordCode.uint16, uint16);
    keywordToType.put(SixteenHighKeywordCode.int32, int32);
    keywordToType.put(SixteenHighKeywordCode.uint32, uint32);
    keywordToType.put(SixteenHighKeywordCode.int64, int64);
    keywordToType.put(SixteenHighKeywordCode.uint64, uint64);
    keywordToType.put(SixteenHighKeywordCode.float16, float16);
    keywordToType.put(SixteenHighKeywordCode.float32, float32);
    keywordToType.put(SixteenHighKeywordCode.float64, float64);
    keywordToType.put(SixteenHighKeywordCode.float128, float128);
    keywordToType.put(SixteenHighKeywordCode.bool, bool);
  }

  public PrimitiveTypeCode getPrimitiveTypeForKeyword(SixteenHighKeywordCode sixteenHighKeywordCode)
  {
    return keywordToType.get(sixteenHighKeywordCode);
  }
}

