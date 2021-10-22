package net.wdc.wdc65816.instruction.interrupt;

import net.wdc.wdc65816.WDC65C816;

public abstract class InterruptVector
{
  public abstract int getAddress(WDC65C816 cpu);
}
