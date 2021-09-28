package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toByte;

public class FetchDataHigh
    extends DataBusCycleOperation
{
  public FetchDataHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public boolean shouldSkipCycle(Cpu65816 cpu)
  {
    return cpu.isAccumulator8Bit() && cpu.isIndex8Bit();
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setDataHigh(cpu.getPinData());
  }
}

