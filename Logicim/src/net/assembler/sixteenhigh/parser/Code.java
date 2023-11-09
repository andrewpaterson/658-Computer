package net.assembler.sixteenhigh.parser;

import net.assembler.sixteenhigh.parser.statment.*;
import net.assembler.sixteenhigh.parser.statment.directive.AccessMode;
import net.assembler.sixteenhigh.parser.statment.directive.AccessTime;
import net.assembler.sixteenhigh.parser.statment.directive.EndAddress;
import net.assembler.sixteenhigh.parser.statment.directive.StartAddress;
import net.assembler.sixteenhigh.parser.statment.expression.BaseExpression;
import net.assembler.sixteenhigh.parser.statment.expression.Expression;
import net.assembler.sixteenhigh.parser.statment.expression.PullExpression;
import net.assembler.sixteenhigh.parser.statment.expression.RegisterExpression;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Code
{
  protected List<Statement> statements;
  protected List<Label> labels;
  protected List<FileVariable> fileVariables;
  protected String filename;
  protected int statementIndex;
  protected List<Routine> routines;
  protected Routine currentRoutine;

  public Code(String filename)
  {
    this.filename = filename;
    this.statements = new ArrayList<>();
    this.statementIndex = 0;
    this.labels = new ArrayList<>();
    this.fileVariables = new ArrayList<>();
    this.routines = new ArrayList<>();
    this.currentRoutine = null;
  }

  public void addLocalLabel(String name)
  {
    Label label = new Label(this, statementIndex++, name);
    statements.add(label);
    labels.add(label);
  }

  public void addLocalVariable(SixteenHighKeywordCode keyword,
                               String name,
                               List<Long> arrayMatrix,
                               int pointerCount,
                               BaseExpression initialiserExpression)
  {
    LocalVariable variable = new LocalVariable(this,
                                               statementIndex++,
                                               keyword,
                                               name,
                                               arrayMatrix,
                                               pointerCount,
                                               initialiserExpression);
    statements.add(variable);
    if (currentRoutine != null)
    {
      currentRoutine.addLocalVariable(variable);
    }
  }

  public void addFileVariable(SixteenHighKeywordCode keyword,
                              String name,
                              List<Long> arrayMatrix,
                              int pointerCount,
                              BaseExpression initialiserExpression)
  {
    FileVariable variable = new FileVariable(this,
                                             statementIndex++,
                                             keyword,
                                             name,
                                             arrayMatrix,
                                             pointerCount,
                                             initialiserExpression);
    statements.add(variable);
    fileVariables.add(variable);
  }

  public GlobalVariable addGlobalVariable(SixteenHighKeywordCode keyword,
                                          String name,
                                          List<Long> arrayMatrix,
                                          int pointerCount,
                                          BaseExpression initialiserExpression)
  {
    GlobalVariable variable = new GlobalVariable(this,
                                                 statementIndex++,
                                                 keyword,
                                                 name,
                                                 arrayMatrix,
                                                 pointerCount,
                                                 initialiserExpression);
    statements.add(variable);
    return variable;
  }

  public If addIf(SixteenHighKeywordCode keyword)
  {
    If anIf = new If(this, statementIndex++, keyword);
    statements.add(anIf);
    return anIf;
  }

  public void addLocalSubroutine(String name)
  {
    LocalSubroutine subroutine = new LocalSubroutine(this, statementIndex++, name);
    statements.add(subroutine);
    routines.add(subroutine);
    currentRoutine = subroutine;
  }

  public GlobalSubroutine addGlobalSubroutine(String name)
  {
    GlobalSubroutine subroutine = new GlobalSubroutine(this, statementIndex++, name);
    statements.add(subroutine);
    currentRoutine = subroutine;
    return subroutine;
  }

  public MainRoutine addMainRoutine(String name)
  {
    MainRoutine routine = new MainRoutine(this, statementIndex++, name);
    statements.add(routine);
    currentRoutine = routine;
    return routine;
  }

  public Statement getLast()
  {
    return statements.get(statements.size() - 1);
  }

  public Statement popLast()
  {
    Statement last = getLast();
    statements.remove(statements.size() - 1);
    return last;
  }

  public void addGo(String label)
  {
    statements.add(new Go(this, statementIndex++, label));
  }

  public void addGosub(String label)
  {
    statements.add(new Gosub(this, statementIndex++, label));
  }

  public void addReturn()
  {
    statements.add(new Ret(this, statementIndex++));
  }

  public void addPush(Expression identifier)
  {
    statements.add(new Push(this, statementIndex++, identifier));
  }

  public void addPull(RegisterExpression identifier)
  {
    statements.add(new Pull(this, statementIndex++, identifier, new PullExpression()));
  }

  public void addBitCompare(RegisterExpression register, SixteenHighKeywordCode keyword)
  {
    statements.add(new BitCompare(this, statementIndex++, register, keyword));
  }

  public void addCrement(RegisterExpression register, SixteenHighKeywordCode keyword)
  {
    statements.add(new Crement(this, statementIndex++, register, keyword));
  }

  public void addNumberCompare(RegisterExpression leftIdentifier, Expression right, SixteenHighKeywordCode keyword)
  {
    statements.add(new NumberCompare(this, statementIndex++, leftIdentifier, right, keyword));
  }

  public void addAssignment(RegisterExpression leftIdentifier, SixteenHighKeywordCode keyword, Expression right)
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
    statements.add(new AccessMode(this, statementIndex, mode));
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

  protected String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder builder = new StringBuilder();
    int lines = 0;
    int depth = 0;
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

      if (statement.isRoutine())
      {
        depth++;
      }
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
}

