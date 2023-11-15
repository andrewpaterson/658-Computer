package net.assembler.sixteenhigh.semanticiser.directive;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.semanticiser.LogResult;
import net.assembler.sixteenhigh.tokeniser.statment.directive.*;
import net.common.SimulatorException;
import net.common.util.StringUtil;

import static net.assembler.sixteenhigh.semanticiser.LogResult.success;

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

  public void setStartAddress(long startAddress)
  {
    this.startAddress = startAddress;
  }

  public void setEndAddress(long endAddress)
  {
    this.endAddress = endAddress;
  }

  public void setAccessMode(AccessMode accessMode)
  {
    this.accessMode = accessMode;
  }

  public void setAccessTime(int accessTime)
  {
    this.accessTime = accessTime;
  }
}

