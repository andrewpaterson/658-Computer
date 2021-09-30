package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class InterruptVector
    extends AddressOffset
{
  private int interruptVector;

  public InterruptVector(int interruptVector)
  {
    this.interruptVector = interruptVector;
  }

  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return interruptVector;
  }
}

