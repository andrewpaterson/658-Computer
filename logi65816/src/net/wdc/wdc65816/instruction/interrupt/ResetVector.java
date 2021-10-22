package net.wdc.wdc65816.instruction.interrupt;

import net.wdc.wdc65816.WDC65C816;

public class ResetVector
    extends InterruptVector
{
  @Override
  public int getAddress(WDC65C816 cpu)
  {
    return 0xfffc;
  }
}

