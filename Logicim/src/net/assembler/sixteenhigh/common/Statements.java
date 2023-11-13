package net.assembler.sixteenhigh.common;

import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.*;
import net.assembler.sixteenhigh.tokeniser.statment.directive.AccessModeStatement;
import net.assembler.sixteenhigh.tokeniser.statment.directive.AccessTime;
import net.assembler.sixteenhigh.tokeniser.statment.directive.EndAddress;
import net.assembler.sixteenhigh.tokeniser.statment.directive.StartAddress;
import net.assembler.sixteenhigh.tokeniser.statment.expression.*;
import net.assembler.sixteenhigh.tokeniser.statment.scope.VariableScope;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Statements
{
  protected List<Statement> statements;
  protected String filename;
  protected int statementIndex;

  public Statements(String filename)
  {
    this.filename = filename;
    this.statements = new ArrayList<>();
    this.statementIndex = 0;
  }

  public void addLocalLabel(String name)
  {
    Label label = new Label(this, statementIndex++, name);
    statements.add(label);
  }

  public void addPrimitiveVariable(SixteenHighKeywordCode keyword,
                                   String name,
                                   VariableScope scope,
                                   List<Long> arrayMatrix,
                                   int pointerCount,
                                   BaseExpression initialiserExpression)
  {
    VariableStatement variable = new PrimitiveVariableStatement(this,
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
                                VariableScope scope,
                                List<Long> arrayMatrix,
                                int pointerCount,
                                BaseExpression initialiserExpression)
  {
    VariableStatement variable = new StructVariableStatement(this,
                                                             statementIndex++,
                                                             structIdentifier,
                                                             name,
                                                             scope,
                                                             arrayMatrix,
                                                             pointerCount,
                                                             initialiserExpression);
    statements.add(variable);
  }

  public IfStatement addIf(SixteenHighKeywordCode keyword)
  {
    IfStatement ifStatement = new IfStatement(this, statementIndex++, keyword);
    statements.add(ifStatement);
    return ifStatement;
  }

  public void addRoutine(String name, VariableScope scope)
  {
    RoutineStatement subroutine = new RoutineStatement(this, statementIndex++, name, scope);
    statements.add(subroutine);
  }

  public Statement getLast()
  {
    return statements.get(statements.size() - 1);
  }

  public void addFlow(FlowExpression flowExpression)
  {
    statements.add(new FlowStatement(this, statementIndex++, flowExpression));
  }

  public void addReturn()
  {
    statements.add(new Ret(this, statementIndex++));
  }

  public void addPush(Expression identifier)
  {
    statements.add(new Push(this, statementIndex++, identifier));
  }

  public void addPull(VariableExpression identifier)
  {
    statements.add(new Pull(this, statementIndex++, identifier, new PullExpression()));
  }

  public void addBitCompare(VariableExpression register, SixteenHighKeywordCode keyword)
  {
    statements.add(new BitCompare(this, statementIndex++, register, keyword));
  }

  public void addCrement(VariableExpression register, SixteenHighKeywordCode keyword)
  {
    statements.add(new Crement(this, statementIndex++, register, keyword));
  }

  public void addNumberCompare(VariableExpression leftIdentifier, Expression right, SixteenHighKeywordCode keyword)
  {
    statements.add(new NumberCompare(this, statementIndex++, leftIdentifier, right, keyword));
  }

  public void addAssignment(VariableExpression leftIdentifier, SixteenHighKeywordCode keyword, Expression right)
  {
    statements.add(new Assignment(this, statementIndex++, leftIdentifier, keyword, right));
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
    statements.add(new AccessModeStatement(this, statementIndex, mode));
  }

  public void addAccessTime(int cycles)
  {
    statements.add(new AccessTime(this, statementIndex, cycles));
  }

  public void addEnd()
  {
    statements.add(new End(this, statementIndex));
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
    for (Statement statement : statements)
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
    StructStatement structStatement = new StructStatement(this, statementIndex++, structName);
    statements.add(structStatement);
  }

  public List<Statement> getStatements()
  {
    return statements;
  }

  public String getFilename()
  {
    return filename;
  }
}

