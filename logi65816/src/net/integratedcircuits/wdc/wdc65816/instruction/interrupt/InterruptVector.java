package net.integratedcircuits.wdc.wdc65816.instruction.interrupt;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public abstract class InterruptVector
{
  public abstract int getAddress(W65C816 cpu);
}
