package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.W65C816;

public class StackPointer
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return cpu.getStackPointer();
  }

  @Override
  public String toString()
  {
    return "S";
  }
}

