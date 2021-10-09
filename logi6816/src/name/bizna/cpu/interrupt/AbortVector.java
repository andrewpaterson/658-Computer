package name.bizna.cpu.interrupt;

import name.bizna.cpu.Cpu65816;

public class AbortVector
    extends InterruptVector
{
  @Override
  public int getAddress(Cpu65816 cpu)
  {
    if (cpu.isEmulation())
    {
      return 0xfff8;
    }
    else
    {
      return 0xffe8;
    }
  }
}

