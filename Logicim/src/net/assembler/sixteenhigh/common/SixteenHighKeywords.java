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
  protected String REC = "rec";
  protected String SUB = "sub";
  protected String END = "end";
  protected String IF = "if";
  protected char OPEN_ROUND = '(';
  protected char CLOSE_ROUND = ')';
  protected char OPEN_SQUARE = '[';
  protected char CLOSE_SQUARE = ']';
  protected char ASSIGN = '=';
  protected char DEREFERENCE = '*';
  protected char REFERENCE = '&';
  protected char DOT = '.';
  protected char AT = '@';
  protected char INTER_STATEMENT = ';';
  protected char LABEL = '#';
  protected char ARRAY_SEPARATOR = ',';
  protected String GLOBAL = "@@";

  protected List<KeywordPair> keywords;
  protected Map<SixteenHighKeywordCode, String> codeToStringMap;

  protected List<String> leadingStatementIdentifiers;
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

    leadingStatementIdentifiers = defineLeadingStatementIdentifiers();
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
    keywords.add(new KeywordPair(assign, str(ASSIGN)));
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
    keywords.add(new KeywordPair(if_, "if"));
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
    keywords.add(new KeywordPair(rec, REC));
    keywords.add(new KeywordPair(sub, SUB));
    keywords.add(new KeywordPair(test_set, "ts"));
    keywords.add(new KeywordPair(test_reset, "tr"));
    keywords.add(new KeywordPair(return_, "return"));
    keywords.add(new KeywordPair(end, END));
    keywords.add(new KeywordPair(start_address, "$start_address"));
    keywords.add(new KeywordPair(end_address, "$end_address"));
    keywords.add(new KeywordPair(access_mode, "$access_mode"));
    keywords.add(new KeywordPair(read_only, "read-only"));
    keywords.add(new KeywordPair(write_only, "write-only"));
    keywords.add(new KeywordPair(read_write, "read-write"));
    keywords.add(new KeywordPair(access_time, "$access_time"));
    keywords.add(new KeywordPair(open_round, str(OPEN_ROUND)));
    keywords.add(new KeywordPair(close_round, str(CLOSE_ROUND)));
    keywords.add(new KeywordPair(open_square, str(OPEN_SQUARE)));
    keywords.add(new KeywordPair(close_square, str(CLOSE_SQUARE)));

    return keywords;
  }

  private String str(char c)
  {
    return Character.toString(c);
  }

  protected List<String> defineLeadingStatementIdentifiers()
  {
    List<String> identifiers = new ArrayList<>();
    add(codeToStringMap, identifiers, int8);
    add(codeToStringMap, identifiers, uint8);
    add(codeToStringMap, identifiers, int16);
    add(codeToStringMap, identifiers, uint16);
    add(codeToStringMap, identifiers, int32);
    add(codeToStringMap, identifiers, uint32);
    add(codeToStringMap, identifiers, int64);
    add(codeToStringMap, identifiers, uint64);
    add(codeToStringMap, identifiers, float16);
    add(codeToStringMap, identifiers, float32);
    add(codeToStringMap, identifiers, float64);
    add(codeToStringMap, identifiers, float128);
    add(codeToStringMap, identifiers, bool);
    add(codeToStringMap, identifiers, flag);
    add(codeToStringMap, identifiers, if_);
    add(codeToStringMap, identifiers, return_);
    return identifiers;
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
    add(codeToStringMap, list, test_set);
    add(codeToStringMap, list, test_reset);
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

  public String end()
  {
    return END;
  }

  public String rec()
  {
    return REC;
  }

  public String if_()
  {
    return IF;
  }

  public String sub()
  {
    return SUB;
  }

  public char openRound()
  {
    return OPEN_ROUND;
  }

  public char closeRound()
  {
    return CLOSE_ROUND;
  }

  public char openSquare()
  {
    return OPEN_SQUARE;
  }

  public char closeSquare()
  {
    return CLOSE_SQUARE;
  }

  public char reference()
  {
    return REFERENCE;
  }

  public char dereference()
  {
    return DEREFERENCE;
  }

  public char assign()
  {
    return ASSIGN;
  }

  public char dot()
  {
    return DOT;
  }

  public char at()
  {
    return AT;
  }

  public char interstatement()
  {
    return INTER_STATEMENT;
  }

  public char label()
  {
    return LABEL;
  }

  public String global()
  {
    return GLOBAL;
  }

  public char arraySeparator()
  {
    return ARRAY_SEPARATOR;
  }

  public String unit()
  {
    return Character.toString(AT);
  }

  public List<SixteenHighKeywordCode> getCrements()
  {
    return CollectionUtil.newList(increment,
                                  decrement);
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

  public List<String> getLeadingStatementIdentifiers()
  {
    return leadingStatementIdentifiers;
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

