package net.assembler.sixteenhigh.parser;

import net.assembler.sixteenhigh.parser.statment.GlobalVariable;

import java.util.ArrayList;
import java.util.List;

public class SixteenHighContext
{
  protected Globals globals;
  protected List<Code> codeList;
  protected List<GlobalVariable> globalVariables;

  public SixteenHighContext()
  {
    globals = new Globals();
    codeList = new ArrayList<>();
    globalVariables = new ArrayList<>();
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
}

