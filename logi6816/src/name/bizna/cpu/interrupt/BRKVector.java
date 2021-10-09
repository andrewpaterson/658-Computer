package name.bizna.cpu.interrupt;

import name.bizna.cpu.Cpu65816;

public class BRKVector
    extends InterruptVector
{
  @Override
  public int getAddress(Cpu65816 cpu)
  {
    if (cpu.isEmulation())
    {
      return 0xfffe;
    }
    else
    {
      return 0xffe6;
    }
  }
}

