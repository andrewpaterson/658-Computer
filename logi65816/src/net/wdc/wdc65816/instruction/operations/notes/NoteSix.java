package net.wdc.wdc65816.instruction.operations.notes;

import net.wdc.wdc65816.Address;
import net.wdc.wdc65816.WDC65816;
import net.wdc.wdc65816.instruction.operations.Operation;

public class NoteSix
    extends Operation
{
  @Override
  public void execute(WDC65816 cpu)
  {
  }

  @Override
  public boolean mustExecute(WDC65816 cpu)
  {
    if (cpu.isEmulation())
    {
      int pcOffset = cpu.getProgramCounter().getOffset();
      return Address.areOffsetsAreOnDifferentPages(pcOffset, pcOffset + cpu.getData16Bit());
    }
    return false;
  }

  @Override
  public String toString()
  {
    return "Note(6)";
  }
}

