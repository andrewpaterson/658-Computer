package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class FetchAbsoluteAddressHigh
    extends DataBusCycleOperation
{
  public FetchAbsoluteAddressHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setAddressHigh(cpu.getPinData());
  }
}

