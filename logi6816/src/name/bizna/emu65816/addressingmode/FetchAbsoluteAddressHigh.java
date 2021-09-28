package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class AbsoluteAddressHigh
    extends DataBusCycleOperation
{
  public AbsoluteAddressHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setAddressHigh(cpu.getPinData());
  }
}

