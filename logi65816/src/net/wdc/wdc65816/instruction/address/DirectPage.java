package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.W65C816;

public class DirectPage
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return cpu.getDirectPage();
  }

  @Override
  public String toString()
  {
    return "D";
  }
}

