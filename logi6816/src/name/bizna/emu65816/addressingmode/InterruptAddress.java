package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.interrupt.InterruptVector;

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

