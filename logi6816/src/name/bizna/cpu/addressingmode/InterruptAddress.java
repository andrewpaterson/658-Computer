package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.interrupt.InterruptVector;

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

