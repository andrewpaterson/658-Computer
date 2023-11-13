package net.assembler.sixteenhigh.semanticiser.directive;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.tokeniser.statment.directive.*;
import net.common.SimulatorException;
import net.common.parser.StringZero;
import net.common.util.StringUtil;

public class DirectiveBlock
{
  public long startAddress;
  public long endAddress;
  public AccessMode accessMode;
  public int accessTime;

  public DirectiveBlock()
  {
    startAddress = 0;
    endAddress = Long.MAX_VALUE;
    accessTime = 1;
    accessMode = AccessMode.READ_WRITE;
  }

  public DirectiveBlock(DirectiveBlock directiveBlock)
  {
    startAddress = directiveBlock.startAddress;
    endAddress = directiveBlock.endAddress;
    accessTime = directiveBlock.accessTime;
    accessMode = directiveBlock.accessMode;
  }

  public boolean set(DirectiveStatement directiveStatement, StringZero error, SixteenHighKeywords sixteenHighKeywords)
  {
    if (directiveStatement instanceof AccessModeStatement)
    {
      AccessMode accessMode = ((AccessModeStatement) directiveStatement).getAccessMode();
      if (accessMode == null)
      {
        error.set((directiveStatement).print(sixteenHighKeywords) + " not allowed.");
        return false;
      }
      this.accessMode = accessMode;
      return true;
    }
    else if (directiveStatement instanceof AccessTime)
    {
      int accessTime = ((AccessTime) directiveStatement).getTime();
      if ((accessTime <= 0) || (accessTime > 32000))
      {
        error.set((directiveStatement).print(sixteenHighKeywords) + " out of bounds.  Expected (> 0, <= 32000).");
        return false;
      }
      this.accessTime = accessTime;
      return true;
    }
    else if (directiveStatement instanceof StartAddress)
    {
      int address = ((StartAddress) directiveStatement).getAddress();
      if (address < 0)
      {
        error.set((directiveStatement).print(sixteenHighKeywords) + " out of bounds.  Expected >= 0.");
        return false;
      }
      this.startAddress = address;
      return true;
    }
    else if (directiveStatement instanceof EndAddress)
    {
      int address = ((EndAddress) directiveStatement).getAddress();
      if (address < 0)
      {
        error.set((directiveStatement).print(sixteenHighKeywords) + " out of bounds.  Expected >= 0.");
        return false;
      }
      this.endAddress = address;
      return true;
    }
    else
    {
      throw new SimulatorException(StringUtil.getClassSimpleName(directiveStatement) + " unexpected.");
    }
  }
}

