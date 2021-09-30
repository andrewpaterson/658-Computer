package name.bizna.emu65816.interrupt;

import name.bizna.emu65816.Cpu65816;

public class BRKVector
    extends InterruptVector
{
  @Override
  public int getAddress(Cpu65816 cpu)
  {
    if (cpu.isEmulationMode())
    {
      return 0xfffe;
    }
    else
    {
      return 0xffe6;
    }
  }
}

