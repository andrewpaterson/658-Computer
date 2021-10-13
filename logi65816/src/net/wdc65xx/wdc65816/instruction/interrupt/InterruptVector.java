package net.wdc65xx.wdc65816.instruction.interrupt;

import net.wdc65xx.wdc65816.Cpu65816;

public abstract class InterruptVector
{
  public abstract int getAddress(Cpu65816 cpu);
}
