package name.bizna.emu65816.interrupt;

import name.bizna.emu65816.Cpu65816;

public abstract class InterruptVector
{
  public abstract int getAddress(Cpu65816 cpu);
}
