package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65C816;

public class ProgramCounter
    extends AddressOffset
{
  public ProgramCounter()
  {
    super();
  }

  @Override
  public int getOffset(WDC65C816 cpu)
  {
    return cpu.getProgramCounter().getOffset();
  }

  @Override
  public String toString()
  {
    return "PC";
  }
}

