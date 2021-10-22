package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65816;

public class ProgramCounter
    extends AddressOffset
{
  public ProgramCounter()
  {
    super();
  }

  @Override
  public int getOffset(WDC65816 cpu)
  {
    return cpu.getProgramCounter().getOffset();
  }

  @Override
  public String toString()
  {
    return "PC";
  }
}

