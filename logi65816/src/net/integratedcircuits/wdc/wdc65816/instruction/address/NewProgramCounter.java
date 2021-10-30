package net.integratedcircuits.wdc.wdc65816.instruction.address;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class NewProgramCounter
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return cpu.getNewProgramCounter().getOffset();
  }

  @Override
  public String toString()
  {
    return "New_PC";
  }
}

