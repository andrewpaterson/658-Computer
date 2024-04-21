package net.assembler.sixteenhigh.common;

import net.assembler.sixteenhigh.tokeniser.KeywordPair;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.common.SimulatorException;
import net.common.parser.primitive.IntegerPointer;
import net.common.util.CollectionUtil;
import net.common.util.StringUtil;

import java.util.*;

import static net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode.*;

public class SixteenHighKeywords
{
  protected String GO = "go";
  protected String END = "end";
  protected String STRUCT = "struct";
  protected String OPEN_ROUND = "(";
  protected String CLOSE_ROUND = ")";
  protected String OPEN_SQUARE = "[";
  protected String CLOSE_SQUARE = "]";
  protected String PUSH = ">";
  protected String PULL = "<";
  protected String ASSIGN = "=";

  protected List<KeywordPair> keywords;
  protected Map<SixteenHighKeywordCode, String> codeToStringMap;

  protected List<String> leadingIdentifiers;
  protected List<String> leadingStrings;
  protected List<String> followingIdentifiers;
  protected List<String> followingStrings;
  protected List<String> directiveIdentifiers;
  protected List<String> accessModes;
  protected List<String> unaryStrings;
  protected List<String> binaryString;

  private static SixteenHighKeywords instance;

  public SixteenHighKeywords()
  {
    keywords = defineKeywords();
    codeToStringMap = createCodeToStringMap();

    leadingIdentifiers = defineLeadingIdentifiers();
    leadingStrings = defineLeadingStrings();
    followingIdentifiers = defineFollowingIdentifiers();
    followingStrings = defineFollowingStrings();
    directiveIdentifiers = defineDirectiveIdentifiers();
    accessModes = defineAccessMode();
    unaryStrings = defineUnaryOperators();
    binaryString = defineBinaryOperators();
  }

  public static SixteenHighKeywords getInstance()
  {
    if (instance == null)
    {
      instance = new SixteenHighKeywords();
    }
    return instance;
  }

  protected List<KeywordPair> defineKeywords()
  {
    List<KeywordPair> keywords = new ArrayList<>();
    keywords.add(new KeywordPair(int8, "int8"));
    keywords.add(new KeywordPair(uint8, "uint8"));
    keywords.add(new KeywordPair(int16, "int16"));
    keywords.add(new KeywordPair(uint16, "uint16"));
    keywords.add(new KeywordPair(int32, "int32"));
    keywords.add(new KeywordPair(uint32, "uint32"));
    keywords.add(new KeywordPair(int64, "int64"));
    keywords.add(new KeywordPair(uint64, "uint64"));
    keywords.add(new KeywordPair(float16, "float16"));
    keywords.add(new KeywordPair(float32, "float32"));
    keywords.add(new KeywordPair(float64, "float64"));
    keywords.add(new KeywordPair(float128, "float128"));
    keywords.add(new KeywordPair(bool, "bool"));
    keywords.add(new KeywordPair(assign, ASSIGN));
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
    keywords.add(new KeywordPair(push, PUSH));
    keywords.add(new KeywordPair(pull, PULL));
    keywords.add(new KeywordPair(end, END));
    keywords.add(new KeywordPair(struct, STRUCT));
    keywords.add(new KeywordPair(start_address, "$start_address"));
    keywords.add(new KeywordPair(end_address, "$end_address"));
    keywords.add(new KeywordPair(access_mode, "$access_mode"));
    keywords.add(new KeywordPair(read_only, "read-only"));
    keywords.add(new KeywordPair(write_only, "write-only"));
    keywords.add(new KeywordPair(read_write, "read-write"));
    keywords.add(new KeywordPair(access_time, "$access_time"));
    keywords.add(new KeywordPair(open_round, OPEN_ROUND));
    keywords.add(new KeywordPair(close_round, CLOSE_ROUND));
    keywords.add(new KeywordPair(open_square, OPEN_SQUARE));
    keywords.add(new KeywordPair(close_square, CLOSE_SQUARE));

    return keywords;
  }

  protected List<String> defineLeadingIdentifiers()
  {
    List<String> leadingIdentifiers = new ArrayList<>();
    add(codeToStringMap, leadingIdentifiers, int8);
    add(codeToStringMap, leadingIdentifiers, uint8);
    add(codeToStringMap, leadingIdentifiers, int16);
    add(codeToStringMap, leadingIdentifiers, uint16);
    add(codeToStringMap, leadingIdentifiers, int32);
    add(codeToStringMap, leadingIdentifiers, uint32);
    add(codeToStringMap, leadingIdentifiers, int64);
    add(codeToStringMap, leadingIdentifiers, uint64);
    add(codeToStringMap, leadingIdentifiers, float16);
    add(codeToStringMap, leadingIdentifiers, float32);
    add(codeToStringMap, leadingIdentifiers, float64);
    add(codeToStringMap, leadingIdentifiers, float128);
    add(codeToStringMap, leadingIdentifiers, bool);
    add(codeToStringMap, leadingIdentifiers, go);
    add(codeToStringMap, leadingIdentifiers, gosub);
    add(codeToStringMap, leadingIdentifiers, ret);
    return leadingIdentifiers;
  }

  protected List<String> defineLeadingStrings()
  {
    List<String> leadingStrings = new ArrayList<>();
    add(codeToStringMap, leadingStrings, if_equals);
    add(codeToStringMap, leadingStrings, if_greater);
    add(codeToStringMap, leadingStrings, if_greater_equals);
    add(codeToStringMap, leadingStrings, if_less);
    add(codeToStringMap, leadingStrings, if_less_equals);
    add(codeToStringMap, leadingStrings, if_not_equals);
    add(codeToStringMap, leadingStrings, push);
    sortLongestToShortest(leadingStrings);
    return leadingStrings;
  }

  private void sortLongestToShortest(List<String> leadingStrings)
  {
    leadingStrings.sort(new Comparator<>()
    {
      @Override
      public int compare(String o1, String o2)
      {
        if (o2.length() > o1.length())
        {
          return 1;
        }
        if (o2.length() < o1.length())
        {
          return -1;
        }
        return o1.compareTo(o2);
      }
    });
  }

  private List<String> defineFollowingIdentifiers()
  {
    List<String> list = new ArrayList<>();
    add(codeToStringMap, list, go);
    add(codeToStringMap, list, test_set);
    add(codeToStringMap, list, test_reset);
    return list;
  }

  private List<String> defineFollowingStrings()
  {
    List<String> list = new ArrayList<>();
    add(codeToStringMap, list, assign);
    add(codeToStringMap, list, add_assign);
    add(codeToStringMap, list, subtract_assign);
    add(codeToStringMap, list, multiply_assign);
    add(codeToStringMap, list, divide_assign);
    add(codeToStringMap, list, modulus_assign);
    add(codeToStringMap, list, shift_left_assign);
    add(codeToStringMap, list, shift_right_assign);
    add(codeToStringMap, list, ushift_right_assign);
    add(codeToStringMap, list, and_assign);
    add(codeToStringMap, list, or_assign);
    add(codeToStringMap, list, xor_assign);
    add(codeToStringMap, list, not_assign);
    add(codeToStringMap, list, increment);
    add(codeToStringMap, list, decrement);
    add(codeToStringMap, list, subtract_compare);
    add(codeToStringMap, list, and_compare);
    add(codeToStringMap, list, is_true);
    add(codeToStringMap, list, is_false);
    add(codeToStringMap, list, pull);
    sortLongestToShortest(list);
    return list;
  }

  private List<String> defineDirectiveIdentifiers()
  {
    List<String> list = new ArrayList<>();
    add(codeToStringMap, list, start_address);
    add(codeToStringMap, list, end_address);
    add(codeToStringMap, list, access_mode);
    add(codeToStringMap, list, read_only);
    add(codeToStringMap, list, write_only);
    add(codeToStringMap, list, read_write);
    add(codeToStringMap, list, access_time);
    return list;
  }

  private List<String> defineAccessMode()
  {
    List<String> list = new ArrayList<>();
    add(codeToStringMap, list, read_only);
    add(codeToStringMap, list, write_only);
    add(codeToStringMap, list, read_write);
    return list;
  }

  private List<String> defineUnaryOperators()
  {
    List<String> list = new ArrayList<>();
    add(codeToStringMap, list, add);
    add(codeToStringMap, list, subtract);
    add(codeToStringMap, list, not);
    add(codeToStringMap, list, shift_left);
    add(codeToStringMap, list, shift_right);
    add(codeToStringMap, list, ushift_right);
    sortLongestToShortest(list);
    return list;
  }

  private List<String> defineBinaryOperators()
  {
    List<String> list = new ArrayList<>();
    add(codeToStringMap, list, add);
    add(codeToStringMap, list, subtract);
    add(codeToStringMap, list, multiply);
    add(codeToStringMap, list, divide);
    add(codeToStringMap, list, modulus);
    add(codeToStringMap, list, and);
    add(codeToStringMap, list, or);
    add(codeToStringMap, list, xor);
    sortLongestToShortest(list);
    return list;
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

  public LinkedHashMap<SixteenHighKeywordCode, String> createCodeToStringMap()
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

  public String struct()
  {
    return STRUCT;
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

  public List<SixteenHighKeywordCode> getPrimitiveTypes()
  {
    return CollectionUtil.newList(int8,
                                  uint8,
                                  int16,
                                  uint16,
                                  int32,
                                  uint32,
                                  int64,
                                  uint64,
                                  float16,
                                  float32,
                                  float64,
                                  float128,
                                  bool);
  }

  public List<String> getLeadingIdentifiers()
  {
    return leadingIdentifiers;
  }

  public List<String> getLeadingStrings()
  {
    return leadingStrings;
  }

  public List<String> getFollowingIdentifiers()
  {
    return followingIdentifiers;
  }

  public List<String> getFollowingStrings()
  {
    return followingStrings;
  }

  public List<String> getDirectiveIdentifiers()
  {
    return directiveIdentifiers;
  }

  public List<String> getAccessModes()
  {
    return accessModes;
  }

  public List<String> getUnaryStrings()
  {
    return unaryStrings;
  }

  public List<String> getBinaryString()
  {
    return binaryString;
  }
}

