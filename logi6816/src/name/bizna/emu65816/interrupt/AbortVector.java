package name.bizna.emu65816.interrupt;

import name.bizna.emu65816.Cpu65816;

public class ResetVector extends InterruptVector
{
  @Override
  public int getAddress(Cpu65816 cpu)
  {
    return 0;
  }
}
