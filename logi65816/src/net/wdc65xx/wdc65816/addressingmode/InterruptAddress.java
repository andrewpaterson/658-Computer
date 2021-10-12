package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.interrupt.InterruptVector;

public class InterruptAddress
    extends AddressOffset
{
  private final InterruptVector interruptVector;

  public InterruptAddress(InterruptVector interruptVector)
  {
    this.interruptVector = interruptVector;
  }

  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return interruptVector.getAddress(cpu);
  }

  @Override
  public String toString()
  {
    return "VA";
  }
}

