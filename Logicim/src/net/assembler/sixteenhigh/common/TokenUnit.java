<<<<<<< Updated upstream
package net.assembler.sixteenhigh.common;

import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.*;
import net.assembler.sixteenhigh.tokeniser.statment.directive.AccessModeTokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.directive.AccessTime;
import net.assembler.sixteenhigh.tokeniser.statment.directive.EndAddress;
import net.assembler.sixteenhigh.tokeniser.statment.directive.StartAddress;
import net.assembler.sixteenhigh.tokeniser.statment.expression.*;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TokenUnit
{
  protected List<TokenStatement> statements;
  protected String filename;
  protected int statementIndex;

  public TokenUnit(String filename)
  {
    this.filename = filename;
    this.statements = new ArrayList<>();
    this.statementIndex = 0;
  }

  public void addLocalLabel(String name)
  {
    LabelTokenStatement label = new LabelTokenStatement(this, statementIndex++, name);
    statements.add(label);
  }

  public void addPrimitiveVariable(SixteenHighKeywordCode keyword,
                                   String name,
                                   Scope scope,
                                   List<Long> arrayMatrix,
                                   int pointerCount,
                                   BaseTokenExpression initialiserExpression)
  {
    VariableTokenStatement variable = new PrimitiveVariableTokenStatement(this,
                                                                          statementIndex++,
                                                                          keyword,
                                                                          name,
                                                                          scope,
                                                                          arrayMatrix,
                                                                          pointerCount,
                                                                          initialiserExpression);
    statements.add(variable);
  }

  public void addStructVariable(String structIdentifier,
                                String name,
                                Scope scope,
                                List<Long> arrayMatrix,
                                int pointerCount,
                                BaseTokenExpression initialiserExpression)
  {
    VariableTokenStatement variable = new StructVariableTokenStatement(this,
                                                                       statementIndex++,
                                                                       structIdentifier,
                                                                       name,
                                                                       scope,
                                                                       arrayMatrix,
                                                                       pointerCount,
                                                                       initialiserExpression);
    statements.add(variable);
  }

  public IfTokenStatement addIf(SixteenHighKeywordCode keyword)
  {
    IfTokenStatement ifStatement = new IfTokenStatement(this, statementIndex++, keyword);
    statements.add(ifStatement);
    return ifStatement;
  }

  public void addRoutine(String name, Scope scope)
  {
    RoutineTokenStatement subroutine = new RoutineTokenStatement(this, statementIndex++, name, scope);
    statements.add(subroutine);
  }

  public TokenStatement getLast()
  {
    return statements.get(statements.size() - 1);
  }

  public void addFlow(FlowTokenExpression flowExpression)
  {
    statements.add(new FlowTokenStatement(this, statementIndex++, flowExpression));
  }

  public void addReturn()
  {
    statements.add(new ReturnTokenStatement(this, statementIndex++));
  }

  public void addCrement(VariableTokenExpression register, SixteenHighKeywordCode keyword)
  {
    statements.add(new CrementTokenStatement(this, statementIndex++, register, keyword));
  }

  public void addAssignment(VariableTokenExpression leftExpression, SixteenHighKeywordCode keyword, TokenExpressionList rightExpressions)
  {
    statements.add(new AssignmentTokenStatement(this, statementIndex++, leftExpression, keyword, rightExpressions));
  }

  public void addStartAddress(int address)
  {
    statements.add(new StartAddress(this, statementIndex, address));
  }

  public void addEndAddress(int address)
  {
    statements.add(new EndAddress(this, statementIndex, address));
  }

  public void addAccessMode(SixteenHighKeywordCode mode)
  {
    statements.add(new AccessModeTokenStatement(this, statementIndex, mode));
  }

  public void addAccessTime(int cycles)
  {
    statements.add(new AccessTime(this, statementIndex, cycles));
  }

  public void addEnd()
  {
    statements.add(new EndTokenStatement(this, statementIndex));
  }

  public void dump(SixteenHighKeywords sixteenHighKeywords)
  {
    System.out.println(print(sixteenHighKeywords));
  }

  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder builder = new StringBuilder();
    int lines = 0;
    int depth = 0;
    boolean previousDirectiveStatement = false;
    for (TokenStatement statement : statements)
    {
      if (statement.isEnd())
      {
        depth--;
        if (depth < 0)
        {
          depth = 0;
        }
      }

      if (previousDirectiveStatement && !statement.isDirective())
      {
        builder.append("\n");
        lines++;
      }

      int apparentDepth = depth;
      if (statement.isLabel() && depth > 0)
      {
        apparentDepth--;
      }

      StringBuilder tabs = StringUtil.pad("   ", apparentDepth);
      builder.append(tabs);
      builder.append(statement.print(sixteenHighKeywords));
      builder.append("\n");
      lines++;

      if (statement.isRoutine() || statement.isStruct())
      {
        depth++;
      }
      previousDirectiveStatement = statement.isDirective();
    }

    if (lines == 1)
    {
      builder.delete(builder.length() - 1, builder.length());
    }
    String s = builder.toString();
    if (s.endsWith("\n\n"))
    {
      s = s.substring(0, s.length() - 1);
    }
    return s;
  }

  public void addStruct(String structName)
  {
    StructTokenStatement structStatement = new StructTokenStatement(this, statementIndex++, structName);
    statements.add(structStatement);
  }

  public List<TokenStatement> getStatements()
  {
    return statements;
  }

  public String getFilename()
  {
    return filename;
  }
}

=======
package net.assembler.sixteenhigh.common;

import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.*;
import net.assembler.sixteenhigh.tokeniser.statment.directive.AccessModeTokenStatement;
import net.assembler.sixteenhigh.tokeniser.statment.directive.AccessTime;
import net.assembler.sixteenhigh.tokeniser.statment.directive.EndAddress;
import net.assembler.sixteenhigh.tokeniser.statment.directive.StartAddress;
import net.assembler.sixteenhigh.tokeniser.statment.expression.*;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TokenUnit
{
  protected List<TokenStatement> statements;
  protected String filename;
  protected int statementIndex;

  public TokenUnit(String filename)
  {
    this.filename = filename;
    this.statements = new ArrayList<>();
    this.statementIndex = 0;
  }

  public void addLocalLabel(String name)
  {
    LabelTokenStatement label = new LabelTokenStatement(this, statementIndex++, name);
    statements.add(label);
  }

  public void addPrimitiveVariable(SixteenHighKeywordCode keyword,
                                   String name,
                                   Scope scope,
                                   List<Long> arrayMatrix,
                                   int pointerCount,
                                   BaseTokenExpression initialiserExpression)
  {
    VariableTokenStatement variable = new PrimitiveVariableTokenStatement(this,
                                                                          statementIndex++,
                                                                          keyword,
                                                                          name,
                                                                          scope,
                                                                          arrayMatrix,
                                                                          pointerCount,
                                                                          initialiserExpression);
    statements.add(variable);
  }

  public void addStructVariable(String structIdentifier,
                                String name,
                                Scope scope,
                                List<Long> arrayMatrix,
                                int pointerCount,
                                BaseTokenExpression initialiserExpression)
  {
    VariableTokenStatement variable = new StructVariableTokenStatement(this,
                                                                       statementIndex++,
                                                                       structIdentifier,
                                                                       name,
                                                                       scope,
                                                                       arrayMatrix,
                                                                       pointerCount,
                                                                       initialiserExpression);
    statements.add(variable);
  }

  public IfTokenStatement addIf(SixteenHighKeywordCode keyword)
  {
    IfTokenStatement ifStatement = new IfTokenStatement(this, statementIndex++, keyword);
    statements.add(ifStatement);
    return ifStatement;
  }

  public void addRoutine(String name, Scope scope)
  {
    RoutineTokenStatement subroutine = new RoutineTokenStatement(this, statementIndex++, name, scope);
    statements.add(subroutine);
  }

  public TokenStatement getLast()
  {
    return statements.get(statements.size() - 1);
  }

  public void addFlow(FlowTokenExpression flowExpression)
  {
    statements.add(new FlowTokenStatement(this, statementIndex++, flowExpression));
  }

  public void addReturn()
  {
    statements.add(new ReturnTokenStatement(this, statementIndex++));
  }

  public void addCrement(VariableTokenExpression register, SixteenHighKeywordCode keyword)
  {
    statements.add(new CrementTokenStatement(this, statementIndex++, register, keyword));
  }

  public void addAssignment(VariableTokenExpression leftExpression, SixteenHighKeywordCode keyword, TokenExpressionList rightExpressions)
  {
    statements.add(new AssignmentTokenStatement(this, statementIndex++, leftExpression, keyword, rightExpressions));
  }

  public void addStartAddress(int address)
  {
    statements.add(new StartAddress(this, statementIndex, address));
  }

  public void addEndAddress(int address)
  {
    statements.add(new EndAddress(this, statementIndex, address));
  }

  public void addAccessMode(SixteenHighKeywordCode mode)
  {
    statements.add(new AccessModeTokenStatement(this, statementIndex, mode));
  }

  public void addAccessTime(int cycles)
  {
    statements.add(new AccessTime(this, statementIndex, cycles));
  }

  public void addEnd()
  {
    statements.add(new EndTokenStatement(this, statementIndex));
  }

  public void dump(SixteenHighKeywords sixteenHighKeywords)
  {
    System.out.println(print(sixteenHighKeywords));
  }

  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder builder = new StringBuilder();
    int lines = 0;
    int depth = 0;
    boolean previousDirectiveStatement = false;
    for (TokenStatement statement : statements)
    {
      if (statement.isEnd())
      {
        depth--;
        if (depth < 0)
        {
          depth = 0;
        }
      }

      if (previousDirectiveStatement && !statement.isDirective())
      {
        builder.append("\n");
        lines++;
      }

      int apparentDepth = depth;
      if (statement.isLabel() && depth > 0)
      {
        apparentDepth--;
      }

      StringBuilder tabs = StringUtil.pad("   ", apparentDepth);
      builder.append(tabs);
      builder.append(statement.print(sixteenHighKeywords));
      builder.append("\n");
      lines++;

      if (statement.isRoutine() || statement.isStruct())
      {
        depth++;
      }
      previousDirectiveStatement = statement.isDirective();
    }

    if (lines == 1)
    {
      builder.delete(builder.length() - 1, builder.length());
    }
    String s = builder.toString();
    if (s.endsWith("\n\n"))
    {
      s = s.substring(0, s.length() - 1);
    }
    return s;
  }

  public void addStruct(String structName)
  {
    StructTokenStatement structStatement = new StructTokenStatement(this, statementIndex++, structName);
    statements.add(structStatement);
  }

  public List<TokenStatement> getStatements()
  {
    return statements;
  }

  public String getFilename()
  {
    return filename;
  }
}

>>>>>>> Stashed changes
