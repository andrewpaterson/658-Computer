package name.bizna.cpu.interrupt;

import name.bizna.cpu.Cpu65816;

public class ResetVector
    extends InterruptVector
{
  @Override
  public int getAddress(Cpu65816 cpu)
  {
    return 0xfffc;
  }
}

