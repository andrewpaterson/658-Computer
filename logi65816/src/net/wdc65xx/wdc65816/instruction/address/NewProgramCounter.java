package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.Cpu65816;

public class NewProgramCounter
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getNewProgramCounter().getOffset();
  }

  @Override
  public String toString()
  {
    return "New_PC";
  }
}

