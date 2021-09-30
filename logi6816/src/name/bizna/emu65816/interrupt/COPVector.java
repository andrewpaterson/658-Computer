package name.bizna.emu65816.interrupt;

import name.bizna.emu65816.Cpu65816;

public class COPVector
    extends InterruptVector
{
  @Override
  public int getAddress(Cpu65816 cpu)
  {
    if (cpu.isEmulationMode())
    {
      return 0xfff4;
    }
    else
    {
      return 0xffe4;
    }
  }
}

