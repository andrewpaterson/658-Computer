package net.assembler.sixteenhigh.parser;

import net.common.SimulatorException;
import net.common.parser.primitive.IntegerPointer;
import net.common.util.CollectionUtil;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.assembler.sixteenhigh.parser.SixteenHighKeywordCode.*;

public class SixteenHighKeywords
{
  protected String GO = "go";
  protected String END = "end";
  protected String OPEN_ROUND = "(";
  protected String CLOSE_ROUND = ")";
  protected String OPEN_SQUARE = "[";
  protected String CLOSE_SQUARE = "]";
  protected String PUSH = ">";
  protected String PULL = "<";
  protected String ASSIGN = "=";

  protected List<KeywordPair> keywords;
  protected List<String> leadingIdentifiers;
  protected List<String> followingIdentifiers;
  protected List<String> secondFollowingIdentifiers;
  protected List<String> directiveIdentifiers;
  protected List<String> accessModes;
  protected List<String> unaryOperators;
  protected List<String> binaryOperators;

  public SixteenHighKeywords()
  {
    keywords = defineKeywords();
    leadingIdentifiers = defineLeadingIdentifiers();
    followingIdentifiers = defineFollowingIdentifiers();
    secondFollowingIdentifiers = defineSecondFollowingIdentifiers();
    directiveIdentifiers = defineDirectiveIdentifiers();
    accessModes = defineAccessMode();
    unaryOperators = defineUnaryOperators();
    binaryOperators = defineBinaryOperators();
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
    keywords.add(new KeywordPair(assign, ASSIGN));
    keywords.add(new KeywordPair(add, "+"));        //?
    keywords.add(new KeywordPair(subtract, "-"));   //?
    keywords.add(new KeywordPair(multiply, "*"));   //?
    keywords.add(new KeywordPair(divide, "/"));     //?
    keywords.add(new KeywordPair(modulus, "%"));    //?
    keywords.add(new KeywordPair(shift_left, "<<"));
    keywords.add(new KeywordPair(shift_right, ">>"));
    keywords.add(new KeywordPair(ushift_right, ">>>"));
    keywords.add(new KeywordPair(and, "&"));        //?
    keywords.add(new KeywordPair(or, "|"));         //?
    keywords.add(new KeywordPair(xor, "^"));        //?
    keywords.add(new KeywordPair(not, "~"));        //?
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
    keywords.add(new KeywordPair(push, PUSH));
    keywords.add(new KeywordPair(pull, PULL));
    keywords.add(new KeywordPair(end, END));
    keywords.add(new KeywordPair(start_address, "$start_address"));
    keywords.add(new KeywordPair(end_address, "$end_address"));
    keywords.add(new KeywordPair(access_mode, "$access_mode"));
    keywords.add(new KeywordPair(read_only, "read-only"));
    keywords.add(new KeywordPair(write_only, "write-only"));
    keywords.add(new KeywordPair(read_write, "read_write"));
    keywords.add(new KeywordPair(access_time, "$access_time"));
    keywords.add(new KeywordPair(open_round, OPEN_ROUND));
    keywords.add(new KeywordPair(close_round, CLOSE_ROUND));
    keywords.add(new KeywordPair(open_square, OPEN_SQUARE));
    keywords.add(new KeywordPair(close_square, CLOSE_SQUARE));
    return keywords;
  }

  protected List<String> defineLeadingIdentifiers()
  {
    Map<SixteenHighKeywordCode, String> codeToStringMap = getCodeToStringMap();

    List<String> leadingIdentifiers = new ArrayList<>();
    add(codeToStringMap, leadingIdentifiers, int8);
    add(codeToStringMap, leadingIdentifiers, uint8);
    add(codeToStringMap, leadingIdentifiers, int16);
    add(codeToStringMap, leadingIdentifiers, uint16);
    add(codeToStringMap, leadingIdentifiers, int24);
    add(codeToStringMap, leadingIdentifiers, uint24);
    add(codeToStringMap, leadingIdentifiers, int32);
    add(codeToStringMap, leadingIdentifiers, uint32);
    add(codeToStringMap, leadingIdentifiers, int64);
    add(codeToStringMap, leadingIdentifiers, uint64);
    add(codeToStringMap, leadingIdentifiers, float8);
    add(codeToStringMap, leadingIdentifiers, float16);
    add(codeToStringMap, leadingIdentifiers, float32);
    add(codeToStringMap, leadingIdentifiers, float64);
    add(codeToStringMap, leadingIdentifiers, float128);
    add(codeToStringMap, leadingIdentifiers, bool);
    add(codeToStringMap, leadingIdentifiers, if_equals);
    add(codeToStringMap, leadingIdentifiers, if_greater);
    add(codeToStringMap, leadingIdentifiers, if_greater_equals);
    add(codeToStringMap, leadingIdentifiers, if_less);
    add(codeToStringMap, leadingIdentifiers, if_less_equals);
    add(codeToStringMap, leadingIdentifiers, if_not_equals);
    add(codeToStringMap, leadingIdentifiers, go);
    add(codeToStringMap, leadingIdentifiers, gosub);
    add(codeToStringMap, leadingIdentifiers, push);
    add(codeToStringMap, leadingIdentifiers, ret);
    return leadingIdentifiers;
  }

  private List<String> defineFollowingIdentifiers()
  {
    Map<SixteenHighKeywordCode, String> codeToStringMap = getCodeToStringMap();

    List<String> followingIdentifiers = new ArrayList<>();
    add(codeToStringMap, followingIdentifiers, assign);
    add(codeToStringMap, followingIdentifiers, add_assign);
    add(codeToStringMap, followingIdentifiers, subtract_assign);
    add(codeToStringMap, followingIdentifiers, multiply_assign);
    add(codeToStringMap, followingIdentifiers, divide_assign);
    add(codeToStringMap, followingIdentifiers, modulus_assign);
    add(codeToStringMap, followingIdentifiers, shift_left_assign);
    add(codeToStringMap, followingIdentifiers, shift_right_assign);
    add(codeToStringMap, followingIdentifiers, ushift_right_assign);
    add(codeToStringMap, followingIdentifiers, and_assign);
    add(codeToStringMap, followingIdentifiers, or_assign);
    add(codeToStringMap, followingIdentifiers, xor_assign);
    add(codeToStringMap, followingIdentifiers, not_assign);
    add(codeToStringMap, followingIdentifiers, increment);
    add(codeToStringMap, followingIdentifiers, decrement);
    add(codeToStringMap, followingIdentifiers, go);
    add(codeToStringMap, followingIdentifiers, subtract_compare);
    add(codeToStringMap, followingIdentifiers, and_compare);
    add(codeToStringMap, followingIdentifiers, is_true);
    add(codeToStringMap, followingIdentifiers, is_false);
    add(codeToStringMap, followingIdentifiers, test_set);
    add(codeToStringMap, followingIdentifiers, test_reset);
    add(codeToStringMap, followingIdentifiers, pull);
    add(codeToStringMap, followingIdentifiers, open_square);
    return followingIdentifiers;
  }

  private List<String> defineSecondFollowingIdentifiers()
  {
    Map<SixteenHighKeywordCode, String> codeToStringMap = getCodeToStringMap();

    List<String> thirdIdentifiers = new ArrayList<>();
    add(codeToStringMap, thirdIdentifiers, add);
    add(codeToStringMap, thirdIdentifiers, subtract);
    add(codeToStringMap, thirdIdentifiers, multiply);
    add(codeToStringMap, thirdIdentifiers, divide);
    add(codeToStringMap, thirdIdentifiers, modulus);
    add(codeToStringMap, thirdIdentifiers, shift_left);
    add(codeToStringMap, thirdIdentifiers, shift_right);
    add(codeToStringMap, thirdIdentifiers, ushift_right);
    add(codeToStringMap, thirdIdentifiers, and);
    add(codeToStringMap, thirdIdentifiers, or);
    add(codeToStringMap, thirdIdentifiers, xor);
    add(codeToStringMap, thirdIdentifiers, not);
    return thirdIdentifiers;
  }

  private List<String> defineDirectiveIdentifiers()
  {
    Map<SixteenHighKeywordCode, String> codeToStringMap = getCodeToStringMap();

    List<String> directiveIdentifiers = new ArrayList<>();
    add(codeToStringMap, directiveIdentifiers, start_address);
    add(codeToStringMap, directiveIdentifiers, end_address);
    add(codeToStringMap, directiveIdentifiers, access_mode);
    add(codeToStringMap, directiveIdentifiers, read_only);
    add(codeToStringMap, directiveIdentifiers, write_only);
    add(codeToStringMap, directiveIdentifiers, read_write);
    add(codeToStringMap, directiveIdentifiers, access_time);
    return directiveIdentifiers;
  }

  private List<String> defineAccessMode()
  {
    Map<SixteenHighKeywordCode, String> codeToStringMap = getCodeToStringMap();

    List<String> accessMode = new ArrayList<>();
    add(codeToStringMap, accessMode, read_only);
    add(codeToStringMap, accessMode, write_only);
    add(codeToStringMap, accessMode, read_write);
    return accessMode;
  }

  private List<String> defineUnaryOperators()
  {
    Map<SixteenHighKeywordCode, String> codeToStringMap = getCodeToStringMap();

    List<String> plusMinus = new ArrayList<>();
    add(codeToStringMap, plusMinus, add);
    add(codeToStringMap, plusMinus, subtract);
    add(codeToStringMap, plusMinus, not);
    return plusMinus;
  }

  private List<String> defineBinaryOperators()
  {
    Map<SixteenHighKeywordCode, String> codeToStringMap = getCodeToStringMap();

    List<String> plusMinus = new ArrayList<>();
    add(codeToStringMap, plusMinus, add);
    add(codeToStringMap, plusMinus, subtract);
    add(codeToStringMap, plusMinus, multiply);
    add(codeToStringMap, plusMinus, divide);
    add(codeToStringMap, plusMinus, modulus);
    add(codeToStringMap, plusMinus, shift_left);
    add(codeToStringMap, plusMinus, shift_right);
    add(codeToStringMap, plusMinus, ushift_right);
    add(codeToStringMap, plusMinus, and);
    add(codeToStringMap, plusMinus, or);
    add(codeToStringMap, plusMinus, xor);
    return plusMinus;
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

  public SixteenHighKeywordCode getKeyword(List<String> allowedIdentifiers, IntegerPointer index)
  {
    if ((index.value >= 0) && (index.value < allowedIdentifiers.size()))
    {
      String keywordString = allowedIdentifiers.get(index.value);
      return getKeyword(keywordString);
    }
    else
    {
      return null;
    }
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

  public String getKeyword(SixteenHighKeywordCode keywordCode)
  {
    for (KeywordPair keyword : keywords)
    {
      if (keyword.code.equals(keywordCode))
      {
        return keyword.name;
      }
    }
    return "";
  }

  public String go()
  {
    return GO;
  }

  public String end()
  {
    return END;
  }

  public String openRound()
  {
    return OPEN_ROUND;
  }

  public String closeRound()
  {
    return CLOSE_ROUND;
  }

  public String openSquare()
  {
    return OPEN_SQUARE;
  }

  public String closeSquare()
  {
    return CLOSE_SQUARE;
  }

  public String push()
  {
    return PUSH;
  }

  public String pull()
  {
    return PULL;
  }

  public String assign()
  {
    return ASSIGN;
  }

  public List<SixteenHighKeywordCode> getBitCompares()
  {
    return CollectionUtil.newList(is_true,
                                  is_false);
  }

  public List<SixteenHighKeywordCode> getCrements()
  {
    return CollectionUtil.newList(increment,
                                  decrement);
  }

  public List<SixteenHighKeywordCode> getNumberCompares()
  {
    return CollectionUtil.newList(subtract_compare,
                                  and_compare);
  }

  public List<SixteenHighKeywordCode> getAssignments()
  {
    return CollectionUtil.newList(assign,
                                  add_assign,
                                  subtract_assign,
                                  multiply_assign,
                                  divide_assign,
                                  modulus_assign,
                                  shift_left_assign,
                                  shift_right_assign,
                                  ushift_right_assign,
                                  and_assign,
                                  or_assign,
                                  xor_assign,
                                  not_assign);
  }

  public List<SixteenHighKeywordCode> getIfs()
  {
    return CollectionUtil.newList(if_greater,
                                  if_equals,
                                  if_less,
                                  if_greater_equals,
                                  if_less_equals,
                                  if_not_equals);
  }

  public List<SixteenHighKeywordCode> getRegisterTypes()
  {
    return CollectionUtil.newList(int8,
                                  uint8,
                                  int16,
                                  uint16,
                                  int24,
                                  uint24,
                                  int32,
                                  uint32,
                                  int64,
                                  uint64,
                                  float8,
                                  float16,
                                  float32,
                                  float64,
                                  float128,
                                  bool);
  }
}

