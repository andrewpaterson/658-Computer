package net.assembler.sixteenhigh.parser;

import net.common.SimulatorException;
import net.common.parser.primitive.IntegerPointer;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.assembler.sixteenhigh.parser.SixteenHighKeywordCode.*;

public class SixteenHighKeywords
{
  protected String GO = "go";

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
    keywords.add(new KeywordPair(go, GO));
    keywords.add(new KeywordPair(gosub, "gosub"));
    keywords.add(new KeywordPair(subtract_compare, "?-"));
    keywords.add(new KeywordPair(and_compare, "?&"));
    keywords.add(new KeywordPair(is_true, "?"));
    keywords.add(new KeywordPair(is_false, "?!"));
    keywords.add(new KeywordPair(test_set, "ts"));
    keywords.add(new KeywordPair(test_reset, "tr"));
    keywords.add(new KeywordPair(ret, "return"));
    keywords.add(new KeywordPair(push, "push"));
    keywords.add(new KeywordPair(pull, "pull"));
    return keywords;
  }

  protected List<String> defineFirstIdentifiers()
  {
    Map<SixteenHighKeywordCode, String> codeToStringMap = getCodeToStringMap();

    List<String> firstIdentifiers = new ArrayList<>();
    add(codeToStringMap, firstIdentifiers, int8);
    add(codeToStringMap, firstIdentifiers, uint8);
    add(codeToStringMap, firstIdentifiers, int16);
    add(codeToStringMap, firstIdentifiers, uint16);
    add(codeToStringMap, firstIdentifiers, int24);
    add(codeToStringMap, firstIdentifiers, uint24);
    add(codeToStringMap, firstIdentifiers, int32);
    add(codeToStringMap, firstIdentifiers, uint32);
    add(codeToStringMap, firstIdentifiers, int64);
    add(codeToStringMap, firstIdentifiers, uint64);
    add(codeToStringMap, firstIdentifiers, float8);
    add(codeToStringMap, firstIdentifiers, float16);
    add(codeToStringMap, firstIdentifiers, float32);
    add(codeToStringMap, firstIdentifiers, float64);
    add(codeToStringMap, firstIdentifiers, float128);
    add(codeToStringMap, firstIdentifiers, bool);
    add(codeToStringMap, firstIdentifiers, if_equals);
    add(codeToStringMap, firstIdentifiers, if_greater);
    add(codeToStringMap, firstIdentifiers, if_greater_equals);
    add(codeToStringMap, firstIdentifiers, if_less);
    add(codeToStringMap, firstIdentifiers, if_less_equals);
    add(codeToStringMap, firstIdentifiers, if_not_equals);
    add(codeToStringMap, firstIdentifiers, go);
    add(codeToStringMap, firstIdentifiers, gosub);
    add(codeToStringMap, firstIdentifiers, pull);
    add(codeToStringMap, firstIdentifiers, push);
    add(codeToStringMap, firstIdentifiers, ret);
    return firstIdentifiers;
  }

  private List<String> defineSecondIdentifiers()
  {
    Map<SixteenHighKeywordCode, String> codeToStringMap = getCodeToStringMap();

    List<String> secondIdentifiers = new ArrayList<>();
    add(codeToStringMap, firstIdentifiers, assign);
    add(codeToStringMap, firstIdentifiers, add);
    add(codeToStringMap, firstIdentifiers, subtract);
    add(codeToStringMap, firstIdentifiers, multiply);
    add(codeToStringMap, firstIdentifiers, divide);
    add(codeToStringMap, firstIdentifiers, modulus);
    add(codeToStringMap, firstIdentifiers, shift_left);
    add(codeToStringMap, firstIdentifiers, shift_right);
    add(codeToStringMap, firstIdentifiers, ushift_right);
    add(codeToStringMap, firstIdentifiers, and);
    add(codeToStringMap, firstIdentifiers, or);
    add(codeToStringMap, firstIdentifiers, xor);
    add(codeToStringMap, firstIdentifiers, not);
    add(codeToStringMap, firstIdentifiers, add_assign);
    add(codeToStringMap, firstIdentifiers, subtract_assign);
    add(codeToStringMap, firstIdentifiers, multiply_assign);
    add(codeToStringMap, firstIdentifiers, divide_assign);
    add(codeToStringMap, firstIdentifiers, modulus_assign);
    add(codeToStringMap, firstIdentifiers, shift_left_assign);
    add(codeToStringMap, firstIdentifiers, shift_right_assign);
    add(codeToStringMap, firstIdentifiers, ushift_right_assign);
    add(codeToStringMap, firstIdentifiers, and_assign);
    add(codeToStringMap, firstIdentifiers, or_assign);
    add(codeToStringMap, firstIdentifiers, xor_assign);
    add(codeToStringMap, firstIdentifiers, not_assign);
    add(codeToStringMap, firstIdentifiers, increment);
    add(codeToStringMap, firstIdentifiers, decrement);
    add(codeToStringMap, firstIdentifiers, go);
    add(codeToStringMap, firstIdentifiers, subtract_compare);
    add(codeToStringMap, firstIdentifiers, and_compare);
    add(codeToStringMap, firstIdentifiers, is_true);
    add(codeToStringMap, firstIdentifiers, is_false);
    add(codeToStringMap, firstIdentifiers, test_set);
    add(codeToStringMap, firstIdentifiers, test_reset);
    return secondIdentifiers;
  }

  private boolean add(Map<SixteenHighKeywordCode, String> codeToStringMap, List<String> identifiers, SixteenHighKeywordCode keywordCode)
  {
    String name = codeToStringMap.get(keywordCode);
    if (name != null)
    {
      return identifiers.add(name);
    }
    else
    {
      throw new SimulatorException("Cannot get name for keyword code [%s].", StringUtil.toEnumString(keywordCode));
    }
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

  public SixteenHighKeywordCode getKeyword(String name)
  {
    for (KeywordPair keyword : keywords)
    {
      if (keyword.name.equals(name))
      {
        return keyword.code;
      }
    }
    return null;
  }

  public String go()
  {
    return GO;
  }
}


