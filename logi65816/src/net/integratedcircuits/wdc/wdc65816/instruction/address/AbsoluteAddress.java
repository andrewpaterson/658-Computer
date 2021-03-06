package net.integratedcircuits.wdc.wdc65816.instruction.address;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class AbsoluteAddress
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return cpu.getAddress().getOffset();
  }

  @Override
  public String toString()
  {
    return "AA";
  }
}

