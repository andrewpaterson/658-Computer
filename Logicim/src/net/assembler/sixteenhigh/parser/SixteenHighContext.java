package net.assembler.sixteenhigh.parser;

import net.assembler.sixteenhigh.parser.statment.GlobalSubroutine;
import net.assembler.sixteenhigh.parser.statment.GlobalVariable;
import net.assembler.sixteenhigh.parser.statment.MainRoutine;

import java.util.ArrayList;
import java.util.List;

public class SixteenHighContext
{
  protected Globals globals;
  protected List<Code> codeList;
  protected List<GlobalVariable> globalVariables;
  protected List<GlobalSubroutine> globalSubroutines;
  protected List<MainRoutine> mainRoutines;

  public SixteenHighContext()
  {
    globals = new Globals();
    codeList = new ArrayList<>();
    globalVariables = new ArrayList<>();
    globalSubroutines = new ArrayList<>();
    mainRoutines = new ArrayList<>();
  }

  public Code addCode(String filename)
  {
    Code code = new Code(filename);
    codeList.add(code);
    return code;
  }

  public void addGlobalVariable(GlobalVariable globalVariable)
  {
    globalVariables.add(globalVariable);
  }

  public void addMainRoutine(MainRoutine mainRoutine)
  {
    mainRoutines.add(mainRoutine);
  }

  public void addGlobalSubroutine(GlobalSubroutine globalSubroutine)
  {
    globalSubroutines.add(globalSubroutine);
  }
}

