package name.bizna.emu65816.interrupt;

import name.bizna.emu65816.Cpu65816;

public class NMIVector
    extends InterruptVector
{
  @Override
  public int getAddress(Cpu65816 cpu)
  {
    if (cpu.isEmulationMode())
    {
      return 0xfffa;
    }
    else
    {
      return 0xffea;
    }
  }
}

