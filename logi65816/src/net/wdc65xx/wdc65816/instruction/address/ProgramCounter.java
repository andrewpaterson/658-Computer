package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.Cpu65816;

public class ProgramCounter
    extends AddressOffset
{
  public ProgramCounter()
  {
    super();
  }

  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getProgramCounter().getOffset();
  }

  @Override
  public String toString()
  {
    return "PC";
  }
}
