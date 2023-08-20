package net.assembler.sixteenhigh.parser;

import net.common.parser.primitive.IntegerPointer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.assembler.sixteenhigh.parser.SixteenHighKeywordCode.*;

public class SixteenHighKeywords
{
  protected List<KeywordPair> keywords;
  protected List<String> firstIdentifiers;
  protected List<String> secondIdentifiers;

  public SixteenHighKeywords()
  {
    keywords = defineKeywords();
    firstIdentifiers = defineFirstIdentifiers();
    secondIdentifiers = defineSecondIdentifiers();
  }

  protected List<KeywordPair> defineKeywords()
  {
    List<KeywordPair> keywords = new ArrayList<>();
    keywords.add(new KeywordPair(int8, "int8"));
    keywords.add(new KeywordPair(uint8, "uint8"));
    keywords.add(new KeywordPair(int16, "int16"));
    keywords.add(new KeywordPair(uint16, "uint16"));
    keywords.add(new KeywordPair(int24, "int24"));
    keywords.add(new KeywordPair(uint24, "uint24"));
    keywords.add(new KeywordPair(int32, "int32"));
    keywords.add(new KeywordPair(uint32, "uint32"));
    keywords.add(new KeywordPair(int64, "int64"));
    keywords.add(new KeywordPair(uint64, "uint64"));
    keywords.add(new KeywordPair(float8, "float8"));
    keywords.add(new KeywordPair(float16, "float16"));
    keywords.add(new KeywordPair(float32, "float32"));
    keywords.add(new KeywordPair(float64, "float64"));
    keywords.add(new KeywordPair(float128, "float128"));
    keywords.add(new KeywordPair(bool, "bool"));
    keywords.add(new KeywordPair(assign, "="));
    keywords.add(new KeywordPair(add, "+"));
    keywords.add(new KeywordPair(subtract, "-"));
    keywords.add(new KeywordPair(multiply, "*"));
    keywords.add(new KeywordPair(divide, "/"));
    keywords.add(new KeywordPair(modulus, "%"));
    keywords.add(new KeywordPair(shift_left, "<<"));
    keywords.add(new KeywordPair(shift_right, ">>"));
    keywords.add(new KeywordPair(ushift_right, ">>>"));
    keywords.add(new KeywordPair(and, "&"));
    keywords.add(new KeywordPair(or, "|"));
    keywords.add(new KeywordPair(xor, "^"));
    keywords.add(new KeywordPair(not, "~"));
    keywords.add(new KeywordPair(if_greater, "if>"));
    keywords.add(new KeywordPair(if_equals, "if="));
    keywords.add(new KeywordPair(if_less, "if<"));
    keywords.add(new KeywordPair(if_greater_equals, "if>="));
    keywords.add(new KeywordPair(if_less_equals, "if<="));
    keywords.add(new KeywordPair(if_not_equals, "if!="));
    keywords.add(new KeywordPair(add_assign, "+="));
    keywords.add(new KeywordPair(subtract_assign, "-="));
    keywords.add(new KeywordPair(multiply_assign, "*="));
    keywords.add(new KeywordPair(divide_assign, "/="));
    keywords.add(new KeywordPair(modulus_assign, "%="));
    keywords.add(new KeywordPair(shift_left_assign, "<<="));
    keywords.add(new KeywordPair(shift_right_assign, ">>="));
    keywords.add(new KeywordPair(ushift_right_assign, ">>>="));
    keywords.add(new KeywordPair(and_assign, "&="));
    keywords.add(new KeywordPair(or_assign, "|="));
    keywords.add(new KeywordPair(xor_assign, "^="));
    keywords.add(new KeywordPair(not_assign, "~="));
    keywords.add(new KeywordPair(increment, "++"));
    keywords.add(new KeywordPair(decrement, "--"));
    keywords.add(new KeywordPair(go, "go"));
    keywords.add(new KeywordPair(go_sub, "gosub"));
    keywords.add(new KeywordPair(subtract_compare, "?-"));
    keywords.add(new KeywordPair(and_compare, "?&"));
    keywords.add(new KeywordPair(is_true, "?"));
    keywords.add(new KeywordPair(test_set, "ts"));
    keywords.add(new KeywordPair(test_reset, "tr"));
    keywords.add(new KeywordPair(ret, "return"));
    return keywords;
  }

  protected List<String> defineFirstIdentifiers()
  {
    Map<SixteenHighKeywordCode, String> codeToStringMap = getCodeToStringMap();

    List<String> firstIdentifiers = new ArrayList<>();
    firstIdentifiers.add(codeToStringMap.get(int8));
    firstIdentifiers.add(codeToStringMap.get(uint8));
    firstIdentifiers.add(codeToStringMap.get(int16));
    firstIdentifiers.add(codeToStringMap.get(uint16));
    firstIdentifiers.add(codeToStringMap.get(int24));
    firstIdentifiers.add(codeToStringMap.get(uint24));
    firstIdentifiers.add(codeToStringMap.get(int32));
    firstIdentifiers.add(codeToStringMap.get(uint32));
    firstIdentifiers.add(codeToStringMap.get(int64));
    firstIdentifiers.add(codeToStringMap.get(uint64));
    firstIdentifiers.add(codeToStringMap.get(float8));
    firstIdentifiers.add(codeToStringMap.get(float16));
    firstIdentifiers.add(codeToStringMap.get(float32));
    firstIdentifiers.add(codeToStringMap.get(float64));
    firstIdentifiers.add(codeToStringMap.get(float128));
    firstIdentifiers.add(codeToStringMap.get(bool));
    firstIdentifiers.add(codeToStringMap.get(if_equals));
    firstIdentifiers.add(codeToStringMap.get(if_greater));
    firstIdentifiers.add(codeToStringMap.get(if_greater_equals));
    firstIdentifiers.add(codeToStringMap.get(if_less));
    firstIdentifiers.add(codeToStringMap.get(if_less_equals));
    firstIdentifiers.add(codeToStringMap.get(if_not_equals));
    firstIdentifiers.add(codeToStringMap.get(go));
    firstIdentifiers.add(codeToStringMap.get(go_sub));
    firstIdentifiers.add(codeToStringMap.get(ret));
    return firstIdentifiers;
  }

  private List<String> defineSecondIdentifiers()
  {
    Map<SixteenHighKeywordCode, String> codeToStringMap = getCodeToStringMap();

    List<String> secondIdentifiers = new ArrayList<>();
    firstIdentifiers.add(codeToStringMap.get(assign));
    firstIdentifiers.add(codeToStringMap.get(add));
    firstIdentifiers.add(codeToStringMap.get(subtract));
    firstIdentifiers.add(codeToStringMap.get(multiply));
    firstIdentifiers.add(codeToStringMap.get(divide));
    firstIdentifiers.add(codeToStringMap.get(modulus));
    firstIdentifiers.add(codeToStringMap.get(shift_left));
    firstIdentifiers.add(codeToStringMap.get(shift_right));
    firstIdentifiers.add(codeToStringMap.get(ushift_right));
    firstIdentifiers.add(codeToStringMap.get(and));
    firstIdentifiers.add(codeToStringMap.get(or));
    firstIdentifiers.add(codeToStringMap.get(xor));
    firstIdentifiers.add(codeToStringMap.get(not));
    firstIdentifiers.add(codeToStringMap.get(add_assign));
    firstIdentifiers.add(codeToStringMap.get(subtract_assign));
    firstIdentifiers.add(codeToStringMap.get(multiply_assign));
    firstIdentifiers.add(codeToStringMap.get(divide_assign));
    firstIdentifiers.add(codeToStringMap.get(modulus_assign));
    firstIdentifiers.add(codeToStringMap.get(shift_left_assign));
    firstIdentifiers.add(codeToStringMap.get(shift_right_assign));
    firstIdentifiers.add(codeToStringMap.get(ushift_right_assign));
    firstIdentifiers.add(codeToStringMap.get(and_assign));
    firstIdentifiers.add(codeToStringMap.get(or_assign));
    firstIdentifiers.add(codeToStringMap.get(xor_assign));
    firstIdentifiers.add(codeToStringMap.get(not_assign));
    firstIdentifiers.add(codeToStringMap.get(increment));
    firstIdentifiers.add(codeToStringMap.get(decrement));
    firstIdentifiers.add(codeToStringMap.get(go));
    firstIdentifiers.add(codeToStringMap.get(subtract_compare));
    firstIdentifiers.add(codeToStringMap.get(and_compare));
    firstIdentifiers.add(codeToStringMap.get(is_true));
    firstIdentifiers.add(codeToStringMap.get(is_false));
    firstIdentifiers.add(codeToStringMap.get(test_set));
    firstIdentifiers.add(codeToStringMap.get(test_reset));
    return secondIdentifiers;
  }

  public Map<SixteenHighKeywordCode, String> getCodeToStringMap()
  {
    LinkedHashMap<SixteenHighKeywordCode, String> result = new LinkedHashMap<>();
    for (KeywordPair keyword : keywords)
    {
      result.put(keyword.code, keyword.name);
    }
    return result;
  }

  public SixteenHighKeywordCode getKeyword(IntegerPointer index)
  {
    KeywordPair keywordPair = keywords.get(index.value);
    return keywordPair.code;
  }
}

