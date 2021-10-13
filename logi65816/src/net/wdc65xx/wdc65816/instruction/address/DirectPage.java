package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.Cpu65816;

public class DirectPage
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getDirectPage();
  }

  @Override
  public String toString()
  {
    return "D";
  }
}

