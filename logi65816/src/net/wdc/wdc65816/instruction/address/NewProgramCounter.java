package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65C816;

public class NewProgramCounter
    extends AddressOffset
{
  @Override
  public int getOffset(WDC65C816 cpu)
  {
    return cpu.getNewProgramCounter().getOffset();
  }

  @Override
  public String toString()
  {
    return "New_PC";
  }
}

