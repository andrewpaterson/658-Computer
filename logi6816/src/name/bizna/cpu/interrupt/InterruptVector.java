package name.bizna.cpu.interrupt;

import name.bizna.cpu.Cpu65816;

public abstract class InterruptVector
{
  public abstract int getAddress(Cpu65816 cpu);
}
