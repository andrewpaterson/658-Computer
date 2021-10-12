package net.wdc65xx.wdc65816.interrupt;

import net.wdc65xx.wdc65816.Cpu65816;

public class ResetVector
    extends InterruptVector
{
  @Override
  public int getAddress(Cpu65816 cpu)
  {
    return 0xfffc;
  }
}

