package net.assembler.sixteenhigh.parser;

import net.assembler.sixteenhigh.parser.literal.CTLiteral;
import net.assembler.sixteenhigh.parser.statment.*;
import net.assembler.sixteenhigh.parser.statment.directive.AccessMode;
import net.assembler.sixteenhigh.parser.statment.directive.AccessTime;
import net.assembler.sixteenhigh.parser.statment.directive.EndAddress;
import net.assembler.sixteenhigh.parser.statment.directive.StartAddress;

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

  public void addLocalVariable(SixteenHighKeywordCode keyword, String name)
  {
    LocalVariable variable = new LocalVariable(this, statementIndex++, keyword, name);
    statements.add(variable);
    currentRoutine.addLocalVariable(variable);
  }

  public void addFileVariable(SixteenHighKeywordCode keyword, String name)
  {
    FileVariable variable = new FileVariable(this, statementIndex++, keyword, name);
    statements.add(variable);
    fileVariables.add(variable);
  }

  public GlobalVariable addGlobalVariable(SixteenHighKeywordCode keyword, String name)
  {
    GlobalVariable variable = new GlobalVariable(this, statementIndex++, keyword, name);
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

  public void addPush(String register)
  {
    statements.add(new Push(this, statementIndex++, register));
  }

  public void addPull(String register)
  {
    statements.add(new Pull(this, statementIndex++, register));
  }

  public void addBitCompare(String register, SixteenHighKeywordCode keyword)
  {
    statements.add(new BitCompare(this, statementIndex++, register, keyword));
  }

  public void addNumberCompare(String leftIdentifier, String rightIdentifier, SixteenHighKeywordCode keyword)
  {
    statements.add(new NumberCompare(this, statementIndex++, leftIdentifier, rightIdentifier, keyword));
  }

  public void addAssignmentOperator(String leftIdentifier, String rightIdentifier, SixteenHighKeywordCode keyword)
  {
    statements.add(new AssignmentFromIdentifier(this, statementIndex++, leftIdentifier, rightIdentifier, keyword));
  }

  public void addAssignmentOperator(String leftIdentifier, CTLiteral rightLiteral, SixteenHighKeywordCode keyword)
  {
    statements.add(new AssignmentFromLiteral(this, statementIndex++, leftIdentifier, rightLiteral, keyword));
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
}

